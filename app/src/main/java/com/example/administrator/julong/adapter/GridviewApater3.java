package com.example.administrator.julong.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.julong.R;
import com.example.administrator.julong.entity.WLEntity;

import java.util.List;

/**
 * Created by Administrator on 2017-03-20.
 */
public class GridviewApater3 extends BaseAdapter {

    private Context mContext = null;
    private List<WLEntity> dataList;

    private LayoutInflater inflater;

    public GridviewApater3(Context context, List<WLEntity> dataList) {
        this.mContext = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return dataList.size() ;
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

            view = inflater.inflate(R.layout.item_gridview2, null);
            holder = new Holder();
            holder.rl = (RelativeLayout) view.findViewById(R.id.rl);
            holder.icon_img = (ImageView) view
                    .findViewById(R.id.iv);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }


        WindowManager wm = (WindowManager) mContext.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = (wm.getDefaultDisplay().getWidth() - 110) / 3;
        int height = width;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.icon_img.getLayoutParams();
        params.width = width;
        params.height = width;
        holder.icon_img.setLayoutParams(params);
        //holder.icon_img.setBackgroundResource(R.drawable.k);

            Bitmap bitmap =base64ToBitmap( dataList.get(i).getImg());
            // holder.icon_img.setImageBitmap(bitmap);
             Log.i("xxx",dataList.get(i).getImg());
            if (null != bitmap) {
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();


                RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) holder.icon_img.getLayoutParams();
                int neww = params2.width;


                int newh = params2.height;
                float sw = (float) neww / w;
                float sh = (float) newh / h;
                Matrix matrix = new Matrix();
                matrix.postScale(sw, sh);
                Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix,
                        true);
                holder.icon_img.setImageBitmap(newbm);
            }



        return view;
    }

    private class Holder {

        RelativeLayout rl;
        ImageView icon_img;

    }

    public Bitmap base64ToBitmap(String base64String) {

        byte[] decode = Base64.decode(base64String, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);

        return bitmap;
    }
}
