package models;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {
    public String name;

    public Category(String name) {
        this.name = name;
    }

    // Parcelable implementation
    protected Category(Parcel in) {
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
