package com.jisupei.vpnew;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.jisupei.activity.base.LoginActivity;
import com.jisupei.R;
import com.jisupei.activity.homepage2.MainActivity11;
import com.jisupei.application.MyApplication;
import com.jisupei.utils.db.DatabaseHelper;
import com.jisupei.ywy.BaseActivity;
import com.jisupei.ywy.widget.NoScrollViewPager;
import com.jisupei.http.HttpBase;
import com.jisupei.utils.update.AppUpdate;
import com.jisupei.utils.StatusBarUtil;
import com.jisupei.vpnew.cart.VpCartsFragment;
import com.jisupei.vpnew.category.VpcategoryFragment;
import com.jisupei.vpnew.db.VpNewProductDb;
import com.jisupei.vpnew.index.VpNewIndexFragment;
import com.jisupei.vpnew.setting.fragment.VpNewSetingFragment;
import com.jisupei.utils.widget.ToasAlert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/9/18.
 */
public class VpHomePageActivity extends BaseActivity {
    @InjectView(R.id.main_viewpage)
 public   NoScrollViewPager mainViewpage;
    @InjectView(R.id.div_view)
    View divView;
    @InjectView(R.id.cart_num)
    TextView cart_num;

    @InjectView(R.id.seting_rb)
    public   RadioButton seting_rb;
    @InjectView(R.id.index_rb)
    public   RadioButton index_rb;
    @InjectView(R.id.category_rb)
    public   RadioButton category_rb;
    @InjectView(R.id.cart_rb)
    public   RadioButton cart_rb;


    @InjectView(R.id.main_radiogroup)
    RadioGroup mainRadiogroup;
    //刷新
    public static  boolean  shRef = true;
    @Override
    public void setLayoutContentView(Bundle savedInstanceState) {

        setContentView(R.layout.newvp_head_main);
        ButterKnife.inject(this);
        MyApplication.instance.addActivity(this);
        StatusBarUtil.setStatusBarColor(VpHomePageActivity.this,R.color.blue_head);
        shRef=true;
        EventBus.getDefault().register(this);
        //判断更新信息
        new AppUpdate(this).getVersion();
        int count=  queryCountNum();
        cart_num.setText(count+"");

    }


    public void onEventMainThread(String  refresh) {
        if(MainActivity11.CARTREFRESH.equals(refresh)){
            int count=  queryCountNum();
            cart_num.setText(count+"");
        }else if("cartSelect".equals(refresh)){

            cart_rb.setChecked(true);
        }else if("to2".equals(refresh)){
            mainViewpage.setCurrentItem(1,false);
            category_rb.setChecked(true);

        }
    }
    //
    public   int queryCountNum(){
        try {
            Dao<VpNewProductDb,Integer> mVpNewProductDbDaoDao;  mVpNewProductDbDaoDao = DatabaseHelper.getHelper(VpHomePageActivity.this).getVpNewProductDbDaoDao();
            List<VpNewProductDb> mProductList  =mVpNewProductDbDaoDao.queryBuilder().query();
            if(mProductList!=null && mProductList.size()>0){
                return mProductList.size();
            }
            return 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }
    @Override
    public void initData() {

        initPager();
        index_rb.setTextColor(getResources().getColor(R.color.yellow1));

        mainRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.index_rb: //首页
                        mainViewpage.setCurrentItem(0,false);
                        StatusBarUtil.setStatusBarColor(VpHomePageActivity.this,R.color.blue_head);

                         // 没有选中的 时候的文字颜色
                        category_rb.setTextColor(getResources()  .getColor(R.color.light_black));
                        cart_rb.setTextColor(getResources()  .getColor(R.color.light_black));
                        seting_rb.setTextColor(getResources()  .getColor(R.color.light_black));
                        // 选中的 时候的文字颜色
                        index_rb.setTextColor(getResources().getColor(R.color.yellow1));
                        break;

                    case R.id.category_rb: //分类
                        mainViewpage.setCurrentItem(1,false);
                        shRef=true;
                        StatusBarUtil.setStatusBarColor(VpHomePageActivity.this,R.color.blue_head);
                        // 没有选中的 时候的文字颜色
                        index_rb  .setTextColor(getResources()  .getColor(R.color.light_black));
                        cart_rb.setTextColor(getResources()  .getColor(R.color.light_black));
                        seting_rb.setTextColor(getResources()  .getColor(R.color.light_black));
                        // 选中的 时候的文字颜色
                        category_rb .setTextColor(getResources().getColor(R.color.yellow1));
                        break;
                    case R.id.cart_rb: //购物车
                        if(!HttpBase.islogin){
                            Intent intent =new Intent(VpHomePageActivity.this, LoginActivity.class);
                            startActivity(intent);
                            if(cheacid!=-1){
                                RadioButton    mRadioButton = (RadioButton)findViewById(cheacid);
                                mRadioButton.setChecked(true);
                            }
                            return;
                        }
                        mainViewpage.setCurrentItem(2,false);
                        StatusBarUtil.setStatusBarColor(VpHomePageActivity.this,R.color.blue_head);

                        index_rb  .setTextColor(getResources()  .getColor(R.color.light_black));
                        category_rb.setTextColor(getResources()  .getColor(R.color.light_black));
                        seting_rb.setTextColor(getResources()  .getColor(R.color.light_black));
                        // 选中的 时候的文字颜色
                        cart_rb   .setTextColor(getResources().getColor(R.color.yellow1));
                        break;
                    case R.id.seting_rb://设置
                        if(!HttpBase.islogin){
                            Intent intent =new Intent(VpHomePageActivity.this, LoginActivity.class);
                            startActivity(intent);
                            seting_rb.setChecked(false);
                            if(cheacid!=-1){
                                RadioButton    mRadioButton = (RadioButton)findViewById(cheacid);
                                mRadioButton.setChecked(true);
                            }
                            return;
                        }
                        mainViewpage.setCurrentItem(3,false);
                        StatusBarUtil.setStatusBarColor(VpHomePageActivity.this,R.color.white);
                        int a11=  StatusBarUtil.StatusBarLightMode(VpHomePageActivity.this); //黑色文字
                        if(a11==0){
                            StatusBarUtil.setStatusBarColor(VpHomePageActivity.this,R.color.black);
                        }

                        index_rb  .setTextColor(getResources()  .getColor(R.color.light_black));
                        cart_rb.setTextColor(getResources()  .getColor(R.color.light_black));
                        category_rb.setTextColor(getResources()  .getColor(R.color.light_black));
                        // 选中的 时候的文字颜色
                        seting_rb   .setTextColor(getResources().getColor(R.color.yellow1));
                        break;

                }
                cheacid=checkedId;

            }
        });
        index_rb.setChecked(true);

    }
    int cheacid=R.id.index_rb;
    List<Fragment> fragmentsList;
    public  VpcategoryFragment mVpcategoryFragment;
    private void initPager() {
        mainViewpage.setOffscreenPageLimit(4);
       fragmentsList= new ArrayList<Fragment>();
        //首页
        VpNewIndexFragment    mVpNewIndexFragment   =  VpNewIndexFragment.getVpNewIndexFragmentInstance();
        //分类
         mVpcategoryFragment   =  VpcategoryFragment.getVpcategoryFragmentInstance();
        //购物车
        VpCartsFragment mVpCartsFragment =  VpCartsFragment.getVpVpNewCartFragmentInstance();
        //设置
        VpNewSetingFragment    mVpNewSetingFragment4   =  VpNewSetingFragment.getVpSetingFragmentInstance();
        fragmentsList.add(mVpNewIndexFragment);
        fragmentsList.add(mVpcategoryFragment);
        fragmentsList.add(mVpCartsFragment);
        fragmentsList.add(mVpNewSetingFragment4);

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

    @Override
    protected void onDestroy() {
        MyApplication.instance.removeActivity(this);
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
