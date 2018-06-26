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

import java.util.List;

/**
 * Created by Administrator on 2017-03-20.
 */
public class WLApater2 extends BaseAdapter {

    private Context mContext = null;
    private List<WLEntity> dataList;

    private LayoutInflater inflater;

    public WLApater2(Context context, List<WLEntity> dataList) {
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

            view = inflater.inflate(R.layout.item_wl2, null);
            holder = new Holder();
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

            holder.iv = (ImageView) view.findViewById(R.id.img1);
            holder.bt = (Button) view.findViewById(R.id.btsc);
            holder.btxg = (Button) view.findViewById(R.id.btxg);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.tv1.setText(dataList.get(i).getFname());
        holder.tv2.setText(dataList.get(i).getGg());
        holder.tv3.setText(dataList.get(i).getJs() + "");
        holder.tv4.setText(dataList.get(i).getXs() + "");

        holder.tv5.setText(dataList.get(i).getXs() * dataList.get(i).getJs() + "");
        holder.tv6.setText(dataList.get(i).getDj() + "");
        holder.tv7.setText(dataList.get(i).getDj() * dataList.get(i).getXs() * dataList.get(i).getJs() + "");
        holder.tv8.setText(dataList.get(i).getTiji() + "");
        holder.tv9.setText(dataList.get(i).getBianhao() + "");
        holder.tv10.setText(dataList.get(i).getBeizhu() + "");

        if (null != dataList.get(i).getImg()) {
            final Bitmap bitmap = base64ToBitmap(dataList.get(i).getImg());
            holder.iv.setImageBitmap(bitmap);
//            if (null != bitmap) {
//                int w = bitmap.getWidth();
//                int h = bitmap.getHeight();
//
//
//                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.iv.getLayoutParams();
//                int neww = params.width;
//
//
//                int newh = params.height;
//                float sw = (float) neww / w;
//                float sh = (float) newh / h;
//                Matrix matrix = new Matrix();
//                matrix.postScale(sw, sh);
//                Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix,
//                        true);
//                holder.iv.setImageBitmap(newbm);
//                holder.iv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (null == bitmap) {
//                            Toast.makeText(mContext, "暂无图片", Toast.LENGTH_LONG).show();
//                        } else {
//
//                            Intent intent = new Intent(mContext, PhotoActivity.class);
//                            intent.putExtra("p", dataList.get(i).getFname());
//                            mContext.startActivity(intent);
//
//                        }
//
//                    }
//                });
//
//            }
        }


        holder.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataList.remove(i);
                notifyDataSetChanged();
            }
        });

        holder.btxg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final WLEntity wlEntity = dataList.get(i);
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                View layout = inflater.inflate(R.layout.dialog2, null);
                final EditText et1 = (EditText) layout.findViewById(R.id.et1);
                final EditText et2 = (EditText) layout.findViewById(R.id.et2);
                final EditText et3 = (EditText) layout.findViewById(R.id.et3);
                final EditText et4 = (EditText) layout.findViewById(R.id.et4);
                final EditText et5s = (EditText) layout.findViewById(R.id.et5s);
                final EditText et6s = (EditText) layout.findViewById(R.id.et6s);
                final EditText et5 = (EditText) layout.findViewById(R.id.et5);
                final EditText et6 = (EditText) layout.findViewById(R.id.et6);
                et1.setText(String.valueOf(wlEntity.getJs()));
                et2.setText(String.valueOf(wlEntity.getXs()));
                et3.setText(String.valueOf(wlEntity.getDj()));
                et4.setText(String.valueOf(wlEntity.getTiji()));
                et5s.setText(wlEntity.getFmaterial());
                et6s.setText(String.valueOf(wlEntity.getFsingleweight()));
                et5.setText(String.valueOf(wlEntity.getBianhao()));
                et6.setText(String.valueOf(wlEntity.getBeizhu()));
                AlertDialog alertDialog = new AlertDialog.Builder(mContext).setTitle("请完成 ").setView(layout)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (et1.getText().toString().equals("")) {
                                    Toast.makeText(mContext, "件数不能为空", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                if (et2.getText().toString().equals("")) {
                                    Toast.makeText(mContext, "箱数不能为空", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                if (et3.getText().toString().equals("")) {
                                    Toast.makeText(mContext, "单价不能为空", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                if (et4.getText().toString().equals("")) {
                                    Toast.makeText(mContext, "体积不能为空", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                if (et5s.getText().toString().equals("")) {
                                    Toast.makeText(mContext, "材质不能为空", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                if (et6s.getText().toString().equals("")) {
                                    Toast.makeText(mContext, "单重不能为空", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                wlEntity.setJs(Integer.parseInt(et1.getText().toString()));
                                wlEntity.setXs(Integer.parseInt(et2.getText().toString()));
                                wlEntity.setDj(Double.parseDouble(et3.getText().toString()));
                                wlEntity.setTiji(Double.parseDouble(et4.getText().toString()));
                                wlEntity.setBianhao(et5.getText().toString() + "");
                                wlEntity.setBeizhu(et6.getText().toString() + "");
                                wlEntity.setFmaterial(et5s.getText().toString());
                                wlEntity.setFsingleweight(Double.parseDouble(et6s.getText().toString()));
                                notifyDataSetChanged();
                            }
                        }).create();
                alertDialog.show();

            }
        });


        return view;
    }

    private class Holder {

        ImageView iv;
        TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10;
        Button bt, btxg;
    }

    public Bitmap base64ToBitmap(String base64String) {

        byte[] decode = Base64.decode(base64String, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);

        return bitmap;
    }

    public void noti(List<WLEntity> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }
}
