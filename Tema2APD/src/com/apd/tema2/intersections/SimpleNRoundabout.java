package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleNRoundabout implements Intersection {
	private final CyclicBarrier barrier;
	private  Semaphore semaphore;
	private int millisecondsToWait;
	private int maxCarInRoundAbout;

	public SimpleNRoundabout() {
		barrier = new CyclicBarrier(Main.carsNo);
	}


	@Override
	public void carWait(Car car) {
		System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");
		try {
			barrier.await();
			// Thread.sleep(car.getWaitingTime());
			semaphore.acquire();
			System.out.println("Car " + car.getId() + " has entered the roundabout");
			Thread.sleep(millisecondsToWait);
			System.out.println("Car " + car.getId() + " has exited the roundabout after 2 seconds");
			semaphore.release();


		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
	}

	public CyclicBarrier getBarrier() {
		return barrier;
	}

	public int getMillisecondsToWait() {
		return millisecondsToWait;
	}

	public void setMillisecondsToWait(int millisecondsToWait) {
		this.millisecondsToWait = millisecondsToWait;
	}

	public int getMaxCarInRoundAbout() {
		return maxCarInRoundAbout;
	}

	public void setMaxCarInRoundAbout(int maxCarInRoundAbout) {
		this.maxCarInRoundAbout = maxCarInRoundAbout;
	}

	public Semaphore getSemaphore() {
		return semaphore;
	}

	public void setSemaphore(int maxCar) {
		this.semaphore = new Semaphore(maxCar);
	}
}
