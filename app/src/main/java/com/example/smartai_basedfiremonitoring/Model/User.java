package com.example.smartai_basedfiremonitoring.Model;

public class User {
    public String id;
    public String username;
    public  String phoneNumber;

    public String email;

    public User(){

    }

    public User(String id,String username,String phoneNumber, String email){
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    //getter&setter for id
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    //getter&setter for username
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    //getter&setter for phoneNumber
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    //getter&setter for email
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }



}
