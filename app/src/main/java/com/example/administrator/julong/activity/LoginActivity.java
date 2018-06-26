package com.example.administrator.julong.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.julong.R;
import com.example.administrator.julong.util.UTIL;
import com.example.administrator.julong.view.CustomProgress;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class LoginActivity extends AppCompatActivity {
    private TextView mBtnLogin;
    private View progress;
    private View mInputLayout;
    private EditText mName, mPsw;
    private String name = "", pass = "";
    private CustomProgress dialog;
    private SharedPreferences sp;
    private CheckBox mima;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (EditText) findViewById(R.id.etname);
        mPsw = (EditText) findViewById(R.id.etpd);
        mima = (CheckBox) findViewById(R.id.cb_mima);

        //判断记住密码多选框的状态
        if(sp.getBoolean("ISCHECK", false))
        {
            //设置默认是记录密码状态
            mima.setChecked(true);
            mName.setText(sp.getString("USER_NAME", ""));
            mPsw.setText(sp.getString("PASSWORD", ""));
        }

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mName.getText().toString();
                pass = mPsw.getText().toString();
                UTIL.FNAME = name;
                UpdateTextTask updateTextTask = new UpdateTextTask();
                updateTextTask.execute();
            }
        });

        mima.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mima.isChecked()) {
                    System.out.println("记住密码已选中<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                    sp.edit().putBoolean("ISCHECK", true).commit();
                }else {
                    System.out.println("记住密码没有选中<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                    sp.edit().putBoolean("ISCHECK", false).commit();
                }
            }
        });
    }


    class UpdateTextTask extends AsyncTask<Void, Integer, Integer> {


        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {

            dialog=CustomProgress.show(LoginActivity.this,"登录中...", true, null);
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {
            // 命名空间
            String nameSpace = "http://tempuri.org/";
            // 调用的方法名称
            String methodName = "Login";
            // EndPoint
            String endPoint = UTIL.ENDPOINT;
            // SOAP Action
            String soapAction = "http://tempuri.org/Login";

            // 指定WebService的命名空间和调用的方法名
            SoapObject rpc = new SoapObject(nameSpace, methodName);

            // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
            rpc.addProperty("UserName", name);
            rpc.addProperty("PassWord", pass);

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
                if (result.equals("成功")) {
                    return 1;
                } else {
                    return 2;
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
            if (integer == 1) {
                if(mima.isChecked())
                {
                    //记住用户名、密码、
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("USER_NAME", name);
                    editor.putString("PASSWORD",pass);
                    editor.commit();
                }
                Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "账号密码错误", Toast.LENGTH_LONG).show();
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
