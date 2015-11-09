package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewTripActivity extends Activity {

    private static final String TAG = "ViewTripActivity";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_trip);

        Intent newintent = getIntent();
        
        Trip trip = getTrip(newintent);
        viewTrip(trip);

        Button button = (Button) findViewById(R.id.back_to_main_button_view);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
        return (Trip) i.getExtras().getParcelable("the_trip");
    }

    /**
     * Populate the View using a Trip model.
     * 
     * @param trip The Trip model used to
     * populate the View.
     */
    public void viewTrip(Trip trip) {
        TextView destination = (TextView) findViewById(R.id.view_destination_text);
        TextView datetime = (TextView) findViewById(R.id.view_date_text);
        TextView attendees = (TextView) findViewById(R.id.attendees_view_text);

        destination.setText(trip.getDestination());
        datetime.setText(trip.getDate());

        String attendees_list = new String();
        for (Person pp : trip.getPeople())
        {
            String attendee = new String();
            attendee = pp.GetFirstName() + " " + pp.GetLastName() + " - "
                    + pp.GetPhoneNumber() + "\n";
            attendees_list += attendee;
        }

        attendees.setText(attendees_list);
    }

    /*public void go_back()
    {
        finish();
    }*/
}
