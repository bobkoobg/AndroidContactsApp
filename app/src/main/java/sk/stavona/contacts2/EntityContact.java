package sk.stavona.contacts2;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class EntityContact implements Parcelable {

    String display_name;
    String number;
    String _id;
    Uri picPath;

    public EntityContact(String display_name, String number, String _id, Uri picPath) {
        this.display_name = display_name;
        this.number = number;
        this._id = _id;
        this.picPath = picPath;
    }

    public Uri getPicPath() {
        return picPath;
    }

    public void setPicPath(Uri picPath) {
        this.picPath = picPath;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }


    // no need for URI getters and setter they are here because we might need it later
    protected EntityContact(Parcel in) {
        display_name = in.readString();
        number = in.readString();
        _id = in.readString();
        picPath = (Uri) in.readValue(Uri.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(display_name);
        dest.writeString(number);
        dest.writeString(_id);
        dest.writeValue(picPath);
    }

@SuppressWarnings("unused")
public static final Parcelable.Creator<EntityContact> CREATOR = new Parcelable.Creator<EntityContact>() {
    @Override
    public EntityContact createFromParcel(Parcel in) {
        return new EntityContact(in);
    }

    @Override
    public EntityContact[] newArray(int size) {
        return new EntityContact[size];
    }
};
}