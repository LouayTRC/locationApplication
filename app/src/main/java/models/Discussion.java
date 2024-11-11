package models;

import java.util.List;

public class Discussion {
    public String _id;
    public User user1;
    public User user2;
    public List<Message> messages;

    // Constructor
    public Discussion(User user1, User user2, List<Message> messages) {
        this.user1 = user1;
        this.user2 = user2;
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Discussion{" +
                "user1='" + user1 + '\'' +
                ", user2='" + user2 + '\'' +
                ", messages=" + messages +
                '}';
    }
}
