package com.example.administrator.julong.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.julong.R;
import com.example.administrator.julong.adapter.OuterAdapter;
import com.example.administrator.julong.adapter.SimpleTreeAdapter;
import com.example.administrator.julong.adapter.TreeListViewAdapter;
import com.example.administrator.julong.adapter.WLApater3;
import com.example.administrator.julong.entity.FileBean;
import com.example.administrator.julong.entity.Node;
import com.example.administrator.julong.entity.OuterEntity;
import com.example.administrator.julong.entity.OuterentryEntity;
import com.example.administrator.julong.entity.WLEntity;
import com.example.administrator.julong.util.DateUtil;
import com.example.administrator.julong.util.UTIL;
import com.example.administrator.julong.view.CustomProgress;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class OuterActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tv_date_begin,tv_date_end,tv_custom;
    private ImageView iv_search;
    private int year,month,day;
    private String d1, d2,cusname;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private List<WLEntity> list = new ArrayList<>();
    private List<FileBean> list1 = new ArrayList<>();
    private List<OuterEntity> outerlist = new ArrayList<>();
    private List<OuterentryEntity> entrylist = new ArrayList<>();
    private ListView lv_outer;
    private OuterAdapter adapter;
    private CustomProgress dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outer);
        setTool();
        setViews();
        UTIL.verifyStoragePermissions(this);
        setListeners();
    }

    private void setTool() {
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("出库单列表");

        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String msg = "";
                switch (item.getItemId()) {
                    case R.id.action_sm:
                        Intent intent = new Intent("com.summi.scan");
                        intent.setPackage("com.sunmi.sunmiqrcodescanner");
                        startActivityForResult(intent, 111);
                        break;
                }
                return true;
            }
        });
    }

    protected void setViews(){
        tv_date_begin = (TextView) findViewById(R.id.tv_date_begin);
        tv_date_end = (TextView) findViewById(R.id.tv_date_end);
        tv_custom = (TextView) findViewById(R.id.tv_custom);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        Calendar mycalendar = Calendar.getInstance();
        year = mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        month = mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        day = mycalendar.get(Calendar.DAY_OF_MONTH);
        Date date = new Date(System.currentTimeMillis());
        tv_date_begin.setText(DateUtil.getPastDate(15));
        tv_date_end.setText(DateUtil.getFetureDate(1));
        d1 = DateUtil.getPastDate(7);
        d2 = sdf.format(date);
        lv_outer = (ListView) findViewById(R.id.lv_outer);
        adapter = new OuterAdapter(this,outerlist);
        lv_outer.setAdapter(adapter);
    }

    protected void setListeners(){
        tv_date_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(OuterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String s = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        try {
                            java.util.Date date = sdf.parse(s);
                            String s1 = sdf.format(date);
                            tv_date_begin.setText(s1);
                            d1 = s1;
                        } catch (Exception e) {
                        }
                    }
                }, year, month, day);
                dpd.show();//显示DatePickerDialog组件
            }
        });

        tv_date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(OuterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String s = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        try {
                            java.util.Date date = sdf.parse(s);
                            String s2 = sdf.format(date);
                            tv_date_end.setText(s2);
                            d2 = s2;
                        } catch (Exception e) {
                        }
                    }
                }, year, month, day);
                dpd.show();//显示DatePickerDialog组件
            }
        });

        tv_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(OuterActivity.this);
                et.setBackgroundResource(R.drawable.et_shapes);
                new AlertDialog.Builder(OuterActivity.this).setTitle("请输入客户").setIcon(
                        R.drawable.customs).setView(
                        et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cusname = et.getText().toString().trim();
                        CusTask task = new CusTask();
                        task.execute();
                    }
                })
                        .setNegativeButton("取消", null).show();
            }
        });

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task tasks = new Task();
                tasks.execute();
            }
        });
    }


    class CusTask extends AsyncTask<Void,Integer,Integer> {

        @Override
        protected void onPreExecute() {
            list1.clear();
            dialog=CustomProgress.show(OuterActivity.this,"搜寻中...", true, null);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            // 命名空间
            String nameSpace = "http://tempuri.org/";
            // 调用的方法名称
            String methodName = "JA_select";
            // EndPoint
            String endPoint = UTIL.ENDPOINT;
            // SOAP Action
            String soapAction = "http://tempuri.org/JA_select";

            // 指定WebService的命名空间和调用的方法名
            SoapObject rpc = new SoapObject(nameSpace, methodName);

            // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
//            rpc.addProperty("FSql", "select FNAME ,FNUMBER from t_organization where fname like '%" + cusname + "%'");
            rpc.addProperty("FSql","select fname,fnumber,fitemid,fparentid from t_item where fname like '%"+cusname+"%' and fitemclassid=1 and (fitemid in (select fparentid from t_item where fitemclassid=1) or fparentid!=0)");
            rpc.addProperty("FTable", "t_organization");

            // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

            envelope.bodyOut = rpc;
            // 设置是否调用的是dotNet开发的WebService
            envelope.dotNet = true;
            // 等价于envelope.bodyOut = rpc;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(endPoint);
            try {
                // 调用WebService
                transport.call(soapAction, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 获取返回的数据
            SoapObject object = (SoapObject) envelope.bodyIn;
            // 获取返回的结果
            if (null != object) {
                String result = object.getProperty(0).toString();
                Log.i("sss", result + "sss");

//
                Document doc = null;
                try {
                    doc = DocumentHelper.parseText(result); // 将字符串转为XML

                    Element rootElt = doc.getRootElement(); // 获取根节点

                    System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称


                    Iterator iter = rootElt.elementIterator("Cust"); // 获取根节点下的子节点head

                    // 遍历head节点

                    while (iter.hasNext()) {
                        Element recordEle = (Element) iter.next();
                        String fname = recordEle.elementTextTrim("fname"); // 拿到head节点下的子节点title值
                        String fnumber = recordEle.elementTextTrim("fnumber");
                        String fitemid = recordEle.elementTextTrim("fitemid");
                        String fparentid = recordEle.elementTextTrim("fparentid");
                        FileBean entity = new FileBean(Integer.parseInt(fitemid),Integer.parseInt(fparentid),fname,null,fnumber,null);
                        list1.add(entity);
                        // 遍历Header节点下的Response节点
                    }


                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return 5;
        }

        @Override
        protected void onPostExecute(Integer s) {
            dialog.dismiss();
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.activity_tree, null);
            ListView lv = (ListView) layout.findViewById(R.id.lv_tree);
            final TreeListViewAdapter adapter;
            try{
                adapter = new SimpleTreeAdapter<FileBean>(lv, OuterActivity.this, list1, 10);
                lv.setAdapter(adapter);

                final AlertDialog alertDialog2 = new AlertDialog.Builder(OuterActivity.this).setTitle("请选择客户").setView(layout).create();
                alertDialog2.show();

                adapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener()
                {
                    @Override
                    public void onClick(Node node, int position) {
                        if (node.isLeaf()) {
                            cusname = node.getName();
                            tv_custom.setText(cusname);
                            alertDialog2.dismiss();
                        }
                    }
                });
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    class Task extends AsyncTask<Void, Integer, Integer> {


        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            outerlist.clear();
            entrylist.clear();
            dialog=CustomProgress.show(OuterActivity.this,"搜寻中...", true, null);
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {

            // 命名空间
            String nameSpace = "http://tempuri.org/";
            // 调用的方法名称
            String methodName = "JA_select";
            // EndPoint
            String endPoint = UTIL.ENDPOINT;
            // SOAP Action
            String soapAction = "http://tempuri.org/JA_select";

            // 指定WebService的命名空间和调用的方法名
            SoapObject rpc = new SoapObject(nameSpace, methodName);

            // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
            if(cusname!=null) {
                rpc.addProperty("FSql", "select a.fbillno,a.fdate,a.finterid,isnull(a.fcheckerid,0) fcheckerid,c.fname cusname,c.fnumber fsupplynumber,d.fname,d.fnumber,d.fmodel,d.fimage1,b.fauxqty,b.fnote,e.fname stockname,e.fnumber fstocknumber from icstockbill a inner join icstockbillentry b on a.finterid=b.finterid \n" +
                        "inner join t_Organization c on c.fitemid=a.FSupplyID inner join t_icitem d on d.fitemid=b.fitemid\n" +
                        "inner join t_stock e on e.fitemid=b.FDCStockID where a.FTranType=21 and (a.fdate BETWEEN  '" + d1 + "' AND '" + d2 + "') and c.fname like '%" + cusname + "%' order by a.fdate desc ,a.finterid desc");
            }else{
                rpc.addProperty("FSql", "select a.fbillno,a.fdate,a.finterid,isnull(a.fcheckerid,0) fcheckerid,c.fname cusname,c.fnumber fsupplynumber,d.fname,d.fnumber,d.fmodel,d.fimage1,b.fauxqty,b.fnote,e.fname stockname,e.fnumber fstocknumber from icstockbill a inner join icstockbillentry b on a.finterid=b.finterid \n" +
                        "inner join t_Organization c on c.fitemid=a.FSupplyID inner join t_icitem d on d.fitemid=b.fitemid\n" +
                        "inner join t_stock e on e.fitemid=b.FDCStockID where a.FTranType=21 and (a.fdate BETWEEN  '" + d1 + "' AND '" + d2 + "') order by a.fdate desc ,a.finterid desc");
            }
            rpc.addProperty("FTable", "t_icitem");


            // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

            envelope.bodyOut = rpc;
            // 设置是否调用的是dotNet开发的WebService
            envelope.dotNet = true;
            // 等价于envelope.bodyOut = rpc;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(endPoint);
            try {
                // 调用WebService
                transport.call(soapAction, envelope);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("kkkk", e.toString() + "dddddddd");
            }

            // 获取返回的数据
            SoapObject object = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            if (null != object) {
                Log.i("sss", object.getProperty(0).toString() + "");
                String result = object.getProperty(0).toString();
                Document doc = null;
                try {
                    doc = DocumentHelper.parseText(result); // 将字符串转为XML

                    Element rootElt = doc.getRootElement(); // 获取根节点

                    System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称


                    Iterator iter = rootElt.elementIterator("Cust"); // 获取根节点下的子节点head

                    // 遍历head节点
                    String s = "";
                    while (iter.hasNext()) {
                        OuterEntity entity = new OuterEntity();
                        Element recordEle = (Element) iter.next();
                        //日期
                        String FDate = recordEle.elementTextTrim("fdate"); // 拿到head节点下的子节点title值
                        //单号
                        String FBillNo = recordEle.elementTextTrim("fbillno");
                        //客户number
                        String Fnumber = recordEle.elementTextTrim("fsupplynumber");
                        //仓库number
                        String Fnumbers = recordEle.elementTextTrim("fstocknumber");
                        //内码
                        String FInterID = recordEle.elementTextTrim("finterid");
                        //客户名称
                        String fname = recordEle.elementTextTrim("cusname");
                        //仓库名称
                        String stock = recordEle.elementTextTrim("stockname");
                        //审核状态
                        String fcheckerid = recordEle.elementTextTrim("fcheckerid");
                        entity.setFbillno(FBillNo);
                        entity.setFsupplynumber(Fnumber);
                        entity.setFstocknumber(Fnumbers);
                        entity.setFInterId(FInterID);
                        entity.setFdate(FDate);
                        entity.setFstock(stock);
                        entity.setFname(fname);
                        entity.setFcheckerid(fcheckerid);

                        OuterentryEntity bt = new OuterentryEntity();
                        bt.setFqty(recordEle.elementTextTrim("fauxqty"));
                        bt.setFname(recordEle.elementTextTrim("fname"));
                        bt.setFmodel(recordEle.elementTextTrim("fmodel"));
                        bt.setFimage1(recordEle.elementTextTrim("fimage1"));
                        bt.setFnumber(recordEle.elementTextTrim("fnumber"));
                        Log.i("bitmap的值",bt.getFimage1());
                        bt.setFinterid(FInterID);
                        bt.setFnote(recordEle.elementTextTrim("fnote"));
                        entrylist.add(bt);
                        outerlist.add(entity);

                        for (OuterEntity e : outerlist) {
                            List<OuterentryEntity> enlist = new ArrayList<>();
                            for (OuterentryEntity b : entrylist) {
                                if (e.getFInterId().equals(b.getFinterid())) {
                                    enlist.add(b);
                                }
                            }
                            e.setOuterentry(enlist);
                        }
                        List<OuterEntity> cp = new ArrayList<>();

                        for  ( int  i  =   0 ; i  <  outerlist.size()  -   1 ; i ++ )  {
                            for  ( int  j  =  outerlist.size()  -   1 ; j  >  i; j -- )  {
                                if  (outerlist.get(j).getFbillno().equals(outerlist.get(i).getFbillno()))  {
                                    outerlist.remove(j);
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                }
            }
            return 3;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer integer) {
            dialog.dismiss();
            adapter.notifyDataSetChanged();
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }


    public static class CheckTask extends AsyncTask<Void, Integer, Integer> {
        String fcheckerid;
        String interid;

        public CheckTask(String fcheckerid,String interid){
            this.fcheckerid = fcheckerid;
            this.interid = interid;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Integer doInBackground(Void... params) {
            try{
            // 命名空间
            String nameSpace = "http://tempuri.org/";
            // 调用的方法名称
            String methodName = "CHECK";
            // EndPoint
            String endPoint = UTIL.ENDPOINT;
            // SOAP Action
            String soapAction = "http://tempuri.org/CHECK";

            // 指定WebService的命名空间和调用的方法名
            SoapObject rpc = new SoapObject(nameSpace, methodName);

            // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
            rpc.addProperty("FTranType", "21");
            rpc.addProperty("FCheckerID", fcheckerid);
            rpc.addProperty("FInterID", interid);
            System.out.println(interid);
            // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

            envelope.bodyOut = rpc;
            // 设置是否调用的是dotNet开发的WebService
            envelope.dotNet = true;
            // 等价于envelope.bodyOut = rpc;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(endPoint);
            try {
                // 调用WebService
                transport.call(soapAction, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 获取返回的数据
            SoapObject object = (SoapObject) envelope.bodyIn;
            // 获取返回的结果
            String result = object.getProperty(0).toString();
            Log.i("审核返回结果", result + "----------------------------");
            return 1;
        } catch (Exception e) {
            Log.i("发生的错误", e.toString() + "++++++");
            return 2;
        }
        }

        @Override
        protected void onPostExecute(Integer integer) {

        }
    }

}
