package com.notadeveloper.app.partyyyadmin;

import java.util.ArrayList;

/**
 * Created by Chirag on 01-Aug-17.
 */

public class Club
{
    String clubpicture;
    String clubname;
    String time;
    String time1;
    String email;
    String number;
    String address1;
    String address2;
    String address3;
    String pin;
    String description;
    String menupicture;
    ArrayList<String> utils;

    public Club(String clubpicture, String clubname, String time, String time1, String email, String number, String address1, String address2, String address3, String pin, String description, String menupicture, ArrayList<String>utils) {
        this.clubpicture = clubpicture;
        this.clubname = clubname;
        this.time = time;
        this.time1 = time1;
        this.email = email;
        this.number = number;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.pin = pin;
        this.description = description;
        this.menupicture = menupicture;
        this.utils = utils;
    }

    public String getClubpicture() {
        return clubpicture;
    }

    public void setClubpicture(String clubpicture) {
        this.clubpicture = clubpicture;
    }

    public String getClubname() {
        return clubname;
    }

    public void setClubname(String clubname) {
        this.clubname = clubname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMenupicture() {
        return menupicture;
    }

    public void setMenupicture(String menupicture) {
        this.menupicture = menupicture;
    }

    public ArrayList<String> getUtils() {
        return utils;
    }

    public void setUtils(ArrayList<String> utils) {
        this.utils = utils;
    }
}
