package models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    public String _id;
    public String cin;
    public String name;
    public String email;
    public String phone;
    public String password;
    public Role role;
    public Integer status;

    public User(String cin, String name, String email, String phone, Role role) {
        this.cin = cin;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    protected User(Parcel in) {
        _id = in.readString();
        cin = in.readString();
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        password = in.readString();
        role = in.readParcelable(Role.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(cin);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(password);
        dest.writeParcelable(role, flags);
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", cin='" + cin + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", role=" + (role != null ? role.toString() : "null") +
                '}';
    }
}
