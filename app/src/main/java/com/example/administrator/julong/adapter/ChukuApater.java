package com.example.administrator.julong.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.julong.R;
import com.example.administrator.julong.entity.WLEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017-03-20.
 */
public class ChukuApater extends BaseAdapter {

    private Context mContext = null;
    private List<WLEntity> dataList;

    private LayoutInflater inflater;

    public ChukuApater(Context context, List<WLEntity> dataList) {
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
        Holder holder;
        if (view == null) {

            view = inflater.inflate(R.layout.item_chuku, null);
            holder = new Holder();
            holder.tv1 = (TextView) view
                    .findViewById(R.id.tv_pinming);
            holder.tv2 = (TextView) view
                    .findViewById(R.id.tv_guige);
            holder.tv3 = (TextView) view
                    .findViewById(R.id.tv_num);
            holder.tv4 = (TextView) view
                    .findViewById(R.id.tv_note);
            holder.bt_del = (Button) view
                    .findViewById(R.id.bt_del);
            holder.bt_edit = (Button) view
                    .findViewById(R.id.bt_edit);
            holder.iv = (ImageView) view.findViewById(R.id.iv_img_ck);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.tv1.setText(dataList.get(i).getFname());
        holder.tv2.setText(dataList.get(i).getGg());
        holder.tv3.setText(dataList.get(i).getJs()+"");
        holder.tv4.setText(dataList.get(i).getBeizhu());

        if(""!=dataList.get(i).getImg()){
            holder.iv.setImageBitmap(base64ToBitmap(dataList.get(i).getImg()));
        }else {
            holder.iv.setImageResource(R.mipmap.ic_launcher);
        }

        holder.bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final WLEntity wlEntity = dataList.get(i);
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                View layout = inflater.inflate(R.layout.dialog4, null);
                final EditText et1 = (EditText) layout.findViewById(R.id.et1);
                et1.setText(wlEntity.getJs()+"");
                final EditText et6 = (EditText) layout.findViewById(R.id.et6);
                et6.setText(wlEntity.getBeizhu());
                AlertDialog alertDialog = new AlertDialog.Builder(mContext).setTitle("请完成 ").setView(layout)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (et1.getText().toString().equals("")) {
                                    Toast.makeText(mContext, "件数不能为空", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                wlEntity.setJs(Integer.parseInt(et1.getText().toString()));
                                wlEntity.setBeizhu(et6.getText().toString() + "");
                                notifyDataSetChanged();
                            }
                        }).create();
                alertDialog.show();
            }
        });

        holder.bt_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataList.remove(i);
                notifyDataSetChanged();
            }
        });
        return view;
    }

    private class Holder {


        TextView tv1,tv2,tv3,tv4;
        ImageView iv;
        Button bt_del,bt_edit;
    }


    public  Bitmap base64ToBitmap(String base64String){

        byte[] decode = Base64.decode(base64String, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);

        return bitmap;
    }
    /*
 *bitmap转base64
 */
    public  String bitmapToBase64(Bitmap bitmap){
        String result="";
        ByteArrayOutputStream bos=null;
        try {
            if(null!=bitmap){
                bos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);//将bitmap放入字节数组流中

                bos.flush();//将bos流缓存在内存中的数据全部输出，清空缓存
                bos.close();

                byte []bitmapByte=bos.toByteArray();
                result=Base64.encodeToString(bitmapByte, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(null!=null){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
