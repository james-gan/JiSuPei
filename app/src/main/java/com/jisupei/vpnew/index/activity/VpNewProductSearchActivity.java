package com.jisupei.vpnew.index.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableRecyclerView;
import com.jisupei.activity.base.MainActivity;
import com.jisupei.R;
import com.jisupei.activity.homepage2.MainActivity11;
import com.jisupei.application.MyApplication;
import com.jisupei.utils.db.DatabaseHelper;
import com.jisupei.ywy.BaseActivity;
import com.jisupei.http.HttpUtil;
import com.jisupei.activity.base.model.Product;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.AutoUtils;
import com.jisupei.utils.StatusBarUtil;
import com.jisupei.vpnew.index.model.ProductVpNewSearch;
import com.jisupei.vpnew.index.widget.AddNewVpIndexSearchPop;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/18.
 */
public class VpNewProductSearchActivity extends BaseActivity {

    @InjectView(R.id.back_bt)
    ImageView back_bt;

    @InjectView(R.id.id_stickynavlayout_innerscrollview)
    PullableRecyclerView id_stickynavlayout_innerscrollview;
    @InjectView(R.id.search_ed)
    EditText search_ed;
    @InjectView(R.id.sera)
    TextView sera;

    @InjectView(R.id.pullToRefreshLayout)
    public PullToRefreshLayout pullToRefreshLayout;
    //刷新
    public static  boolean  shRef = true;
    @Override
    public void setLayoutContentView(Bundle savedInstanceState) {
        setContentView(R.layout.newvp_product_serach);
        ButterKnife.inject(this);
//        AutoUtils.auto(this);
        StatusBarUtil.setStatusBarColor(this,R.color.blue_head);


        MyApplication.instance.addActivity(this);

        EventBus.getDefault().register(this);

        AppUtils.expandViewTouchDelegate(back_bt, 30, 30, 50, 50);
        back_bt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish();
             }
         });
        sera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageno=1;
                load(search_ed.getText().toString().trim(),null);
            }
        });
        search_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        pullToRefreshLayout.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pageno=1;
                load(search_ed.getText().toString().trim(),pullToRefreshLayout);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

                load(search_ed.getText().toString().trim(),pullToRefreshLayout);
            }
        });
        pullToRefreshLayout.setPullUpEnable(false);
        AutoUtils.auto(pullToRefreshLayout.defaultRefreshView);

    }
    public void onEventMainThread(String refresh) {


    }

    int pageno=1;
    String classId="";
    String searchText="";

    @Override
    public void initData() {
        if(!TextUtils.isEmpty(searchText))
          load(searchText,null);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.instance.removeActivity(this);
    }
    public  void load(String searchText , final PullToRefreshLayout pullToRefreshLayout){

        AppLoading.show(this);
        HttpUtil.getInstance().serarchProductList(searchText, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppLoading.close();
                ToasAlert.toastCenter("连接服务器失败，请稍后再试！");
                    if(pullToRefreshLayout!=null)
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
            @Override
            public void onResponse(String response, int id) {
                    if(pullToRefreshLayout!=null)
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                Logger.d("app2.0",response);
                AppLoading.close();
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String optFlag = object.optString("optFlag");

                if("yes".equals(optFlag)) {
                    String list = object.optJSONObject("res").optString("list");
                    List<ProductVpNewSearch> 	   arrProduct = new Gson().fromJson(list, new TypeToken<ArrayList<ProductVpNewSearch>>(){}.getType());

                    MyAdapter mNoLoginHomeAdapter=new MyAdapter(VpNewProductSearchActivity.this,arrProduct, MainActivity.HOME_EVENT);
                	LinearLayoutManager linearLayoutManager=new LinearLayoutManager(VpNewProductSearchActivity.this);
                   linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    id_stickynavlayout_innerscrollview.setLayoutManager(linearLayoutManager);
                    id_stickynavlayout_innerscrollview.setAdapter(mNoLoginHomeAdapter);

                    if(arrProduct.size()<=0){
                        ToasAlert.toastCenter("没有查询到数据！");

                    }


                }

            }
        });
    }



    public class MyAdapter extends  RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        Context context;
        LayoutInflater inflate;
        JSONArray array;
        List<ProductVpNewSearch> arrProduct;
        Dao<Product,Integer> cartDao;
        public MyAdapter(Context context,    List<ProductVpNewSearch> 	   arrProduct, String eventMessage) {
            this.context=context;
            this.arrProduct=arrProduct;
//
            try {
                cartDao = DatabaseHelper.getHelper(context).getCartDao();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=   LayoutInflater.from(context).inflate(R.layout.vp_search_adapter_index_good, null, false);
//        AutoUtils.auto(view);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final ProductVpNewSearch dish   =  arrProduct.get(position);
            holder.product_name.setText(dish.sku_name);
            holder.sp_guig_tv.setText("规格:"+dish.styleno);
            holder.gys_name.setText(dish.supplier_name);
            holder.sp_jiage.setText("￥"+dish.unit_price+"/"+dish.unit);
            Picasso.with(context).load(HttpUtil.HOST_STRING +"/"+ dish.img_path).error(R.mipmap.error).resize(AppUtils.dp2px(context,50),AppUtils.dp2px(context,50)).into(holder.img);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,VpNewDetailNewActivity.class);
                    intent.putExtra("productid",dish.id);
                    context.startActivity(intent);
                }
            });
            holder.num_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    backgroundAlpha(0.5f);
                    AddNewVpIndexSearchPop mPopupWindow = new AddNewVpIndexSearchPop(VpNewProductSearchActivity.this,dish);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                            WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                    int[] location = new int[2];
                    v.getLocationOnScreen(location);
                    mPopupWindow.showAtLocation(holder.num_ll,  Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//				  mPopupWindow.showAsDropDown(right_listview);
                    mPopupWindow.setOnRefresh(new AddNewVpIndexSearchPop.Refresh() {
                        @Override
                        public void refresh() {
                            // notifyDataSetChanged();
                            EventBus.getDefault().post( MainActivity11.CARTREFRESH);
                        }
                    });
                    mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            backgroundAlpha(1f);
                        }
                    });

                }
            });


        }
        @Override
        public int getItemCount() {

            return  arrProduct.size();
        }
          class MyViewHolder  extends RecyclerView.ViewHolder{

            public ImageView img;



            public LinearLayout num_ll;
              public TextView product_name;
              public TextView sp_guig_tv;
              public TextView gys_name;
              public TextView sp_jiage;

            public   MyViewHolder(View view) {
                super(view);
                ButterKnife.inject(this, view);
                img = (ImageView)view.findViewById(R.id.img);

                product_name = (TextView)view.findViewById(R.id.product_name);
                sp_guig_tv = (TextView)view.findViewById(R.id.sp_guig_tv);
                gys_name = (TextView)view.findViewById(R.id.gys_name);
                sp_jiage = (TextView)view.findViewById(R.id.sp_jiage);
                num_ll = (LinearLayout)view.findViewById(R.id.num_ll);

            }
        }

        public Product queryProduct(String  id){
            try {
//            List<Product> mProductList  =cartDao.queryBuilder().where().eq("id", id).and().eq("account", HttpBase.getAccount(context)).query();
                List<Product> mProductList  =cartDao.queryBuilder().where().eq("id", id).query();
                if(mProductList!=null&&mProductList.size()>0){
                    return mProductList.get(0);
                }
                return null;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }

        }
            public void backgroundAlpha(float bgAlpha)
            {
                WindowManager.LayoutParams lp =((Activity)context). getWindow().getAttributes();
                lp.alpha = bgAlpha; //0.0-1.0
                ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                ((Activity)context).getWindow().setAttributes(lp);
            }
    }

}
