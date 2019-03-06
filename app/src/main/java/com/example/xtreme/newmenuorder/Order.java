package com.example.xtreme.newmenuorder;

public class Order {
    String username, itemname;
    public Order(){
    }
    public Order(String username,String itemname){
        this.itemname = itemname;
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public String getItemname() {
        return itemname;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setItemname(String itemname) {
        this.itemname = itemname;
    }
}
