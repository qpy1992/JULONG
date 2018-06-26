package com.example.administrator.julong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.julong.R;
import com.example.administrator.julong.entity.MainMenuEntity;


import java.util.List;

/**
 * Created by Administrator on 2017-03-20.
 */
public class GridviewApater1 extends BaseAdapter {

    private Context mContext = null;
    private List<MainMenuEntity> dataList;

    private LayoutInflater inflater;

    public GridviewApater1(Context context, List<MainMenuEntity> dataList) {
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

            view = inflater.inflate(R.layout.item_gridview, null);
            holder = new Holder();
            holder.linearLayout = (LinearLayout) view
                    .findViewById(R.id.mydada_item_ll);
            holder.icon_img = (ImageView) view
                    .findViewById(R.id.mydada_icon_img);
            holder.text_tv = (TextView) view.findViewById(R.id.mydada_text_tv);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.icon_img.setImageResource(dataList.get(i).getResId());
        holder.text_tv.setText(dataList.get(i).getText());

        WindowManager wm = (WindowManager) mContext.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = (wm.getDefaultDisplay().getWidth() - 2) / 3;
        int height = width;
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(width,
                height);
        holder.linearLayout.setLayoutParams(params);

        return view;
    }

    private class Holder {

        LinearLayout linearLayout;
        ImageView icon_img;
        TextView text_tv;
    }
}
