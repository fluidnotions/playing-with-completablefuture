package fluidnotions.vehicles;

import fluidnotions.vehicles.exceptions.OilChangeRequired;
import fluidnotions.vehicles.exceptions.RefuelRequired;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


public abstract class Vehicle {


    private final Integer initialCargoCapacity;
    private Integer refuelCount = 0;
    private Integer oilChangeCount = 0;
    Integer currentCargoWeight = 100;
    Double fuel = 100.0;
    Double oil = 100.0;
    Integer millage = 0;
    Integer fuelEfficiencyConstant = 0;
    ExecutorService executor;

    public Vehicle(Integer cargoSpace, Integer fuelEfficiencyCoefficient , ExecutorService executor) {
        this.initialCargoCapacity = cargoSpace;
        this.executor = executor;
        this.fuelEfficiencyConstant = fuelEfficiencyCoefficient ;
    }

    public void addCargo(Integer kgs) {
        if(kgs + currentCargoWeight <= initialCargoCapacity){
            currentCargoWeight += kgs;
        }else{
            String exceedsBy = String.valueOf(Math.abs(currentCargoWeight - kgs));
            String msg = """
                    Vehicle type: %s
                    Cargo addition, exceeds current vehicle's capacity by %s kgs.
                    Cargo load rejected. 
                    (Vehicle capacity: %s kgs.)
                    """.formatted(getClass().getSimpleName(), exceedsBy, initialCargoCapacity);
            System.out.println(msg);
        }
    }

    CompletableFuture<Void> refuel() {
        CompletableFuture<Void> refueledFuture = new CompletableFuture<>();

        // Simulate refueling
        fuel = 100.0;
        refuelCount++;

        // Add car back to thread pool
        executor.submit((Runnable) this);
        System.out.println("Vehicle (%s) added back to thread pool, after refuel\n".formatted(getClass().getSimpleName()));

        refueledFuture.complete(null);
        return refueledFuture;
    }
    
    CompletableFuture<Void> changeOil() {
        CompletableFuture<Void> oilChangedFuture = new CompletableFuture<>();

        // Simulate refueling
        oil = 100.0;
        oilChangeCount++;

        // Add car back to thread pool
        executor.submit((Runnable) this);
        System.out.println("Vehicle (%s) added back to thread pool, after oil change\n".formatted(getClass().getSimpleName()));

        oilChangedFuture.complete(null);
        return oilChangedFuture;
    }

    public void printStats(){
        String msg = """
                    Vehicle type: %s.
                    Millage: %s. 
                    Current Cargo: %s kgs.
                    Refuel count: %s.
                    Oil change count: %s.
                    """.formatted(getClass().getSimpleName(), millage, currentCargoWeight, refuelCount, oilChangeCount);
        System.out.println(msg);
    }

    private void simulateEntropyImpactFuel() throws RefuelRequired {
        double reduction = (currentCargoWeight * fuelEfficiencyConstant * EntropyAdjustmentFactors.CARGO_WEIGHT) + (millage * EntropyAdjustmentFactors.FUEL_MILEAGE);
        fuel = fuel - reduction;
        if(fuel <= 0.0){
            throw new RefuelRequired();
        }
    }

    private void simulateEntropyImpactOil() throws OilChangeRequired {
        double reduction = (currentCargoWeight * fuelEfficiencyConstant * EntropyAdjustmentFactors.CARGO_WEIGHT) + (millage * EntropyAdjustmentFactors.OIL_MILEAGE);
        oil = oil - reduction;
        if(oil <= 0.0){
            throw new OilChangeRequired();
        }
    }

    public void drive(Integer velocityFactor) throws RefuelRequired, OilChangeRequired {
        try {
            TimeUnit.MILLISECONDS.sleep(500);
            millage += 100 * velocityFactor;
            simulateEntropyImpactFuel();
            simulateEntropyImpactOil();
            if(millage % 2000 == 0){
                printStats();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
