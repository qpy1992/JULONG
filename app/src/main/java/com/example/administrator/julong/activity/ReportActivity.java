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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.administrator.julong.R;
import com.example.administrator.julong.adapter.ReportAdapter;
import com.example.administrator.julong.adapter.SimpleTreeAdapter;
import com.example.administrator.julong.adapter.TreeListViewAdapter;
import com.example.administrator.julong.adapter.WLApater;
import com.example.administrator.julong.entity.FileBean;
import com.example.administrator.julong.entity.Node;
import com.example.administrator.julong.entity.ReportEntity;
import com.example.administrator.julong.entity.WLEntity;
import com.example.administrator.julong.util.DateUtil;
import com.example.administrator.julong.util.UTIL;
import com.example.administrator.julong.view.CustomProgress;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tv_begin,tv_end,tv_item;
    private ImageView iv_search;
    private int year,month,day;
    private String d1, d2,item;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private List<FileBean> listwl = new ArrayList<>();
    private List<ReportEntity> list = new ArrayList<>();
    private ListView lv;
    private ReportAdapter adapter;
    private CustomProgress dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setTool();
        setViews();
        setListeners();
    }

    private void setTool() {
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("报表查询");

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
        tv_begin = (TextView) findViewById(R.id.tv_begin);
        tv_end = (TextView) findViewById(R.id.tv_end);
        tv_item = (TextView) findViewById(R.id.tv_item);
        iv_search = (ImageView) findViewById(R.id.iv_search8);
        Calendar mycalendar = Calendar.getInstance();
        year = mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        month = mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        day = mycalendar.get(Calendar.DAY_OF_MONTH);
        Date date = new Date(System.currentTimeMillis());
        tv_begin.setText(DateUtil.getPastDate(15));
        tv_end.setText(DateUtil.getFetureDate(1));
        d1 = DateUtil.getPastDate(7);
        d2 = sdf.format(date);
        lv = (ListView) findViewById(R.id.lv_item);
        adapter = new ReportAdapter(this,list);
        lv.setAdapter(adapter);
    }

    protected void setListeners(){
        tv_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(ReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String s = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        try {
                            java.util.Date date = sdf.parse(s);
                            String s1 = sdf.format(date);
                            tv_begin.setText(s1);
                            d1 = s1;
                        } catch (Exception e) {
                        }
                    }
                }, year, month, day);
                dpd.show();//显示DatePickerDialog组件
            }
        });
        tv_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(ReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String s = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        try {
                            java.util.Date date = sdf.parse(s);
                            String s1 = sdf.format(date);
                            tv_end.setText(s1);
                            d2 = s1;
                        } catch (Exception e) {
                        }
                    }
                }, year, month, day);
                dpd.show();//显示DatePickerDialog组件
            }
        });

        tv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(ReportActivity.this);
                et.setBackgroundResource(R.drawable.et_shapes);
                new AlertDialog.Builder(ReportActivity.this).setTitle("请输入物料").setIcon(
                        R.drawable.items).setView(
                        et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        item = et.getText().toString().trim();
                        ItemTask task = new ItemTask();
                        task.execute();
                    }
                })
                        .setNegativeButton("取消", null).show();
            }
        });

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                task.execute();
            }
        });
    }


    class ItemTask extends AsyncTask<Void,Integer,Integer> {

        @Override
        protected void onPreExecute() {
            dialog=CustomProgress.show(ReportActivity.this,"搜寻中...", true, null);
            listwl.clear();
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
            rpc.addProperty("FSql", "select a.fname,a.fnumber,a.fitemid,a.fparentid,b.fimage1 from t_item a left join t_icitem b on a.fitemid=b.fitemid  where a.fitemclassid=4 and  left(left(a.ffullname,CHARINDEX('_', a.ffullname)),2)=(select fdescription from t_user where fname='"+UTIL.FNAME+"') and a.fname like '%" + item + "%'");
//            rpc.addProperty("FSql", "select FNAME ,fimage1,FNUMBER from t_icitem where fname like '%" + item + "%'");
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
                        String fimage1 = recordEle.elementTextTrim("fimage1");
                        String fitemid = recordEle.elementTextTrim("fitemid");
                        String fparentid = recordEle.elementTextTrim("fparentid");
                        FileBean entity = new FileBean(Integer.parseInt(fitemid),Integer.parseInt(fparentid),fname,fimage1,fnumber,null);
                        listwl.add(entity);
                    }
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(listwl.size()==0){
                // 指定WebService的命名空间和调用的方法名
                SoapObject rpc1 = new SoapObject(nameSpace, methodName);

                // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
                rpc1.addProperty("FSql", "select a.fname,a.fnumber,a.fitemid,a.fparentid,b.fimage1 from t_item a left join t_icitem b on a.fitemid=b.fitemid  where a.fitemclassid=4 and a.fname like '%" + item + "%'");
//                rpc1.addProperty("FSql", "select fname,fnumber ,fmodel ,fimage1 from  t_icitem where fname like '%" + name + "%' order by fnumber");
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

//
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
                            String fimage = recordEle1.elementTextTrim("fimage1");
                            String fitemid = recordEle1.elementTextTrim("fitemid");
                            String fparentid = recordEle1.elementTextTrim("fparentid");
                            FileBean entity1 = new FileBean(Integer.parseInt(fitemid),Integer.parseInt(fparentid),fname,fimage,fnumber,null);
                            listwl.add(entity1);
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

        @Override
        protected void onPostExecute(Integer s) {
            dialog.dismiss();
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.activity_tree, null);
            ListView lv = (ListView) layout.findViewById(R.id.lv_tree);

            final TreeListViewAdapter adapter;

            try {
                adapter = new SimpleTreeAdapter<FileBean>(lv, ReportActivity.this, listwl, 10);


            lv.setAdapter(adapter);
            final AlertDialog alertDialog2 = new AlertDialog.Builder(ReportActivity.this).setTitle("请选择物料").setView(layout).create();
            alertDialog2.show();

            adapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(Node node, int position) {
                    if(node.isLeaf()){
                        item = node.getName();
                        tv_item.setText(item);
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
            dialog=CustomProgress.show(ReportActivity.this,"搜寻中...", true, null);
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
            if(item!=null) {
                rpc.addProperty("FSql", "select a.fname,a.fmodel,isnull(b.fqty,0) fqty,isnull(c.fqtyrk,0) fqtyrk,isnull(d.fqtyck,0) fqtyck from t_icitem a left join " +
                        "(select fitemid,sum(fqty) fqty from poorderentry a inner join poorder b on a.finterid=b.finterid where b.fdate BETWEEN  '" + d1 + "' AND '" + d2 + "' group by fitemid) b on b.fitemid=a.fitemid left join " +
                        "(select fitemid,sum(fqty) fqtyrk from icstockbill a inner join icstockbillentry b on a.finterid=b.finterid where ftrantype=1 and a.fdate BETWEEN  '" + d1 + "' AND '" + d2 + "' group by fitemid) c on c.fitemid=a.fitemid left join " +
                        "(select fitemid,sum(fqty) fqtyck from icstockbill a inner join icstockbillentry b on a.finterid=b.finterid where ftrantype=21 and a.fdate BETWEEN  '" + d1 + "' AND '" + d2 + "' group by fitemid) d on d.fitemid=a.fitemid where a.fname like '%" + item + "%'");//
            }else{
                rpc.addProperty("FSql", "select a.fname,a.fmodel,isnull(b.fqty,0) fqty,isnull(c.fqtyrk,0) fqtyrk,isnull(d.fqtyck,0) fqtyck from t_icitem a left join " +
                        "(select fitemid,sum(fqty) fqty from poorderentry a inner join poorder b on a.finterid=b.finterid where b.fdate BETWEEN  '" + d1 + "' AND '" + d2 + "' group by fitemid)b on b.fitemid=a.fitemid left join " +
                        "(select fitemid,sum(fqty) fqtyrk from icstockbill a inner join icstockbillentry b on a.finterid=b.finterid where ftrantype=1 and a.fdate BETWEEN  '" + d1 + "' AND '" + d2 + "' group by fitemid) c on c.fitemid=a.fitemid left join " +
                        "(select fitemid,sum(fqty) fqtyck from icstockbill a inner join icstockbillentry b on a.finterid=b.finterid where ftrantype=21 and a.fdate BETWEEN  '" + d1 + "' AND '" + d2 + "' group by fitemid)d on d.fitemid=a.fitemid");
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
                Log.i("报表查询的结果", object.getProperty(0).toString() + "");
                String result = object.getProperty(0).toString();
                Document doc = null;
                try {
                    doc = DocumentHelper.parseText(result); // 将字符串转为XML
                    Element rootElt = doc.getRootElement(); // 获取根节点
                    System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称
                    Iterator iter = rootElt.elementIterator("Cust"); // 获取根节点下的子节点head

                    while (iter.hasNext()) {
                        ReportEntity entity = new ReportEntity();
                        Element recordEle = (Element) iter.next();
                        String Fname = recordEle.elementTextTrim("fname");
                        String fmodel = recordEle.elementTextTrim("fmodel");
                        String fqty = recordEle.elementTextTrim("fqty");
                        String fqtyrk = recordEle.elementTextTrim("fqtyrk");
                        String fqtyck = recordEle.elementTextTrim("fqtyck");
                        entity.setFname(Fname);
                        entity.setFmodel(fmodel);
                        entity.setOrdernum(fqty);
                        entity.setInnernum(fqtyrk);
                        entity.setOuternum(fqtyck);
                        System.out.println(entity);
                        list.add(entity);

                    }
                } catch (Exception e) {
                }
            }
            return 3;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            dialog.dismiss();
            adapter.notifyDataSetChanged();
        }
    }
}
