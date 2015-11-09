package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateTripActivity extends Activity
{
    
    private static final String TAG = "CreateTripActivity";
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        Button create_trip_button = (Button) findViewById(R.id.createTripButtonCmd);
        create_trip_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Trip trip = createTrip();
                System.out.println("TEST");
                saveTrip(trip);
            }
        });
    }
    
    /**
     * This method should be used to
     * instantiate a Trip model object.
     * 
     * @return The Trip as represented
     * by the View.
     */
    public Trip createTrip()
    {
        // get all data from the view
        EditText trip_date_input = (EditText) findViewById(R.id.date_input);
        EditText trip_destination_input = (EditText) findViewById(R.id.destination_input);
        CheckBox sergeybox = (CheckBox) findViewById(R.id.sergey);
        CheckBox larrybox = (CheckBox) findViewById(R.id.larry);
        CheckBox richardbox = (CheckBox) findViewById(R.id.richard);
        CheckBox erlichbox = (CheckBox) findViewById(R.id.erlich);
        CheckBox dineshbox = (CheckBox) findViewById(R.id.dinesh);
        CheckBox gilfoylebox = (CheckBox) findViewById(R.id.gilfoyle);
        CheckBox jaredbox = (CheckBox) findViewById(R.id.jared);

        String trip_date = String.valueOf(trip_date_input.getText());
        String trip_destination = String.valueOf(trip_destination_input.getText());
        ArrayList<Person> people = new ArrayList<Person>();

        // manually create new Persons
        if (sergeybox.isChecked())
            people.add(new Person("Sergey", "Brin", "415-122-3456"));
        if (larrybox.isChecked())
            people.add(new Person("Larry", "Page", "408-322-1111"));
        if (richardbox.isChecked())
            people.add(new Person("Richard", "Hendricks", "650-123-4678"));
        if (erlichbox.isChecked())
            people.add(new Person("Erlich", "Bachmann", "650-111-1111"));
        if (dineshbox.isChecked())
            people.add(new Person("Dinesh", "Chugtai", "650-222-2222"));
        if (gilfoylebox.isChecked())
            people.add(new Person("Bertram", "Gilfoyle", "650-666-6666"));
        if (jaredbox.isChecked())
            people.add(new Person("Jared", "Dunn", "650-555-5555"));

        try
        {
            return new Trip(trip_date, trip_destination, people);
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    /**
     * For HW2 you should treat this method as a 
     * way of sending the Trip data back to the
     * main Activity.
     * 
     * Note: If you call finish() here the Activity 
     * will end and pass an Intent back to the
     * previous Activity using setResult().
     * 
     * @return whether the Trip was successfully 
     * saved.
     */
    public boolean saveTrip(Trip trip)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("new_trip", trip);
        try
        {
            this.setResult(RESULT_OK, intent);
            this.finish();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Error! " + e.toString(), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    /**
     * This method should be used when a
     * user wants to cancel the creation of
     * a Trip.
     * 
     * Note: You most likely want to call this
     * if your activity dies during the process
     * of a trip creation or if a cancel/back
     * button event occurs. Should return to
     * the previous activity without a result
     * using finish() and setResult().
     */
    public void cancelTrip()
    {
        Intent intent = new Intent(this, MainActivity.class);
        this.setResult(RESULT_CANCELED, intent);
        this.finish();
    }
}
