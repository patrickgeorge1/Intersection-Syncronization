package com.apd.tema2.intersections;

import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

public class SimpleMaintenance implements Intersection {
	private int maxCarsToPass;
	private int priorityDirection = 0;
	private Semaphore semaphore0;
	private Semaphore semaphore1;
	private CyclicBarrier barrier;

	private final LinkedBlockingDeque<Car> direction0 = new LinkedBlockingDeque<>();
	private final LinkedBlockingDeque<Car> direction1 = new LinkedBlockingDeque<>();


	@Override
	public void carWait(Car car) {
		// Reach
		reach(car);
		addToQueue(car);

		// Wait untill can enter
		tryToPass(car);
	}

	public void setMaxCarsToPass(int X) {
		maxCarsToPass = X;
		semaphore0 = new Semaphore(X);
		semaphore1 = new Semaphore(0);
		barrier = new CyclicBarrier(X);
	}

	/**
	 * swap the lane, letting X car from the opposite direction to pass
	 */
	public void change_direction() {
		if (priorityDirection == 0) {
			priorityDirection = 1;
			//System.out.println("CHANGE -> now priority is " + priorityDirection);
			semaphore1.release(maxCarsToPass);
		} else {
			priorityDirection = 0;
			//System.out.println("CHANGE -> now priority is " + priorityDirection);
			semaphore0.release(maxCarsToPass);
		}

	}

	/**
	 * print the reach message
	 */
	public void reach(Car car) {
		System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() + " has reached the bottleneck");
	}

	/**
	 * print the enter message
	 */
	public void enter(Car car) {
		System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() + " has passed the bottleneck");
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
	 * trying to pass the semaphore or wait
	 */
	public void tryToPass(Car car) {
		Semaphore s = (car.getStartDirection() == 0) ? semaphore0 : semaphore1;

		try {
			s.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Enter
		Car carToEnter = removeFromQueue(car);
		enter(carToEnter);

		try {
			barrier.await();
		} catch (InterruptedException | BrokenBarrierException e) { e.printStackTrace(); }

		// let other to pass
		synchronized (this) {
			if(s.availablePermits() == 0 && priorityDirection == car.getStartDirection()) {
				change_direction();
			}
		}
	}
}
