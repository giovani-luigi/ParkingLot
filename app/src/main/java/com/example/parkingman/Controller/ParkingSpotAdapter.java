package com.example.parkingman.Controller;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parkingman.App;
import com.example.parkingman.Model.ListItemParkingSpot;
import com.example.parkingman.Model.ParkingLot;
import com.example.parkingman.Model.ParkingSpot;
import com.example.parkingman.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ParkingSpotAdapter extends ArrayAdapter<ParkingSpot> {

    private static final int ITEM_TYPE_NORMAL = 0;
    private static final int ITEM_TYPE_HEADER = 1;

    private int item_view_layout;
    private int list_divider_layout;
    private String filterExpression;
    private ParkingLot parkingLot;
    private List<ListItemParkingSpot> items;
    private LayoutInflater layoutInflater;

    public ParkingSpotAdapter(@NonNull Activity activity, int item_view_layout, int list_divider_layout, ParkingLot parkingLot) {
        super(activity, item_view_layout);
        this.item_view_layout = item_view_layout;
        this.list_divider_layout = list_divider_layout;
        this.parkingLot = parkingLot;
        rebuildItems("");
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

     public void rebuildItems(String filter) {
        filterExpression = filter.toLowerCase(Locale.getDefault());

        ArrayList<ParkingSpot> spots = new ArrayList<>(); // spots to generate items for
        items = new ArrayList<>(); // generated items

        // first filter the spots to use
        if (filterExpression.length() == 0) {
            spots.addAll(parkingLot.getSpots());
        }else{
            for (ParkingSpot spot : parkingLot.getSpots()){
                try {
                    if (spot.getVehiclePlateNo().toLowerCase(Locale.getDefault()).contains(filterExpression))
                        spots.add(spot);
                } catch (Exception ignored) {
                } // if plate is null, is reasonable to assume its not in the result
            }
        }

        // now build all items (views)

        // filled spots
        items.add(new ListItemParkingSpot(App.string(R.string.used_spots_group)));
        for (ParkingSpot spot : spots)
            if (!spot.isEmpty())
                items.add(new ListItemParkingSpot(spot));

        // available spots:
        items.add(new ListItemParkingSpot(App.string(R.string.available_spots_group)));
        for (ParkingSpot spot : spots)
            if (spot.isEmpty())
                items.add(new ListItemParkingSpot(spot));

        notifyDataSetChanged();
    }

    private void fillItem(View rowView, ListItemParkingSpot item){

        if (item.isGroupHeader()){ // if item is a group header (list separator)
            // acquire objects from the view
            TextView title = rowView.findViewById(R.id.item_divider_header);

            // set content
            title.setText(item.getTitle());

        }else{
            // acquire objects from the view
            ImageView icon = rowView.findViewById(R.id.item_iconView);
            TextView title = rowView.findViewById(R.id.item_textView);
            TextView tag = rowView.findViewById(R.id.item_tagView);

            // populate content
            icon.setImageDrawable(App.drawable(item.getIcon()));
            title.setText(item.getTitle());

            // hide tag when there is no text
            if (item.getTag()==null || item.getTag().equals("")) {
                tag.setVisibility(View.INVISIBLE);
            }else{
                tag.setVisibility(View.VISIBLE);
                tag.setText(item.getTag());
            }
        }
    }

    @Nullable
    @Override
    public ParkingSpot getItem(int position) {
        ListItemParkingSpot item = items.get(position);
        return item.getObject();
    }

    /**
     * Returns the type of item at a given position of the list.
     * This is required to ensure that inside the getView() we will generate the correct type
     * as the framework will group them by this type on recycle
     * */
    @Override
    public int getItemViewType(int position) {
        if (items.get(position).isGroupHeader())
            return ITEM_TYPE_HEADER;
        return ITEM_TYPE_NORMAL;
    }

    /**
     * Returns the amount of different ITEMS (normal + header = 2)
     * This override is required when overriding getItemViewType()
     * */
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isEnabled(int position) {
        // make group header not clickable
        return (!items.get(position).isGroupHeader());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CachedView cache; // view holder pattern implementation
        // get the data model to be rendered
        ListItemParkingSpot item = items.get(position);

        if (convertView==null){
            switch (getItemViewType(position)){
                case ITEM_TYPE_NORMAL:
                    convertView = layoutInflater.inflate(item_view_layout, null);
                    break;
                case ITEM_TYPE_HEADER:
                    convertView = layoutInflater.inflate(list_divider_layout, null);
                    break;
            }
            // view holder pattern:
            cache = new CachedView();
            cache.rowView = convertView;
            convertView.setTag(cache);
        }else{
            cache = (CachedView) convertView.getTag();
        }

        // update data in the view to display of the item at the requested position
        fillItem(cache.rowView, item);

        return convertView;
    }


    // cache to avoid calling findViewById() too often
    static class CachedView{
        View rowView;
    }

}