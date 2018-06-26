package com.example.administrator.julong.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.julong.R;
import com.example.administrator.julong.entity.BiaotiEntity;
import com.example.administrator.julong.entity.OuterentryEntity;
import com.example.administrator.julong.util.Bitmaputil;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2017-07-03.
 */
public class OuterentryAdapter extends BaseAdapter{
    private Context mContext = null;
    private List<OuterentryEntity> dataList;

    private LayoutInflater inflater;
    DecimalFormat df = new DecimalFormat("######0.00");
    public OuterentryAdapter(Context context, List<OuterentryEntity> dataList) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Holder holder;
        if (view == null) {

            view = inflater.inflate(R.layout.item_outer_entry, null);
            holder = new Holder();
            holder.iv_img = (ImageView) view
                    .findViewById(R.id.iv_img2);
            holder.tv_pm = (TextView) view
                    .findViewById(R.id.tv_pm);
            holder.tv_gg = (TextView) view
                    .findViewById(R.id.tv_gg);
            holder.tv_sl = (TextView) view
                    .findViewById(R.id.tv_sl);
            holder.tv_note = (TextView) view
                    .findViewById(R.id.tv_note);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.iv_img.setImageBitmap(Bitmaputil.base64ToBitmap(dataList.get(i).getFimage1()));
        holder.tv_pm.setText("物料："+dataList.get(i).getFname());
        holder.tv_gg.setText("规格："+dataList.get(i).getFmodel());
        holder.tv_sl.setText("数量："+df.format(Double.parseDouble(dataList.get(i).getFqty())));
        holder.tv_note.setText("备注："+dataList.get(i).getFnote());

        holder.iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(mContext, R.style.edit_AlertDialog_style);
                dialog.setContentView(R.layout.imageload);
                ImageView iv = (ImageView) dialog.findViewById(R.id.iv_img_large);
                holder.iv_img.setDrawingCacheEnabled(true);
                iv.setImageBitmap(Bitmaputil.base64ToBitmap(dataList.get(i).getFimage1()));
                holder.iv_img.setDrawingCacheEnabled(false);
                dialog.setCanceledOnTouchOutside(true); // Sets whether this dialog is
                Window w = dialog.getWindow();
                WindowManager.LayoutParams lp = w.getAttributes();
                lp.x = 0;
                lp.y = 40;
                dialog.onWindowAttributesChanged(lp);
                dialog.show();
            }
        });
        return view;
    }

    private class Holder {

        ImageView iv_img;
        TextView tv_pm,tv_gg,tv_sl,tv_note;
    }



}
