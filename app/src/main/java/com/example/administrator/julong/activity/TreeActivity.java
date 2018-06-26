package com.example.administrator.julong.activity;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.example.administrator.julong.R;
import com.example.administrator.julong.adapter.SimpleTreeAdapter;
import com.example.administrator.julong.adapter.TreeListViewAdapter;
import com.example.administrator.julong.entity.FileBean;
import com.example.administrator.julong.util.UTIL;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TreeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private List<FileBean> list = new ArrayList<FileBean>();
    private ProgressDialog dialog;
    private Button btn_tree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree2);
        dialog = new ProgressDialog(this);
        setTools();
        setViews();
        setListeners();
    }

    protected void setTools(){
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("物料列表");

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
        btn_tree = (Button) findViewById(R.id.btn_tree);
    }

    protected void setListeners(){
        btn_tree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TreeTask task = new TreeTask();
                task.execute();
            }
        });
    }



    class TreeTask extends AsyncTask<Void, Integer, Integer> {
        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            dialog.show();
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
            rpc.addProperty("FSql", "select fitemid,fname,fparentid from t_item where fitemclassid=4");
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
                        String fname = recordEle.elementTextTrim("fname");
                        String fitemid = recordEle.elementTextTrim("fitemid");
                        String fparentid = recordEle.elementTextTrim("fparentid");
                        Log.i("qqq", fname);
                        System.out.println("title:" + fname);
                        FileBean filebean = new FileBean(Integer.parseInt(fitemid),Integer.parseInt(fparentid),fname,fname,fname,fname);
                        list.add(filebean);
                        Log.i("NEW LIST:",list.toString());
                    }
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
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
                Toast.makeText(TreeActivity.this, "找不到物料", Toast.LENGTH_LONG).show();
                return;
            }
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.activity_tree, null);
            ListView lv1 = (ListView) layout.findViewById(R.id.lv_tree);
            try{
            final TreeListViewAdapter adapter1 = new SimpleTreeAdapter<FileBean>(lv1, TreeActivity.this, list, 10);
            lv1.setAdapter(adapter1);
            } catch (IllegalAccessException e) {
            e.printStackTrace();
            }
            final AlertDialog alertDialog2 = new AlertDialog.Builder(TreeActivity.this).setTitle("请选择物料").setView(layout).create();
            alertDialog2.show();
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }
}
