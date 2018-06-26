package com.example.administrator.julong.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/10 0010.
 */

public class OuterentryEntity  implements Serializable {
    private String fname;
    private String fqty;
    private String fnote;
    private String fimage1;
    private String fmodel;
    private String finterid;
    private String fnumber;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFqty() {
        return fqty;
    }

    public void setFqty(String fqty) {
        this.fqty = fqty;
    }

    public String getFnote() {
        return fnote;
    }

    public void setFnote(String fnote) {
        this.fnote = fnote;
    }

    public String getFimage1() {
        return fimage1;
    }

    public void setFimage1(String fimage1) {
        this.fimage1 = fimage1;
    }

    public String getFmodel() {
        return fmodel;
    }

    public void setFmodel(String fmodel) {
        this.fmodel = fmodel;
    }

    public String getFinterid() {
        return finterid;
    }

    public void setFinterid(String finterid) {
        this.finterid = finterid;
    }

    public String getFnumber() {
        return fnumber;
    }

    public void setFnumber(String fnumber) {
        this.fnumber = fnumber;
    }
}
