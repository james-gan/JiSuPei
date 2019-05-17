package com.jisupei.vpnew.index;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableRecyclerView;
import com.jisupei.activity.base.LoginActivity;
import com.jisupei.activity.base.MessageActivity;
import com.jisupei.R;
import com.jisupei.ywy.BaseFragment;
import com.jisupei.http.HttpBase;
import com.jisupei.http.HttpUtil;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.AutoUtils;
import com.jisupei.vpnew.VpHomePageActivity;
import com.jisupei.vpnew.index.activity.VpNewCouponsCenterActivity;
import com.jisupei.vpnew.index.activity.VpNewProductMoreActivity;
import com.jisupei.vpnew.index.activity.VpNewProductSearchActivity;
import com.jisupei.vpnew.index.adapter.VpIndexAdapter1;
import com.jisupei.vpnew.index.model.CartItem;
import com.jisupei.vpnew.index.model.CategoryData;
import com.jisupei.vpnew.index.model.IndexData;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/18.
 */
public class VpNewIndexFragment extends BaseFragment {
     @InjectView(R.id.pullToRefreshLayout)
     PullToRefreshLayout pullToRefreshLayout;
     @InjectView(R.id.tuijian_title)
     TextView tuijian_title;
    @InjectView(R.id.tuijian_more)
    TextView tuijian_more;
    @InjectView(R.id.tj_more)
    TextView tj_more;
    @InjectView(R.id.auto_view_pager)
    AutoScrollViewPager auto_view_pager;
    @InjectView(R.id.id_stickynavlayout_innerscrollview)
    PullableRecyclerView id_stickynavlayout_innerscrollview;
    @InjectView(R.id.id_stickynavlayout_innerscrollview2)
    PullableRecyclerView id_stickynavlayout_innerscrollview2;

    @InjectView(R.id.category_list)
    RecyclerView category_list;
    @InjectView(R.id.cart_list)
    PullableRecyclerView cart_list;
    @InjectView(R.id.cart_list_ll)
    LinearLayout cart_list_ll;
    @InjectView(R.id.xiaoxi2)
    ImageView xiaoxi2;
    @InjectView(R.id.xiaoxi2_ll)
    LinearLayout xiaoxi2_ll;
    View rootView;
    @InjectView(R.id.search)
    LinearLayout search;
    public static VpNewIndexFragment getVpNewIndexFragmentInstance() {
        VpNewIndexFragment instance = new VpNewIndexFragment();
        return instance;
    }
     public void setingDefault(){
     }
    @Override
    public View initView(LayoutInflater inflater) {
          rootView = inflater.inflate(R.layout.vpnew_index_fragment, null);
        ButterKnife.inject(this, rootView);
        //监听的方法
        pullToRefreshLayout.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                loadIndexData(pullToRefreshLayout);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            }
        });
        pullToRefreshLayout.setPullUpEnable(false);
        AutoUtils.auto(pullToRefreshLayout.defaultRefreshView);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(),VpNewProductSearchActivity.class);
                getActivity().startActivity(intent);
            }
        });
        xiaoxi2_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!HttpBase.islogin){
                    Intent intent =new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                startActivity(new Intent(getActivity(), MessageActivity.class));
            }
        });
        tuijian_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(),VpNewProductMoreActivity.class);
                intent.putExtra("flagmore","0");
                getActivity().startActivity(intent);
            }
        });
        tj_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(),VpNewProductMoreActivity.class);
                intent.putExtra("flagmore","1");
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void initData() {

        loadIndexData(null);
        setBannerImages(null);
        setCategory();//加载分类
        setCart();//加载优惠卷
        getMessageCount();

    }
    public void   setCategory(){
        AppLoading.show(getActivity());
        HttpUtil.getInstance().selClass(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Logger.e("TAG", e);
                AppLoading.close();
                ToasAlert.toast("连接服务器加载分类失败");

            }
            @Override
            public void onResponse(String response, int id) {
                AppLoading.close();
                Logger.e("TAG", response);
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String optFlag = object.optString("optFlag");
                JSONObject res = object.optJSONObject("res");
                if ( "yes".equals(optFlag)) {

                    final List<CategoryData> mCategoryDataListTemp = new Gson().fromJson(res.optString("list").toString(), new TypeToken<ArrayList<CategoryData>>() {
                    }.getType());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    category_list.setLayoutManager(linearLayoutManager);
                    CategoryAdapter adapter = new CategoryAdapter(getActivity(),mCategoryDataListTemp);
                    category_list.setAdapter(adapter);
                }else {
                    ToasAlert.toast("暂时没有数据!");
                }
            }
        });

    }
    public void   setCart(){
        AppLoading.show(getActivity());
        HttpUtil.getInstance().getAllCouponApp(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Logger.e("TAG", e);
                AppLoading.close();
                ToasAlert.toast("连接服务器加载分类失败");
            }
            @Override
            public void onResponse(String response, int id) {

                AppLoading.close();

                Logger.e("TAG", response);
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String optFlag = object.optString("optFlag");
                String res = object.optString("res");
                if ( "yes".equals(optFlag)) {

                    final List<CartItem> mCartItemListTemp = new Gson().fromJson(res, new TypeToken<ArrayList<CartItem>>() {
                    }.getType());

                  if(mCartItemListTemp==null||mCartItemListTemp.size()<1){
                      cart_list.setVisibility(View.GONE);
                      cart_list_ll.setVisibility(View.GONE);
                      return;
                  }
                    cart_list.setVisibility(View.VISIBLE);
                    cart_list_ll.setVisibility(View.VISIBLE);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    cart_list.setLayoutManager(linearLayoutManager);
                    CartAdapter adapter = new CartAdapter(getActivity(),mCartItemListTemp);
                    cart_list.setAdapter(adapter);
                }else {
                    ToasAlert.toast("暂时没有数据!");
                }
            }

        });


    }
    public void loadIndexData(final PullToRefreshLayout mPullToRefreshLayout) {
        AppLoading.show(getActivity());
        HttpUtil.getInstance().productIndex(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Logger.e("TAG", e);
                AppLoading.close();
                ToasAlert.toast("连接服务器接口失败");
                 if(mPullToRefreshLayout!=null){
                     mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                 }

            }

            @Override
            public void onResponse(String response, int id) {
                if(mPullToRefreshLayout!=null){
                    mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }

                AppLoading.close();

                Logger.e("TAG", "loadIndexData");
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String optFlag = object.optString("optFlag");
                String res = object.optString("res");
                 if ( "yes".equals(optFlag)) {
                     IndexData mIndexData=  new Gson().fromJson(res,IndexData.class);
                     mIndexData.groupList.clear();
                     mIndexData.groupList.add(mIndexData.discountList);
                     mIndexData.groupList.add(mIndexData.hotList);

                     VpIndexAdapter1 mNoLoginHomeAdapter1=new VpIndexAdapter1(getActivity(),mIndexData.hotList);
                     GridLayoutManager mGridLayoutManager1=new GridLayoutManager(getActivity(),3);
                     id_stickynavlayout_innerscrollview.setHasFixedSize(true);
                     id_stickynavlayout_innerscrollview.setNestedScrollingEnabled(false);
                     id_stickynavlayout_innerscrollview.setLayoutManager(mGridLayoutManager1);
                     id_stickynavlayout_innerscrollview.setAdapter(mNoLoginHomeAdapter1);
                     if(mIndexData.discountList==null||mIndexData.discountList.size()==0){
                         tuijian_title.setVisibility(View.GONE);
                         tuijian_more.setVisibility(View.GONE);
                     }
//2
                     GridLayoutManager mGridLayoutManager2=new GridLayoutManager(getActivity(),3);
                     VpIndexAdapter1 mNoLoginHomeAdapter2=new VpIndexAdapter1(getActivity(),mIndexData.discountList);
                     id_stickynavlayout_innerscrollview2.setHasFixedSize(true);
                     id_stickynavlayout_innerscrollview2.setNestedScrollingEnabled(false);
                     id_stickynavlayout_innerscrollview2.setLayoutManager(mGridLayoutManager2);
                     id_stickynavlayout_innerscrollview2.setAdapter(mNoLoginHomeAdapter2);

                 }else {
                     ToasAlert.toast("暂时没有数据!");
                 }

            }

        });


    }

    public void   getMessageCount(){
        if(!HttpBase.islogin){

            return;
        }
        AppLoading.show(getActivity());
        HttpUtil.getInstance().messageCount(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Logger.e("TAG", e);
                AppLoading.close();
                ToasAlert.toast("连接服务器失败");

            }

            @Override
            public void onResponse(String response, int id) {

                AppLoading.close();

                Logger.e("TAG", response);
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String optFlag = object.optString("optFlag");
                String res = object.optString("res");
                if ( "yes".equals(optFlag)) {
                     if(!"0".equals(res)){
                         xiaoxi2.setImageResource(R.mipmap.my_message_point);
                     }else {
                         xiaoxi2.setImageResource(R.mipmap.my_message);
                     }

                }else {
                    ToasAlert.toast("暂时没有数据!");
                }

            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //开启自动轮播，延时时间为getInterval()

        auto_view_pager.startAutoScroll();
        //开启自动轮播，并设置延时
//        auto_view_pager.startAutoScroll(delayTime);
    }
    @Override
    public void onPause() {
        super.onPause();
        //停止轮播
        auto_view_pager.stopAutoScroll();
    }

    private void setBannerImages(List<String> imageList) {

        if(imageList==null){
            imageList= new ArrayList<String>();

            imageList.add(HttpUtil.HOST_STRING+"/upload/adv/idx/top1.jpg");
            imageList.add(HttpUtil.HOST_STRING+"/upload/adv/idx/top2.jpg");
            imageList.add(HttpUtil.HOST_STRING+"/upload/adv/idx/top3.jpg");
        }
        if(imageList.size()==1){
            auto_view_pager.stopAutoScroll();
        }
        auto_view_pager.setAdapter(new MyAdapter(imageList));
//
        //设置延时时间
        auto_view_pager.setInterval(5000);
        //设置轮播的方向 AutoScrollViewPager.RIGHT/AutoScrollViewPager.LEFT
        auto_view_pager.setDirection(AutoScrollViewPager.RIGHT);
        //设置是否自动循环轮播，默认为true
        //注意:一旦设为true，则不能和ViewPagerIndicator一起使用
        auto_view_pager.setCycle(true);
        //设置切换动画的时长
        auto_view_pager.setScrollDurationFactor(5);
        //设置当滑动到最后一个或者第一个时，如何切换下一张
        /**
         * SLIDE_BORDER_MODE_NONE:不能再滑动
         * SLIDE_BORDER_MODE_TO_PARENT:移动父视图的Pager m
         * SLIDE_BORDER_MODE_CYCLE:循环
         * 默认为SLIDE_BORDER_MODE_NONE
         */
////        auto_view_pager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);
        //当滑动到最后一张或第一张时是否开启动画，默认为true
        auto_view_pager.setBorderAnimation(true);
        //当触摸的时候，停止轮播
        auto_view_pager.setStopScrollWhenTouch(true);

//
//        //解决最后一个跳转到第一个闪动问题
//         auto_view_pager.setCurrentItem((Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % imageList.size()));

        auto_view_pager.setCurrentItem(imageList.size()*10);

        final LinearLayout point_group = (LinearLayout) rootView.findViewById(R.id.point_group);
        point_group.removeAllViews();
        for (int i = 0; i < imageList.size(); i++) {
            // 添加指示点
            ImageView point = new ImageView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = AppUtils.dp2px(getActivity(),10);

            point.setLayoutParams(params);
            point.setBackgroundResource(R.drawable.point_bg);
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
            }
            point_group.addView(point);

        }

//
        final List<String> finalImageList = imageList;
        auto_view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                // Logger.e("TAG", "position" + position);
                // 改变指示点的状态
                // 把当前点enbale 为true
                try {
                    point_group.getChildAt(position % finalImageList.size()).setEnabled(true);
                    // 把上一个点设为false
                    point_group.getChildAt(lastPosition).setEnabled(false);
                    lastPosition = position % finalImageList.size();
                } catch (Exception e) {
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    int lastPosition;



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }

//轮播图的adapter
    class MyAdapter extends PagerAdapter {

        List<String> mSimpleDraweeViewList;
        public MyAdapter(List<String> mSimpleDraweeViewList) {
            this.mSimpleDraweeViewList=mSimpleDraweeViewList;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView draweeView= new ImageView(getActivity());
            draweeView.setScaleType( ImageView.ScaleType.FIT_XY);
            Picasso.with(getActivity()).load(mSimpleDraweeViewList.get(position % mSimpleDraweeViewList.size())).error(R.mipmap.error).memoryPolicy(MemoryPolicy.NO_CACHE).into(draweeView);
            ViewParent parent = draweeView.getParent();
            // remove掉View之前已经加到一个父控件中，否则报异常
            if (parent != null) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(draweeView);
            }
            container.addView(draweeView);
            return draweeView;

        }


    }

//分类的Adapter
    public class CategoryAdapter extends  RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
        Context context;
        String unit;
        public List<CategoryData> mCategoryDataListTemp;
        public CategoryAdapter(Context context ,List<CategoryData> mCategoryDataListTemp ) {
            this.context=context;
            this.mCategoryDataListTemp=mCategoryDataListTemp;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=   LayoutInflater.from(context).inflate(R.layout.newvp_adapter_category, null, false);
            AutoUtils.auto(view);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
           final CategoryData mCategoryData  =  mCategoryDataListTemp.get(position);
             holder.category_name.setText( mCategoryData.name);
            Picasso.with(context).load(HttpUtil.HOST_STRING +"/"+ mCategoryData.icon).error(R.mipmap.error).resize(100,100).into(holder.category_img);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VpHomePageActivity mVpHomePageActivity=  (VpHomePageActivity)context;
                    mVpHomePageActivity.category_rb.setChecked(true);
//                    mVpHomePageActivity.mainViewpage.setCurrentItem(1,false);
                    mVpHomePageActivity.mVpcategoryFragment.selectCurrLeft(position);


                }
            });
        }
        @Override
        public int getItemCount() {
            return  mCategoryDataListTemp.size();
        }
        class MyViewHolder  extends RecyclerView.ViewHolder{
            @InjectView(R.id.category_img)
            ImageView category_img;
            @InjectView(R.id.category_name)
            TextView category_name;

            public   MyViewHolder(View view) {
                super(view);
                ButterKnife.inject(this, view);
            }
        }



    }
    //优惠卷的的Adapter
    public class CartAdapter extends  RecyclerView.Adapter<CartAdapter.MyViewHolder> {
        Context context;
        public List<CartItem> mCartItemListTemp;

        public CartAdapter(Context context ,List<CartItem> mCartItemListTemp ) {
            this.context=context;
            this.mCartItemListTemp=mCartItemListTemp;

        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=   LayoutInflater.from(context).inflate(R.layout.vp_cart_list_adapter, null, false);
//            AutoUtils.auto(view);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final CartItem mCartItem  =  mCartItemListTemp.get(position);
            holder.cart_name.setText( mCartItem.couponName);


            if("0".equals(mCartItem.parentId)){
                holder.money_cart.setText("卡包套餐");
                holder.man_user.setText(mCartItem.totalCount+"张优惠券可使用");
            }else {
                holder.money_cart.setText("￥"+ mCartItem.couponValue);
                holder.man_user.setText("满"+ mCartItem.minMoney+"元可用");
            }
            if(position==mCartItemListTemp.size()-1){
                holder.content1.setVisibility(View.VISIBLE);
            }else {
                holder.content1.setVisibility(View.GONE);
            }
            if("1".equals(mCartItem.hasGotten)){ //已使用
                holder.img_rl.setBackgroundResource(R.mipmap.card_disabled);
                holder.lingqu.setVisibility(View.GONE);
                holder.has_get.setVisibility(View.VISIBLE);
            }else {
                holder.img_rl.setBackgroundResource(R.mipmap.card_active);
                holder.lingqu.setVisibility(View.VISIBLE);
                holder.has_get.setVisibility(View.GONE);
                 //判断是不是抢完
                if(mCartItem.gottenCount.equals(mCartItem.  totalNum)){
                    holder.lingqu.setText("已抢完");
                    holder.lingqu.setAlpha(0.3f);
                    holder.lingqu.setEnabled(false);
                }else {
                    holder.lingqu.setText("立即领取");
                    holder.lingqu.setAlpha(1.0f);
                    holder.lingqu.setEnabled(true);
                }


            }





            holder.content1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!HttpBase.islogin){
                        Intent intent =new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
//                        finish();
                        return;
                    }
                    Intent intent = new Intent(getActivity(),VpNewCouponsCenterActivity.class);
                    getActivity().startActivity(intent);

                }
            });
            holder.lingqu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!HttpBase.islogin){
                        Intent intent =new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
//                        finish();
                        return;
                    }
                    AppLoading.show(getActivity());
                    HttpUtil.getInstance().getAllCouponApp(mCartItem.parentId,mCartItem.ID, new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Logger.e("TAG", e);
                            AppLoading.close();
                            ToasAlert.toast("连接服务器加载分类失败");

                        }

                        @Override
                        public void onResponse(String response, int id) {

                            AppLoading.close();

                            Logger.e("TAG", response);
                            JSONObject object = null;
                            try {
                                object = new JSONObject(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String optFlag = object.optString("optFlag");

                            if ( "yes".equals(optFlag)) {
                                ToasAlert.toast("领取成功!");
                                setCart();

                            }else {
                                ToasAlert.toast(object.optString("optDesc"));
                            }

                        }

                    });
                }
            });
//            Picasso.with(context).load(HttpUtil.HOST_STRING +"/"+ mCategoryData.icon).error(R.mipmap.error).resize(100,100).into(holder.category_img);
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    VpHomePageActivity mVpHomePageActivity=  (VpHomePageActivity)context;
//                    mVpHomePageActivity.category_rb.setChecked(true);
////                    mVpHomePageActivity.mainViewpage.setCurrentItem(1,false);
//                    mVpHomePageActivity.mVpcategoryFragment.selectCurrLeft(position);
//
//
//                }
//            });
        }
        @Override
        public int getItemCount() {
            return  mCartItemListTemp.size();
        }
        class MyViewHolder  extends RecyclerView.ViewHolder{

            @InjectView(R.id.money_cart)
            TextView money_cart;
            @InjectView(R.id.lingqu)
            TextView lingqu;

            @InjectView(R.id.man_user)
            TextView man_user;
            @InjectView(R.id.cart_name)
            TextView cart_name;
            @InjectView(R.id.content1)
            RelativeLayout content1;
            @InjectView(R.id.img_rl)
            RelativeLayout img_rl;
            @InjectView(R.id.has_get)
            ImageView has_get;

            public   MyViewHolder(View view) {
                super(view);
                ButterKnife.inject(this, view);
            }
        }



    }
}
