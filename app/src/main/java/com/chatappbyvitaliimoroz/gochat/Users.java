package com.chatappbyvitaliimoroz.gochat;

/**
 * Created by Admin on 24.07.2017.
 */

public class Users {

    public String mName;
    public String mImage;
    public String mStatus;


    public Users (){}

    public Users(String mName, String mImage, String mStatus) {
        this.mName = mName;
        this.mImage = mImage;
        this.mStatus = mStatus;
    }

    public String getmName() {

        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }
}
