package com.example.administrator.julong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.julong.R;
import com.example.administrator.julong.entity.RukuEntity;
import com.example.administrator.julong.entity.WLEntity;

import java.util.List;

/**
 * Created by Administrator on 2017-03-20.
 */
public class WLApater4 extends BaseAdapter {

    private Context mContext = null;
    private List<RukuEntity> dataList;

    private LayoutInflater inflater;

    public WLApater4(Context context, List<RukuEntity> dataList) {
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

            view = inflater.inflate(R.layout.item_wl3, null);
            holder = new Holder();
            holder.tv1 = (TextView) view
                    .findViewById(R.id.tv1);



            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.tv1.setText(dataList.get(i).getFname());





        return view;
    }

    private class Holder {


        TextView tv1;
    }
}
