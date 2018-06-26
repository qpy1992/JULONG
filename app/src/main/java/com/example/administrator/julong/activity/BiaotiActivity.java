package com.example.administrator.julong.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.administrator.julong.R;
import com.example.administrator.julong.util.UTIL;
import com.example.administrator.julong.adapter.EntryAdapter;
import com.example.administrator.julong.entity.BiaotiEntity;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BiaotiActivity extends AppCompatActivity {
    private String interid = "";
    private Toolbar toolbar;
    private ProgressDialog dialog;
    private ListView listView;
    private List<BiaotiEntity> list = new ArrayList<>();
    private EntryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biaoti);
        Intent intent=getIntent();
        interid = intent.getStringExtra("id");
        dialog = new ProgressDialog(this);
        setTool();
        listView = (ListView) findViewById(R.id.lv);
        adapter = new EntryAdapter(BiaotiActivity.this, list);
        listView.setAdapter(adapter);
        UpdateTextTask updateTextTask=new UpdateTextTask();
        updateTextTask.execute();


    }
    private void setTool() {
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("订单详情");

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
    class UpdateTextTask extends AsyncTask<Void, Integer, Integer> {


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
            rpc.addProperty("FSql", "select a.FAuxPrice,a.FAuxQty,a.FAmount,b.FName,b.fmodel,b.fimage1,a.FEntrySelfP0227,a.FEntrySelfP0229,a.FEntrySelfP0236,a.FEntrySelfP0237 from POOrderEntry a INNER JOIN dbo.t_ICItem b ON a.FItemID=b.FItemID WHERE FInterID='"+interid+"'");

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
                Log.i("sss", object.getProperty(0).toString()+"");
                String result = object.getProperty(0).toString();
                Document doc = null;
                try {
                    doc = DocumentHelper.parseText(result); // 将字符串转为XML

                    Element rootElt = doc.getRootElement(); // 获取根节点

                    System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称


                    Iterator iter = rootElt.elementIterator("Cust"); // 获取根节点下的子节点head

                    // 遍历head节点

                    while (iter.hasNext()) {
                        BiaotiEntity entity = new BiaotiEntity();
                        Element recordEle = (Element) iter.next();
                        String FAuxQty = recordEle.elementTextTrim("FAuxQty"); // 拿到head节点下的子节点title值
                        String FAmount = recordEle.elementTextTrim("FAmount");
                        String FName = recordEle.elementTextTrim("FName");
                        String Fmodel = recordEle.elementTextTrim("fmodel");
                        String Fxiang = recordEle.elementTextTrim("FEntrySelfP0227");
                        String Ftiji = recordEle.elementTextTrim("FEntrySelfP0229");
                        String fimage1 = recordEle.elementTextTrim("fimage1");
                        String fmaterial = recordEle.elementTextTrim("FEntrySelfP0236");
                        String fsingleweight = recordEle.elementTextTrim("FEntrySelfP0237");
                        entity.setFAmount(FAmount);
                        entity.setFName(FName);
                        entity.setFAuxQty(FAuxQty);
                        entity.setXs(Integer.parseInt(Fxiang));
                        entity.setTj(Double.parseDouble(Ftiji));
                        entity.setFmodel(Fmodel);
                        entity.setFimage1(fimage1);
                        entity.setFAuxPrice(recordEle.elementTextTrim("FAuxPrice"));
                        Log.i("材质：", fmaterial+"sssssssssssssssssssssssssssssssssssssss");
                        entity.setFmaterial(fmaterial);
                        Log.i("单重", fsingleweight+"ssssssssssssssssssssssssssssssssssssss");
                        entity.setFsingleweight(Double.parseDouble(fsingleweight));
                        list.add(entity);
                        // 遍历Header节点下的Response节点
                    }
                    Log.i("ggg", list.get(0).getFName()+"___");
                }catch (Exception e){
                    Log.i("ggg", e.toString());
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
}
