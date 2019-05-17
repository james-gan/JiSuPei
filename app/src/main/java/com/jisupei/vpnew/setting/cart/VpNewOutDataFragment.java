package com.jisupei.vpnew.setting.cart;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableRecyclerView;
import com.jisupei.activity.base.MainActivity;
import com.jisupei.R;
import com.jisupei.utils.db.DatabaseHelper;
import com.jisupei.ywy.BaseFragment;
import com.jisupei.http.HttpUtil;
import com.jisupei.activity.base.model.Product;
import com.jisupei.utils.AutoUtils;
import com.jisupei.vpnew.setting.cart.bean.Coupon;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/18.
 */
public class VpNewOutDataFragment extends BaseFragment {



    @InjectView(R.id.listView)
    PullableRecyclerView listView;
    @InjectView(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;

    public static VpNewOutDataFragment getVpNewNotUseFragment() {

        VpNewOutDataFragment instance = new VpNewOutDataFragment();
        return instance;
    }

    @Override
    public View initView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.newvp_notuse_cart, null);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void initData() {

        loadDatanew(null);

        pullToRefreshLayout.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//                pageno=1;
                loadDatanew(pullToRefreshLayout);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

//                load(search_ed.getText().toString().trim(),pullToRefreshLayout);
            }
        });
        pullToRefreshLayout.setPullUpEnable(false);
        AutoUtils.auto(pullToRefreshLayout.defaultRefreshView);

    }
    //加载数据
  public   String  orderCode;
    public   String  sub_status;
    public   String  orderType;
    //加载数据
    public   String  payStatus;
    public void loadDatanew(final PullToRefreshLayout pullToRefreshLayout) {

        AppLoading.show(getActivity());
        HttpUtil.getInstance().getMyCouponAppD(new StringCallback() {
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
                    String list = object.optJSONObject("res").optString("couponList");
                    List<Coupon> 	   arrProduct = new Gson().fromJson(list, new TypeToken<ArrayList<Coupon>>(){}.getType());

                    MyAdapter mNoLoginHomeAdapter=new MyAdapter(getActivity(),arrProduct, MainActivity.HOME_EVENT);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    listView.setLayoutManager(linearLayoutManager);
                    listView.setAdapter(mNoLoginHomeAdapter);

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
        List<Coupon> arrCoupon;

        Dao<Product,Integer> cartDao;

        public MyAdapter(Context context,    List<Coupon>  	   arrCoupon, String eventMessage) {
            this.context=context;
            this.arrCoupon=arrCoupon;
//
            try {
                cartDao = DatabaseHelper.getHelper(context).getCartDao();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=   LayoutInflater.from(context).inflate(R.layout.vp_cart_hastimeout_adapter, null, false);
//        AutoUtils.auto(view);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final Coupon dish   =  arrCoupon.get(position);
            holder.cart_name.setText(dish.couponName);
            holder.money_cart.setText("￥"+dish.couponValue);
            holder.man_user.setText("满"+dish.minMoney+"元可用");
            holder.user_time.setText(dish.beginTime.split(" ")[0]+"~"+dish.endTime.split(" ")[0]);
//            Picasso.with(context).load(HttpUtil.HOST_STRING +"/"+ dish.img_path).error(R.mipmap.error).resize(AppUtils.dp2px(context,50),AppUtils.dp2px(context,50)).into(holder.img);
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context,VpNewDetailNewActivity.class);
//                    intent.putExtra("productid",dish.id);
//                    context.startActivity(intent);
//                }
//            });


        }
        @Override
        public int getItemCount() {

            return  arrCoupon.size();
        }
        class MyViewHolder  extends RecyclerView.ViewHolder{

            public ImageView img;



            public LinearLayout num_ll;
            public TextView cart_name;
            public TextView money_cart;
            public TextView man_user;
            public TextView user_time;
            public TextView sy;

            public   MyViewHolder(View view) {
                super(view);
                ButterKnife.inject(this, view);
//                img = (ImageView)view.findViewById(R.id.img);
//
                  cart_name = (TextView)view.findViewById(R.id.cart_name);
                 money_cart = (TextView)view.findViewById(R.id.money_cart);
                man_user = (TextView)view.findViewById(R.id.man_user);
                user_time = (TextView)view.findViewById(R.id.user_time);

//                 gys_name = (TextView)view.findViewById(R.id.gys_name);
//                sp_jiage = (TextView)view.findViewById(R.id.sp_jiage);
//                num_ll = (LinearLayout)view.findViewById(R.id.num_ll);

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
