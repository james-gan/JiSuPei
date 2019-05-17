package com.jisupei.vpheadquarters.index;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jisupei.R;
import com.jisupei.http.HttpUtil;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/7/12.
 */
public class VpHeadAddVouchersActicity extends Activity {
    private String[] requestPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    @InjectView(R.id.back_bt)
    ImageView backBt;
    @InjectView(R.id.textView)
    TextView textView;
    @InjectView(R.id.dyj_et)
    TextView dyj_et;
    @InjectView(R.id.mc_rl)
    RelativeLayout mc_rl;
    @InjectView(R.id.kaishiRL)
    RelativeLayout kaishiRL;
    @InjectView(R.id.jieshuRL)
    RelativeLayout jieshuRL;

    @OnClick(R.id.back_bt)
    void back(View view) {
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_vp_headadd_diyjuanadd);
        ButterKnife.inject(this);
        AppUtils.expandViewTouchDelegate(backBt, 30, 30, 50, 50);
        lodeCK();
        mc_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        jieshuRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        dyj_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(VpHeadAddVouchersActicity.this);
                        builder.setTitle("请选择");
                        final String[]  ckNamesArr=  ckNames.toArray(new String[]{});
                        builder.setItems(ckNamesArr, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dyj_et.setText(ckNamesArr[which]);
                                currId=ids.get(which);


                            }
                        });
                        builder.show();



            }
        });




    }
String currId;

    private void lodeCK() {
        AppLoading.show(VpHeadAddVouchersActicity.this);
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

}
