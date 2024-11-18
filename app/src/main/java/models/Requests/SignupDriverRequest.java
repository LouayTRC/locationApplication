package models.Requests;

public class SignupDriverRequest {
    public SignupClientRequest user;
    public String region;
    public Number priceByDay;
    public String genre;

    public SignupDriverRequest(SignupClientRequest signupClientReq,String region,Number price,String genre) {
        user=signupClientReq;
        this.region=region;
        this.priceByDay=price;
        this.genre=genre;
    }
}
