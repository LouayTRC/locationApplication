package models;

public class Client {
    public String _id; // Client ID from MongoDB
    public String user; // User ID that the client references

    public Client(String _id, String user) {
        this._id = _id;
        this.user = user;
    }

}
