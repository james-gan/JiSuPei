package com.jisupei.application;

import android.app.Activity;
import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;

import com.igexin.sdk.PushManager;
import com.jisupei.BuildConfig;
import com.jisupei.utils.ImagePipelineConfigFactory;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.pgyersdk.crash.PgyCrashManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/7/12.
 */
public class MyApplication extends Application {
  public   static MyApplication instance;
    //注册的信息
    //是否有更新
    public   boolean isUpdata=false;
    List<Activity> activityList = new LinkedList<Activity>();
    @Override
    public void onCreate() {
        super.onCreate();
        //使用  在Application里
        SDKInitializer.initialize(this);
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15000L, TimeUnit.MILLISECONDS)
                .readTimeout(15000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
        Fresco.initialize(this, ImagePipelineConfigFactory.getImagePipelineConfig(this));
        if (BuildConfig.LOG_DEBUG) {
            Logger.init("applog").logLevel((LogLevel.FULL));
            instance = this;
            // 个推SDK初始化，第三方程序启动时，都要进行SDK初始化工作
            PushManager.getInstance().initialize(this);
            //注册蒲公英
            PgyCrashManager.register(this);
        }

    }
        // 添加Activity到容器中
        public void addActivity (Activity activity){
            activityList.add(activity);
        }
        // 添加Activity到容器中
        public void removeActivity (Activity activity ){
            activityList.remove(activity);
        }
        // 遍历所有Activity并finish
        public void exit () {
            if (activityList != null && activityList.size() > 0) {
                for (Activity activity : activityList) {
                    activity.finish();
                }
                activityList.clear();
            }

        }

}
