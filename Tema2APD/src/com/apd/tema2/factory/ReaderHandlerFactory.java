package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.entities.ReaderHandler;
import com.apd.tema2.intersections.*;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Returneaza sub forma unor clase anonime implementari pentru metoda de citire din fisier.
 */
public class ReaderHandlerFactory {

    public static ReaderHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of them)
        // road in maintenance - 1 lane 2 ways, X cars at a time
        // road in maintenance - N lanes 2 ways, X cars at a time
        // railroad blockage for T seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) {
                    // Exemplu de utilizare:
                     Main.intersection = IntersectionFactory.getIntersection("simple_semaphore");
                }
            };
            case "simple_n_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    // To parse input line use:
                    Main.intersection = IntersectionFactory.getIntersection("simple_n_roundabout");
                    SimpleNRoundabout s = (SimpleNRoundabout) Main.intersection;

                    String[] line = br.readLine().split(" ");
                    int maxCar = Integer.parseInt(line[0]);
                    int time = Integer.parseInt(line[1]);

                    s.setMillisecondsToWait(time);
                    s.setMaxCarInRoundAbout(maxCar);
                    s.setSemaphore(maxCar);
                }
            };
            case "simple_strict_1_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("simple_strict_1_car_roundabout");
                    SimpleStrict1CarRoundabout s = (SimpleStrict1CarRoundabout) Main.intersection;

                    String[] line = br.readLine().split(" ");
                    int maxLanes = Integer.parseInt(line[0]);
                    int time = Integer.parseInt(line[1]);

                    s.setMillisecondsToWait(time);
                    s.setNumberOfLanes(maxLanes);

                }
            };
            case "simple_strict_x_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("simple_strict_x_car_roundabout");
                    SimpleStrictXCarRoundabout s = (SimpleStrictXCarRoundabout) Main.intersection;

                    String[] line = br.readLine().split(" ");
                    int maxLanes = Integer.parseInt(line[0]);
                    int time = Integer.parseInt(line[1]);
                    int maxCarPerLane = Integer.parseInt(line[2]);

                    s.setMillisecondsToWait(time);
                    s.setMaxCarsPerLane(maxCarPerLane);
                    s.setNumberOfLanes(maxLanes);
                    s.initBarrierBeforeNextRound();
                }
            };
            case "simple_max_x_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("simple_max_x_car_roundabout");
                    SimpleMaxXCarRoundabout s = (SimpleMaxXCarRoundabout) Main.intersection;

                    String[] line = br.readLine().split(" ");
                    int maxLanes = Integer.parseInt(line[0]);
                    int time = Integer.parseInt(line[1]);
                    int maxCarPerLane = Integer.parseInt(line[2]);

                    s.setMillisecondsToWait(time);
                    s.setMaxCarsPerLane(maxCarPerLane);
                    s.setNumberOfLanes(maxLanes);
                }
            };
            case "priority_intersection" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("priority_intersection");
                    PriorityIntersection s = (PriorityIntersection) Main.intersection;
                }
            };
            case "crosswalk" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("crosswalk");

                    String[] line = br.readLine().split(" ");
                    int time = Integer.parseInt(line[0]);
                    int maxPedestrians = Integer.parseInt(line[1]);

                    Main.pedestrians = new Pedestrians(time, maxPedestrians);

                }
            };
            case "simple_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("simple_maintenance");
                    SimpleMaintenance s = (SimpleMaintenance) Main.intersection;

                    String[] line = br.readLine().split(" ");
                    int maxCars = Integer.parseInt(line[0]);
                    s.setMaxCarsToPass(maxCars);
                }
            };
            case "complex_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    
                }
            };
            case "railroad" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("railroad");
                }
            };
            default -> null;
        };
    }

}
