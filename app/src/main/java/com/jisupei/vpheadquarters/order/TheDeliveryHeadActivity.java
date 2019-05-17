package com.jisupei.vpheadquarters.order;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import com.jisupei.R;
import com.jisupei.activity.homepage2.DetailNewActivity;
import com.jisupei.activity.homepage2.MainActivity11;
import com.jisupei.application.MyApplication;
import com.jisupei.ywy.BaseActivity;
import com.jisupei.http.HttpUtil;
import com.jisupei.activity.base.model.Product;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.AutoUtils;
import com.jisupei.utils.StatusBarUtil;
import com.jisupei.vpheadquarters.order.bean.TheDeliveryHead;
import com.jisupei.vpheadquarters.order.bean.OrderPaidIn;
import com.jisupei.vpheadquarters.order.bean.PaidInParams;
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
public class TheDeliveryHeadActivity extends BaseActivity {

    @InjectView(R.id.back_bt)
    ImageView back_bt;
    @InjectView(R.id.listview)
    PullableListView listview;
    @InjectView(R.id.queding)
    Button queding;

    @InjectView(R.id.pullToRefreshLayout)
    public PullToRefreshLayout pullToRefreshLayout;
    //刷新
    public static  boolean  shRef = true;
    String ordercode;
    @Override
    public void setLayoutContentView(Bundle savedInstanceState) {
        setContentView(R.layout.fahuo_collection);
        ButterKnife.inject(this);
        StatusBarUtil.setStatusBarColor(this,R.color.white);
        int a2=  StatusBarUtil.StatusBarLightMode(TheDeliveryHeadActivity.this);
        if(a2==0){
            StatusBarUtil.setStatusBarColor(TheDeliveryHeadActivity.this,R.color.black);
        }
        MyApplication.instance.addActivity(this);

        EventBus.getDefault().register(this);
        ordercode = getIntent().getStringExtra("ordercode");
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
        pullToRefreshLayout.setPullDownEnable(false);
        AutoUtils.auto(pullToRefreshLayout.defaultRefreshView);

    }
    public void onEventMainThread(String refresh) {


    }

    int pageno=1;
    String classId="";
    String searchText="";

    @Override
    public void initData() {

          load(null);



        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TheDeliveryHeadActivity.this,R.style.update_dialog);

                builder.setMessage("确认实收吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                      cancelOrder(mOrder.orderCode);this






                        PaidInParams mPaidInParams = new PaidInParams();
                        mPaidInParams.orderCode=order_code;

                        mPaidInParams.skus.clear();
                        for (OrderPaidIn mOrderPaidIn :arrProduct) {
                            PaidInParams.SkuTemp mSkuTemp = mPaidInParams.new SkuTemp();
                            mSkuTemp.id= mOrderPaidIn.id;
                            mSkuTemp.real_amount= mOrderPaidIn.real_amount;
                            mSkuTemp.sum_amount= mOrderPaidIn.sum_amount;
                            mPaidInParams.skus.add(mSkuTemp);

                        }
                        AppLoading.show(TheDeliveryHeadActivity.this);
                        HttpUtil.getInstance().saveAppRealAmount(mPaidInParams,new StringCallback() {
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

                                if("yes".equals(optFlag)) {

                                    ToasAlert.toastCenter(object.optString("data"));
                                    finish();
                                }else {
                                    ToasAlert.toastCenter(object.optString("data"));
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
        });
    }

    List<OrderPaidIn> 	   arrProduct;
    String   order_code;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.instance.removeActivity(this);
    }
    public  void load(final PullToRefreshLayout pullToRefreshLayout){

        AppLoading.show(this);
        HttpUtil.getInstance().appVpOrderLineList(ordercode,new StringCallback() {
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

                if("yes".equals(optFlag)) {



                    loadHead(object);
                    loadfoot(object);
                   String list = object.optString("orderSku");
            	   arrProduct = new Gson().fromJson(list, new TypeToken<ArrayList<OrderPaidIn>>(){}.getType());
                        order_code = object.optJSONObject("orderLine").optString("order_code");

                    ProductShiShouAdapter mProductShiShouAdapter = new ProductShiShouAdapter(TheDeliveryHeadActivity.this,arrProduct);
                    listview.setAdapter(mProductShiShouAdapter);


                }

            }
        });
    }


 public  void  loadHead(JSONObject object){

     View view= View.inflate(this,R.layout.fahuo_item_head,null);
        listview.addHeaderView(view);

     LinearLayout a1=(LinearLayout) view.findViewById(R.id.a1);
     LinearLayout a2=(LinearLayout) view.findViewById(R.id.a2);
     LinearLayout a3=(LinearLayout) view.findViewById(R.id.a3);
     final ImageView a1_iv=(ImageView) view.findViewById(R.id.a1_iv);
     final ImageView a2_iv=(ImageView) view.findViewById(R.id.a2_iv);
     final ImageView a3_iv=(ImageView) view.findViewById(R.id.a3_iv);

     final RelativeLayout cl_rl=(RelativeLayout) view.findViewById(R.id.cl_rl);
     final View cl_rl_view=(View) view.findViewById(R.id.fa_view);

     final TextView    fhcl=(TextView) view.findViewById(R.id.fhcl);
     final TextView    fhck=(TextView) view.findViewById(R.id.fhck);

     a1.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             a1_iv.setImageResource(R.mipmap.list_icon_choose1);
             a2_iv.setImageResource(R.mipmap.list_icon_default1);
             a3_iv.setImageResource(R.mipmap.list_icon_default1);
             cl_rl.setVisibility(View.VISIBLE);
             cl_rl_view.setVisibility(View.VISIBLE);
         }
     });

     a2.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             a2_iv.setImageResource(R.mipmap.list_icon_choose1);
             a1_iv.setImageResource(R.mipmap.list_icon_default1);
             a3_iv.setImageResource(R.mipmap.list_icon_default1);
             cl_rl.setVisibility(View.GONE);
             cl_rl_view.setVisibility(View.GONE);
         }
     });
     a3.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             a3_iv.setImageResource(R.mipmap.list_icon_choose1);
             a1_iv.setImageResource(R.mipmap.list_icon_default1);
             a2_iv.setImageResource(R.mipmap.list_icon_default1);
             cl_rl.setVisibility(View.GONE);
             cl_rl_view.setVisibility(View.GONE);
         }
     });

     final List<String> fhclStr=new ArrayList<>(); //发货车辆

     final List<String> fhckStr=new ArrayList<>(); //发货仓库


     fhcl.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             AlertDialog.Builder builder = new AlertDialog.Builder(TheDeliveryHeadActivity.this);
             builder.setTitle("请选择");

              final String[]  fhclArr=  fhclStr.toArray(new String[]{});
             builder.setItems(fhclArr, new DialogInterface.OnClickListener()
             {
                 @Override
                 public void onClick(DialogInterface dialog, int which)
                 {
                     fhcl.setText(fhclArr[which]);
                 }
             });
             builder.show();
         }
     });
     fhck.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             AlertDialog.Builder builder = new AlertDialog.Builder(TheDeliveryHeadActivity.this);
             builder.setTitle("请选择");

             final String[]  fhckArr=  fhckStr.toArray(new String[]{});
             builder.setItems(fhckArr, new DialogInterface.OnClickListener()
             {
                 @Override
                 public void onClick(DialogInterface dialog, int which)
                 {

                     fhck.setText(fhckArr[which]);
                 }
             });
             builder.show();
         }
     });

     AppLoading.show(this);    //加载头部的信息
     HttpUtil.getInstance().initAppSendOrder(new StringCallback() {
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
             String optFlag = object.optString("success");

             if("yes".equals(optFlag)) {
                 String data = object.optString("data");
                 Gson gson=new Gson();
                 TheDeliveryHead mTheDeliveryHead = gson.fromJson(data, TheDeliveryHead.class);
                 if(mTheDeliveryHead.carList!=null && mTheDeliveryHead.carList.size()>0){
                     for (TheDeliveryHead.CarTemp mCarTemp: mTheDeliveryHead.carList) {

                         fhclStr.add(mCarTemp.car_name);

                     }
                 }
                 if(mTheDeliveryHead.myWmList!=null && mTheDeliveryHead.myWmList.size()>0){
                     for (TheDeliveryHead.MyWmTemp mMyWmTemp: mTheDeliveryHead.myWmList) {

                         fhckStr.add(mMyWmTemp.wm_nm);

                     }
                 }


             }

         }
     });


 }

    TextView    beizhu;
    public  void  loadfoot(JSONObject object){

        View view= View.inflate(this,R.layout.fahuo_item_foot,null);
        listview.addFooterView(view);
             beizhu=(TextView) view.findViewById(R.id.beizhu);

    }


    public class ProductShiShouAdapter extends BaseAdapter {
        Context context;

        LayoutInflater inflate;

        List<OrderPaidIn> 		arrProduct;
        Dao<Product,Integer> cartDao;
        String eventMessage;

        public ProductShiShouAdapter(Context context,    List<OrderPaidIn> 	   arrProduct ){
            this.context=context;
            this.arrProduct=arrProduct;

            inflate=LayoutInflater.from(context);

        }


        public final class ViewHolder{
            public ImageView img;
            public ImageView collection_img;
            public TextView name_text;
            public TextView rank_text;
            public TextView num_text;
            public TextView price_text;

//            public TextView  xuangou;
//            public TextView  ck_text;
         public TextView       shifa;
            public TextView       shishou;
        }
        public int oldCount;


        @Override
        public int getCount() {
            return arrProduct.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final OrderPaidIn dish = arrProduct.get(position);
            final ViewHolder holder;


            if (convertView == null) {

                holder=new ViewHolder();
                convertView = inflate.inflate(R.layout.confirm_received_item, null);
                holder.img = (ImageView)convertView.findViewById(R.id.product_img);
                holder.collection_img = (ImageView)convertView.findViewById(R.id.collect);
                holder.name_text = (TextView)convertView.findViewById(R.id.name_text);
                holder.rank_text = (TextView)convertView.findViewById(R.id.rank_text);
                holder.num_text = (TextView)convertView.findViewById(R.id.num_text);
                holder.price_text = (TextView)convertView.findViewById(R.id.price_text);
                holder.   shifa=(TextView)convertView.findViewById(R.id.shifa);
                holder.   shishou=(TextView)convertView.findViewById(R.id.shishou);
                convertView.setTag(holder);

            }else {
                holder = (ViewHolder)convertView.getTag();
            }

           holder.price_text.setText("￥"+dish.unit_price+"/"+dish.unit); //价格

            holder.name_text.setText(dish.sku_name);
            if("0".equals(dish.equation_factor)||"1".equals(dish.equation_factor)||"1.0".equals(dish.equation_factor)||TextUtils.isEmpty(dish.equation_factor)){
                holder.rank_text.setText("规格:"+dish.styleno);
            }else { //二个 单位
                String unit = dish.unit;
                holder.rank_text.setText("规格:" + dish.styleno + "(1" + unit + "=" + dish.equation_factor + dish.uom_default + ")");
            }

             holder.num_text.setText("请货量:" + dish.unitqty + dish.unit);
             holder.shifa.setText("实发:" + dish.sendqty + dish.unit);
             holder.shishou.setText("实收:" + dish.recivedqty + dish.unit);

                Picasso.with(context).load(HttpUtil.HOST_STRING +"/"+ dish.img_path).error(R.mipmap.error).into(holder.img);
                holder.img.setVisibility(View.VISIBLE);


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //详情页面
                    Intent intent = new Intent();
                    intent.putExtra("productId", dish.id);
                    intent.putExtra("dish",dish);
                    intent.setClass(context, DetailNewActivity.class);
                    context.startActivity(intent);
                }
            });
            return convertView;
        }




 //

    }


}
