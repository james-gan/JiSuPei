package com.jisupei.vpheadquarters.seting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import com.jisupei.R;
import com.jisupei.activity.homepage2.MainActivity11;
import com.jisupei.activity.order.bean.NemuItem;
import com.jisupei.application.MyApplication;
import com.jisupei.ywy.BaseActivity;
import com.jisupei.ywy.HomePageActivity;
import com.jisupei.http.HttpUtil;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.AutoUtils;
import com.jisupei.utils.StatusBarUtil;
import com.jisupei.vpheadquarters.bean.OrderVp;
import com.jisupei.vpheadquarters.seting.bean.VpGood;
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
import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/18.
 */
public class VpHeadProductSearchActivity extends BaseActivity {

    @InjectView(R.id.back_bt)
    ImageView back_bt;

    @InjectView(R.id.order_ListView)
    public PullableListView order_ListView;
    @InjectView(R.id.search_ed)
    EditText search_ed;
    @InjectView(R.id.sera)
    TextView sera;

    @InjectView(R.id.pullToRefreshLayout)
    public PullToRefreshLayout pullToRefreshLayout;
    //刷新
    public static  boolean  shRef = true;
    @Override
    public void setLayoutContentView(Bundle savedInstanceState) {
        setContentView(R.layout.vphead_product_serach);
        ButterKnife.inject(this);
//        AutoUtils.auto(this);
        StatusBarUtil.setStatusBarColor(this,R.color.blue_head);


        MyApplication.instance.addActivity(this);

        EventBus.getDefault().register(this);

        AppUtils.expandViewTouchDelegate(back_bt, 30, 30, 50, 50);
        back_bt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish();
             }
         });
        sera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageno=1;

                lodeOrder(search_ed.getText().toString().trim(),pageno,3);
            }
        });
        search_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                OkHttpUtils.getInstance().cancelTag(this);

//                pageno=1;
//                load(classId,search_ed.getText().toString().trim(),pageno,null);
            }
        });
        pullToRefreshLayout.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pageno=1;
                lodeOrder(search_ed.getText().toString().trim(),1,1);

            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

//                load(search_ed.getText().toString().trim(),pullToRefreshLayout);


                lodeOrder(search_ed.getText().toString().trim(),pageno,2);
            }
        });
        pullToRefreshLayout.setPullUpEnable(false);
        AutoUtils.auto(pullToRefreshLayout.defaultRefreshView);

    }
    public void onEventMainThread(String refresh) {
        if(MainActivity11.CARTREFRESH.equals(refresh)){



        }

    }

    String searchText="";

    @Override
    public void initData() {
        if(!TextUtils.isEmpty(searchText)){
            pageno=1;
            lodeOrder(searchText,1,3);
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.instance.removeActivity(this);
    }
    int pageno = 1;
    String isAudit;
    AllOrderAdapter mAllOrderAdapter;
    private void lodeOrder( String key,final int pagenoTemp, final int isPull) {
        AppLoading.show(VpHeadProductSearchActivity.this);
        HttpUtil.getInstance().productIndexH5Search("","",key,pagenoTemp ,new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //   Logger.e("TAG",e);
                AppLoading.close();
                ToasAlert.toast("连接服务器失败");
                if (isPull == 1) {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else if (isPull == 2) {
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }

            @Override
            public void onResponse(String response, int id) {

                if (isPull == 1) {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else if (isPull == 2) {
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
                Logger.e("tag", "返回订单列表 ->" + response);
                try {

                    AppLoading.close();

                    JSONObject object = new JSONObject(response);

                    String optFlag = object.optString("optFlag");

                    if ( "yes".equals(optFlag)) {

                        JSONObject object1 = object.getJSONObject("res");

                        isAudit = object.optString("isAudit");
                        final List<VpGood> orderListTemp = new Gson().fromJson(object1.optString("list").toString(), new TypeToken<ArrayList<VpGood>>() {
                        }.getType());
//                        if(true)
//                            return;


                        if (pagenoTemp == 1) {
                            Logger.e("tag", "订单列表长度orderList" + orderListTemp.size());
                            if(orderListTemp==null||orderListTemp.size()==0){
                                mAllOrderAdapter = new AllOrderAdapter(VpHeadProductSearchActivity.this, null);
                                order_ListView.setAdapter(mAllOrderAdapter);
                                ToasAlert.toast("没有查询到数据");
                            }
                            mAllOrderAdapter = new AllOrderAdapter(VpHeadProductSearchActivity.this, orderListTemp);
                            order_ListView.setAdapter(mAllOrderAdapter);


                        } else {
                            if (orderListTemp != null && orderListTemp.size() > 0) {

                                mAllOrderAdapter.addList(orderListTemp);
                                mAllOrderAdapter.notifyDataSetChanged();
                                order_ListView.smoothScrollToPosition(mAllOrderAdapter.oldCount);

                            } else {
                                ToasAlert.toast("没有更多的数据");
                            }

                        }
//                         listView1.smoothScrollToPosition(orderList.size() - orderListTemp.size() + 5);

                        if (orderListTemp != null && orderListTemp.size() > 0)
                            pageno++;

                    } else {
                        ToasAlert.toast(object.optString("optDesc"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Logger.e("TAG", "json解析异常");
                    ToasAlert.toast("获取数据发生错误");
                }
            }
        });
    }




    class AllOrderAdapter extends BaseAdapter {

        public  int curPos=-1;
        Context context;
        List<VpGood> orderListTemp;
        public     int oldCount;

        public AllOrderAdapter(Context context,  List<VpGood> orderListTemp) {
            this.context = context;
            this.orderListTemp = orderListTemp;
        }
        public void addList( List<VpGood> mOrderlistAdd){
            oldCount=orderListTemp.size();
            orderListTemp.addAll(mOrderlistAdd);
        }
        @Override
        public int getCount() {
            return orderListTemp==null?0:orderListTemp.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            final VpGood mOrder = orderListTemp.get(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.vp_head_adapter_huopin_good, null);
//                AutoUtils.auto(convertView);
                holder.img = (ImageView)convertView.findViewById(R.id.img);

                holder.product_name = (TextView)convertView.findViewById(R.id.product_name);
                holder.sp_guig_tv = (TextView)convertView.findViewById(R.id.sp_guig_tv);
                holder. gys_name = (TextView)convertView.findViewById(R.id.gys_name);
                holder.sp_jiage = (TextView)convertView.findViewById(R.id.sp_jiage);
                holder.is_enable_tex = (TextView)convertView.findViewById(R.id.is_enable_tex);
//                holder.  num_ll = (LinearLayout)convertView.findViewById(R.id.num_ll);






                holder.  bianma = (TextView)convertView.findViewById(R.id.bianma);
                holder.  kuchun = (TextView)convertView.findViewById(R.id.kuchun);

                holder.menuFu = (LinearLayout) convertView.findViewById(R.id.menuFu);
                holder.menuFu1 = (LinearLayout) convertView.findViewById(R.id.menuFu1);
                holder.menuFu2 = (LinearLayout) convertView.findViewById(R.id.menuFu2);
                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    Intent intent = new Intent(context,OrderDetailsActicity.class);
////                    Intent intent = new Intent(context,OrderDetailsNewFragmentActicity.class);
////                    intent.putExtra("orderCode",mOrder.orderCode);
////                    context.startActivity(intent);
//                }
//            });

            if("Y".equals(mOrder.is_enable)){
                holder.is_enable_tex.setBackgroundColor(getResources().getColor(R.color.blue_head));
                holder.is_enable_tex.setText("已上架");
            }else {
                holder.is_enable_tex.setBackgroundColor(getResources().getColor(R.color.grey));
                holder.is_enable_tex.setText("已下架");
            }
            holder.product_name.setText(mOrder.sku_name==null?"":mOrder.sku_name);
            holder.sp_guig_tv.setText("规格："+mOrder.styleno);
            holder.gys_name.setText("供应商："+ mOrder.supplier_name);
            holder.sp_jiage.setText("￥"+ mOrder.unit_price+"/"+mOrder.unit);

            holder.bianma.setText("编码："+ mOrder.sku_code);
            holder.kuchun.setText("库存："+ mOrder.resultQty+"/"+mOrder.unit);
            Picasso.with(context).load(HttpUtil.HOST_STRING +"/"+ mOrder.img_path).error(R.mipmap.error).resize(AppUtils.dp2px(context,50),AppUtils.dp2px(context,50)).into(holder.img);
//            holder.category_count.setText("采购种类:"+mOrder.skuNum + "");
//            if(mOrder.sum_amount!=null && !"0".equals(mOrder.sum_amount) && !"0.0".equals(mOrder.sum_amount)&& !"0.00".equals(mOrder.sum_amount)){
//                holder.order_price_tv.setText("订单总额:"+"￥" + new DecimalFormat("0.00").format(Double.parseDouble(mOrder.sum_amount)));
//            }else {
//                holder.order_price_tv.setText("");
//            }
//
//            holder.order_time.setText(mOrder.create_time);
//            if(!TextUtils.isEmpty(mOrder.freight_charges)){
//                holder.freight_price.setText("运费:￥"+mOrder.freight_charges);
//                holder.freight_price.setVisibility(View.VISIBLE);
//            }else {

//                holder.freight_price.setVisibility(View.GONE);
//            }
            if(curPos == position){  // 显示

                holder.menuFu.setVisibility(View.VISIBLE);
            }else {
                holder.menuFu.setVisibility(View.GONE);
            }
//            if(position==mOrderlist.size()-1){
//
//                holder. henxian_flag3.setVisibility(View.GONE);
//            }else {
//                holder. henxian_flag3.setVisibility(View.VISIBLE);
//
//            }


            holder. menuFu1.removeAllViews();
            holder. menuFu2.removeAllViews();

//            setStatus( holder.status, mOrder, convertView, holder. menuFu,position);
            List<NemuItem> nemuItemList = new ArrayList<NemuItem>();

            NemuItem     nemuItemDatail1  = new NemuItem("编辑价格",R.mipmap.xiangqing);
            nemuItemList.add(nemuItemDatail1);
            NemuItem     nemuItemDatail2  = new NemuItem("编辑图片",R.mipmap.xiangqing);
            nemuItemList.add(nemuItemDatail2);
            NemuItem     nemuItemDatail3  = new NemuItem("编辑",R.mipmap.xiangqing);
            nemuItemList.add(nemuItemDatail3);
            NemuItem     nemuItemDatail4  = new NemuItem("详情",R.mipmap.xiangqing);
            nemuItemList.add(nemuItemDatail4);

//             holder.status_image2.setVisibility(View.GONE);

            if(nemuItemList.size()<=5){
                holder. menuFu2.setVisibility(View.GONE);

            }else {  //显示2排
                holder. menuFu2.setVisibility(View.VISIBLE);
            }

            if(nemuItemList!=null && nemuItemList.size()>0){
                for ( int i=0;i<nemuItemList.size();i++) {
                    final NemuItem mNemuItem=nemuItemList.get(i);
                    View view  =View.inflate(context,R.layout.vphead_nemu_item,null);

//                    AutoUtils.auto(view);
                    ImageView   memu_iv   = (ImageView)view.findViewById(R.id.memu_iv);
                    TextView  memu_text= (TextView)view.findViewById(R.id.memu_text);
                    LinearLayout  menuTemp= (LinearLayout)view.findViewById(R.id.menu2);
                    //设置宽度
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.width=AutoUtils.displayWidth/5;
                    //
                    menuTemp.setLayoutParams(lp);
                    memu_iv.setImageResource(mNemuItem.resImgId);
                    memu_text.setText(mNemuItem.nemuName);

                    if(i<5){
                        holder. menuFu1.addView(view);
                    }else {
                        holder. menuFu2.addView(view);
                    }
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if("取消订单".equals(mNemuItem.nemuName)){

                                AlertDialog.Builder builder = new AlertDialog.Builder(VpHeadProductSearchActivity.this,R.style.update_dialog);

                                builder.setMessage("确认取消吗？");
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

//                                         cancelOrder(mOrder.orderCode);
                                    }
                                });
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }

                                });
                                builder.create().show();

                            }
                            else
                            if("编辑价格".equals(mNemuItem.nemuName)){
                                Intent intent = new Intent(context,VpHeadEditorPriceActivity.class);
                                intent.putExtra("productid",mOrder.id);
//                                intent.putExtra("payStatus",mOrder.pay_status);
                                context.startActivity(intent);

                            }
                            else
                            if("详情".equals(mNemuItem.nemuName)){
                                Intent intent = new Intent(context,VpHeadGoodDetailNewActivity.class);
                                intent.putExtra("productid",mOrder.id);
//                                intent.putExtra("payStatus",mOrder.pay_status);
                                context.startActivity(intent);

                            }
                            else
                            if("编辑".equals(mNemuItem.nemuName)){
                                Intent intent = new Intent(context,VpHeadGoodEditsActivity.class);
                                intent.putExtra("productid",mOrder.id);
//                                intent.putExtra("payStatus",mOrder.pay_status);
                                context.startActivity(intent);

                            }


                        }
                    });

                }
            }
            // 更多
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(more_button.getText().toString().contains("取消")){ //取消订单
//                        cancelOrder(mOrder.orderCode);
//
//                    }else {  //更多
                    if(   holder.menuFu.getVisibility()==View.VISIBLE){  //当前显示
                        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_SELF,
                                0f, Animation.RELATIVE_TO_SELF,1.0f);
                        mShowAction.setDuration(200);
                        //透明度控制动画效果 alpha
                        AlphaAnimation animation_alpha=new AlphaAnimation(1.0f,0.1f);
                        //第一个参数fromAlpha为 动画开始时候透明度
                        //第二个参数toAlpha为 动画结束时候透明度
//                        animation_alpha.setRepeatCount(-1);//设置循环
                        animation_alpha.setDuration(200);//设置时间持续时间为 5000毫秒
                        AnimationSet animationSet=new AnimationSet(true);
                        animationSet.addAnimation(mShowAction);//透明度
                        animationSet.addAnimation(animation_alpha);//旋转
                        holder.menuFu.startAnimation(animationSet);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                holder.menuFu.setVisibility(View.GONE);
                                notifyDataSetChanged();
                            }

                        }, 200);

                        curPos=-1;
                    }else {//当前隐藏
                        TranslateAnimation       mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_SELF,
                                -1.0f, Animation.RELATIVE_TO_SELF, 0f);
                        mShowAction.setDuration(200);

                        //透明度控制动画效果 alpha
                        AlphaAnimation      animation_alpha=new AlphaAnimation(0.1f,1.0f);
                        //第一个参数fromAlpha为 动画开始时候透明度
                        //第二个参数toAlpha为 动画结束时候透明度
//                        animation_alpha.setRepeatCount(-1);//设置循环
                        animation_alpha.setDuration(200);//设置时间持续时间为 5000毫秒

                        AnimationSet       animationSet=new AnimationSet(true);
                        animationSet.addAnimation(mShowAction);//透明度
                        animationSet.addAnimation(animation_alpha);//旋转

                        holder.menuFu.startAnimation(animationSet);
                        holder.menuFu.setVisibility(View.VISIBLE);
                        curPos=position;
                        notifyDataSetChanged();
                        if(position==orderListTemp.size()-1){
                            HomePageActivity mAllOrderActicity= (HomePageActivity)context;
                            //滑动到最下面
                            mAllOrderActicity.mOrderFragment3.  order_ListView.setSelection(position);
                        }
//         }


                    }

                }
            });



            return convertView;
        }

        public final class ViewHolder {
            public ImageView img;



//            public LinearLayout num_ll;
            public TextView product_name;
            public TextView is_enable_tex;
            public TextView sp_guig_tv;
            public TextView gys_name;
            public TextView sp_jiage;



            public    LinearLayout   menuFu1;
            public    LinearLayout   menuFu2;
            public    LinearLayout   menuFu;

            public TextView bianma;
            public TextView kuchun;



        }

        public void setStatus(TextView status_tv, final OrderVp mOrder, final View convertView , final LinearLayout menu, final int position) {
//        int yellow= Color.rgb(255, 132, 70);
            int green = Color.rgb(132, 195, 85);
            final String handStatus = mOrder.handle_status;
            switch (handStatus) {
                case "1":
                    String opt_type = mOrder.isAudit;
                    if ("yes".equals(opt_type)) { //审核通过
                        status_tv.setText("待配送");
                        status_tv.setTextColor(Color.parseColor("#318eff"));
                        //           more_button.setVisibility(View.INVISIBLE);
                    } else {//没审核
                        status_tv.setText("待配送");
                        status_tv.setTextColor(Color.parseColor("#318eff"));
                        //       more_button.setText("取消订单");
                        //          more_button.setVisibility(View.VISIBLE);
                    }
                    break;
                case "2":
                    status_tv.setText("配送中");
                    status_tv.setTextColor(Color.parseColor("#318eff"));

                    break;
                case "3":
                    status_tv.setTextColor(green);
                    status_tv.setText("已签收");

                    break;
                case "4": // 已经取消的
                    status_tv.setTextColor(Color.parseColor("#9b9b9b"));

                    status_tv.setText("已取消");

                    break;
                case "9":
                    status_tv.setText("待审核");
                    status_tv.setTextColor(Color.parseColor("#318eff"));

                    break;
                case "8":
                    status_tv.setText("待审核");
                    status_tv.setTextColor(Color.parseColor("#318eff"));
                    break;
                default:
                    break;
            }
            // 更多
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(more_button.getText().toString().contains("取消")){ //取消订单
//                        cancelOrder(mOrder.orderCode);
//
//                    }else {  //更多
                    if( menu.getVisibility()==View.VISIBLE){  //当前显示
                        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_SELF,
                                0f, Animation.RELATIVE_TO_SELF,1.0f);
                        mShowAction.setDuration(200);
                        //透明度控制动画效果 alpha
                        AlphaAnimation animation_alpha=new AlphaAnimation(1.0f,0.1f);
                        //第一个参数fromAlpha为 动画开始时候透明度
                        //第二个参数toAlpha为 动画结束时候透明度
//                        animation_alpha.setRepeatCount(-1);//设置循环
                        animation_alpha.setDuration(200);//设置时间持续时间为 5000毫秒
                        AnimationSet animationSet=new AnimationSet(true);
                        animationSet.addAnimation(mShowAction);//透明度
                        animationSet.addAnimation(animation_alpha);//旋转
                        menu.startAnimation(animationSet);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                menu.setVisibility(View.GONE);
                                notifyDataSetChanged();
                            }

                        }, 200);

                        curPos=-1;
                    }else {//当前隐藏
                        TranslateAnimation       mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_SELF,
                                -1.0f, Animation.RELATIVE_TO_SELF, 0f);
                        mShowAction.setDuration(200);

                        //透明度控制动画效果 alpha
                        AlphaAnimation      animation_alpha=new AlphaAnimation(0.1f,1.0f);
                        //第一个参数fromAlpha为 动画开始时候透明度
                        //第二个参数toAlpha为 动画结束时候透明度
//                        animation_alpha.setRepeatCount(-1);//设置循环
                        animation_alpha.setDuration(200);//设置时间持续时间为 5000毫秒

                        AnimationSet       animationSet=new AnimationSet(true);
                        animationSet.addAnimation(mShowAction);//透明度
                        animationSet.addAnimation(animation_alpha);//旋转

                        menu.startAnimation(animationSet);
                        menu.setVisibility(View.VISIBLE);
                        curPos=position;
                        notifyDataSetChanged();
                        if(position==orderListTemp.size()-1){
                            HomePageActivity mAllOrderActicity= (HomePageActivity)context;
                            //滑动到最下面
                            mAllOrderActicity.mOrderFragment3.  order_ListView.setSelection(position);
                        }
//         }


                    }

                }
            });


        }

    }

}
