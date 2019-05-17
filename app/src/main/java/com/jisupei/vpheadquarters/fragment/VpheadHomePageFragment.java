package com.jisupei.vpheadquarters.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jisupei.application.MyApplication;
import com.jisupei.activity.base.ContentValue;
import com.jisupei.activity.base.LoginActivity;
import com.jisupei.R;
import com.jisupei.ywy.BaseFragment;
import com.jisupei.http.HttpBase;
import com.jisupei.http.HttpUtil;
import com.jisupei.utils.update.AppUpdate;
import com.jisupei.utils.AppUtils;
import com.jisupei.vpheadquarters.index.VpHeadVouchersListActicity;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/18.
 */
public class VpheadHomePageFragment extends BaseFragment {



    public static VpheadHomePageFragment getHomePageFragmentInstance (){

        VpheadHomePageFragment instance = new VpheadHomePageFragment();
        return instance;
    }


    @Override
    public View initView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.vphead_fragment_homepage, null);
        ButterKnife.inject(this, rootView);
//        AutoUtils.auto(rootView);
         if(!EventBus.getDefault().isRegistered(this)) {
             EventBus.getDefault().register(this);
         }
        return rootView;
    }

    @Override
    public void initData() {
        View view  =   getRootView();
        TextView     shop_user_name  =(TextView)view.findViewById(R.id.shop_user_name);
        TextView     shop_name  =(TextView)view.findViewById(R.id.shop_name);
        ImageView     exit_app  =(ImageView)view.findViewById(R.id.exit_app);

        RelativeLayout     daishenhe1  =(RelativeLayout)view.findViewById(R.id.daishenhe1);
        RelativeLayout     daipeisong1  =(RelativeLayout)view.findViewById(R.id.daipeisong1);
        RelativeLayout     dyj  =(RelativeLayout)view.findViewById(R.id.dyj);
        RelativeLayout     wzf_s1  =(RelativeLayout)view.findViewById(R.id.wzf_s1);
        RelativeLayout     daiyichang  =(RelativeLayout)view.findViewById(R.id.daiyichang);
        daishenhe1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post("fromHomeDaiShenHeResh");
            }
        });
        dyj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent inte=new Intent(getActivity(),VpHeadVouchersListActicity.class);
                startActivity(inte);

            }
        });

        daipeisong1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post("fromHomeDaiPeiSongResh");
            }
        });



        //
              icondaishenhe1_num11  =(TextView)view.findViewById(R.id.icondaishenhe1_num11);
              icondaipeisong1_num11  =(TextView)view.findViewById(R.id.icondaipeisong1_num11);
              weizfu_num11  =(TextView)view.findViewById(R.id.weizfu_num11);
              daiyic_num1  =(TextView)view.findViewById(R.id.daiyic_num1);



            jingriprice  =(TextView)view.findViewById(R.id.jingriprice);
            benzhouprice  =(TextView)view.findViewById(R.id.benzhouprice);
            benyueprice  =(TextView)view.findViewById(R.id.benyueprice);
            zongprice  =(TextView)view.findViewById(R.id.zongprice);

        //  弹出二维码
//        LinearLayout     alert_yqm  =(LinearLayout)view.findViewById(R.id.alert_yqm);
//
//        alert_yqm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                QrcodeDialog mQrcodeDialog=  new QrcodeDialog(getActivity());
//                mQrcodeDialog.show();
//            }
//        });
        AppUtils.expandViewTouchDelegate(exit_app, 30, 30, 50, 50);
        exit_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.update_dialog);
                builder.setTitle("提示");
                builder.setMessage("确定退出账号吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        exitLogin();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }

                });
                builder.create().show();


//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                getActivity().startActivity(intent);
            }
        });
        shop_user_name.setText(HttpBase.account_2+"("+HttpBase.realName_2+")");
        shop_name.setText(HttpBase.companyName_2);


        //更新
     new    AppUpdate(getActivity()).getVersion();
        //加载数据
        loadData();
    }
    //邀请码
      TextView     yqm_text ;
      TextView     daishenghe_num;
      TextView     daifahuo_num;
      TextView     daicli_num ;
      TextView     yicli_num ;


    TextView     icondaishenhe1_num11;
    TextView     icondaipeisong1_num11;
    TextView     weizfu_num11;
    TextView     daiyic_num1;



    TextView   jingriprice;
    TextView    benzhouprice;
    TextView     benyueprice;
    TextView     zongprice  ;
   public  void loadData(){
       AppLoading.show(getContext());
       HttpUtil.getInstance().manageHome(new StringCallback() {
           @Override
           public void onError(Call call, Exception e, int id) {
               ToasAlert.toastCenter("连接服务器失败，请稍后再试！");
               AppLoading.close();
           }

           @Override
           public void onResponse(String response, int id) {
               AppLoading.close();
               Logger.e("tag", "返回订单列表 ->" + response);

               AppLoading.close();
               JSONObject object = null;

               try {
                   object = new JSONObject(response);
               } catch (JSONException e) {
                   e.printStackTrace();
               }
               String optFlag = object.optString("optFlag");

               if ("yes".equals(optFlag)) {
                   JSONObject resJSONObject = object.optJSONObject("res");
                    String waitCheckOrderCount= resJSONObject.optString("waitCheckOrderCount");
                   icondaishenhe1_num11.setText(waitCheckOrderCount);
                    String waitSendOrderCount= resJSONObject.optString("waitSendOrderCount");
                   icondaipeisong1_num11.setText(waitSendOrderCount);
                   String unPaidCount= resJSONObject.optString("unPaidCount");
                   weizfu_num11.setText(unPaidCount);
                   String waitHandleErro= resJSONObject.optString("waitHandleErro");
                   daiyic_num1.setText(waitHandleErro);



                       jingriprice.setText("￥"+resJSONObject.optString("todayOrderSumAmount"));
                        benzhouprice.setText("￥"+resJSONObject.optString("weekOrderSumAmount"));
                         benyueprice.setText("￥"+resJSONObject.optString("monthOrderSumAmount"));
                         zongprice .setText("￥"+resJSONObject.optString("totalOrderSumAmount"));
               }
           }
       });
   }

    //刷新
    public void onEventMainThread(String refresh) {

        if (ContentValue.RECEIPTSREFRESH.equals(refresh)) {
            loadData();
        }
        if (ContentValue.EXCEPTIONREFRESH.equals(refresh)) { //回复异常DealWithExceptionActivity
            loadData();
        }

    }
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }







    public static final String EXIT_LOGIN_REQUEST="/appAccount/loginOut.do?";
    private void exitLogin() {

//        AppLoading.show(getActivity());
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUtil.HOST_STRING + EXIT_LOGIN_REQUEST,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        Logger.d("TAG", "退出登录返回结果+response -> " + response);
//
//                        AppLoading.close();
//
//                        try {
//                            JSONObject object =new JSONObject(response);
//                            String flag = object.optString("optFlag");
//
////                            if(flag.equals("yes")) {
//

                            //清空购物车数据库
//                                Dao<Product,Integer> cartDao = DatabaseHelper.getHelper(PersonInfoActivity.this).getCartDao();
//                             //   List<Product> carts = cartDao.queryForAll();
//                            List<Product> carts = cartDao.queryBuilder().orderBy("id1", false).where().eq("account",HttpBase.getAccount(getApplicationContext())).query();
//                                cartDao.delete(carts);

                            //启动登录页面,并清除所有栈内的activity
                           //退出登录
                            HttpBase.Exit(getActivity());
                            Intent intent = new Intent();
                            intent.setClass(getActivity(),
                                    LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            MyApplication.instance.exit();
                            getActivity().finish();
//
////                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//
//                Map<String, String> map = new HashMap<>();
//                map.put("content", new JSONObject().toString());
//                map.put("version","1");
//                map.put("login_token",HttpBase.token);
//                map.put("deviceOS","android-"+android.os.Build.VERSION.RELEASE);
//                return map;
//            }
//        };
//        //超时时间10s,最大重连次数2次
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
//        requestQueue.add(stringRequest);
    }




}
