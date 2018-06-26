package com.example.administrator.julong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.julong.R;
import com.example.administrator.julong.entity.BiaotiEntity;
import com.example.administrator.julong.entity.ReportEntity;
import com.example.administrator.julong.util.Bitmaputil;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2017-07-03.
 */
public class ReportAdapter extends BaseAdapter{
    private Context mContext = null;
    private List<ReportEntity> dataList;

    private LayoutInflater inflater;
    DecimalFormat df = new DecimalFormat("######0");
    public ReportAdapter(Context context, List<ReportEntity> dataList) {
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

            view = inflater.inflate(R.layout.item_report, null);
            holder = new Holder();
            holder.iv_img8 = (ImageView) view
                    .findViewById(R.id.iv_img8);
            holder.tva = (TextView) view
                    .findViewById(R.id.tv_a);
            holder.tvb = (TextView) view
                    .findViewById(R.id.tv_b);
            holder.tvc = (TextView) view
                    .findViewById(R.id.tv_c);
            holder.tvd = (TextView) view
                    .findViewById(R.id.tv_d);
            holder.tve = (TextView) view
                    .findViewById(R.id.tv_e);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
//        holder.iv_img8.setImageBitmap(Bitmaputil.base64ToBitmap(dataList.get(i).getFimage1()));
            holder.tva.setText("品名：" + dataList.get(i).getFname());
            holder.tvb.setText("规格：" + dataList.get(i).getFmodel());
            holder.tvc.setText("订单数量：" + df.format(Double.parseDouble(dataList.get(i).getOrdernum())));
            holder.tvd.setText("入库数量：" + df.format(Double.parseDouble(dataList.get(i).getInnernum())));
            holder.tve.setText("出库数量：" + df.format(Double.parseDouble(dataList.get(i).getOuternum())));
        return view;
    }

    private class Holder {

        ImageView iv_img8;
        TextView tva,tvb,tvc,tvd,tve;
    }



}
