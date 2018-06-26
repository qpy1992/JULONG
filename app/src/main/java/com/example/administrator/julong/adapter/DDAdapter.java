package com.example.administrator.julong.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.julong.activity.BiaotouActivity;
import com.example.administrator.julong.activity.KDActivity;
import com.example.administrator.julong.R;
import com.example.administrator.julong.activity.RukuActivity;
import com.example.administrator.julong.util.CreateYCMdf;
import com.example.administrator.julong.util.ExcelUtil;
import com.example.administrator.julong.view.SubListView;
import com.example.administrator.julong.entity.BiaotiEntity;
import com.example.administrator.julong.entity.BiaotouEntity;
import com.example.administrator.julong.entity.StockEntity;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017-07-03.
 */
public class DDAdapter extends BaseAdapter{
    private Context mContext = null;
    private List<BiaotouEntity> dataList;
//    private List<StockEntity> list = new ArrayList<>();
    private LayoutInflater inflater;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    public DDAdapter(Context context, List<BiaotouEntity> dataList
    //        ,List<StockEntity> list
    ) {
        this.mContext = context;
        this.dataList = dataList;
//        this.list = list;
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

            view = inflater.inflate(R.layout.item_head, null);
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
            holder.lv = (SubListView) view
                    .findViewById(R.id.lv);

            holder.bt = (ImageView) view.findViewById(R.id.btfz);
            holder.btxg = (ImageView) view.findViewById(R.id.btxg);
            holder.btn_rk = (ImageView) view.findViewById(R.id.btrk);
            holder.v_rk = (View) view.findViewById(R.id.v_rk);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.tv1.setText(dataList.get(i).getFBillNo());
        try {
            Date d=sdf.parse(dataList.get(i).getFDate());
            String s = sdf2.format(d);
            holder.tv2.setText(s);
        }catch (Exception e){

        }

        holder.tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext,android.R.style.Theme_Holo_Light_Dialog);
                builder.setTitle("选择生成格式");
                 //   指定下拉列表的显示数据
        final String[] stocks = new String[]{"PDF文档","EXCEL表格"};
                  //  设置一个下拉的列表选择项
        builder.setItems(stocks, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which) {
                    case 0:
                        try {
                            File file = new File(Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES), holder.tv1.getText()+".pdf");
                            System.out.println(mContext.getFilesDir());
                            file.createNewFile();
                            List<BiaotiEntity> entrylist= dataList.get(i).getBt();
                            int j =12 - entrylist.size();
                            if(j>0){
                                for(int k=0;k<j;k++){
                                    entrylist.add(new BiaotiEntity());
                                }
                            }
                            new CreateYCMdf(file).generateYCMPDF(dataList.get(i),entrylist);
                            Toast.makeText(mContext,"生成成功！",Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(mContext,"生成失败！",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        try {
                            Log.i("是否执行", "时间哈大家看哈十大健康收到货");
                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), holder.tv1.getText() + ".xls");
                            file.createNewFile();
                            ExcelUtil.writeExcel(mContext, dataList.get(i), dataList.get(i).getBt(), file);
                            Toast.makeText(mContext, "生成成功", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            Toast.makeText(mContext,"生成失败！",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
        AlertDialog r_dialog = builder.create();
//        r_dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_blue_light);
        r_dialog.show();
            }
        });

        holder.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, KDActivity.class);
                List<BiaotiEntity> list=dataList.get(i).getBt();
                intent.putExtra("list",(Serializable)list);
                intent.putExtra("bill", dataList.get(i).getFBillNo());
                intent.putExtra("inter", dataList.get(i).getFInterID());
                intent.putExtra("bt", (Serializable)dataList.get(i));
                intent.putExtra("zt", "xz");
                mContext.startActivity(intent);

            }
        });
        holder.btxg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, KDActivity.class);
                List<BiaotiEntity> list=dataList.get(i).getBt();
                intent.putExtra("list",(Serializable)list);
                intent.putExtra("bill", dataList.get(i).getFBillNo());
                intent.putExtra("inter", dataList.get(i).getFInterID());
                intent.putExtra("bt", (Serializable)dataList.get(i));
                intent.putExtra("zt", "xg");
                mContext.startActivity(intent);

            }
        });
        holder.btn_rk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fbillno = holder.tv1.getText().toString();
                String maitou = holder.tv5.getText().toString();
                Intent i = new Intent(mContext,RukuActivity.class);
                i.putExtra("fbillno",fbillno);
                i.putExtra("maitou",maitou);
                mContext.startActivity(i);
            }
        });
        holder.tv3.setText("供应商:"+dataList.get(i).getGys());
        holder.tv4.setText("跟单:"+dataList.get(i).getGd());
        holder.tv5.setText("唛头:"+dataList.get(i).getMt());

        String fbioaji = dataList.get(i).getFbiaoji();
        Log.i("标记",fbioaji+"");
        if(fbioaji.equals("1")) {
            holder.tv6.setText("完全入库");
            holder.tv6.setTextColor(Color.BLUE);
            holder.btn_rk.setVisibility(View.INVISIBLE);
            holder.v_rk.setVisibility(View.INVISIBLE);
        }
         EntryAdapter adapter = new EntryAdapter(mContext, dataList.get(i).getBt());
        holder.lv.setAdapter(adapter);
        return view;
    }

    private class Holder {


        TextView tv1,tv2,tv3,tv4,tv5,tv6;
        SubListView lv;
        ImageView bt,btxg,btn_rk;
        View v_rk;

    }

}
