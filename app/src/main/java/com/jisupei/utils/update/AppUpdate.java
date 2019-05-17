package com.jisupei.utils.update;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
        import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
        import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.j256.ormlite.dao.Dao;
        import com.jisupei.R;
import com.jisupei.application.MyApplication;
import com.jisupei.utils.db.DatabaseHelper;
import com.jisupei.http.HttpBase;
import com.jisupei.http.HttpUtil;
import com.jisupei.activity.base.model.Product;
import com.jisupei.activity.base.model.RankPriceItem;
        import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/9.
 */
public class AppUpdate {

    String DOWNLOAD_SAVE_PATH;

    Context context;

    Handler mHandler = new Handler();

    public static final String VERSION_REQUEST = "/appVersion/findNewVersion.do?";

    String appVersionName;

    int appVersioncode;

    public AppUpdate(Context context) {

        this.context = context;

        DOWNLOAD_SAVE_PATH = Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState()) ? Environment
                .getExternalStorageDirectory().getPath() : context
                .getCacheDir().getPath() + File.separator + "jisupei_download";

        PackageManager pm = context.getPackageManager();

        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        appVersionName = pi.versionName;

        appVersioncode = pi.versionCode;

    }

    public void getVersion() {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUtil.HOST_STRING + VERSION_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Logger.d("TAG", "返回升级信息+response -> " + response);
//                        if(true)return;
                        try {
                            JSONObject object =new JSONObject(response);

                            String flag = object.getString("optFlag");

                            if(flag.equals("yes")) {

                                JSONObject resObject=object.getJSONObject("res");

                                int versionCode = resObject.getInt("versionCode");
                                String mversionName = resObject.getString("versionName");
                                //符合条件弹窗更新

                                Logger.e("tag","新版本"+versionCode);
                                Logger.e("tag","老版本"+appVersioncode);
                                if(versionCode>appVersioncode){
                                    showUpdateDialog(resObject.getString("appUrl"),mversionName);
                                    MyApplication.instance.isUpdata=true;

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            MyApplication.instance.isUpdata=false;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.e("TAG", "返回失败"+error.getMessage(), error);
                MyApplication.instance.isUpdata=false;
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();
                try {
                    map.put("content", new JSONObject().put("deviceOS","android").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                map.put("version","1");
                map.put("login_token", HttpBase.token);
                map.put("deviceOS","android-"+android.os.Build.VERSION.RELEASE);
                return map;
            }
        };
        //超时时间10s,最大重连次数2次
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
        requestQueue.add(stringRequest);


    }

    private void showUpdateDialog(final String loadUrl,String mVersionName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.update_dialog);
        builder.setTitle("您有新的集速配版本！");
        builder.setMessage("发现有集速配新版本(v"+mVersionName+")，升级将极大的提高您的用户体验.");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                downFile(loadUrl);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//                System.exit(0);
            }

        });
        builder.create().show();
    }

    void downFile(final String loadUrl) {
        final ProgressDialog pBar = new ProgressDialog(context,ProgressDialog.THEME_HOLO_LIGHT);

        pBar.setTitle("请稍等");
        //设置对话进度条样式为水平
        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //设置提示信息
        pBar.setMessage("正在下载中......");
       pBar.setProgress(0);
        pBar.show();
        new Thread() {
            public void run() {
                try {
                    /**
                     * 创建住父文件路径
                     * 检查下载路径下是否存在相同文件
                     * 不存在则新建路径，存在删除原文件
                     * 以apk名字为文件名
                     */
                    File f = new File(DOWNLOAD_SAVE_PATH);
                    if (!f.exists()) {
                        f.mkdirs();
                    }

                    String[] urls = loadUrl.split("/");
                    String filepath = DOWNLOAD_SAVE_PATH + File.separator
                            + urls[urls.length - 1];

                    File apkFile = new File(filepath);
                    if (apkFile.exists()) {
                        apkFile.delete();
                    }
                    /**
                     *
                     * 下载apk文件
                     */

                    URL url = new URL(loadUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();
                    pBar.setMax(length);
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(
                                Environment.getExternalStorageDirectory(),
                                "JiSuPei.apk");
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[1024];   //这个是缓冲区，即一次读取10个比特，我弄的小了点，因为在本地，所以数值太大一 下就下载完了，看不出progressbar的效果。
                        int ch = -1;
                        int process = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            process += ch;
                            pBar.setProgress(process);
                            float all = length/1024/1024f;
                            float percent = process/1024/1024f;
                            pBar.setProgressNumberFormat(String.format("%.2fM/%.2fM", percent, all));


                        }

                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    //执行安装
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(context!=null){
                                HttpBase.Exit(context);
                                try {
                                    Dao<Product,Integer> cartDao = DatabaseHelper.getHelper(context).getCartDao();
                                    Dao<RankPriceItem,Integer> getRankPriceDao;     getRankPriceDao = DatabaseHelper.getHelper(context).getRankPriceDao();
                                    List<Product> data= cartDao.queryBuilder().orderBy("id1", false).query();

                                    cartDao.delete(data);
                                    if(data!=null && data.size()>0){
                                        for (Product mProduct:data) {
                                            if(mProduct.mRankPriceList!=null&&mProduct.mRankPriceList.size()>0){
                                                getRankPriceDao.delete(mProduct.mRankPriceList);
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                            pBar.cancel();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.addFlags(  Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setDataAndType(Uri.fromFile(new File(Environment
                                            .getExternalStorageDirectory(), "JiSuPei.apk")),
                                    "application/vnd.android.package-archive");
                            context.startActivity(intent);

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(context,"下载路径发生错误！",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }

        }.start();
    }


}
