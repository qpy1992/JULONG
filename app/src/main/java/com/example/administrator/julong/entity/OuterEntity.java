package com.example.administrator.julong.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/10/10 0010.
 * 出库主表
 */

public class OuterEntity implements Serializable {
    /**出库单号*/
    private String fbillno;
    /**客户名称*/
    private String fname;
    /**出库日期*/
    private String fdate;
    /**发货仓库*/
    private String fstock;
    /**增改标记*/
    private String billno;
    /**interid*/
    private String FInterId;
    /**客户number*/
    private String fsupplynumber;
    /**仓库number*/
    private String fstocknumber;
    /**审核状态*/
    private String fcheckerid;
    /**出库子表*/
    private List<OuterentryEntity> outerentry;

    public List<OuterentryEntity> getOuterentry() {
        return outerentry;
    }

    public void setOuterentry(List<OuterentryEntity> outerentry) {
        this.outerentry = outerentry;
    }

    public String getFbillno() {
        return fbillno;
    }

    public void setFbillno(String fbillno) {
        this.fbillno = fbillno;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFdate() {
        return fdate;
    }

    public void setFdate(String fdate) {
        this.fdate = fdate;
    }

    public String getFstock() {
        return fstock;
    }

    public void setFstock(String fstock) {
        this.fstock = fstock;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getFInterId() {
        return FInterId;
    }

    public void setFInterId(String FInterId) {
        this.FInterId = FInterId;
    }

    public String getFsupplynumber() {
        return fsupplynumber;
    }

    public void setFsupplynumber(String fsupplynumber) {
        this.fsupplynumber = fsupplynumber;
    }

    public String getFstocknumber() {
        return fstocknumber;
    }

    public void setFstocknumber(String fstocknumber) {
        this.fstocknumber = fstocknumber;
    }

    public String getFcheckerid() {
        return fcheckerid;
    }

    public void setFcheckerid(String fcheckerid) {
        this.fcheckerid = fcheckerid;
    }
}
