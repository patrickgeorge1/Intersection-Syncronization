package com.apd.tema2.intersections;

import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.atomic.AtomicInteger;

public class PriorityIntersection implements Intersection  {
	private int millisecondsToWait;
	private final AtomicInteger numOfPriorityCardInIntersection = new AtomicInteger(0);
	private static final Object lock = new Object();

	@Override
	public void carWait(Car car) {
		try {

			// Reach
			Thread.sleep(car.getWaitingTime());
			if (car.getPriority() == 1) {
				System.out.println("Car " + car.getId() + " with low priority is trying to enter the intersection...");
			}

			// Enter
			if (car.getPriority() == 1) {
				// no priority cars in intersection
				if (numOfPriorityCardInIntersection.get() == 0) {
					System.out.println("Car " + car.getId() + " with low priority has entered the intersection");
				} else {
					// wait untill intersection is free
					synchronized (PriorityIntersection.lock) {
						PriorityIntersection.lock.wait();
						System.out.println("Car " + car.getId() + " with low priority has entered the intersection");
					}
				}
			} else {
				System.out.println("Car " + car.getId() + " with high priority has entered the intersection");
				numOfPriorityCardInIntersection.addAndGet(1);
				Thread.sleep(2000);
			}

			// Exit
			if (car.getPriority() != 1) {
				System.out.println("Car " + car.getId() + " with high priority has exited the intersection");
				int value = numOfPriorityCardInIntersection.addAndGet(-1);
				if (value == 0){
					synchronized (PriorityIntersection.lock) {
						PriorityIntersection.lock.notifyAll();
					}
				}
			}


		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public int getMillisecondsToWait() {
		return millisecondsToWait;
	}

	public void setMillisecondsToWait(int millisecondsToWait) {
		this.millisecondsToWait = millisecondsToWait;
	}
}
