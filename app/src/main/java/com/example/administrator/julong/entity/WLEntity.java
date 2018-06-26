package com.example.administrator.julong.entity;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017-06-16.
 */
public class WLEntity {
    /**ID*/
    private String id;
    /**品名*/
    private String fname;
    /**编号*/
    private String bianhao;
    /**地址*/
    private String dz;
    /**电话*/
    private String dh;
    /**仓库ID*/
    private String fstockid;
    /**物料ID*/
    private String fitemid;
    /**联系人*/
    private String lxr;
    /**图片bitmap*/
    private Bitmap bitmap;
    /**备注*/
    private String beizhu;
    /**fnumber*/
    private String fnumber;
    /**规格*/
    private String gg;
    /**件数*/
    private int js=0;
    /**箱数*/
    private int xs=0;
    /**总数*/
    private int zs=0;
    /**单价*/
    private double dj=0;
    /**总价*/
    private double zj=0;
    /**体积*/
    private double tiji=0;
    /**图片*/
    private String img;
    /**材质*/
    private String fmaterial;
    /**单重*/
    private double fsingleweight;

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.lxr = lxr;
    }

    public String getDz() {
        return dz;
    }

    public void setDz(String dz) {
        this.dz = dz;
    }

    public String getDh() {
        return dh;
    }

    public void setDh(String dh) {
        this.dh = dh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getBianhao() {
        return bianhao;
    }

    public void setBianhao(String bianhao) {
        this.bianhao = bianhao;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public double getTiji() {
        return tiji;
    }

    public void setTiji(double tiji) {
        this.tiji = tiji;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
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

    public int getZs() {
        return zs;
    }

    public void setZs(int zs) {
        this.zs = zs;
    }

    public double getDj() {
        return dj;
    }

    public void setDj(double dj) {
        this.dj = dj;
    }

    public double getZj() {
        return zj;
    }

    public void setZj(double zj) {
        this.zj = zj;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getFnumber() {
        return fnumber;
    }

    public void setFnumber(String fnumber) {
        this.fnumber = fnumber;
    }

    public String getFname() {return fname;}

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFstockid() {
        return fstockid;
    }

    public void setFstockid(String fstockid) {
        this.fstockid = fstockid;
    }

    public String getFitemid() {
        return fitemid;
    }

    public void setFitemid(String fitemid) {
        this.fitemid = fitemid;
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
}
