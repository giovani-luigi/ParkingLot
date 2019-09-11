package com.example.parkingman.Model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.parkingman.App;
import com.example.parkingman.Controller.TimeProvider;
import com.example.parkingman.R;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ParkingSpot implements Serializable {

    private final int id;
    private final String name;
    private ParkedVehicle parkedVehicle;
    private float hourFee;

    public ParkingSpot(int id, String name, float hourFee){
        this.id = id;
        this.name = name;
        this.hourFee = hourFee;
    }

    void park(String plateNo, ParkedVehicle.VehicleType type) throws Exception{
        if (!isEmpty())
            throw new Exception("There is a car already parked in this spot");
        parkedVehicle = new ParkedVehicle(plateNo, type , TimeProvider.getUniversalTime());
    }

    ParkedVehicle unPark() {
        ParkedVehicle tmp = parkedVehicle;
        parkedVehicle = null;
        return tmp;
    }

    public String getVehiclePlateNo() {
        return parkedVehicle.getPlateNo();
    }

    public boolean isEmpty() {
        return (parkedVehicle==null);
    }

    @Nullable
    public ParkedVehicle getVehicle(){
        return parkedVehicle;
    }

    public int getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        if (parkedVehicle==null){
            return App.string(R.string.parking_spot_tostring_available, id);
        }else{
            return App.string(R.string.parking_spot_tostring_used, id, parkedVehicle.getPlateNo());
        }
    }

    public float getTotalFee() {
        Date parkingTime = parkedVehicle.getParkStartTimeUTC();
        long wholeHours = TimeUnit.MILLISECONDS.toHours(TimeProvider.getUniversalTime().getTime() - parkingTime.getTime());
        return hourFee * wholeHours;
    }
}
