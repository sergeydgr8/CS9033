package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.database.TripDatabaseHelper;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewTripActivity extends Activity {

    private static final String TAG = "ViewTripActivity";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_trip);

        Intent newintent = getIntent();
        
        final Trip trip = getTrip(newintent);
        viewTrip(trip);

        final Button toggle_current_trip = (Button) findViewById(R.id.view_trip_toggle_current);

        toggle_current_trip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TripDatabaseHelper db = new TripDatabaseHelper(getApplicationContext());
                if (!trip.isCurrent())
                {
                    trip.setAsCurrentTrip();
                    db.MakeTripCurrent(trip);
                    toggle_current_trip.setText("Remove from current");
                }
                else
                {
                    trip.notCurrentAnymore();
                    db.RemoveFromCurrent(trip);
                    toggle_current_trip.setText("Set as current");
                }
            }
        });
    }
    
    /**
     * Create a Trip object via the recent trip that
     * was passed to TripViewer via an Intent.
     * 
     * @param i The Intent that contains
     * the most recent trip data.
     * 
     * @return The Trip that was most recently
     * passed to TripViewer, or null if there
     * is none.
     */
    public Trip getTrip(Intent i)
    {
        TripDatabaseHelper db = new TripDatabaseHelper(getApplicationContext());
        return db.GetTrip(i.getLongExtra("id", -1));
    }

    /**
     * Populate the View using a Trip model.
     * 
     * @param trip The Trip model used to
     * populate the View.
     */
    public void viewTrip(Trip trip)
    {
        try
        {
            TextView trip_name = (TextView) findViewById(R.id.view_trip_name);
            TextView trip_destination = (TextView) findViewById(R.id.view_trip_destination);
            TextView trip_datetime = (TextView) findViewById(R.id.view_trip_datetime);
            TextView trip_attendees = (TextView) findViewById(R.id.attendees_view_text);

            trip_name.setText(trip.getTripName());
            trip_destination.setText(trip.getDestination() + " (" + trip.getLatitude()
                + ", " + trip.getLongitude() + ")");

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            trip_datetime.setText(sdf.format(new Date(trip.getDate() * 1000)));

            for (Person p : trip.getPeople())
            {
                String person_description = "* " + p.getPersonName() + ": " + p.getPhoneNumber() + "; "
                        + p.getEmailAddress() + "\n";
                trip_attendees.setText(trip_attendees.getText() + person_description);
            }

            Button toggle_current = (Button) findViewById(R.id.view_trip_toggle_current);

            if (trip.isCurrent())
            {
                toggle_current.setText("Remove from current");
            }
            else
            {
                toggle_current.setText("Set as current");
            }
        }
        catch (Exception e)
        {
            Log.i(TAG, "Exception in viewTrip: " + e.toString());
            Toast.makeText(this, "Exception in viewTrip: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
