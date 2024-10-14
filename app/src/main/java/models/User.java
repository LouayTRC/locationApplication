package models;


public class User {
    private String cin;
    private String name;
    private String email;
    private String phone;

    public User(String cin, String name, String email, String phone) {
        this.cin = cin;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getters
    public String getCin() { return cin; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    @Override
    public String toString() {
        return "User{" +
                "cin='" + cin + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

