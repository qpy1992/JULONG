package com.example.administrator.julong.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.julong.R;
import com.example.administrator.julong.entity.WLEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017-03-20.
 */
public class GYSApater extends BaseAdapter {

    private Context mContext = null;
    private List<WLEntity> dataList;

    private LayoutInflater inflater;

    public GYSApater(Context context, List<WLEntity> dataList) {
        this.mContext = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {

            view = inflater.inflate(R.layout.item_wl, null);
            holder = new Holder();
            holder.tv1 = (TextView) view
                    .findViewById(R.id.tv1);
            holder.tv2 = (TextView) view
                    .findViewById(R.id.tv2);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.tv1.setText(dataList.get(i).getFnumber());
        holder.tv2.setText(dataList.get(i).getFname());


       // holder.iv.setImageBitmap(base64ToBitmap(dataList.get(i).getImg()));

       //"data:image/jpeg;base64,"+
        return view;
    }

    private class Holder {


        TextView tv1,tv2;

    }



}
