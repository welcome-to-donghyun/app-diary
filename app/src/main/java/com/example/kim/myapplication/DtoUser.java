package com.example.kim.myapplication;

/**
 * Created by Kim on 2017-06-07.
 */

public class DtoUser {
    private int uno;
    private String uid;
    private String upw;
    private String uemail;

    public DtoUser(int uno, String uid, String upw, String uemail) {
        this.uno = uno;
        this.uid = uid;
        this.upw = upw;
        this.uemail = uemail;
    }

    public void setUno(int uno) {
        this.uno = uno;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUpw(String upw) {
        this.upw = upw;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public int getUno() {
        return uno;
    }

    public String getUid() {
        return uid;
    }

    public String getUpw() {
        return upw;
    }

    public String getUemail() {
        return uemail;
    }
}
