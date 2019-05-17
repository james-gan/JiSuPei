package com.jisupei.vpnew.setting.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.jisupei.activity.base.AboutActivity;
import com.jisupei.activity.base.LoginActivity;
import com.jisupei.R;
import com.jisupei.activity.basis.ContactPeopleActicity;
import com.jisupei.activity.shippingaddress.ShippingAddressActicity;
import com.jisupei.activity.shippingaddress.UpdatePassWordActicity;
import com.jisupei.activity.tongji.StatisticalActicity;
import com.jisupei.application.MyApplication;
import com.jisupei.utils.db.DatabaseHelper;
import com.jisupei.ywy.BaseFragment;
import com.jisupei.http.HttpBase;
import com.jisupei.http.HttpUtil;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.AutoUtils;
import com.jisupei.vpnew.db.VpNewProductDb;
import com.jisupei.vpnew.order.VpNewAllOrderActicity;
import com.jisupei.vpnew.setting.NewCartAllFragmentActicity;
import com.jisupei.vpnew.setting.activity.VpNewCollectionActivity;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/18.
 */
public class VpNewSetingFragment extends BaseFragment {


     @InjectView(R.id.name)
     TextView name;
     @InjectView(R.id.cart_num1)
     TextView cart_num1;
     @InjectView(R.id.cart_num2)
     TextView cart_num2;
     @InjectView(R.id.cart_num3)
     TextView cart_num3;
     @InjectView(R.id.cart_num4)
     TextView cart_num4;
     @InjectView(R.id.cart_num5)
     TextView cart_num5;
     @InjectView(R.id.sumAmount)
     TextView sumAmount;
     @InjectView(R.id.llorder)
     RelativeLayout llorder;

    @InjectView(R.id.exit_ll)
    RelativeLayout exit_ll;
    @InjectView(R.id.daifukuan)
    RelativeLayout daifukuan;

    @InjectView(R.id.daipeisong)
    RelativeLayout daipeisong;

    @InjectView(R.id.peisongzhong)
    RelativeLayout peisongzhong;

    @InjectView(R.id.qisnshou)
    RelativeLayout qisnshou;

    @InjectView(R.id.quxiao)
    RelativeLayout quxiao;
    @InjectView(R.id.kabao)
    RelativeLayout kabao;

    @InjectView(R.id.phone)
    public   TextView phone;
    @InjectView(R.id.editname)
    public ImageView editname;


    public static VpNewSetingFragment getVpSetingFragmentInstance() {
        VpNewSetingFragment instance = new VpNewSetingFragment();
        return instance;
    }

     public void setingDefault(){

     }

    @Override
    public View initView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.vpnew_seting_fragment, null);
        ButterKnife.inject(this, rootView);
//        findViewIdHead(rootView);
//        include_title_tv.setText("订单");
        AutoUtils.auto(rootView);
        name.setText(HttpBase.shopName);
        AppUtils.expandViewTouchDelegate(editname, 30, 30, 50, 50);

//        panpay_name.setText("所属公司:"+HttpBase.companyName_2);
//        username.setText(HttpBase.realName_2);
//        phone.setText(HttpBase.phone_2);
        exit_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpBase.Exit(    getActivity());
                //启动登录页面,并清除所有栈内的activity
                Intent intent = new Intent();
                intent.setClass(    getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                MyApplication.instance.exit();

                try {
                    Dao<VpNewProductDb,Integer> mVpNewProductDbDao = DatabaseHelper.getHelper(getActivity()).getVpNewProductDbDaoDao();
                    List<VpNewProductDb> mVpNewProductDbList= mVpNewProductDbDao.queryBuilder().orderBy("id1", false).query();
                    if(mVpNewProductDbList!=null && mVpNewProductDbList.size()>0){
                        mVpNewProductDbDao.delete(mVpNewProductDbList);
                    }

//                    Dao<Product,Integer>  cartDao = DatabaseHelper.getHelper(getActivity()).getCartDao();
//                    Dao<RankPriceItem,Integer>getRankPriceDao = DatabaseHelper.getHelper(getActivity()).getRankPriceDao();
//                    List<Product> data= cartDao.queryBuilder().orderBy("id1", false).query();
//                    cartDao.delete(data);
//
//                    if(data!=null && data.size()>0){
//                        for (Product mProduct:data) {
//                            if(mProduct.mRankPriceList!=null&&mProduct.mRankPriceList.size()>0){
//                                getRankPriceDao.delete(mProduct.mRankPriceList);
//                            }
//                        }
//                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }



                getActivity().finish();
            }
        });
        llorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(    getActivity(), VpNewAllOrderActicity.class);
                startActivity(intent);
            }
        });
        daifukuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("nopay","no");
                intent.setClass(getActivity(), VpNewAllOrderActicity.class);
                startActivity(intent);
            }
        });
        daipeisong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("state","1");
                intent.setClass(getActivity(), VpNewAllOrderActicity.class);
                startActivity(intent);
            }
        });

        editname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("修改门店名称");
//                builder.setMessage("111");
                View view1 = View.inflate(getActivity(), R.layout.newvp_include_dalog, null);
                 final EditText  name_et= (EditText)view1.findViewById(R.id.name_et);
                 builder.setView(view1);
                 builder.setNegativeButton("取消", null);
                 builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                if(TextUtils.isEmpty(name_et.getText().toString().trim())){
                                    ToasAlert.toast("请输入门店名称！");
                                    return;
                                }

                                AppLoading.show(getActivity());
                                HttpUtil.getInstance().setAccountShopName(name_et.getText().toString().trim(),new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        Logger.e("TAG", e);
                                        AppLoading.close();
                                        ToasAlert.toast("连接服务器加载分类失败");

                                    }

                                    @Override
                                    public void onResponse(String response, int id) {

                                        AppLoading.close();

                                        Logger.e("TAG", response);
                                        JSONObject object = null;
                                        try {
                                            object = new JSONObject(response);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        String optFlag = object.optString("optFlag");

                                        if ( "yes".equals(optFlag)) {
                                            name.setText(name_et.getText().toString().trim());
                                            HttpBase.shopName=name_et.getText().toString().trim();
//                                            JSONArray res = object.optJSONArray("res");
                                            ToasAlert.toast("修改门店名称成功！");

                                        }else {
                                            ToasAlert.toast("暂时没有数据!");
                                        }

                                    }

                                });



                    }
                });
                builder.setCancelable(true);
                 builder.show();


            }
        });
        peisongzhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("state","2");
                intent.setClass(getActivity(), VpNewAllOrderActicity.class);
                startActivity(intent);
            }
        });

        qisnshou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("state","3");
                intent.setClass(getActivity(), VpNewAllOrderActicity.class);
                startActivity(intent);
            }
        });
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("state","4");
                intent.setClass(getActivity(), VpNewAllOrderActicity.class);
                startActivity(intent);
            }
        });
        //卡包
        kabao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("state","4");
                intent.setClass(getActivity(), NewCartAllFragmentActicity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }


    @Override
    public void initData() {

        getVpStateCount();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }
    public void   getVpStateCount(){

        if(!HttpBase.islogin){
//            ToasAlert.toast(HttpBase.islogin+"");
            return;
        }
        AppLoading.show(getActivity());
        HttpUtil.getInstance().getVpStateCount(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Logger.e("TAG", e);
                AppLoading.close();
                ToasAlert.toast("连接服务器加载分类失败");

            }

            @Override
            public void onResponse(String response, int id) {

                AppLoading.close();

                Logger.e("TAG", response);
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String optFlag = object.optString("optFlag");

                if ( "yes".equals(optFlag)) {
                    JSONArray res = object.optJSONArray("res");

                    if(res!=null && res.length()>0){
                        try {
                             JSONObject  json=     ((JSONObject)res.get(0));
                           String UnpaidCount=  json.optString("UnpaidCount");
                            if("0".equals(UnpaidCount)||TextUtils.isEmpty(UnpaidCount)){
                                cart_num1.setVisibility(View.GONE);
                            }else {
                                cart_num1.setVisibility(View.VISIBLE);
                                cart_num1.setText(UnpaidCount);
                            }

                            String PendingDistributionCount=json.optString("PendingDistributionCount");
                            if("0".equals(PendingDistributionCount)||TextUtils.isEmpty(PendingDistributionCount)){
                                cart_num2.setVisibility(View.GONE);
                            }else {
                                cart_num2.setVisibility(View.VISIBLE);
                                cart_num2.setText(PendingDistributionCount);
                            }


                            String ShippedCount=json.optString("ShippedCount");

                            if("0".equals(ShippedCount)||TextUtils.isEmpty(ShippedCount)){
                                cart_num3.setVisibility(View.GONE);
                            }else {
                                cart_num3.setVisibility(View.VISIBLE);
                                cart_num3.setText(ShippedCount);
                            }

                            cart_num4.setVisibility(View.GONE);
                            cart_num5.setVisibility(View.GONE);
                            String sumAmountVal=((JSONObject)res.get(0)).optString("sumAmount");
                            sumAmount.setText(sumAmountVal);

                        }  catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }else {
                    ToasAlert.toast("暂时没有数据!");
                }

            }

        });


    }

    @OnClick(R.id.shdz)
    void shdz(View view) {
        Intent intent = new Intent(getActivity(), ShippingAddressActicity.class);
        getActivity().startActivity(intent);
    }
    @OnClick(R.id.cglxr)
    void cglxr(View view) {
        Intent intent = new Intent(getActivity(), ContactPeopleActicity.class);

        getActivity().startActivity(intent);
    }
    @OnClick(R.id.tjjs)
    void tjjs(View view) {
        startActivity(new Intent(getActivity(), StatisticalActicity.class));
    }
    @OnClick(R.id.grsc)
    void grsc(View view) {
        startActivity(new Intent(getActivity(), VpNewCollectionActivity.class));
    }
    @OnClick(R.id.xgmm)
    void xgmm(View view) {
        Intent intent = new Intent(getActivity(), UpdatePassWordActicity.class);
        getActivity().startActivity(intent);
    }
    @OnClick(R.id.kfdh)
    void kfdh(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.update_dialog);

        builder.setMessage("确定呼叫电话吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phone.getText().toString());
                intent.setData(data);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        });
        builder.create().show();

    }
    //关务我们
    @OnClick(R.id.gywm)
    void gywm(View view) {
        startActivity(new Intent(getActivity(), AboutActivity.class));
    }
}
