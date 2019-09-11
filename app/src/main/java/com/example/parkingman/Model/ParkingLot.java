package com.example.parkingman.Model;

import android.support.annotation.Nullable;

import com.example.parkingman.Constants;
import com.example.parkingman.Storage.InternalStorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParkingLot implements Serializable {

    private ArrayList<ParkingSpot> spots = new ArrayList<>();

    private ParkingLot(){
    }

    private static ParkingLot build(float hourFee, int spotCount) {

        ParkingLot parkingLot = new ParkingLot();

        // build and name the spots using a flat fee
        for (int i = 1; i <= spotCount; i++)
            parkingLot.spots.add(new ParkingSpot( i, String.valueOf(i), hourFee));

        return parkingLot;
    }

    public static ParkingLot load() {
        Object loaded = InternalStorage.readObject(Constants.DATA_FILE_NAME);
        if (loaded != null)
            return (ParkingLot) loaded;
        return build(Constants.STANDARD_HOUR_FEE, Constants.PARKING_LOT_SIZE);
    }

    /**
     * Save the changes in the permanent storage, returning false if fails.
     * */
    private boolean saveChanges(){
        return InternalStorage.writeObject(this, Constants.DATA_FILE_NAME);
    }

    public List<ParkingSpot> getSpots(){
        return spots;
    }

    public List<ParkingSpot> getAvailableSpots(){
        ArrayList<ParkingSpot> availableSpots = new ArrayList<>();
        for (ParkingSpot spot : spots) {
            if (spot.isEmpty())
                availableSpots.add(spot);
        }
        return availableSpots;
    }

    /**
     * Park a car at a given spot, returning a value indicating
     * whether the operation succeeded (true) or not (false)
     * */
    public boolean park(ParkedVehicle.VehicleType type, String carPlate, int spotId){
        ParkingSpot spot = findSpot(spotId);

        if (spot==null)
            return false; // spot not found

        try {
            spot.park(carPlate, type);
        } catch (Exception e) { // cannot park
            e.printStackTrace();
            return false;
        }

        return saveChanges();
    }

    private ParkingBill unpark(ParkingSpot spot) {
        ParkingBill bill = new ParkingBill(spot.getTotalFee());
        spot.unPark();
        saveChanges();
        return bill;
    }

    /**
     * Unpark a car by the ID of the parking spot
     * A ParkingBill will be generated and returned, or null if no spot was found
     * */
    public ParkingBill unpark(int spotId){
        ParkingSpot spot = findSpot(spotId);
        if (spot==null) // not found
            return null;
        return unpark(spot);
    }

    /**
     * Unpark a car by the plate number
     * A ParkingBill will be generated and returned, or null if no spot was found
     * */
    public ParkingBill unpark(String carPlate){
        ParkingSpot spot = findSpot(carPlate);
        if (spot==null) // not found
            return null;
        return unpark(spot);
    }

    @Nullable
    public ParkingSpot findSpot(String carPlate) {
        for (ParkingSpot spot : spots) {
            if (spot.getVehiclePlateNo().equals(carPlate))
                return spot;
        }
        return null;
    }

    @Nullable
    public ParkingSpot findSpot(int spotId) {
        for (ParkingSpot spot : spots) {
            if (spot.getId()==spotId)
                return spot;
        }
        return null;
    }

}
