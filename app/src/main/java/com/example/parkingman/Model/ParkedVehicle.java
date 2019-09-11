package com.example.parkingman.Model;

import android.support.annotation.NonNull;

import com.example.parkingman.App;
import com.example.parkingman.R;

import java.io.Serializable;
import java.util.Date;

public class ParkedVehicle implements Serializable {

    public enum VehicleType{
        CAR,
        TRUCK,
        MOTORCYLE,
        OTHER;

        @Override
        public String toString() {
            switch (this){
                case TRUCK: return App.string(R.string.vehicle_type_truck);
                case CAR: return App.string(R.string.vehicle_type_car);
                case MOTORCYLE: return App.string(R.string.vehicle_type_motorcycle);
                case OTHER: return App.string(R.string.vehicle_type_other);
                default: return ""; // keeps compiler happy
            }
        }
    }

    private final String plateNo;
    private final VehicleType type;
    private final Date parkStartTimeUTC;

    public ParkedVehicle(String plateNo, VehicleType type, Date parkStartTimeUTC) {
        this.plateNo = plateNo;
        this.type = type;
        this.parkStartTimeUTC = parkStartTimeUTC;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public VehicleType getType() {
        return type;
    }

    public Date getParkStartTimeUTC() {
        return parkStartTimeUTC;
    }

    @NonNull
    @Override
    public String toString() {
        return "Vehicle=" + plateNo;
    }
}
