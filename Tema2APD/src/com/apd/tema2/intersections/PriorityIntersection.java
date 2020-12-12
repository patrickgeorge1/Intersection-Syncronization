package com.apd.tema2.intersections;

import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class PriorityIntersection implements Intersection  {
	private int millisecondsToWait;
	private final AtomicInteger numOfPriorityCardInIntersection = new AtomicInteger(0);
	private final LinkedBlockingDeque<Car> lowPriorityCars = new LinkedBlockingDeque<>();
	private static final Object lock = new Object();

	@Override
	public void carWait(Car car) {
		try {
			// Reach
			tryToEnter(car);

			// Enter
			if (car.hasLowPriority()) {
				if (noPriorityCarsInIntersection()) enterIntersection(car);
				else addLowPriorityCarToQueue(car);
			} else {
				enterIntersection(car);
				intersectionIncreasePriorityCars();
				Thread.sleep(2000);
			}

			// Exit
			if (car.hasHighPriority()) {
				exitIntersection(car);

				// last high priority car => low priority queued cars will pass
				if (intersectionDecreasePriorityCars() == 0) intersectionPassLowPriorityQueue();
			}
			else exitIntersection(car);
		} catch (InterruptedException e) { e.printStackTrace(); }
	}

	public int getMillisecondsToWait() {
		return millisecondsToWait;
	}

	public void setMillisecondsToWait(int millisecondsToWait) {
		this.millisecondsToWait = millisecondsToWait;
	}

	/**
	 * add low priority car to the queue
	 */
	public void addLowPriorityCarToQueue(Car car) {
		synchronized (lowPriorityCars) {
			lowPriorityCars.addLast(car);
		}
	}

	/**
	 * check if there are priority cars in intersections
	 */
	public boolean noPriorityCarsInIntersection() {
		return (numOfPriorityCardInIntersection.get() == 0);
	}

	/**
	 *  output attempting message
	 */
	public void tryToEnter(Car car) {
		if (car.hasLowPriority())
			System.out.println("Car " + car.getId() + " with low priority is trying to enter the intersection...");
	}

	/**
	 * output proper message depending on car priority type
	 */
	public void enterIntersection(Car car) {
		if (car.hasLowPriority())
			System.out.println("Car " + car.getId() + " with low priority has entered the intersection");
		else
			System.out.println("Car " + car.getId() + " with high priority has entered the intersection");
	}

	/**
	 * output  message
	 */
	public void exitIntersection(Car car) {
		if (car.hasHighPriority())
			System.out.println("Car " + car.getId() + " with high priority has exited the intersection");
	}

	/**
	 * increment number of priority cars from intersection and get
	 */
	public int intersectionIncreasePriorityCars() {
		return numOfPriorityCardInIntersection.addAndGet(1);
	}

	/**
	 * decrement number of priority cars from intersection and get
	 */
	public int intersectionDecreasePriorityCars() {
		return numOfPriorityCardInIntersection.addAndGet(-1);
	}

	/**
	 * enter all low priority waiting cars in intersection
	 */
	public void intersectionPassLowPriorityQueue() {
		synchronized (lowPriorityCars) {
			while (!lowPriorityCars.isEmpty()) {
				Car lowCar = lowPriorityCars.poll();

				// print, considering that car traversed the intersection
				enterIntersection(lowCar);
				exitIntersection(lowCar);
			}
		}
	}
}
