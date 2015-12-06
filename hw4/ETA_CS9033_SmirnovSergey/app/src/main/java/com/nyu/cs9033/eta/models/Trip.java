package com.nyu.cs9033.eta.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class Trip implements Parcelable
{
    
    // Member fields should exist here, what else do you need for a trip?
    // Please add additional fields
    private int trip_id;
    private String trip_name;
    private String destination;
    private String date;
    private ArrayList<Person> people;

    /**
     * Parcelable creator. Do not modify this function.
     */
    public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>()
    {
        public Trip createFromParcel(Parcel p) {
            return new Trip(p);
        }
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    public Trip()
    {
        people = new ArrayList<Person>();
    }
    
    /**
     * Create a Trip model object from a Parcel. This
     * function is called via the Parcelable creator.
     * 
     * @param p The Parcel used to populate the
     * Model fields.
     */
    public Trip(Parcel p)
    {
        this();
        readFromParcel(p);
    }

    public void readFromParcel(Parcel p)
    {
        this.trip_id = p.readInt();
        this.trip_name = p.readString();
        this.date = p.readString();
        this.destination = p.readString();
        p.readTypedList(people, Person.CREATOR);

    }
    
    /**
     * Create a Trip model object from arguments
     *
     * @param nm the name of the trip
     * @param dt the date passed in
     * @param dst the destination for the trip
     * @param p the list of people
     * @param id the ID from the database
     * Add arbitrary number of arguments to
     * instantiate Trip class based on member variables.
     */
    public Trip(String nm, String dt, String dst, ArrayList<Person> p, int id)
    {
        trip_name = nm;
        date = dt;
        destination = dst;
        people = p;
        trip_id = id;
    }

    public Trip(String nm, String dt, String dst, ArrayList<Person> p)
    {
        this(nm, dt, dst, p, -1);
    }

    /**
     * Serialize Trip object by using writeToParcel. 
     * This function is automatically called by the
     * system when the object is serialized.
     * 
     * @param dest Parcel object that gets written on 
     * serialization. Use functions to write out the
     * object stored via your member variables. 
     * 
     * @param flags Additional flags about how the object 
     * should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
     * In our case, you should be just passing 0.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(trip_id);
        dest.writeString(trip_name);
        dest.writeString(date);
        dest.writeString(destination);
        dest.writeTypedList(people);
    }
    
    /**
     * Feel free to add additional functions as necessary below.
     */

    /**
     * Add a person to the trip after the trip has been created
     *
     * @param p the person in question
     */
    public void AddPersonToTrip(Person p) { people.add(p); }

    /**
     * Get the name of the trip
     * @return the name of the trip
     */
    public String getTripName() { return trip_name; }

    /**
     * Get the date & time of the trip
     * @return date the datetime
     */
    public String getDate() { return date; }

    /**
     * Get the destination of the trip
     * @return destination the destination
     */
    public String getDestination() { return destination; }

    public int getTripID() { return trip_id; }

    /**
     * Get the people attending this event
     * @return people the attendees
     */
    public ArrayList<Person> getPeople()
    {
        return people;
    }

    public void ChangeID(int newID)
    {
        trip_id = newID;
    }
    
    /**
     * Do not implement
     */
    @Override
    public int describeContents()
    {
        // Do not implement!
        return 0;
    }
}
