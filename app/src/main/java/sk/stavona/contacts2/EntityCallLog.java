package sk.stavona.contacts2;

public class EntityCallLog {
    private String number;
    private String type;
    private String date;
    private String duration;

    public EntityCallLog(String number, String type, String date, String duration) {
        this.number = number;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}

