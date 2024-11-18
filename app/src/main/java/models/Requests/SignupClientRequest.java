package models.Requests;

public class SignupClientRequest {
    private String cin;
    private String phone;
    private String email;
    private String password;
    private String name;
    private String role;

    public SignupClientRequest(String cin, String phone, String email, String password, String name, String role) {
        this.cin = cin;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }
}
