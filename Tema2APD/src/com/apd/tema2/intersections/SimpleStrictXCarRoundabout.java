package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleStrictXCarRoundabout implements Intersection {
	private int maxCarsPerLane;
	private int numberOfLanes;
	private int millisecondsToWait;
	private final CyclicBarrier barrierToReach;
	private CyclicBarrier barrierBeforeNextRound;
	private Semaphore[] lanesSemaphore;


	public SimpleStrictXCarRoundabout() {
		barrierToReach = new CyclicBarrier(Main.carsNo);
	}

	@Override
	public void carWait(Car car) {
		System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");
		try {
			// Reach
			barrierToReach.await();

			// Select
			lanesSemaphore[car.getStartDirection()].acquire();
			System.out.println("Car " + car.getId() + " was selected to enter the roundabout from lane " + car.getStartDirection());
			barrierBeforeNextRound.await();

			// Enter
			System.out.println("Car " + car.getId() + " has entered the roundabout from lane " + car.getStartDirection());
			Thread.sleep(millisecondsToWait);
			barrierBeforeNextRound.await();

			// Exit
			System.out.println("Car " + car.getId() + " has exited the roundabout after " + (millisecondsToWait / 1000) + " seconds");
			barrierBeforeNextRound.await();
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

	public void setNumberOfLanes(int numberOfLanes) {
		this.numberOfLanes = numberOfLanes;

		lanesSemaphore = new Semaphore[numberOfLanes + 1];
		for (int i = 0; i < numberOfLanes + 1; i++) {
			lanesSemaphore[i] = new Semaphore(maxCarsPerLane);
		}
	}

	public int getMaxCarsPerLane() {
		return maxCarsPerLane;
	}

	public void setMaxCarsPerLane(int maxCarsPerLane) {
		this.maxCarsPerLane = maxCarsPerLane;
	}

	public void initBarrierBeforeNextRound() {
		barrierBeforeNextRound = new CyclicBarrier(numberOfLanes * maxCarsPerLane);
	}
}
