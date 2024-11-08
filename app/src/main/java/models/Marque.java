package models;

import android.os.Parcel;
import android.os.Parcelable;

public class Marque implements Parcelable {
    public String name;

    public Marque(String name) {
        this.name = name;
    }

    // Parcelable implementation
    protected Marque(Parcel in) {
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

    public static final Creator<Marque> CREATOR = new Creator<Marque>() {
        @Override
        public Marque createFromParcel(Parcel in) {
            return new Marque(in);
        }

        @Override
        public Marque[] newArray(int size) {
            return new Marque[size];
        }
    };
}
