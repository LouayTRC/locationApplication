package models;

import android.os.Parcel;
import android.os.Parcelable;

public class Car implements Parcelable {
    public String _id;
    public String model;
    public Integer year;
    public double price;
    public String description;
    public String features;
    public String picture;
    public Marque marque;
    public Category category;
    public Integer status;

    public Car(String model, Integer year, double price, String features, String description, String picture, Marque marque, Category category) {
        this.model = model;
        this.year = year;
        this.price = price;
        this.description = description;
        this.features = features;
        this.picture = picture;
        this.marque = marque;
        this.category = category;
    }

    // Parcelable implementation
    protected Car(Parcel in) {
        _id = in.readString();
        model = in.readString();
        year = (Integer) in.readValue(Integer.class.getClassLoader());
        price = in.readDouble();
        description = in.readString();
        features = in.readString();
        picture = in.readString();
        marque = in.readParcelable(Marque.class.getClassLoader());
        category = in.readParcelable(Category.class.getClassLoader());
        status = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(model);
        dest.writeValue(year);
        dest.writeDouble(price);
        dest.writeString(description);
        dest.writeString(features);
        dest.writeString(picture);
        dest.writeParcelable(marque, flags);
        dest.writeParcelable(category, flags);
        dest.writeValue(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    // toString method to return a string representation of the Car object
    @Override
    public String toString() {
        return "Car{" +
                "_id='" + _id + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", features='" + features + '\'' +
                ", picture='" + picture + '\'' +
                ", marque=" + (marque != null ? marque.name : "null") +
                ", category=" + (category != null ? category.name : "null") +
                ", status=" + status +
                '}';
    }
}
