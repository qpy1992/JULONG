package com.example.administrator.julong.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.julong.R;
import com.example.administrator.julong.adapter.GridviewApater1;
import com.example.administrator.julong.entity.MainMenuEntity;
import com.example.administrator.julong.util.UTIL;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    private GridView gv;
    private long exitTime = 0;

    private int[] resArr = new int[]{R.drawable.order, R.drawable.wuliao,R.drawable.orderlist,R.drawable.wlgl,R.drawable.supplier,R.drawable.ckkd
    ,R.drawable.ck,R.drawable.baobiao,0
    };

    private String[] textArr = new String[]{"申请开单", "物料图片","已开单据","物料维护","供应商维护","出库开单","出库单详情","报表查询",""
    };
    private List<MainMenuEntity> dadaList;
    private GridviewApater1 adapter;
//    private RollPagerView mRollViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        dadaList = new ArrayList<MainMenuEntity>();
        for (int i = 0; i < resArr.length; i++) {
            MainMenuEntity data = new MainMenuEntity();
            data.setResId(resArr[i]);
            data.setText(textArr[i]);
            dadaList.add(data);
        }
        gv = (GridView) findViewById(R.id.gv);
        adapter = new GridviewApater1(this, dadaList);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        startActivity(new Intent(Main2Activity.this, KDActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(Main2Activity.this, MainActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(Main2Activity.this, BiaotouActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(Main2Activity.this, XinzenwuliaoActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(Main2Activity.this, GYSActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(Main2Activity.this, ChukuActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(Main2Activity.this, OuterActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(Main2Activity.this, ReportActivity.class));
//                    case 8:
//                        startActivity(new Intent(Main2Activity.this, TreeActivity.class));
                }
            }
        });
//        mRollViewPager = (RollPagerView) findViewById(R.id.roll_view_pager);

        //设置播放时间间隔
//        mRollViewPager.setPlayDelay(3000);
        //设置透明度
//        mRollViewPager.setAnimationDurtion(500);
        //设置适配器
//        mRollViewPager.setAdapter(new TestNormalAdapter());

        //设置指示器（顺序依次）
        //自定义指示器图片
        //设置圆点指示器颜色
        //设置文字指示器
        //隐藏指示器
        //mRollViewPager.setHintView(new IconHintView(this, R.drawable.point_focus, R.drawable.point_normal));
//        mRollViewPager.setHintView(new ColorPointHintView(this, Color.YELLOW, Color.WHITE));
        //mRollViewPager.setHintView(new TextHintView(this));
        //mRollViewPager.setHintView(null);
    }
    private class TestNormalAdapter extends StaticPagerAdapter {
        private int[] imgs = {
                R.drawable.aaa,
                R.drawable.head1,
                R.drawable.head2,
        };


        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }


        @Override
        public int getCount() {
            return imgs.length;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出应用",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
