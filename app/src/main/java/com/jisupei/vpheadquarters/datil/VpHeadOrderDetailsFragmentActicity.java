package com.jisupei.vpheadquarters.datil;

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
import com.jisupei.ywy.order.fragment.SalesmanReceiptOrderFragment;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/7/12.
 */
public class VpHeadOrderDetailsFragmentActicity extends AppCompatActivity {

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
        setContentView(R.layout.fragmentactivity_datail_new);
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
    String sub_status;
    public void  loadData() {
        orderCode=  getIntent().getStringExtra("orderCode");
        payStatus=  getIntent().getStringExtra("payStatus");
        sub_status=  getIntent().getStringExtra("sub_status");

     String   flag=  getIntent().getStringExtra("flag");

        List<Fragment> fragmentLists = new ArrayList<Fragment>();
        //订单详情
        VpHeadOrderDetailFragment mChildOrderFragment1 = new VpHeadOrderDetailFragment();

        mChildOrderFragment1.orderCode=orderCode;
        mChildOrderFragment1.payStatus=payStatus;
        mChildOrderFragment1.sub_status=sub_status;
         //物流
        VpHeadLogisticsOrderFragment mChildOrderFragment2 = new VpHeadLogisticsOrderFragment();
        mChildOrderFragment2.orderCode=orderCode;
        // 回单
        SalesmanReceiptOrderFragment mChildOrderFragment3 = new SalesmanReceiptOrderFragment();
        mChildOrderFragment3.orderCode=orderCode;
        // 异常
        fragmentLists.add(mChildOrderFragment1);
        fragmentLists.add(mChildOrderFragment2);
        fragmentLists.add(mChildOrderFragment3);
        OrderDetailsAdapter   pagerAdapter = new OrderDetailsAdapter(getSupportFragmentManager(),
                fragmentLists);
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
    @Override
    protected void onResume() {
        super.onResume();
    }
    //订单的详情
    public class OrderDetailsAdapter extends FragmentPagerAdapter {
//        private final String[] titles = { "详情", "物流","回单","异常"};
        private final String[] titles = { "详情", "物流","回单"};
        private List<Fragment> fragmentLists;
        @Override
        public CharSequence getPageTitle(int position) {
            // TODO Auto-generated method stub
            return titles[position];
        }
        public OrderDetailsAdapter(FragmentManager fm,
                                 List<Fragment> fragmentLists) {
            super(fm);
            this.fragmentLists = fragmentLists;
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
