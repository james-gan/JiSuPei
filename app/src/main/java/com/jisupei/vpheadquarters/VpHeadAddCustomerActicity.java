package com.jisupei.vpheadquarters;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jisupei.R;
import com.jisupei.http.HttpBase;
import com.jisupei.http.HttpUtil;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.AutoUtils;
import com.jisupei.utils.BitmapUtils;
import com.jisupei.utils.ImageUtil;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.jisupei.utils.zoomimage.PicturePreviewActivity;
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
public class VpHeadAddCustomerActicity extends Activity {
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
    @InjectView(R.id.custom_accout) //登录帐号
            EditText custom_accout;
    @InjectView(R.id.custom_accout_pwd) //登录密码
            EditText custom_accout_pwd;
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


    @InjectView(R.id.add1_iv)
    SimpleDraweeView add1_iv;
    @InjectView(R.id.add2_iv)
    SimpleDraweeView add2_iv;
    @InjectView(R.id.add3_iv)
    SimpleDraweeView add3_iv;


    @InjectView(R.id.add1_iv_del)
    ImageView add1_iv_del;
    @InjectView(R.id.add2_iv_del)
    ImageView add2_iv_del;
    @InjectView(R.id.add3_iv_del)
    ImageView add3_iv_del;


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
        if (TextUtils.isEmpty(custom_accout.getText().toString().trim())) {
            ToasAlert.toastCenter("请填写登录账号");
            return;
        }
        if (TextUtils.isEmpty(custom_accout_pwd.getText().toString().trim())) {
            ToasAlert.toastCenter("请填写登录账号密码");
            return;
        }


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
        if (!TextUtils.isEmpty(imagePath1)) {
            fileList.add(new File(imagePath1));
        }
        if (!TextUtils.isEmpty(imagePath2)) {
            fileList.add(new File(imagePath2));
        }
        if (!TextUtils.isEmpty(imagePath3)) {
            fileList.add(new File(imagePath3));
        }
        if (fileList.size() < 1) {
            ToasAlert.toastCenter("请上传门店照片");
            return;

        }
//        if (!isTrue) {
//            checkAccunt(custom_accout.getText().toString());
//            return;
//        }
        JSONObject Obj = new JSONObject();
        try {
            Obj.put("companyId", currMenDianId);
            Obj.put("shopName", khmc_et.getText().toString()); //客户名称
            Obj.put("wmCode", currCangkuId);
            Obj.put("contactName", lianxiren_et.getText().toString());
            Obj.put("contactPhone", lianxiren_et_phone.getText().toString());
            Obj.put("province", currShenId);
            Obj.put("city", currshiId);
            Obj.put("district", currjieId);
            Obj.put("address", mdxxdz_tv.getText().toString());
            Obj.put("shopArea", "");
            Obj.put("account", HttpBase.account_2);
            Obj.put("autoDays", shjijian.getText().toString());
            Obj.put("loginAccount", custom_accout.getText().toString());
            Obj.put("psw", custom_accout_pwd.getText().toString());
            Obj.put("typeId", currtypeId);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        AppLoading.show(this);
        HttpUtil.getInstance().vpHeadaddShop(Obj.toString(), fileList, new StringCallback() {
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
                    VpHeadAddCustomerActicity.this.finish();
                    ToasAlert.toastCenter("添加客户成功");
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
        setContentView(R.layout.acticity_vp_headadd_cus_reg);
        AutoUtils.auto(this);
        ButterKnife.inject(this);
        AppUtils.expandViewTouchDelegate(backBt, 30, 30, 50, 50);
        init();
        loadData();
    }


    String  currMenDianId;
    String  currMenDianNm;
    @OnClick(R.id.mendian)
    void mendian(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(VpHeadAddCustomerActicity.this);

        builder.setTitle("请选择");

        List<String> list= new ArrayList<String>();
        if(mAddCustomerBean.companyList!=null && mAddCustomerBean.companyList.size()>0){
            for (AddCustomerBean.CompanyListItem mCompanyListItem: mAddCustomerBean.companyList) {
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
                currMenDianId=mAddCustomerBean.companyList.get(which).id;

                AppLoading.show(VpHeadAddCustomerActicity.this);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(VpHeadAddCustomerActicity.this);
        builder.setTitle("请选择");
        List<String> list= new ArrayList<String>();
        if(mAddCustomerBean.provinceList!=null && mAddCustomerBean.provinceList.size()>0){
            for (AddCustomerBean.ProvinceListItem mProvinceListItem: mAddCustomerBean.provinceList) {
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
                currShenId=mAddCustomerBean.provinceList.get(which).id;


                shi_et.setText("");//初始话数据
                currshiTitle="";
                currshiId="";
                jie_et.setText("");
                currjieTitle="";
                currjieId="";




                AppLoading.show(VpHeadAddCustomerActicity.this);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(VpHeadAddCustomerActicity.this);
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

                AppLoading.show(VpHeadAddCustomerActicity.this);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(VpHeadAddCustomerActicity.this);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(VpHeadAddCustomerActicity.this);
        builder.setTitle("请选择");
        List<String> list= new ArrayList<String>();
        if(mAddCustomerBean!=null &&mAddCustomerBean.customerTypeList!=null && mAddCustomerBean.customerTypeList.size()>0){
            for (  AddCustomerBean.CustomerTypeListItem mCustomerTypeListItem: mAddCustomerBean.customerTypeList) {
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
                currtypeId=mAddCustomerBean.customerTypeList.get(which).id;




            }
        });
        builder.show();


    }
    String  currCangkuId;
    String  currCangkuTitle;

    @OnClick(R.id.cangku_et)
    void cangku_et(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(VpHeadAddCustomerActicity.this);
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
        add1_iv_del.setVisibility(View.GONE);
        add2_iv_del.setVisibility(View.GONE);
        add3_iv_del.setVisibility(View.GONE);
//        custom_accout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
////
//                } else {
//                    //失去焦点
//                    if (!TextUtils.isEmpty(custom_accout.getText().toString())) {
//                        String custom_accoutStr = custom_accout.getText().toString();
//
////                        checkAccunt(custom_accoutStr);
//
//
//                    } else {
////                        account_msg.setText("");
//                    }
//
//                }
//            }
//        });

//        lianxiren_et_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
////
//                } else {
//                    //失去焦点
//                    if (!TextUtils.isEmpty(lianxiren_et_phone.getText().toString())) {
//
//                        custom_accout.setText(lianxiren_et_phone.getText().toString());
//                        custom_accout_pwd.setText(lianxiren_et_phone.getText().toString());
////                        mima_msg.setVisibility(View.VISIBLE);
//                    } else {
//                        custom_accout.setText("");
//                        custom_accout_pwd.setText("");
////                        mima_msg.setVisibility(View.GONE);
//                    }
//
//                }
//            }
//        });

//        initGPS();

    }

//    boolean isTrue = false;

    //  检查用户名
//    public void checkAccunt(String accunt) {
//        AppLoading.show(this);
//        HttpUtil.getInstance().getIsCustomAccount(accunt, new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                ToasAlert.toastCenter("连接服务器失败");
//
//                AppLoading.close();
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//                Logger.d("app2.0", response);
//                AppLoading.close();
//                JSONObject object = null;
//                try {
//                    object = new JSONObject(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                String optFlag = object.optString("optFlag");
//                if (optFlag.equals("yes")) {
////                    account_msg.setText("提示:用户名可用");
//                    isTrue = true;
//                } else {
////                    account_msg.setText(object.optString("optDesc"));
//                }
//            }
//        });
//
//
//    }


    //门店区域
    String mdid;
    //仓库
    String ckcode;

    String provinceid = "18";
    String cityid = "200";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 168) {
            //做需要做的事情，比如再次检测是否打开GPS了 或者定位
//            initGPS();
        }
        if (resultCode == 200) {    // 省市
            String di = data.getStringExtra("dz");
            provinceid = data.getStringExtra("provinceid");
            cityid = data.getStringExtra("cityid");
//                      "id" : 18,
//                    "title" : "湖北",
//            mddzTv.setText(di);
//          mRequestRegPam.address=di;

//            mRequestRegPam.province=provinceid;
//            mRequestRegPam.city=cityid;
        }
        if (resultCode == 202) {
            String md = data.getStringExtra("md");
            mdid = data.getStringExtra("mdid");
//            mdqyuTv.setText(md);
//            mRequestRegPam.shopArea=mdid;
        }
        if (resultCode == 203) {        //仓库
            String ck = data.getStringExtra("ck");
            ckcode = data.getStringExtra("ckcode");
//            psckTv.setText(ck);

//            mRequestRegPam.wmCode=ckcode;
        }

        //图片
        if (requestCode == PHOTO_REQUEST_GALLERY) {   //相册中获取
            if (data != null) {
                // 得到图片的全路径
                Uri mUri = data.getData();

                String path = ImageUtil.getPath(VpHeadAddCustomerActicity.this, mUri);
                String pathUpdate = BitmapUtils.compressImageUpload(path);

                Uri uri = Uri.parse("file://包名(实际可以是任何字符串甚至留空)/" + pathUpdate);
                if (index == 1) {
                    add1_iv.setImageURI(uri);
                    imagePath1 = pathUpdate;
                    add1_iv_del.setVisibility(View.VISIBLE);
                } else if (index == 2) {
                    add2_iv.setImageURI(uri);
                    imagePath2 = pathUpdate;
                    add2_iv_del.setVisibility(View.VISIBLE);
                } else if (index == 3) {
                    add3_iv.setImageURI(uri);
                    imagePath3 = pathUpdate;
                    add3_iv_del.setVisibility(View.VISIBLE);
                }

            }
        } else if (resultCode == RESULT_OK && requestCode == PHOTO_REQUEST_CAMERA && data == null) {   //照相中获取
            if (hasSdcard()) {
                if (tempCameraFile != null && tempCameraFile.exists()) {
//                    File    tempCameraFile = new File(Environment.getExternalStorageDirectory(),ExceptionPopupWindow.PHOTO_FILE_NAME);
//                  Uri    uri = Uri.fromFile(tempCameraFile);
//                    ImageUtil.getimage(ExceptionPopupWindow.tempCameraFile.getAbsolutePath());
                    //                ToasAlert.toast(ExceptionPopupWindow.tempCameraFile.getAbsolutePath());
                    String pathUpdate = BitmapUtils.compressImageUpload(tempCameraFile.getAbsolutePath());

                    Uri uri = Uri.parse("file://包名(实际可以是任何字符串甚至留空)/" + pathUpdate);
                    if (index == 1) {
                        add1_iv.setImageURI(uri);
                        imagePath1 = pathUpdate;
                        add1_iv_del.setVisibility(View.VISIBLE);
                    } else if (index == 2) {
                        add2_iv.setImageURI(uri);
                        imagePath2 = pathUpdate;
                        add2_iv_del.setVisibility(View.VISIBLE);
                    } else if (index == 3) {
                        add3_iv.setImageURI(uri);
                        imagePath3 = pathUpdate;

                        add3_iv_del.setVisibility(View.VISIBLE);
                    }
                }

            } else {
//                Toast.makeText(SelectPicActivity.this, "未找到存储卡，无法存储照片！", 0).show();
            }

        }

    }

    /* 头像名称 */
    public static final String PHOTO_FILE_NAME = "temp_photo.jpg";  //拍照后的名称
    /***
     * 使用照相机拍照获取图片
     */
    public static final int PHOTO_REQUEST_CAMERA = 1;
    /***
     * 使用相册中的图片
     */
    public static final int PHOTO_REQUEST_GALLERY = 2;
    // 剪切的结果
    public static final int PHOTO_REQUEST_CUT = 3;

    String imagePath1;
    String imagePath2;
    String imagePath3;

    @OnClick(R.id.add1_iv)
    void add1_iv(View view) {
        if (!TextUtils.isEmpty(imagePath1)) {
            Intent intent = new Intent(this, PicturePreviewActivity.class);
            intent.putExtra("url", imagePath1);
            // intent.putExtra("smallPath", getSmallPath());
            intent.putExtra("indentify", 0);
            this.startActivity(intent);
        } else {
            showPicturePicker(1);
        }


    }

    @OnClick(R.id.add2_iv)
    void add2_iv(View view) {


        if (!TextUtils.isEmpty(imagePath2)) {
            Intent intent = new Intent(this, PicturePreviewActivity.class);
            intent.putExtra("url", imagePath2);
            // intent.putExtra("smallPath", getSmallPath());
            intent.putExtra("indentify", 0);
            this.startActivity(intent);
        } else {
            showPicturePicker(2);
        }

    }

    @OnClick(R.id.add3_iv)
    void add3_iv(View view) {

        if (!TextUtils.isEmpty(imagePath3)) {
            Intent intent = new Intent(this, PicturePreviewActivity.class);
            intent.putExtra("url", imagePath3);
            // intent.putExtra("smallPath", getSmallPath());
            intent.putExtra("indentify", 0);

            this.startActivity(intent);
        } else {
            showPicturePicker(3);
        }


    }

    @OnClick(R.id.add1_iv_del)
    void add1_iv_del(View view) {
        //
        imagePath1 = null;
        add1_iv.setImageURI("");
        add1_iv_del.setVisibility(View.GONE);
    }

    @OnClick(R.id.add2_iv_del)
    void add2_iv_del(View view) {
        imagePath2 = null;
        add2_iv.setImageURI("");
        add2_iv_del.setVisibility(View.GONE);
    }

    @OnClick(R.id.add3_iv_del)
    void add3_iv_del(View view) {

        imagePath3 = null;
        add3_iv.setImageURI("");
        add3_iv_del.setVisibility(View.GONE);
    }

    // 上传图片
    private void showPicturePicker(final int addIndex) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择");
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setItems(new String[]{"拍照", "选择照片"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO
                        if (which == 0) {
                            camera(addIndex);
                        } else if (which == 1) {
                            gallery(addIndex);
                        }
                    }
                });
        builder.create().show();
    }

    /*
        * 从相机获取
        */
    public static File tempCameraFile = null;

    /*
   * 从相册获取
   */
    public void gallery(int addIndex) {
        index = addIndex;
        // 激活系统图库，选择一张图片 ，一般用 ACTION_GET_CONTENT
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setAction(Intent.ACTION_PICK);
//        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    int index = 0;

    public void camera(int addIndex) {
        index = addIndex;
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
//            index++;
            if (tempCameraFile != null && tempCameraFile.exists()) {
                tempCameraFile.delete();
            }
            File fileFolder = new File(Environment.getExternalStorageDirectory()
                    + "/jisupei/");
            if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"jisupei"的目录
                fileFolder.mkdir();
            }
            try {
                tempCameraFile = new File(fileFolder, "iv" + PHOTO_FILE_NAME);

            } catch (Exception e1) {
                e1.printStackTrace();
                ToasAlert.toast("请打开手机文件读写权限");
                return;
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempCameraFile));

            startActivityForResult(intent, PHOTO_REQUEST_CAMERA);

        } else {
//            Toast.makeText(mContext, "无内存卡~", 1).show();
            ToasAlert.toast("无内存卡~");
        }

    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private void loadData() {
        AppLoading.show(this);
        HttpUtil.getInstance().initAddShop( new StringCallback() {
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

                      mAddCustomerBean=  new Gson().fromJson(res,AddCustomerBean.class);






                } else {

                }

            }
        });
    }


    AddCustomerBean mAddCustomerBean;




    String gpsAddress;
    String gpsLongitude;
    String gpsLatitude;

    @Override
    public void onDestroy() {
        super.onDestroy();
        //保存城市

        //停止定位
//        stopLocation();
    }

}
