package com.developer.sarthak.updgeekverse;
import com.google.firebase.database.IgnoreExtraProperties;
/**
 * Created by ASUS on 21-04-2018.
 */
@IgnoreExtraProperties
public class User {
    String fname,lname,email,fandom, userId;
    User(){

    }
    User(String fname,String lname, String email, String fandom, String userId){
        this.fname=fname;
        this.lname=lname;
        this.email=email;
        this.fandom=fandom;
        this.userId=userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getFname() {

        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() {
        return email;
    }

    public String getFandom() {
        return fandom;
    }
}
