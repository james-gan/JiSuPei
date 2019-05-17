package com.jisupei.vpnew.order;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jingchen.pulltorefresh.PullableListView;
import com.jisupei.R;
import com.jisupei.activity.order.bean.ReceivePeople;
import com.jisupei.ywy.BaseFragment;
import com.jisupei.http.HttpBase;
import com.jisupei.http.HttpUtil;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.AutoUtils;
import com.jisupei.utils.SPUtils;
import com.jisupei.vpnew.order.bean.NewVpOrderDetailData;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/18.
 */
public class VpNewOrderDetailFragment extends BaseFragment {



    @InjectView(R.id.order_ListView)
    PullableListView order_ListView;

    public static VpNewOrderDetailFragment getChildOrderFragment() {

        VpNewOrderDetailFragment instance = new VpNewOrderDetailFragment();
        return instance;
    }

    @Override
    public View initView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.acticity_order_details, null);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void initData() {
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(linearLayoutManager);
//        ChildOrderAdapter childOrderAdapter = new ChildOrderAdapter(getActivity(),null);
//        mRecyclerView.setAdapter(childOrderAdapter);

        loadDatanew();

    }


    //加载数据
  public   String  orderCode;
    public   String  sub_status;
    public   String  orderType;
    //加载数据
    public   String  payStatus;
    public void loadDatanew() {
//        orderCode=  getIntent().getStringExtra("orderCode");
        View view = View.inflate(getActivity(), R.layout.include_order_details, null);
        View viewFoot = View.inflate(getActivity(), R.layout.include_order_details_new_foot, null);
        AutoUtils.auto(view);
        //加上头部
        order_ListView.addHeaderView(view);
        AutoUtils.auto(viewFoot);
        order_ListView.addFooterView(viewFoot);
        lodeOrderDetal(view,viewFoot);
    }
    private void lodeOrderDetal(View view,View viewFoot) {
        final LinearLayout yige_ll=  (LinearLayout) view.findViewById(R.id.yige_ll);
        final LinearLayout  sange_ll=  (LinearLayout) view.findViewById(R.id.sange_ll);

        final TextView yige_name=  (TextView) view.findViewById(R.id.yige_name);
        final TextView  yige_phone=  (TextView) view.findViewById(R.id.yige_phone);

        final TextView  name1=  (TextView) view.findViewById(R.id.name1);
        final TextView  phone_et1=  (TextView) view.findViewById(R.id.phone_et1);
        final TextView  name2=  (TextView) view.findViewById(R.id.name2);
        final TextView  phone_et2=  (TextView) view.findViewById(R.id.phone_et2);
        final TextView  name3=  (TextView) view.findViewById(R.id.name3);
        final TextView  phone_et3=  (TextView) view.findViewById(R.id.phone_et3);
        //第三个
        final RelativeLayout rl3=  (RelativeLayout) view.findViewById(R.id.rl3);
        final TextView  order_id_tv=  (TextView) view.findViewById(R.id.order_id_tv);
        final TextView  order_status=  (TextView) view.findViewById(R.id.order_status);
        //下单时间
        final TextView  xdsj_tv=  (TextView) view.findViewById(R.id.xdsj_tv);
        //实际付款
//        final TextView  sjfk_tv=  (TextView) view.findViewById(R.id.sjfk_tv);
        // 运费
        final TextView  yunfei_tv=  (TextView) viewFoot.findViewById(R.id.yunfei_tv);
        //商品总额
        final TextView  spzje_tv=  (TextView) viewFoot.findViewById(R.id.spzje_tv);
        final RelativeLayout  youhuijuan_rl=  (RelativeLayout) viewFoot.findViewById(R.id.youhuijuan_rl);
        final TextView  youhjuan_money=  (TextView) viewFoot.findViewById(R.id.youhjuan_money);
        final RelativeLayout  yfk_rl=  (RelativeLayout) viewFoot.findViewById(R.id.yfk_rl);
        final TextView  yfk_money=  (TextView) viewFoot.findViewById(R.id.yfk_money);


        final RelativeLayout  ytk_rl=  (RelativeLayout) viewFoot.findViewById(R.id.ytk_rl);
        final TextView  ytk_money=  (TextView) viewFoot.findViewById(R.id.ytk_money);

        //留言
        final TextView liuyan_tv=  (TextView) view.findViewById(R.id.liuyan_tv);
        final     View  liuyan_el= view.findViewById(R.id.liuyan_el);
        //优化
        final     View  view_11= view.findViewById(R.id.view_11);
        // 地址
        final TextView  address=  (TextView) view.findViewById(R.id.address);

        //审核人的信息
        final      RelativeLayout  rl_shenghe=  (RelativeLayout) view.findViewById(R.id.rl_shenghe);
        final   RelativeLayout  rl_sh1=  (RelativeLayout) view.findViewById(R.id.rl_sh1);
        final    RelativeLayout  rl_sh2=  (RelativeLayout) view.findViewById(R.id.rl_sh2);

        final    TextView  sh_name1=  (TextView) view.findViewById(R.id.sh_name);
        final    TextView  sh_time1=  (TextView) view.findViewById(R.id.sh_time);
        final    TextView  sh_beizhu1=  (TextView) view.findViewById(R.id.sh_beizhu);
        final    ImageView  sh_status1=  (ImageView) view.findViewById(R.id.sh_status1);

        final   TextView  sh_name2=  (TextView) view.findViewById(R.id.sh_name2);
        final    TextView  sh_time2=  (TextView) view.findViewById(R.id.sh_time2);
        final   TextView  sh_beizhu2=  (TextView) view.findViewById(R.id.sh_beizhu2);
        final    ImageView  sh_status2=  (ImageView) view.findViewById(R.id.sh_status2);


        final    ImageView wzf_status = (ImageView)view.findViewById(R.id.wzf_status);
        final    ImageView  yzf_status = (ImageView)view.findViewById(R.id.yzf_status);

        if("1".equals(HttpBase.isSelf)){//是自营

            if(!"1".equals(payStatus) ){//没有支付,

                wzf_status.setVisibility(View.VISIBLE);
                yzf_status.setVisibility(View.GONE);
            }else { //已经支付
                wzf_status.setVisibility(View.GONE);
                yzf_status.setVisibility(View.VISIBLE);
            }
        }else {//不是自营
            wzf_status.setVisibility(View.GONE);
            yzf_status.setVisibility(View.GONE);
        }

        AppLoading.getInstance().show(getActivity());
        HttpUtil.getInstance().toVpOrderDetail(orderCode, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppLoading.close();
                ToasAlert.toast("连接服务器失败");
            }

            @Override
            public void onResponse(String response, int id) {
                Logger.e("tag", "返回订单列表 ->" + response);
                AppLoading.close();
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String optFlag = object.optString("optFlag");

                if ( "yes".equals(optFlag)) {

                    Gson gson = new Gson();
                    NewVpOrderDetailData mOrderDetailData=    gson.fromJson(object.optString("res"),NewVpOrderDetailData.class);

                    if(mOrderDetailData.skuList!=null&&mOrderDetailData.skuList.size()>0){

                        order_ListView.setAdapter(new OrderDetailsAdapter(getActivity(),mOrderDetailData.skuList));
                    }else{
                        order_ListView.setAdapter(new OrderDetailsAdapter(getActivity(),null));
                    }
                    order_id_tv.setText(""+mOrderDetailData.orderCode);
                    //订单的状态
                    setStatus(order_status,mOrderDetailData.handleStatus);
                    //下单时间
                    xdsj_tv.setText("(下单时间:"+mOrderDetailData.orderSubmitTime+")");
                    //商品总额
                    spzje_tv.setText("￥"+ new DecimalFormat("0.00").format(Double.parseDouble(mOrderDetailData.sumAmount)));

//
//                    if(!TextUtils.isEmpty(mOrderDetailData.coupon_amount)&&!"0".equals(mOrderDetailData.coupon_amount)){
//                        //优惠卷
//                        youhjuan_money.setText("-￥"+ new DecimalFormat("0.00").format(Double.parseDouble(mOrderDetailData.coupon_amount)));
//                        youhuijuan_rl.setVisibility(View.VISIBLE);
//                        yfk_money.setVisibility(View.VISIBLE);
//                        double shiji= HttpBase.sub(Double.parseDouble(mOrderDetailData.sumAmount),Double.parseDouble(mOrderDetailData.coupon_amount));
//                        yfk_money.setText("￥"+ new DecimalFormat("0.00").format(shiji));//应付款
//                        yfk_rl.setVisibility(View.VISIBLE);
//                    }else {
//                        //优惠卷
//                        youhjuan_money.setText("-￥"+ new DecimalFormat("0.00").format(Double.parseDouble("0")));
//                        youhuijuan_rl.setVisibility(View.GONE);
//                        yfk_money.setVisibility(View.GONE);
//                        yfk_rl.setVisibility(View.GONE);
//                    }


                    if(!TextUtils.isEmpty(mOrderDetailData.coupon_amount)&&!"0".equals(mOrderDetailData.coupon_amount)){
                        //优惠卷
                        youhjuan_money.setText("-￥"+ new DecimalFormat("0.00").format(Double.parseDouble(mOrderDetailData.coupon_amount)));
                        youhuijuan_rl.setVisibility(View.VISIBLE);
                        yfk_money.setVisibility(View.VISIBLE);
                        double shiji= HttpBase.sub(Double.parseDouble(mOrderDetailData.sumAmount),Double.parseDouble(mOrderDetailData.coupon_amount));


                        double shiji2= HttpBase.sub(shiji,Double.parseDouble(mOrderDetailData.returnSumAmount));

                        yfk_rl.setVisibility(View.VISIBLE);

                        if(!"0.0".equals(mOrderDetailData.returnSumAmount)&&!"0".equals(mOrderDetailData.returnSumAmount)){

//                            if("1".equals(HttpBase.isSelf_2)){//是自营

                            if(!"1".equals(payStatus) ){//没有支付,
                                ytk_rl.setVisibility(View.GONE);
                                ytk_money.setVisibility(View.GONE);
                                ytk_money.setText("￥"+ new DecimalFormat("0.00").format(0.0));
                                yfk_money.setText("￥"+ new DecimalFormat("0.00").format(shiji));//应付款

                            }else { //已经支付
                                ytk_rl.setVisibility(View.VISIBLE);
                                ytk_money.setVisibility(View.VISIBLE);
                                double tuikuan=  HttpBase.sub( HttpBase.add(Double.parseDouble(mOrderDetailData.returnSumAmount),Double.parseDouble(mOrderDetailData.coupon_amount)),Double.parseDouble(mOrderDetailData.couponValue));



                                ytk_money.setText("￥"+ new DecimalFormat("0.00").format(tuikuan));

                                yfk_money.setText("￥"+ new DecimalFormat("0.00").format(shiji2));//应付款
                            }
//                            }else {//不是自营
//                                ytk_rl.setVisibility(View.GONE);
//                                ytk_money.setVisibility(View.GONE);
//                                ytk_money.setText("￥"+ new DecimalFormat("0.00").format(0.0));
//                            }


                        }else {

                            yfk_money.setText("￥"+ new DecimalFormat("0.00").format(shiji));//应付款
                            ytk_rl.setVisibility(View.GONE);
                            ytk_money.setVisibility(View.GONE);
                            ytk_money.setText("￥"+ new DecimalFormat("0.00").format(0.0));
                        }


                    }else {
                        //优惠卷
                        youhjuan_money.setText("-￥"+ new DecimalFormat("0.00").format(Double.parseDouble("0")));
                        youhuijuan_rl.setVisibility(View.GONE);
                        yfk_money.setVisibility(View.GONE);
                        yfk_rl.setVisibility(View.GONE);
                        ytk_rl.setVisibility(View.GONE);
                        ytk_money.setVisibility(View.GONE);



                    }

                    //运费
                    if(!TextUtils.isEmpty(mOrderDetailData.freight_charges)){

                        yunfei_tv.setText("￥"+new DecimalFormat("0.00").format(Double.parseDouble(mOrderDetailData.freight_charges)));

                        double sum= HttpBase.add(Double.parseDouble(mOrderDetailData.sumAmount),Double.parseDouble(mOrderDetailData.freight_charges));
//                        sjfk_tv.setText("实际付款:￥"+ new DecimalFormat("0.00").format(sum));
                    }else {
                        yunfei_tv.setText("￥0.00");
                        //实际付款
//                        sjfk_tv.setText("实际付款:￥"+ new DecimalFormat("0.00").format(Double.parseDouble(mOrderDetailData.sumAmount)));
                    }
                    //审核人的信息
                       if(mOrderDetailData.auditList!=null &&mOrderDetailData.auditList.size()>0){
                           rl_shenghe.setVisibility(View.VISIBLE);
                           if(mOrderDetailData.auditList.size()==1){
                               rl_sh1.setVisibility(View.VISIBLE);
                               rl_sh2.setVisibility(View.GONE);
                               sh_name1.setText("审核人:"+mOrderDetailData.auditList.get(0).create_by);
                               sh_time1.setText("审核时间:"+mOrderDetailData.auditList.get(0).opt_time);
                               sh_beizhu1.setText("审核备注:"+mOrderDetailData.auditList.get(0).remark);

                               if("T".equals(mOrderDetailData.auditList.get(0).audit_status)||"TT".equals(mOrderDetailData.auditList.get(0).audit_status)){
                                   sh_status1.setImageResource(R.mipmap.tongguo);
                               }else {
                                   sh_status1.setImageResource(R.mipmap.jijue);
                               }

                           }else if(mOrderDetailData.auditList.size()==2){
                               rl_sh1.setVisibility(View.VISIBLE);
                               rl_sh2.setVisibility(View.VISIBLE);
                               //
                               sh_name1.setText("审核人:"+mOrderDetailData.auditList.get(0).create_by);
                               sh_time1.setText("审核时间:"+mOrderDetailData.auditList.get(0).opt_time);
                               sh_beizhu1.setText("审核备注:"+mOrderDetailData.auditList.get(0).remark);

                               sh_name2.setText("审核人:"+mOrderDetailData.auditList.get(1).create_by);
                               sh_time2.setText("审核时间:"+mOrderDetailData.auditList.get(1).opt_time);
                               sh_beizhu2.setText("审核备注:"+mOrderDetailData.auditList.get(1).remark);

                               if("T".equals(mOrderDetailData.auditList.get(0).audit_status)||"TT".equals(mOrderDetailData.auditList.get(0).audit_status)){
                                   sh_status1.setImageResource(R.mipmap.tongguo);
                               }else {
                                   sh_status1.setImageResource(R.mipmap.jijue);
                               }
                               if("T".equals(mOrderDetailData.auditList.get(1).audit_status)||"TT".equals(mOrderDetailData.auditList.get(1).audit_status)){
                                   sh_status2.setImageResource(R.mipmap.tongguo);
                               }else {
                                   sh_status2.setImageResource(R.mipmap.jijue);
                               }

                           }


                       }else {
                           rl_shenghe.setVisibility(View.GONE);
                       }

                    //留言
                    if(!TextUtils.isEmpty(mOrderDetailData.note)){
                        liuyan_tv.setText("留言:"+mOrderDetailData.note);
                        liuyan_el.setVisibility(View.VISIBLE);
                        view_11.setVisibility(View.VISIBLE);
                    }else {
                        liuyan_tv.setText("");
                        liuyan_el.setVisibility(View.GONE);
                        view_11.setVisibility(View.GONE);
                    }
                    //地址的信息
//                    address.setText(mOrderDetailData.receiveAddress);
                    if(!TextUtils.isEmpty(mOrderDetailData.city_cn)){
                        if(mOrderDetailData.city_cn.equals(mOrderDetailData.province_cn)){
                            address.setText(mOrderDetailData.city_cn+mOrderDetailData.receiveAddress);
                        }else {
                            address.setText(mOrderDetailData.province_cn+mOrderDetailData.city_cn+mOrderDetailData.receiveAddress);
                        }
                    }else {
                        address.setText(mOrderDetailData.receiveAddress);
                    }

                    List<ReceivePeople> receiveList = new ArrayList<ReceivePeople>();
                    if(!TextUtils.isEmpty(mOrderDetailData.receiveName)){
                        ReceivePeople mReceivePeople = new ReceivePeople();
                        mReceivePeople.receiveName=mOrderDetailData.receiveName;
                        mReceivePeople.receivePhone=mOrderDetailData.receivePhone;
                        receiveList.add(mReceivePeople);
                    }
                    if(!TextUtils.isEmpty(mOrderDetailData.receive_name2)){
                        ReceivePeople mReceivePeople = new ReceivePeople();
                        mReceivePeople.receiveName=mOrderDetailData.receive_name2;
                        mReceivePeople.receivePhone=mOrderDetailData.receive_phone2;
                        receiveList.add(mReceivePeople);

                    }
                    if(!TextUtils.isEmpty(mOrderDetailData.receive_name3)){
                        ReceivePeople mReceivePeople = new ReceivePeople();
                        mReceivePeople.receiveName=mOrderDetailData.receive_name3;
                        mReceivePeople.receivePhone=mOrderDetailData.receive_phone3;
                        receiveList.add(mReceivePeople);
                    }
                    //判断集合的长度
                    if(receiveList.size()==1){    //
                        yige_ll.setVisibility(View.VISIBLE);
                        sange_ll.setVisibility(View.GONE);
                        yige_name.setText(receiveList.get(0).receiveName);
                        yige_phone.setText(receiveList.get(0).receivePhone);
                    } else if(receiveList.size()==2){  //2个
                        yige_ll.setVisibility(View.GONE);
                        sange_ll.setVisibility(View.VISIBLE);
                        name1.setText(receiveList.get(0).receiveName);
                        phone_et1.setText(receiveList.get(0).receivePhone);
                        name2.setText(receiveList.get(1).receiveName);
                        phone_et2.setText(receiveList.get(1).receivePhone);
                        //第三个
                        rl3.setVisibility(View.GONE);
                    }else if(receiveList.size()==3){  //3 个
                        yige_ll.setVisibility(View.GONE);
                        sange_ll.setVisibility(View.VISIBLE);
                        name1.setText(receiveList.get(0).receiveName);
                        phone_et1.setText(receiveList.get(0).receivePhone);
                        name2.setText(receiveList.get(1).receiveName);
                        phone_et2.setText(receiveList.get(1).receivePhone);
                        name3.setText(receiveList.get(2).receiveName);
                        phone_et3.setText(receiveList.get(2).receivePhone);
                        //第三个
                        rl3.setVisibility(View.VISIBLE);
                    }

                }



            }
        });


    }

    class OrderDetailsAdapter extends BaseAdapter {

        Context context;
        public List<NewVpOrderDetailData.SkuListItem> skuList;
        public     int oldCount;

        public OrderDetailsAdapter(Context context, List<NewVpOrderDetailData.SkuListItem> skuList) {
            this.context = context;
            this.skuList = skuList;
        }

        @Override
        public int getCount() {
            return skuList==null ? 0:skuList.size();
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
            NewVpOrderDetailData.SkuListItem  mSkuListItem = skuList.get(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.adapter_order_details_item, null);

                holder.name_text   =  (TextView)convertView.findViewById(R.id.name_text);
                holder.guige   =  (TextView)convertView.findViewById(R.id.guige);
                holder.shifa_text   =  (TextView)convertView.findViewById(R.id.shifa_text);
                holder.shishou_text   =  (TextView)convertView.findViewById(R.id.shishou_text);
                holder.sl   =  (TextView)convertView.findViewById(R.id.sl);
                holder.sl_zp   =  (TextView)convertView.findViewById(R.id.sl_zp);
                holder.peice_et   =  (TextView)convertView.findViewById(R.id.peice_et);
                holder.product_img   =  (ImageView)convertView.findViewById(R.id.product_img);
//                holder.product_img   =  (SimpleDraweeView)convertView.findViewById(R.id.product_img);

                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }
            //设置图片
//            Uri uri = Uri.parse(HttpUtil.HOST_STRING +"/"+ mSkuListItem.img_path);
//            holder.product_img.setImageURI(uri);
            if(mSkuListItem.unit_price!=null && !"0".equals(mSkuListItem.unit_price)&& !"0.0".equals(mSkuListItem.unit_price)&& !"0.00".equals(mSkuListItem.unit_price)){

                holder.  peice_et.setText("￥" + new DecimalFormat("0.00").format(Double.parseDouble(mSkuListItem.unit_price))+"/"+mSkuListItem.unit);

            }else {
                holder.  peice_et.setText("" );
            }
            // 图片
            boolean  ishasimage= (boolean) SPUtils.get(context,"ishasimage",true);
            if(ishasimage) { //有图
                Picasso.with(context).load(HttpUtil.HOST_STRING +"/"+mSkuListItem.img_path).error(R.mipmap.error).into(holder.product_img);
                holder.product_img.setVisibility(View.VISIBLE);
            }else {//
                holder.product_img.setVisibility(View.GONE);
            }

            holder.name_text.setText(mSkuListItem.skuName);
            if("1".equals(mSkuListItem.equation_factor)||"1.0".equals(mSkuListItem.equation_factor)|| TextUtils.isEmpty(mSkuListItem.equation_factor)){
                   holder.guige.setText("规格:"+mSkuListItem.styleno);
                    holder.sl.setText("请货量:" + mSkuListItem.unit_qty + mSkuListItem.unit);
                if(!TextUtils.isEmpty(mSkuListItem.send_qty)){
                    holder.shifa_text.setText("实发:" + mSkuListItem.send_qty + mSkuListItem.unit);
                    holder.shifa_text.setVisibility(View.VISIBLE);
                }else {
                    holder.shifa_text.setVisibility(View.GONE);
                }
                if(!TextUtils.isEmpty(mSkuListItem.recived_qty)){
                        holder.shishou_text.setText("实收:" + mSkuListItem.recived_qty + mSkuListItem.unit);
                    holder.shishou_text.setVisibility(View.VISIBLE);
                }else {
                    holder.shishou_text.setVisibility(View.GONE);
                }

                //赠品
                if(!TextUtils.isEmpty(mSkuListItem.giftQty) && !"0".equals( mSkuListItem.giftQty)){
                    holder.sl_zp.setText("(赠品:" + mSkuListItem.giftQty + mSkuListItem.uom_default+")");
                    holder.sl_zp.setVisibility(View.VISIBLE);
                }else {
                    holder.sl_zp.setVisibility(View.GONE);
                }

            }else {
                holder. guige.setText("规格:"+mSkuListItem.styleno);
                // 总
                int     dsjjian= (int)HttpBase.div(Double.parseDouble(mSkuListItem.qty),Double.parseDouble(mSkuListItem.equation_factor));
                float     dsdai=   AppUtils.calculateDefault(mSkuListItem.qty,mSkuListItem.equation_factor);
                 holder.sl .setText("请货量:" + mSkuListItem.unit_qty + mSkuListItem.unit );
                //实发
                if(!TextUtils.isEmpty(mSkuListItem.send_qty)){

                    holder.shifa_text.setVisibility(View.VISIBLE);

                    int     sfjjian= (int)HttpBase.div(Double.parseDouble(mSkuListItem.send_qty),Double.parseDouble(mSkuListItem.equation_factor));
                    // 计算主单位
                    float     sfdai=   AppUtils.calculateDefault(mSkuListItem.send_qty,mSkuListItem.equation_factor);
                      holder.shifa_text .setText("实发:" + sfjjian + mSkuListItem.unit );
                }else {
                    holder.shifa_text.setVisibility(View.GONE);
                }
                //实收
                if(!TextUtils.isEmpty(mSkuListItem.recived_qty)){
                    holder.shishou_text.setVisibility(View.VISIBLE);

                    int     ssjjian= (int)HttpBase.div(Double.parseDouble(mSkuListItem.recived_qty),Double.parseDouble(mSkuListItem.equation_factor));
                    // 计算主单位
                    float     ssdai=   AppUtils.calculateDefault(mSkuListItem.recived_qty,mSkuListItem.equation_factor);
                     holder.shishou_text .setText("实收:" + ssjjian + mSkuListItem.unit );

                }else {
                    holder.shishou_text.setVisibility(View.GONE);
                }

                //赠品
                if(!TextUtils.isEmpty(mSkuListItem.giftQty) && !"0".equals( mSkuListItem.giftQty)){

                    int     zpjjian= (int)HttpBase.div(Double.parseDouble(mSkuListItem.giftQty),Double.parseDouble(mSkuListItem.equation_factor));
                    // 计算主单位
                    float     zpdai=   AppUtils.calculateDefault(mSkuListItem.giftQty,mSkuListItem.equation_factor);

                    if(zpjjian==0){
                        holder.sl_zp .setText("(赠品:" +  zpdai + mSkuListItem.uom_default+")");
                    }else if(zpdai==0){
                        holder.sl_zp .setText("(赠品:" + zpjjian + mSkuListItem.assist_unit +")");
                    }else {
                        holder.sl_zp .setText("(赠品:" + zpjjian + mSkuListItem.assist_unit + zpdai + mSkuListItem.uom_default+")");
                    }
                    holder.sl_zp.setVisibility(View.VISIBLE);
                }else {
                    holder.sl_zp.setVisibility(View.GONE);
                }
            }
            if("0".equals(mSkuListItem.qty)||"0.0".equals(mSkuListItem.qty)){
                holder. sl.setVisibility(View.GONE);
            }else {
                holder. sl.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        public final class ViewHolder {
            public TextView name_text;
            public TextView guige;
            public TextView shifa_text;
            public TextView shishou_text;
            public TextView sl ;
            public TextView sl_zp ;
            public TextView peice_et;
            public ImageView product_img;

        }

    }
    public void setStatus(TextView status_tv, String handleStatus) {


//        int yellow= Color.rgb(255, 132, 70);
        int green = Color.rgb(132, 195, 85);
        // final String handStatus = mOrder.handleStatus;
        switch (handleStatus) {
            case "1":

//                    String opt_type = mOrder.opt_type;
//                    if ("T".equals(opt_type)) {
//                        status_tv.setText("待配送");
//                        status_tv.setTextColor(Color.parseColor("#318eff"));
//
//
//                    } else {
                status_tv.setText("待配送");
                status_tv.setTextColor(Color.parseColor("#318eff"));

//                    }

                break;
            case "2":
                status_tv.setText("配送中");
                status_tv.setTextColor(Color.parseColor("#318eff"));

                break;
            case "3":
                status_tv.setTextColor(green);
                status_tv.setText("已签收");
                break;
            case "4":
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
    }


}
