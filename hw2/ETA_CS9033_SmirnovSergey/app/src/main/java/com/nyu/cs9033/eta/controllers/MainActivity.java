package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private Trip saved_trip;
    private int request_code = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button create_trip_button = (Button) findViewById(R.id.createTripButton);
        Button view_trip_button = (Button) findViewById(R.id.viewTripButton);

        create_trip_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) { startCreateTripActivity(view); }
        });
        view_trip_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) { startViewTripActivity(view); }
        });
    }

    /**
     * This method should start the
     * Activity responsible for creating
     * a Trip.
     */
    public void startCreateTripActivity(View view)
    {
        Intent create_trip_intent = new Intent(this, CreateTripActivity.class);
        //request_code = 1;
        startActivityForResult(create_trip_intent, request_code);
    }
    
    /**
     * This method should start the
     * Activity responsible for viewing
     * a Trip.
     */
    public void startViewTripActivity(View view)
    {
        if (saved_trip != null)
        {
            Intent view_trip_intent = new Intent(this, ViewTripActivity.class);
            //view_trip_intent.putExtra("the_trip", saved_trip);
            view_trip_intent.putExtra("the_trip", saved_trip);
            //request_code = 1;
            try
            {
                startActivityForResult(view_trip_intent, request_code);
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        else
            Toast.makeText(this, "You have to create a trip first!", Toast.LENGTH_LONG).show();
    }
    
    /**
     * Receive result from CreateTripActivity here.
     * Can be used to save instance of Trip object
     * which can be viewed in the ViewTripActivity.
     * 
     * Note: This method will be called when a Trip
     * object is returned to the main activity. 
     * Remember that the Trip will not be returned as
     * a Trip object; it will be in the persisted
     * Parcelable form. The actual Trip object should
     * be created and saved in a variable for future
     * use, i.e. to view the trip.
     * 
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                saved_trip = (Trip) data.getExtras().getParcelable("new_trip");
                Toast.makeText(this, "If you're reading this, it's saved.", Toast.LENGTH_LONG).show();
            }
            /*else if (resultCode == RESULT_CANCELED)
                Toast.makeText(this, "If you're reading this, you cancelled your trip creation.", Toast.LENGTH_LONG).show();*/
        }
        else
            Toast.makeText(this, "If you're reading this, something's wrong! ¯\\_(ツ)_/¯", Toast.LENGTH_LONG).show();
    }

}
