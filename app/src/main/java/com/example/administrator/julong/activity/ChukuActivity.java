package com.example.administrator.julong.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.julong.R;
import com.example.administrator.julong.adapter.SimpleTreeAdapter;
import com.example.administrator.julong.adapter.TreeListViewAdapter;
import com.example.administrator.julong.entity.FileBean;
import com.example.administrator.julong.entity.Node;
import com.example.administrator.julong.entity.OuterEntity;
import com.example.administrator.julong.entity.OuterentryEntity;
import com.example.administrator.julong.view.CustomProgress;
import com.example.administrator.julong.view.SideslipListView;
import com.example.administrator.julong.util.UTIL;
import com.example.administrator.julong.adapter.ChukuApater;
import com.example.administrator.julong.adapter.WLApater;
import com.example.administrator.julong.adapter.WLApater3;
import com.example.administrator.julong.entity.WLEntity;

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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ChukuActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CustomProgress dialog;
    private TextView tv_cus,tv_dates,tv_fhck;
    private Button bt_xgrq,bt_search_wl,bt_submit;
    private EditText et_wl;
    DecimalFormat df = new DecimalFormat("######0");
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private Date date = new Date();
    private int year;
    private int month;
    private int day;
    private String cusname,FSUPPLYNUMBER,FSTOCKNUMBER,stockname,wlname,fstockid,fitemid,fqty;
    private List<FileBean> list = new ArrayList<>();
    private List<WLEntity> list2 = new ArrayList<>();
    private List<WLEntity> list1 = new ArrayList<>();
    private ChukuApater adapter;
    private SideslipListView lv;
    private String inter = "0",bill="a",dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuku);
        setTool();
        setViews();
        setListeners();
        Intent intent = getIntent();
        List<OuterentryEntity> entrylist = (List<OuterentryEntity>) intent.getSerializableExtra("list");
        if(entrylist!=null){
            OuterEntity outer = (OuterEntity) intent.getSerializableExtra("outer");
            tv_cus.setText(outer.getFname());
            tv_fhck.setText(outer.getFstock());
            dates=outer.getFdate().substring(0,10);
            tv_dates.setText(dates);
            inter = outer.getFInterId();
            bill = outer.getFbillno();
            FSUPPLYNUMBER = outer.getFsupplynumber();
            FSTOCKNUMBER = outer.getFstocknumber();

            for(OuterentryEntity e : entrylist){
                WLEntity wlEntity = new WLEntity();
                wlEntity.setFname(e.getFname());
                String qty = e.getFqty();
                wlEntity.setJs(Integer.parseInt(qty.substring(0,qty.indexOf("."))));
                wlEntity.setBeizhu(e.getFnote());
                wlEntity.setImg(e.getFimage1());
                wlEntity.setFnumber(e.getFnumber());
                Log.i("sss", wlEntity.getFname() + "++++");
                list1.add(wlEntity);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void setTool() {
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("出库单");

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
        tv_cus = (TextView) findViewById(R.id.tv_cus);
        tv_dates = (TextView) findViewById(R.id.tv_dates);
        bt_xgrq = (Button) findViewById(R.id.bt_xgrq);
        bt_search_wl = (Button) findViewById(R.id.bt_search_wl);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        tv_fhck = (TextView) findViewById(R.id.tv_fhck);
        et_wl = (EditText) findViewById(R.id.et_wl);
        tv_dates.setText(format.format(date));
        dates = format.format(date);
        Calendar mycalendar = Calendar.getInstance();
        year = mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        month = mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        day = mycalendar.get(Calendar.DAY_OF_MONTH);
        FSUPPLYNUMBER = "000";
        lv = (SideslipListView) findViewById(R.id.lv_ck);
        adapter = new ChukuApater(ChukuActivity.this, list1);
        lv.setAdapter(adapter);
    }

    protected void setListeners(){
        bt_xgrq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(ChukuActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String s = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        try {
                            java.util.Date date = format.parse(s);
                            String s1 = format.format(date);
                            tv_dates.setText(s1);
                            dates = s1;
                        } catch (Exception e) {
                        }
                    }
                }, year, month, day);
                dpd.show();//显示DatePickerDialog组件
            }
        });

        tv_cus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(ChukuActivity.this);
                et.setBackgroundResource(R.drawable.et_shapes);
                new AlertDialog.Builder(ChukuActivity.this).setTitle("请输入客户").setIcon(
                        R.drawable.customs).setView(
                        et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cusname = et.getText().toString().trim();
                        Task task = new Task();
                        task.execute();
                    }
                })
                        .setNegativeButton("取消", null).show();
            }
        });

        tv_fhck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et1 = new EditText(ChukuActivity.this);
                et1.setBackgroundResource(R.drawable.et_shapes);
                new AlertDialog.Builder(ChukuActivity.this).setTitle("请输入仓库").setIcon(
                        R.drawable.stock).setView(
                        et1).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stockname = et1.getText().toString().trim();
                        Task1 task1 = new Task1();
                        task1.execute();
                    }
                })
                        .setNegativeButton("取消", null).show();
            }
        });

        bt_search_wl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wlname = et_wl.getText().toString().trim();
                Task2 task2 = new Task2();
                task2.execute();
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_cus.getText().toString().equals("")) {
                    Toast.makeText(ChukuActivity.this, "客戶不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (list1.size() == 0) {
                    Toast.makeText(ChukuActivity.this, "物料条数不能为0", Toast.LENGTH_LONG).show();
                    return;
                }
                Task3 task3 = new Task3();
                task3.execute();
            }
        });
    }


    class Task extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            dialog=CustomProgress.show(ChukuActivity.this,"搜寻中...", true, null);
            list.clear();
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
            rpc.addProperty("FSql","select fname,fnumber,fitemid,fparentid from t_item where fname like '%"+cusname+"%' and fitemclassid=1 and (fitemid in (select fparentid from t_item where fitemclassid=1) or fparentid!=0)\n");
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
                        list.add(entity);
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
        protected void onPostExecute(Integer integer) {
            dialog.dismiss();
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.activity_tree, null);
            ListView lv = (ListView) layout.findViewById(R.id.lv_tree);
            final TreeListViewAdapter adapter;
            try{
                adapter = new SimpleTreeAdapter<FileBean>(lv, ChukuActivity.this, list, 10);
                lv.setAdapter(adapter);

                final AlertDialog alertDialog2 = new AlertDialog.Builder(ChukuActivity.this).setTitle("请选择客户").setView(layout).create();
                alertDialog2.show();

                adapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener()
                {
                    @Override
                    public void onClick(Node node, int position) {
                        if (node.isLeaf()) {
                            tv_cus.setText(node.getName());
                            FSUPPLYNUMBER = node.getFnumber();
                            cusname = node.getName();
                            alertDialog2.dismiss();
                        }
                    }
                    });
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }


        class Task1 extends AsyncTask<Void, Integer, Integer> {
            @Override
            protected void onPreExecute() {
                dialog=CustomProgress.show(ChukuActivity.this,"搜寻中...", true, null);
                list.clear();
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
//                rpc.addProperty("FSql", "select FNAME ,FNUMBER,FItemID from t_stock where fname like '%" + stockname + "%'");
                rpc.addProperty("FSql", "select FNAME ,FNUMBER,FItemID,fparentid from t_item where fname like '%" + stockname + "%' and fitemclassid=5");
                rpc.addProperty("FTable", "t_stock");

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
                    Document doc = null;
                    try {
                        doc = DocumentHelper.parseText(result); // 将字符串转为XML

                        Element rootElt = doc.getRootElement(); // 获取根节点

                        System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称

                        Iterator iter = rootElt.elementIterator("Cust"); // 获取根节点下的子节点head

                        // 遍历head节点

                        while (iter.hasNext()) {
                            Element recordEle = (Element) iter.next();
                            String fname = recordEle.elementTextTrim("FNAME"); // 拿到head节点下的子节点title值
                            String fstockid = recordEle.elementTextTrim("FItemID");
                            String fnumber = recordEle.elementTextTrim("FNUMBER");
                            String fparentid = recordEle.elementTextTrim("fparentid");
                            FileBean entity = new FileBean(Integer.parseInt(fstockid),Integer.parseInt(fparentid),fname,null,fnumber,null);
                            list.add(entity);
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
            protected void onPostExecute(Integer integer) {
                dialog.dismiss();
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.activity_tree, null);
                ListView lv = (ListView) layout.findViewById(R.id.lv_tree);
                final TreeListViewAdapter adapter;
                try{
                    adapter = new SimpleTreeAdapter<FileBean>(lv, ChukuActivity.this, list, 10);
                    lv.setAdapter(adapter);

                    final AlertDialog alertDialog2 = new AlertDialog.Builder(ChukuActivity.this).setTitle("请选择客户").setView(layout).create();
                    alertDialog2.show();

                    adapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                        @Override
                        public void onClick(Node node, int position) {
                            if (node.isLeaf()) {
                                tv_fhck.setText(node.getName());
                                FSTOCKNUMBER = node.getFnumber();
                                stockname = node.getName();
                                fstockid = node.getId()+"";
                                alertDialog2.dismiss();
                            }
                        }
                    });
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

    /**
     * 物料搜索
     */
    class Task2 extends AsyncTask<Void, Integer, Integer> {
        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            dialog=CustomProgress.show(ChukuActivity.this,"搜寻中...", true, null);
            list.clear();
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
            rpc.addProperty("FSql", "select a.fname,a.fnumber,a.fitemid,a.fparentid,b.fmodel,b.fimage1 from t_item a left join t_icitem b on a.fitemid=b.fitemid  where a.fitemclassid=4 and  left(left(a.ffullname,CHARINDEX('_', a.ffullname)),2)=(select fdescription from t_user where fname='"+UTIL.FNAME+"') and a.fname like '%" + wlname + "%'");
//            rpc.addProperty("FSql", "select a.fname,a.fnumber ,a.fmodel ,a.fimage1 from  t_icitem a inner join t_user b on a.fnote=b.fdescription where a.fname like '%" + wlname + "%' and b.fname='"+UTIL.FNAME+"'order by a.fnumber");
            //  rpc.addProperty("FSql", "select b.fname,b.fnumber ,b.fmodel ,a.fimage  from z_image a inner join t_icitem b on a.fnumber=b.fnumber where b.fname like '%"+name+"%'  ");
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
            }

            // 获取返回的数据
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (null != object) {
                // 获取返回的结果
                String result = object.getProperty(0).toString();
                Log.i("sss", result + "sss");

                Document doc = null;
                try {
                    doc = DocumentHelper.parseText(result); // 将字符串转为XML

                    Element rootElt = doc.getRootElement(); // 获取根节点

                    System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称

                    Iterator iter = rootElt.elementIterator("Cust"); // 获取根节点下的子节点head

                    System.out.println("内容:"+iter);

                    // 遍历head节点

                    while (iter.hasNext()) {
                        Element recordEle = (Element) iter.next();
                        String fname = recordEle.elementTextTrim("fname"); // 拿到head节点下的子节点title值
                        String fnumber = recordEle.elementTextTrim("fnumber");
                        String fmodel = recordEle.elementTextTrim("fmodel");
                        String fimage = recordEle.elementTextTrim("fimage1");
                        String fitemid = recordEle.elementTextTrim("fitemid");
                        String fparentid = recordEle.elementTextTrim("fparentid");
                        FileBean entity = new FileBean(Integer.parseInt(fitemid),Integer.parseInt(fparentid),fname,fimage,fnumber,fmodel);
                        list.add(entity);
                    }
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(list.size()==0){
                // 指定WebService的命名空间和调用的方法名
                SoapObject rpc1 = new SoapObject(nameSpace, methodName);
                // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
                rpc1.addProperty("FSql", "select a.fname,a.fnumber,a.fitemid,a.fparentid,b.fmodel,b.fimage1 from t_item a left join t_icitem b on a.fitemid=b.fitemid  where a.fitemclassid=4 and a.fname like '%" + wlname + "%'");
//                rpc1.addProperty("FSql", "select fname,fnumber ,fmodel ,fimage1 from  t_icitem where fname like '%" + wlname + "%' order by fnumber");
                //  rpc.addProperty("FSql", "select b.fname,b.fnumber ,b.fmodel ,a.fimage  from z_image a inner join t_icitem b on a.fnumber=b.fnumber where b.fname like '%"+name+"%'  ");
                rpc1.addProperty("FTable", "t_icitem");
                // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
                SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(SoapEnvelope.VER10);

                envelope1.bodyOut = rpc1;
                // 设置是否调用的是dotNet开发的WebService
                envelope1.dotNet = true;
                // 等价于envelope.bodyOut = rpc;
                envelope1.setOutputSoapObject(rpc1);

                HttpTransportSE transport1 = new HttpTransportSE(endPoint);
                try {
                    // 调用WebService
                    transport.call(soapAction, envelope1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 获取返回的数据
                SoapObject object1 = (SoapObject) envelope1.bodyIn;
                if (null != object1) {
                    // 获取返回的结果
                    String result1 = object1.getProperty(0).toString();
                    Log.i("sss", result1 + "sss");

                    Document doc1 = null;
                    try {
                        doc1 = DocumentHelper.parseText(result1); // 将字符串转为XML

                        Element rootElt1 = doc1.getRootElement(); // 获取根节点

                        System.out.println("根节点：" + rootElt1.getName()); // 拿到根节点的名称


                        Iterator iter1 = rootElt1.elementIterator("Cust"); // 获取根节点下的子节点head

                        System.out.println("内容:"+iter1);

                        // 遍历head节点

                        while (iter1.hasNext()) {
                            Element recordEle1 = (Element) iter1.next();
                            String fname = recordEle1.elementTextTrim("fname"); // 拿到head节点下的子节点title值
                            String fnumber = recordEle1.elementTextTrim("fnumber");
                            String fmodel = recordEle1.elementTextTrim("fmodel");
                            String fimage = recordEle1.elementTextTrim("fimage1");
                            String fitemid = recordEle1.elementTextTrim("fitemid");
                            String fparentid = recordEle1.elementTextTrim("fparentid");
                            FileBean entity1 = new FileBean(Integer.parseInt(fitemid),Integer.parseInt(fparentid),fname,fimage,fnumber,fmodel);
                            list.add(entity1);
                        }
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return 5;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer integer) {
            dialog.dismiss();
            if (list.size() == 0) {
                Toast.makeText(ChukuActivity.this, "找不到物料", Toast.LENGTH_LONG).show();
                return;
            }
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.activity_tree, null);
            ListView lv1 = (ListView) layout.findViewById(R.id.lv_tree);

            final TreeListViewAdapter adapter1;

            try {
                adapter1 = new SimpleTreeAdapter<FileBean>(lv1, ChukuActivity.this, list, 10);
                lv1.setAdapter(adapter1);
                final AlertDialog alertDialog2 = new AlertDialog.Builder(ChukuActivity.this).setTitle("请选择物料").setView(layout).create();
                alertDialog2.show();

                adapter1.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                    @Override
                    public void onClick(final Node node, int position) {
                        if(node.isLeaf()){
                            if (fstockid==null) {
                                alertDialog2.dismiss();
                                Toast.makeText(ChukuActivity.this, "请先选择仓库", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            fitemid = String.valueOf(node.getId());
                            alertDialog2.dismiss();
                            Task4 task4 = new Task4();
                            task4.execute();
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.dialog4, null);
                            final EditText et1 = (EditText) layout.findViewById(R.id.et1);
                            final EditText et3 = (EditText) layout.findViewById(R.id.et3);
                            final EditText et4 = (EditText) layout.findViewById(R.id.et4);
                            final EditText et5 = (EditText) layout.findViewById(R.id.et5);
                            final EditText et6 = (EditText) layout.findViewById(R.id.et6);
                            AlertDialog alertDialog = new AlertDialog.Builder(ChukuActivity.this).setTitle("请完成 ").setView(layout)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (et1.getText().toString().equals("")) {
                                                Toast.makeText(ChukuActivity.this, "数量不能为空", Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                            if (et3.getText().toString().equals("")) {
                                                Toast.makeText(ChukuActivity.this, "体积不能为空", Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                            if (et4.getText().toString().equals("")) {
                                                Toast.makeText(ChukuActivity.this, "单重不能为空", Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                            if (et5.getText().toString().equals("")) {
                                                Toast.makeText(ChukuActivity.this, "重量不能为空", Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                            System.out.println("当前库存："+fqty+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//                                    et1.setHint("当前库存："+df.format(Double.parseDouble(fqty)));
                                            if(Integer.parseInt(df.format(Double.parseDouble(et1.getText().toString())))>Integer.parseInt(df.format(Double.parseDouble(fqty)))){
                                                Toast.makeText(ChukuActivity.this,"所输数量超出库存，当前库存为"+df.format(Double.parseDouble(fqty)), Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            WLEntity entity = new WLEntity();
                                            entity.setFname(node.getName());
                                            entity.setFnumber(node.getFnumber());
                                            entity.setFitemid(String.valueOf(node.getId()));
                                            entity.setImg(node.getFimage());
                                            entity.setGg(node.getFmodel());
                                            entity.setJs(Integer.parseInt(df.format(Double.parseDouble(et1.getText().toString()))));
                                            entity.setTiji(Double.parseDouble(et3.getText().toString()));
                                            entity.setDz(et4.getText().toString());
                                            entity.setDh(et5.getText().toString());
                                            entity.setBeizhu(et6.getText().toString() + "");
                                            list1.add(entity);
                                            adapter = new ChukuApater(ChukuActivity.this, list1);
                                            lv.setAdapter(adapter);
                                        }
                                    }).create();
                            alertDialog.show();
                        }
                    }
                });
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }


    class Task3 extends AsyncTask<Void, Integer, Integer> {
        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {

        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {
            try {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String no = "CK" + format.format(date);
                // 命名空间
                String nameSpace = "http://tempuri.org/";
                // 调用的方法名称
                String methodName = "F_SendBill_21";
                // EndPoint
                String endPoint = UTIL.ENDPOINT;
                // SOAP Action
                String soapAction = "http://tempuri.org/F_SendBill_21";

                // 指定WebService的命名空间和调用的方法名
                SoapObject rpc = new SoapObject(nameSpace, methodName);

                // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
                rpc.addProperty("FTrantype", "21");
                rpc.addProperty("FSBILLNO", no);
                rpc.addProperty("InterID", inter);
                rpc.addProperty("BillNO", bill);
                //表头
                Document document = DocumentHelper.createDocument();
                Element rootElement = document.addElement("NewDataSet");
                Element cust = rootElement.addElement("Cust");
                cust.addElement("FDATE").setText(dates);
                cust.addElement("FSUPPLYNUMBER").setText(FSUPPLYNUMBER);
                cust.addElement("FSTOCKNUMBER").setText(FSTOCKNUMBER);
                //表体
                Document document2 = DocumentHelper.createDocument();
                Element rootElement2 = document2.addElement("NewDataSet");
                for (WLEntity e : list1) {
                    Log.i("各个物料的fnumber", e.getFnumber() + "sss");
                    Element cust2 = rootElement2.addElement("Cust");
                    cust2.addElement("FNUMBER").setText(e.getFnumber());
                    cust2.addElement("FQTY").setText(e.getJs() + "");
                    cust2.addElement("FNOTE").setText(e.getBeizhu() + "");
                    cust2.addElement("FEntrySelfB0136").setText(e.getTiji() + "");
                    cust2.addElement("FEntrySelfB0137").setText(e.getDz() + "");
                    cust2.addElement("FEntrySelfB0138").setText(e.getDh() + "");
                }

                OutputFormat outputFormat = OutputFormat.createPrettyPrint();
                outputFormat.setSuppressDeclaration(false);
                outputFormat.setNewlines(false);
                StringWriter stringWriter = new StringWriter();
                StringWriter stringWriter2 = new StringWriter();
                // xmlWriter是用来把XML文档写入字符串的(工具)
                XMLWriter xmlWriter = new XMLWriter(stringWriter, outputFormat);
                XMLWriter xmlWriter2 = new XMLWriter(stringWriter2, outputFormat);
                // 把创建好的XML文档写入字符串
                xmlWriter.write(document);
                xmlWriter2.write(document2);
                Log.i("表头", stringWriter.toString().substring(38) + "***********");
                Log.i("表体", stringWriter2.toString().substring(38) + "***********");
                rpc.addProperty("FBtouXMl", stringWriter.toString().substring(38));
                rpc.addProperty("FBtiXML", stringWriter2.toString().substring(38));
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
                Log.i("提交出库单的结果", result + "----------------------------");
                return 1;
            } catch (Exception e) {
                Log.i("发生的错误", e.toString() + "++++++");
                return 2;
            }
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer integer) {
            if(integer.equals(1)) {
                list1.clear();
                adapter.notifyDataSetChanged();
                Toast.makeText(ChukuActivity.this, "提交成功", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(ChukuActivity.this, "提交失败", Toast.LENGTH_LONG).show();
            }
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

    class Task4 extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected void onPreExecute() {

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
            rpc.addProperty("FSql", "select isnull(sum(FQty),0) fqty from ICInventory where fitemid = "+fitemid+" and FStockID="+fstockid);
            rpc.addProperty("FTable", "ICInventory");

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

                Document doc = null;
                try {
                    doc = DocumentHelper.parseText(result); // 将字符串转为XML

                    Element rootElt = doc.getRootElement(); // 获取根节点

                    System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称

                    Iterator iter = rootElt.elementIterator("Cust"); // 获取根节点下的子节点head

                    // 遍历head节点

                    while (iter.hasNext()) {
                        Element recordEle = (Element) iter.next();
                        fqty = recordEle.elementTextTrim("fqty"); // 拿到head节点下的子节点title值
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
        protected void onPostExecute(Integer integer) {

        }
    }
}
