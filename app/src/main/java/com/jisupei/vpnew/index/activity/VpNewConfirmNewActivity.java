package com.jisupei.vpnew.index.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.j256.ormlite.dao.Dao;
import com.jisupei.R;
import com.jisupei.activity.datail.H5orderActivity;
import com.jisupei.activity.homepage2.MainActivity11;
import com.jisupei.activity.shippingaddress.DefaultContactShop;
import com.jisupei.activity.shippingaddress.ShippingAddressSubmitActicity;
import com.jisupei.application.MyApplication;
import com.jisupei.utils.db.DatabaseHelper;
import com.jisupei.http.HttpBase;
import com.jisupei.http.HttpUtil;
import com.jisupei.activity.base.model.ShopAddress;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.AutoUtils;
import com.jisupei.vpnew.VpHomePageActivity;
import com.jisupei.vpnew.db.VpNewProductDb;
import com.jisupei.vpnew.index.adapter.ProductConfirmAdapter;
import com.jisupei.vpnew.index.model.ProductVpNewSearch;
import com.jisupei.vpnew.index.widget.PagePayPop;
import com.jisupei.vpnew.index.widget.CouponsPop;
import com.jisupei.vpnew.setting.cart.bean.Coupon;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by xiayumo on 16/5/12.
 */
public class VpNewConfirmNewActivity extends Activity {

    ListView confirm_list;
    ProductConfirmAdapter adapter1;
    ImageView back_bt;
    Button submit_bt;
    TextView num_text,price_text;
    EditText edit;
    List<ProductVpNewSearch> listObj;
    String  summoney;
    public static final String CONFIRM_REQUEST = "/appVpOrder/submitOrder.do";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newvp_confirm);
        MyApplication.instance.addActivity(this);
        back_bt= (ImageView)findViewById(R.id.back_bt);
        AppUtils.expandViewTouchDelegate( findViewById(R.id.back_bt), 30, 30, 50, 50);
       Intent intent=  getIntent();
         listObj =  (ArrayList<ProductVpNewSearch>) intent.getSerializableExtra("arrProduct");
          summoney =  intent.getStringExtra("summoney");
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submit_bt= (Button) findViewById(R.id.submit_bt);
        initList();
        submit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView       price_text1= price_text;
               String      price_textSVal   =  price_text1.getText().toString();
                if(!TextUtils.isEmpty(price_textSVal)&&price_textSVal.substring(1).length()==11){
                     int num=   Integer.parseInt(price_textSVal.substring(1,2));
                    if(num>=5){

                        ToasAlert.toast("合计金额过大!");
                        return;
                    }
                } else if(!TextUtils.isEmpty(price_textSVal)&&price_textSVal.substring(1).length()>11){
                    ToasAlert.toast("合计金额过大!");
                    return;
                }
                submitOrder();

            }
        });

    }

    String  contact_name ;
    String  contact_phone ;
    String  contact_name2;
     String  contact_phone2;
    String  contact_name3;
    String  contact_phone3;
    String  addressVal;
    private void submitOrder(){
        if(TextUtils.isEmpty(addressSend)){
             ToasAlert.toastCenter("请完善地址信息");
            loadData();
            return;
        }
        if(sange_ll.getVisibility()==View.GONE){
            contact_name=yige_name.getText().toString();
            contact_phone=yige_phone.getText().toString();
            addressVal = address.getText().toString();
        }else {
            contact_name = name1.getText().toString().trim();
            contact_phone = phone_et1.getText().toString().trim();

            contact_name2 =  name2.getText().toString().trim();
            contact_phone2 =  phone_et2.getText().toString().trim();
            contact_name3 =  name3.getText().toString().trim();
            contact_phone3 = phone_et3.getText().toString().trim();
            addressVal = address.getText().toString();
        }
            AppLoading.show(this);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUtil.HOST_STRING +
                    CONFIRM_REQUEST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Logger.e("tag", "订单提交结果 ->" + response);

                            AppLoading.close();

                            try {

                                JSONObject object =new JSONObject(response);

                                String flag = object.optString("optFlag");

                                if(flag.equals("yes")) {
                                    String orderCode = object.optJSONObject("res").optString("orderCode");
                                    deleteCartProduct(listObj);
                                    Intent i= new Intent(VpNewConfirmNewActivity.this, H5orderActivity.class);
                                    i.putExtra("orderCode",orderCode);
                                    VpNewConfirmNewActivity.this.startActivity(i);
                                    finish();
                                }else {
                                    ToasAlert.toastCenter(object.optString("optDesc"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Logger.e("TAG", "json解析异常");
                                ToasAlert.toast("提交订单发生错误！");
                            }

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.e("TAG", "返回失败"+error.getMessage(), error);
                    ToasAlert.toastCenter("连接服务器失败，请稍后再试！");
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    //在这里设置需要post的参数
                    JSONObject object = new JSONObject();
                    try {
                        JSONArray skuArray=new JSONArray();

                        object.put("companyId", HttpBase.companyId);
                        object.put("owner", HttpBase.owner);
                        object.put("wmCode", HttpBase.wmCode);
                        object.put("shopId", HttpBase.shopId);
                        object.put("account", HttpBase.account);
                        object.put("receiveName", contact_name);
                        object.put("receivePhone", contact_phone);

                        object.put("receiveName2", contact_name2);
                        object.put("receivePhone2", contact_phone2);

                        object.put("receiveName3", contact_name3);
                        object.put("receivePhone3", contact_phone3);
                        if(jphwl.isChecked()){
                            object.put("receiveAddress",  addressSend);
                            object.put("shopProvinceCode",  shopProviceCode);
                            object.put("shopProvinceName",  shopProviceName);
                            object.put("shopCityCode",  shopCityCode);
                            object.put("shopCityName",  shopCityName);
                        }else {
                            object.put("receiveAddress",  address.getText().toString());
                            object.put("shopProvinceCode",  wmProvince);
                            object.put("shopProvinceName",  wmProvinceName);
                            object.put("shopCityCode",  wmCity);
                            object.put("shopCityName",  wmCityName);

                        }
                        object.put("note", edit.getText().toString() );
                        object.put("createBy", HttpBase.account);
                        Logger.e("tag",edit.getText().toString());

                            //添加sku
                            for( ProductVpNewSearch mProductVpNewSearch : listObj){
                                JSONObject object1 = new JSONObject();
                                object1.put("skuCode", mProductVpNewSearch.sku_code);
                                object1.put("qty", queryProductDbNum(mProductVpNewSearch.id));
                                object1.put("wmCode",mProductVpNewSearch.wm_code);
                                object1.put("price",mProductVpNewSearch.unit_price);
                                skuArray.put(object1);
                            }

                        object.put("skus", skuArray);

                        if("在线支付".equals(sizaixian.getText().toString())){
                            object.put("paymentMethod", "0");
                        }else {
                            object.put("paymentMethod", "1"); //货到付款
                        }
                        if(!TextUtils.isEmpty(idYHJParam)){
                            object.put("couponId", idYHJParam);
                        }
                        if(!TextUtils.isEmpty(moneyYHJParam)){
                            object.put("couponAmount", moneyYHJParam);
                        }

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
    String noteLXR="";
    RadioGroup mRadioGroup;
    TextView   dikou;
    TextView   sizaixian;
    TextView   YhjMoney;
    RelativeLayout   dikou_rl;
    TextView   qulinqu;
    RadioButton  jphwl;
    RadioButton  zt;
    RelativeLayout   yhj_rl;
    RelativeLayout   yhj_no;
    RelativeLayout   yhj_has;
      TextView  cha_money;
      TextView  coudan1_4;
    private void initList() {
        num_text= (TextView) findViewById(R.id.num_text);
        price_text= (TextView) findViewById(R.id.price_text);
        confirm_list= (ListView) findViewById(R.id.page_tab_listview);

        // 地址
        View view = View.inflate(this, R.layout.acticity_addhead_submit_order_new, null);
          jphwl= (RadioButton) view.findViewById(R.id.jphwl);
            zt= (RadioButton) view.findViewById(R.id.zt);

        mRadioGroup= (RadioGroup) view.findViewById(R.id.mRadioGroup);
        AutoUtils.auto(view);
        confirm_list.addHeaderView(view);
        loadaHead(view);
        //加载地址数据
        loadData();
        View footer_view= LayoutInflater.from(this).inflate(R.layout.new_vp_footer,null);
        edit= (EditText) footer_view.findViewById(R.id.edit);
        yhj_no= (RelativeLayout) footer_view.findViewById(R.id.yhj_no);
        yhj_has= (RelativeLayout) footer_view.findViewById(R.id.yhj_has);
         cha_money= (TextView) footer_view.findViewById(R.id.cha_money);


        TextView   money= (TextView) footer_view.findViewById(R.id.money);
        //优惠卷
           yhj_rl= (RelativeLayout) footer_view.findViewById(R.id.yhj_rl);

       dikou= (TextView) footer_view.findViewById(R.id.dikou);
        coudan1_4= (TextView) footer_view.findViewById(R.id.coudan1_4);
        sizaixian= (TextView) footer_view.findViewById(R.id.sizaixian); //支付方式
        YhjMoney= (TextView) footer_view.findViewById(R.id.YhjMoney);

        YhjMoney= (TextView) footer_view.findViewById(R.id.YhjMoney);



//           miaoshu= (TextView) footer_view.findViewById(R.id.miaoshu);//
        qulinqu= (TextView) footer_view.findViewById(R.id.qulinqu);
        qulinqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(VpNewConfirmNewActivity.this,VpNewCouponsCenterActivity.class);
                    VpNewConfirmNewActivity.this.startActivity(intent);



            }
        });

        coudan1_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                     //去凑单
                    Intent intent = new Intent(VpNewConfirmNewActivity.this,VpHomePageActivity.class);

                    VpNewConfirmNewActivity.this.startActivity(intent);
                    EventBus.getDefault().post("to2");


            }
        });

        final RelativeLayout   zhifu= (RelativeLayout) footer_view.findViewById(R.id.zhifu);
        final RelativeLayout   dikou_rl= (RelativeLayout) footer_view.findViewById(R.id.dikou_rl);

        zhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                backgroundAlpha(0.5f);
                final PagePayPop mPopupWindow = new PagePayPop(VpNewConfirmNewActivity.this,hdfkFlag, hdfkFlagStr);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                        WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                mPopupWindow.showAtLocation(zhifu,  Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//				  mPopupWindow.showAsDropDown(right_listview);

                mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });
                mPopupWindow.setOnRefresh(new PagePayPop.Refresh() {
                    @Override
                    public void queding(int curr) {
                        mPopupWindow.dismiss();
                        if(curr==1){
                            sizaixian.setText("在线支付");
                        }else{
                            sizaixian.setText("货到付款");
                        }
                    }
                });

            }
        });

        //抵扣
        dikou_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ids1= "";
                for (ProductVpNewSearch mProductVpNewSearch: listObj) {
                    ids1+=mProductVpNewSearch.class_id+",";
                }
                ids1= ids1.substring(0,ids1.length()-1);
                backgroundAlpha(0.5f);
                final CouponsPop mPopupWindow = new CouponsPop(VpNewConfirmNewActivity.this,ids1,idYHJ);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                        WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                mPopupWindow.showAtLocation(dikou_rl,  Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//				  mPopupWindow.showAsDropDown(right_listview);

                mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });
                mPopupWindow.setOnRefresh(new CouponsPop.Refresh() {
                    @Override
                    public void queding(Coupon mCoupon) {

                        YhjMoney.setText("-￥"+mCoupon.couponValue);
                      dikou.setText("优惠"+mCoupon.couponValue+"元");

                        String shijiMoney= ""+HttpBase.sub(Double.parseDouble(summoney),Double.parseDouble(mCoupon.couponValue));
                        price_text.setText("￥"+shijiMoney);


                        idYHJParam=mCoupon.ID;
                        moneyYHJParam=mCoupon.couponValue;

//                        mPopupWindow.dismiss();
//                        if(curr==1){
////                            sizaixian.setText("在线支付");
//                        }else{
////                            sizaixian.setText("货到付款");
//                        }
                    }
                });

            }
        });

        money.setText("￥"+summoney);
       // 其他联系人:张三18023233344/李四13899997777
        if(!TextUtils.isEmpty(HttpBase.contactName2) && !TextUtils.isEmpty(HttpBase.contactName3)){
            noteLXR="(其他联系人:"+HttpBase.contactName2+HttpBase.contactPhone2 +" "+ HttpBase.contactName3+HttpBase.contactPhone3+")";
        }
        if(!TextUtils.isEmpty(HttpBase.contactName2) && TextUtils.isEmpty(HttpBase.contactName3)){
            noteLXR="(其他联系人:"+HttpBase.contactName2+HttpBase.contactPhone2 + ")";
        }
        if(TextUtils.isEmpty(HttpBase.contactName2) && !TextUtils.isEmpty(HttpBase .contactName3)){
            noteLXR="(其他联系人:"+HttpBase.contactName3+HttpBase.contactPhone3 + ")";
        }
        confirm_list.addFooterView(footer_view);
        adapter1=new ProductConfirmAdapter(this,listObj);
        confirm_list.setAdapter(adapter1);


         num_text.setText(listObj.size()+"");

         price_text.setText("￥" + summoney);

        loadZhiTi(); //自提的地址

        loadHasCart();  //查询可用的优惠卷

        loadHasHDFk();  //查询是否货到付款

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.jphwl: //首页
                        address.setText(currAddress);
                        break;
                    case R.id.zt: //分类
                         address.setText(wmAddr);
                        break;


                }


            }
        });



    }
    String wmAddr;  //自己提的地址


    String wmProvinceName;  //自己提的地址
    String wmProvince;  //自己提的地址
    String wmCity;  //自己提的地址
    String wmCityName;//

    public void   loadZhiTi( ){
        AppLoading.show(this);
        HttpUtil.getInstance().appCompanyWm(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppLoading.close();
                ToasAlert.toastCenter("连接服务器失败，请稍后再试！");
            }
            @Override
            public void onResponse(String response, int id) {
                AppLoading.close();
                Logger.d("app2.0", response);
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String optFlag = object.optString("optFlag");

                if ("yes".equals(optFlag)) {
                    wmAddr= object.optJSONObject("res").optJSONObject("companywm").optString("wmAddr");
                    wmProvinceName= object.optJSONObject("res").optJSONObject("companywm").optString("wmProvinceName");
                    wmProvince= object.optJSONObject("res").optJSONObject("companywm").optString("wmProvince");
                    wmCity= object.optJSONObject("res").optJSONObject("companywm").optString("wmCity");
                    wmCityName= object.optJSONObject("res").optJSONObject("companywm").optString("wmCityName");

                }else {
                    ToasAlert.toastCenter(object.optString("optDesc"));
                }
            }
        });


    }
    String  idYHJ;
    String moneyYHJ;
    String moneyYHJParam;
    String idYHJParam;
    //查询有没优惠卷
    public void   loadHasCart(){
        AppLoading.show(this);
        String ids1= "";
        for (ProductVpNewSearch mProductVpNewSearch: listObj) {
            ids1+=mProductVpNewSearch.class_id+",";
        }
        ids1= ids1.substring(0,ids1.length()-1);

        HttpUtil.getInstance().getMaxValueCouponApp(ids1,summoney,new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppLoading.close();
                ToasAlert.toastCenter("连接服务器失败，请稍后再试！");
            }
            @Override
            public void onResponse(String response, int id) {
                AppLoading.close();
                Logger.d("app2.0", response);
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String optFlag = object.optString("optFlag");

                if ("yes".equals(optFlag)) {
//                    wmAddr= object.optJSONObject("res").optJSONObject("companywm").optString("wmAddr");
                    idYHJ= object.optJSONObject("res").optString("ID");
                    moneyYHJ= object.optJSONObject("res").optString("couponValue");

                    idYHJParam=idYHJ;
                    moneyYHJParam=moneyYHJ;

                    YhjMoney.setText("-￥"+moneyYHJ);
                 dikou.setText("优惠"+moneyYHJ+"元");
                    dikou.setTextColor(getResources().getColor(R.color.blue_head));

                    String shijiMoney= ""+HttpBase.sub(Double.parseDouble(summoney),Double.parseDouble(moneyYHJ));
                    price_text.setText("￥"+shijiMoney);


//                     miaoshu.setText("您可以使用更多优惠卷");
//                     coudan.setText("去领取");

                    yhj_rl.setVisibility(View.VISIBLE);//显示金额

                    yhj_has.setVisibility(View.VISIBLE); //有 优惠卷
                    yhj_no.setVisibility(View.GONE);   //没优惠卷的头部




                }else {//无可用的优惠卷
//                    ToasAlert.toastCenter(object.optString("optDesc"));

                        if(object.optJSONObject("res")==null){

                            dikou.setText("不可用");
                            dikou.setTextColor(Color.parseColor("#666666"));

                            yhj_rl.setVisibility(View.GONE); //没优惠卷
                            yhj_no.setVisibility(View.GONE);//没优惠卷  还差
                            yhj_has.setVisibility(View.VISIBLE);//


                        }else {

                            String      minMoney= object.optJSONObject("res").optString("minMoney");
                            double cha= HttpBase.sub(Double.parseDouble(minMoney),Double.parseDouble(summoney));
                            cha_money.setText(cha+"元"); //还差
                            dikou.setText("不可用");
                            dikou.setTextColor(Color.parseColor("#666666"));
                            yhj_rl.setVisibility(View.GONE); //没优惠卷

                            yhj_no.setVisibility(View.VISIBLE);//没优惠卷  还差
                            yhj_has.setVisibility(View.GONE);//

                        }


                }
            }
        });


    }
    public void onEventMainThread(String  refresh) {
        if("getCart".equals(refresh)){
            loadHasCart();
        }

    }
    boolean hdfkFlag=true;
    String hdfkFlagStr="";
    //查看是不是不能货到付款
    public void   loadHasHDFk(){
        AppLoading.show(this);
//        String ids1= "";
//        for (ProductVpNewSearch mProductVpNewSearch: listObj) {
//            ids1+=mProductVpNewSearch.class_id+",";
//        }
//        ids1= ids1.substring(0,ids1.length()-1);

        HttpUtil.getInstance().checkPaymentMethod(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppLoading.close();
                ToasAlert.toastCenter("连接服务器失败，请稍后再试！");
            }
            @Override
            public void onResponse(String response, int id) {
                AppLoading.close();
                Logger.d("app2.0", response);
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String optFlag = object.optString("optFlag");

                if ("yes".equals(optFlag)) {
//                    wmAddr= object.optJSONObject("res").optJSONObject("companywm").optString("wmAddr");
                    hdfkFlag=true;
                }else {//无可用的优惠卷
//                    ToasAlert.toastCenter(object.optString("optDesc"));
//                 dikou.setText("不可用");
                    hdfkFlag=false;
                    hdfkFlagStr = object.optString("optDesc");

                }
            }
        });


    }
    LinearLayout     yige_ll;
    LinearLayout   sange_ll;
    TextView   address;
    TextView   name1;
    TextView   phone_et1;
    TextView   name2;
    TextView   phone_et2;
    TextView   name3;
    TextView   phone_et3;
    TextView   yige_name;
    TextView   yige_phone;
    RelativeLayout rl3;
    public void   loadaHead(View view ){
          yige_ll  =  (LinearLayout)findViewById(R.id.yige_ll);
          sange_ll  =  (LinearLayout)   findViewById(R.id.sange_ll);
          rl3  =  (RelativeLayout)   findViewById(R.id.rl3);
           address        =  (TextView)   findViewById(R.id.address);
//        ToasAlert.toastCenter(address.getText().toString());
           yige_name         =  (TextView)   findViewById(R.id.yige_name);
          yige_phone         =  (TextView)   findViewById(R.id.yige_phone);

           name1         =  (TextView)   findViewById(R.id.name1);
           phone_et1     =  (TextView)   findViewById(R.id.phone_et1);
           name2        =  (TextView)   findViewById(R.id.name2);
           phone_et2     =  (TextView)   findViewById(R.id.phone_et2);
           name3         =  (TextView)   findViewById(R.id.name3);
           phone_et3     =  (TextView)   findViewById(R.id.phone_et3);

        ImageView   goto_address     =  (ImageView)   findViewById(R.id.goto_address);
        if("2".equals(HttpBase.companyType)){
            goto_address.setVisibility(View.VISIBLE);
        }else {
            goto_address.setVisibility(View.GONE);

        }

       view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if("2".equals(HttpBase.companyType)){
                   Intent intent = new Intent(VpNewConfirmNewActivity.this,ShippingAddressSubmitActicity.class);
                   intent.putExtra("position",position);
                   VpNewConfirmNewActivity.this.startActivityForResult(intent,200);
               }else {

               }



           }
       });

    }

    int position=-1;

    String currAddress;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(requestCode==200 &&  data != null){
            ShopAddress   mShopAddress = (ShopAddress) data.getSerializableExtra("ADDRESS");
             String   shop =   data.getStringExtra("shop");
                 position =   data.getIntExtra("position",-1);

            if(mShopAddress!=null){  // 其他地址
                if (TextUtils.isEmpty(mShopAddress.contact_name2) && TextUtils.isEmpty(mShopAddress.contact_name3)) {
                    sange_ll.setVisibility(View.GONE);
                    yige_ll.setVisibility(View.VISIBLE);
                    yige_name.setText(mShopAddress.contact_name);
                    yige_phone.setText(mShopAddress.contact_phone);


                    name1.setText("");
                    phone_et1.setText("");
                    name2.setText("");
                    phone_et2.setText("");
                    name3.setText("");
                    phone_et3.setText("");

                } else if (!TextUtils.isEmpty(mShopAddress.contact_name2) && TextUtils.isEmpty(mShopAddress.contact_name3)) {


                    name1.setText(mShopAddress.contact_name);
                    phone_et1.setText(mShopAddress.contact_phone);

                    name2.setText(mShopAddress.contact_name2);
                    phone_et2.setText(mShopAddress.contact_phone2);

                    sange_ll.setVisibility(View.VISIBLE);
                    yige_ll.setVisibility(View.GONE);
                    rl3.setVisibility(View.GONE); //隐藏第三个
                    name3.setText("");
                    phone_et3.setText("");

                } else if (TextUtils.isEmpty(mShopAddress.contact_name2) && !TextUtils.isEmpty(mShopAddress.contact_name3)) {

                    name1.setText(mShopAddress.contact_name);
                    phone_et1.setText(mShopAddress.contact_phone);
                    name2.setText(mShopAddress.contact_name3);
                    phone_et2.setText(mShopAddress.contact_phone3);

                    sange_ll.setVisibility(View.VISIBLE);
                    yige_ll.setVisibility(View.GONE);
                    rl3.setVisibility(View.GONE); //隐藏第三个
                    name3.setText("");
                    phone_et3.setText("");
                } else if (!TextUtils.isEmpty(mShopAddress.contact_name2) && !TextUtils.isEmpty(mShopAddress.contact_name3)) {
                    name1.setText(mShopAddress.contact_name);
                    phone_et1.setText(mShopAddress.contact_phone);

                    name2.setText(mShopAddress.contact_name2);
                    phone_et2.setText(mShopAddress.contact_phone2);
                    name3.setText(mShopAddress.contact_name3);
                    phone_et3.setText(mShopAddress.contact_phone3);


                    sange_ll.setVisibility(View.VISIBLE);
                    yige_ll.setVisibility(View.GONE);
                    rl3.setVisibility(View.VISIBLE); //显示第三个
                }

            if(mShopAddress.province_id.equals(mShopAddress.city_id)){
                address.setText(TextUtils.isEmpty(mShopAddress.province)?"":mShopAddress.province + (TextUtils.isEmpty(mShopAddress.contact_address)?"":mShopAddress.contact_address));
            }else {

                address.setText(TextUtils.isEmpty(mShopAddress.province)?"":mShopAddress.province  +  (TextUtils.isEmpty(mShopAddress.city)?"":mShopAddress.city) + (TextUtils.isEmpty(mShopAddress.contact_address)?"":mShopAddress.contact_address));
            }
                currAddress=address.getText().toString();

                addressSend          =   mShopAddress.contact_address;
                shopProviceCode     =     mShopAddress.province_id;
                shopProviceName     =     mShopAddress.province;
                shopCityCode         =        mShopAddress.city_id;
                shopCityName         =        mShopAddress.city;


            }else { //选择的是 默认的地址

                DefaultContactShop      mDefaultContactShop=  new Gson().fromJson(shop,DefaultContactShop.class);
                 loadDefaultAddress(mDefaultContactShop);
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public void loadData() {
        AppLoading.show(this);
        HttpUtil.getInstance().appShopAddressList(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppLoading.close();
                ToasAlert.toastCenter("连接服务器失败，请稍后再试！");
            }
            @Override
            public void onResponse(String response, int id) {
                AppLoading.close();
                Logger.d("app2.0", response);
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String optFlag = object.optString("optFlag");

                if ("yes".equals(optFlag)) {
                    String    shop = object.optString("shop");
                    DefaultContactShop      mDefaultContactShop=  new Gson().fromJson(shop,DefaultContactShop.class);
                    if(mDefaultContactShop==null){
                        ToasAlert.toastCenter("获取数据错误,请重新登录！");
                        return;
                    }
                    loadDefaultAddress(mDefaultContactShop);

                }else {
                    ToasAlert.toastCenter(object.optString("optDesc"));
                }
            }
        });
    }



    public  void  loadDefaultAddress(DefaultContactShop mDefaultContactShop ){
        String    shopTact2Temp  =   mDefaultContactShop.shopTact2==null?"":mDefaultContactShop.shopTact2;
        String    shopTact3Temp  =   mDefaultContactShop.shopTact3==null?"":mDefaultContactShop.shopTact3;
        if (TextUtils.isEmpty(shopTact2Temp.trim()) && TextUtils.isEmpty(shopTact3Temp.trim())) {
            sange_ll.setVisibility(View.GONE);
            yige_ll.setVisibility(View.VISIBLE);
            yige_name.setText(mDefaultContactShop.shopTact);
            yige_phone.setText(mDefaultContactShop.shopTel);

            name1.setText("");
            phone_et1.setText("");
            name2.setText("");
            phone_et2.setText("");
            name3.setText("");
            phone_et3.setText("");
        } else if (!TextUtils.isEmpty(shopTact2Temp.trim()) && TextUtils.isEmpty(shopTact3Temp.trim())) {
            name1.setText(mDefaultContactShop.shopTact);
            phone_et1.setText(mDefaultContactShop.shopTel);
            name2.setText(mDefaultContactShop.shopTact2);
            phone_et2.setText(mDefaultContactShop.shopTel2);

            sange_ll.setVisibility(View.VISIBLE);
            yige_ll.setVisibility(View.GONE);
            rl3.setVisibility(View.GONE); //隐藏第三个
            name3.setText("");
            phone_et3.setText("");
        } else if (TextUtils.isEmpty(shopTact2Temp.trim()) && !TextUtils.isEmpty(shopTact3Temp.trim())) {

            name1.setText(mDefaultContactShop.shopTact);
            phone_et1.setText(mDefaultContactShop.shopTel);
            name2.setText(mDefaultContactShop.shopTact3);
            phone_et2.setText(mDefaultContactShop.shopTel3);

            sange_ll.setVisibility(View.VISIBLE);
            yige_ll.setVisibility(View.GONE);
            rl3.setVisibility(View.GONE); //隐藏第三个

            name3.setText("");
            phone_et3.setText("");
        } else if (!TextUtils.isEmpty(shopTact2Temp) && !TextUtils.isEmpty(shopTact3Temp)) {
            name1.setText(mDefaultContactShop.shopTact);
            phone_et1.setText(mDefaultContactShop.shopTel);

            name2.setText(mDefaultContactShop.shopTact2);
            phone_et2.setText(mDefaultContactShop.shopTel2);
            name3.setText(mDefaultContactShop.shopTact3);
            phone_et3.setText(mDefaultContactShop.shopTel3);
            sange_ll.setVisibility(View.VISIBLE);
            yige_ll.setVisibility(View.GONE);
            rl3.setVisibility(View.VISIBLE); //显示第三个
        }

        if(   mDefaultContactShop.city ==null ||  mDefaultContactShop.province_id.equals(mDefaultContactShop.city_id)){
            address.setText("(默认)"+(TextUtils.isEmpty(  mDefaultContactShop.province)?"":  mDefaultContactShop.province) + (TextUtils.isEmpty(  mDefaultContactShop.address)?"":  mDefaultContactShop.address));
        }else {
            address.setText("(默认)"+(TextUtils.isEmpty(  mDefaultContactShop.province)?"":  mDefaultContactShop.province) +(TextUtils.isEmpty(  mDefaultContactShop.city)?"":  mDefaultContactShop.city)+ (TextUtils.isEmpty(  mDefaultContactShop.address)?"":  mDefaultContactShop.address));
        }
        currAddress=address.getText().toString();
        addressSend         =        mDefaultContactShop.address;
         shopProviceCode     =       mDefaultContactShop.province_id;
         shopProviceName     =        mDefaultContactShop.province;
         shopCityCode     =        mDefaultContactShop.city_id;
          shopCityName     =        mDefaultContactShop.city;

    }

    String addressSend;
    String shopProviceCode;
    String shopProviceName;
    String shopCityCode;
    String shopCityName;


    //根据id查询 小定单个数量
    public String queryProductDbNum(String id) {
        try {

            Dao<VpNewProductDb, Integer> mVpNewProductDbDaoDao = DatabaseHelper.getHelper(VpNewConfirmNewActivity.this).getVpNewProductDbDaoDao();

            List<VpNewProductDb> mVpNewProductDbList = mVpNewProductDbDaoDao.queryBuilder().where().eq("id", id).query();
            if (mVpNewProductDbList != null && mVpNewProductDbList.size() > 0) {
                String pro_num = mVpNewProductDbList.get(0).pro_num;
//                int jianNumDb = (int) HttpBase.mul(Double.parseDouble(pro_num), Double.parseDouble(mVpNewProductDbList.get(0).equation_factor));
                return pro_num + "";

            }
            return "";
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }

    }
    //
    public void deleteCartProduct( List<ProductVpNewSearch> listObj) {
        for (ProductVpNewSearch mProductVpNewSearch:listObj ) {
            Dao<VpNewProductDb, Integer> mVpNewProductDbDaoDao = null;
            try {
                mVpNewProductDbDaoDao = DatabaseHelper.getHelper(VpNewConfirmNewActivity.this).getVpNewProductDbDaoDao();
                List<VpNewProductDb> mVpNewProductDbList = mVpNewProductDbDaoDao.queryBuilder().where().eq("id", mProductVpNewSearch.id).query();
                mVpNewProductDbDaoDao.delete(mVpNewProductDbList);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        EventBus.getDefault().post(MainActivity11.CARTREFRESH);
    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
     getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
       getWindow().setAttributes(lp);
    }

}
