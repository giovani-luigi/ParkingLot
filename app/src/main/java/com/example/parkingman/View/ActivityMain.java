package com.example.parkingman.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.parkingman.Controller.ActivityMainController;
import com.example.parkingman.Controller.ControllableActivity;
import com.example.parkingman.R;

public class ActivityMain extends ControllableActivity {

    private EditText filterText;
    private Button btnParkNew;
    private ListView listView;

    private ActivityMainController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the layout of the view
        setContentView(R.layout.activity_main);

        // set the controller (MVC)
        controller = new ActivityMainController(this, savedInstanceState);

        // set local references to components
        filterText = findViewById(R.id.main_searchBox);
        btnParkNew = findViewById(R.id.main_button_park);
        listView = findViewById(R.id.main_listView);

        // assign handlers
        btnParkNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.startParkingNew();
            }
        });
        filterText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable){}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                controller.filterAdapter(charSequence.toString());
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                controller.onItemClick(position);
            }
        });

        update();
    }

    private void update(){
        // update view components with their latest content
        listView.setAdapter(controller.createParkingSpotsAdapter(R.layout.main_list_item, R.layout.main_header_item));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        controller.processResult(requestCode, resultCode, data);
    }

    @Override
    protected void onControllerUpdateRequest() {
        controller.reloadData(); // force re-load to reflect any possible change
        update();
    }
}
