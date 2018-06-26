package com.example.administrator.julong.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.julong.R;
import com.example.administrator.julong.adapter.SimpleTreeAdapter;
import com.example.administrator.julong.adapter.TreeListViewAdapter;
import com.example.administrator.julong.entity.FileBean;
import com.example.administrator.julong.entity.Node;
import com.example.administrator.julong.util.UTIL;
import com.example.administrator.julong.adapter.WLApater;
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XinzenwuliaoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CustomProgress dialog;
    private Spinner sp;
    private EditText et1,et2,et3;
    private Button bt;
    List<String> list = new ArrayList<>();
    private ArrayAdapter<String> arr_adapter;
    private String danwei="",s1="",s2="",s3="",name="";
    private int x=0;
    private String id="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xinzenwuliao);
        setTool();
        setView();
        Task1 task1=new Task1();
        task1.execute();


    }
    private void setView(){
        sp = (Spinner) findViewById(R.id.sp);
        arr_adapter= new ArrayAdapter<String>(XinzenwuliaoActivity.this, android.R.layout.simple_spinner_item, list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        sp.setAdapter(arr_adapter);
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);
        bt = (Button) findViewById(R.id.bt);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                danwei = list.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 s1=et1.getText().toString();
                 s2=et2.getText().toString();
                 s3=et3.getText().toString();
                if(s1.equals("")){
                    Toast.makeText(XinzenwuliaoActivity.this, "请输入编号", Toast.LENGTH_LONG).show();
                    return;
                }
                if(s2.equals("")){
                    Toast.makeText(XinzenwuliaoActivity.this, "请输入名称", Toast.LENGTH_LONG).show();
                    return;
                }
                if(s3.equals("")){
                    Toast.makeText(XinzenwuliaoActivity.this, "请输入规格", Toast.LENGTH_LONG).show();
                    return;
                }
                Task2 task2 = new Task2();
                task2.execute();

            }
        });

    }
    class Task1 extends AsyncTask<Void, Integer, Integer> {


        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {

//            dialog=CustomProgress.show(XinzenwuliaoActivity.this,"搜寻中...", true, null);
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
            rpc.addProperty("FSql", "select FName from t_MeasureUnit where fitemid>0");
            //  rpc.addProperty("FSql", "select b.fname,b.fnumber ,b.fmodel ,a.fimage  from z_image a inner join t_icitem b on a.fnumber=b.fnumber where b.fname like '%"+name+"%'  ");
            rpc.addProperty("FTable", "t_MeasureUnit");

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
                       list.add(recordEle.elementTextTrim("FName")); // 拿到head节点下的子节点title值



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
//            dialog.dismiss();
            arr_adapter= new ArrayAdapter<String>(XinzenwuliaoActivity.this, android.R.layout.simple_spinner_item, list);
            //设置样式
            arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //加载适配器
            sp.setAdapter(arr_adapter);
//            if(list.size()>0){
//                danwei = list.get(0);
//            }
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    danwei = list.get(i);
                    Log.i("实时的单位",danwei+"=====================================");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    danwei = list.get(0);
                    Log.i("刚进去时的单位",danwei+"=====================================");
                }
            });
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }
    class Task2 extends AsyncTask<Void, Integer, Integer> {


        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {

            dialog=CustomProgress.show(XinzenwuliaoActivity.this,"搜寻中...", true, null);
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {


            // 命名空间
            String nameSpace = "http://tempuri.org/";
            // 调用的方法名称
            String methodName = "F_ICItem_in";
            // EndPoint
            String endPoint = UTIL.ENDPOINT;
            // SOAP Action
            String soapAction = "http://tempuri.org/F_ICItem_in";

            // 指定WebService的命名空间和调用的方法名
            SoapObject rpc = new SoapObject(nameSpace, methodName);

            // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
            rpc.addProperty("FNumber", s1);
            rpc.addProperty("FName", s2);
            rpc.addProperty("FModel", s3);
            rpc.addProperty("FUnit", danwei);
            if(x==0){
                rpc.addProperty("ItemID", "-1");
            }else {
                rpc.addProperty("ItemID", id);
            }


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
                Log.i("新增物料返回的结果", result + "ssssssssssssssssssssssssssssssssssssssssss");
                if(result.equals("1")){
                    return 1;
                }else if(result.equals("2")){
                    return 2;
                }else if(result.equals("3")){
                    return 3;
                }else{
                    return 0;
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
            if(integer==1){
                Toast.makeText(XinzenwuliaoActivity.this, "新增成功", Toast.LENGTH_LONG).show();
            }else if(integer==2){
                Toast.makeText(XinzenwuliaoActivity.this, "已存在相同的物料", Toast.LENGTH_LONG).show();
            }else if(integer==3) {
                Toast.makeText(XinzenwuliaoActivity.this, "物料类别不规范", Toast.LENGTH_LONG).show();
            }else if(integer==0){
                Toast.makeText(XinzenwuliaoActivity.this, "修改成功", Toast.LENGTH_LONG).show();
            }
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

    private void setTool() {
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("物料维护");

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
                        ;break;
                    case R.id.action_xz:
                        x=0;
                        s1=et1.getText().toString();
                        s2=et2.getText().toString();
                        s3=et3.getText().toString();
                        if(s1.equals("")){
                            Toast.makeText(XinzenwuliaoActivity.this, "请输入编号", Toast.LENGTH_LONG).show();
                           break;
                        }
                        if(s2.equals("")){
                            Toast.makeText(XinzenwuliaoActivity.this, "请输入名称", Toast.LENGTH_LONG).show();
                            break;
                        }
                        if(s3.equals("")){
                            Toast.makeText(XinzenwuliaoActivity.this, "请输入规格", Toast.LENGTH_LONG).show();
                            break;
                        }
                        Task2 task2 = new Task2();
                        task2.execute();
                        break;
                    case R.id.action_xg:
                        x=1;
                        s1=et1.getText().toString();
                        s2=et2.getText().toString();
                        s3=et3.getText().toString();
                        if(s1.equals("")){
                            Toast.makeText(XinzenwuliaoActivity.this, "请输入编号", Toast.LENGTH_LONG).show();
                            return true;
                        }
                        if(s2.equals("")){
                            Toast.makeText(XinzenwuliaoActivity.this, "请输入名称", Toast.LENGTH_LONG).show();
                            return true;
                        }
                        if(s3.equals("")){
                            Toast.makeText(XinzenwuliaoActivity.this, "请输入规格", Toast.LENGTH_LONG).show();
                            return true;
                        }
                        Task2 task4 = new Task2();
                        task4.execute();
                        ;break;

                }
                return true;
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu3, menu);//加载menu文件到布局

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu3, menu);
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

    //
    private List<FileBean> listwl = new ArrayList<>();
    class Task3 extends AsyncTask<Void, Integer, Integer> {


        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            listwl.clear();
            dialog=CustomProgress.show(XinzenwuliaoActivity.this,"搜寻中...", true, null);
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
            rpc.addProperty("FSql", "select a.fname,a.fnumber,a.fitemid,a.fparentid,b.fmodel,b.fimage1 from t_item a left join t_icitem b on a.fitemid=b.fitemid  where a.fitemclassid=4 and  left(left(a.ffullname,CHARINDEX('_', a.ffullname)),2)=(select fdescription from t_user where fname='"+UTIL.FNAME+"') and a.fname like '%" + name + "%'");
//            rpc.addProperty("FSql", "select a.fname,a.fnumber ,a.fmodel ,a.fimage1 from  t_icitem a inner join t_user b on a.fnote=b.fdescription where a.fname like '%" + name + "%' and b.fname='"+UTIL.FNAME+"'order by a.fnumber");
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
                rpc1.addProperty("FSql", "select a.fname,a.fnumber,a.fitemid,a.fparentid,b.fmodel,b.fimage1 from t_item a left join t_icitem b on a.fitemid=b.fitemid  where a.fitemclassid=4 and a.fname like '%" + name + "%'");
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
                            String fmodel = recordEle1.elementTextTrim("fmodel");
                            String fimage = recordEle1.elementTextTrim("fimage1");
                            String fitemid = recordEle1.elementTextTrim("fitemid");
                            String fparentid = recordEle1.elementTextTrim("fparentid");
                            FileBean entity1 = new FileBean(Integer.parseInt(fitemid),Integer.parseInt(fparentid),fname,fimage,fnumber,fmodel);
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
                Toast.makeText(XinzenwuliaoActivity.this, "找不到物料", Toast.LENGTH_LONG).show();
                return;
            }

            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.activity_tree, null);
            ListView lv = (ListView) layout.findViewById(R.id.lv_tree);
            final TreeListViewAdapter adapter;
            try {
                adapter = new SimpleTreeAdapter<FileBean>(lv, XinzenwuliaoActivity.this, listwl, 10);
                lv.setAdapter(adapter);


                final AlertDialog alertDialog2 = new AlertDialog.Builder(XinzenwuliaoActivity.this).setTitle("请选择物料").setView(layout).create();
                alertDialog2.show();


                adapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener()
                {
                    @Override
                    public void onClick(Node node, int position) {
                        if (node.isLeaf()) {
                            alertDialog2.dismiss();
                            et1.setText(node.getFnumber());
                            et2.setText(node.getName());
                            et3.setText(node.getFmodel());
                            XinzenwuliaoActivity.this.id=String.valueOf(node.getId());

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
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {

                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

}
