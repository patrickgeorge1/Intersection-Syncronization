package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SimpleSemaphore implements Intersection {
	private final CyclicBarrier barrier;

	public SimpleSemaphore(){
		barrier = new CyclicBarrier(Main.carsNo);
	}

	@Override
	public void carWait(Car car) {
		System.out.println("Car " + car.getId() + " has reached the semaphore, now waiting...");
		try {
			barrier.await();
			Thread.sleep(car.getWaitingTime());
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
		System.out.println("Car "+ car.getId() +" has waited enough, now driving...");
	}
}
