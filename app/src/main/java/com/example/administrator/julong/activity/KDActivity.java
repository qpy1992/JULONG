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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.julong.R;
import com.example.administrator.julong.adapter.SimpleTreeAdapter;
import com.example.administrator.julong.adapter.TreeListViewAdapter;
import com.example.administrator.julong.entity.FileBean;
import com.example.administrator.julong.entity.Node;
import com.example.administrator.julong.view.CustomProgress;
import com.example.administrator.julong.view.SideslipListView;
import com.example.administrator.julong.util.UTIL;
import com.example.administrator.julong.adapter.WLApater2;
import com.example.administrator.julong.entity.BiaotiEntity;
import com.example.administrator.julong.entity.BiaotouEntity;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class KDActivity extends AppCompatActivity {
    private SideslipListView lv;
    private List<FileBean> list = new ArrayList<>();
    private List<WLEntity> listentry = new ArrayList<>();
    private WLApater2 adapter;
    private Toolbar toolbar;
    private Button btczwl, btrq, bttj, btsh;
    private EditText et;
    private String name;
    private String cmpname, date, date1;
    private CustomProgress dialog;
    private String cmp, FSUPPLYNUMBER;
    private TextView tvkh, tvdate, tvsh;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private int year;
    private int month;
    private int day;
    private EditText etdz, etgd, etsj, etdh;
    private Spinner spmt;
    private String mt = "", dz = "", gd = "", sj = "", dh = "";
    private String inter = "-1", bill = "-1";
    private  boolean flag=false;
    private ArrayAdapter<String> arr_adapter;
    String[] maitou = new String[]{"C","M","I","Q","G","MX"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kd);
        setTool();
        setView();
        setListen();
        Intent intent = getIntent();

        List<BiaotiEntity> list3 = (List<BiaotiEntity>) intent.getSerializableExtra("list");
        if (null != list3) {
            flag=true;
            BiaotouEntity entity = (BiaotouEntity) intent.getSerializableExtra("bt");
            for(int i=0;i<maitou.length;i++){
                if(maitou[i].equals(entity.getMt())) {
                    spmt.setSelection(i);
                }
            }

            etdz.setText(entity.getDz());

            etgd.setText(entity.getGd());
            etsj.setText(entity.getSj());
            etdh.setText(entity.getDh());
            cmpname=entity.getGys();
            FSUPPLYNUMBER=entity.getGysno();
            tvkh.setText(cmpname);
            ;
            if (intent.getStringExtra("zt").equals("xz")) {

            } else {
                inter = intent.getStringExtra("inter");
                bill = intent.getStringExtra("bill");
            }

            Log.i("qqq", inter + "sss"+bill);
            for (BiaotiEntity bt : list3) {
                WLEntity wlEntity = new WLEntity();
                wlEntity.setFname(bt.getFName());
                wlEntity.setJs(bt.getJs());
                wlEntity.setDj(Double.parseDouble(bt.getFAuxPrice()));
                wlEntity.setBeizhu(bt.getFnote());
                wlEntity.setXs(bt.getXs());
                wlEntity.setTiji(bt.getTj());
                wlEntity.setBianhao(bt.getBianhao());
                wlEntity.setFnumber(bt.getFnumber());
                wlEntity.setBeizhu(bt.getFnote());
                wlEntity.setImg(bt.getFimage1());
                wlEntity.setFmaterial(bt.getFmaterial());
                wlEntity.setFsingleweight(bt.getFsingleweight());
                Log.i("sss", wlEntity.getFname() + "++++");
                listentry.add(wlEntity);

            }
        }
        adapter.notifyDataSetChanged();


    }

    private void setListen() {
        spmt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mt = maitou[i];
                Log.i("当前选中的唛头",mt+"====================================");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btczwl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et.getText().toString().trim();
                Task1 task1 = new Task1();
                task1.execute();
            }
        });
        btrq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(KDActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String s = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        try {
                            java.util.Date dates = format.parse(s);
                            String s1 = format.format(dates);
                            tvdate.setText(s1);
                            date = s1;
                        } catch (Exception e) {
                        }
                    }
                }, year, month, day);
                dpd.show();//显示DatePickerDialog组件
            }
        });
        btsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(KDActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String s2 = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        try {
                            java.util.Date datep = format.parse(s2);
                            String s3 = format.format(datep);
                            tvsh.setText(s3);
                            date1 = s3;
                        } catch (Exception e) {
                        }
                    }
                }, year, month, day);
                dpd.show();//显示DatePickerDialog组件
            }
        });
        bttj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvkh.getText().toString().equals("")) {
                    Toast.makeText(KDActivity.this, "供应商不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (listentry.size() == 0) {
                    Toast.makeText(KDActivity.this, "物料条数不能为0", Toast.LENGTH_LONG).show();
                    return;
                }
                Task3 task3 = new Task3();
                task3.execute();

            }
        });

    }

    private void setView() {
        etsj = (EditText) findViewById(R.id.etsj);
        etdh = (EditText) findViewById(R.id.etdh);
        etdz = (EditText) findViewById(R.id.etdz);
        etgd = (EditText) findViewById(R.id.etgd);
        spmt = (Spinner) findViewById(R.id.spmt);
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, maitou);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spmt.setAdapter(arr_adapter);
        et = (EditText) findViewById(R.id.et);
        lv = (SideslipListView) findViewById(R.id.lv1);
        adapter = new WLApater2(this, listentry);
        lv.setAdapter(adapter);
        //设置item点击事件
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View view, int position, long id) {
//                if (lv.isAllowItemClick()) {
//                    Log.i("TAG", list.get(position) + "被点击了");
//                }
//            }
//        });
//        //设置item长按事件
//        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
//                if (lv.isAllowItemClick()) {
//                    Log.i("TAG", list.get(position) + "被长按了");
//                    return true;//返回true表示本次事件被消耗了，若返回
//                }
//                return false;
//            }
//        });
        btczwl = (Button) findViewById(R.id.btczwl);
        btrq = (Button) findViewById(R.id.btrq);
        btsh = (Button) findViewById(R.id.btsh);
        bttj = (Button) findViewById(R.id.bttj);
        tvkh = (TextView) findViewById(R.id.tvkh);
        tvdate = (TextView) findViewById(R.id.tvdate);
        tvsh = (TextView) findViewById(R.id.tvsh);
        Date datey = new Date();
        tvdate.setText(format.format(datey));
        tvsh.setText(format.format(datey));
        date = format.format(datey);
        date1 = format.format(datey);
        Calendar mycalendar = Calendar.getInstance();
        year = mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        month = mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        day = mycalendar.get(Calendar.DAY_OF_MONTH);

    }

    private void setTool() {
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("开单");

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
                        final EditText et = new EditText(KDActivity.this);
                        et.setBackgroundResource(R.drawable.et_shape);
                        new AlertDialog.Builder(KDActivity.this).setTitle("请输入供应商").setIcon(
                                R.drawable.suppliers).setView(
                                et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cmp = et.getText().toString().trim();
                                Task2 task2 = new Task2();
                                task2.execute();
                            }
                        })
                                .setNegativeButton("取消", null).show();
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);//加载menu文件到布局
        return true;
    }

    class Task1 extends AsyncTask<Void, Integer, Integer> {
        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            list.clear();
            dialog=CustomProgress.show(KDActivity.this,"搜寻中...", true, null);
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
            //rpc.addProperty("FSql", "select b.fname,b.fnumber ,b.fmodel ,a.fimage  from z_image a inner join t_icitem b on a.fnumber=b.fnumber where b.fname like '%"+name+"%'  ");
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
//                        WLEntity entity = new WLEntity();
                        Element recordEle = (Element) iter.next();
                        String fname = recordEle.elementTextTrim("fname"); // 拿到head节点下的子节点title值
                        String fnumber = recordEle.elementTextTrim("fnumber");
                        String fmodel = recordEle.elementTextTrim("fmodel");
                        String fimage = recordEle.elementTextTrim("fimage1");
                        String fitemid = recordEle.elementTextTrim("fitemid");
                        String fparentid = recordEle.elementTextTrim("fparentid");
//                        entity.setFname(fname);
//                        entity.setFnumber(fnumber);
//                        entity.setGg(fmodel);
//                        entity.setImg(fimage);
//                        entity.setFitemid(fitemid);
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
                Toast.makeText(KDActivity.this, "找不到物料", Toast.LENGTH_LONG).show();
                return;
            }
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.activity_tree, null);
            ListView lv = (ListView) layout.findViewById(R.id.lv_tree);
//            final WLApater adapter = new WLApater(KDActivity.this, list);
            final TreeListViewAdapter adapter;
            try {
                adapter = new SimpleTreeAdapter<FileBean>(lv, KDActivity.this, list, 10);
                lv.setAdapter(adapter);

            final AlertDialog alertDialog2 = new AlertDialog.Builder(KDActivity.this).setTitle("请选择物料").setView(layout).create();
            alertDialog2.show();

            adapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener()
            {
                @Override
                public void onClick(Node node, int position)
                {
                    if (node.isLeaf())
                    {
                        final WLEntity entity = new WLEntity();
                        entity.setFname(node.getName());
                        entity.setFnumber(node.getFnumber());
                        entity.setFitemid(String.valueOf(node.getId()));
                        entity.setGg(node.getFmodel());
                    alertDialog2.dismiss();
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.dialog2, null);
                    final EditText et1 = (EditText) layout.findViewById(R.id.et1);
                    final EditText et2 = (EditText) layout.findViewById(R.id.et2);
                    final EditText et3 = (EditText) layout.findViewById(R.id.et3);
                    final EditText et4 = (EditText) layout.findViewById(R.id.et4);
                    final EditText et5s = (EditText) layout.findViewById(R.id.et5s);
                    final EditText et6s = (EditText) layout.findViewById(R.id.et6s);
                    final EditText et5 = (EditText) layout.findViewById(R.id.et5);
                    final EditText et6 = (EditText) layout.findViewById(R.id.et6);

                    AlertDialog alertDialog = new AlertDialog.Builder(KDActivity.this).setTitle("请完成 ").setView(layout)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (et1.getText().toString().equals("")) {
                                        Toast.makeText(KDActivity.this, "件数不能为空", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    if (et2.getText().toString().equals("")) {
                                        Toast.makeText(KDActivity.this, "箱数不能为空", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    if (et3.getText().toString().equals("")) {
                                        Toast.makeText(KDActivity.this, "单价不能为空", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    if (et4.getText().toString().equals("")) {
                                        Toast.makeText(KDActivity.this, "体积不能为空", Toast.LENGTH_LONG).show();
                                        return;
                                    }
//                                    if (et5s.getText().toString().equals("")) {
//                                        Toast.makeText(KDActivity.this, "材质不能为空", Toast.LENGTH_LONG).show();
//                                        return;
//                                    }
//                                    if (et6s.getText().toString().equals("")) {
//                                        Toast.makeText(KDActivity.this, "单重不能为空", Toast.LENGTH_LONG).show();
//                                        return;
//                                    }
                                    entity.setJs(Integer.parseInt(et1.getText().toString()));
                                    entity.setXs(Integer.parseInt(et2.getText().toString()));
                                    entity.setDj(Double.parseDouble(et3.getText().toString()));
                                    entity.setTiji(Double.parseDouble(et4.getText().toString()));
                                    entity.setFmaterial(et5s.getText().toString());
                                    if(et6s.getText().toString().equals("")) {
                                        entity.setFsingleweight(0);
                                    }else{
                                        entity.setFsingleweight(Double.parseDouble(et6s.getText().toString()));
                                    }
                                    entity.setBianhao(et5.getText().toString() + "");
                                    entity.setBeizhu(et6.getText().toString() + "");
                                    listentry.add(entity);
                                    KDActivity.this.adapter.notifyDataSetChanged();
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

    /**
     * 供应商获取
     */
    class Task2 extends AsyncTask<Void, Integer, Integer> {


        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            list.clear();
            dialog=CustomProgress.show(KDActivity.this,"搜寻中...", true, null);
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
//            rpc.addProperty("FSql", "select FNAME ,FNUMBER from t_Supplier where fname like '%" + cmp + "%'");
            rpc.addProperty("FSql", "select a.fname ,a.fnumber,a.fitemid,a.fparentid,b.faddress,b.ffax,b.fphone from t_item a left join t_supplier b on a.fitemid=b.fitemid where a.fitemclassid=8 and  (left(left(a.ffullname,CHARINDEX('_', a.ffullname)),2)=(select fdescription from t_user where fname='"+UTIL.FNAME+"') or\n"  +
                    "left(a.ffullname,2)=(select fdescription from t_user where fname='"+UTIL.FNAME+"')) and a.fname like '%" + cmp + "%'");
            rpc.addProperty("FTable", "t_item");

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
//                        WLEntity entity = new WLEntity();
                        Element recordEle = (Element) iter.next();
                        String fname = recordEle.elementTextTrim("fname"); // 拿到head节点下的子节点title值
                        String fnumber = recordEle.elementTextTrim("fnumber");
                        String fitemid = recordEle.elementTextTrim("fitemid");
                        String fparentid = recordEle.elementTextTrim("fparentid");
                        String faddress = recordEle.elementTextTrim("faddress");
                        String fphone = recordEle.elementTextTrim("fphone");
                        String ffax = recordEle.elementTextTrim("ffax");
//                        entity.setFname(fname);
//                        entity.setFnumber(recordEle.elementTextTrim("FNUMBER"));
                        FileBean entity = new FileBean(Integer.parseInt(fitemid),Integer.parseInt(fparentid),fname,faddress,fnumber,ffax+","+fphone);
                        list.add(entity);
                        // 遍历Header节点下的Response节点
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
                rpc1.addProperty("FSql", "select a.FNAME ,a.FNUMBER,a.fitemid,a.fparentid,b.faddress,b.ffax,b.fphone from t_item a left join t_supplier b on a.fitemid=b.fitemid where a.fitemclassid=8 and a.fname like '%" + cmp + "%'");
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
//                            WLEntity entity1 = new WLEntity();
                            Element recordEle1 = (Element) iter1.next();
                            String fname = recordEle1.elementTextTrim("FNAME"); // 拿到head节点下的子节点title值
                            String fnumber = recordEle1.elementTextTrim("FNUMBER");
                            System.out.println(fnumber);
                            String fitemid = recordEle1.elementTextTrim("fitemid");
                            String fparentid = recordEle1.elementTextTrim("fparentid");
                            String faddress = recordEle1.elementTextTrim("faddress");
                            String fphone = recordEle1.elementTextTrim("fphone");
                            String ffax = recordEle1.elementTextTrim("ffax");
//                            entity1.setFname(fname);
//                            entity1.setFnumber(fnumber);
                            FileBean entity1 = new FileBean(Integer.parseInt(fitemid),Integer.parseInt(fparentid),fname,faddress,fnumber,ffax+","+fphone);
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

            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.activity_tree, null);
            ListView lv = (ListView) layout.findViewById(R.id.lv_tree);
//            final WLApater3 adapter = new WLApater3(KDActivity.this, list);
            final TreeListViewAdapter adapter;
            try {
                adapter = new SimpleTreeAdapter<FileBean>(lv, KDActivity.this, list, 10);
                lv.setAdapter(adapter);

                final AlertDialog alertDialog2 = new AlertDialog.Builder(KDActivity.this).setTitle("请选择供应商").setView(layout).create();
                alertDialog2.show();

                adapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                    @Override
                    public void onClick(Node node, int position) {
                        if (node.isLeaf()) {
                            tvkh.setText(node.getName() + "");
                            etdz.setText(node.getFimage());
                            String spare[] = node.getFmodel().split(",");
                            int i = spare.length;
                            switch (i){
                                case 0:
                                    break;
                                case 1:
                                    System.out.println(node.getFmodel());
                                    System.out.println("字符串第一个字母:"+node.getFmodel().substring(0,1)+"-----------------------------------");
                                    if(node.getFmodel().substring(0,1)==","){
                                        etdh.setText(spare[0]);
                                    }else{
                                        etsj.setText(spare[0]);
                                    }
                                    break;
                                case 2:
                                    etsj.setText(spare[0]);
                                    etdh.setText(spare[1]);
                                    break;
                            }

                            System.out.println(node.getFnumber());
                            FSUPPLYNUMBER = node.getFnumber();
                            cmpname = node.getName();
                            alertDialog2.dismiss();
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
            mt = spmt.getSelectedItem().toString() + "";
            Log.i("此时选中的唛头值",mt+"==============================");
            dz = etdz.getText().toString() + "";
            gd = etgd.getText().toString() + "";
            sj = etsj.getText().toString() + "";
            dh = etdh.getText().toString() + "";
            dialog=CustomProgress.show(KDActivity.this,"提交中...", true, null);
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {
            try {
                Date datew = new Date(System.currentTimeMillis());
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String no = "NO" + format.format(datew);
                // 命名空间
                String nameSpace = "http://tempuri.org/";
                // 调用的方法名称
                String methodName = "F_SendBill";
                // EndPoint
                String endPoint = UTIL.ENDPOINT;
                // SOAP Action
                String soapAction = "http://tempuri.org/F_SendBill";

                // 指定WebService的命名空间和调用的方法名
                SoapObject rpc = new SoapObject(nameSpace, methodName);

                rpc.addProperty("FTrantype", "71");
                rpc.addProperty("FSBILLNO", no);
                rpc.addProperty("InterID", inter);
                rpc.addProperty("BillNO", bill);
                //表头
                Document document = DocumentHelper.createDocument();
                Element rootElement = document.addElement("NewDataSet");
                Element cust = rootElement.addElement("Cust");
                //供应商number
                cust.addElement("FSUPPLYNUMBER").setText(FSUPPLYNUMBER);
                //供应商名称
                cust.addElement("FSUPPLYNAME").setText(cmpname);
                //解释
                cust.addElement("FEXPLANATION").setText(no);
                //唛头
                cust.addElement("FHeadSelfP0220").setText(mt + "");
                //地址
                cust.addElement("FHeadSelfP0221").setText(dz + "");
                //跟单员
                cust.addElement("FHeadSelfP0222").setText(gd + "");
                //手机号
                cust.addElement("FHeadSelfP0223").setText(sj + "");
                //电话
                cust.addElement("FHeadSelfP0224").setText(dh + "");
                //开单日期
                cust.addElement("FDATE").setText(date);
                //交货日期
                System.out.println("新的日期："+date1+"======================================");
                cust.addElement("FDATE1").setText(date1);

                //表体
                Document document2 = DocumentHelper.createDocument();
                Element rootElement2 = document2.addElement("NewDataSet");
                for (WLEntity e : listentry) {
                    Log.i("qqq", e.getFnumber() + "sss");
                    Element cust2 = rootElement2.addElement("Cust");
                    //体积
                    cust2.addElement("FEntrySelfP0229").setText(e.getTiji() + "");
                    //fnumber
                    cust2.addElement("Fnumber").setText(e.getFnumber());
                    //品名
                    cust2.addElement("FNAME").setText(e.getFname() + "");
                    //总数
                    cust2.addElement("FQTY").setText(e.getXs() * e.getJs() + "");
                    //单价
                    cust2.addElement("FPRICE").setText(e.getDj() + "");
                    //总价
                    cust2.addElement("FAMOUNT").setText(e.getXs() * e.getJs() * e.getDj() + "");
                    //备注
                    cust2.addElement("FNOTE").setText(e.getBeizhu() + "");
                    //件数
                    cust2.addElement("FEntrySelfP0227").setText(e.getJs() + "");
                    //箱数
                    cust2.addElement("FEntrySelfP0228").setText(e.getXs() + "");
                    //编号
                    cust2.addElement("FEntrySelfP0230").setText(e.getBianhao() + "");
                    //材质
                    cust2.addElement("FEntrySelfP0236").setText(e.getFmaterial());
                    //单重
                    cust2.addElement("FEntrySelfP0237").setText(String.valueOf(e.getFsingleweight()));
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
                Log.i("sss", stringWriter.toString() + "***********");

                Log.i("sss", stringWriter2.toString().substring(38) + "***********");
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
                Log.i("提交采购订单返回的结果：", result + "----------------------------");
                if(result.equals("成功")) {
                    return 1;
                }else{
                    return 2;
                }
            } catch (Exception e) {
                Log.i("sss", e.toString() + "++++++");
                return 2;
            }

        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer integer) {
            dialog.dismiss();
            if(integer.equals(1)) {
                listentry.clear();
                adapter.notifyDataSetChanged();
                Toast.makeText(KDActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(KDActivity.this, "提交异常", Toast.LENGTH_SHORT).show();
            }
            if(flag){
                finish();
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
