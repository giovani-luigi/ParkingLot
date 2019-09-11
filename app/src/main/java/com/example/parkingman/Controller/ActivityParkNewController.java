package com.example.parkingman.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.parkingman.App;
import com.example.parkingman.Model.ParkedVehicle;
import com.example.parkingman.Model.ParkingLot;
import com.example.parkingman.Model.ParkingSpot;
import com.example.parkingman.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityParkNewController {

    private final ParkingLot parkingLot;
    private ControllableActivity activity;
    private ArrayAdapter adapterVehicleTypes;
    private ArrayAdapter adapterAvailableSpots;
    private List<ParkingSpot> availableSpots;
    private int selectedAvailableSpotId = -1; // the ID of the item
    private ParkedVehicle.VehicleType selectedVehicleType;


    public ActivityParkNewController(ControllableActivity activity, Bundle state) {
        this.activity = activity;

        parkingLot = ParkingLot.load(); // retrieve from storage or create new
        availableSpots = parkingLot.getAvailableSpots();

        // load initial values:
        //  check if some spot has been pre-selected, otherwise use first available
        setSelectedAvailableSpotById(activity.getIntent().getIntExtra("spot", 0));
        selectedVehicleType = ParkedVehicle.VehicleType.CAR;
    }


    /**
     * Creates and returns a new adapter with all available types of vehicles to park
     * */
    public SpinnerAdapter createVehicleTypesAdapter(int itemLayout, int textViewId) {

        // make a new adapter with every possible Enum value
        ArrayList<String> vehicleOptions = new ArrayList<>();
        for (ParkedVehicle.VehicleType type : ParkedVehicle.VehicleType.values()) {
            vehicleOptions.add(type.toString());
        }
        adapterVehicleTypes = new ArrayAdapter<>(activity, itemLayout, textViewId, vehicleOptions);

        return adapterVehicleTypes;
    }

    /**
     * Creates and returns a new adapter reflecting the most recent available parking spots in the database
     * */
    public SpinnerAdapter createAvailableSpotsAdapter(int itemLayout, int textViewId) {

        // populate and return adapter
        ArrayAdapter<ParkingSpot> adapter = new ArrayAdapter<ParkingSpot>(activity, itemLayout, textViewId, availableSpots);

        return adapter;
    }

    /**
     * Set the selected spot using an ID.
     * */
    private void setSelectedAvailableSpotById(int spotId) {
        selectedAvailableSpotId = spotId;
    }

    /**
     * Set the ID of the selected spot using the selected position in the view.
     * That position will be translated into a spot ID value
     * */
    public void setSelectedAvailableSpotByPosition(int selectedViewPosition){
        // using the position of the selection from available spots, retrieve the ID of the spot
        selectedAvailableSpotId = availableSpots.get(selectedViewPosition).getId();
    }

    public int getSelectedAvailableSpotPosition(){
        // find the position of the selected spot by its ID
        for (int i = 0; i < availableSpots.size(); i++) {
            if (availableSpots.get(i).getId() == selectedAvailableSpotId)
                return i;
        }
        return -1; // none find
    }


    /**
     * Set the selected type of vehicle using the selected position in the view
     * That position will be converted into a value of an ENUM
     * */
    public void setSelectedVehicleType(int selectedViewPosition){
        selectedVehicleType = ParkedVehicle.VehicleType.values()[selectedViewPosition];
    }

    public ParkedVehicle.VehicleType getSelectedVehicleType(){
        return selectedVehicleType;
    }

    public int getSelectedVehicleTypePosition(){
        return selectedVehicleType.ordinal(); // this works because the spinner list exactly as in the ENUM order.
    }

    private void showNotification(String message){
        Toast t = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 20);
        t.show();
    }

    public void finishParking(String plateNo) {

        if (!isValidPlate(plateNo)){
            showNotification(App.string(R.string.error_invalid_plate));
            return;
        }

        boolean success = parkingLot.park(getSelectedVehicleType(), plateNo, selectedAvailableSpotId);

        if (!success){
            showNotification(App.string(R.string.errro_saving_data)); // tell user to try again
        }else{
            activity.setResult(Activity.RESULT_OK);
            activity.finish();
        }

    }

    private boolean isValidPlate(String plateNo) {
        return (plateNo.length() > 4);
    }

}
