package com.example.parkingman.Controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.example.parkingman.App;
import com.example.parkingman.Model.ParkedVehicle;
import com.example.parkingman.Model.ParkingBill;
import com.example.parkingman.Model.ParkingLot;
import com.example.parkingman.Model.ParkingSpot;
import com.example.parkingman.R;

import java.text.NumberFormat;
import java.util.Date;

public class ActivityParkingMeterController {

    private final ControllableActivity activity;
    private final ParkingLot parkingLot;
    private final ParkingSpot spot;
    private final int spotId;

    public ActivityParkingMeterController(ControllableActivity activity, int spotId){
        this.activity = activity;
        this.spotId = spotId;
        parkingLot = ParkingLot.load(); // retrieve from storage or create new
        this.spot = parkingLot.findSpot(spotId);
    }

    public void unpark(){
        ParkingBill bill = parkingLot.unpark(spotId);
        Intent intent = new Intent();
        intent.putExtra("bill", bill.getTotalAmount());
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    /**
     * Returns a localized string containing the local time when the vehicle was parked
     * */
    public String getParkingTimeLocal(){
        ParkedVehicle vehicle = spot.getVehicle();
        if (vehicle != null){
            Date parkingTime = TimeProvider.getLocalFromUtc(vehicle.getParkStartTimeUTC());
            return parkingTime.toString(); // this will generate Local time string
        }
        return ""; // should never happen
    }

    /**
     * Returns a localized string containing the value of the fee to bill for
     * the parked vehicle, based in the current moment in time.
     * */
    public String getParkingFee(){
        if (!spot.isEmpty()){
            NumberFormat format = NumberFormat.getCurrencyInstance();
            return format.format(spot.getTotalFee());
        }
        return ""; // default value for unknown situation
    }

    public ParkedVehicle getVehicle(){
        return spot.getVehicle();
    }

    public Drawable getVehicleDrawable(){

        ParkedVehicle vehicle = spot.getVehicle();

        if (vehicle==null) return null;

        switch (vehicle.getType()){
            case TRUCK:     return App.drawable(R.drawable.pickup);
            case MOTORCYLE: return App.drawable(R.drawable.motorcycle);
            default:        return App.drawable(R.drawable.car);
        }
    }

}
