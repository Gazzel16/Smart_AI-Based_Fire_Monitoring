package com.example.smartai_basedfiremonitoring.Model;

public class User {
    public String id;
    public String username;

    public String email;
    public String age;

    public String gender;
    public String role;

    public User(){

    }

    public User(String id,String username, String email, String age, String gender, String role){
        this.id = id;
        this.username = username;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.role = role;
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


    //getter&setter for email
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getAge(){
        return age;
    }
    public void setAge(String age){
        this.age = age;
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getGender(){
        return gender;
    }

    public void setGender(String gender){
        this.gender = gender;
    }


}
