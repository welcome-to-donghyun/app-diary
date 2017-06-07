package com.example.kim.myapplication;

/**
 * Created by Kim on 2017-06-07.
 */

public class DtoDiary {
    private int dno;
    private int uno;
    private String dtitle;
    private String dcontent;
    private String dimg;
    private String ddate;


    public DtoDiary(int dno, int uno, String dtitle, String dcontent, String dimg, String ddate) {
        this.dno = dno;
        this.uno = uno;
        this.dtitle = dtitle;
        this.dcontent = dcontent;
        this.dimg = dimg;
        this.ddate = ddate;
    }

    public String getDdate() {
        return ddate;
    }

    public void setDdate(String ddate) {
        this.ddate = ddate;
    }

    public int getDno() {
        return dno;
    }

    public void setDno(int dno) {
        this.dno = dno;
    }

    public int getUno() {
        return uno;
    }

    public void setUno(int uno) {
        this.uno = uno;
    }

    public String getDtitle() {
        return dtitle;
    }

    public void setDtitle(String dtitle) {
        this.dtitle = dtitle;
    }

    public String getDcontent() {
        return dcontent;
    }

    public void setDcontent(String dcontent) {
        this.dcontent = dcontent;
    }

    public String getDimg() {
        return dimg;
    }

    public void setDimg(String dimg) {
        this.dimg = dimg;
    }
}
