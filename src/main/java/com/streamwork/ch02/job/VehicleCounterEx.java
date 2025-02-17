package com.streamwork.ch02.job;

import com.streamwork.ch02.api.Event;

import java.util.*;

public class VehicleCounterEx extends VehicleCounter{
    /*
    Добавлена логика по подсчету суммы сбора
     */

    protected final Map<String, Double> priceMap = new HashMap<String, Double>();
    private final Double price = 10.0; //Цена за проезд
    public VehicleCounterEx(String name) {
        super(name);
    }

    @Override
    public void apply(Event vehicleEvent, List<Event> eventCollector) {
        String vehicle = ((VehicleEvent)vehicleEvent).getData();
        Integer count = countMap.getOrDefault(vehicle, 0) + 1;
        countMap.put(vehicle, count);

        System.out.println("VehicleCounter --> ");
        super.printCountMap();

        priceMap.put(vehicle, count*price);
        System.out.println("VehiclePriceCounter --> ");
        printPriceMap();
    }

    protected void printPriceMap() {
        List<String> vehicles = new ArrayList<>(priceMap.keySet());
        Collections.sort(vehicles);

        for (String vehicle: vehicles) {
            System.out.println("  " + vehicle + ": " + priceMap.get(vehicle));
        }
    }



}
