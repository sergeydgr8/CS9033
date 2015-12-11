package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.database.TripDatabaseHelper;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;

import org.apache.http.HttpConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ViewCurrentTripActivity extends Activity {

    private static final String TAG = "ViewCurrentTripActivity";
    private static Trip trip;
    private Location current_location;
    private LocationManager location_manager;
    private Timer trip_status_timer = new Timer();
    private Timer location_timer = new Timer();
    private String location_provider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_current_trip);

        Intent newintent = getIntent();
        
        trip = getTrip(newintent);
        viewTrip(trip);

        final Button toggle_current_trip = (Button) findViewById(R.id.CURRENT_view_trip_toggle_current);
        toggle_current_trip.setText("Finish trip");

        toggle_current_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TripDatabaseHelper db = new TripDatabaseHelper(getApplicationContext());
                trip.notCurrentAnymore();
                db.RemoveFromCurrent(trip);
                finish();
            }
        });

        location_manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (location_manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            location_provider = LocationManager.GPS_PROVIDER;
        else if (location_manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            location_provider = LocationManager.NETWORK_PROVIDER;
        else
            Log.e(TAG, "Error: Neither location provider is running.");

        try
        {
            current_location = location_manager.getLastKnownLocation(location_provider);
            location_manager.requestLocationUpdates(location_provider, 10000, 15, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) { }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) { }

                @Override
                public void onProviderEnabled(String provider)
                {
                    current_location = location_manager.getLastKnownLocation(location_provider);
                }

                @Override
                public void onProviderDisabled(String provider) { }
            });
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error in onCreate: " + e.toString());
        }

        UpdateTripStatusTimerTask utstt = new UpdateTripStatusTimerTask();
        trip_status_timer.schedule(utstt, 0, 10000);

        UpdateLocationTimerTask ultt = new UpdateLocationTimerTask();
        location_timer.schedule(ultt, 0, 10000);

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
            TextView trip_name = (TextView) findViewById(R.id.CURRENT_view_trip_name);
            TextView trip_destination = (TextView) findViewById(R.id.CURRENT_view_trip_destination);
            TextView trip_datetime = (TextView) findViewById(R.id.CURRENT_view_trip_datetime);
            TextView trip_attendees = (TextView) findViewById(R.id.CURRENT_attendees_view_text);

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
        }
        catch (Exception e)
        {
            Log.i(TAG, "Exception in viewTrip: " + e.toString());
            Toast.makeText(this, "Exception in viewTrip: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public String buildUpdateLocationJson()
    {
        current_location = location_manager.getLastKnownLocation(location_provider);
        try
        {
            JSONObject js = new JSONObject();
            js.put("command", "UPDATE_LOCATION");
            js.put("latitude", current_location.getLatitude());
            js.put("longitude", current_location.getLongitude());
            js.put("datetime", new Date().getTime() / 1000);
            return js.toString();
        }
        catch (JSONException e)
        {
            Log.e(TAG, "Exception in buildUpdateLocationJson: " + e.toString());
            return null;
        }
    }

    private String sendUpdateLocationRequest(String urls) throws IOException
    {
        StringBuilder response = new StringBuilder();
        URL url = new URL(urls);
        HttpURLConnection cxn = (HttpURLConnection) url.openConnection();

        try
        {
            cxn.setRequestMethod("POST");
            DataOutputStream out = new DataOutputStream(cxn.getOutputStream());
            out.writeBytes(buildUpdateLocationJson());
            out.flush();
            out.close();

            Scanner response_input = new Scanner(cxn.getInputStream());
            while (response_input.hasNext())
                response.append(response_input.nextLine());
        }
        finally
        {
            cxn.disconnect();
        }

        return response.toString();
    }

    private class UpdateLocationTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                return sendUpdateLocationRequest(urls[0]);
            }
            catch (IOException e)
            {
                Log.e(TAG, "Error in UpdateLocationTask::doInBackground: " + e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            try
            {
                JSONObject js = new JSONObject(result);
                if (js.getInt("response_code") != 0)
                    Log.e(TAG, "Error in UpdateLocationTask::onPostExecute: error with request.");
            }
            catch (JSONException e)
            {
                Log.e(TAG, "Error in UpdateLocationTask::onPostExecute: " + e.toString());
            }
        }
    }

    private class UpdateLocationTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            UpdateLocationTask update_location = new UpdateLocationTask();
            update_location.execute("http://cs9033-homework.appspot.com");
        }
    }


    public String buildUpdateTripStatusJson()
    {
        try
        {
            JSONObject js = new JSONObject();
            js.put("command", "TRIP_STATUS");
            js.put("trip_id", trip.getTripID());
            return js.toString();
        }
        catch (JSONException e)
        {
            Log.e(TAG, "Exception in buildUpdateTripStatusJson: " + e.toString());
            return null;
        }
    }

    private String sendUpdateTripStatusRequest(String urls) throws IOException
    {
        StringBuilder response = new StringBuilder();
        URL url = new URL(urls);
        HttpURLConnection cxn = (HttpURLConnection) url.openConnection();

        try
        {
            cxn.setRequestMethod("POST");
            DataOutputStream out = new DataOutputStream(cxn.getOutputStream());
            out.writeBytes(buildUpdateTripStatusJson());
            out.flush();
            out.close();

            Scanner response_input = new Scanner(cxn.getInputStream());
            while (response_input.hasNext())
                response.append(response_input.nextLine());
        }
        finally
        {
            cxn.disconnect();
        }

        return response.toString();
    }

    private class UpdateTripStatusTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                return sendUpdateTripStatusRequest(urls[0]);
            }
            catch (IOException e)
            {
                Log.e(TAG, "Error in UpdateTripStatusTask::doInBackground: " + e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            JSONArray distances = new JSONArray(), times = new JSONArray(), people = new JSONArray();
            ArrayList<String> list_of_attendees = new ArrayList<String>();
            try
            {
                JSONObject js = new JSONObject(result);
                distances = (JSONArray) js.get("distance_left");
                times = (JSONArray) js.get("time_left");
                people = (JSONArray) js.get("people");

                for (int i = 0; i < distances.length(); i++)
                {
                    DecimalFormat df = new DecimalFormat("#.####");
                    df.setRoundingMode(RoundingMode.CEILING);
                    Double distance = distances.getDouble(i);
                    int hours = times.getInt(i) / 60;
                    int minutes = times.getInt(i) - (hours * 60);
                    list_of_attendees.add(people.getString(i) + ": " + df.format(distance) + " miles away ("
                        + hours + " hours, " + minutes + " minutes left)");
                }
            }
            catch (JSONException e)
            {
                Log.e(TAG, "Error in UpdateTripStatusTask::onPostExecute: " + e.toString());
            }

            String list_to_display = "";

            for (String s : list_of_attendees)
            {
                list_to_display += "* " + s + "\n";
            }

            TextView tv = (TextView) findViewById(R.id.CURRENT_attendees_update_text);
            tv.setText(list_to_display);
        }
    }

    private class UpdateTripStatusTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            UpdateTripStatusTask utst = new UpdateTripStatusTask();
            utst.execute("http://cs9033-homework.appspot.com");
        }
    }
}
