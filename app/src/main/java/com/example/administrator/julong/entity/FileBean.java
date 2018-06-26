package com.example.administrator.julong.entity;


/**
 * Created by Administrator on 2017/11/14 0014.
 */

public class FileBean {
    @TreeNodeId
    private int _id;
    @TreeNodePid
    private int parentId;
    @TreeNodeLabel
    private String name;
    @TreeNodeFimage
    private String fimage;
    @TreeNodeFnumber
    private String fnumber;
    @TreeNodeFmodel
    private String fmodel;

    public FileBean(int _id, int parentId, String name, String fimage, String fnumber, String fmodel)
    {
        super();
        this._id = _id;
        this.parentId = parentId;
        this.name = name;
        this.fimage = fimage;
        this.fnumber = fnumber;
        this.fmodel = fmodel;
    }
}
