package fluidnotions;

import fluidnotions.vehicles.Car;
import fluidnotions.vehicles.ElectricCar;
import fluidnotions.vehicles.Truck;
import fluidnotions.vehicles.Vehicle;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Simulator {
    public static void main(String[] args){
        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Vehicle> vehicles = List.of(
                new Car(13, 500, 3, executor),
                new ElectricCar(8, 500, 5, executor),
                new Truck(5, 18000, 13, executor)
        );

        vehicles.forEach((vehicle) -> {
            executor.submit((Runnable) vehicle);
        });

        Callable<Void> addRandomCargo = () -> {
            while (!executor.isShutdown()) {
                Vehicle randomVehiclePick = vehicles.get((int) (Math.random() * vehicles.size()));
                Integer randomCargoLoad = (int) (Math.random() * 1000);
                randomVehiclePick.addCargo(randomCargoLoad);
                System.out.println("Attempting to load " + randomCargoLoad + "kg cargo to " + randomVehiclePick.getClass().getSimpleName() + "\n");
                TimeUnit.SECONDS.sleep(3);
            }
            return null;
        };
        executor.submit(addRandomCargo);

        // Wait for user to press enter to shut down the thread pool
        System.out.println("Press enter to shutdown the thread pool");
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Shutdown the thread pool
        executor.shutdown();
    }
}
