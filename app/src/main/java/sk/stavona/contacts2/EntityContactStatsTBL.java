package sk.stavona.contacts2;

public class EntityContactStatsTBL {
    String id;
    String number;
    String duration;
    String countOfSms;
    String picPath;

    public EntityContactStatsTBL(String id, String number, String duration, String countOfSms, String picPath) {
        this.id = id;
        this.number = number;
        this.duration = duration;
        this.countOfSms = countOfSms;
        this.picPath = picPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCountOfSms() {
        return countOfSms;
    }

    public void setCountOfSms(String countOfSms) {
        this.countOfSms = countOfSms;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
