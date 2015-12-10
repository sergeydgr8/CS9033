package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.database.TripDatabaseHelper;
import com.nyu.cs9033.eta.models.Trip;

import java.util.ArrayList;


public class TripHistoryActivity extends Activity
{
    private static final String TAG = "TripHistoryActivity";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);
        setTitle("ETA: Trip History");
        ListView layout_list_of_trips = (ListView) findViewById(R.id.list_of_trips);
        ListView layout_of_current_trips = (ListView) findViewById(R.id.list_of_current_trips);

        TripDatabaseHelper db_helper = new TripDatabaseHelper(getApplicationContext());
        ArrayList<Trip> trips = db_helper.GetAllTrips();

        ArrayList<String> trip_titles = new ArrayList<String>();
        final ArrayList<Long> trip_ids = new ArrayList<Long>();
        for (Trip t : trips)
        {
            trip_titles.add(t.getTripName());
            trip_ids.add(t.getTripID());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.trip_row,
                trip_titles);

        ArrayList<String> current_trip_titles = new ArrayList<String>();
        final ArrayList<Long> current_trip_ids = new ArrayList<Long>();
        for (Trip t : trips)
        {
            if (t.isCurrent())
            {
                current_trip_titles.add(t.getTripName());
                current_trip_ids.add(t.getTripID());
            }
        }
        ArrayAdapter<String> current_adapter = new ArrayAdapter<String>(
                this,
                R.layout.trip_row,
                current_trip_titles);

        layout_list_of_trips.setAdapter(adapter);
        layout_list_of_trips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //int actual_id = (int)id + 1;
                    long actual_id = trip_ids.get(position);
                    Intent view_trip_intent = new Intent(getApplicationContext(), ViewTripActivity.class);
                    view_trip_intent.putExtra("id", actual_id);
                    startActivity(view_trip_intent);
                } catch (Exception e) {
                    Log.i(TAG, "Exception in setOnItemClickListener: " + e.toString());
                    Toast.makeText(TripHistoryActivity.this, "Exception in setOnItemClickListener: "
                            + e.toString(), Toast.LENGTH_LONG).show();
                }

                /*TextView text_view = (TextView) view;
                String str = "Position: " + position + "\nID: " + id;
                Toast.makeText(TripHistoryActivity.this, str, Toast.LENGTH_SHORT).show();*/
            }
        });

        layout_of_current_trips.setAdapter(current_adapter);
        layout_of_current_trips.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                try
                {
                    long actual_id = current_trip_ids.get(position);
                    Intent view_trip_intent = new Intent(getApplicationContext(), ViewTripActivity.class);
                    view_trip_intent.putExtra("id", actual_id);
                    startActivity(view_trip_intent);
                }
                catch (Exception e)
                {
                    Log.i(TAG, "Exception in setOnItemClickListener: " + e.toString());
                    Toast.makeText(TripHistoryActivity.this, "Exception in setOnItemClickListener: "
                            + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
