package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.database.TripDatabaseHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateTripActivity extends Activity
{
    private static String location;
    private ArrayList<Person> people = new ArrayList<Person>();

    private TripDatabaseHelper db_helper;

    private final int SEARCH_LOCATION = 1;
    private final int REQUEST_CONTACT = 2;
    private final Uri HW3API_LOC_URI = Uri.parse("location://com.example.nyu.hw3api");

    private static final String TAG = "CreateTripActivity";
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        Button create_trip_button = (Button) findViewById(R.id.createTripButtonCmd);
        Button choose_destination_button = (Button) findViewById(R.id.chooseDestinationBtn);
        Button choose_contacts_button = (Button) findViewById(R.id.chooseContactsBtn);

        create_trip_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Trip trip = createTrip();
                saveTrip(trip);
            }
        });

        choose_destination_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent search_location_intent = new Intent(Intent.ACTION_VIEW, HW3API_LOC_URI);
                EditText search_area_text = (EditText) findViewById(R.id.enter_destination_area);
                EditText search_location_text = (EditText) findViewById(R.id.enter_destination_location);
                EditText search_type_text = (EditText) findViewById(R.id.enter_destination_type);
                String area_string = search_area_text.getText().toString();
                String location_string = search_location_text.getText().toString();
                String type_string = search_type_text.getText().toString();

                if (area_string != null && area_string.length() > 0 && location_string != null
                        && location_string.length() > 0 && type_string != null && type_string.length() > 0)
                {
                    search_location_intent.putExtra("searchVal", area_string + ", " + location_string + "::" + type_string);
                    startActivityForResult(search_location_intent, SEARCH_LOCATION);
                }
            }
        });

        choose_contacts_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                PickContact();
            }
        });
    }


    /**
     * Function to handle Foursquare data
     * @param data the data received
     * @return a string of the location
     */
    public String GetHW3APIData(Intent data)
    {
        ArrayList<String> result = data.getExtras().getStringArrayList("retVal");
        String retstring = result.get(0) + ", " + result.get(1);
        return retstring;
    }

    /**
     * Pick a contact from the contact book
     */
    public void PickContact()
    {
        Intent pick_contact_intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(pick_contact_intent, REQUEST_CONTACT);
    }

    /**
     * Gets the data from the contact book
     * @param data from contact book
     */
    public void GetContactData(Intent data)
    {
        try
        {
            Uri contact_uri = data.getData();
            //String[] query_fields = new String[] { ContactsContract.Contacts.DISPLAY_NAME };
            Cursor c = getContentResolver().query(contact_uri, null, null, null, null);
            if (c.getCount() == 0)
            {
                c.close();
                return;
            }

            c.moveToFirst();
            String person_name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            String has_phone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            String phone_number = "-1";
            if (has_phone.equalsIgnoreCase("1"))
            {
                Cursor phones = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                phones.moveToFirst();
                phone_number = phones.getString(phones.getColumnIndex("data1"));
                phones.close();
            }
            Cursor emails = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id, null, null);
            emails.moveToFirst();
            String email_address = emails.getString(emails.getColumnIndex("data1"));
            if (email_address == null)
                email_address = "no email";
            emails.close();
            people.add(new Person(person_name, phone_number, email_address));

            TextView attendees = (TextView) findViewById(R.id.added_attendees);
            String current_attendees = attendees.getText().toString();
            current_attendees += "* " + person_name + ": " + phone_number + "; " + email_address + "\n";
            attendees.setText(current_attendees);

            Toast.makeText(CreateTripActivity.this, "Contact added!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Log.i(TAG, "Exception in GetContactData: " + e.toString());
        }

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
        EditText trip_name_input = (EditText) findViewById(R.id.trip_name);
        EditText trip_date_input = (EditText) findViewById(R.id.date_input);
        TextView trip_destination_input = (TextView) findViewById(R.id.choose_destination_text);

        try
        {
            return new Trip(trip_name_input.getText().toString(), trip_date_input.getText().toString(),
                trip_destination_input.getText().toString(), people);
        }
        catch (Exception e)
        {
            Log.i(TAG, "Exception in createTrip: " + e.toString());
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
        /*Intent intent = new Intent(this, MainActivity.class);
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
        }*/

        try
        {
            TripDatabaseHelper db_helper = new TripDatabaseHelper(getApplicationContext());
            //trip.ChangeID(db_helper.InsertTrip(trip));
            db_helper.PopulateTrips(trip);
            Toast.makeText(this, "Trip added to database!", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Inserted into database!");
            this.finish();
            return true;
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Exception in saveTrip: " + e.toString(), Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Exception in saveTrip: " + e.toString());
            return false;
        }
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

    @Override
    protected void onActivityResult(int request_code, int result_code, Intent data)
    {
        if (result_code != Activity.RESULT_OK && result_code != Activity.RESULT_FIRST_USER) return;

        switch (request_code)
        {
            case SEARCH_LOCATION:
                TextView search_loc = (TextView) findViewById(R.id.choose_destination_text);
                search_loc.setText(GetHW3APIData(data));
                break;
            case REQUEST_CONTACT:
                GetContactData(data);
                break;

        }
    }
}
