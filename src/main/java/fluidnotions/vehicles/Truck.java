package fluidnotions.vehicles;

import fluidnotions.vehicles.exceptions.OilChangeRequired;
import fluidnotions.vehicles.exceptions.RefuelRequired;

import java.util.concurrent.ExecutorService;

public class Truck extends Vehicle implements Runnable{
    private Integer velocityFactor;
    public Truck(Integer velocityFactor, Integer cargoSpace, Integer fuelEfficiencyMockConstant, ExecutorService executor) {
        super(cargoSpace, fuelEfficiencyMockConstant, executor);
        this.velocityFactor = velocityFactor;
    }

    @Override
    public void run() {
        while (!executor.isShutdown()) {
            try {
                super.drive(velocityFactor);
            } catch (RefuelRequired e) {
                refuel().join();
            } catch (OilChangeRequired e) {
                changeOil().join();
            }
        }
    }
}
