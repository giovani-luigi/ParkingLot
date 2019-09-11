package com.example.parkingman.Model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.parkingman.App;
import com.example.parkingman.R;

public class ListItemParkingSpot {

    private int icon = -1;
    private String title;
    private String tag;
    private ParkingSpot object = null; // stores a reference to an object when available

    private boolean isGroupHeader = false;

    @Nullable
    public ParkingSpot getObject(){
        return object;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getTag() {
        return tag;
    }

    public boolean isGroupHeader() {
        return isGroupHeader;
    }

    /**
     * Builds a list view element from a ParkingSpot object
     * */
    public ListItemParkingSpot(ParkingSpot parkingSpot){

        object = parkingSpot;

        ParkedVehicle vehicle = parkingSpot.getVehicle();
        if (vehicle!=null){
            title = App.string(R.string.parking_spot_used, parkingSpot.getId());
            tag = vehicle.getPlateNo();
            switch (vehicle.getType()){
                case TRUCK:
                    icon = R.drawable.glyph_truck_48x28;
                    break;
                case MOTORCYLE:
                    icon = R.drawable.glyph_motorcycle_48x28;
                    break;
                default: // also 'OTHER' and 'CAR'
                    icon = R.drawable.glyph_car_48x28;
            }
        }else{
            title = App.string(R.string.parking_spot_tostring_available, parkingSpot.getId());
            icon = R.drawable.glyph_parkingspot_45x46;
            tag = "";
        }
    }

    /**
     * Builds a list view element to represent a simple item with a tag
     * @param icon the ID of the resource used to show an icon. If left at -1 will use default icon
     * @param title the main text to display in the item
     * @param tag any small text tag to assign to the item. Leaving empty/null will result in no tag.
     * */
    public ListItemParkingSpot(int icon, @NonNull String title, @Nullable String tag){
        this.icon = icon;
        this.title = title;
        this.tag = tag;
    }

    /**
     * Builds a lit view element to represent the title of a group of items.
     * */
    public ListItemParkingSpot(@NonNull String title){
        this(-1, title, null);
        isGroupHeader=true;
    }

}
