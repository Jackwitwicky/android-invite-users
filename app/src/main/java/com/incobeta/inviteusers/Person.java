package com.incobeta.inviteusers;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by witwicky on 16/02/18.
 */

public class Person implements Parcelable {

    private String name;
    private String phone;
    private Bitmap photo;
    private boolean selected;

    public Person(String name, String phone, Bitmap photo, boolean selected) {
        this.name = name;
        this.phone = phone;
        this.photo = photo;
        this.selected = selected;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeValue(this.photo);
        dest.writeByte(selected ? (byte) 1 : (byte) 0);
    }

    private Person(Parcel in) {
        this.name = in.readString();
        this.phone = in.readString();
        this.photo = (Bitmap) in.readValue(getClass().getClassLoader());
        this.selected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Person> CREATOR = new Creator<Person>() {
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
