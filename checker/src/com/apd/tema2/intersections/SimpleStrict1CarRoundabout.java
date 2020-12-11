package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleStrict1CarRoundabout implements Intersection {
	private int millisecondsToWait;
	private int numberOfLanes;
	private Semaphore[] lanesSemaphore;

	@Override
	public void carWait(Car car) {
		System.out.println("Car " + car.getId() + " has reached the roundabout");
		try {
			lanesSemaphore[car.getStartDirection()].acquire();
			System.out.println("Car " + car.getId() + " has entered the roundabout from lane " + car.getStartDirection());
			Thread.sleep(millisecondsToWait);
			System.out.println("Car " + car.getId() + " has exited the roundabout after " + (millisecondsToWait / 1000) + " seconds");
			lanesSemaphore[car.getStartDirection()].release();
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

	public int getNumberOfLanes() {
		return numberOfLanes;
	}

	public void setNumberOfLanes(int numberOfLanes) {
		this.numberOfLanes = numberOfLanes;

		lanesSemaphore = new Semaphore[numberOfLanes + 1];
		for (int i = 0; i < numberOfLanes + 1; i++) {
			lanesSemaphore[i] = new Semaphore(1);
		}
	}
}
