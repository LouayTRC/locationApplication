package models;

import android.os.Parcel;
import android.os.Parcelable;

public class Role implements Parcelable {
    public String _id;
    public String name;

    public Role(String _id, String name) {
        this._id = _id;
        this.name = name;
    }

    protected Role(Parcel in) {
        _id = in.readString();
        name = in.readString();
    }

    public static final Creator<Role> CREATOR = new Creator<Role>() {
        @Override
        public Role createFromParcel(Parcel in) {
            return new Role(in);
        }

        @Override
        public Role[] newArray(int size) {
            return new Role[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(_id);
        parcel.writeString(name);
    }

    public String getName() {
        return name;
    }
}

