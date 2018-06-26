package com.example.administrator.julong.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-07-03.
 */
public class BiaotiEntity implements Serializable{
    /**数量*/
    private String FAuxQty;
    /**金额*/
    private String FAmount;
    /**品名*/
    private String FName;
    /**件数*/
    private int js=0;
    /**箱数*/
    private int xs=0;
    /**体积*/
    private double tj=0;
    /**编号*/
    private String bianhao;
    /**fnumber*/
    private String fnumber;
    /**规格*/
    private String fmodel;
    /**图片*/
    private String fimage1;
    /**物料内码*/
    private String finterid;
    /**备注*/
    private String fnote;
    /**单价*/
    private String FAuxPrice;
    /**材质*/
    private String fmaterial;
    /**单重*/
    private double fsingleweight=0;
    /**单位*/
    private String funit;

    public String getFnumber() {
        return fnumber;
    }

    public void setFnumber(String fnumber) {
        this.fnumber = fnumber;
    }

    public int getJs() {
        return js;
    }

    public void setJs(int js) {
        this.js = js;
    }

    public int getXs() {
        return xs;
    }

    public void setXs(int xs) {
        this.xs = xs;
    }

    public double getTj() {
        return tj;
    }

    public void setTj(double tj) {
        this.tj = tj;
    }

    public String getBianhao() {
        return bianhao;
    }

    public void setBianhao(String bianhao) {
        this.bianhao = bianhao;
    }

    public String getFinterid() {
        return finterid;
    }

    public void setFinterid(String finterid) {
        this.finterid = finterid;
    }

    public String getFnote() {
        return fnote;
    }

    public void setFnote(String fnote) {
        this.fnote = fnote;
    }

    public String getFAuxPrice() {
        return FAuxPrice;
    }

    public void setFAuxPrice(String FAuxPrice) {
        this.FAuxPrice = FAuxPrice;
    }

    public String getFAuxQty() {
        return FAuxQty;
    }

    public void setFAuxQty(String FAuxQty) {
        this.FAuxQty = FAuxQty;
    }

    public String getFAmount() {
        return FAmount;
    }

    public void setFAmount(String FAmount) {
        this.FAmount = FAmount;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getFmodel() {
        return fmodel;
    }

    public void setFmodel(String fmodel) {
        this.fmodel = fmodel;
    }

    public String getFimage1() {
        return fimage1;
    }

    public void setFimage1(String fimage1) {
        this.fimage1 = fimage1;
    }

    public String getFmaterial() {
        return fmaterial;
    }

    public void setFmaterial(String fmaterial) {
        this.fmaterial = fmaterial;
    }

    public double getFsingleweight() {
        return fsingleweight;
    }

    public void setFsingleweight(double fsingleweight) {
        this.fsingleweight = fsingleweight;
    }

    public String getFunit() {
        return funit;
    }

    public void setFunit(String funit) {
        this.funit = funit;
    }
}
