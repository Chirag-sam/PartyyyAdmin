package com.notadeveloper.app.partyyyadmin;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Chirag on 04-Jul-17.
 * User Model For FireBase
 */

public class Users {
  private String uid;
  private String name;
  private String number;
  private String email;
  private Boolean isorganizer;
  private String orgname;
  private ArrayList<String> myparties = new ArrayList<>();
  private HashMap<String, Party.BookedTickets> mytickets = new HashMap<>();
  private Club myclub;

  public Users() {
  }

  public Users(String uid, String name, String number, String email) {
    this.uid = uid;
    this.name = name;
    this.number = number;
    this.email = email;
  }

  public Club getMyclub() {
    return myclub;
  }

  public void setMyclub(Club myclub) {
    this.myclub = myclub;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public HashMap<String, Party.BookedTickets> getMytickets() {
    return mytickets;
  }

  public void setMytickets(HashMap<String, Party.BookedTickets> mytickets) {
    this.mytickets = mytickets;
  }

  public Boolean getIsorganizer() {
    return isorganizer;
  }

  public void setIsorganizer(Boolean isorganizer) {
    this.isorganizer = isorganizer;
  }

  public ArrayList<String> getMyparties() {
    return myparties;
  }

  public void setMyparties(ArrayList<String> myparties) {
    this.myparties = myparties;
  }

  public String getOrgname() {
    return orgname;
  }

  public void setOrgname(String orgname) {
    this.orgname = orgname;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String phone) {
    this.number = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public static class sheeshaorders {
    String orderid;
    String orderdate;
    String amount;
    String status;
    ArrayList<String> flavours;
    String deliverby;
    String add1;
    String add2;
    String addpin;
    String phone;
    String name;
    String noofpots;

    public sheeshaorders() {

    }

    public sheeshaorders(String orderid, String orderdate, String amount, String status,
        ArrayList<String> flavours, String deliverby, String add1, String add2, String addpin,
        String phone, String name, String noofpots) {
      this.orderid = orderid;
      this.orderdate = orderdate;
      this.amount = amount;
      this.status = status;
      this.flavours = flavours;
      this.deliverby = deliverby;
      this.add1 = add1;
      this.add2 = add2;
      this.addpin = addpin;
      this.phone = phone;
      this.name = name;
      this.noofpots = noofpots;
    }

    public String getNoofpots() {
      return noofpots;
    }

    public void setNoofpots(String noofpots) {
      this.noofpots = noofpots;
    }

    public String getOrderid() {
      return orderid;
    }

    public void setOrderid(String orderid) {
      this.orderid = orderid;
    }

    public String getOrderdate() {
      return orderdate;
    }

    public void setOrderdate(String orderdate) {
      this.orderdate = orderdate;
    }

    public String getAmount() {
      return amount;
    }

    public void setAmount(String amount) {
      this.amount = amount;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public ArrayList<String> getFlavours() {
      return flavours;
    }

    public void setFlavours(ArrayList<String> flavours) {
      this.flavours = flavours;
    }

    public String getDeliverby() {
      return deliverby;
    }

    public void setDeliverby(String deliverby) {
      this.deliverby = deliverby;
    }

    public String getAdd1() {
      return add1;
    }

    public void setAdd1(String add1) {
      this.add1 = add1;
    }

    public String getAdd2() {
      return add2;
    }

    public void setAdd2(String add2) {
      this.add2 = add2;
    }

    public String getAddpin() {
      return addpin;
    }

    public void setAddpin(String addpin) {
      this.addpin = addpin;
    }

    public String getPhone() {
      return phone;
    }

    public void setPhone(String phone) {
      this.phone = phone;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}

