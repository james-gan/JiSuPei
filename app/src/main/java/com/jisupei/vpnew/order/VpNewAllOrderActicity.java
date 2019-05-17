package com.jisupei.vpnew.order;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import com.jisupei.activity.base.ContentValue;
import com.jisupei.R;
import com.jisupei.activity.datail.H5orderActivity;
import com.jisupei.activity.order.ConfirmGoodsActivity;
import com.jisupei.activity.order.ConfirmGoodsWaybillSelectActivity;
import com.jisupei.activity.order.NewSearchOrderActivity;
import com.jisupei.activity.order.OrderHistoryActicity;
import com.jisupei.activity.order.bean.NemuItem;
import com.jisupei.activity.order.widget.SelectOrderPopupwindow;
import com.jisupei.http.HttpBase;
import com.jisupei.http.HttpUtil;
import com.jisupei.activity.base.model.Order;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.AutoUtils;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/7/12.
 */
public class VpNewAllOrderActicity extends Activity {


    @InjectView(R.id.order_ListView)
    public   PullableListView order_ListView;
    @InjectView(R.id.back_bt)
    ImageView backBt;
    @InjectView(R.id.textView)
    TextView textView;
    @InjectView(R.id.textView_iv)
    ImageView textView_iv;
    @InjectView(R.id.search_iv)
    ImageView searchIv;
    @InjectView(R.id.top_layout)
    RelativeLayout topLayout;
    @InjectView(R.id.root)
    LinearLayout root;
    String nopay;
    @InjectView(R.id.pullToRefreshLayout)
  public  PullToRefreshLayout pullToRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vpnew_activity_order_all);
        EventBus.getDefault().register(this);
        AutoUtils.auto(this);
        ButterKnife.inject(this);
        AppUtils.expandViewTouchDelegate(backBt, 30, 30, 50, 50);

          Intent  intent=   getIntent();


        loadData();
        setListener();
    }
    int defaultButton = 1;
    //默认
   String handleStatus=null;

    SelectOrderPopupwindow mSelectOrderPopupwindow;
    public void setListener() {
        searchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(VpNewAllOrderActicity.this, NewSearchOrderActivity.class);
                VpNewAllOrderActicity.this.startActivity(intent);
            }
        });


        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pullToRefreshLayout.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pageno=1;
                lodeOrder(handleStatus ,pageno,1);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

                lodeOrder(handleStatus ,pageno,2);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  mSelectOrderPopupwindow =new SelectOrderPopupwindow(  defaultButton,VpNewAllOrderActicity.this, new SelectOrderPopupwindow.MyitemsOnClick() {
                    @Override
                    public void bit1Click(View v) {
                        pageno = 1;
                        handleStatus=null;
                        lodeOrder(handleStatus ,pageno, 3);
                        mSelectOrderPopupwindow.dismiss();
                        textView.setText("全部订单");
                        defaultButton=1;

                    }

                    @Override
                    public void bit2Click(View v) { //审核
                        pageno = 1;
                        handleStatus="8,9";
                        lodeOrder( handleStatus,pageno, 3);
                        mSelectOrderPopupwindow.dismiss();
                        textView.setText("待审核订单");
                        defaultButton=2;
                    }

                    @Override
                    public void bit3Click(View v) {
                        pageno = 1;
                        handleStatus="1";
                        lodeOrder( handleStatus,pageno, 3);
                        mSelectOrderPopupwindow.dismiss();
                        textView.setText("待配送订单");
                        defaultButton=3;
                    }

                    @Override
                    public void bit4Click(View v) {
                        pageno = 1;
                        handleStatus="2";
                        lodeOrder(handleStatus,pageno, 3);
                        mSelectOrderPopupwindow.dismiss();
                        textView.setText("配送中订单");
                        defaultButton=4;
                    }

                    @Override
                    public void bit5Click(View v) {
                        pageno = 1;
                        handleStatus="3";
                        lodeOrder(handleStatus ,pageno, 3);
                        mSelectOrderPopupwindow.dismiss();

                        textView.setText("已签收订单");
                        defaultButton=5;
                    }

                    @Override
                    public void bit6Click(View v) {
                        pageno = 1;
                        handleStatus="4";
                        lodeOrder(handleStatus ,pageno, 3);
                        mSelectOrderPopupwindow.dismiss();
                        textView.setText("已取消订单");
                        defaultButton=6;

                    }
                });

                mSelectOrderPopupwindow.showAsDropDown(topLayout);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    public void loadData() {
       // lodeOrder( handleStatus,pageno, 3);
        pageno = 1;
        nopay= getIntent().getStringExtra("nopay");
        String   state=   getIntent().getStringExtra("state");
        searchIv.setVisibility(View.GONE);
        if("1".equals(state)){
            handleStatus="1";
            lodeOrder( handleStatus,pageno, 3);
//            textView.setEnabled(false);
//            textView_iv.setVisibility(View.GONE);
            textView.setText("待配送订单");
            defaultButton=3;

        }else if("2".equals(state)){
            handleStatus="2";
            lodeOrder( handleStatus,pageno, 3);
//            textView.setEnabled(false);
//            textView_iv.setVisibility(View.GONE);
            textView.setText("配送中订单");
            defaultButton=4;
        }else if("3".equals(state)){

            pageno = 1;
            handleStatus="3";
            lodeOrder(handleStatus ,pageno, 3);
            textView.setText("已签收订单");
            defaultButton=5;

        }
        else if("4".equals(state)){

            pageno = 1;
            handleStatus="4";
            lodeOrder(handleStatus ,pageno, 3);
            textView.setText("已取消订单");
            defaultButton=6;

        }else {
            lodeOrder(null ,pageno, 3);
            textView.setEnabled(true);
            textView_iv.setVisibility(View.VISIBLE);
            textView.setText("全部订单");
            if("no".equals(nopay)){
                textView.setText("待付款订单");
            }
//            searchIv.setVisibility(View.VISIBLE);
            defaultButton=1;
        }


    }

    int pageno = 1;
    String isAudit;
    AllOrderAdapter mAllOrderAdapter;
    private void lodeOrder( String handleStatus,final int pagenoTemp, final int isPull) {
        AppLoading.show(VpNewAllOrderActicity.this);
        HttpUtil.getInstance().vporderList(handleStatus,nopay,pagenoTemp ,  new StringCallback() {
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
                        final ArrayList<Order> orderListTemp = new Gson().fromJson(object1.getJSONArray("orders").toString(), new TypeToken<ArrayList<Order>>() {
                        }.getType());

                        if (pagenoTemp == 1) {
                            Logger.e("tag", "订单列表长度orderList" + orderListTemp.size());
                            if(orderListTemp==null||orderListTemp.size()==0){
                                mAllOrderAdapter = new AllOrderAdapter(VpNewAllOrderActicity.this, null);
                                order_ListView.setAdapter(mAllOrderAdapter);
                                ToasAlert.toast("没有查询到数据");
                            }
//                            if(mAllOrderAdapter==null){
                                mAllOrderAdapter = new AllOrderAdapter(VpNewAllOrderActicity.this, orderListTemp);
                                order_ListView.setAdapter(mAllOrderAdapter);
//                            }else {
//                                mAllOrderAdapter.notifyDataSetChanged();
//                            }
//                            }

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
        List<Order> mOrderlist;
    public     int oldCount;

        public AllOrderAdapter(Context context, List<Order> mOrderlist) {
            this.context = context;
            this.mOrderlist = mOrderlist;
        }
        public void addList( List<Order> mOrderlistAdd){
            oldCount=mOrderlist.size();
            mOrderlist.addAll(mOrderlistAdd);
        }
        @Override
        public int getCount() {
            return mOrderlist==null?0:mOrderlist.size();
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
            final Order mOrder = mOrderlist.get(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.adapter_all_order_item, null);
                AutoUtils.auto(convertView);
                holder.order_code_tv = (TextView) convertView.findViewById(R.id.order_code_tv);
                holder.status = (TextView) convertView.findViewById(R.id.status);
                holder.category_count = (TextView) convertView.findViewById(R.id.category_count);
                holder.order_price_tv = (TextView) convertView.findViewById(R.id.order_price_tv);
                holder.order_time = (TextView) convertView.findViewById(R.id.order_time);
                holder.freight_price = (TextView) convertView.findViewById(R.id.freight_price);
                holder.ck_name = (TextView) convertView.findViewById(R.id.ck_name);
//                holder.more_button = (TextView) convertView.findViewById(R.id.more_button);
                holder.menuFu = (LinearLayout) convertView.findViewById(R.id.menuFu);
                holder.menuFu1 = (LinearLayout) convertView.findViewById(R.id.menuFu1);
                holder.menuFu2 = (LinearLayout) convertView.findViewById(R.id.menuFu2);
                holder.henxian_flag3 = convertView.findViewById(R.id.henxian_flag3);
                //点击进入订单详情
                holder.rl1 = (RelativeLayout)convertView.findViewById(R.id.rl1);

                holder.status_image1 = (ImageView)convertView.findViewById(R.id.status_image1);

                holder.status_image2 = (ImageView)convertView.findViewById(R.id.status_image2);

                holder.wzf_status = (ImageView)convertView.findViewById(R.id.wzf_status);
                holder.yzf_status = (ImageView)convertView.findViewById(R.id.yzf_status);

               convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }
            holder.ck_name.setText(mOrder.wm_name==null?"":mOrder.wm_name);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context,OrderDetailsActicity.class);
//                    Intent intent = new Intent(context,OrderDetailsNewFragmentActicity.class);
//                    intent.putExtra("orderCode",mOrder.orderCode);
//                    context.startActivity(intent);
                }
            });
            holder.order_code_tv.setText(""+ mOrder.orderCode);

            holder.category_count.setText("采购种类:"+mOrder.skuNum + "");
            if(mOrder.sumAmount!=null && !"0".equals(mOrder.sumAmount) && !"0.0".equals(mOrder.sumAmount)&& !"0.00".equals(mOrder.sumAmount)){
                holder.order_price_tv.setText("订单总额:"+"￥" + new DecimalFormat("0.00").format(Double.parseDouble(mOrder.sumAmount)));
            }else {
                holder.order_price_tv.setText("");
            }

            holder.order_time.setText(mOrder.createTime);
            if(!TextUtils.isEmpty(mOrder.freight_charges)){
                holder.freight_price.setText("运费:￥"+mOrder.freight_charges);
                holder.freight_price.setVisibility(View.VISIBLE);
            }else {

                holder.freight_price.setVisibility(View.GONE);
            }
            if(curPos == position){  // 显示

                holder.menuFu.setVisibility(View.VISIBLE);
            }else {
                holder.menuFu.setVisibility(View.GONE);
            }
            if(position==mOrderlist.size()-1){

                holder. henxian_flag3.setVisibility(View.GONE);
            }else {
                holder. henxian_flag3.setVisibility(View.VISIBLE);

            }


            holder. menuFu1.removeAllViews();
            holder. menuFu2.removeAllViews();
            List<NemuItem> nemuItemList = new ArrayList<NemuItem>();
           //
//           if(!"4".equals(mOrder.handleStatus) ){   //取消订单

               if("1".equals(HttpBase.isSelf)){//是自营

                   if(!"1".equals(mOrder.payStatus) ){//没有支付,

                       if(!"4".equals(mOrder.handleStatus) ){   //取消订单
                           NemuItem     nemuItemzf  = new NemuItem("立即支付",R.mipmap.zf);
                           nemuItemList.add(nemuItemzf);
                       }
                       holder. wzf_status.setVisibility(View.VISIBLE);
                       holder. yzf_status.setVisibility(View.GONE);
                   }else { //已经支付
                       holder. wzf_status.setVisibility(View.GONE);
                       holder. yzf_status.setVisibility(View.VISIBLE);
                   }
               }else {//不是自营
                   holder. wzf_status.setVisibility(View.GONE);
                   holder. yzf_status.setVisibility(View.GONE);
               }
//           }
            NemuItem     nemuItemDatail  = new NemuItem("查看详情",R.mipmap.xiangqing);
            nemuItemList.add(nemuItemDatail);

            if(mOrder.orderCode.contains("A")){
                NemuItem     nemuItem1  = new NemuItem("历史订单",R.mipmap.lsdd);
                nemuItemList.add(nemuItem1);
                //图片

                holder.status_image2.setVisibility(View.VISIBLE);
            }else {

                holder.status_image2.setVisibility(View.GONE);
            }


       //取消订单
             final String handStatus = mOrder.handleStatus;
            switch (handStatus) {
                case "1":
                    String opt_type = mOrder.isAudit;
                    if ("yes".equals(opt_type)) { //审核通过
                    } else {//没审核
                        NemuItem     nemuItem1  = new NemuItem("取消订单",R.mipmap.canalorder);
                        nemuItemList.add(nemuItem1);
                    }
                    break;

                case "9":  //取消订单
                    NemuItem     nemuItem1  = new NemuItem("取消订单",R.mipmap.canalorder);
                    nemuItemList.add(nemuItem1);
                    break;

                default:
                    break;
            }

            //查看物流
            if(mOrder.viewLogisCase==null||Integer.parseInt(mOrder.viewLogisCase)==0){

            }else  if(Integer.parseInt(mOrder.viewLogisCase)==1){
                NemuItem     nemuItem1  = new NemuItem("查看物流",R.mipmap.ckwl);
                nemuItemList.add(nemuItem1);
            }else {
                NemuItem     nemuItem1  = new NemuItem("查看物流",R.mipmap.ckwl);
                nemuItemList.add(nemuItem1);

            }
            //查看异常
            if(mOrder.viewErrorCase==null||Integer.parseInt(mOrder.viewErrorCase)==0){
                holder.status_image1.setVisibility(View.GONE);

            }else  if(Integer.parseInt(mOrder.viewErrorCase)==1){
                holder.status_image1.setVisibility(View.VISIBLE);
                NemuItem     nemuItem1  = new NemuItem("查看异常",R.mipmap.scyc);
                nemuItemList.add(nemuItem1);
            }else {
                holder.status_image1.setVisibility(View.VISIBLE);
                NemuItem     nemuItem1  = new NemuItem("查看异常",R.mipmap.scyc);
                nemuItemList.add(nemuItem1);
            }
            //查看回单
            if(mOrder.isExistReceipt==null||Integer.parseInt(mOrder.isExistReceipt)==0){

            }else  if(Integer.parseInt(mOrder.isExistReceipt)==1){
                NemuItem     nemuItem1  = new NemuItem("查看回单",R.mipmap.ckhd);
                nemuItemList.add(nemuItem1);
            }else {
                NemuItem     nemuItem1  = new NemuItem("查看回单",R.mipmap.ckhd);
                nemuItemList.add(nemuItem1);
            }

            //上传异常
            if(mOrder.upErrorCase==null||Integer.parseInt(mOrder.upErrorCase)==0){

            }else  if(Integer.parseInt(mOrder.upErrorCase)==1){
                NemuItem     nemuItem1  = new NemuItem("上传异常",R.mipmap.scyc);
                nemuItemList.add(nemuItem1);
            }else {
                NemuItem     nemuItem1  = new NemuItem("上传异常",R.mipmap.scyc);
                nemuItemList.add(nemuItem1);
            }

            //上传回单
            if(mOrder.upReceiptCase==null||Integer.parseInt(mOrder.upReceiptCase)==0){

            }else  if(Integer.parseInt(mOrder.upReceiptCase)==1){
                NemuItem     nemuItem1  = new NemuItem("上传回单",R.mipmap.up_hd);
                nemuItemList.add(nemuItem1);
            }else {
                NemuItem     nemuItem1  = new NemuItem("上传回单",R.mipmap.up_hd);
                nemuItemList.add(nemuItem1);
            }

            //确认收货Integer.parseInt(mOrder.receiptCase)==0&&
            if("2".equals(mOrder.handleStatus)){
                NemuItem     nemuItem1  = new NemuItem("确认收货",R.mipmap.qrsh_img);
                nemuItemList.add(nemuItem1);
            }else {
//                NemuItem     nemuItem1  = new NemuItem("确认收货",R.mipmap.qrsh_img);
//                nemuItemList.add(nemuItem1);
            }

                    if(nemuItemList.size()<=5){
                        holder. menuFu2.setVisibility(View.GONE);

                    }else {  //显示2排
                        holder. menuFu2.setVisibility(View.VISIBLE);
                    }

                if(nemuItemList!=null && nemuItemList.size()>0){
                    for ( int i=0;i<nemuItemList.size();i++) {
                        final NemuItem mNemuItem=nemuItemList.get(i);
                        View view  =View.inflate(context,R.layout.nemu_item,null);

                        AutoUtils.auto(view);
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

                                    AlertDialog.Builder builder = new AlertDialog.Builder(VpNewAllOrderActicity.this,R.style.update_dialog);

                                    builder.setMessage("确认取消吗？");
                                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            cancelOrder(mOrder.orderCode);
                                        }
                                    });
                                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }

                                    });
                                    builder.create().show();

                                }else
                                if("查看详情".equals(mNemuItem.nemuName)){
                                    Intent intent = new Intent(context,NewVpOrderDetailsNewFragmentActicity.class);
                                    intent.putExtra("orderCode",mOrder.orderCode);
                                    intent.putExtra("payStatus",mOrder.payStatus);
                                    intent.putExtra("price_type",mOrder.price_type);
                                    context.startActivity(intent);

                                }else
                                if("历史订单".equals(mNemuItem.nemuName)){
                                        Intent intent = new Intent(context,OrderHistoryActicity.class);
                                        intent.putExtra("orderCode",mOrder.orderCode);
                                    intent.putExtra("price_type",mOrder.price_type);
                                        startActivity(intent);

                                }else if("确认收货".equals(mNemuItem.nemuName)){

                                     if("1".equals(mOrder.receiptCase)){
//                                         ToasAlert.toastCenter("确认收货");
                                         Intent intent = new Intent(context,ConfirmGoodsActivity.class);
                                         intent.putExtra("count","1");
                                         intent.putExtra("orderCode",mOrder.orderCode);
                                         intent.putExtra("price_type",mOrder.price_type);
                                         startActivity(intent);
                                     }else {  //多个确认收货
//                                         ToasAlert.toastCenter("多个确认收货");
                                         Intent intent = new Intent(context,ConfirmGoodsWaybillSelectActivity.class);
                                         intent.putExtra("orderCode",mOrder.orderCode);
                                         intent.putExtra("price_type",mOrder.price_type);
                                         startActivity(intent);
                                     }
                                 }else if("上传回单".equals(mNemuItem.nemuName)){
//                                     if("1".equals(mOrder.upReceiptCase)){
////                                         ToasAlert.toastCenter("上传回单");
//                                         Intent intent = new Intent(context,LogisticsReceiptAddImgActivity.class);
//                                         intent.putExtra("count","1");
//                                         intent.putExtra("orderCode",mOrder.orderCode);
//                                         startActivity(intent);
//                                     }else {  //多个回单
////                                         ToasAlert.toastCenter("多个上传回单");
//                                         Intent intent = new Intent(context,ReceiptWaybillSelectAddActivity.class);
//                                         intent.putExtra("orderCode",mOrder.orderCode);
//                                         startActivity(intent);
//                                     }
                                    Intent intent = new Intent(context,NewVpOrderDetailsNewFragmentActicity.class);
                                    intent.putExtra("orderCode",mOrder.orderCode);
                                    intent.putExtra("price_type",mOrder.price_type);
                                    intent.putExtra("flag","3");
                                    startActivity(intent);
                                 } else if("上传异常".equals(mNemuItem.nemuName)){

//                                     if("1".equals(mOrder.upErrorCase)){
////                                         ToasAlert.toastCenter("上传异常");
//                                         Intent intent = new Intent(context,ExptionSelectUpProductActivity.class);
//                                         intent.putExtra("count","1");
//                                         intent.putExtra("orderCode",mOrder.orderCode);
//                                         intent.putExtra("price_type",mOrder.price_type);
//                                         startActivity(intent);
//
//                                     }else { //多个上传异常
////                                         ToasAlert.toastCenter("多个上传异常");
//                                         Intent intent = new Intent(context,ExceptionUpWaybillSelectActivity.class);
//                                         intent.putExtra("orderCode",mOrder.orderCode);
//                                         intent.putExtra("price_type",mOrder.price_type);
//                                         startActivity(intent);
//                                     }

                                    Intent intent = new Intent(context,NewVpOrderDetailsNewFragmentActicity.class);
                                    intent.putExtra("orderCode",mOrder.orderCode);
                                    intent.putExtra("price_type",mOrder.price_type);
                                    intent.putExtra("flag","4");
                                    startActivity(intent);
                                 }else if("查看回单".equals(mNemuItem.nemuName)){
//
                                    Intent intent = new Intent(context,NewVpOrderDetailsNewFragmentActicity.class);
                                    intent.putExtra("orderCode",mOrder.orderCode);
                                    intent.putExtra("price_type",mOrder.price_type);
                                    intent.putExtra("flag","3");
                                    startActivity(intent);

                                 }else if("查看异常".equals(mNemuItem.nemuName)){

                                    Intent intent = new Intent(context,NewVpOrderDetailsNewFragmentActicity.class);
                                    intent.putExtra("orderCode",mOrder.orderCode);
                                    intent.putExtra("price_type",mOrder.price_type);
                                    intent.putExtra("flag","4");
                                    startActivity(intent);

                                   }else if("查看物流".equals(mNemuItem.nemuName)){

                                    Intent intent = new Intent(context,NewVpOrderDetailsNewFragmentActicity.class);
                                    intent.putExtra("orderCode",mOrder.orderCode);
                                    intent.putExtra("price_type",mOrder.price_type);
                                    intent.putExtra("flag","2");
                                    startActivity(intent);

                                 }
//                                else if("历史订单".equals(mNemuItem.nemuName)){
//                                     ToasAlert.toastCenter("历史订单");
//                                 }
                                else if("立即支付".equals(mNemuItem.nemuName)){
                                    Intent i= new Intent(context, H5orderActivity.class);

                                    i.putExtra("orderCode",mOrder.orderCode);
                                    context.startActivity(i);
                                }

                            }
                        });

                    }
                }
//                  OrderItemPopupwindow mOrderItemPopupwindow = new OrderItemPopupwindow(AllOrderActicity.this, holder.more_button, new OrderItemPopupwindow.MyitemsOnClick() {
//                        @Override
//                        public void btn1Click(View v) {
//
//                            // 上传回单
//                            Intent intent = new Intent(context,ReceiptWaybillSelectAddActivity.class);
//                            context.startActivity(intent);
//
//                        }
//
//                        @Override
//                        public void btn2Click(View v) {
//
//                              //确认收货
//                            Intent intent = new Intent(context,ConfirmGoodsActivity.class);
//                            context.startActivity(intent);
//                        }
//                        @Override
//                        public void btn3Click(View v) {
//
//                            // 上传异常
//
//                            context.startActivity(intent);
//                        }
//                        @Override
//                        public void btn4Click(View v) {
//                            // 查看物流
//                        }
//                        @Override
//                        public void btn5Click(View v) {
//
//                            // 历史订单
//                            Intent intent = new Intent(context,OrderHistoryActicity.class);
//                            context.startActivity(intent);
//                        }
//                        @Override
//                        public void btn6Click(View v) {
//                            // 查看回单
//                            Intent intent = new Intent(context,LogisticsReceiptReadActivity.class);
//                            context.startActivity(intent);
//                        }
//                        @Override
//                        public void btn7Click(View v) {
//                            // 查看异常
//                            Intent intent = new Intent(context,ExptionReadProductActivity.class);
//                            context.startActivity(intent);
//
//                        }
//                    });



          setStatus( holder.status, mOrder, convertView, holder. menuFu,position);
                 return convertView;
        }

        public final class ViewHolder {
            public TextView order_code_tv;
            public TextView ck_name;
            public TextView status;
            public TextView category_count;
            public TextView order_price_tv;
            public TextView order_time;
            public TextView freight_price;
//            public TextView more_button;

            public View henxian_flag3;
            public    RelativeLayout   rl1;
            public    LinearLayout   menuFu1;
            public    LinearLayout   menuFu2;
            public    LinearLayout   menuFu;
            public    ImageView   status_image1;
            public    ImageView   status_image2;
            public    ImageView   wzf_status;
            public    ImageView   yzf_status;
//            public    LinearLayout btn1;
//            public    LinearLayout btn2;
//            public    LinearLayout btn3;
//            public    LinearLayout btn4;
//            public    LinearLayout btn5;
//            public    LinearLayout btn6;
//            public    LinearLayout btn7;


        }

        public void setStatus(TextView status_tv, final Order mOrder, final View more_button , final LinearLayout menu, final int position) {
//        int yellow= Color.rgb(255, 132, 70);
            int green = Color.rgb(132, 195, 85);
            final String handStatus = mOrder.handleStatus;
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
//                    more_button.setText("更多");
             //       more_button.setVisibility(View.VISIBLE);
                    break;
                case "3":
                    status_tv.setTextColor(green);
                    status_tv.setText("已签收");
//                    more_button.setText("更多");
             //       more_button.setVisibility(View.VISIBLE);
                    break;
                case "4": // 已经取消的
                    status_tv.setTextColor(Color.parseColor("#9b9b9b"));

                    status_tv.setText("已取消");
//                    more_button.setText("更多");
//                   more_button.setVisibility(View.INVISIBLE);
                    break;
                case "9":
                    status_tv.setText("待审核");
                    status_tv.setTextColor(Color.parseColor("#318eff"));

              //      more_button.setText("取消订单");
              //      more_button.setVisibility(View.VISIBLE);
                    break;
                case "8":
                    status_tv.setText("待审核");

                    status_tv.setTextColor(Color.parseColor("#318eff"));
            //        more_button.setText("取消订单"); //隐藏
//                    more_button.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;
            }
            // 更多
            more_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(more_button.getText().toString().contains("取消")){ //取消订单
//                        cancelOrder(mOrder.orderCode);
//
//                    }else {  //更多
                        if( menu.getVisibility()==View.VISIBLE){  //当前显示
                            TranslateAnimation       mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_SELF,
                                    0f, Animation.RELATIVE_TO_SELF,1.0f);
                            mShowAction.setDuration(200);
                            //透明度控制动画效果 alpha
                            AlphaAnimation      animation_alpha=new AlphaAnimation(1.0f,0.1f);
                            //第一个参数fromAlpha为 动画开始时候透明度
                            //第二个参数toAlpha为 动画结束时候透明度
//                        animation_alpha.setRepeatCount(-1);//设置循环
                            animation_alpha.setDuration(200);//设置时间持续时间为 5000毫秒
                            AnimationSet       animationSet=new AnimationSet(true);
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
                            if(position==mOrderlist.size()-1){
                                VpNewAllOrderActicity mAllOrderActicity= (VpNewAllOrderActicity)context;
                                mAllOrderActicity.  order_ListView.setSelection(position);
                            }
//                        }


                    }

                }
            });


        }

    }

 //取消订单
  public void cancelOrder(final String orderCode){
//     AppLoading.show(this.getContext());

        RequestQueue requestQueue = Volley.newRequestQueue(VpNewAllOrderActicity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUtil.HOST_STRING +
                "/appOrder/cancelOrder.do?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        AppLoading.close();
                        Logger.e("tag", "取消订单返回 +" + response);

                        try {
                            JSONObject object =new JSONObject(response);
                            String hint = object.getString("optFlag");
                            if(hint.equals("yes")) {

                                ToasAlert.toastCenter("取消订单成功");
                                pageno=1;
                                lodeOrder(handleStatus ,pageno, 3);  //加载数据

                            }else{
                                ToasAlert.toastCenter(object.optString("optDesc"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToasAlert.toastCenter("连接服务器失败");
                Logger.e("TAG", "失败"+error.getMessage(), error);
//                AppLoading.close();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                JSONObject object = new JSONObject();

                try {
                    object.put("owner", HttpBase.owner);
                    object.put("account", HttpBase.account);
                    object.put("orderCode", orderCode);
                    object.put("deviceOS","android");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Map<String, String> map = new HashMap<>();
                map.put("content", object.toString());
                map.put("version","1");
                map.put("login_token",HttpBase.token);
                map.put("deviceOS","android-"+android.os.Build.VERSION.RELEASE);
                return map;
            }
        };
        //超时时间10s,最大重连次数2次
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
        requestQueue.add(stringRequest);

    }

//    boolean show=false;
//    private void performAnim2(){
//        //View是否显示的标志
//        show = !show;
//        //属性动画对象
//        ValueAnimator va ;
//        if(show){
//            //显示view，高度从0变到height值
//            va = ValueAnimator.ofInt(0,200);
//        }else{
//            //隐藏view，高度从height变为0
//            va = ValueAnimator.ofInt(200,0);
//        }
//        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                //获取当前的height值
//                int h =(Integer)valueAnimator.getAnimatedValue();
//                //动态更新view的高度
//                top2.getLayoutParams().height = h;
//                top2.requestLayout();
//            }
//        });
//        va.setDuration(300);
//        //开始动画
//        va.start();
//    }
//
//    //属性动画
//    animatetop = top2.animate();
//
//    LinearLayout	top2;
//    private ViewPropertyAnimator animatetop;
//    public void onScroll() {
//        if (!show) {
//            animatetop.setDuration(300).translationY(-200);
//        }else {
//            animatetop.setDuration(300).translationY(0);
//        }
//
//    }
    //刷新
    public void onEventMainThread(String  refresh) {

        if(ContentValue.RECEIPTSREFRESH.equals(refresh)){
            loadData();

        }
        if(ContentValue.EXCEPTIONREFRESH.equals(refresh)){
            loadData();

        }
        if(ContentValue.QRSHREFRESH.equals(refresh)){
            loadData();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
