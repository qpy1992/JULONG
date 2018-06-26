package com.example.administrator.julong.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.julong.R;
import com.example.administrator.julong.entity.RukuEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-07-03.
 */
public class RukuAdapter extends BaseAdapter{
    private Context mContext = null;
    private List<RukuEntity> list = new ArrayList<>();
    private LayoutInflater inflater;
    public RukuAdapter(Context context,List<RukuEntity> list) {
        this.mContext = context;
        this.list = list;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Holder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_ruku, null);
            holder = new Holder();
            holder.tv_wl = (TextView) view
                    .findViewById(R.id.tv_wl);
            holder.et_tj = (EditText) view
                    .findViewById(R.id.et_tj);
            holder.tv_num = (TextView)view
                    .findViewById(R.id.tv_num);
            holder.et_xs = (EditText) view
                    .findViewById(R.id.et_xs);
            holder.et_xs1 = (EditText) view
                    .findViewById(R.id.et_xs1);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        String num = list.get(i).getFnumber().toString();
        String tj = list.get(i).getFtiji().toString();
        String xs = list.get(i).getFxs().toString();
        String xs1 = list.get(i).getFxs1().toString();
        holder.tv_wl.setText(list.get(i).getFname().toString()+":");
        holder.tv_num.setText(num);
        holder.et_tj.setText(tj);
        holder.et_xs.setText(xs);
        holder.et_xs1.setText(xs1);
        num = holder.tv_num.getText().toString();
        Log.i("新的num",num);
        final int[] sl = {0};
        final String[] js = new String[1];
        final String[] x = new String[1];
        holder.et_xs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                js[0] = holder.et_xs.getText().toString();
                x[0] = holder.et_xs1.getText().toString();
                if(js[0].equals("")|| x[0].equals("")){
                    sl[0] = 0;
                }else {
                    sl[0] = Integer.parseInt(js[0]) * Integer.parseInt(x[0]);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                holder.tv_num.setText(String.valueOf(sl[0]));
            }
        });
        holder.et_xs1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                js[0] = holder.et_xs.getText().toString();
                x[0] = holder.et_xs1.getText().toString();
                if(js[0].equals("")|| x[0].equals("")){
                    sl[0] = 0;
                }else {
                    sl[0] = Integer.parseInt(js[0]) * Integer.parseInt(x[0]);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                holder.tv_num.setText(String.valueOf(sl[0]));
            }
        });
        return view;
    }

    private class Holder {
        TextView tv_num;
        EditText et_tj,et_xs,et_xs1;
        TextView tv_wl;
    }

}
