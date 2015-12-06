package com.nyu.cs9033.eta.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {
    
    // Member fields should exist here, what else do you need for a person?
    // Please add additional fields
    private int user_id;
    private String person_name;
    private String phone_number;
    private String email_address;

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
        user_id = p.readInt();
        person_name = p.readString();
        phone_number = p.readString();
        email_address = p.readString();
    }

    /**
     * Create a Person model object from arguments
     * 
     * @param nm person's name
     * @param ph person's phone number
     * @param em person's email address
     * @param id the ID taken from the database
     */
    public Person(String nm, String ph, String em, int id)
    {
        person_name = nm;
        phone_number = ph;
        email_address = em;
        user_id = id;
    }

    public Person(String nm, String ph, String em)
    {
        this(nm, ph, em, -1);
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
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(user_id);
        if (person_name == null)
            dest.writeString("PersonName");
        else
            dest.writeString(person_name);
        if (phone_number == null)
            dest.writeString("PhoneNumber");
        else
            dest.writeString(phone_number);
        if (email_address == null)
            dest.writeString("EmailAddress");
        else
            dest.writeString(email_address);
    }
    
    /**
     * Feel free to add additional functions as necessary below.
     */

    /**
     * Gets the person's name
     * @return the person's name
     */
    public String getPersonName() { return person_name; }

    /**
     * Gets the phone number of the person
     * @return the phone number
     */
    public String getPhoneNumber()
    {
        return phone_number;
    }

    /**
     * Gets the person's email address
     * @return the email address
     */
    public String getEmailAddress() { return email_address; }

    public int getUserID() { return user_id; }

    /**
     * Changes the phone number for the person
     * @param ph the new phone number
     */
    public void ChangeNumber(String ph)
    {
        phone_number = ph;
    }

    /**
     * Changes the person's email address
     * @param em the new email address
     */
    public void ChangeEmail(String em) { email_address = em; }

    /**
     * Replaces the ID (for database use only)
     * @param id the new ID
     */
    public void ReplaceID(int id) { user_id = id; }
    
    /**
     * Do not implement
     */
    @Override
    public int describeContents() {
        // Do not implement!
        return 0;
    }
}
