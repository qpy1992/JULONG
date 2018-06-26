package com.example.administrator.julong.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
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
import com.example.administrator.julong.entity.WLEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017-03-20.
 */
public class WLApater extends BaseAdapter {

    private Context mContext = null;
    private List<WLEntity> dataList;

    private LayoutInflater inflater;

    public WLApater(Context context, List<WLEntity> dataList) {
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
            holder.iv = (ImageView) view.findViewById(R.id.img);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.tv1.setText(dataList.get(i).getFnumber());
        holder.tv2.setText(dataList.get(i).getFname());

        if(""!=dataList.get(i).getImg()){
            holder.iv.setImageBitmap(base64ToBitmap(dataList.get(i).getImg()));
        }else {
            holder.iv.setImageResource(R.mipmap.ic_launcher);
        }

       // holder.iv.setImageBitmap(base64ToBitmap(dataList.get(i).getImg()));

       //"data:image/jpeg;base64,"+
        return view;
    }

    private class Holder {


        TextView tv1,tv2;
        ImageView iv;
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
