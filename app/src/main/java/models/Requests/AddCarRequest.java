package models.Requests;

public class AddCarRequest {
    public String _id;
    public String model;
    public Integer year;
    public double price;
    public String features;
    public String description;
    public String picture;
    public String marque;
    public String category;

    public AddCarRequest(String model, Integer year, double price, String features, String description, String marque, String category, String picture){
        this.model=model;
        this.year=year;
        this.price=price;
        this.features=features;
        this.description=description;
        this.marque=marque;
        this.category=category;
        this.picture=picture;
    }
}
