package com.example.parkingman.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.widget.Toast;

import com.example.parkingman.App;
import com.example.parkingman.Model.ParkedVehicle;
import com.example.parkingman.Model.ParkingLot;
import com.example.parkingman.Model.ParkingSpot;
import com.example.parkingman.R;
import com.example.parkingman.View.ActivityParkNew;
import com.example.parkingman.View.ActivityParkingMeter;

public class ActivityMainController {

    private ControllableActivity activity;

    private ParkingSpotAdapter adapter;

    private ParkingLot parkingLot;

    private final static int REQUEST_CODE_PARK_NEW     = 1;
    private final static int REQUEST_CODE_PARKINGMETER = 2;

    /**
    * Create a new controller for the main view.
     * @param activity The activity/context target of this controller
     */
    public ActivityMainController(ControllableActivity activity, Bundle state){
        this.activity = activity;
        reloadData(); // retrieve from storage or create new
    }

    /**
     * Creates a new adapter to list all parking spots.
     * @param list_item_layout The ID of a view able to display the content of regular items
     * @param list_divider_layout The ID of a view able to display the content of group header
     * */
    public ParkingSpotAdapter createParkingSpotsAdapter(int list_item_layout, int list_divider_layout){
        adapter = new ParkingSpotAdapter(activity, list_item_layout, list_divider_layout, parkingLot);
        return adapter;
    }

    public void reloadData(){
        parkingLot = ParkingLot.load();
    }

    public void filterAdapter(String chars) {
        adapter.rebuildItems(chars);
    }

    /**
     * Starts a new activity to park a new vehicle
     * */
    public void startParkingNew(){
        Intent intent = new Intent(activity, ActivityParkNew.class);
        activity.startActivityForResult(intent, REQUEST_CODE_PARK_NEW);
    }

    /**
     * Starts a new activity to park a new vehicle into a pre-selected spot
     * */
    public void startParkingNew(int spotId){
        Intent intent = new Intent(activity, ActivityParkNew.class);
        intent.putExtra("spot", spotId);
        activity.startActivityForResult(intent, REQUEST_CODE_PARK_NEW);
    }

    /**
     * Remove the parked car from the spot, and generate a parking bill
     * */
    public void showParkingMeter(int spotId){
        Intent intent = new Intent(activity, ActivityParkingMeter.class);
        intent.putExtra("spot", spotId);
        activity.startActivityForResult(intent, REQUEST_CODE_PARKINGMETER);
    }

    private void showNotification(String message){
        //Snackbar bar = Snackbar.make(activity.findViewById(R.id.main_outerLayout), message, durationMs);
        Toast t = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 20);
        t.show();
    }

    @Nullable
    public ParkedVehicle getVehicle(String plateNo){
        for (ParkingSpot spot : parkingLot.getSpots()) {
            ParkedVehicle vehicle = spot.getVehicle();
            if (vehicle != null)
                if (vehicle.getPlateNo().equals(plateNo)) return vehicle;
        }
        return null;
    }

    public void onItemClick(int position){

        if (position < 0)
            return;

        ParkingSpot item = adapter.getItem(position);
        if (item.isEmpty()){
            if (parkingLot.getAvailableSpots().size() == 0){
                showNotification(App.string(R.string.parking_lot_full));
            }else{
                startParkingNew(item.getId());
            }
        }else{
            showParkingMeter(item.getId());
        }
    }

    public void processResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case REQUEST_CODE_PARK_NEW:
                if (resultCode==Activity.RESULT_OK){
                    showNotification(App.string(R.string.activity_main_message_parked));
                    activity.onControllerUpdateRequest();
                }
                break;
            case REQUEST_CODE_PARKINGMETER:
                if (resultCode==Activity.RESULT_OK){
                    // show bill
                    if (data.hasExtra("bill")){
                        float total = data.getFloatExtra("bill", 0);
                        showNotification(App.string(R.string.activity_main_billing_message, total));
                    }
                    activity.onControllerUpdateRequest();
                }
                break;
        }

    }
}
