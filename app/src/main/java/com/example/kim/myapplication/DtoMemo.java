package com.example.kim.myapplication;

/**
 * Created by Kim on 2017-06-07.
 */

public class DtoMemo {
    private int mno;
    private int uno;
    private String mtitle;
    private String mcontent;

    public DtoMemo(int mno, int uno, String mtitle, String mcontent) {
        this.mno = mno;
        this.uno = uno;
        this.mtitle = mtitle;
        this.mcontent = mcontent;
    }

    public int getMno() {
        return mno;
    }

    public void setMno(int mno) {
        this.mno = mno;
    }

    public int getUno() {
        return uno;
    }

    public void setUno(int uno) {
        this.uno = uno;
    }

    public String getMtitle() {
        return mtitle;
    }

    public void setMtitle(String mtitle) {
        this.mtitle = mtitle;
    }

    public String getMcontent() {
        return mcontent;
    }

    public void setMcontent(String mcontent) {
        this.mcontent = mcontent;
    }
}
