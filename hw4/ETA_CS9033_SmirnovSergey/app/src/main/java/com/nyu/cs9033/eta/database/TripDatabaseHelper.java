package com.nyu.cs9033.eta.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;

import java.util.ArrayList;

public class TripDatabaseHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ETA_db";

    // trips table
    private static final String TRIPS_TABLE_NAME = "trips";
    private static final String TRIP_ID = "id";
    private static final String TRIP_NAME = "name";
    private static final String TRIP_DEST = "destination";
    private static final String TRIP_LAT = "latitude";
    private static final String TRIP_LON = "longitude";
    private static final String TRIP_DATE = "date";
    private static final String CURRENT_TRIP = "current";

    // people table
    private static final String PEOPLE_TABLE_NAME = "people";
    private static final String PERSON_ID = "id";
    private static final String PERSON_NAME = "name";
    private static final String PERSON_PHONE = "phone";
    private static final String PERSON_EMAIL = "email";

    // people - trip relation table
    private static final String PEOPLE_TRIP_TABLE_NAME = "people_and_trips";
    private static final String PT_ID = "id";
    private static final String PT_PERSON_ID = "person_id";
    private static final String PT_TRIP_ID = "trip_id";

    private String trips_columns[] = {
            TRIPS_TABLE_NAME,
            TRIP_ID,
            TRIP_NAME,
            TRIP_DEST,
            TRIP_DATE,
            CURRENT_TRIP
    };
    private String people_columns[] = {
            PEOPLE_TABLE_NAME,
            PERSON_ID,
            PERSON_NAME,
            PERSON_PHONE,
            PERSON_EMAIL
    };
    private String people_trips_columns[] = {
            PEOPLE_TRIP_TABLE_NAME,
            PT_ID,
            PT_PERSON_ID,
            PT_TRIP_ID
    };

    private static final String TAG = "TRIPDATABASEHELPER";

    public TripDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            db.execSQL("CREATE TABLE " + TRIPS_TABLE_NAME + "("
                //+ TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TRIP_ID + " LONG PRIMARY KEY, "
                + TRIP_NAME + " VARCHAR(255), "
                + TRIP_DEST + " VARCHAR(255), "
                + TRIP_LAT + " REAL, "
                + TRIP_LON + " REAL, "
                + TRIP_DATE + " LONG, "
                + CURRENT_TRIP + " INTEGER)");

            db.execSQL("CREATE TABLE " + PEOPLE_TABLE_NAME + "("
                + PERSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PERSON_NAME + " VARCHAR(255), "
                + PERSON_PHONE + " VARCHAR(255), "
                + PERSON_EMAIL + " VARCHAR(255))");

            db.execSQL("CREATE TABLE " + PEOPLE_TRIP_TABLE_NAME + "("
                + PT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PT_PERSON_ID + " INTEGER, "
                + PT_TRIP_ID + " LONG)");
        }
        catch (SQLException e)
        {
            Log.i(TAG, "Exception in onCreate: " + e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try
        {
            db.execSQL("DROP TABLE IF EXISTS " + TRIPS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + PEOPLE_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + PEOPLE_TRIP_TABLE_NAME);
            onCreate(db);
        }
        catch (SQLException e)
        {
            Log.i(TAG, "Exception in onUpgrade: " + e.toString());
        }
    }

    /**
     * Inserts a trip into the database
     * @param trip the trip to be inserted
     * @return the unique ID in the database
     */
    public long InsertTripAndGetID(Trip trip)
    {
        try
        {
            ContentValues cv = new ContentValues();
            cv.put(TRIP_NAME, trip.getTripName());
            cv.put(TRIP_DATE, trip.getDate());
            cv.put(TRIP_LAT, trip.getLatitude());
            cv.put(TRIP_LON, trip.getLongitude());
            cv.put(TRIP_DEST, trip.getDestination());
            cv.put(CURRENT_TRIP, 0);
            return getWritableDatabase().insert(TRIPS_TABLE_NAME, null, cv);
            //return newID;
        }
        catch (Exception e)
        {
            Log.i(TAG, "Exception in InsertTrip: " + e.toString());
        }
        return -1;
    }

    public void InsertTrip(Trip trip)
    {
        try
        {
            ContentValues cv = new ContentValues();
            cv.put(TRIP_ID, trip.getTripID());
            cv.put(TRIP_NAME, trip.getTripName());
            cv.put(TRIP_DATE, trip.getDate());
            cv.put(TRIP_LAT, trip.getLatitude());
            cv.put(TRIP_LON, trip.getLongitude());
            cv.put(TRIP_DEST, trip.getDestination());
            cv.put(CURRENT_TRIP, 0);
            long temp = getWritableDatabase().insert(TRIPS_TABLE_NAME, null, cv);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Exception in InsertTrip: " + e.toString());
        }
    }

    /**
     * Inserts a person into the database
     * @param person the person to be inserted
     * @return the unique ID in the database
     */
    public int InsertPerson(Person person)
    {
        try
        {
            ContentValues cv = new ContentValues();
            cv.put(PERSON_NAME, person.getPersonName());
            cv.put(PERSON_PHONE, person.getPhoneNumber());
            cv.put(PERSON_EMAIL, person.getEmailAddress());
            long newID = getWritableDatabase().insert(PEOPLE_TABLE_NAME, null, cv);
            return (int) newID;
        }
        catch (Exception e)
        {
            Log.i(TAG, "Exception in InsertPerson: " + e.toString());
        }
        return -1;
    }

    public void PopulateTrips(Trip trip)
    {
        try
        {
            if (trip.getTripID() == -1)
                trip.ChangeID(InsertTripAndGetID(trip));
            else InsertTrip(trip);
            for (Person p : trip.getPeople())
            {
                if (p.getUserID() == -1)
                    p.ReplaceID(InsertPerson(p));
                ContentValues cv = new ContentValues();
                cv.put(PT_TRIP_ID, trip.getTripID());
                cv.put(PT_PERSON_ID, p.getUserID());
                long newID = getWritableDatabase().insert(PEOPLE_TRIP_TABLE_NAME, null, cv);
            }
        }
        catch (Exception e)
        {
            Log.i(TAG, "Exception in PopulateTrips: " + e.toString());
        }
    }

    public Trip GetTrip(long id)
    {
        //Trip trip = new Trip();
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TRIPS_TABLE_NAME + " WHERE " +
                TRIP_ID + "=" + id, null);
            cursor.moveToFirst();
            Trip t = new Trip(cursor.getString(1), cursor.getLong(5), cursor.getDouble(3),
                    cursor.getDouble(4), cursor.getString(2), GetAttendees(id), id);
            if (cursor.getInt(6) == 1)
                t.setAsCurrentTrip();
            return t;
        }
        catch (SQLException e)
        {
            Log.i(TAG, "Exception in GetTrip: " + e.toString());
            return null;
        }
    }

    public ArrayList<Trip> GetAllTrips()
    {
        ArrayList<Trip> alltrips = new ArrayList<Trip>();

        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TRIPS_TABLE_NAME, null);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            {
                ArrayList<Person> people = GetAttendees(cursor.getInt(0));
                Trip trip_to_add = new Trip(cursor.getString(1), cursor.getLong(5), cursor.getDouble(3),
                        cursor.getDouble(4), cursor.getString(2), people, cursor.getLong(0));
                if (cursor.getInt(6) == 1)
                    trip_to_add.setAsCurrentTrip();
                alltrips.add(trip_to_add);
            }
            cursor.close();
        }
        catch (SQLException e)
        {
            Log.i(TAG, "Exception in GetAllTrips: " + e.toString());
        }

        return alltrips;
    }

    public ArrayList<Person> GetAttendees(long trip_id)
    {
        ArrayList<Person> attendees = new ArrayList<Person>();
        ArrayList<Integer> attendee_ids = new ArrayList<Integer>();

        try
        {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor tp_cursor = db.rawQuery("SELECT * FROM " + PEOPLE_TRIP_TABLE_NAME
                    + " WHERE " + PT_TRIP_ID + "=" + trip_id, null);
            for (tp_cursor.moveToFirst(); !tp_cursor.isAfterLast(); tp_cursor.moveToNext())
                attendee_ids.add((Integer) tp_cursor.getInt(1));
            tp_cursor.close();

            Cursor people_cursor;
            for (Integer i : attendee_ids)
            {
                people_cursor = db.rawQuery("SELECT * FROM " + PEOPLE_TABLE_NAME + " WHERE "
                        + PERSON_ID + "=" + i, null);
                if (people_cursor.moveToFirst())
                    attendees.add(new Person(people_cursor.getString(1), people_cursor.getString(2),
                            people_cursor.getString(3), people_cursor.getInt(0)));
            }
        }
        catch (SQLException e)
        {
            Log.i(TAG, "Exception in GetAttendees: " + e.toString());
        }

        return attendees;
    }
}
