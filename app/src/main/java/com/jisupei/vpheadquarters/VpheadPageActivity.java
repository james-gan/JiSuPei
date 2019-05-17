package com.jisupei.vpheadquarters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jisupei.R;
import com.jisupei.application.MyApplication;
import com.jisupei.ywy.BaseActivity;
import com.jisupei.ywy.widget.NoScrollViewPager;
import com.jisupei.utils.update.AppUpdate;
import com.jisupei.utils.StatusBarUtil;
import com.jisupei.vpheadquarters.fragment.VpheadCustomerFragment;
import com.jisupei.vpheadquarters.fragment.VpheadHomePageFragment;
import com.jisupei.vpheadquarters.fragment.VpheadOrderFragmentNew;
import com.jisupei.vpheadquarters.fragment.VpheadSetingFragment;
import com.jisupei.utils.widget.ToasAlert;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/9/18.
 */
public class VpheadPageActivity extends BaseActivity {
    @InjectView(R.id.main_viewpage)
 public   NoScrollViewPager mainViewpage;
    @InjectView(R.id.div_view)
    View divView;
//    @InjectView(R.id.home_page_rb)
//    public   RadioButton homePageRb;


    @InjectView(R.id.seting_rb)
    public   RadioButton seting_rb;
    @InjectView(R.id.order_rb)
    public   RadioButton orderRb;
    @InjectView(R.id.index_rb)
    public   RadioButton index_rb;



//    @InjectView(R.id.product_rb)
//    RadioButton productRb;
    @InjectView(R.id.main_radiogroup)
    RadioGroup mainRadiogroup;
    //刷新
    public static  boolean  shRef = true;
    @Override
    public void setLayoutContentView(Bundle savedInstanceState) {

        setContentView(R.layout.vphead_head_main);
        ButterKnife.inject(this);
        //setStatucolor();
//        int a=1/0;
        StatusBarUtil.setStatusBarColor(VpheadPageActivity.this,R.color.white);
        int a2=  StatusBarUtil.StatusBarLightMode(VpheadPageActivity.this);
        if(a2==0){
            StatusBarUtil.setStatusBarColor(VpheadPageActivity.this,R.color.black);
        }
        shRef=true;
        EventBus.getDefault().register(this);

        //判断更新信息
        new AppUpdate(this).getVersion();
    }


    public void onEventMainThread(String  refresh) {

            if("fromHomeDaiShenHeResh".equals(refresh)||("fromHomeDaiPeiSongResh".equals(refresh))){
                mainViewpage.setCurrentItem(2,false);
//                homePageRb.setTextColor(getResources() .getColor(R.color.light_black));
                orderRb.setTextColor(getResources()
                        .getColor(R.color.yellow1));
                orderRb.setChecked(true);

//                if(mOrderFragment3!=null){
//                    mOrderFragment3.   setingDefault();
//                }
//                shRef=true;
//                StatusBarUtil.setStatusBarColor(HomePageActivity.this,R.color.white);
//                int a2=  StatusBarUtil.StatusBarLightMode(HomePageActivity.this);
//                if(a2==0){
//                    StatusBarUtil.setStatusBarColor(HomePageActivity.this,R.color.black);
//                }
            }



    }
    @Override
    public void initData() {

        initPager();
        index_rb.setTextColor(getResources().getColor(R.color.yellow1));



        mainRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                /** 改变文字的颜色 */
                int length = group.getChildCount();
                for (int i = 0; i < length; i++) {
                    RadioButton radioButton = (RadioButton) group
                            .getChildAt(i);
                    if (radioButton.getId() != checkedId) {
                        // 没有选中的 时候的文字颜色
                        radioButton.setTextColor(getResources()
                                .getColor(R.color.light_black));
                    } else { // 选中的时候的文字颜色
                        radioButton.setTextColor(getResources()
                                .getColor(R.color.yellow1));
                    }
                }
                switch (checkedId) {
                    case R.id.index_rb: //首页
                        mainViewpage.setCurrentItem(0,false);

                        shRef=true;
                        StatusBarUtil.setStatusBarColor(VpheadPageActivity.this,R.color.white);
                        int a2=  StatusBarUtil.StatusBarLightMode(VpheadPageActivity.this);
                        if(a2==0){
                            StatusBarUtil.setStatusBarColor(VpheadPageActivity.this,R.color.black);
                        }
                        break;

                    case R.id.kehu_rb:  //客户
                        mainViewpage.setCurrentItem(1,false);

                        shRef=true;
                        StatusBarUtil.setStatusBarColor(VpheadPageActivity.this,R.color.white);
                        int a3=  StatusBarUtil.StatusBarLightMode(VpheadPageActivity.this);
                        if(a3==0){
                            StatusBarUtil.setStatusBarColor(VpheadPageActivity.this,R.color.black);
                        }
                        break;


                    case R.id.order_rb: //订单
                        mainViewpage.setCurrentItem(2,false);

                        shRef=true;
                        StatusBarUtil.setStatusBarColor(VpheadPageActivity.this,R.color.white);
                        int a4=  StatusBarUtil.StatusBarLightMode(VpheadPageActivity.this);
                        if(a4==0){
                            StatusBarUtil.setStatusBarColor(VpheadPageActivity.this,R.color.black);
                        }
                        break;
                    case R.id.seting_rb:
                        mainViewpage.setCurrentItem(3,false);
                        StatusBarUtil.setStatusBarColor(VpheadPageActivity.this,R.color.white);
                        int a1=  StatusBarUtil.StatusBarLightMode(VpheadPageActivity.this); //黑色文字
                        if(a1==0){
                            StatusBarUtil.setStatusBarColor(VpheadPageActivity.this,R.color.black);
                        }
                        break;
//                    case R.id.product_rb:
//                        mainViewpage.setCurrentItem(3,false);
//                        break;

                }
            }
        });


    }
    List<Fragment> fragmentsList;
 public VpheadOrderFragmentNew mOrderFragment3=null;
    private void initPager() {
        mainViewpage.setOffscreenPageLimit(4);
       fragmentsList= new ArrayList<Fragment>();


        //首页
        VpheadHomePageFragment    mVpheadHomePageFragment   =  VpheadHomePageFragment.getHomePageFragmentInstance();

        //客户
        VpheadCustomerFragment   mVpheadCustomerFragment   =  VpheadCustomerFragment.getCustomerFragmentInstance();
        //订单
        mOrderFragment3   =  VpheadOrderFragmentNew.getOrderFragmentInstance();
        //订单
        mOrderFragment3   =  VpheadOrderFragmentNew.getOrderFragmentInstance();
     //设置
        VpheadSetingFragment mVpSetingFragment   =  VpheadSetingFragment.getVpSetingFragmentInstance();


        fragmentsList.add(mVpheadHomePageFragment);
        fragmentsList.add(mVpheadCustomerFragment);
        fragmentsList.add(mOrderFragment3);
        fragmentsList.add(mVpSetingFragment);


        FragmentPagerAdapter  mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragmentsList.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragmentsList.get(arg0);
            }
        };
        mainViewpage.setAdapter(mAdapter);
    }

   long exitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
//				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                ToasAlert.toastColor("再按一次退出应用");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                MyApplication.instance.exit();

                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                System.gc();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
