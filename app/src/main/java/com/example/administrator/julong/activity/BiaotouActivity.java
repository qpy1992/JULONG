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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.julong.R;
import com.example.administrator.julong.adapter.SimpleTreeAdapter;
import com.example.administrator.julong.adapter.TreeListViewAdapter;
import com.example.administrator.julong.entity.FileBean;
import com.example.administrator.julong.entity.Node;
import com.example.administrator.julong.util.DateUtil;
import com.example.administrator.julong.util.UTIL;
import com.example.administrator.julong.adapter.DDAdapter;
import com.example.administrator.julong.adapter.WLApater3;
import com.example.administrator.julong.entity.BiaotiEntity;
import com.example.administrator.julong.entity.BiaotouEntity;
import com.example.administrator.julong.entity.StockEntity;
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

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class BiaotouActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CustomProgress dialog;
    private ListView lv;
    private Spinner sp1;
    private ArrayAdapter<String> arr_adapter;
    private int year,month,day;
    private ImageView cx;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String d1, d2;
    private TextView tv1, tv2, tv_sup;
    private DDAdapter adapter;
    private List<BiaotouEntity> list = new ArrayList<>();
    private List<StockEntity> list1 = new ArrayList<>();
    private List<BiaotiEntity> listbt = new ArrayList<>();
    private String result, supname, sup, fbiaoji;
    private List<FileBean> list2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biaotou);
        setTool();
        Calendar mycalendar = Calendar.getInstance();
        year = mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        month = mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        day = mycalendar.get(Calendar.DAY_OF_MONTH);
        lv = (ListView) findViewById(R.id.lv);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv_sup = (TextView) findViewById(R.id.tv_sup);
        cx = (ImageView) findViewById(R.id.cx);
        sp1 = (Spinner)findViewById(R.id.sp1);
        arr_adapter= new ArrayAdapter<String>(BiaotouActivity.this, android.R.layout.simple_spinner_item, new String[]{"","完全入库","未完全入库"});
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        sp1.setAdapter(arr_adapter);
        //RkTask task1 = new RkTask();
        //task1.execute();
        adapter = new DDAdapter(BiaotouActivity.this, list);
        lv.setAdapter(adapter);
        setListen();
        Date date = new Date(System.currentTimeMillis());
        tv1.setText(DateUtil.getPastDate(15));
        tv2.setText(DateUtil.getFetureDate(1));
        d1 = DateUtil.getPastDate(7);
        d2 = sdf.format(date);
        fbiaoji = "0";
        UTIL.verifyStoragePermissions(this);
    }

    private void setListen() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BiaotouActivity.this, BiaotiActivity.class);
                intent.putExtra("id", list.get(position).getFInterID() + "");
                Log.i("sss", list.get(position).getFInterID() + "*********");
                startActivity(intent);
            }
        });

        cx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                listbt.clear();
                UpdateTextTask updateTextTask = new UpdateTextTask();
                updateTextTask.execute();

            }
        });

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(BiaotouActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String s = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        try {
                            java.util.Date date = sdf.parse(s);
                            String s1 = sdf.format(date);
                            tv1.setText(s1);
                            d1 = s1;
                        } catch (Exception e) {
                        }
                    }
                }, year, month, day);
                dpd.show();//显示DatePickerDialog组件
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(BiaotouActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String s = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        try {
                            java.util.Date date = sdf.parse(s);
                            String s1 = sdf.format(date);
                            tv2.setText(s1);
                            d2 = s1;
                        } catch (Exception e) {
                        }
                    }
                }, year, month, day);
                dpd.show();//显示DatePickerDialog组件
            }
        });

        tv_sup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(BiaotouActivity.this);
                et.setBackgroundResource(R.drawable.et_shape);
                new AlertDialog.Builder(BiaotouActivity.this).setTitle("请输入供应商").setIcon(
                        R.drawable.suppliers).setView(
                        et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sup = et.getText().toString().trim();
                        SupTask task = new SupTask();
                        task.execute();
                    }
                })
                        .setNegativeButton("取消", null).show();
            }
        });

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 1:
                        fbiaoji = "1";
                        break;
                    case 2:
                        fbiaoji = null;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setTool() {
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("订单列表");

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



    class UpdateTextTask extends AsyncTask<Void, Integer, Integer> {
        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            dialog=CustomProgress.show(BiaotouActivity.this,"搜寻中...", true, null);
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
            if(supname!=null&&fbiaoji=="1") {//选择供应商，选择完全入库
                rpc.addProperty("FSql", "select a.FHeadSelfP0220,a.FHeadSelfP0221,a.FHeadSelfP0223,a.FHeadSelfP0224,a.fbiaoji, b.FEntrySelfP0234,b.FEntrySelfP0235,b.FEntrySelfP0229,b.FEntrySelfP0230,b.FQTY,b.FPRICE,a.finterid,b.finterid, b.fnote,b.FEntrySelfP0236,b.FEntrySelfP0237,a.finterid,a.fdate,a.FHeadSelfP0226,a.fbillno,c.fname fbillerid,d.fname fsupplyid,d.fnumber fsupplyno, a.FHeadSelfP0222,FAuxQty,FAuxPrice,FAmount,e.fname fwuliao,e.fimage1,e.fmodel, e.fnumber,f.fname fdanwei  from poorder a \n" +
                        "inner join poorderentry b on a.finterid=b.finterid inner join t_user c on c.FUserID=a.FBillerID\n" +
                        "inner join t_Supplier d on d.fitemid=a.FSupplyID inner join t_icitem e on e.fitemid=b.fitemid inner join t_MeasureUnit f on f.FMeasureUnitID=b.FUnitID  WHERE (a.FDate BETWEEN  '" + d1 + "' AND '" + d2 + "') and d.fname like '%" + supname + "%' and a.fbiaoji=1 order by a.FDate desc ,a.finterid desc");
            }else if(supname!=null&&fbiaoji=="0"){//选择供应商，未选择是否完全入库
                rpc.addProperty("FSql", "select a.FHeadSelfP0220,a.FHeadSelfP0221,a.FHeadSelfP0223,a.FHeadSelfP0224,a.fbiaoji, b.FEntrySelfP0234,b.FEntrySelfP0235,b.FEntrySelfP0229,b.FEntrySelfP0230,b.FQTY,b.FPRICE,a.finterid,b.finterid, b.fnote,b.FEntrySelfP0236,b.FEntrySelfP0237,a.finterid,a.fdate,a.FHeadSelfP0226,a.fbillno,c.fname fbillerid,d.fname fsupplyid,d.fnumber fsupplyno, a.FHeadSelfP0222,FAuxQty,FAuxPrice,FAmount,e.fname fwuliao,e.fimage1,e.fmodel, e.fnumber,f.fname fdanwei  from poorder a \n" +
                        "inner join poorderentry b on a.finterid=b.finterid inner join t_user c on c.FUserID=a.FBillerID\n" +
                        "inner join t_Supplier d on d.fitemid=a.FSupplyID inner join t_icitem e on e.fitemid=b.fitemid inner join t_MeasureUnit f on f.FMeasureUnitID=b.FUnitID  WHERE (a.FDate BETWEEN  '" + d1 + "' AND '" + d2 + "') and d.fname like '%" + supname + "%'  order by a.FDate desc ,a.finterid desc");
            }else if(supname==null&&fbiaoji==null){//未选择供应商，选择未完全入库
                rpc.addProperty("FSql", "select a.FHeadSelfP0220,a.FHeadSelfP0221,a.FHeadSelfP0223,a.FHeadSelfP0224,a.fbiaoji, b.FEntrySelfP0234,b.FEntrySelfP0235,b.FEntrySelfP0229,b.FEntrySelfP0230,b.FQTY,b.FPRICE,a.finterid,b.finterid, b.fnote,b.FEntrySelfP0236,b.FEntrySelfP0237,a.finterid,a.fdate,a.FHeadSelfP0226,a.fbillno,c.fname fbillerid,d.fname fsupplyid,d.fnumber fsupplyno, a.FHeadSelfP0222,FAuxQty,FAuxPrice,FAmount,e.fname fwuliao,e.fimage1,e.fmodel, e.fnumber,f.fname fdanwei  from poorder a \n" +
                        "inner join poorderentry b on a.finterid=b.finterid inner join t_user c on c.FUserID=a.FBillerID\n" +
                        "inner join t_Supplier d on d.fitemid=a.FSupplyID inner join t_icitem e on e.fitemid=b.fitemid inner join t_MeasureUnit f on f.FMeasureUnitID=b.FUnitID  WHERE (a.FDate BETWEEN  '" + d1 + "' AND '" + d2 + "') and a.fbiaoji is null order by a.FDate desc ,a.finterid desc");
            }else if(supname==null&&fbiaoji=="1"){//未选择供应商，选择完全入库
                rpc.addProperty("FSql", "select a.FHeadSelfP0220,a.FHeadSelfP0221,a.FHeadSelfP0223,a.FHeadSelfP0224,a.fbiaoji, b.FEntrySelfP0234,b.FEntrySelfP0235,b.FEntrySelfP0229,b.FEntrySelfP0230,b.FQTY,b.FPRICE,a.finterid,b.finterid, b.fnote,b.FEntrySelfP0236,b.FEntrySelfP0237,a.finterid,a.fdate,a.FHeadSelfP0226,a.fbillno,c.fname fbillerid,d.fname fsupplyid,d.fnumber fsupplyno, a.FHeadSelfP0222,FAuxQty,FAuxPrice,FAmount,e.fname fwuliao,e.fimage1,e.fmodel, e.fnumber,f.fname fdanwei  from poorder a \n" +
                        "inner join poorderentry b on a.finterid=b.finterid inner join t_user c on c.FUserID=a.FBillerID\n" +
                        "inner join t_Supplier d on d.fitemid=a.FSupplyID inner join t_icitem e on e.fitemid=b.fitemid inner join t_MeasureUnit f on f.FMeasureUnitID=b.FUnitID  WHERE (a.FDate BETWEEN  '" + d1 + "' AND '" + d2 + "') and a.fbiaoji=1 order by a.FDate desc ,a.finterid desc");
            }else if(supname!=null&&fbiaoji==null) {//选择供应商,选择未完全入库
                rpc.addProperty("FSql", "select a.FHeadSelfP0220,a.FHeadSelfP0221,a.FHeadSelfP0223,a.FHeadSelfP0224,a.fbiaoji, b.FEntrySelfP0234,b.FEntrySelfP0235,b.FEntrySelfP0229,b.FEntrySelfP0230,b.FQTY,b.FPRICE,a.finterid,b.finterid, b.fnote,b.FEntrySelfP0236,b.FEntrySelfP0237,a.finterid,a.fdate,a.FHeadSelfP0226,a.fbillno,c.fname fbillerid,d.fname fsupplyid,d.fnumber fsupplyno, a.FHeadSelfP0222,FAuxQty,FAuxPrice,FAmount,e.fname fwuliao,e.fimage1,e.fmodel, e.fnumber,f.fname fdanwei  from poorder a \n" +
                        "inner join poorderentry b on a.finterid=b.finterid inner join t_user c on c.FUserID=a.FBillerID\n" +
                        "inner join t_Supplier d on d.fitemid=a.FSupplyID inner join t_icitem e on e.fitemid=b.fitemid inner join t_MeasureUnit f on f.FMeasureUnitID=b.FUnitID  WHERE (a.FDate BETWEEN  '" + d1 + "' AND '" + d2 + "') and d.fname like '%" + supname + "%'  and a.fbiaoji is null order by a.FDate desc ,a.finterid desc");
            }else{//查找所有单子
                rpc.addProperty("FSql", "select a.FHeadSelfP0220,a.FHeadSelfP0221,a.FHeadSelfP0223,a.FHeadSelfP0224,a.fbiaoji, b.FEntrySelfP0234,b.FEntrySelfP0235,b.FEntrySelfP0229,b.FEntrySelfP0230,b.FQTY,b.FPRICE,a.finterid,b.finterid, b.fnote,b.FEntrySelfP0236,b.FEntrySelfP0237,a.finterid,a.fdate,a.FHeadSelfP0226,a.fbillno,c.fname fbillerid,d.fname fsupplyid,d.fnumber fsupplyno, a.FHeadSelfP0222,FAuxQty,FAuxPrice,FAmount,e.fname fwuliao,e.fimage1,e.fmodel, e.fnumber,f.fname fdanwei  from poorder a \n" +
                        "inner join poorderentry b on a.finterid=b.finterid inner join t_user c on c.FUserID=a.FBillerID\n" +
                        "inner join t_Supplier d on d.fitemid=a.FSupplyID inner join t_icitem e on e.fitemid=b.fitemid inner join t_MeasureUnit f on f.FMeasureUnitID=b.FUnitID  WHERE (a.FDate BETWEEN  '" + d1 + "' AND '" + d2 + "') order by a.FDate desc ,a.finterid desc");
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
                Log.i("查询订单返回的结果：", object.getProperty(0).toString() + "《《《《《《《《《《《《《《《《《《《《《《《《《");
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
                        BiaotouEntity entity = new BiaotouEntity();
                        Element recordEle = (Element) iter.next();
                        String fdate1 = recordEle.elementTextTrim("FHeadSelfP0226");
                        String FDate = recordEle.elementTextTrim("fdate"); // 拿到head节点下的子节点title值
                        String FBillNo = recordEle.elementTextTrim("fbillno");
                        String FInterID = recordEle.elementTextTrim("finterid");
                        String fbiaoji = recordEle.elementTextTrim("fbiaoji");
                        entity.setFbiaoji(fbiaoji);
                        entity.setFInterID(FInterID);
                        entity.setFBillNo(FBillNo);
                        entity.setFDate(FDate);
                        entity.setFdate1(fdate1);
                        entity.setGd(recordEle.elementTextTrim("FHeadSelfP0222"));
                        entity.setGys(recordEle.elementTextTrim("fsupplyid"));
                        entity.setGysno(recordEle.elementTextTrim("fsupplyno"));
                        entity.setMt(recordEle.elementTextTrim("FHeadSelfP0220"));
                        entity.setDz(recordEle.elementTextTrim("FHeadSelfP0221"));
                        entity.setSj(recordEle.elementTextTrim("FHeadSelfP0223"));
                        entity.setDh(recordEle.elementTextTrim("FHeadSelfP0224"));
                        //entity.setFinterid(recordEle.elementTextTrim("finterid"));
                        BiaotiEntity bt = new BiaotiEntity();
                        bt.setFAmount(recordEle.elementTextTrim("FAmount"));
                        bt.setFName(recordEle.elementTextTrim("fwuliao"));
                        bt.setFimage1(recordEle.elementTextTrim("fimage1"));
                        Log.i("bitmap的值",bt.getFimage1());
                        bt.setFAuxQty(recordEle.elementTextTrim("FAuxQty"));
                        bt.setFAuxPrice(recordEle.elementTextTrim("FAuxPrice"));
                        bt.setFinterid(FInterID);
                        bt.setFnote(recordEle.elementTextTrim("fnote"));
                        bt.setJs(Integer.parseInt(recordEle.elementTextTrim("FEntrySelfP0234")));
                        bt.setXs(Integer.parseInt(recordEle.elementTextTrim("FEntrySelfP0235")));
                        bt.setTj(Double.parseDouble(recordEle.elementTextTrim("FEntrySelfP0229")));
                        bt.setBianhao(recordEle.elementTextTrim("FEntrySelfP0230"));
                        bt.setFnumber(recordEle.elementTextTrim("fnumber"));
                        bt.setFmaterial(recordEle.elementTextTrim("FEntrySelfP0236"));
                        String fsingleweight = recordEle.elementTextTrim("FEntrySelfP0237");
                        System.out.println("当前的单重为:"+fsingleweight);
                        if(!fsingleweight.equals("")) {
                            bt.setFsingleweight(Double.parseDouble(fsingleweight));
                        }else{
                            bt.setFsingleweight(0);
                        }
                        bt.setFmodel(recordEle.elementTextTrim("fmodel"));
                        bt.setFunit(recordEle.elementTextTrim("fdanwei"));
                        listbt.add(bt);
                        list.add(entity);
                        //                       entity.setBt(listbt);
//                        if(s.equals( "")){
//                            s=recordEle.elementTextTrim("fbillno");
//                        }
//                        if(s.equals(recordEle.elementTextTrim("fbillno"))){
//
//                        }else {
//                            s=recordEle.elementTextTrim("fbillno");
//                            list.add(entity);
//                            listbt.clear();
//                        }

                        for (BiaotouEntity e : list) {
                            List<BiaotiEntity> biaotiEntityList = new ArrayList<>();
                            for (BiaotiEntity b : listbt) {
                                if (e.getFInterID().equals(b.getFinterid())) {
                                    biaotiEntityList.add(b);
                                }
                            }
                            e.setBt(biaotiEntityList);
                        }
                        List<BiaotouEntity> cp = new ArrayList<>();
                        for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
                            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
                                if  (list.get(j).getFBillNo().equals(list.get(i).getFBillNo()))  {
                                    list.remove(j);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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


    class RkTask extends AsyncTask<Void, Integer, Integer> {
        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            list1.clear();
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
            rpc.addProperty("FSql", "select fitemid,fname from t_stock");
            //  rpc.addProperty("FSql", "select b.fname,b.fnumber ,b.fmodel ,a.fimage  from z_image a inner join t_icitem b on a.fnumber=b.fnumber where b.fname like '%"+name+"%'  ");
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
                    // 遍历head节点
                    while (iter.hasNext()) {
                        StockEntity entitys = new StockEntity();
                        Element recordEle = (Element) iter.next();
                        String fname = recordEle.elementTextTrim("fname");
                        String fitemid = recordEle.elementTextTrim("fitemid");
                        Log.i("qqq", fname);
                        System.out.println("title:" + fname);
                        entitys.setFname(fname);
                        entitys.setFitemid(fitemid);
                        list1.add(entitys);
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

        }
        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }




    class SupTask extends AsyncTask<Void,Integer,Integer> {

        @Override
        protected void onPreExecute() {
            list2.clear();
            dialog=CustomProgress.show(BiaotouActivity.this,"搜寻中...", true, null);
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
            rpc.addProperty("FSql", "select FNAME ,FNUMBER,fitemid,fparentid from t_item where fitemclassid=8 and  (left(left(ffullname,CHARINDEX('_', ffullname)),2)=(select fdescription from t_user where fname='" + UTIL.FNAME + "') or\n" +
                    "left(ffullname,2)=(select fdescription from t_user where fname='" + UTIL.FNAME + "')) and fname like '%" + sup + "%'");
//            rpc.addProperty("FSql", "select FNAME ,FNUMBER from t_Supplier where fname like '%" + sup + "%'");
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
                        String fnumber = recordEle.elementTextTrim("FNUMBER");
                        String fitemid = recordEle.elementTextTrim("fitemid");
                        String fparentid = recordEle.elementTextTrim("fparentid");
                        FileBean entity = new FileBean(Integer.parseInt(fitemid), Integer.parseInt(fparentid), fname, null, fnumber, null);
                        list2.add(entity);
                        // 遍历Header节点下的Response节点
                    }

                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (list2.size() == 0) {
                // 指定WebService的命名空间和调用的方法名
                SoapObject rpc1 = new SoapObject(nameSpace, methodName);

                // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
                rpc1.addProperty("FSql", "select FNAME ,FNUMBER,fitemid,fparentid from t_item where fitemclassid=8 and fname like '%" + sup + "%'");
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

                    Document doc1 = null;
                    try {
                        doc1 = DocumentHelper.parseText(result1); // 将字符串转为XML

                        Element rootElt1 = doc1.getRootElement(); // 获取根节点

                        System.out.println("根节点：" + rootElt1.getName()); // 拿到根节点的名称

                        Iterator iter1 = rootElt1.elementIterator("Cust"); // 获取根节点下的子节点head

                        System.out.println("内容:" + iter1);

                        // 遍历head节点

                        while (iter1.hasNext()) {
//                            WLEntity entity1 = new WLEntity();
                            Element recordEle1 = (Element) iter1.next();
                            String fname = recordEle1.elementTextTrim("FNAME"); // 拿到head节点下的子节点title值
                            String fnumber = recordEle1.elementTextTrim("FNUMBER");
                            System.out.println(fnumber);
                            String fitemid = recordEle1.elementTextTrim("fitemid");
                            String fparentid = recordEle1.elementTextTrim("fparentid");
//                            entity1.setFname(fname);
//                            entity1.setFnumber(fnumber);
                            FileBean entity1 = new FileBean(Integer.parseInt(fitemid), Integer.parseInt(fparentid), fname, null, fnumber, null);
                            list2.add(entity1);
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
                adapter = new SimpleTreeAdapter<FileBean>(lv, BiaotouActivity.this, list2, 10);
                lv.setAdapter(adapter);

                final AlertDialog alertDialog2 = new AlertDialog.Builder(BiaotouActivity.this).setTitle("请选择供应商").setView(layout).create();
                alertDialog2.show();

                adapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                    @Override
                    public void onClick(Node node, int position) {
                        if (node.isLeaf()) {
                            supname = node.getName();
                            tv_sup.setText(supname);
                            alertDialog2.dismiss();
                        }
                    }
                });
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }
    }
