package models;

public class Car {
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

    public Car(String model,Integer year,double price,String features,String description,String picture,Marque marque,Category category){
        this.model=model;
        this.year=year;
        this.price=price;
        this.description=description;
        this.features=features;
        this.picture=picture;
        this.marque=marque;
        this.category=category;
    }
}
