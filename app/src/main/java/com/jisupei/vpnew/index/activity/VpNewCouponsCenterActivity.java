package com.jisupei.vpnew.index.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableRecyclerView;
import com.jisupei.R;
import com.jisupei.activity.homepage2.MainActivity11;
import com.jisupei.application.MyApplication;
import com.jisupei.ywy.BaseActivity;
import com.jisupei.http.HttpUtil;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.AutoUtils;
import com.jisupei.utils.StatusBarUtil;
import com.jisupei.vpnew.index.model.CartItem;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bingoogolapple.progressbar.BGAProgressBar;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/18.
 */
public class VpNewCouponsCenterActivity extends BaseActivity {

    @InjectView(R.id.back_bt)
    ImageView back_bt;

    @InjectView(R.id.id_stickynavlayout_innerscrollview)
    PullableRecyclerView id_stickynavlayout_innerscrollview;

    @InjectView(R.id.pullToRefreshLayout)
    public PullToRefreshLayout pullToRefreshLayout;
    //刷新
    public static  boolean  shRef = true;
    @Override
    public void setLayoutContentView(Bundle savedInstanceState) {
        setContentView(R.layout.newvp_coupons_center);
        ButterKnife.inject(this);
//        AutoUtils.auto(this);
        StatusBarUtil.setStatusBarColor(this,R.color.white);
        int a2=  StatusBarUtil.StatusBarLightMode(VpNewCouponsCenterActivity.this);
        if(a2==0){
            StatusBarUtil.setStatusBarColor(VpNewCouponsCenterActivity.this,R.color.black);
        }

        MyApplication.instance.addActivity(this);

        EventBus.getDefault().register(this);

        AppUtils.expandViewTouchDelegate(back_bt, 30, 30, 50, 50);
        back_bt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish();
             }
         });
        pullToRefreshLayout.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pageno=1;
                load(pullToRefreshLayout);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

                load(pullToRefreshLayout);
            }
        });
        pullToRefreshLayout.setPullUpEnable(false);
        AutoUtils.auto(pullToRefreshLayout.defaultRefreshView);

    }
    public void onEventMainThread(String refresh) {
        if(MainActivity11.CARTREFRESH.equals(refresh)){



        }

    }

    int pageno=1;
//    String classId="";
//    String searchText="";

    @Override
    public void initData() {
//        if(!TextUtils.isEmpty(searchText))
          load(null);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.instance.removeActivity(this);
    }
    public  void load(final PullToRefreshLayout pullToRefreshLayout){

        AppLoading.show(this);
        HttpUtil.getInstance().getAllCouponAppAll(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppLoading.close();
                ToasAlert.toastCenter("连接服务器失败，请稍后再试！");
                    if(pullToRefreshLayout!=null)
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
            @Override
            public void onResponse(String response, int id) {
                    if(pullToRefreshLayout!=null)
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                Logger.d("app2.0",response);
                AppLoading.close();
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String optFlag = object.optString("optFlag");
                String res = object.optString("res");
                if("yes".equals(optFlag)) {

                    final List<CartItem> mCartItemListTemp = new Gson().fromJson(res, new TypeToken<ArrayList<CartItem>>() {
                    }.getType());

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VpNewCouponsCenterActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    id_stickynavlayout_innerscrollview.setLayoutManager(linearLayoutManager);
                    CartAdapter adapter = new CartAdapter(VpNewCouponsCenterActivity.this,mCartItemListTemp);
                    id_stickynavlayout_innerscrollview.setAdapter(adapter);
                }

            }
        });
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
            View view=   LayoutInflater.from(context).inflate(R.layout.vp_cart_center_adapter, null, false);
//            AutoUtils.auto(view);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final CartItem mCartItem  =  mCartItemListTemp.get(position);
            holder.cart_name.setText( mCartItem.couponName);
            holder.money_cart.setText("￥"+ mCartItem.couponValue);
            holder.man_user.setText("满"+ mCartItem.minMoney+"元可用");

            holder.mBGAProgressBar.setProgress((int)(Double.parseDouble(mCartItem.gottenCount) / Double.parseDouble(mCartItem.  totalNum))*100);

            if("1".equals(mCartItem.hasGotten)){ //已使用
//                holder.img_rl.setBackgroundResource(R.mipmap.card_disabled);
                holder.lingqu.setVisibility(View.VISIBLE);
                holder.has_get.setVisibility(View.VISIBLE);
                holder.lingqu.setBackgroundResource(R.drawable.shape_order_blue_exception2);
                holder.lingqu.setText("去使用");
                holder.mBGAProgressBar.setVisibility(View.GONE);
            }else {
//                holder.img_rl.setBackgroundResource(R.mipmap.card_active);
                holder.lingqu.setVisibility(View.VISIBLE);
                holder.has_get.setVisibility(View.GONE);
                holder.lingqu.setBackgroundResource(R.drawable.shape_order_blue_exception);
                holder.lingqu.setText("立即领取");
                holder.mBGAProgressBar.setVisibility(View.VISIBLE);

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
            Picasso.with(context).load(HttpUtil.HOST_STRING +"/"+ mCartItem.couponIcon).error(R.mipmap.error).resize(AppUtils.dp2px(context,50),AppUtils.dp2px(context,50)).into(holder.img_cart);



            holder.lingqu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if("立即领取".equals(  holder.lingqu.getText().toString())){
                        AppLoading.show(VpNewCouponsCenterActivity.this);
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
                                        load(null);
                                    EventBus.getDefault().post("getCart");

                                }else {
                                    ToasAlert.toast("暂时没有数据!");
                                }

                            }

                        });

                    }else {//去使用
                        EventBus.getDefault().post("to2");
                        VpNewCouponsCenterActivity.this.finish();
                    }

                }
            });

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
//            @InjectView(R.id.content1)
//            RelativeLayout content1;
            @InjectView(R.id.img_rl)
            RelativeLayout img_rl;
            @InjectView(R.id.has_get)
            ImageView has_get;
            @InjectView(R.id.img_cart)
            ImageView img_cart;
            @InjectView(R.id.mBGAProgressBar)
            BGAProgressBar mBGAProgressBar;

            public   MyViewHolder(View view) {
                super(view);
                ButterKnife.inject(this, view);
            }
        }



    }

}
