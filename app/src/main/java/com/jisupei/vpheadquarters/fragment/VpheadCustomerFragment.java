package com.jisupei.vpheadquarters.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableRecyclerView;
import com.jisupei.R;
import com.jisupei.ywy.BaseFragment;
import com.jisupei.ywy.customer.bean.Customer;
import com.jisupei.http.HttpUtil;
import com.jisupei.utils.AutoUtils;
import com.jisupei.vpheadquarters.VpHeadAddCustomerActicity;
import com.jisupei.vpheadquarters.adapter.VpheadCustomerAdapter;
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
import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/18.
 */
public class VpheadCustomerFragment extends BaseFragment {


    @InjectView(R.id.mRecyclerView)
    PullableRecyclerView mRecyclerView;

    @InjectView(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    @InjectView(R.id.ck_rl)
    RelativeLayout ck_rl;
    @InjectView(R.id.ck)
    TextView ck;

//    @InjectView(R.id.lin)
//    LinearLayout lin;

    public static VpheadCustomerFragment getCustomerFragmentInstance() {

        VpheadCustomerFragment instance = new VpheadCustomerFragment();
        return instance;
    }

    TranslateAnimation    mShowAction;
    @Override
    public View initView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.head_customer_fragment, null);
        ButterKnife.inject(this, rootView);
        AutoUtils.auto(rootView);
        EventBus.getDefault().register(this);
        ImageView     include_back_iv =     (ImageView)rootView.findViewById(R.id.include_back_iv);
        TextView   include_title_tv =     (TextView)rootView.findViewById(R.id.include_title_tv);
        ImageView   include_menu_iv =     (ImageView)rootView.findViewById(R.id.include_menu_iv);
        LinearLayout   head_include =     (LinearLayout)rootView.findViewById(R.id.head_include);

        include_title_tv.setText("我的客户");
        include_menu_iv.setVisibility(View.VISIBLE);//添加客户
        include_back_iv.setVisibility(View.GONE);
        include_menu_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(VpheadCustomerFragment.this.getActivity(), VpHeadAddCustomerActicity.class);
                getActivity().startActivity(intent);

//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("请选择");
//                builder.setNegativeButton(android.R.string.cancel, null);
//                builder.setItems(new String[]{"新增客户", "绑定客户"},
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //TODO
//                                if(which==0){
//                                    Intent intent = new Intent(VpheadCustomerFragment.this.getActivity(), VpHeadAddCustomerActicity.class);
//                                    getActivity().startActivity(intent);
//                                }else if(which==1){
//                                    Intent intent = new Intent(VpheadCustomerFragment.this.getActivity(), VpSearchAddCustomActivity.class);
//                                    getActivity().startActivity(intent);
//                                }
//                            }
//                        });
//                builder.create().show();


            }
        });
        pullToRefreshLayout.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                load(pullToRefreshLayout);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                page++;
                loadData(page,pullToRefreshLayout);

            }
        });

        pullToRefreshLayout.setPullUpEnable(true);

//             mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
//                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
//                1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
//               mShowAction.setDuration(500);
//        rootView.findViewById(R.id.include_title_tv).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lin.startAnimation(mShowAction);
//                lin.setVisibility(View.VISIBLE);
//            }
//        });


        ck_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("请选择");

                final String[]  ckNamesArr=  ckNames.toArray(new String[]{});
                builder.setItems(ckNamesArr, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
//                     Toast.makeText(TheDeliveryHeadActivity.this, "车辆为：" + fhclArr[which], Toast.LENGTH_SHORT).show();
                        ck.setText(ckNamesArr[which]);
                        currCompanyId=idsList.get(which);
                        page=1;
                        loadData(page,null);  //加载数据

                    }
                });
                builder.show();
            }
        });
        return rootView;
    }

    @Override
    public void initData() {

        load(null);





    }


    public void onEventMainThread(String  refresh) {

        if("VpheadCustomerFragment".equals(refresh)){
               page=1;
              loadData(1,null);
        }

    }
    List<String> idsList= new ArrayList<String>();
    List<String> ckNames= new ArrayList<String>();
    String currCompanyId;

   public  void load(final PullToRefreshLayout pullToRefreshLayout){
           if(getActivity()!=null){
               AppLoading.show(getActivity());
           }

       HttpUtil.getInstance().initShopPageMsg(new StringCallback() {
           @Override
           public void onError(Call call, Exception e, int id) {
               AppLoading.close();
               ToasAlert.toast("连接服务器失败");
               if(pullToRefreshLayout!=null)
               pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
           }

           @Override
           public void onResponse(String response, int id) {
               if(pullToRefreshLayout!=null)
               pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
               AppLoading.close();
               Logger.e("", "返回订单列表 ->" + response);

               AppLoading.close();
               JSONObject object = null;
               try {
                   object = new JSONObject(response);
               } catch (JSONException e) {
                   e.printStackTrace();
               }
               String optFlag = object.optString("optFlag");

               if ("yes".equals(optFlag)) {
                   idsList.clear();
                   ckNames.clear();
                     JSONArray companyList = object.optJSONObject("res").optJSONArray("companyList");
                        for(int i=0;i<companyList.length();i++){
                            try {
                                JSONObject  itemObj=   (JSONObject)companyList.get(i);

                                   ids+=itemObj.optString("id")+",";


                                idsList.add( itemObj.optString("id")) ;
                                ckNames.add( itemObj.optString("nm")) ;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                      ids=ids.substring(0,ids.length()-1);

                       page=1;
                       loadData(page,null);
                       currCompanyId=ids;




               }else{
                   ToasAlert.toastCenter("没有查询到客户数据！");
               }
           }
       });
   }

String ids="";
int page=1;
    public  void  loadData(final int page, final PullToRefreshLayout pullToRefreshLayout){

        HttpUtil.getInstance().selectShopList(page,currCompanyId,"",new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppLoading.close();
                ToasAlert.toast("连接服务器失败");
                if(pullToRefreshLayout!=null)
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
            @Override
            public void onResponse(String response, int id) {
                if(pullToRefreshLayout!=null)
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                AppLoading.close();
                Logger.e("", "返回订单列表 ->" + response);

                AppLoading.close();
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String optFlag = object.optString("optFlag");

                if ("yes".equals(optFlag)) {
                    String shopList = object.optString("res");
//
                    List<Customer> mCustomerListTemp = new Gson().fromJson(shopList, new TypeToken<ArrayList<Customer>>() {
                    }.getType());
                    //

                    if(page==1){
                        mCustomerList=mCustomerListTemp;

                        if(mCustomerList!=null && mCustomerList.size()>0){

                            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
                            //                       linearLayoutManager.setOrientation();
                            mRecyclerView.setLayoutManager(linearLayoutManager);
                              mCustomerAdapter = new VpheadCustomerAdapter(getActivity(),null,mCustomerList);
                            mRecyclerView.setAdapter(mCustomerAdapter);
                        }else {

                            ToasAlert.toastCenter("没有查询到客户数据！");
                        }


                    }else {
                        mCustomerList.addAll(mCustomerListTemp);
                        mCustomerAdapter.notifyDataSetChanged();

                    }



                }else{
                    ToasAlert.toastCenter("没有查询到客户数据！");
                }
            }
        });


    }
    VpheadCustomerAdapter mCustomerAdapter;
     List<Customer> mCustomerList;
}
