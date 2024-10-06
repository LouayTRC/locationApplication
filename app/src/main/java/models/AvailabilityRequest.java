package models;

public class AvailabilityRequest {
    public String startDate;
    public String endDate;

    public AvailabilityRequest(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}

