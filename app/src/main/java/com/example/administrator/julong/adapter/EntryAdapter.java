package com.example.administrator.julong.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.julong.R;
import com.example.administrator.julong.entity.BiaotiEntity;
import com.example.administrator.julong.entity.BiaotouEntity;
import com.example.administrator.julong.util.Bitmaputil;

import java.text.DecimalFormat;
import java.util.List;

import static com.example.administrator.julong.R.id.imageView;

/**
 * Created by Administrator on 2017-07-03.
 */
public class EntryAdapter extends BaseAdapter{
    private Context mContext = null;
    private List<BiaotiEntity> dataList;

    private LayoutInflater inflater;
    DecimalFormat df = new DecimalFormat("######0.00");
    public EntryAdapter(Context context, List<BiaotiEntity> dataList) {
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
            view = inflater.inflate(R.layout.item_entry, null);
            holder = new Holder();
            holder.iv_img = (ImageView) view
                    .findViewById(R.id.iv_img);
            holder.tv1 = (TextView) view
                    .findViewById(R.id.tv1);
            holder.tv2 = (TextView) view
                    .findViewById(R.id.tv2);
            holder.tv3 = (TextView) view
                    .findViewById(R.id.tv3);
            holder.tv4 = (TextView) view
                    .findViewById(R.id.tv4);
            holder.tv5 = (TextView) view
                    .findViewById(R.id.tv5);
            holder.tv6 = (TextView) view
                    .findViewById(R.id.tv6);
            holder.tv7 = (TextView) view
                    .findViewById(R.id.tv7);
            holder.tv8 = (TextView) view
                    .findViewById(R.id.tv8);
            holder.tv9 = (TextView) view
                    .findViewById(R.id.tv9);
            holder.tv10 = (TextView) view
                    .findViewById(R.id.tv10);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.iv_img.setImageBitmap(Bitmaputil.base64ToBitmap(dataList.get(i).getFimage1()));
        holder.tv1.setText("品名："+dataList.get(i).getFName());
        holder.tv2.setText("规格："+dataList.get(i).getFmodel());
        holder.tv3.setText("数量："+df.format(Double.parseDouble(dataList.get(i).getFAuxQty())));
        holder.tv4.setText("箱数："+dataList.get(i).getXs());
        holder.tv5.setText("体积："+dataList.get(i).getTj());
        holder.tv6.setText("单价："+df.format(Double.parseDouble(dataList.get(i).getFAuxPrice())));
        holder.tv7.setText("金额："+df.format(Double.parseDouble(dataList.get(i).getFAmount())));
        holder.tv8.setText("备注："+dataList.get(i).getFnote());
        holder.tv9.setText("材质："+dataList.get(i).getFmaterial());
        holder.tv10.setText("单重："+df.format(dataList.get(i).getFsingleweight()));

//        holder.iv.setImageBitmap(base64ToBitmap(dataList.get(i).getImg()));
        //"data:image/jpeg;base64,"+

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
        TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10;
    }

}
