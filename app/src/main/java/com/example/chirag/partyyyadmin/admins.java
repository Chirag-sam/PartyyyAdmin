package com.example.chirag.partyyyadmin;

import java.util.HashMap;

/**
 * Created by Chirag on 14-Jul-17.
 */

public class admins {

    static HashMap<String,String>admins;

    public admins(HashMap<String, String> admins) {
        this.admins = admins;
    }

    public HashMap<String, String> getAdmins() {
        return admins;
    }

    public void setAdmins(HashMap<String, String> admins) {
        this.admins = admins;
    }
}
