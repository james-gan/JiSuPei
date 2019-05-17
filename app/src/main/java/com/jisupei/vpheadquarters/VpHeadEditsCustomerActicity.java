package com.jisupei.vpheadquarters;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jisupei.R;
import com.jisupei.http.HttpBase;
import com.jisupei.http.HttpUtil;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.AutoUtils;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/7/12.
 */
public class VpHeadEditsCustomerActicity extends Activity {
    private String[] requestPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @InjectView(R.id.back_bt)
    ImageView backBt;
    @InjectView(R.id.textView)
    TextView textView;
//    @InjectView(R.id.yqm_et)
//    EditText yqmEt;



    @InjectView(R.id.mendian)
    TextView mendian;

    @InjectView(R.id.khmc_et) //名称
            EditText khmc_et;
    @InjectView(R.id.lianxiren_et) //联系人姓名
            EditText lianxiren_et;

    @InjectView(R.id.lianxiren_et_phone) //联系人电话
            EditText lianxiren_et_phone;
//    @InjectView(R.id.custom_accout) //登录帐号
//            EditText custom_accout;
//    @InjectView(R.id.custom_accout_pwd) //登录密码
//            EditText custom_accout_pwd;
//
//    @InjectView(R.id.mddz_tv)       //门店地址
//            TextView mddzTv;

    @InjectView(R.id.shen_et)       //省
            TextView shen_et;
    @InjectView(R.id.shi_et)       //省
            TextView shi_et;
    @InjectView(R.id.jie_et)       //省
            TextView jie_et;
    @InjectView(R.id.leixing_et)       //省
            TextView leixing_et;
    @InjectView(R.id.cangku_et)       //省
            TextView cangku_et;
    @InjectView(R.id.mdxxdz_tv)       //省
            TextView mdxxdz_tv;
    @InjectView(R.id.shjijian)       //省
            TextView shjijian;

//    @InjectView(R.id.account_msg)
//    TextView account_msg;

//    @InjectView(R.id.mima_msg)
//    TextView mima_msg;


//    @InjectView(R.id.add1_iv)
//    SimpleDraweeView add1_iv;
//    @InjectView(R.id.add2_iv)
//    SimpleDraweeView add2_iv;
//    @InjectView(R.id.add3_iv)
//    SimpleDraweeView add3_iv;
//
//
//    @InjectView(R.id.add1_iv_del)
//    ImageView add1_iv_del;
//    @InjectView(R.id.add2_iv_del)
//    ImageView add2_iv_del;
//    @InjectView(R.id.add3_iv_del)
//    ImageView add3_iv_del;


    @OnClick(R.id.back_bt)
    void back(View view) {
        finish();
    }


    @OnClick(R.id.save_btn)
    void save_btn(View view) {
//        if (TextUtils.isEmpty(gpsAddress)) {
////            initGPS();
//            return;
//        }
        if (TextUtils.isEmpty(khmc_et.getText().toString().trim())) {
            ToasAlert.toastCenter("请填写门店名称");
            return;
        }
        if (TextUtils.isEmpty(lianxiren_et.getText().toString().trim())) {
            ToasAlert.toastCenter("请填写联系人姓名");
            return;
        }
        if (TextUtils.isEmpty(lianxiren_et_phone.getText().toString().trim())) {
            ToasAlert.toastCenter("请填写联系人电话");
            return;
        }
//        if (TextUtils.isEmpty(custom_accout.getText().toString().trim())) {
//            ToasAlert.toastCenter("请填写登录账号");
//            return;
//        }
//        if (TextUtils.isEmpty(custom_accout_pwd.getText().toString().trim())) {
//            ToasAlert.toastCenter("请填写登录账号密码");
//            return;
//        }


        if (TextUtils.isEmpty(mdxxdz_tv.getText().toString().trim())) {
            ToasAlert.toastCenter("请填写门店详细地址");
            return;
        }

//        if (TextUtils.isEmpty(psckTv.getText().toString().trim())) {
//            ToasAlert.toastCenter("请选择配送仓库");
//            return;
//        }

        //上传图片 回单图片

       List<File> fileList = new ArrayList<File>();
//        if (!TextUtils.isEmpty(imagePath1)) {
//            fileList.add(new File(imagePath1));
//        }
//        if (!TextUtils.isEmpty(imagePath2)) {
//            fileList.add(new File(imagePath2));
//        }
//        if (!TextUtils.isEmpty(imagePath3)) {
//            fileList.add(new File(imagePath3));
//        }
//        if (fileList.size() < 1) {
//            ToasAlert.toastCenter("请上传门店照片");
//            return;
//
//        }

        JSONObject Obj = new JSONObject();
        try {
            Obj.put("shopId", shopId);
            Obj.put("companyId", currMenDianId);
            Obj.put("shopName", khmc_et.getText().toString()); //客户名称
            Obj.put("wmCode", currCangkuId);
            Obj.put("contactName", lianxiren_et.getText().toString());
            Obj.put("contactPhone", lianxiren_et_phone.getText().toString());
            Obj.put("province", currShenId);
            Obj.put("city", currshiId);
            Obj.put("district", currjieId==null ?"":currjieId);
            Obj.put("address", mdxxdz_tv.getText().toString());
            Obj.put("shopArea", "");
            Obj.put("account", HttpBase.account_2);
            Obj.put("autoDays", shjijian.getText().toString());
//            Obj.put("loginAccount", custom_accout.getText().toString());
//            Obj.put("psw", custom_accout_pwd.getText().toString());
            Obj.put("typeId", currtypeId);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        AppLoading.show(this);
        HttpUtil.getInstance().updateShop(Obj.toString(), fileList, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppLoading.close();
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
                    VpHeadEditsCustomerActicity.this.finish();
                    ToasAlert.toastCenter("修改客户成功");
                    EventBus.getDefault().post("VpheadCustomerFragment");

                } else {
                    ToasAlert.toastCenter(object.optString("msg"));
                }

            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_vp_headadd_bianij);
        AutoUtils.auto(this);
        ButterKnife.inject(this);

          shopId=  getIntent().getStringExtra("shopId");
        AppUtils.expandViewTouchDelegate(backBt, 30, 30, 50, 50);
        init();
        loadData();
    }

    String  shopId;



    String  currMenDianId;
    String  currMenDianNm;
    @OnClick(R.id.mendian)
    void mendian(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(VpHeadEditsCustomerActicity.this);

        builder.setTitle("请选择");

        List<String> list= new ArrayList<String>();
        if(mEditsCustomerBean.companyList!=null && mEditsCustomerBean.companyList.size()>0){
            for (EditsCustomerBean.CompanyListItem mCompanyListItem: mEditsCustomerBean.companyList) {
                list.add(mCompanyListItem.nm);
            }
        }
        final String[]  listArr=  list.toArray(new String[]{});

        builder.setItems(listArr, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
//                     Toast.makeText(TheDeliveryHeadActivity.this, "车辆为：" + fhclArr[which], Toast.LENGTH_SHORT).show();
//                ck.setText(ckNamesArr[which]);
//                currCompanyId=ids.get(which);
//                pageno=1;
//                lodeOrder(currCompanyId,handleStatus ,pageno, 3);
                mendian.setText(listArr[which]);
                currMenDianNm=listArr[which];
                currMenDianId= mEditsCustomerBean.companyList.get(which).id;

                AppLoading.show(VpHeadEditsCustomerActicity.this);
                HttpUtil.getInstance().selectInitInfoByCompanyId(currMenDianId,  new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        AppLoading.close();
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
                            String res = object.optString("res");

                            mSelectInitInfoByCompanyBean=  new Gson().fromJson(res,SelectInitInfoByCompanyBean.class);



                        } else {
                            ToasAlert.toastCenter("失败！");
                        }

                    }
                });









            }
        });
        builder.show();


    }

    SelectInitInfoByCompanyBean mSelectInitInfoByCompanyBean;

    String  currShenId;
    String  currShenTitle;
    PovinceCityBean mPovinceCityBean;
    @OnClick(R.id.shen_et)
    void shen_et(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(VpHeadEditsCustomerActicity.this);
        builder.setTitle("请选择");
        List<String> list= new ArrayList<String>();
        if(mEditsCustomerBean.provinceList!=null && mEditsCustomerBean.provinceList.size()>0){
            for (EditsCustomerBean.ProvinceListItem mProvinceListItem: mEditsCustomerBean.provinceList) {
                list.add(mProvinceListItem.title);
            }
        }
        final String[]  listArr=  list.toArray(new String[]{});

        builder.setItems(listArr, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
//                     Toast.makeText(TheDeliveryHeadActivity.this, "车辆为：" + fhclArr[which], Toast.LENGTH_SHORT).show();
//                ck.setText(ckNamesArr[which]);
//                currCompanyId=ids.get(which);
//                pageno=1;
//                lodeOrder(currCompanyId,handleStatus ,pageno, 3);

                shen_et.setText(listArr[which]);
                currShenTitle=listArr[which];
                currShenId= mEditsCustomerBean.provinceList.get(which).id;


                shi_et.setText("");//初始话数据
                currshiTitle="";
                currshiId="";
                jie_et.setText("");
                currjieTitle="";
                currjieId="";




                AppLoading.show(VpHeadEditsCustomerActicity.this);
                HttpUtil.getInstance().selectCityOrDistrictList(currShenId,"", new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        AppLoading.close();
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
                        if (optFlag.equals("yes")) {

                            String res = object.optString("res");

                            mPovinceCityBean =  new Gson().fromJson(res,PovinceCityBean.class);





                        } else {

                        }

                    }
                });


            }
        });
        builder.show();


    }



    String  currshiId;
    String  currshiTitle;
    PovinceCityBean mjieDaoBean;
    @OnClick(R.id.shi_et)
    void shi_et(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(VpHeadEditsCustomerActicity.this);
        builder.setTitle("请选择");
        List<String> list= new ArrayList<String>();
        if(mPovinceCityBean !=null && mPovinceCityBean.cityList!=null && mPovinceCityBean.cityList.size()>0){
            for ( PovinceCityBean.CityListItem mCityListItem: mPovinceCityBean.cityList) {
                list.add(mCityListItem.title);
            }
        }
        final String[]  listArr=  list.toArray(new String[]{});

        builder.setItems(listArr, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
//                     Toast.makeText(TheDeliveryHeadActivity.this, "车辆为：" + fhclArr[which], Toast.LENGTH_SHORT).show();
//                ck.setText(ckNamesArr[which]);
//                currCompanyId=ids.get(which);
//                pageno=1;
//                lodeOrder(currCompanyId,handleStatus ,pageno, 3);

                shi_et.setText(listArr[which]);
                currshiTitle=listArr[which];
                currshiId= mPovinceCityBean.cityList.get(which).id;



                jie_et.setText("");
                currjieTitle="";
                currjieId="";

                AppLoading.show(VpHeadEditsCustomerActicity.this);
                HttpUtil.getInstance().selectCityOrDistrictList("",currshiId, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        AppLoading.close();
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
                        if (optFlag.equals("yes")) {

                            String res = object.optString("res");
                            mjieDaoBean=  new Gson().fromJson(res,PovinceCityBean.class);


                        } else {

                        }

                    }
                });


            }
        });
        builder.show();


    }

    String  currjieId;
    String  currjieTitle;
//    PovinceCityBean mjieDaoBean;
    @OnClick(R.id.jie_et)
    void jie_et(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(VpHeadEditsCustomerActicity.this);
        builder.setTitle("请选择");
        List<String> list= new ArrayList<String>();
        if(mjieDaoBean!=null &&mjieDaoBean.districtList!=null && mjieDaoBean.districtList.size()>0){
            for ( PovinceCityBean.DistrictListItem mDistrictListItem: mjieDaoBean.districtList) {
                list.add(mDistrictListItem.title);
            }
        }
        final String[]  listArr=  list.toArray(new String[]{});

        builder.setItems(listArr, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                jie_et.setText(listArr[which]);
                currjieTitle=listArr[which];
                currjieId=mjieDaoBean.districtList.get(which).id;




            }
        });
        builder.show();


    }
    String  currtypeId;
    String  currtypeTitle;

    @OnClick(R.id.leixing_et)
    void leixing_et(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(VpHeadEditsCustomerActicity.this);
        builder.setTitle("请选择");
        List<String> list= new ArrayList<String>();
        if(mEditsCustomerBean !=null && mEditsCustomerBean.customerTypeList!=null && mEditsCustomerBean.customerTypeList.size()>0){
            for (   EditsCustomerBean.CustomerTypeListItem mCustomerTypeListItem: mEditsCustomerBean.customerTypeList) {
                list.add(mCustomerTypeListItem.typeName);
            }
        }
        final String[]  listArr=  list.toArray(new String[]{});

        builder.setItems(listArr, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                leixing_et.setText(listArr[which]);
                currtypeTitle=listArr[which];
                currtypeId= mEditsCustomerBean.customerTypeList.get(which).id;




            }
        });
        builder.show();


    }
    String  currCangkuId;
    String  currCangkuTitle;

    @OnClick(R.id.cangku_et)
    void cangku_et(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(VpHeadEditsCustomerActicity.this);
        builder.setTitle("请选择");
        List<String> list= new ArrayList<String>();
        if(mSelectInitInfoByCompanyBean!=null &&mSelectInitInfoByCompanyBean.mywarehouseList!=null && mSelectInitInfoByCompanyBean.mywarehouseList.size()>0){
            for (  SelectInitInfoByCompanyBean.MywarehouseListItem mMywarehouseListItem: mSelectInitInfoByCompanyBean.mywarehouseList) {
                list.add(mMywarehouseListItem.wmNm);
            }
        }
        final String[]  listArr=  list.toArray(new String[]{});

        builder.setItems(listArr, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                cangku_et.setText(listArr[which]);
                currCangkuTitle=listArr[which];
                currCangkuId=mSelectInitInfoByCompanyBean.mywarehouseList.get(which).id;




            }
        });
        builder.show();


    }

    String company_type;
    String company_id;

    //     TextView  ywy_et;
    private void init() {
//        mddzTv.setText("湖北武汉市");
//        qymc.setText(HttpBase.companyName_2);
        company_id = HttpBase.companyId_2;

    }




    private void loadData() {
        AppLoading.show(this);
        HttpUtil.getInstance().initUpdateShop( shopId, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppLoading.close();
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

                if (optFlag.equals("yes")) {

                    String res = object.optString("res");
                      mEditsCustomerBean =  new Gson().fromJson(res,EditsCustomerBean.class);
                        mendian.setText(getName());
                       currMenDianNm=getName();
                       currMenDianId= mEditsCustomerBean.shopMap.company_id;
                        khmc_et.setText(mEditsCustomerBean.shopMap.shop_name);
                    mdxxdz_tv.setText(mEditsCustomerBean.shopMap.address);
                    currtypeId= mEditsCustomerBean.shopMap.type_id;
                    if("2".equals(currtypeId)){
                        currtypeTitle="批发";
                    }else {
                        currtypeTitle="零售";
                    }
                    leixing_et.setText(currtypeTitle);
                    currCangkuId= mEditsCustomerBean.shopMap.wm_code;
                    currCangkuTitle= mEditsCustomerBean.shopMap.wm_nm;
                    cangku_et.setText(currCangkuTitle);
                    lianxiren_et.setText(mEditsCustomerBean.shopMap.contact_name);
                    lianxiren_et_phone.setText(mEditsCustomerBean.shopMap.contact_phone);
                    shjijian.setText(mEditsCustomerBean.shopMap.auto_days);
                    shen_et.setText(getProvince());
                    currShenId= mEditsCustomerBean.shopMap.province;
                    currShenTitle=getProvince();

                    shi_et.setText(getCitye());
                    currshiId= mEditsCustomerBean.shopMap.city;
                    currshiTitle=getCitye();

                    jie_et.setText(getDistrict());
                    currjieId= mEditsCustomerBean.shopMap.district;
                    currjieTitle=getDistrict();




                    HttpUtil.getInstance().selectInitInfoByCompanyId(currMenDianId,  new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            AppLoading.close();
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
                                String res = object.optString("res");

                                mSelectInitInfoByCompanyBean=  new Gson().fromJson(res,SelectInitInfoByCompanyBean.class);



                            } else {
                                ToasAlert.toastCenter("失败！");
                            }

                        }
                    });

                } else {

                }

            }
        });
    }


    EditsCustomerBean mEditsCustomerBean;
public String getName(){
    if(mEditsCustomerBean.companyList!=null && mEditsCustomerBean.companyList.size()>0&& mEditsCustomerBean.shopMap.company_id!=null){

        for (EditsCustomerBean.CompanyListItem mCompanyListItem: mEditsCustomerBean.companyList) {
            if(mEditsCustomerBean.shopMap.company_id.equals(mCompanyListItem.id)){
                return  mCompanyListItem.nm;

            }


        }

    }
 return "";

}

    public String getProvince(){
        if(mEditsCustomerBean.provinceList!=null && mEditsCustomerBean.provinceList.size()>0&& mEditsCustomerBean.shopMap.province!=null){

            for (EditsCustomerBean.ProvinceListItem mProvinceListItem: mEditsCustomerBean.provinceList) {
                if(mEditsCustomerBean.shopMap.province.equals(mProvinceListItem.id)){
                    return  mProvinceListItem.title;
                }

            }
        }
        return "";
    }
    public String getCitye(){
        if(mEditsCustomerBean.cityList!=null && mEditsCustomerBean.cityList.size()>0 && mEditsCustomerBean.shopMap.city!=null){

            for (EditsCustomerBean.CityListItem mCityListItem: mEditsCustomerBean.cityList) {
                if(mEditsCustomerBean.shopMap.city.equals(mCityListItem.id)){
                    return  mCityListItem.title;
                }

            }
        }
        return "";
    }
    public String getDistrict(){
        if(mEditsCustomerBean.districtList!=null && mEditsCustomerBean.districtList.size()>0&& mEditsCustomerBean.shopMap.district!=null){

            for (EditsCustomerBean.DistrictListItem mDistrictListItem: mEditsCustomerBean.districtList) {
                if(mEditsCustomerBean.shopMap.district.equals(mDistrictListItem.id)){
                    return  mDistrictListItem.title;
                }

            }
        }
        return "";
    }
//    String gpsAddress;
//    String gpsLongitude;
//    String gpsLatitude;

    @Override
    public void onDestroy() {
        super.onDestroy();
        //保存城市

        //停止定位
//        stopLocation();
    }

}
