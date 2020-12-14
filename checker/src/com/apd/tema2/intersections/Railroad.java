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
		enter(car);


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
		}

		exit(car);
	}



	/**
	 * print the railroad enter message
	 */
	public void enter(Car car) {
		if (car.getStartDirection() == 0) {
			synchronized (direction0) {
				System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() + " has stopped by the railroad");
				direction0.addLast(car);
			}
		} else {
			synchronized (direction1) {
				System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() + " has stopped by the railroad");
				direction1.addLast(car);
			}
		}
	}

	/**
	 * print the railroad exit message
	 */
	public void exit(Car car) {
		Car leavingCar;
		if (car.getStartDirection() == 0) {
			synchronized (direction0) {
				leavingCar = direction0.removeFirst();
				System.out.println("Car " + leavingCar.getId() + " from side number " + leavingCar.getStartDirection() + " has started driving");
			}
		} else {
			synchronized (direction1) {
				leavingCar = direction1.removeFirst();
				System.out.println("Car " + leavingCar.getId() + " from side number " + leavingCar.getStartDirection() + " has started driving");
			}
		}
	}
}
