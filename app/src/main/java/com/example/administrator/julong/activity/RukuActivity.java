package com.example.administrator.julong.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.julong.R;
import com.example.administrator.julong.adapter.WLApater3;
import com.example.administrator.julong.adapter.WLApater4;
import com.example.administrator.julong.entity.WLEntity;
import com.example.administrator.julong.util.Consts;
import com.example.administrator.julong.util.UTIL;
import com.example.administrator.julong.adapter.RukuAdapter;
import com.example.administrator.julong.entity.RukuEntity;

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

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class RukuActivity extends AppCompatActivity {
    private ListView lv_ruku;
    private List<RukuEntity> list = new ArrayList<>();
    private List<WLEntity> list1 = new ArrayList<>();
    private RukuAdapter adapter;
    private Button btn_sub;
    private String fbillno,stockname,stockid,maitou;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog3);
        setTool();
        Intent intent = getIntent();
        fbillno = intent.getStringExtra("fbillno");
        maitou = intent.getStringExtra("maitou");
        System.out.println("唛头名称:"+maitou+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        Tasks task = new Tasks();
        task.execute();
//        setView();
//        setListener();
    }

    private void setTool() {
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("入库单");

        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setView() {
        lv_ruku = (ListView) findViewById(R.id.lv_ruku);
        btn_sub = (Button)findViewById(R.id.btn_sub);
        adapter = new RukuAdapter(RukuActivity.this,list);
        lv_ruku.setAdapter(adapter);
        System.out.println("最终的list:"+list+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }

    public void setListener(){
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final EditText et1 = new EditText(RukuActivity.this);
//                new AlertDialog.Builder(RukuActivity.this).setTitle("请输入仓库").setIcon(
//                        android.R.drawable.ic_dialog_info).setView(
//                        et1).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        stockname = et1.getText().toString().trim();
//                        Task1 task1 = new Task1();
//                        task1.execute();
//                    }
//                })
//                        .setNegativeButton("取消", null).show();

                //2017-12-05改动  根据唛头自动选仓库
                switch(maitou){
                    case "唛头:C":
                        //智利仓库
                        stockname = Consts.Chile;
                        stockid = Consts.Chile_id;
                        break;
                    case "唛头:M":
                        //秘鲁仓库
                        stockname = Consts.Peru;
                        stockid = Consts.Peru_id;
                        break;
                    case "唛头:I":
                        //伊基克
                        stockname = Consts.Iquique;
                        stockid = Consts.Iquique_id;
                        break;
                    case "唛头:Q":
                        //Q仓库
                        stockname = Consts.Q;
                        stockid = Consts.Q_id;
                        break;
                    case "唛头:G":
                        //G仓库
                        stockname = Consts.G;
                        stockid = Consts.G_id;
                        break;
                    case "唛头:MX":
                        //墨西哥仓库
                        stockname = Consts.Mexico;
                        stockid = Consts.Mexico_id;
                        break;
                }
                System.out.println("当前的仓库id："+stockid+"当前的仓库："+stockname);
                for(int i=0;i<list.size();i++){
                    View view1 = lv_ruku.getChildAt(i);
                    final TextView ets = (TextView) view1.findViewById(R.id.tv_num);
                    EditText etj = (EditText) view1.findViewById(R.id.et_tj);
                    final EditText etx = (EditText) view1.findViewById(R.id.et_xs);
                    final EditText etxs = (EditText) view1.findViewById(R.id.et_xs1);
                    Log.i("获取的数量",ets.getText().toString());
                    String oldnum = list.get(i).getFnumber();
                    if(Integer.parseInt(oldnum)<Integer.parseInt(ets.getText().toString())){
                        Toast.makeText(RukuActivity.this,"所输数量超出未入库数量",Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        list.get(i).setFnumber(ets.getText().toString());
                        list.get(i).setFtiji(etj.getText().toString());
                        list.get(i).setFxs(etx.getText().toString());
                        list.get(i).setFxs1(etxs.getText().toString());
                    }
                }
                Tasks1 t = new Tasks1(fbillno);
                t.execute();
            }
        });
    }


    public class Tasks extends AsyncTask<Void, Integer, Integer> {

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
            Log.i("执行异步方法","啦啦啦啦啦啦啦啦");
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
            rpc.addProperty("FSql", "SELECT a.fitemid,a.fqty-isnull(a.fentryselfp0231,0) fqty,a.FEntrySelfP0229,a.FEntrySelfP0235,a.FEntrySelfP0234,c.fname FROM POOrderEntry a inner join poorder b on a.finterid=b.finterid inner join t_icitem c on c.fitemid=a.fitemid  where fbillno='"+fbillno+"'");
            rpc.addProperty("FTable", "POOrderEntry");
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
                Log.i("查询订单物料的结果", result + "sss");
//
                Document doc = null;
                try {
                    doc = DocumentHelper.parseText(result); // 将字符串转为XML
                    Element rootElt = doc.getRootElement(); // 获取根节点
                    System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称
                    Iterator iter = rootElt.elementIterator("Cust"); // 获取根节点下的子节点head
                    // 遍历head节点
                    while (iter.hasNext()) {
                        RukuEntity entity = new RukuEntity();
                        Element recordEle = (Element) iter.next();
                        String fname = recordEle.elementTextTrim("fname");
                        String fitemid = recordEle.elementTextTrim("fitemid");
                        String fnumber = recordEle.elementTextTrim("fqty");
                        fnumber = fnumber.substring(0,fnumber.indexOf("."));
                        String ftiji = recordEle.elementTextTrim("FEntrySelfP0229");
                        String fxs = recordEle.elementTextTrim("FEntrySelfP0235");
                        String fjs = recordEle.elementTextTrim("FEntrySelfP0234");
                        Log.i("qqq", fname);
                        Log.i("数量", fnumber);
                        System.out.println("title:" + fname);
                        entity.setFname(fname);
                        entity.setFitemid(fitemid);
                        entity.setFnumber(fnumber);
                        entity.setFtiji(ftiji);
                        entity.setFxs1(fxs);
                        entity.setFxs(fjs);
                        Log.i("获得的entity",entity+"呵呵呵");
                        list.add(entity);
                    }
                    Log.i("最终的list",list+"呵呵呵");
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return 1;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        protected void onPostExecute(Integer integer) {
            setView();
            setListener();
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        protected void onProgressUpdate(Integer... values) {

        }
    }


    public class Tasks1 extends AsyncTask<Void, Void, String> {
        String fbillno;

        public  Tasks1(String fbillno){
            this.fbillno = fbillno;
        }

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
        protected String doInBackground(Void... params) {
            // 命名空间
            String nameSpace = "http://tempuri.org/";
            // 调用的方法名称
            String methodName = "F_ICStockBill_1";
            // EndPoint
            String endPoint = UTIL.ENDPOINT;
            // SOAP Action
            String soapAction = "http://tempuri.org/F_ICStockBill_1";
            // 指定WebService的命名空间和调用的方法名
            SoapObject rpc = new SoapObject(nameSpace, methodName);
            // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
            rpc.addProperty("FBillNO" , fbillno);

            //表体
            Document document = DocumentHelper.createDocument();
            Element rootElement = document.addElement("NewDataSet");
            for (RukuEntity e : list) {
                Log.i("qqq", e.getFnumber() + "sss");
                Element cust = rootElement.addElement("Cust");
                cust.addElement("FITEMID").setText(e.getFitemid() + "");
                cust.addElement("FQTY").setText(e.getFnumber());
                cust.addElement("fstockid").setText(stockid);
                cust.addElement("FEntrySelfA0132").setText(e.getFtiji());
                cust.addElement("FEntrySelfA0135").setText(e.getFxs());
                cust.addElement("FEntrySelfP0239").setText(e.getFxs1());
            }
            OutputFormat outputFormat = OutputFormat.createPrettyPrint();
            outputFormat.setSuppressDeclaration(false);
            outputFormat.setNewlines(false);
            StringWriter stringWriter = new StringWriter();
            // xmlWriter是用来把XML文档写入字符串的(工具)
            XMLWriter xmlWriter = new XMLWriter(stringWriter, outputFormat);
            // 把创建好的XML文档写入字符串
            try {
                xmlWriter.write(document);
                rpc.addProperty("FBtiXML", stringWriter.toString().substring(38));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("sss", stringWriter.toString().substring(38) + "***********");
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
            Log.i("返回结果:", object.toString());
            String s = object.toString();
            s = s.replace("F_ICStockBill_1Response{F_ICStockBill_1Result=","");
            s = s.replace("; }","");
            return s;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        protected void onPostExecute(String s) {
            if(s.equals("1")){
                Toast.makeText(RukuActivity.this,"提交成功！",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RukuActivity.this,BiaotouActivity.class));
                finish();
            }else{
                Toast.makeText(RukuActivity.this,"提交失败!",Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        protected void onProgressUpdate(Integer... values) {

        }
    }

    /*class Task1 extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            list1.clear();
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
            rpc.addProperty("FSql", "select FNAME ,FNUMBER,FItemID from t_stock where fname like '%" + stockname + "%'");
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

//
                Document doc = null;
                try {
                    doc = DocumentHelper.parseText(result); // 将字符串转为XML

                    Element rootElt = doc.getRootElement(); // 获取根节点

                    System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称


                    Iterator iter = rootElt.elementIterator("Cust"); // 获取根节点下的子节点head

                    // 遍历head节点

                    while (iter.hasNext()) {
                        WLEntity entity = new WLEntity();
                        Element recordEle = (Element) iter.next();
                        String fname = recordEle.elementTextTrim("FNAME"); // 拿到head节点下的子节点title值
                        String fstockid = recordEle.elementTextTrim("FItemID");
                        entity.setFstockid(fstockid);
                        entity.setFname(fname);
                        entity.setFnumber(recordEle.elementTextTrim("FNUMBER"));
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
        protected void onPostExecute(Integer integer) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.dialog, null);
            ListView lv = (ListView) layout.findViewById(R.id.lv);
            final WLApater3 adapter = new WLApater3(RukuActivity.this, list1);
            lv.setAdapter(adapter);
            final AlertDialog alertDialog3 = new AlertDialog.Builder(RukuActivity.this).setTitle("请选择仓库").setView(layout).create();
            alertDialog3.show();


            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    WLEntity e = list1.get(position);
                    stockname = e.getFname();
                    stockid = e.getFstockid();
                    alertDialog3.dismiss();
                    for(int i=0;i<list.size();i++){
                        View view1 = lv_ruku.getChildAt(i);
                        EditText ets = (EditText) view1.findViewById(R.id.et_num);
                        EditText etj = (EditText) view1.findViewById(R.id.et_tj);
                        Log.i("获取的数量",ets.getText().toString());
                        String oldnum = list.get(i).getFnumber();
                        if(Integer.parseInt(oldnum)<Integer.parseInt(ets.getText().toString())){
                            Toast.makeText(RukuActivity.this,"所输数量超出未入库数量",Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            list.get(i).setFnumber(ets.getText().toString());
                            list.get(i).setFtiji(etj.getText().toString());
                        }
                    }
                    Tasks1 t = new Tasks1(fbillno);
                    t.execute();
                }
            });

        }
    }*/
}
