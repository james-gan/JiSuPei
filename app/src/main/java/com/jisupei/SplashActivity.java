package com.jisupei;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import com.jisupei.activity.homepage2.MainActivity11;

import com.jisupei.ywy.HomePageActivity;
import com.jisupei.http.HttpBase;
import com.jisupei.utils.AutoUtils;
import com.jisupei.vpheadquarters.VpheadPageActivity;
import com.jisupei.vpnew.VpHomePageActivity;

import java.lang.reflect.Method;

/**
 * Created by xiayumo on 16/6/1.
 */
public class SplashActivity extends Activity{

    private MyCountDownTimer mc;

    private TextView count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.splash);
        AutoUtils.setSize(this, true, 720, 1280);
        com.jingchen.pulltorefresh.AutoUtils.setSize(this, true, 720, 1280);
        count = (TextView) findViewById(R.id.count);

        mc = new MyCountDownTimer(4000, 1000);

        mc.start();

        handler.postDelayed(MainRunnable, 3000);

        findViewById(R.id.splash_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
 
                String loginResultRoleId =  HttpBase.isShopOrYWy();
                if (!TextUtils.isEmpty(loginResultRoleId)) { //登录了

                    if("01".equals(loginResultRoleId)){//业务员
                        //业务员登录
                        HttpBase.Resume_2(SplashActivity.this);
                       startActivity(new Intent(SplashActivity.this, HomePageActivity.class));
                        //   startActivity(new Intent(SplashActivity.this, DriverHomePageActivity.class));
                    }else if("02".equals(loginResultRoleId)){ //司机
                    }
                    else if("0".equals(loginResultRoleId)){//vp
                        HttpBase.Resume_2(SplashActivity.this);
                        startActivity(new Intent(SplashActivity.this, VpheadPageActivity.class));
                    }
                    else {//门店登录
                         HttpBase.Resume(SplashActivity.this);
                        if("1".equals(HttpBase.isSelf)){//未批
                            startActivity(new Intent(SplashActivity.this, VpHomePageActivity.class));
                        }else{
                            startActivity(new Intent(SplashActivity.this, MainActivity11.class));

                        }
                    }

                } else {//没登录
                      Intent intent = new Intent(SplashActivity.this, VpHomePageActivity.class);
                    startActivity(intent);
                }


                handler.removeCallbacks(MainRunnable);

                finish();
            }
        });
    }

    private Runnable MainRunnable =new Runnable() {
        @Override
        public void run() {

            String loginResultRoleId =  HttpBase.isShopOrYWy();

            if (!TextUtils.isEmpty(loginResultRoleId)) {
                if("01".equals(loginResultRoleId)){//业务员
                    //业务员登录
                    HttpBase.Resume_2(SplashActivity.this);
                   startActivity(new Intent(SplashActivity.this, HomePageActivity.class));
                    // startActivity(new Intent(SplashActivity.this, DriverHomePageActivity.class));
                }else if("02".equals(loginResultRoleId)){ //司机
                }
                else if("0".equals(loginResultRoleId)){//vp总部
                    HttpBase.Resume_2(SplashActivity.this); startActivity(new Intent(SplashActivity.this, VpheadPageActivity.class));
                }
                else {//门店登录
                    HttpBase.Resume(SplashActivity.this);
                    if("1".equals(HttpBase.isSelf)){//未批
                        startActivity(new Intent(SplashActivity.this, VpHomePageActivity.class));
                    }else{
                        startActivity(new Intent(SplashActivity.this, MainActivity11.class));
                    }
                }
            } else {
                Intent intent = new Intent(SplashActivity.this, VpHomePageActivity.class);
                startActivity(intent);
            }

            finish();
        }
    };

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private Handler handler=new Handler();
    /**
     * 继承 CountDownTimer 防范
     *
     * 重写 父类的方法 onTick() 、 onFinish()
     */
    class MyCountDownTimer extends CountDownTimer {
        /**
         *
         * @param millisInFuture
         * 表示以毫秒为单位 倒计时的总数
         *
         * 例如 millisInFuture=1000 表示1秒
         *
         * @param countDownInterval
         * 表示 间隔 多少微秒 调用一次 onTick 方法
         *
         * 例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         *
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        public void onFinish() {
            count.setText("正在跳转");
        }
        public void onTick(long millisUntilFinished) {
            count.setText(millisUntilFinished / 1000+"");
        }
    }

}
