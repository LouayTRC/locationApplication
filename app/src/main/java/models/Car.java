package models;

public class Car {
    public String _id;
    public String model;
    public Integer year;
    public double price;
    public String features;
    public String description;
    public String picture;
    public Marque marque;
    public Category category;

    public Car(String model,Integer year,double price,String features,String description,String picture,Marque marque,Category category){
        this.model=model;
        this.year=year;
        this.price=price;
        this.features=features;
        this.description=description;
        this.picture=picture;
        this.marque=marque;
        this.category=category;
    }
    public String getId() {
        return _id;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return model;
    }

    public double getPrice() {
        return price;
    }
}
