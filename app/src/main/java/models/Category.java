package models;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {
    public String _id;  // Unique identifier for Category
    public String name;  // Name of the category

    // Constructor to initialize with name
    public Category(String name) {
        this.name = name;
    }

    // Constructor to initialize with both _id and name
    public Category(String _id, String name) {
        this._id = _id;
        this.name = name;
    }

    // Parcelable implementation
    protected Category(Parcel in) {
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
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);  // Create a Category from the Parcel
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];  // Create an array of Category
        }
    };

}
