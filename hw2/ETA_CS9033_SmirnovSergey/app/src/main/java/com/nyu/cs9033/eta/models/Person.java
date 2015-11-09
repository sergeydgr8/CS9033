package com.nyu.cs9033.eta.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {
    
    // Member fields should exist here, what else do you need for a person?
    // Please add additional fields
    private String first_name;
    private String last_name;
    private String phone_number;

    /**
     * Parcelable creator. Do not modify this function.
     */
    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel p) {
            return new Person(p);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
    
    /**
     * Create a Person model object from a Parcel. This
     * function is called via the Parcelable creator.
     * 
     * @param p The Parcel used to populate the
     * Model fields.
     */
    public Person(Parcel p)
    {
        first_name = p.readString();
        last_name = p.readString();
        phone_number = p.readString();
        if (first_name == "" || first_name == null)
            first_name = "FirstName";
        if (last_name == "" || last_name == null)
            last_name = "LastName";
        if (phone_number == "" || phone_number == null)
            phone_number = "PhoneNumber";
    }
    
    /**
     * Create a Person model object from arguments
     * 
     * @param fn person's first name
     * @param ln person's last name (aka surname)
     * @param ph person's phone number
     *
     * Add arbitrary number of arguments to
     * instantiate Person class based on member variables.
     */
    public Person(String fn, String ln, String ph)
    {
        first_name = fn;
        last_name = ln;
        phone_number = ph;
    }

    /**
     * Serialize Person object by using writeToParcel.  
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
    public void writeToParcel(Parcel dest, int flags) {
        if (first_name == null)
            dest.writeString("FirstName");
        else
            dest.writeString(first_name);
        if (last_name == null)
            dest.writeString("LastName");
        else
            dest.writeString(last_name);
        if (phone_number == null)
            dest.writeString("PhoneNumber");
        else
            dest.writeString(phone_number);
    }
    
    /**
     * Feel free to add additional functions as necessary below.
     */

    /**
     * Gets the person's first name
     * @return the first name of the user
     */
    public String GetFirstName()
    {
        return first_name;
    }

    /**
     * Gets the last name (surname) of the person
     * @return the last name
     */
    public String GetLastName()
    {
        return last_name;
    }

    /**
     * Gets the phone number of the person
     * @return the phone number
     */
    public String GetPhoneNumber()
    {
        return phone_number;
    }

    /**
     * Changes the phone number for the person
     * @param ph the new phone number
     */
    public void ChangeNumber(String ph)
    {
        phone_number = ph;
    }
    
    /**
     * Do not implement
     */
    @Override
    public int describeContents() {
        // Do not implement!
        return 0;
    }
}
