package websocket;

public class Bed {
    private int bedNumber;
    private String patientName;
    private String status;
    private String statusImagePath;
    private boolean availability; 
    private String availabilityImage;

    public int getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(int bedNumber) {
        this.bedNumber = bedNumber;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getStatusImagePath() {
        return statusImagePath;
    }

    public void setStatusImagePath(String statusImagePath) {
        this.statusImagePath = statusImagePath;
    }
    
    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
        // Set the image path based on availability
        this.availabilityImage = availability ? "images/checkmark.png" : "images/crossmark.png";
    }

    public String getAvailabilityImage() {
        return availabilityImage;
    }
}
