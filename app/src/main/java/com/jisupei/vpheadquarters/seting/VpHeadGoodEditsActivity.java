package com.jisupei.vpheadquarters.seting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.jisupei.R;
import com.jisupei.activity.homepage2.MainActivity11;
import com.jisupei.application.MyApplication;
import com.jisupei.http.HttpUtil;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.StatusBarUtil;
import com.jisupei.vpheadquarters.seting.bean.VpBIanjiParams;
import com.jisupei.vpheadquarters.seting.bean.VpEditor;
import com.jisupei.vpnew.db.VpNewProductDb;
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
public class VpHeadGoodEditsActivity extends AppCompatActivity {
    EditText spmc;
    EditText bianma;
    EditText guige;
    EditText leixing;
    EditText gys;
    LinearLayout rexiao_select;
    LinearLayout rexiao_default;
    LinearLayout cuxiao_select;
    LinearLayout cuxiao_default;
    LinearLayout zhenpin_select;
    LinearLayout zhenpin_default;
    LinearLayout fuwua1_select;
    LinearLayout fuwua1_default;
    EditText spfl;
    EditText xiaofeicj;




    EditText jinzhong;
    EditText maozhong;
    EditText chang;
    EditText kuan;
    EditText gao;
    EditText tiji;
    EditText youxiapqi;
    EditText cunsfangshi;
    EditText miaoshu;

    ImageView rexiao_iv1;
    ImageView rexiao_iv2;
    ImageView cuxiao_iv_1;
    ImageView cuxiao_iv_2;
    ImageView zhenpin_iv_1;
    ImageView zhenpin_iv_2;
    Button baocun;

    Dao<VpNewProductDb,Integer> mVpNewProductDbDaoDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.vp_head_bianji_huopin_xinxi);
        StatusBarUtil.setStatusBarColor(this,R.color.white);
        int a2=  StatusBarUtil.StatusBarLightMode(VpHeadGoodEditsActivity.this);
        if(a2==0){
            StatusBarUtil.setStatusBarColor(VpHeadGoodEditsActivity.this,R.color.black);
        }
        spmc= (EditText) findViewById(R.id.spmc);
        baocun= (Button) findViewById(R.id.baocun);
        bianma= (EditText) findViewById(R.id.bianma);
        bianma.setCursorVisible(false);
        bianma.setFocusable(false);
        bianma.setFocusableInTouchMode(false);

        guige= (EditText) findViewById(R.id.guige);
        leixing= (EditText) findViewById(R.id.leixing);
        leixing.setCursorVisible(false);
        leixing.setFocusable(false);
        leixing.setFocusableInTouchMode(false);
        leixing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(VpHeadGoodEditsActivity.this);
//             builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("请选择");
                //    指定下拉列表的显示数据
             final String[]  leixes = {"计件", "抄码"};
//
//                final String[]  fhclArr=  fhclStr.toArray(new String[]{});
                builder.setItems(leixes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        leixing.setText(leixes[which]);
                        if(which==0){
                            skutype="0";
                        }else {
                            skutype="1";
                        }
                    }
                });
                builder.show();
            }
        });



        gys= (EditText) findViewById(R.id.gys);
        gys.setCursorVisible(false);
        gys.setFocusable(false);
        gys.setFocusableInTouchMode(false);
        gys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(VpHeadGoodEditsActivity.this);
//             builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("请选择");
                //    指定下拉列表的显示数据

               final String[]  supplierListArrStr=  supplierListArr.toArray(new String[]{});
                builder.setItems(supplierListArrStr, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        gys.setText(supplierListArrStr[which]);
                        supplierCode= mVpEditor.supplierList.get(which).supplier_code;


                    }
                });
                builder.show();

            }
        });


        rexiao_select= (LinearLayout) findViewById(R.id.a1);
        rexiao_default= (LinearLayout) findViewById(R.id.a2);
        rexiao_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rexiao_iv1.setImageResource(R.mipmap.list_icon_choose1);
                rexiao_iv2.setImageResource(R.mipmap.list_icon_default1);
                isHot="Y";


            }
        });
        rexiao_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rexiao_iv2.setImageResource(R.mipmap.list_icon_choose1);
                rexiao_iv1.setImageResource(R.mipmap.list_icon_default1);
                isHot="N";
            }
        });
        rexiao_iv1= (ImageView) findViewById(R.id.a1_iv);
        rexiao_iv2= (ImageView) findViewById(R.id.a2_iv);



        cuxiao_select= (LinearLayout) findViewById(R.id.a1_2);
        cuxiao_default= (LinearLayout) findViewById(R.id.a2_2);

        cuxiao_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cuxiao_iv_1.setImageResource(R.mipmap.list_icon_choose1);
                cuxiao_iv_2.setImageResource(R.mipmap.list_icon_default1);
                isDiscount="Y";


            }
        });
        cuxiao_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cuxiao_iv_2.setImageResource(R.mipmap.list_icon_choose1);
                cuxiao_iv_1.setImageResource(R.mipmap.list_icon_default1);
                isDiscount="N";
            }
        });



        cuxiao_iv_1= (ImageView) findViewById(R.id.a1_iv_2);
        cuxiao_iv_2= (ImageView) findViewById(R.id.a2_iv_2);



         zhenpin_select= (LinearLayout) findViewById(R.id.a1_3);
         zhenpin_default= (LinearLayout) findViewById(R.id.a2_3);
        zhenpin_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                zhenpin_iv_1.setImageResource(R.mipmap.list_icon_choose1);
                zhenpin_iv_2.setImageResource(R.mipmap.list_icon_default1);
                isGift="Y";


            }
        });
        zhenpin_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zhenpin_iv_2.setImageResource(R.mipmap.list_icon_choose1);
                zhenpin_iv_1.setImageResource(R.mipmap.list_icon_default1);
                isGift="N";
            }
        });

        zhenpin_iv_1= (ImageView) findViewById(R.id.a1_iv_3);
        zhenpin_iv_2= (ImageView) findViewById(R.id.a2_iv_3);
        fuwua1_select= (LinearLayout) findViewById(R.id.fuwua1);
        fuwua1_default= (LinearLayout) findViewById(R.id.fuwua2_3);


        spfl= (EditText) findViewById(R.id.spfl);
        spfl.setCursorVisible(false);
        spfl.setFocusable(false);
        spfl.setFocusableInTouchMode(false);
        spfl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(VpHeadGoodEditsActivity.this);
//             builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("请选择");
                //    指定下拉列表的显示数据

                final String[]  tclassListArrStr=  tclassListArr.toArray(new String[]{});
                builder.setItems(tclassListArrStr, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        spfl.setText(tclassListArrStr[which]);
                        classId= mVpEditor.tclassList.get(which).id;

                    }
                });
                builder.show();

            }
        });

        xiaofeicj= (EditText) findViewById(R.id.xiaofeicj);
        xiaofeicj.setCursorVisible(false);
        xiaofeicj.setFocusable(false);
        xiaofeicj.setFocusableInTouchMode(false);
        xiaofeicj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(VpHeadGoodEditsActivity.this);
//             builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("请选择");
                //    指定下拉列表的显示数据

                final String[]  tVclassListArrStr=  tVclassListArr.toArray(new String[]{});
                builder.setItems(tVclassListArrStr, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        xiaofeicj.setText(tVclassListArrStr[which]);
//                        classId=mVpEditor.tclassList.get(which).id;

                        vclassCode= mVpEditor.tVclassList.get(which).id;
                        vclassName= mVpEditor.tVclassList.get(which).className;
                    }
                });
                builder.show();

            }
        });




        jinzhong= (EditText) findViewById(R.id.jinzhong);
        maozhong= (EditText) findViewById(R.id.maozhong);
        chang= (EditText) findViewById(R.id.chang);
        kuan= (EditText) findViewById(R.id.kuan);
        gao= (EditText) findViewById(R.id.gao);
        tiji= (EditText) findViewById(R.id.tiji);
        youxiapqi= (EditText) findViewById(R.id.youxiapqi);
        cunsfangshi= (EditText) findViewById(R.id.cunsfangshi);
        cunsfangshi.setCursorVisible(false);
        cunsfangshi.setFocusable(false);
        cunsfangshi.setFocusableInTouchMode(false);
        cunsfangshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(VpHeadGoodEditsActivity.this);
//             builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("请选择");
                //    指定下拉列表的显示数据

                final String[]  saveTypeArrStr=  saveTypeArr.toArray(new String[]{});
                builder.setItems(saveTypeArrStr, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        cunsfangshi.setText(saveTypeArrStr[which]);
//                        classId=mVpEditor.tclassList.get(which).id;

                        storageMode= mVpEditor.saveType.get(which).item_id;
                        storageModeId= mVpEditor.saveType.get(which).item_value;

                    }
                });
                builder.show();

            }
        });

        miaoshu= (EditText) findViewById(R.id.miaoshu);




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


        baocun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppLoading.show(VpHeadGoodEditsActivity.this);
                VpBIanjiParams mVpBIanjiParams= new VpBIanjiParams();
                mVpBIanjiParams.owner= mVpEditor.info.info.owner;
                mVpBIanjiParams.sku_code= mVpEditor.info.info.skuCode;
                mVpBIanjiParams.product_id= mVpEditor.info.info.id;
                mVpBIanjiParams.classId=classId;
                mVpBIanjiParams.tax_type="3";
                mVpBIanjiParams.tax_rate="";
                mVpBIanjiParams.tax_amount="";
                mVpBIanjiParams.supplier_code=supplierCode;
                mVpBIanjiParams.equationFactor= mVpEditor.info.info.equationFactor;
                mVpBIanjiParams.weight=jinzhong.getText().toString();
                mVpBIanjiParams.grossWeight=maozhong.getText().toString();
                mVpBIanjiParams.length=chang.getText().toString();
                mVpBIanjiParams.volume=tiji.getText().toString();
                mVpBIanjiParams.validLength=youxiapqi.getText().toString();
                mVpBIanjiParams.storageMode=storageMode;
                mVpBIanjiParams.storageModeId=storageModeId;
                mVpBIanjiParams.skutype=skutype;//类型
                mVpBIanjiParams.sku_dec=miaoshu.getText().toString();
                mVpBIanjiParams.sku_name=spmc.getText().toString();
                mVpBIanjiParams.isGift=isGift;
                mVpBIanjiParams.isDiscount=isDiscount;
                mVpBIanjiParams.styleno=guige.getText().toString();
                mVpBIanjiParams.isEnable="Y";
                mVpBIanjiParams.isHot=isHot;
                mVpBIanjiParams.sort="1";
                mVpBIanjiParams.vclassCode=vclassCode;
                mVpBIanjiParams.vclassName=vclassName;
                mVpBIanjiParams.hasHomePage="Y";


                HttpUtil.getInstance().vpEditProduct(mVpBIanjiParams, new StringCallback() {  //编辑的信息
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

                            VpHeadGoodEditsActivity.this.finish();
                                EventBus.getDefault().post("VpheadSellFragmentNew");

                        }else{
                            ToasAlert.toastCenter(object.optString("optDesc"));

                        }

                    }
                });


            }
        });

    }

    String isGift;
    String isHot;
    String isDiscount;
    VpEditor mVpEditor;

    String classId;
    String supplierCode;
    String storageMode; //冷冻
    String storageModeId; //冷冻
    String vclassCode;
    String vclassName;

    String skutype; //0计件 1 抄码
     List<String>   supplierListArr=new ArrayList<String>();
     List<String>   tclassListArr=new ArrayList<String>();
     List<String>   tVclassListArr=new ArrayList<String>();
     List<String>   saveTypeArr=new ArrayList<String>();


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
                     vclassCode= mVpEditor.info.info.vclassCode;
                     vclassName= mVpEditor.info.info.vclassName;
                     classId= mVpEditor.info.info.classId;

                     storageMode= mVpEditor.info.info.storageMode;
                     storageModeId= mVpEditor.info.info.storageModeId;
                     skutype= mVpEditor.info.info.skuType;
                     supplierCode= mVpEditor.info.info.supplierCode;


                spmc.setText(mVpEditor.info.info.skuName);
                bianma.setText(mVpEditor.info.info.skuCode);
                     bianma.setText(mVpEditor.info.info.skuCode);
                     guige.setText(mVpEditor.info.info.styleno);
                     leixing.setText("计件");
                     gys.setText(mVpEditor.info.info.supplierName);
                  if("Y".equals(mVpEditor.info.info.isHot)){ //热销
                      rexiao_iv1.setImageResource(R.mipmap.list_icon_choose1);
                      rexiao_iv2.setImageResource(R.mipmap.list_icon_default1);
                      isHot="Y";
                  }else {
                      rexiao_iv1.setImageResource(R.mipmap.list_icon_default1);
                      rexiao_iv2.setImageResource(R.mipmap.list_icon_choose1);
                      isHot="N";
                  }
                  if("Y".equals(mVpEditor.info.info.isDiscount)){ //促销
                      cuxiao_iv_1.setImageResource(R.mipmap.list_icon_choose1);
                      cuxiao_iv_2.setImageResource(R.mipmap.list_icon_default1);
                      isDiscount="Y";
                  }else {
                      cuxiao_iv_1.setImageResource(R.mipmap.list_icon_default1);
                      cuxiao_iv_2.setImageResource(R.mipmap.list_icon_choose1);
                      isDiscount="N";
                  }
                  if("Y".equals(mVpEditor.info.info.isGift)){ //赠品
                      zhenpin_iv_1.setImageResource(R.mipmap.list_icon_choose1);
                      zhenpin_iv_2.setImageResource(R.mipmap.list_icon_default1);
                      isGift="Y";
                  }else {
                      zhenpin_iv_1.setImageResource(R.mipmap.list_icon_default1);
                      zhenpin_iv_2.setImageResource(R.mipmap.list_icon_choose1);
                      isGift="N";
                  }
                     spfl.setText(mVpEditor.info.info.className);
                     xiaofeicj.setText(mVpEditor.info.info.vclassName);
                     jinzhong.setText(mVpEditor.info.info.weight);
                     maozhong.setText(mVpEditor.info.info.grossWeight);
                     chang.setText(mVpEditor.info.info.length);
                     kuan.setText(mVpEditor.info.info.width);
                     gao.setText(mVpEditor.info.info.height);
                     tiji.setText(mVpEditor.info.info.volume);
                     youxiapqi.setText(mVpEditor.info.info.validLength);
                     cunsfangshi.setText(mVpEditor.info.info.storageMode);
                     miaoshu.setText(Html.fromHtml(mVpEditor.info.info.skuDec));
                     for (VpEditor.SupplierListItem mSupplierListItem: mVpEditor.supplierList) {

                         supplierListArr.add(mSupplierListItem.supplier_name);

                     }
                     for (VpEditor.TclassListItem mtclassListItem: mVpEditor.tclassList) {

                         tclassListArr.add(mtclassListItem.name);

                     }
                     for (VpEditor.TVclassListItem mtVclassListItem: mVpEditor.tVclassList) {

                         tVclassListArr.add(mtVclassListItem.className);

                     }
                     for (VpEditor.SaveTypeItem mSaveTypeItem: mVpEditor.saveType) {

                         saveTypeArr.add(mSaveTypeItem.item_value);

                     }


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




}
