package models;

import android.os.Parcel;
import android.os.Parcelable;

public class Marque implements Parcelable {
    public String _id;  // Unique identifier for Marque
    public String name;  // Name of the marque

    // Constructor to initialize with name
    public Marque(String name) {
        this.name = name;
    }

    // Constructor to initialize with both name and id
    public Marque(String _id, String name) {
        this._id = _id;
        this.name = name;
    }

    // Parcelable implementation
    protected Marque(Parcel in) {
        _id = in.readString();  // Read _id from the Parcel
        name = in.readString(); // Read name from the Parcel
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);  // Write _id to the Parcel
        dest.writeString(name); // Write name to the Parcel
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Creator for the Parcelable interface
    public static final Creator<Marque> CREATOR = new Creator<Marque>() {
        @Override
        public Marque createFromParcel(Parcel in) {
            return new Marque(in);  // Create a Marque from the Parcel
        }

        @Override
        public Marque[] newArray(int size) {
            return new Marque[size];  // Create an array of Marque
        }
    };
}
