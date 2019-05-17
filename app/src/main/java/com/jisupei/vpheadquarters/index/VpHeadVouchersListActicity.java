package com.jisupei.vpheadquarters.index;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jisupei.R;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.AutoUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/12.
 */
public class VpHeadVouchersListActicity extends Activity {
    private String[] requestPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @InjectView(R.id.back_bt)
    ImageView backBt;
    @InjectView(R.id.textView)
    TextView textView;
    @InjectView(R.id.xinjian)
    TextView xinjian;

    @OnClick(R.id.back_bt)
    void back(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_vp_headadd_diyjuan);
        AutoUtils.auto(this);
        ButterKnife.inject(this);
        AppUtils.expandViewTouchDelegate(backBt, 30, 30, 50, 50);


    }
    @OnClick(R.id.xinjian)
    void xinjian(View view) { //新建
        Intent intent =  new Intent(this,VpHeadAddVouchersActicity.class);
        startActivity(intent);

    }



}
