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
    private String date;
    private String destination;
    private ArrayList<Person> people;

    /**
     * Parcelable creator. Do not modify this function.
     */
    public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
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
        this.date = p.readString();
        this.destination = p.readString();
        //this.people = p.readArrayList(null);
        p.readTypedList(people, Person.CREATOR);
        //Person person = (Person) p.readValue(Person.class.getClassLoader());
        //Person person = p.readParcelable(Person.class.getClassLoader());

    }
    
    /**
     * Create a Trip model object from arguments
     * 
     * @param dat the date passed in
     * @param dest the destination for the trip
     * @param p the list of people
     * Add arbitrary number of arguments to
     * instantiate Trip class based on member variables.
     */
    public Trip(String dat, String dest, ArrayList<Person> p)
    {
        date = dat;
        destination = dest;
        people = p;
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
        dest.writeString(date);
        dest.writeString(destination);
        dest.writeTypedList(people);
        //dest.writeValue(people.get(0));
        //dest.writeParcelable(people.get(0), 0);
        //dest.writeTypedObject()
    }
    
    /**
     * Feel free to add additional functions as necessary below.
     */

    /**
     * Add a person to the trip after the trip has been created
     *
     * @param p the person in question
     */
    public void AddPersonToTrip(Person p)
    {
        people.add(p);
    }

    /**
     * Kick a person out from the trip
     *
     * @param p the person in question
     */
    public void RemovePersonFromTrip(Person p)
    {
        for (Person pp : people)
        {
            if (pp.GetFirstName() == p.GetFirstName()
                    && pp.GetLastName() == p.GetLastName()
                    && pp.GetPhoneNumber() == p.GetPhoneNumber())
                people.remove(pp);
        }
    }

    /**
     * Get the date & time of the trip
     * @return date the datetime
     */
    public String getDate()
    {
        return date;
    }

    /**
     * Get the destination of the trip
     * @return destination the destination
     */
    public String getDestination()
    {
        return destination;
    }

    /**
     * Get the people attending this event
     * @return people the attendees
     */
    public ArrayList<Person> getPeople()
    {
        return people;
    }

    public void setPeople(ArrayList<Person> p)
    {
        people = p;
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
