package models;

public class Client {
    public String _id; // Client ID from MongoDB
    public User user; // User ID that the client references

    public Client(User user) {
        this.user = user;
    }



}
