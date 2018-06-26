package com.example.administrator.julong.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.julong.R;
import com.example.administrator.julong.adapter.SimpleTreeAdapter;
import com.example.administrator.julong.adapter.TreeListViewAdapter;
import com.example.administrator.julong.entity.FileBean;
import com.example.administrator.julong.entity.Node;
import com.example.administrator.julong.util.UTIL;
import com.example.administrator.julong.adapter.GridviewApater2;
import com.example.administrator.julong.adapter.WLApater;
import com.example.administrator.julong.entity.WLEntity;
import com.example.administrator.julong.view.CustomProgress;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.lang.Integer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
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


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private List<WLEntity> list = new ArrayList<>();
    private List<FileBean> list1 = new ArrayList<>();
    public Button uploadImageBt, cs, btczwl, tj;//上传图片
    public ImageView imageView;//显示图片
    // public HttpUtils httpUtils;
    public String url = "http://192.168.10.114:8080/Upload/upload";
    //生成当前系统时间的文件用于存储相机拍摄的照片
    public File tempFile = null;//new File(Environment.getExternalStorageDirectory(),getPhotoFileName());
    public String[] items = {"拍照", "相册"};
    public String title = "选择照片";
    public DialogInterface.OnClickListener dialogListener;
    public static final int PHOTO_CAMERA = 0;//表示从相机获得照片
    public static final int PHOTO_WALL = 1;//表示从相册获得照片
    public static final int PHOTO_STORE = 2;//表示需要存储图片
    public static final int PHOTO_NOT_STORE = 3;//表示不需要存储图片
    public Intent tempIntent;//目的是为了解决获取图片路径为null的问题
    public Handler mHandler;//用于更新进度条
    public ProgressDialog progressDialog;//进度条
    private EditText et;
    private String name, imgname;
    private CustomProgress dialog;
    private Toolbar toolbar;
    private WLEntity entity = new WLEntity();
    private TextView tvname, tvnumber;
    private GridView gv;
    private List<WLEntity> listwl = new ArrayList<>();
    private GridviewApater2 gridviewApater2;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setTool();
//        httpUtils = new HttpUtils(100000);
//        httpUtils.configCurrentHttpCacheExpiry(5000);

    }

    /**
     * 初始化view控件
     */
    public void initView() {
        gv = (GridView) findViewById(R.id.gv);
        gridviewApater2 = new GridviewApater2(this, listwl);
        gv.setAdapter(gridviewApater2);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("xx", position + "%");
                if (position == listwl.size()) {
                    if (null == entity) {
                        Toast.makeText(MainActivity.this, "请先选择物料", Toast.LENGTH_LONG).show();

                    } else {
                        showDialog(title, items);
                    }
                }

            }
        });
        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

                if (position == listwl.size()) {
                    if (null == entity) {


                    }
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("确认删除吗？");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            listwl.remove(position);
                            gridviewApater2.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
                return true;
            }
        });
        tvnumber = (TextView) findViewById(R.id.tvnumber);
        tvname = (TextView) findViewById(R.id.tvname);
        btczwl = (Button) findViewById(R.id.btczwl);
        tj = (Button) findViewById(R.id.tj);
        tj.setOnClickListener(this);
        btczwl.setOnClickListener(this);
        et = (EditText) findViewById(R.id.et);
        uploadImageBt = (Button) findViewById(R.id.upload_image);
        cs = (Button) findViewById(R.id.cs);
        imageView = (ImageView) findViewById(R.id.imageView);
        uploadImageBt.setOnClickListener(this);
        cs.setOnClickListener(this);
        progressDialog = getProgressDialog();//获得进度条
        dialogListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        tempFile = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());
                        //调用系统拍照
                        startCamera(dialog);
                        break;
                    case 1:
                        //打开系统图库
                        tempFile = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());
                        startWall(dialog);
                        break;
                    default:
                        break;
                }
            }
        };
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.arg1 > 0)
                    progressDialog.setProgress(msg.arg1);//更新进度条
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload_image:
                showDialog(title, items);//显示选择图片的dialog
                break;
            case R.id.cs:
                Log.i("sss", "--------------");
                //list.clear();

                break;
            case R.id.btczwl:
                name = et.getText().toString().trim();
                Task1 task1 = new Task1();
                task1.execute();
                break;
            case R.id.tj:
                if (listwl.size()==0) {
                    Toast.makeText(MainActivity.this, "请选择图片", Toast.LENGTH_LONG).show();
                    return;
                }
                Task2 task2 = new Task2();
                task2.execute();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_CAMERA:
                //表示从相机获得的照片，需要进行裁剪
                startPhotoCut(FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", tempFile), 300, true);
                break;
            case PHOTO_WALL:
                if (null != data)
                    startPhotoCut(data.getData(), 300, true);
                break;
            case PHOTO_STORE:
                if (null != data) {
                    setPictureToImageView(data, true);
                }
                break;
            case PHOTO_NOT_STORE:
                if (null != data) {
                    setPictureToImageView(data, false);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 显示选择图片来源的dialog(来自拍照还是本地图库)
     *
     * @param title
     * @param items
     */
    public void showDialog(String title, String[] items) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this).setTitle(title).setItems(items, dialogListener);
        //显示dialog
        dialog.show();
    }

    /**
     * 调用相机来照相
     *
     * @param dialog
     */
    public void startCamera(DialogInterface dialog) {
        dialog.dismiss();//首先隐藏选择照片来源的dialog
        //调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("camerasensortype", 2);//调用前置摄像头
        intent.putExtra("autofocus", true);//进行自动对焦操作
        intent.putExtra("fullScreen", false);//设置全屏
        intent.putExtra("showActionIcons", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", tempFile));//指定调用相机之后所拍照存储到的位置
        startActivityForResult(intent, PHOTO_CAMERA);
    }

    /**
     * 打开系统图库
     *
     * @param dialog
     */
    public void startWall(DialogInterface dialog) {
        dialog.dismiss();//设置隐藏dialog
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, PHOTO_WALL);
    }

    /**
     * 将图片裁剪到指定大小
     *
     * @param uri
     * @param size
     * @param flag
     */
    public void startPhotoCut(Uri uri, int size, boolean flag) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("crop", true);//设置Intent中的view是可以裁剪的
        //设置宽高比
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //设置裁剪图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        //设置是否返回数据
        intent.putExtra("return-data", true);
        if (flag == true) {
            startActivityForResult(intent, PHOTO_STORE);
            Log.i("sss", "a");
        } else {
            Log.i("sss", "b");
            tempIntent = intent;
            try {
                startActivityForResult(tempIntent, PHOTO_NOT_STORE);
                System.out.println("haha");
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    /**
     * 将图片显示到ImageView上面
     *
     * @param data
     * @param flag 表示如果是拍照获得的照片的话则是true，如果是从系统选择的照片的话就是false
     */
    public void setPictureToImageView(Intent data, boolean flag) {
        Bundle bundle = data.getExtras();
        if (null != bundle) {
            Bitmap bitmap = bundle.getParcelable("data");
            imageView.setImageBitmap(bitmap);//将图片显示到ImageView上面
            WLEntity wlEntity = new WLEntity();
            wlEntity.setBitmap(bitmap);
            wlEntity.setFname(imgname);
            listwl.add(wlEntity);
            gridviewApater2.notifyDataSetChanged();
            //上传图片到服务器
            if (flag == false) {
                //需要首先修改tempFile的值
                String path = getSelectPhotoPath(tempIntent);
                Log.i("sss", "path:  " + path);
                System.out.println("path:  " + path);
                tempFile = new File(path);



            } else {

            }
            if (flag == true)
                savePictureToSD(bitmap);//保存图片到sd卡上面
        }
    }

    /**
     * 将图片保存到SD卡上面
     *
     * @param bitmap
     */
    public void savePictureToSD(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fos = null;

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//第2个参数表示压缩率，100表示不压缩
        try {
            fos = new FileOutputStream(tempFile);
            fos.write(baos.toByteArray());
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != baos) {
                    baos.close();
                    baos = null;
                }
                if (null != fos) {
                    fos.close();
                    fos = null;
                }
            } catch (Exception e2) {

            }
        }
    }


    /**
     * 生成当前所拍照片的名字(其值为IMG_当前时间.jpg)
     *
     * @return
     */
    public String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());//获得当前时间
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
        System.out.println("imagePath:  " + Environment.getExternalStorageDirectory() + "IMG" + format.format(date) + ".jpg");
        imgname = "IMG_" + format.format(date) + ".jpg";
        return "IMG_" + format.format(date) + ".jpg";
    }

    /**
     * 获得选择图片的路径
     *
     * @param data
     * @return
     */
    public String getSelectPhotoPath(Intent data) {
        String path = "";
        Uri uri = data.getData();
        Log.i("ss", uri + "-----");
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, proj, null, null, null);
        //获得选中图片的索引值
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        //将光标移到开头处
        cursor.moveToFirst();
        //根据索引值获得图片地址
        path = cursor.getString(index);
        return path;
    }

//    class UploadThread extends Thread {
//        @Override
//        public void run() {
//            uploadPicture();
//        }
//    }

    /**
     * 获取下载进度条
     *
     * @return
     */
    public ProgressDialog getProgressDialog() {
        ProgressDialog pg = new ProgressDialog(this);
        pg.setProgress(0);
        pg.setIndeterminate(false);
        pg.setCancelable(false);//表示该进度条不可以被点没
        pg.setTitle("上传进度");
        pg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置进度条风格
        pg.setMax(100);//设置进度条的最大值
        return pg;
    }


    class Task1 extends AsyncTask<Void, Integer, Integer> {


        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            list.clear();
            dialog=CustomProgress.show(MainActivity.this,"搜寻中...", true, null);
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
                        list1.add(entity);
                    }
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(list1.size()==0){
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
                            list1.add(entity1);
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
            if (list1.size() == 0) {
                Toast.makeText(MainActivity.this, "找不到物料", Toast.LENGTH_LONG).show();
                return;
            }
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.activity_tree, null);
            ListView lv = (ListView) layout.findViewById(R.id.lv_tree);
            final TreeListViewAdapter adapter;
            try {
                adapter = new SimpleTreeAdapter<FileBean>(lv, MainActivity.this, list1, 10);
                lv.setAdapter(adapter);


                final AlertDialog alertDialog2 = new AlertDialog.Builder(MainActivity.this).setTitle("请选择物料").setView(layout).create();
                alertDialog2.show();


                adapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener()
                {
                    @Override
                    public void onClick(Node node, int position) {
                        if (node.isLeaf()) {
                            entity.setFnumber(node.getFnumber());
                            entity.setFname(node.getName());
                            entity.setImg(node.getFimage());
                            entity.setGg(node.getFmodel());
                            alertDialog2.dismiss();
                            imageView.setImageBitmap(base64ToBitmap(node.getFimage()));
                            tvname.setText(node.getName());
                            tvnumber.setText(node.getFnumber());
                            name = node.getName();
                            getData();

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

    private void getData() {
        listwl.clear();
        Task3 task3 = new Task3();
        task3.execute();
    }

    class Task2 extends AsyncTask<Void, Integer, Integer> {


        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {

            dialog=CustomProgress.show(MainActivity.this,"搜寻中...", true, null);
            image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {
            try {

                // 命名空间
                String nameSpace = "http://tempuri.org/";
                // 调用的方法名称
                String methodName = "F_qinggoudan_insert";
                // EndPoint
                String endPoint =UTIL.ENDPOINT;
                // SOAP Action
                String soapAction = "http://tempuri.org/F_qinggoudan_insert";

                // 指定WebService的命名空间和调用的方法名
                SoapObject rpc = new SoapObject(nameSpace, methodName);

                // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
                rpc.addProperty("FNumber", entity.getFnumber());

                String i = bitmapToBase64(image);
               // Log.i("rrr", i + "");

                //图片名
                Document document1 = DocumentHelper.createDocument();
                Element rootElement1 = document1.addElement("NewDataSet");
                for (WLEntity e : listwl) {
                    Log.i("xxxx",  e.getFname()+"^^^^^^^^^^^^^^^^^^^^");
                    Element cust2 = rootElement1.addElement("Cust");
                    cust2.addElement("fname").setText(e.getFname());


                }

                //图片
                Document document2 = DocumentHelper.createDocument();
                Element rootElement2 = document2.addElement("NewDataSet");
                for (WLEntity e : listwl) {
                    Element cust2 = rootElement2.addElement("Cust");
                    cust2.addElement("fimage").setText(bitmapToBase64(e.getBitmap()));


                }

                //
                OutputFormat outputFormat = OutputFormat.createPrettyPrint();
                outputFormat.setSuppressDeclaration(false);
                outputFormat.setNewlines(false);
                StringWriter stringWriter = new StringWriter();
                StringWriter stringWriter2 = new StringWriter();
                // xmlWriter是用来把XML文档写入字符串的(工具)
                XMLWriter xmlWriter = new XMLWriter(stringWriter, outputFormat);
                XMLWriter xmlWriter2 = new XMLWriter(stringWriter2, outputFormat);
                // 把创建好的XML文档写入字符串
                xmlWriter.write(document1);
                xmlWriter2.write(document2);


                rpc.addProperty("base64string", stringWriter2.toString().substring(38));//<NewDataSet><Cust>fname</Cust><Cust>fname</Cust></NewDataSet>
                rpc.addProperty("Filename", stringWriter.toString().substring(38));


                //

                Log.i("qwe", stringWriter2.toString().substring(38));
                Log.i("asd", stringWriter.toString().substring(38));

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
                    Log.i("sss", e.toString() + "sss");
                }

                // 获取返回的数据
                SoapObject object = (SoapObject) envelope.bodyIn;
                // 获取返回的结果
                String result = object.getProperty(0).toString();
                Log.i("sss", result + "sss");

            } catch (Exception e) {
                Log.i("sss", e.toString() + "sss");
            }

            return 5;


        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer integer) {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, "修改图片成功", Toast.LENGTH_LONG).show();

        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

    public Bitmap base64ToBitmap(String base64String) {

        byte[] decode = Base64.decode(base64String, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);

        return bitmap;
    }

    /*
 *bitmap转base64
 */
    public String bitmapToBase64(Bitmap bitmap) {
        String result = "";
        ByteArrayOutputStream bos = null;
        try {
            if (null != bitmap) {
                bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);//将bitmap放入字节数组流中

                bos.flush();//将bos流缓存在内存中的数据全部输出，清空缓存
                bos.close();

                byte[] bitmapByte = bos.toByteArray();
                result = Base64.encodeToString(bitmapByte, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private void setTool() {
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("修改图片");

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
                        if (null == entity) {
                            Toast.makeText(MainActivity.this, "请选择物料", Toast.LENGTH_LONG).show();

                        } else {
                            showDialog(title, items);
                        }

                        break;

                }


                return true;

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);//加载menu文件到布局
        return true;
    }

    //获取全部图片
    class Task3 extends AsyncTask<Void, Integer, Integer> {


        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {

            dialog=CustomProgress.show(MainActivity.this,"搜寻中...", true, null);
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
            String endPoint =UTIL.ENDPOINT;
            // SOAP Action
            String soapAction = "http://tempuri.org/JA_select";

            // 指定WebService的命名空间和调用的方法名
            SoapObject rpc = new SoapObject(nameSpace, methodName);

            // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
            //rpc.addProperty("FSql", "select * from z_image a inner join t_icitem b on a.fnumber=b.fnumber where b.fname like '%"+name+"%'");
            Log.i("sss", name + "qqq");
            rpc.addProperty("FSql", "select b.fname,b.fnumber ,b.fmodel ,a.fimage  from z_image a inner join t_icitem b on a.fnumber=b.fnumber where b.fname = '" + name + "'  ");
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
                        WLEntity entity = new WLEntity();
                        Element recordEle = (Element) iter.next();
                        // 拿到head节点下的子节点title值
                        String fnumber = recordEle.elementTextTrim("fnumber");

                        String fimage = recordEle.elementTextTrim("fimage");
                        Log.i("qqq", fimage);

                        System.out.println("title:" + fimage);

                        entity.setFnumber(fnumber);

                        entity.setImg(fimage);
                        entity.setBitmap(base64ToBitmap(fimage));
                        entity.setFname(getPhotoFileName());
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
