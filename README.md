# playing-with-completablefuture

## Synopsis
* A not too realistic in terms of the stats, simulation of a hierarchy of vehicles. 
* ‘Driving’ around in a thread pool and when various factors that reduce oil and fuel to zero, they exit the thread pool are refueled and placed back in the threadpool.
* The frequency of oil changes or refueling is determined by characteristics of each vehicle instance.
* The various vehicle types extend an abstract class containing all the common logic.
* Each vehicle concrete class implements the Runnable interface.
* Each vehicle type instance has attributes such is velocityFactor, cargoCapactity, fuelEfficiencyCoefficient

## Simulator
* This is the entry point.
* A fixed thread pool is used to accommodate the 3 vehicle types & the addRandomCargo Callable.

## Behavior
* The 3 vehicle instances are added to the executor along with the addRandomCargo Function.
* The runnable vehicle instances call a ‘drive’ function in the super class.
* According to some factors defined in the EntropyAdjustmentFactors interface, together with the current cargo load, mileage and the fuelEfficiencyCoefficient, both fuel and oil is reduced over time. 
* When either fuel or oil reaches zero an expectation is thrown, causing the instance to exit the thread pool, be refueled or have an oil changed and then it is returned to the threadpool to continue driving.
* Statistics about the vehicle type, cargo weight, millage and number of oil changes or refuels are printed out periodically
* The executor is shutdown when the user hits enter in the terminal
