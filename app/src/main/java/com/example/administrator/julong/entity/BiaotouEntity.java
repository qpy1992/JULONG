package com.example.administrator.julong.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017-07-03.
 */
public class BiaotouEntity implements Serializable{
    private String FBillNo;
    private String FDate;
    private String gys;
    private String fbiaoji;
    private String fdate1;

    public String getFdate1() {
        return fdate1;
    }

    public void setFdate1(String fdate1) {
        this.fdate1 = fdate1;
    }

    public String getGysno() {
        return gysno;
    }

    public void setGysno(String gysno) {
        this.gysno = gysno;
    }

    private String gysno;
    private String mt,dz,sj,dh;

    public String getMt() {
        return mt;
    }

    public void setMt(String mt) {
        this.mt = mt;
    }

    public String getDz() {
        return dz;
    }

    public void setDz(String dz) {
        this.dz = dz;
    }

    public String getSj() {
        return sj;
    }

    public void setSj(String sj) {
        this.sj = sj;
    }

    public String getDh() {
        return dh;
    }

    public void setDh(String dh) {
        this.dh = dh;
    }

    private List<BiaotiEntity>bt;

    public List<BiaotiEntity> getBt() {
        return bt;
    }

    public void setBt(List<BiaotiEntity> bt) {
        this.bt = bt;
    }

    public String getGd() {
        return gd;
    }

    public void setGd(String gd) {
        this.gd = gd;
    }

    public String getGys() {
        return gys;
    }

    public void setGys(String gys) {
        this.gys = gys;
    }

    private String gd;

    public String getFInterID() {
        return FInterID;
    }

    public void setFInterID(String FInterID) {
        this.FInterID = FInterID;
    }

    private String FInterID;

    public String getFBillNo() {
        return FBillNo;
    }

    public void setFBillNo(String FBillNo) {
        this.FBillNo = FBillNo;
    }

    public String getFDate() {
        return FDate;
    }

    public void setFDate(String FDate) {
        this.FDate = FDate;
    }

    public String getFbiaoji() {
        return fbiaoji;
    }

    public void setFbiaoji(String fbiaoji) {
        this.fbiaoji = fbiaoji;
    }
}
