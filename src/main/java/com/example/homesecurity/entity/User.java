package com.example.homesecurity.entity;

public class User {
    private Integer id;
    private String username;
    private String password;
    private Boolean isAdministrator;


    public User(Integer id,String username, String password, Boolean isAdministrator) {
        this.id=id;
        this.username = username;
        this.password = password;
        this.isAdministrator = isAdministrator;

    }

    public Integer getId(){return id;}

    public void setId(Integer id){
        this.id=id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAdministrator() {
        return isAdministrator;
    }

    public void setAdministrator(Boolean administrator) {
        isAdministrator = administrator;
    }

}
