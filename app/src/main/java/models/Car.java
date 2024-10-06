package models;

public class Car {
    public String _id;
    public String model;
    public Integer year;
    public double price;
    public String features;
    public String description;
    public String picture;

    public Car(String model,Integer year,double price,String features,String description,String picture){
        this.model=model;
        this.year=year;
        this.price=price;
        this.features=features;
        this.description=description;
        this.picture=picture;
    }
}
