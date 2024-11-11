package models;

public class Message {
    public User sender;
    public String description;

    // Constructor
    public Message(User sender, String description) {
        this.sender = sender;
        this.description = description;
    }

    // toString method
    @Override
    public String toString() {
        return "Message{" +
                "sender=" + sender + // Assumes User class has a meaningful toString()
                ", description='" + description + '\'' +
                '}';
    }
}
