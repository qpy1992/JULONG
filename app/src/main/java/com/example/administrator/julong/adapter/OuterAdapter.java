package com.example.administrator.julong.adapter;

import android.app.Activity;
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

import com.example.administrator.julong.R;
import com.example.administrator.julong.activity.BiaotouActivity;
import com.example.administrator.julong.activity.ChukuActivity;
import com.example.administrator.julong.activity.KDActivity;
import com.example.administrator.julong.activity.OuterActivity;
import com.example.administrator.julong.entity.BiaotiEntity;
import com.example.administrator.julong.entity.BiaotouEntity;
import com.example.administrator.julong.entity.OuterEntity;
import com.example.administrator.julong.entity.OuterentryEntity;
import com.example.administrator.julong.entity.StockEntity;
import com.example.administrator.julong.util.CreateOuterpdf;
import com.example.administrator.julong.util.ExcelUtil;
import com.example.administrator.julong.view.SubListView;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017-07-03.
 */
public class OuterAdapter extends BaseAdapter{
    private Context mContext = null;
    private List<OuterEntity> dataList;
    private LayoutInflater inflater;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    public OuterAdapter(Context context, List<OuterEntity> dataList) {
        this.mContext = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(mContext);
    }
    private int flag = 0;

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
            view = inflater.inflate(R.layout.item_outer, null);
            holder = new Holder();
            holder.tv_billno = (TextView) view
                    .findViewById(R.id.tv_billno);
            holder.tv_fdate = (TextView) view
                    .findViewById(R.id.tv_fdate);
            holder.tv_fname = (TextView) view
                    .findViewById(R.id.tv_name);
            holder.tv_stock = (TextView) view
                    .findViewById(R.id.tv_stock);
            holder.lv_outer_entry = (SubListView) view
                    .findViewById(R.id.lv_outer_entry);
            holder.tv_status = (TextView) view
                    .findViewById(R.id.tv_status);
            holder.bt_add = (ImageView) view.findViewById(R.id.bt_add);
            holder.bt_edits = (ImageView) view.findViewById(R.id.bt_edits);
            holder.iv_check = (ImageView) view.findViewById(R.id.iv_check);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.tv_billno.setText(dataList.get(i).getFbillno());
        try {
            Date d=sdf.parse(dataList.get(i).getFdate());
            String s = sdf2.format(d);
            holder.tv_fdate.setText(s);
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.tv_billno.setOnClickListener(new View.OnClickListener() {
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
                        switch (which){
                            case 0:
                                try {
                                    File file = new File(Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES), holder.tv_billno.getText()+".pdf");
                                    System.out.println(mContext.getFilesDir());
                                    file.createNewFile();
                                    new CreateOuterpdf(file).generateYCMPDF(dataList.get(i),dataList.get(i).getOuterentry());
                                    Toast.makeText(mContext,"生成成功！",Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(mContext,"生成失败！",Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), holder.tv_billno.getText() + ".xls");
                                    file.createNewFile();
                                    ExcelUtil.writeExcelck(mContext, dataList.get(i), dataList.get(i).getOuterentry(), file);
                                    Toast.makeText(mContext,"生成成功！",Toast.LENGTH_SHORT).show();
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

        holder.bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ChukuActivity.class);
                /*List<OuterentryEntity> list=dataList.get(i).getOuterentry();
                intent.putExtra("list",(Serializable)list);
                intent.putExtra("bill", dataList.get(i).getBillno());
                intent.putExtra("inter", dataList.get(i).getFInterId());
                intent.putExtra("outer", (Serializable) dataList.get(i));
                intent.putExtra("zt", "xz");*/
                mContext.startActivity(intent);

            }
        });
        holder.bt_edits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ChukuActivity.class);
                List<OuterentryEntity> list=dataList.get(i).getOuterentry();
                intent.putExtra("list",(Serializable)list);
                intent.putExtra("bill", dataList.get(i).getFbillno());
                intent.putExtra("inter", dataList.get(i).getFInterId());
                intent.putExtra("outer", (Serializable)dataList.get(i));
                intent.putExtra("zt", "xg");
                mContext.startActivity(intent);

            }
        });

        holder.tv_fname.setText("客户:"+dataList.get(i).getFname());
        holder.tv_stock.setText("仓库:"+dataList.get(i).getFstock());
        OuterentryAdapter adapter = new OuterentryAdapter(mContext, dataList.get(i).getOuterentry());
        holder.lv_outer_entry.setAdapter(adapter);

        if(dataList.get(i).getFcheckerid().equals("0")){
            holder.tv_status.setText("未审");
            holder.tv_status.setTextColor(Color.RED);

            holder.iv_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                if(flag%2==0) {
                    new AlertDialog.Builder(mContext).setTitle("审核").setMessage("确认审核？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    OuterActivity.CheckTask task = new OuterActivity.CheckTask("16394",dataList.get(i).getFInterId());
                                    task.execute();
                                    holder.iv_check.setImageResource(R.drawable.removecheck);
                                    holder.tv_status.setText("已审");
                                    holder.tv_status.setTextColor(Color.RED);
                                    Toast.makeText(mContext,"审核成功！",Toast.LENGTH_SHORT).show();
                                    flag++;
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }else{
                    new AlertDialog.Builder(mContext).setTitle("反审核").setMessage("确认反审核？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which){
                                    dialog.dismiss();
                                    OuterActivity.CheckTask task = new OuterActivity.CheckTask("0",dataList.get(i).getFInterId());
                                    task.execute();
                                    holder.iv_check.setImageResource(R.drawable.check);
                                    holder.tv_status.setText("未审");
                                    holder.tv_status.setTextColor(Color.RED);
                                    Toast.makeText(mContext,"反审核成功！",Toast.LENGTH_SHORT).show();
                                    flag++;
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
                }
            });
        }else{
            holder.tv_status.setText("已审");
            holder.tv_status.setTextColor(Color.RED);
            holder.iv_check.setImageResource(R.drawable.removecheck);
            holder.iv_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(flag%2==0) {
                        new AlertDialog.Builder(mContext).setTitle("反审核").setMessage("确认反审核？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        OuterActivity.CheckTask task = new OuterActivity.CheckTask("0",dataList.get(i).getFInterId());
                                        task.execute();
                                        holder.iv_check.setImageResource(R.drawable.check);
                                        holder.tv_status.setText("未审");
                                        holder.tv_status.setTextColor(Color.RED);
                                        Toast.makeText(mContext,"反审核成功！",Toast.LENGTH_SHORT).show();
                                        flag++;
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }else{
                        new AlertDialog.Builder(mContext).setTitle("审核").setMessage("确认审核？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        OuterActivity.CheckTask task = new OuterActivity.CheckTask("16394",dataList.get(i).getFInterId());
                                        task.execute();
                                        holder.iv_check.setImageResource(R.drawable.removecheck);
                                        holder.tv_status.setText("已审");
                                        holder.tv_status.setTextColor(Color.RED);
                                        Toast.makeText(mContext,"审核成功！",Toast.LENGTH_SHORT).show();
                                        flag++;
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                }
            });
        }
        return view;
    }

    private class Holder {
        TextView tv_billno,tv_fname,tv_fdate,tv_stock,tv_status;
        SubListView lv_outer_entry;
        ImageView bt_add,bt_edits,iv_check;
    }

}
