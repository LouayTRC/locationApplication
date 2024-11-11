package models.Requests;

public class SendMessageRequest {
    public String source;
    public String message;

    public SendMessageRequest(String source,String message){
        this.source=source;
        this.message=message;
    }
}
