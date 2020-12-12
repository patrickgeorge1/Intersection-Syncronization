package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;
import java.util.concurrent.ConcurrentHashMap;


public class Crosswalk implements Intersection {
	private final ConcurrentHashMap<Integer, SemaphoreColor> carsSemaphoreColor = new ConcurrentHashMap<>();

	@Override
	public  void carWait(Car car) {
		while (!Main.pedestrians.isFinished()) {
			// get previous color
			SemaphoreColor color = carsSemaphoreColor.getOrDefault(car.getId(), new SemaphoreColor());

			// try to pass and display message only on color update
			if (Main.pedestrians.isPass()) {
				if (color.setRed()) System.out.println("Car " + car.getId() + " has now red light");
			} else {
				if (color.setGreen()) System.out.println("Car " + car.getId() + " has now green light");
			}

			// update car message
			carsSemaphoreColor.put(car.getId(), color);
		}
	}
}

class SemaphoreColor {
	Integer type;

	SemaphoreColor() { type = 0; }   // not set

	// return true if I managed to change something from the previous type
	public boolean setRed() { int oldType = type; type = 1; return (oldType != type); }
	public boolean setGreen() { int oldType = type; type = 2; return (oldType != type); }
}
