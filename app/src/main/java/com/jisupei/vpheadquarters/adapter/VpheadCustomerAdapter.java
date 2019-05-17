package com.jisupei.vpheadquarters.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.jisupei.R;
import com.jisupei.activity.order.bean.ReceivePeople;
import com.jisupei.utils.db.DatabaseHelper;
import com.jisupei.ywy.customer.bean.Customer;
import com.jisupei.http.HttpUtil;
import com.jisupei.activity.base.model.Product;
import com.jisupei.utils.AutoUtils;
import com.jisupei.vpheadquarters.VpHeadEditsCustomerActicity;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.orhanobut.logger.Logger;
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
 * Created by Administrator on 2016/7/26.
 */
public class VpheadCustomerAdapter extends  RecyclerView.Adapter<VpheadCustomerAdapter.MyViewHolder> {


    Context context;
    OnItemListener mOnItemListener;
    List<Customer> mCustomerList;

    public VpheadCustomerAdapter(Context context , OnItemListener mOnItemListener, List<Customer> mCustomerList) {

            this.context=context;

            this.mCustomerList=mCustomerList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View view=   LayoutInflater.from(context).inflate(R.layout.vphead_customer_fragment_item, null, false);
        AutoUtils.auto(view);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Customer mCustomer   =  mCustomerList.get(position);
        holder.shop_name.setText( mCustomer.shop_name);
            holder.dcai_bt.setVisibility(View.VISIBLE);
        if("N".equals(mCustomer.is_enable)){
            holder.dcai_bt.setText("启用");
        }else {
            holder.dcai_bt.setText("停用");

         }
        holder.beiji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent= new Intent(context,VpHeadEditsCustomerActicity.class);
                intent.putExtra("shopId",mCustomer.shopId);
                ( (Activity)context).startActivity(intent);
            }
        });
        holder.dcai_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.update_dialog);
                builder.setTitle("提示");
                builder.setMessage("你确定修改客户状态吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Dao<Product,Integer> cartDao = DatabaseHelper.getHelper(context).getCartDao();
                            List<Product> data= cartDao.queryBuilder().orderBy("id1", false).query();
                            cartDao.delete(data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        if("停用".equals( holder.dcai_bt.getText().toString())){


                            AppLoading.show(context);
                            HttpUtil.getInstance().updateShopChangeStatus(mCustomer.shopId,"Y", new StringCallback() {//停用
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    AppLoading.close();
                                    ToasAlert.toastCenter("连接服务器失败");
                                }
                                @Override
                                public void onResponse(String response, int id) {
                                    AppLoading.close();
                                    Logger.d("app2.0", response);
                                    JSONObject object = null;
                                    try {                                    object = new JSONObject(response);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    String optFlag = object.optString("optFlag");
                                    if ("yes".equals(optFlag)) {
                                        //获取门店的登录数据
                                        EventBus.getDefault().post("VpheadCustomerFragment");
                                    }else {
                                        ToasAlert.toastCenter( object.optString("optDesc"));
                                    }
                                }

                            });



                        }else {   //启用

                            AppLoading.show(context);
                            HttpUtil.getInstance().updateShopChangeStatus(mCustomer.shopId,"N", new StringCallback() {//停用
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    AppLoading.close();
                                    ToasAlert.toastCenter("连接服务器失败");
                                }
                                @Override
                                public void onResponse(String response, int id) {
                                    AppLoading.close();
                                    Logger.d("app2.0", response);
                                    JSONObject object = null;
                                    try {                                    object = new JSONObject(response);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    String optFlag = object.optString("optFlag");
                                    if ("yes".equals(optFlag)) {
                                        //获取门店的登录数据
                                        ToasAlert.toastCenter( object.optString("msg"));
                                        EventBus.getDefault().post("VpheadCustomerFragment");
                                    }else {
//                                    ToasAlert.toastCenter("授权门店登录失败！");
                                        ToasAlert.toastCenter( object.optString("optDesc"));
                                    }
                                }

                            });


                        }




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

        holder.address.setText(mCustomer.address);
        List<ReceivePeople> receiveList = new ArrayList<ReceivePeople>();
        if(!TextUtils.isEmpty(mCustomer.contact_name)){
            ReceivePeople mReceivePeople = new ReceivePeople();
            mReceivePeople.receiveName=mCustomer.contact_name;
            mReceivePeople.receivePhone=mCustomer.contact_phone;
            receiveList.add(mReceivePeople);
        }
        if(!TextUtils.isEmpty(mCustomer.contact_name2)){
            ReceivePeople mReceivePeople = new ReceivePeople();
            mReceivePeople.receiveName=mCustomer.contact_name2;
            mReceivePeople.receivePhone=mCustomer.contact_phone2;
            receiveList.add(mReceivePeople);
        }
        if(!TextUtils.isEmpty(mCustomer.contact_name3)){
            ReceivePeople mReceivePeople = new ReceivePeople();
            mReceivePeople.receiveName=mCustomer.contact_name3;
            mReceivePeople.receivePhone=mCustomer.contact_phone3;
            receiveList.add(mReceivePeople);
        }
        //判断集合的长度
        if(receiveList.size()==1){    //
            holder.yige_ll.setVisibility(View.VISIBLE);
            holder. sange_ll.setVisibility(View.GONE);
            holder.yige_name.setText(receiveList.get(0).receiveName);
            holder. yige_phone.setText(receiveList.get(0).receivePhone);
        } else if(receiveList.size()==2){  //2个
            holder. yige_ll.setVisibility(View.GONE);
            holder. sange_ll.setVisibility(View.VISIBLE);
            holder. name1.setText(receiveList.get(0).receiveName);
            holder. phone_et1.setText(receiveList.get(0).receivePhone);
            holder. name2.setText(receiveList.get(1).receiveName);
            holder. phone_et2.setText(receiveList.get(1).receivePhone);
            //第三个
            holder.  rl3.setVisibility(View.GONE);
        }else if(receiveList.size()==3){  //3 个
            holder.  yige_ll.setVisibility(View.GONE);
            holder.  sange_ll.setVisibility(View.VISIBLE);
            holder.  name1.setText(receiveList.get(0).receiveName);
            holder.  phone_et1.setText(receiveList.get(0).receivePhone);
            holder.   name2.setText(receiveList.get(1).receiveName);
            holder.   phone_et2.setText(receiveList.get(1).receivePhone);
            holder.  name3.setText(receiveList.get(2).receiveName);
            holder. phone_et3.setText(receiveList.get(2).receivePhone);
            //第三个
            holder.  rl3.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemCount() {

        return  mCustomerList.size();
    }
    static class MyViewHolder  extends RecyclerView.ViewHolder{

        @InjectView(R.id.shop_name)
         TextView shop_name;
        @InjectView(R.id.address)
         TextView address;
        @InjectView(R.id.yige_ll)
        LinearLayout yige_ll;
        @InjectView(R.id.sange_ll)
        LinearLayout sange_ll;
 
        @InjectView(R.id.yige_name)
        TextView yige_name;
        @InjectView(R.id.beiji)
        ImageView beiji;
        @InjectView(R.id.yige_phone)
        TextView yige_phone;
        @InjectView(R.id.dcai_bt)
        Button dcai_bt;

        @InjectView(R.id.name1)
        TextView name1;
        @InjectView(R.id.phone_et1)
        TextView phone_et1;
        @InjectView(R.id.name2)
        TextView name2;
        @InjectView(R.id.phone_et2)
        TextView phone_et2;

        @InjectView(R.id.name3)
        TextView name3;
        @InjectView(R.id.phone_et3)
        TextView phone_et3;
        //第三个
        @InjectView(R.id.rl3)
        RelativeLayout rl3;
        public   MyViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    public interface    OnItemListener {
        void   setPinPaiClick(String brand_nameAndId);

    }
}

