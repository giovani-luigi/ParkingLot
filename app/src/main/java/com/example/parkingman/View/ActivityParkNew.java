package com.example.parkingman.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.parkingman.Controller.ActivityParkNewController;
import com.example.parkingman.Controller.ControllableActivity;
import com.example.parkingman.R;

public class ActivityParkNew extends ControllableActivity {

    private ActivityParkNewController controller;
    private EditText txtPlate;
    private Button btnOk;
    private Spinner spinSpots;
    private Spinner spinTypes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_park_new);

        // set the controller (MVC)
        controller = new ActivityParkNewController(this, savedInstanceState);

        // set local references to components
        txtPlate = findViewById(R.id.addnew_txtbox_plate);
        btnOk = findViewById(R.id.addnew_button_ok);
        spinSpots = findViewById(R.id.addnew_spinner_spot);
        spinTypes = findViewById(R.id.addnew_spinner_type);

        // assign handlers
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                controller.finishParking(txtPlate.getText().toString());
            }
        });
        spinSpots.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                controller.setSelectedAvailableSpotByPosition(position);
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {
                controller.setSelectedAvailableSpotByPosition(-1);
            }
        });
        spinTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                controller.setSelectedVehicleType(position);
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {
                controller.setSelectedVehicleType(-1);
            }
        });

        update();
    }

    private void update(){

        // update view components with their latest contents
        // (we update the view by creating a new adapter with the new content)

        spinTypes.setAdapter(
                controller.createVehicleTypesAdapter(
                        R.layout.spinner_simple_item,
                        R.id.spinner_item_textView));

        spinTypes.setSelection(controller.getSelectedVehicleTypePosition());

        spinSpots.setAdapter(
                controller.createAvailableSpotsAdapter(
                        R.layout.spinner_simple_item,
                        R.id.spinner_item_textView
                        ));

        spinSpots.setSelection(controller.getSelectedAvailableSpotPosition());
    }

}
