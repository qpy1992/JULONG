package com.example.administrator.julong.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.example.administrator.julong.R;
import com.example.administrator.julong.util.UTIL;
import com.example.administrator.julong.adapter.GridviewApater3;
import com.example.administrator.julong.entity.WLEntity;

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

public class PhotoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private GridView gv;
    private GridviewApater3 gridviewApater2;
    private List<WLEntity> listwl = new ArrayList<>();
    private String name = "";
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        setTool();
        gv = (GridView) findViewById(R.id.gv);
        gridviewApater2 = new GridviewApater3(this, listwl);
        gv.setAdapter(gridviewApater2);
        Intent i=getIntent();
        name=i.getStringExtra("p");
        dialog = new ProgressDialog(this);
        getData();
    }
    private void getData(){
        Task1 task1 = new Task1();
        task1.execute();

    }
    private void setTool() {
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("物料信息");

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


                        break;

                }


                return true;

            }
        });

    }
    class Task1 extends AsyncTask<Void, Integer, Integer> {


        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {

            dialog.show();
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
            //rpc.addProperty("FSql", "select * from z_image a inner join t_icitem b on a.fnumber=b.fnumber where b.fname like '%"+name+"%'");
            rpc.addProperty("FSql", "select b.fname,b.fnumber ,b.fmodel ,a.fimage  from z_image a inner join t_icitem b on a.fnumber=b.fnumber where b.fname = '"+name+"'  ");
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
                        WLEntity entity = new WLEntity();
                        Element recordEle = (Element) iter.next();
                        // 拿到head节点下的子节点title值
                        String fnumber = recordEle.elementTextTrim("fnumber");

                        String fimage = recordEle.elementTextTrim("fimage");
                        Log.i("qqq", fimage);

                        System.out.println("title:" + fimage);

                        entity.setFnumber(fnumber);

                        entity.setImg(fimage);
                        listwl.add(entity);


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

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer integer) {
            dialog.dismiss();
            gridviewApater2.notifyDataSetChanged();




        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

}
