package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingDeque;

public class Railroad implements Intersection {
	private final LinkedBlockingDeque<Car> direction0 = new LinkedBlockingDeque<>();
	private final LinkedBlockingDeque<Car> direction1 = new LinkedBlockingDeque<>();
	private final CyclicBarrier barrier;
	private boolean trainPassed = false;

	public Railroad() {
		barrier = new CyclicBarrier(Main.carsNo);
	}

	@Override
	public void carWait(Car car) {
		// enter
		synchronized (this) {
			enter(car);
			addToQueue(car);
		}


		// wait for the train
		try {
			barrier.await();
		} catch (InterruptedException | BrokenBarrierException e) { e.printStackTrace(); }

		// exit after the train passed away
		synchronized (this) {
			if (!trainPassed) {
				trainPassed = true;
				System.out.println("The train has passed, cars can now proceed");
			}

			Car leavingCar = removeFromQueue(car);
			exit(leavingCar);
		}
	}

	/**
	 *  add car to its direction queue
	 */
	public void addToQueue(Car car) {
		if (car.getStartDirection()  == 0) {
			direction0.addLast(car);
		} else {
			direction1.addLast(car);
		}
	}

	/**
	 *  remove car from its direction queue
	 */
	public Car removeFromQueue(Car car) {
		if (car.getStartDirection()  == 0) {
			return direction0.removeFirst();
		} else {
			return direction1.removeFirst();
		}
	}


	/**
	 * print the railroad enter message
	 */
	public void enter(Car car) {
		System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() + " has stopped by the railroad");
	}

	/**
	 * print the railroad exit message
	 */
	public void exit(Car car) {
		System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() + " has started driving");
	}
}
