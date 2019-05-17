package com.jisupei.vpheadquarters.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
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
import com.jisupei.R;
import com.jisupei.activity.datail.H5orderActivity;
import com.jisupei.activity.order.bean.NemuItem;
import com.jisupei.activity.order.widget.SelectOrderPopupwindow;
import com.jisupei.ywy.BaseFragment;
import com.jisupei.ywy.HomePageActivity;
import com.jisupei.ywy.order.activity.SalesmanOrderDetailsFragmentActicity;
import com.jisupei.ywy.order.activity.SearchNameOrderActivity;
import com.jisupei.ywy.order.activity.ShenHeGoodsActivity;
import com.jisupei.http.HttpBase;
import com.jisupei.http.HttpUtil;
import com.jisupei.utils.AutoUtils;
import com.jisupei.vpheadquarters.bean.OrderVp;
import com.jisupei.vpheadquarters.datil.VpHeadOrderDetailsFragmentActicity;
import com.jisupei.vpheadquarters.order.Confirm_ReceivedActivity;
import com.jisupei.vpheadquarters.order.TheDeliveryHeadActivity;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
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
 * Created by Administrator on 2016/9/18.
 */
public class VpheadOrderFragmentNew extends BaseFragment {


    @InjectView(R.id.order_ListView)
    public PullableListView order_ListView;
//    @InjectView(R.id.back_bt)
//    ImageView backBt;
    @InjectView(R.id.textView)
    TextView textView;
    @InjectView(R.id.textView_iv)
    ImageView textView_iv;
    @InjectView(R.id.search_iv)
    ImageView searchIv;
    @InjectView(R.id.top_layout)
    RelativeLayout topLayout;
    @InjectView(R.id.ck_rl)
    RelativeLayout ck_rl;
    @InjectView(R.id.ck)
    TextView ck;
    @InjectView(R.id.root)
    LinearLayout root;

    @InjectView(R.id.pullToRefreshLayout)
    public PullToRefreshLayout pullToRefreshLayout;

    public static VpheadOrderFragmentNew getOrderFragmentInstance() {
        VpheadOrderFragmentNew instance = new VpheadOrderFragmentNew();
        return instance;
    }

     public void setingDefault(){
         pageno = 1;
         handleStatus=null;
         defaultButton=1;
         lodeOrder(currCompanyId,handleStatus ,pageno, 3);
         textView.setText("全部订单");
     }
    @Override
    public View initView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.vpheand_activity_order_all, null);
        ButterKnife.inject(this, rootView);
//        findViewIdHead(rootView);
//        include_title_tv.setText("订单");
//       AutoUtils.auto(rootView);

        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void initData() {
        pageno=1;
        lodeCK();//加载仓库

        setListener();
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }
  public  int defaultButton = 1;
    //默认
    String handleStatus=null;

    SelectOrderPopupwindow mSelectOrderPopupwindow;
    public void setListener() {

        ck_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("请选择");

                final String[]  ckNamesArr=  ckNames.toArray(new String[]{});
                builder.setItems(ckNamesArr, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
//                     Toast.makeText(TheDeliveryHeadActivity.this, "车辆为：" + fhclArr[which], Toast.LENGTH_SHORT).show();
                        ck.setText(ckNamesArr[which]);
                        currCompanyId=ids.get(which);
                        pageno=1;
                        lodeOrder(currCompanyId,handleStatus ,pageno, 3);

                    }
                });
                builder.show();
            }
        });






        searchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), SearchNameOrderActivity.class);
                getActivity().overridePendingTransition(0,0);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(0,0);
            }
        });

        pullToRefreshLayout.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pageno=1;
                lodeOrder(currCompanyId,handleStatus ,pageno,1);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

                lodeOrder(currCompanyId,handleStatus ,pageno,2);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectOrderPopupwindow =new SelectOrderPopupwindow(  defaultButton,getActivity(), new SelectOrderPopupwindow.MyitemsOnClick() {
                    @Override
                    public void bit1Click(View v) {
                        pageno = 1;
                        handleStatus=null;
                        lodeOrder(currCompanyId,handleStatus ,pageno, 3);
                        mSelectOrderPopupwindow.dismiss();
                        textView.setText("全部订单");
                        defaultButton=1;
                    }
                    @Override
                    public void bit2Click(View v) { //审核
                        pageno = 1;
                        handleStatus="8,9";
                        lodeOrder(currCompanyId, handleStatus,pageno, 3);
                        mSelectOrderPopupwindow.dismiss();
                        textView.setText("待审核订单");
                        defaultButton=2;
                    }
                    @Override
                    public void bit3Click(View v) {
                        pageno = 1;
                        handleStatus="1";
                        lodeOrder( currCompanyId,handleStatus,pageno, 3);
                        mSelectOrderPopupwindow.dismiss();
                        textView.setText("待配送订单");
                        defaultButton=3;
                    }

                    @Override
                    public void bit4Click(View v) {
                        pageno = 1;
                        handleStatus="2";
                        lodeOrder(currCompanyId,handleStatus,pageno, 3);
                        mSelectOrderPopupwindow.dismiss();
                        textView.setText("配送中订单");
                        defaultButton=4;
                    }

                    @Override
                    public void bit5Click(View v) {
                        pageno = 1;
                        handleStatus="3";
                        lodeOrder(currCompanyId,handleStatus ,pageno, 3);
                        mSelectOrderPopupwindow.dismiss();

                        textView.setText("已签收订单");
                        defaultButton=5;
                    }

                    @Override
                    public void bit6Click(View v) {
                        pageno = 1;
                        handleStatus="4";
                        lodeOrder(currCompanyId,handleStatus ,pageno, 3);
                        mSelectOrderPopupwindow.dismiss();
                        textView.setText("已取消订单");
                        defaultButton=6;

                    }
                });

                mSelectOrderPopupwindow.showAsDropDown(topLayout);
            }
        });
    }




    int pageno = 1;
    String isAudit;
    AllOrderAdapter mAllOrderAdapter;
    private void lodeOrder(String companyId, String handleStatus,final int pagenoTemp, final int isPull) {
        AppLoading.show(getContext());
        HttpUtil.getInstance().appVpOrderList(companyId,null,handleStatus,pagenoTemp ,  new StringCallback() {
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

//                        JSONObject object1 = object.getJSONObject("res");
                        isAudit = object.optString("isAudit");
                        final ArrayList<OrderVp> orderListTemp = new Gson().fromJson(object.getJSONArray("order").toString(), new TypeToken<ArrayList<OrderVp>>() {
                        }.getType());

                        if (pagenoTemp == 1) {
                            Logger.e("tag", "订单列表长度orderList" + orderListTemp.size());
                            if(orderListTemp==null||orderListTemp.size()==0){
                                mAllOrderAdapter = new AllOrderAdapter(getActivity(), null);
                                order_ListView.setAdapter(mAllOrderAdapter);
                                ToasAlert.toast("没有查询到数据");
                            }
                            mAllOrderAdapter = new AllOrderAdapter(getActivity(), orderListTemp);
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


    List<String> ids= new ArrayList<String>();
    List<String> ckNames= new ArrayList<String>();
    String currCompanyId;
    private void lodeCK() {
        AppLoading.show(getContext());
        HttpUtil.getInstance().initShopPageMsg( new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //   Logger.e("TAG",e);
                AppLoading.close();
                ToasAlert.toast("连接服务器失败");

            }

            @Override
            public void onResponse(String response, int id) {


                Logger.e("tag", "返回订单列表 ->" + response);
                try {

                    AppLoading.close();

                    JSONObject object = new JSONObject(response);

                    String optFlag = object.optString("optFlag");

                    if ( "yes".equals(optFlag)) {
                        JSONArray companyList = object.optJSONObject("res").optJSONArray("companyList");
                            for(int i=0;i<companyList.length();i++){
                                try {
                                    JSONObject  itemObj=   (JSONObject)companyList.get(i);
                                      ids.add( itemObj.optString("id")) ;
                                      ckNames.add( itemObj.optString("nm")) ;




                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        currCompanyId=((JSONObject)companyList.get(0)).optString("id");
                        ck.setText(((JSONObject)companyList.get(0)).optString("nm"));
                        pageno=1;
                        lodeOrder(currCompanyId,null ,pageno, 3);

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
        List<OrderVp> mOrderlist;
        public     int oldCount;

        public AllOrderAdapter(Context context, List<OrderVp> mOrderlist) {
            this.context = context;
            this.mOrderlist = mOrderlist;
        }
        public void addList( List<OrderVp> mOrderlistAdd){
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
            final OrderVp mOrder = mOrderlist.get(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.vphead_adapter_all_order_item, null);
                AutoUtils.auto(convertView);
                holder.shop_name = (TextView) convertView.findViewById(R.id.shop_name);
                holder.ck_name = (TextView) convertView.findViewById(R.id.ck_name);
                holder.order_code_tv = (TextView) convertView.findViewById(R.id.order_code_tv1);
                holder.status = (TextView) convertView.findViewById(R.id.status);
                holder.category_count = (TextView) convertView.findViewById(R.id.category_count);
                holder.order_price_tv = (TextView) convertView.findViewById(R.id.order_price_tv);
                holder.order_time = (TextView) convertView.findViewById(R.id.order_time);
                holder.freight_price = (TextView) convertView.findViewById(R.id.freight_price);
//                holder.more_button = (TextView) convertView.findViewById(R.id.more_button);
                holder.menuFu = (LinearLayout) convertView.findViewById(R.id.menuFu);
                holder.menuFu1 = (LinearLayout) convertView.findViewById(R.id.menuFu1);
                holder.menuFu2 = (LinearLayout) convertView.findViewById(R.id.menuFu2);
                holder.henxian_flag3 = convertView.findViewById(R.id.henxian_flag3);
                //点击进入订单详情
                holder.rl1 = (RelativeLayout)convertView.findViewById(R.id.rl1);

//                holder.status_image1 = (ImageView)convertView.findViewById(R.id.status_image1);

                holder.status_image2 = (ImageView)convertView.findViewById(R.id.status_image2);
                holder.wzf_status = (ImageView)convertView.findViewById(R.id.wzf_status);
                holder.yzf_status = (ImageView)convertView.findViewById(R.id.yzf_status);
                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context,OrderDetailsActicity.class);
//                    Intent intent = new Intent(context,OrderDetailsNewFragmentActicity.class);
//                    intent.putExtra("orderCode",mOrder.orderCode);
//                    context.startActivity(intent);
                }
            });
            holder.ck_name.setText(mOrder.wm_name==null?"":mOrder.wm_name);
            holder.shop_name.setText(mOrder.shop_name);
            holder.order_code_tv.setText("订单编号:"+ mOrder.order_code);

            holder.category_count.setText("采购种类:"+mOrder.skuNum + "");
            if(mOrder.sum_amount!=null && !"0".equals(mOrder.sum_amount) && !"0.0".equals(mOrder.sum_amount)&& !"0.00".equals(mOrder.sum_amount)){
                holder.order_price_tv.setText("订单总额:"+"￥" + new DecimalFormat("0.00").format(Double.parseDouble(mOrder.sum_amount)));
            }else {
                holder.order_price_tv.setText("");
            }

            holder.order_time.setText(mOrder.create_time);
//            if(!TextUtils.isEmpty(mOrder.freight_charges)){
//                holder.freight_price.setText("运费:￥"+mOrder.freight_charges);
//                holder.freight_price.setVisibility(View.VISIBLE);
//            }else {

                holder.freight_price.setVisibility(View.GONE);
//            }
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

            setStatus( holder.status, mOrder, convertView, holder. menuFu,position);
            List<NemuItem> nemuItemList = new ArrayList<NemuItem>();
            if("待配送".equals(holder.status.getText().toString())){
                NemuItem     nemuItemDps  = new NemuItem("发货",R.mipmap.xiangqing);
                nemuItemList.add(nemuItemDps);
                NemuItem     nemuItemss  = new NemuItem("确认实收",R.mipmap.xiangqing);
                nemuItemList.add(nemuItemss);

            }
            if("配送中".equals(holder.status.getText().toString())){
                NemuItem     nemuItemwc  = new NemuItem("订单完成",R.mipmap.xiangqing);
                nemuItemList.add(nemuItemwc);
                NemuItem     nemuItemss  = new NemuItem("确认实收",R.mipmap.xiangqing);
                nemuItemList.add(nemuItemss);

            }
//            if(!"4".equals(mOrder.handleStatus) ){   //取消订单
//
//                if(!"1".equals(mOrder.payStatus)&& "1".equals(HttpBase.isSelf_2)){//没有支付,是自营
//                    NemuItem     nemuItemzf  = new NemuItem("立即支付",R.mipmap.zf);
//                    nemuItemList.add(nemuItemzf);
//                }
//            }
//           if(!"4".equals(mOrder.handleStatus) ){   //取消订单

            if("1".equals(HttpBase.isSelf_2)){//是自营

                if(!"1".equals(mOrder.pay_type) ){//没有支付,

                    if(!"4".equals(mOrder.handle_status) ){   //取消订单
//                        NemuItem     nemuItemzf  = new NemuItem("立即支付",R.mipmap.zf);
//                        nemuItemList.add(nemuItemzf);
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




//            if(mOrder.orderCode.contains("A")){
//                NemuItem     nemuItem1  = new NemuItem("历史订单",R.mipmap.lsdd);
//                nemuItemList.add(nemuItem1);
//                //图片
//
//                holder.status_image2.setVisibility(View.VISIBLE);
//            }else {

                holder.status_image2.setVisibility(View.GONE);
//            }


            //取消订单
            final String handStatus = mOrder.handle_status;
//            switch (handStatus) {
//                case "1":
//                    String opt_type = mOrder.isAudit;
//                    if ("yes".equals(opt_type)) { //审核通过
//                    } else {//没审核
//                        NemuItem     nemuItem1  = new NemuItem("取消订单",R.mipmap.canalorder);
//                        nemuItemList.add(nemuItem1);
//                    }
//                    break;
//
//                case "9":  //取消订单
//                    NemuItem     nemuItems  = new NemuItem("审核订单",R.mipmap.shenheorder);
//                    nemuItemList.add(nemuItems);
//                    NemuItem     nemuItem1  = new NemuItem("取消订单",R.mipmap.canalorder);
//                    nemuItemList.add(nemuItem1);
//
//                    break;
//
//                default:
//                    break;
//            }

            //查看物流
//            if(mOrder.viewLogisCase==null||Integer.parseInt(mOrder.viewLogisCase)==0){
//
//            }else  if(Integer.parseInt(mOrder.viewLogisCase)==1){
//                NemuItem     nemuItem1  = new NemuItem("查看物流",R.mipmap.ckwl);
//                nemuItemList.add(nemuItem1);
//            }else {
//                NemuItem     nemuItem1  = new NemuItem("查看物流",R.mipmap.ckwl);
//                nemuItemList.add(nemuItem1);
//
//            }
//            //查看异常
//            if(mOrder.viewErrorCase==null||Integer.parseInt(mOrder.viewErrorCase)==0){
//                holder.status_image1.setVisibility(View.GONE);
//
//            }else  if(Integer.parseInt(mOrder.viewErrorCase)==1){
//                holder.status_image1.setVisibility(View.VISIBLE);
//                NemuItem     nemuItem1  = new NemuItem("查看异常",R.mipmap.scyc);
//                nemuItemList.add(nemuItem1);
//            }else {
//                holder.status_image1.setVisibility(View.VISIBLE);
//                NemuItem     nemuItem1  = new NemuItem("查看异常",R.mipmap.scyc);
//                nemuItemList.add(nemuItem1);
//            }
//            //查看回单
//            if(mOrder.isExistReceipt==null||Integer.parseInt(mOrder.isExistReceipt)==0){
//
//            }else  if(Integer.parseInt(mOrder.isExistReceipt)==1){
//                NemuItem     nemuItem1  = new NemuItem("查看回单",R.mipmap.ckhd);
//                nemuItemList.add(nemuItem1);
//            }else {
//                NemuItem     nemuItem1  = new NemuItem("查看回单",R.mipmap.ckhd);
//                nemuItemList.add(nemuItem1);
//            }

//            //上传异常
//            if(Integer.parseInt(mOrder.upErrorCase)==0){
//
//            }else  if(Integer.parseInt(mOrder.upErrorCase)==1){
//                NemuItem     nemuItem1  = new NemuItem("上传异常",R.mipmap.scyc);
//                nemuItemList.add(nemuItem1);
//            }else {
//                NemuItem     nemuItem1  = new NemuItem("上传异常",R.mipmap.scyc);
//                nemuItemList.add(nemuItem1);
//            }

//            //上传回单
//            if(Integer.parseInt(mOrder.upReceiptCase)==0){
//
//            }else  if(Integer.parseInt(mOrder.upReceiptCase)==1){
//                NemuItem     nemuItem1  = new NemuItem("上传回单",R.mipmap.up_hd);
//                nemuItemList.add(nemuItem1);
//            }else {
//                NemuItem     nemuItem1  = new NemuItem("上传回单",R.mipmap.up_hd);
//                nemuItemList.add(nemuItem1);
//            }

//            //确认收货
//            if(Integer.parseInt(mOrder.receiptCase)==0){
//
//            }else  if(Integer.parseInt(mOrder.receiptCase)==1){
//                NemuItem     nemuItem1  = new NemuItem("确认收货",R.mipmap.qrsh_img);
//                nemuItemList.add(nemuItem1);
//            }else {
//                NemuItem     nemuItem1  = new NemuItem("确认收货",R.mipmap.qrsh_img);
//                nemuItemList.add(nemuItem1);
//            }

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

                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.update_dialog);

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
                            if("查看详情".equals(mNemuItem.nemuName)){
                                Intent intent = new Intent(context,VpHeadOrderDetailsFragmentActicity.class);
                                intent.putExtra("orderCode",mOrder.order_code);
                                intent.putExtra("payStatus",mOrder.pay_status);
                                context.startActivity(intent);

                            }
                            else
                            if("审核订单".equals(mNemuItem.nemuName)){
                                Intent intent = new Intent(context,ShenHeGoodsActivity.class);
                                intent.putExtra("orderCode",mOrder.order_code);
                                context.startActivity(intent);

                            }
                            else
                            if("订单完成".equals(mNemuItem.nemuName)){
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.update_dialog);

                                builder.setMessage("确认完成吗？");
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        AppLoading.show(getContext());
                                        HttpUtil.getInstance().orderCompletion(mOrder.order_code,  new StringCallback() {
                                            @Override
                                            public void onError(Call call, Exception e, int id) {
                                                //   Logger.e("TAG",e);
                                                AppLoading.close();
                                                ToasAlert.toast("连接服务器失败");

                                            }

                                            @Override
                                            public void onResponse(String response, int id) {

                                                Logger.e("tag", "返回订单列表 ->" + response);
                                                try {

                                                    AppLoading.close();

                                                    JSONObject object = new JSONObject(response);

                                                    String optFlag = object.optString("success");

                                                    if ( "yes".equals(optFlag)) {
                                                        ToasAlert.toast(object.optString("data"));
                                                        pageno=1;
                                                        lodeOrder(currCompanyId,handleStatus ,pageno, 3);  //加载数据


                                                    } else {
                                                        ToasAlert.toast(object.optString("data"));
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Logger.e("TAG", "json解析异常");
                                                    ToasAlert.toast("获取数据发生错误");
                                                }
                                            }
                                        });


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
                            if("确认实收".equals(mNemuItem.nemuName)){
                                Intent intent = new Intent(context,Confirm_ReceivedActivity.class);
                                intent.putExtra("ordercode",mOrder.order_code);
                                startActivity(intent);

                            }
                            else
                            if("发货".equals(mNemuItem.nemuName)){
                                Intent intent = new Intent(context,TheDeliveryHeadActivity.class);
                                intent.putExtra("ordercode",mOrder.order_code);
                                startActivity(intent);

                            }

//                            else if("确认收货".equals(mNemuItem.nemuName)){
//
//                                if("1".equals(mOrder.receiptCase)){
////                                         ToasAlert.toastCenter("确认收货");
//                                    Intent intent = new Intent(context,ConfirmGoodsActivity.class);
//                                    intent.putExtra("count","1");
//                                    intent.putExtra("orderCode",mOrder.orderCode);
//                                    startActivity(intent);
//                                }else {  //多个确认收货
////                                         ToasAlert.toastCenter("多个确认收货");
//                                    Intent intent = new Intent(context,ConfirmGoodsWaybillSelectActivity.class);
//
//                                    intent.putExtra("orderCode",mOrder.orderCode);
//                                    startActivity(intent);
//                                }
//                            }

//                            else if("上传回单".equals(mNemuItem.nemuName)){
//                                if("1".equals(mOrder.upReceiptCase)){
////                                         ToasAlert.toastCenter("上传回单");
//                                    Intent intent = new Intent(context,LogisticsReceiptAddImgActivity.class);
//                                    intent.putExtra("count","1");
//                                    intent.putExtra("orderCode",mOrder.orderCode);
//                                    startActivity(intent);
//                                }else {  //多个回单
////                                         ToasAlert.toastCenter("多个上传回单");
//                                    Intent intent = new Intent(context,ReceiptWaybillSelectAddActivity.class);
//                                    intent.putExtra("orderCode",mOrder.orderCode);
//                                    startActivity(intent);
//                                }
//                            }

//                            else if("上传异常".equals(mNemuItem.nemuName)){
//
//                                if("1".equals(mOrder.upErrorCase)){
////                                         ToasAlert.toastCenter("上传异常");

//                                    intent.putExtra("count","1");
//                                    intent.putExtra("orderCode",mOrder.orderCode);
//                                    startActivity(intent);
//
//                                }else { //多个上传异常
////                                         ToasAlert.toastCenter("多个上传异常");
//                                    Intent intent = new Intent(context,ExceptionUpWaybillSelectActivity.class);
//                                    intent.putExtra("orderCode",mOrder.orderCode);
//                                    startActivity(intent);
//                                }
//
//                            }

                            else if("查看回单".equals(mNemuItem.nemuName)){

                                Intent intent = new Intent(context,SalesmanOrderDetailsFragmentActicity.class);
                                intent.putExtra("orderCode",mOrder.order_code);
                                intent.putExtra("flag","3");
                                startActivity(intent);

                            }else if("查看异常".equals(mNemuItem.nemuName)){

                                Intent intent = new Intent(context,SalesmanOrderDetailsFragmentActicity.class);
                                intent.putExtra("orderCode",mOrder.order_code);
                                intent.putExtra("flag","4");
                                startActivity(intent);

                            }else if("查看物流".equals(mNemuItem.nemuName)){

                                Intent intent = new Intent(context,SalesmanOrderDetailsFragmentActicity.class);
                                intent.putExtra("orderCode",mOrder.order_code);
                                intent.putExtra("flag","2");
                                startActivity(intent);

                            }  else if("立即支付".equals(mNemuItem.nemuName)){
                                Intent i= new Intent(context, H5orderActivity.class);

                                i.putExtra("orderCode",mOrder.order_code);
                                context.startActivity(i);
                            }

                        }
                    });

                }
            }



            return convertView;
        }

        public final class ViewHolder {
            public TextView order_code_tv;
            public TextView shop_name;
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
            public    TextView   ck_name;

            public    ImageView   wzf_status;
            public    ImageView   yzf_status;
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
                        if(position==mOrderlist.size()-1){
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

   public static  String OrderFragmentResh = "OrderFragmentResh";
    //从首页待审核
   public static  String fromHomeDaiShenHeResh = "fromHomeDaiShenHeResh";
    //待配送
   public static  String fromHomeDaiPeiSongResh = "fromHomeDaiPeiSongResh";
    public void onEventMainThread(String  refresh) {
        if(OrderFragmentResh.equals(refresh)){  //审核 ShenHeGoodsActivity
            pageno=1;
            lodeOrder(currCompanyId,handleStatus ,pageno,1);
            return;
        }
        if(fromHomeDaiShenHeResh.equals(refresh)){// 首页
            pageno = 1;
            handleStatus="8,9";
            lodeOrder(currCompanyId, handleStatus,pageno, 3);
            textView.setText("待审核订单");
            defaultButton=2;
            return;
        }
        if(fromHomeDaiPeiSongResh.equals(refresh)){//// 首页
            pageno = 1;
            handleStatus="1";
            lodeOrder(currCompanyId, handleStatus,pageno, 3);

            textView.setText("待配送订单");
            defaultButton=3;
            return;
        }
    }

    //取消订单
    public void cancelOrder(final String orderCode){
//     AppLoading.show(this.getContext());

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

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
                                lodeOrder(currCompanyId,handleStatus ,pageno, 3);  //加载数据

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
                    object.put("owner", HttpBase.owner_2);
                    object.put("account", HttpBase.account_2);
                    object.put("orderCode", orderCode);
                    object.put("deviceOS","android");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Map<String, String> map = new HashMap<>();
                map.put("content", object.toString());
                map.put("version","1");
                map.put("login_token",HttpBase.token_2);
                map.put("deviceOS","android-"+android.os.Build.VERSION.RELEASE);
                return map;
            }
        };
        //超时时间10s,最大重连次数2次
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
        requestQueue.add(stringRequest);

    }
}
