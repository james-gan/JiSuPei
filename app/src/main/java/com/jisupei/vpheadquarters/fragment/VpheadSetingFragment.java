package com.jisupei.vpheadquarters.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jisupei.R;
import com.jisupei.activity.datail.wiget.MyDatailPagerSlidingTabStrip;
import com.jisupei.activity.datail.wiget.MyViewPager;
import com.jisupei.vpheadquarters.seting.VpheadAlreadyShelvesFragment;
import com.jisupei.vpheadquarters.seting.VpheadSoldAlreadyFragment;
import com.jisupei.ywy.BaseFragment;
import com.jisupei.vpheadquarters.seting.VpHeadProductSearchActivity;
import com.jisupei.vpheadquarters.seting.VpheadSellFragmentNew;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/9/18.
 */
public class VpheadSetingFragment extends BaseFragment {

//
//    @InjectView(R.id.back_bt)
//    ImageView backBt;
    @InjectView(R.id.textView)
    TextView textView;

    @InjectView(R.id.top_layout)
    RelativeLayout topLayout;


    // 导航
    @InjectView(R.id.id_stickynavlayout_indicator)
    MyDatailPagerSlidingTabStrip id_stickynavlayout_indicator;


    @InjectView(R.id.id_stickynavlayout_viewpager)
    MyViewPager id_stickynavlayout_viewpager;
    @InjectView(R.id.serach)
    LinearLayout serach;

    //


    public static VpheadSetingFragment getVpSetingFragmentInstance() {
        VpheadSetingFragment instance = new VpheadSetingFragment();
        return instance;
    }
     public void setingDefault(){

     }

    @Override
    public View initView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.vphead_seting_fragment_new, null);
        ButterKnife.inject(this, rootView);

        serach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  i= new Intent(getActivity(),VpHeadProductSearchActivity.class);
                startActivity(i);
            }
        });
//        findViewIdHead(rootView);
//        include_title_tv.setText("订单");
//        AutoUtils.auto(rootView);
//        name.setText("账号:"+HttpBase.account_2);
//        panpay_name.setText("所属公司:"+HttpBase.companyName_2);
//        username.setText(HttpBase.realName_2);
//        phone.setText(HttpBase.phone_2);
//        exit_ll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HttpBase.Exit(    getActivity());
//                //启动登录页面,并清除所有栈内的activity
//                Intent intent = new Intent();
//                intent.setClass(    getActivity(),
//                        LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                MyApplication.instance.exit();
//                getActivity().finish();
//            }
//        });

        return rootView;
    }




    String orderCode;
    String payStatus;
    String sub_status;

    @Override
    public void initData() {





//        orderCode=  getIntent().getStringExtra("orderCode");
//        payStatus=  getIntent().getStringExtra("payStatus");
//        sub_status=  getIntent().getStringExtra("sub_status");
//        String   flag=  getIntent().getStringExtra("flag");
//
        List<Fragment> fragmentLists = new ArrayList<Fragment>();


        //出售中
        VpheadSellFragmentNew mChildOrderFragment1 = new VpheadSellFragmentNew();

//        mChildOrderFragment1.orderCode=orderCode;
//        mChildOrderFragment1.payStatus=payStatus;
//        mChildOrderFragment1.sub_status=sub_status;
        //已销新
        VpheadSoldAlreadyFragment mChildOrderFragment2 = new VpheadSoldAlreadyFragment();
//        mChildOrderFragment2.orderCode=orderCode;
        // 回单
        VpheadAlreadyShelvesFragment mChildOrderFragment3 = new VpheadAlreadyShelvesFragment();
        fragmentLists.add(mChildOrderFragment1);
        fragmentLists.add(mChildOrderFragment2);
        fragmentLists.add(mChildOrderFragment3);
//        fragmentLists.add(mChildOrderFragment4);
        OrderSetingsAdapter   pagerAdapter = new OrderSetingsAdapter(getChildFragmentManager(),
                fragmentLists);
        id_stickynavlayout_viewpager.setAdapter(pagerAdapter);
        id_stickynavlayout_indicator.setViewPager(id_stickynavlayout_viewpager);

//        if("2".equals(flag)){
//            id_stickynavlayout_viewpager.setCurrentItem(1);
//        }
//        if("3".equals(flag)){
//            id_stickynavlayout_viewpager.setCurrentItem(2);
//        }
//        if("4".equals(flag)){
//            id_stickynavlayout_viewpager.setCurrentItem(3);
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }

    //订单的详情
    public class OrderSetingsAdapter extends FragmentPagerAdapter {
        //        private final String[] titles = { "详情", "物流","回单","异常"};
        private final String[] titles = { "出售中", "已告罄","已下架"};
        private List<Fragment> fragmentLists;
        @Override
        public CharSequence getPageTitle(int position) {
            // TODO Auto-generated method stub
            return titles[position];
        }
        public OrderSetingsAdapter(FragmentManager fm,
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
