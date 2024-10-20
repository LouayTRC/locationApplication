package models.Requests;

public class AddCarRequest {
    public String model;
    public Integer year;
    public double price;
    public String description;
    public String features;
    public String picture;
    public String marque;
    public String category;

    public AddCarRequest(String model, Integer year, double price, String description,String features, String marque, String category,String pic) {
        this.model = model;
        this.year = year;
        this.price = price;
        this.description = description;
        this.features=features;
        this.marque = marque;
        this.category = category;
        picture=pic;
    }

    @Override
    public String toString() {
        return "AddCarRequest{" +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", picture='" + picture + '\'' +
                ", marque='" + marque + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
