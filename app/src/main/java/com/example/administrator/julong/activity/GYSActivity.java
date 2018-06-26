package com.example.administrator.julong.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.julong.R;
import com.example.administrator.julong.adapter.SimpleTreeAdapter;
import com.example.administrator.julong.adapter.TreeListViewAdapter;
import com.example.administrator.julong.entity.FileBean;
import com.example.administrator.julong.entity.Node;
import com.example.administrator.julong.util.UTIL;
import com.example.administrator.julong.adapter.GYSApater;
import com.example.administrator.julong.entity.WLEntity;
import com.example.administrator.julong.view.CustomProgress;

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

public class GYSActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CustomProgress dialog;
    private EditText et1,et2,et3,et4,et5,et6;
    private String s1="",s2="",s3="",s4="",s5="",s6="";
    private Button bt,bt2;
    private String id = "0";
    private TextView tv,tv1,tv2,tv3;
    int x=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gys);
        setTool();
        setView();
        LinearLayout.LayoutParams ll0=(LinearLayout.LayoutParams)tv.getLayoutParams();
        LinearLayout.LayoutParams ll1=(LinearLayout.LayoutParams)tv1.getLayoutParams();
        LinearLayout.LayoutParams ll2=(LinearLayout.LayoutParams)tv2.getLayoutParams();
        LinearLayout.LayoutParams ll3=(LinearLayout.LayoutParams)tv3.getLayoutParams();
        ll2.width=ll0.width;
        ll1.width=ll0.width;
        ll3.width=ll0.width;
        Log.i("ss", ll2.width + "+++");
        Log.i("ss", tv.getWidth() + "+++");
        tv1.setLayoutParams(ll1);
        tv2.setLayoutParams(ll2);
        tv3.setLayoutParams(ll3);

        //
        tv1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
//                tv1.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        tv1.setWidth(tv.getWidth());
//                    }
//                }, 300);
                tv1.setWidth(tv.getWidth());
            }
        });
        tv2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                tv2.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        tv2.setWidth(tv.getWidth());
                    }
                }, 300);
            }
        });
        tv3.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                tv3.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        tv3.setWidth(tv.getWidth());
                    }
                }, 300);
            }
        });
    }
    private void setView(){
        tv = (TextView) findViewById(R.id.tv);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv3 = (TextView) findViewById(R.id.tv3);

        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);
        et4 = (EditText) findViewById(R.id.et4);
        et5 = (EditText) findViewById(R.id.et5);
        et6 = (EditText) findViewById(R.id.et6);
        bt = (Button) findViewById(R.id.bt);
        bt2 = (Button) findViewById(R.id.bt2);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                s1=et1.getText().toString();
//                s2=et2.getText().toString();
//                x=0;
//                if(s1.equals("")){
//                    Toast.makeText(GYSActivity.this, "请输入编号", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                if(s2.equals("")){
//                    Toast.makeText(GYSActivity.this, "请输入名称", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                Task2 task2 = new Task2();
//                task2.execute();
                LinearLayout.LayoutParams ll0=(LinearLayout.LayoutParams)tv.getLayoutParams();
                ll0.width=ll0.width;
                Log.i("ss", ll0.width + "+++");
                Log.i("ss", tv.getWidth() + "+++");
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1=et1.getText().toString();
                s2=et2.getText().toString();
                s3=et3.getText().toString();
                s4=et4.getText().toString();
                s5=et5.getText().toString();
                s6=et6.getText().toString();
                x=1;
                if(s1.equals("")){
                    Toast.makeText(GYSActivity.this, "请输入编号", Toast.LENGTH_LONG).show();
                    return;
                }
                if(s2.equals("")){
                    Toast.makeText(GYSActivity.this, "请输入名称", Toast.LENGTH_LONG).show();
                    return;
                }
                if(s3.equals("")){
                    Toast.makeText(GYSActivity.this, "请输入地址", Toast.LENGTH_LONG).show();
                    return;
                }
                if(s4.equals("")){
                    Toast.makeText(GYSActivity.this, "请输入联系人", Toast.LENGTH_LONG).show();
                    return;
                }
                if(s5.equals("")){
                    Toast.makeText(GYSActivity.this, "请输入电话", Toast.LENGTH_LONG).show();
                    return;
                }
                if(s6.equals("")){
                    Toast.makeText(GYSActivity.this, "请输入手机", Toast.LENGTH_LONG).show();
                    return;
                }

                Task2 task2 = new Task2();
                task2.execute();

            }
        });

    }
    private void setTool() {
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("供应商维护");

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
                    case R.id.action_2:
                        name = "";
                        Task3 task3=new Task3();
                        task3.execute();
                        break;
                    case R.id.action_xz:
                        s1=et1.getText().toString();
                        s2=et2.getText().toString();
                        s3=et3.getText().toString();
                        s4=et4.getText().toString();
                        s5=et5.getText().toString();
                        s6=et6.getText().toString();
                        x=0;
                        if(s1.equals("")){
                            Toast.makeText(GYSActivity.this, "请输入编号", Toast.LENGTH_LONG).show();
                            break;
                        }
                        if(s2.equals("")){
                            Toast.makeText(GYSActivity.this, "请输入名称", Toast.LENGTH_LONG).show();
                            break;
                        }

                        Task2 task2 = new Task2();
                        task2.execute();
                        break;
                    case R.id.action_xg:
                        s1=et1.getText().toString();
                        s2=et2.getText().toString();
                        s3=et3.getText().toString();
                        s4=et4.getText().toString();
                        s5=et5.getText().toString();
                        s6=et6.getText().toString();
                        x=1;
                        if(s1.equals("")){
                            Toast.makeText(GYSActivity.this, "请输入编号", Toast.LENGTH_LONG).show();
                            break;
                        }
                        if(s2.equals("")){
                            Toast.makeText(GYSActivity.this, "请输入名称", Toast.LENGTH_LONG).show();
                            break;
                        }

                        Task2 task4 = new Task2();
                        task4.execute();
                        break;

                }


                return true;

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu3, menu);//加载menu文件到布局

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu4, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                name=query;
                Task3 task3=new Task3();
                task3.execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
        return super.onCreateOptionsMenu(menu);
    }
    private SearchView searchView;
    private String name = "";
    class Task2 extends AsyncTask<Void, Integer, Integer> {


        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {

            dialog=CustomProgress.show(GYSActivity.this,"提交中...", true, null);
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {


            // 命名空间
            String nameSpace = "http://tempuri.org/";
            // 调用的方法名称
            String methodName = "F_supplier_in";
            // EndPoint
            String endPoint = UTIL.ENDPOINT;
            // SOAP Action
            String soapAction = "http://tempuri.org/F_supplier_in";

            // 指定WebService的命名空间和调用的方法名
            SoapObject rpc = new SoapObject(nameSpace, methodName);

            // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
            rpc.addProperty("FNumber", s1);
            rpc.addProperty("FName", s2);
            Log.i("拼接的数据",s3+s4+s5+s6+"++++++++++++++++++++++++++++++++++++++++");
            rpc.addProperty("FAddress", s3);
            rpc.addProperty("FContact", s4);
            rpc.addProperty("FPhone", s5);
            rpc.addProperty("FFax", s6);

            if(x==0){
                rpc.addProperty("ItemID", "-1");
            }else {
                rpc.addProperty("ItemID", id);
            }

//

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
                Log.i("提交供应商的结果:", result + "sssssssssssssssssssssssssssssssssssssssssss");
                if(result.equals("0")){
                    return 1;
                }else {
                    return 2;
                }
            }else{
                return 3;
            }
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer integer) {
            dialog.dismiss();
            if(integer==1){
                Toast.makeText(GYSActivity.this, "修改成功", Toast.LENGTH_LONG).show();
            }else if(integer==2) {
                Toast.makeText(GYSActivity.this, "新增成功", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(GYSActivity.this, "提交失败", Toast.LENGTH_LONG).show();
            }


        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

    private List<FileBean> listwl = new ArrayList<>();

    class Task3 extends AsyncTask<Void, Integer, Integer> {


        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            listwl.clear();
            dialog=CustomProgress.show(GYSActivity.this,"搜寻中...", true, null);
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
            rpc.addProperty("FSql", "select a.fname ,a.fnumber,a.fitemid,a.fparentid,b.faddress,b.fcontact,b.fphone,b.ffax from t_item a left join t_supplier b on a.fitemid=b.fitemid where a.fitemclassid=8 and  (left(left(a.ffullname,CHARINDEX('_', a.ffullname)),2)=(select fdescription from t_user where fname='"+UTIL.FNAME+"') or\n" +
                                        "left(a.ffullname,2)=(select fdescription from t_user where fname='"+UTIL.FNAME+"')) and a.fname like '%" + name + "%'");
//            rpc.addProperty("FSql", "select fname,fnumber ,fitemid,FAddress,FContact,FPhone from  t_supplier where fname like '%" + name + "%'");
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
                        String fname = recordEle.elementTextTrim("fname"); // 拿到head节点下的子节点title值
                        String fnumber = recordEle.elementTextTrim("fnumber");
                        String faddress = recordEle.elementTextTrim("faddress");
                        String fphone = recordEle.elementTextTrim("fphone");
                        String fcontact = recordEle.elementTextTrim("fcontact");
                        String fitemid = recordEle.elementTextTrim("fitemid");
                        String fparentid = recordEle.elementTextTrim("fparentid");
                        String ffax = recordEle.elementTextTrim("ffax");
                        FileBean entity = new FileBean(Integer.parseInt(fitemid),Integer.parseInt(fparentid),fname,faddress,fnumber,fcontact+","+fphone+","+ffax);
                        listwl.add(entity);
                        // 遍历Header节点下的Response节点
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
                rpc1.addProperty("FSql", "select a.FNAME ,a.FNUMBER,a.fitemid,a.fparentid,b.faddress,b.fcontact,b.fphone,b.ffax from t_item a left join t_supplier b on a.fitemid=b.fitemid where a.fitemclassid=8 and a.fname like '%" + name + "%'");
//                rpc1.addProperty("FSql", "select FNAME ,FNUMBER from t_Supplier where fname like '%" + cmp + "%'");
                //  rpc.addProperty("FSql", "select b.fname,b.fnumber ,b.fmodel ,a.fimage  from z_image a inner join t_icitem b on a.fnumber=b.fnumber where b.fname like '%"+name+"%'  ");
                rpc1.addProperty("FTable", "t_item");

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
                            String fname = recordEle1.elementTextTrim("FNAME"); // 拿到head节点下的子节点title值
                            String fnumber = recordEle1.elementTextTrim("FNUMBER");
                            String fitemid = recordEle1.elementTextTrim("fitemid");
                            String fparentid = recordEle1.elementTextTrim("fparentid");
                            String faddress = recordEle1.elementTextTrim("faddress");
                            String fphone = recordEle1.elementTextTrim("fphone");
                            String fcontact = recordEle1.elementTextTrim("fcontact");
                            String ffax = recordEle1.elementTextTrim("ffax");
                            FileBean entity1 = new FileBean(Integer.parseInt(fitemid),Integer.parseInt(fparentid),fname,faddress,fnumber,fcontact+","+fphone+","+ffax);
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

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer integer) {
            dialog.dismiss();
            if (listwl.size() == 0) {
                Toast.makeText(GYSActivity.this, "找不到物料", Toast.LENGTH_LONG).show();
                return;
            }
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.activity_tree, null);
            ListView lv = (ListView) layout.findViewById(R.id.lv_tree);

           final TreeListViewAdapter adapter;

            try {
                adapter = new SimpleTreeAdapter<FileBean>(lv, GYSActivity.this, listwl, 10);
                lv.setAdapter(adapter);

                final AlertDialog alertDialog2 = new AlertDialog.Builder(GYSActivity.this).setTitle("请选择供应商").setView(layout).create();
                alertDialog2.show();

                adapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                    @Override
                    public void onClick(Node node, int position) {
                        if(node.isLeaf()){
                            alertDialog2.dismiss();
                            et1.setText(node.getFnumber());
                            et2.setText(node.getName());
                            et3.setText(node.getFimage());
                            String spare[] = node.getFmodel().split(",");
                            int i = spare.length;
                            System.out.println("数组的长度:"+i+"---------------------------------------");
                            System.out.println("拼接的字符串:"+node.getFmodel()+"--------------------------------");
                            switch(i){
                                case 0:
                                    break;
                                case 1:
                                    et4.setText(spare[0]);
                                    break;
                                case 2:
                                    et4.setText(spare[0]);
                                    et5.setText(spare[1]);
                                    break;
                                case 3:
                                    et4.setText(spare[0]);
                                    et5.setText(spare[1]);
                                    et6.setText(spare[2]);
                                    break;
                            }

                            GYSActivity.this.id=String.valueOf(node.getId());
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


}
