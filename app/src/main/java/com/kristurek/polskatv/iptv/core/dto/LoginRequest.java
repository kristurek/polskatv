package com.kristurek.polskatv.iptv.core.dto;

public class LoginRequest {

    private String login;
    private String pass;

    public LoginRequest() {
    }

    public LoginRequest(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "login='" + login + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
