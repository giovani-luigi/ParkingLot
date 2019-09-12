package com.example.parkingman.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parkingman.App;
import com.example.parkingman.Controller.ActivityParkingMeterController;
import com.example.parkingman.Controller.ControllableActivity;
import com.example.parkingman.R;

public class ActivityParkingMeter extends ControllableActivity {

    private int spotId;
    ActivityParkingMeterController controller;
    private TextView txtTitle;
    private TextView txtPlate;
    private TextView txtParkTime;
    private TextView txtFee;
    private ImageView imgVehicle;
    private Button btnUnpark;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_parking_meter);

        // load activity parameters
        this.spotId =  getIntent().getIntExtra("spot", -1);

        // set the controller (MVC)
        controller = new ActivityParkingMeterController(this, spotId);

        // set local references to components
        txtTitle = findViewById(R.id.parkingmeter_textview_title);
        txtPlate = findViewById(R.id.parkingmeter_plateno);
        txtParkTime = findViewById(R.id.parkingmeter_text_parktime);
        txtFee = findViewById(R.id.parkingmeter_text_fee);
        btnUnpark = findViewById(R.id.parkingmeter_button_unpark);
        imgVehicle = findViewById(R.id.parkingmeter_image);

        // assign handlers
        btnUnpark.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                controller.unpark();
            }
        });

        update();
    }

    private void update(){
        // update view components with their latest content
        txtTitle.setText(App.string(R.string.parkingmeter_title, controller.getParkingSpot().getId()));
        txtPlate.setText(controller.getVehicle().getPlateNo());
        txtFee.setText(controller.getParkingSpot().getTotalFeeString());
        txtParkTime.setText(controller.getParkingTimeLocal());
        imgVehicle.setImageDrawable(controller.getVehicleDrawable());
    }

}
