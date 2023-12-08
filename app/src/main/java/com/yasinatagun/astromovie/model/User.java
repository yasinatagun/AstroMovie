package com.yasinatagun.astromovie.model;

public class User {
    private String emailAdress;
    private String password;

    public User(String emailAdress, String password) {
        this.emailAdress = emailAdress;
        this.password = password;
    }

    public String getEmailAdress() {
        return emailAdress;
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAdress = emailAdress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
