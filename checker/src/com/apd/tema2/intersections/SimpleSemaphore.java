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
		try {
			// Reach
			System.out.println("Car " + car.getId() + " has reached the semaphore, now waiting...");
			barrier.await();
			Thread.sleep(car.getWaitingTime());

			// Exit
			System.out.println("Car "+ car.getId() +" has waited enough, now driving...");

		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
	}
}
