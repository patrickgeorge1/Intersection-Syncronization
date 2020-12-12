package com.apd.tema2.intersections;

import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
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
					synchronized (car) {
						synchronized (lowPriorityCars) {
							lowPriorityCars.addLast(car);
						}
						car.wait();
					}
					// nothing will execute next, because print is made in last high priority car
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

				// last high priority car must let other cars enter the intersection
				// in their arrival chronological order
				if (value == 0){
					synchronized (lowPriorityCars) {
						while (!lowPriorityCars.isEmpty()) {
							Car lowCar = lowPriorityCars.poll();
							// let car enter the execution
							synchronized (lowCar) {
								// print here, because some low priority cars will not respect the order otherwise
								System.out.println("Car " + lowCar.getId() + " with low priority has entered the intersection");
								lowCar.notify();
							}
						}
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
