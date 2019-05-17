package com.jisupei.vpheadquarters.seting;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jisupei.R;
import com.jisupei.activity.homepage2.MainActivity11;
import com.jisupei.application.MyApplication;
import com.jisupei.http.HttpUtil;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.StatusBarUtil;
import com.jisupei.vpheadquarters.seting.bean.VpEditor;
import com.jisupei.vpheadquarters.seting.bean.VpPricesParams;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by xiayumo on 16/5/11.
 */
public class VpHeadEditorPriceActivity extends AppCompatActivity {
    TextView spmc;
    TextView coding;
    TextView danwei1;
    TextView danwei2;
    EditText shichangjiage;
    EditText youhuijiage;
    Button save;
    ListView   listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.vp_head_bianji_jiage);
        StatusBarUtil.setStatusBarColor(this,R.color.white);
        int a2=  StatusBarUtil.StatusBarLightMode(VpHeadEditorPriceActivity.this);
        if(a2==0){
            StatusBarUtil.setStatusBarColor(VpHeadEditorPriceActivity.this,R.color.black);
        }

        save= (Button) findViewById(R.id.baocun);
        shichangjiage= (EditText) findViewById(R.id.shichangjiage);
        youhuijiage= (EditText) findViewById(R.id.youhuijiage);
         listview= (ListView) findViewById(R.id.listview);


        ImageView  add= (ImageView) findViewById(R.id.add);
        //编辑
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if(mVpEditor.info.priceList!=null && mVpEditor.info.priceList.size()>0){
                    if(mVpEditor.info.priceList.size()>=5){
                        ToasAlert.toastCenter("价格区间不能超过5个");
                        return;

                    }
                   VpEditor.PriceListItem mPriceListItem= mVpEditor.info.priceList.get( mVpEditor.info.priceList.size()-1);

                    VpEditor.PriceListItem mPriceListItem1=  new VpEditor().new PriceListItem();
                    mPriceListItem1.begin_qty=  (Integer.parseInt(mPriceListItem.end_qty)+1)+"";
                    mPriceListItem1.end_qty=  (Integer.parseInt(mPriceListItem.end_qty)+11)+"";
                    mPriceListItem1.qty_price= mPriceListItem.qty_price;
                    mVpEditor.info.priceList.add(mPriceListItem1);
                    mJiaGedapter.notifyDataSetChanged();

                }


            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(mVpEditor.info.priceList!=null && mVpEditor.info.priceList.size()>0){
                    for (VpEditor.PriceListItem mPriceListItem: mVpEditor.info.priceList) {

                        if(Double.parseDouble(mPriceListItem.begin_qty)>Double.parseDouble(mPriceListItem.end_qty)){
                            ToasAlert.toastCenter("开始数量不能大于结束数量");
                            return;
                        }

                    }

                }

                //


                AppLoading.show(VpHeadEditorPriceActivity.this);
                VpPricesParams mVpPricesParams = new VpPricesParams();
                mVpPricesParams.productId= mVpEditor.info.info.id;
                mVpPricesParams.marketUnitprice=shichangjiage.getText().toString();
                mVpPricesParams.unitPrice=youhuijiage.getText().toString();
                mVpPricesParams.unitCost=youhuijiage.getText().toString();
                mVpPricesParams.purPrice=youhuijiage.getText().toString();
                mVpPricesParams.price=youhuijiage.getText().toString();
                mVpPricesParams.hasHomePage="true";

                List<VpPricesParams.PriceListTemp>  mlist=new ArrayList<VpPricesParams.PriceListTemp>();
                if(mVpEditor.info.priceList!=null && mVpEditor.info.priceList.size()>0){
                    for (VpEditor.PriceListItem mPriceListItem: mVpEditor.info.priceList) {

                        VpPricesParams.PriceListTemp mPriceListTemp= mVpPricesParams.new PriceListTemp();
                        mPriceListTemp.key=mPriceListItem.id;
                        VpPricesParams.Value mValue= mVpPricesParams.new Value();
                        mPriceListTemp.value=mValue;
                        mValue.begin_qty=mPriceListItem.begin_qty;
                        mValue.end_qty=mPriceListItem.end_qty;
                        mValue.id=mPriceListItem.id;
                        mValue.qty_price=mPriceListItem.qty_price;
                        mValue.sku_code=mPriceListItem.sku_code;
                        mValue.type="A";

                        mlist.add(mPriceListTemp);


                    }
                    mVpPricesParams.priceList=mlist;
                }


                HttpUtil.getInstance().vpEditProductPrice(mVpPricesParams, new StringCallback() {  //编辑的信息
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        AppLoading.close();
                        ToasAlert.toastCenter("连接服务器失败，请稍后再试！");

                    }
                    @Override
                    public void onResponse(String response, int id) {

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
                            ToasAlert.toastCenter(object.optString("optDesc"));

                        }else{
                            ToasAlert.toastCenter(object.optString("optDesc"));

                        }

                    }
                });


            }
        });

        spmc= (TextView) findViewById(R.id.spmc);
        coding= (TextView) findViewById(R.id.bianma);
        danwei1= (TextView) findViewById(R.id.danwei1);
        danwei2= (TextView) findViewById(R.id.danwei2);
        shichangjiage= (EditText) findViewById(R.id.shichangjiage);
        youhuijiage= (EditText) findViewById(R.id.youhuijiage);




        MyApplication.instance.addActivity(this);
        EventBus.getDefault().register(this);

        findViewById(R.id.back_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        AppUtils.expandViewTouchDelegate( findViewById(R.id.back_bt), 30, 30, 50, 50);
         final String productId=    getIntent().getStringExtra("productid");

        //加载数据
        productInfoData(productId);


    }


    VpEditor mVpEditor;
    JiaGedapter mJiaGedapter;
    //查询商品的详情的接口
     public void productInfoData(String productid){

         AppLoading.show(this);
         HttpUtil.getInstance().initVpProductEdit(productid, new StringCallback() {  //编辑的信息
             @Override
             public void onError(Call call, Exception e, int id) {
                 AppLoading.close();
                 ToasAlert.toastCenter("连接服务器失败，请稍后再试！");

             }
             @Override
             public void onResponse(String response, int id) {

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
                        mVpEditor =  new Gson().fromJson(object.optString("res"),VpEditor.class);

                     spmc.setText(mVpEditor.info.info.skuName);
                     coding.setText(mVpEditor.info.info.skuCode);
                     shichangjiage.setText(mVpEditor.info.info.marketUnitprice);
                     youhuijiage.setText(mVpEditor.info.info.unitPrice);
                     danwei1.setText("元/"+ mVpEditor.info.info.unit);
                     danwei2.setText("元/"+ mVpEditor.info.info.unit);

                       mJiaGedapter = new JiaGedapter(VpHeadEditorPriceActivity.this, mVpEditor.info.priceList);
                     listview.setAdapter(mJiaGedapter);
//                     JSONObject object1 = null;
//                     try {
//                         object1 = object.getJSONObject("res");
//                     } catch (JSONException e) {
//                         e.printStackTrace();
//                     }
//                     final List<VpGood> orderListTemp = new Gson().fromJson(object1.optString("list").toString(), new TypeToken<ArrayList<VpGood>>() {
//                     }.getType());



                 }else{
                     ToasAlert.toastCenter(object.optString("optDesc"));

                 }

             }
         });

     }

    //详情页面被首页以及搜索页面启动且不缓存,所以详情页面只接收购物车发送的消息更新
    //更新的部分包括底部的购物车信息栏以及数量加减的布局
    public void onEventMainThread(String refresh) {
        if(MainActivity11.CARTREFRESH.equals(refresh)){


//            cart_num.setText(count+"");

      }

    }

    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);


    }





    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //停止轮播

    }



    class JiaGedapter extends BaseAdapter {

        public  int curPos=-1;
        Context context;
        List<VpEditor.PriceListItem> priceList;
        public     int oldCount;

        public JiaGedapter(Context context,  List<VpEditor.PriceListItem> priceList) {
            this.context = context;
            this.priceList = priceList;
        }
        public void suaxin(  List<VpEditor.PriceListItem> priceList) {

            this.priceList = priceList;
        }
        public void addList( List<VpEditor.PriceListItem> mpriceList){
//            oldCount=orderListTemp.size();
            priceList.addAll(mpriceList);
        }
        @Override
        public int getCount() {
            return priceList==null?0:priceList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            final VpEditor.PriceListItem mPrice = priceList.get(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.vp_head_adapter_jiage_good, null);

                holder.minDanWei = (TextView)convertView.findViewById(R.id.minDanWei);
                holder.minDanWei_text = (TextView)convertView.findViewById(R.id.minDanWei_text);
                holder.maxDanWei = (TextView)convertView.findViewById(R.id.maxDanWei);
                holder.maxDanWei_text = (TextView)convertView.findViewById(R.id.maxDanWei_text);
                holder.danjia = (TextView)convertView.findViewById(R.id.danjia);
                holder.danjia_text = (TextView)convertView.findViewById(R.id.danjia_text);


                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }
            holder.minDanWei.setText(mPrice.begin_qty);
            holder. minDanWei_text.setText(mVpEditor.info.info.unit);
            holder.maxDanWei.setText(mPrice.end_qty);
            holder. maxDanWei_text.setText(mVpEditor.info.info.unit);
            holder.danjia.setText(mPrice.qty_price);
            holder. danjia_text.setText("元/"+ mVpEditor.info.info.unit);
            holder.minDanWei.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("数量");
                    View view1 = View.inflate(context, R.layout.include_dalog_shuliang, null);
                    builder.setView(view1);
                    final EditText    num_et=  (EditText)view1.findViewById(R.id.num_et);
                    num_et.setText(holder.minDanWei.getText().toString());
                    builder.setNegativeButton("取消", null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //  updateCart(mEditText.getText().toString(),dai,position);
                            mPrice.begin_qty=num_et.getText().toString();
                            notifyDataSetChanged();
                        }
                    });
                    AlertDialog  alZHu=   builder.show();


                }
            });
            holder.maxDanWei.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("数量");
                    View view1 = View.inflate(context, R.layout.include_dalog_shuliang, null);
                    builder.setView(view1);
                    final EditText    num_et=  (EditText)view1.findViewById(R.id.num_et);

                    builder.setNegativeButton("取消", null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //  updateCart(mEditText.getText().toString(),dai,position);
                            mPrice.end_qty=num_et.getText().toString();
                            notifyDataSetChanged();
                        }
                    });
                    AlertDialog  alZHu=   builder.show();

                }
            });
            holder.danjia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("价格");
                    View view1 = View.inflate(context, R.layout.include_dalog_shuliang, null);
                    builder.setView(view1);
                    final EditText    num_et=  (EditText)view1.findViewById(R.id.num_et);

                    builder.setNegativeButton("取消", null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //  updateCart(mEditText.getText().toString(),dai,position);
                            mPrice.qty_price=num_et.getText().toString();
                            notifyDataSetChanged();
                        }
                    });
                    AlertDialog  alZHu=   builder.show();

                }
            });








            return convertView;
        }

        public final class ViewHolder {

            public TextView minDanWei;
            public TextView minDanWei_text;
            public TextView maxDanWei;
            public TextView maxDanWei_text;
            public TextView danjia;
            public TextView danjia_text;



        }


    }



}
