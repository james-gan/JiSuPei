package com.jisupei.vpnew.setting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jisupei.R;
import com.jisupei.activity.datail.wiget.MyDatailPagerSlidingTabStrip;
import com.jisupei.activity.datail.wiget.MyViewPager;
import com.jisupei.http.HttpUtil;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.AutoUtils;
import com.jisupei.vpnew.setting.cart.VpNewHasUseFragment;
import com.jisupei.vpnew.setting.cart.VpNewNotUseFragment;
import com.jisupei.vpnew.setting.cart.VpNewOutDataFragment;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/7/12.
 */
public class NewCartAllFragmentActicity extends AppCompatActivity {



    @InjectView(R.id.back_bt)
    ImageView backBt;
    @InjectView(R.id.textView)
    TextView textView;

    @InjectView(R.id.top_layout)
    RelativeLayout topLayout;


    // 导航
    @InjectView(R.id.id_stickynavlayout_indicator)
    MyDatailPagerSlidingTabStrip id_stickynavlayout_indicator;


    @InjectView(R.id.id_stickynavlayout_viewpager)
    MyViewPager id_stickynavlayout_viewpager;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmentactivity_cart_new);
        AutoUtils.auto(this);
        ButterKnife.inject(this);
        AppUtils.expandViewTouchDelegate(backBt, 30, 30, 50, 50);
    //设置监听
        setListener();
        //加载数据
        loadData();
    }
    public void setListener() {
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    String orderCode;
    String payStatus;
    String price_type;
    public void  loadData() {
        orderCode=  getIntent().getStringExtra("orderCode");
        payStatus=  getIntent().getStringExtra("payStatus");
        price_type=  getIntent().getStringExtra("price_type");

     final String   flag=  getIntent().getStringExtra("flag");

        final List<Fragment> fragmentLists = new ArrayList<Fragment>();
        //订单未使用的
        VpNewNotUseFragment mVpNewNotUseFragment = new VpNewNotUseFragment();
        mVpNewNotUseFragment.orderCode=orderCode;
        mVpNewNotUseFragment.payStatus=payStatus;
         //使用的
        VpNewHasUseFragment mCVpNewHasUseFragment = new VpNewHasUseFragment();
        mCVpNewHasUseFragment.orderCode=orderCode;
//        mChildOrderFragment2.price_type=price_type;

        // 回单
        VpNewOutDataFragment mVpNewOutDataFragment = new VpNewOutDataFragment();
        mVpNewOutDataFragment.orderCode=orderCode;
        // 异常
//        ExceptionOrderFragment mChildOrderFragment4 = new ExceptionOrderFragment();
//        mChildOrderFragment4.orderCode=orderCode;
//        mChildOrderFragment4.price_type=price_type;

        fragmentLists.add(mVpNewNotUseFragment);
        fragmentLists.add(mCVpNewHasUseFragment);
        fragmentLists.add(mVpNewOutDataFragment);
//        fragmentLists.add(mChildOrderFragment4);




        AppLoading.show(this);
        HttpUtil.getInstance().getMyCouponApp(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppLoading.close();
                ToasAlert.toastCenter("连接服务器失败，请稍后再试！");

            }
            @Override
            public void onResponse(String response, int id) {

                Logger.d("app2.0",response);
                AppLoading.close();
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String optFlag = object.optString("optFlag");

                if("yes".equals(optFlag)) {
                    JSONArray list = object.optJSONObject("res").optJSONArray("couponStatusCount");

                    OrderDetailsAdapter   pagerAdapter = new OrderDetailsAdapter(getSupportFragmentManager(),
                            fragmentLists ,list);
                            id_stickynavlayout_viewpager.setAdapter(pagerAdapter);
                            id_stickynavlayout_indicator.setViewPager(id_stickynavlayout_viewpager);

                            if("2".equals(flag)){
                                id_stickynavlayout_viewpager.setCurrentItem(1);
                            }
                            if("3".equals(flag)){
                                id_stickynavlayout_viewpager.setCurrentItem(2);
                            }
                            if("4".equals(flag)){
                                id_stickynavlayout_viewpager.setCurrentItem(3);
                            }



                }

            }
        });








    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    //订单的详情
    public class OrderDetailsAdapter extends FragmentPagerAdapter {
        private final String[] titles = { "未使用", "已使用","回单","异常"};
        private List<Fragment> fragmentLists;
        private JSONArray list;
        @Override
        public CharSequence getPageTitle(int position) {
            // TODO Auto-generated method stub
            try {
                return ( (JSONObject)list.get(position)).optString("couponStatus")+"("+ ((JSONObject)list.get(position)).optString("totalCount")+")";
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        }
        public OrderDetailsAdapter(FragmentManager fm,
                                 List<Fragment> fragmentLists,JSONArray list) {
            super(fm);
            this.fragmentLists = fragmentLists;
            this.list = list;
        }
        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub
            return fragmentLists.get(position);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fragmentLists.size();
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
        }
    }
}
