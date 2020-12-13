package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleStrict1CarRoundabout implements Intersection {
	private int millisecondsToWait;
	private int numberOfLanes;
	private Semaphore[] lanesSemaphore;
	private CyclicBarrier barrier;

	@Override
	public void carWait(Car car) {
		try {
			// Reach
			System.out.println("Car " + car.getId() + " has reached the roundabout");


			// Enter
			lanesSemaphore[car.getStartDirection()].acquire();
			System.out.println("Car " + car.getId() + " has entered the roundabout from lane " + car.getStartDirection());
			Thread.sleep(millisecondsToWait);
			barrier.await();


			// Exit
			System.out.println("Car " + car.getId() + " has exited the roundabout after " + (millisecondsToWait / 1000) + " seconds");
			barrier.await();
			lanesSemaphore[car.getStartDirection()].release();


		} catch (InterruptedException | BrokenBarrierException e) {
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

	/**
	 * set number of lanes and init the semaphores and the barrier
	 */
	public void setNumberOfLanes(int numberOfLanes) {
		this.numberOfLanes = numberOfLanes;

		lanesSemaphore = new Semaphore[numberOfLanes + 1];
		for (int i = 0; i < numberOfLanes + 1; i++) {
			lanesSemaphore[i] = new Semaphore(1);
		}

		barrier = new CyclicBarrier(numberOfLanes);
	}
}
