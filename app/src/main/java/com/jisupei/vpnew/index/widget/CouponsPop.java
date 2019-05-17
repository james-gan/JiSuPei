package com.jisupei.vpnew.index.widget;

/**
 * Created by xiayumo on 16/5/11.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.jingchen.pulltorefresh.PullableRecyclerView;
import com.jisupei.activity.base.MainActivity;
import com.jisupei.R;
import com.jisupei.utils.db.DatabaseHelper;
import com.jisupei.activity.base.event.CartEvent;
import com.jisupei.http.HttpUtil;
import com.jisupei.activity.base.model.Product;
import com.jisupei.vpnew.index.model.ProductVpNewSearch;
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
import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class CouponsPop extends PopupWindow {
    private View view;
    private Context mContext;
    String  idYHJ="";
    PullableRecyclerView nnerscrollview;
    PullableRecyclerView nnerscrollview2;
    public void onEventMainThread(CartEvent event) {
    }
    public CouponsPop(final Activity context, final String ids , final String idYHJ) {
        super(context);
        this.mContext=context;
        this.idYHJ=idYHJ;
//        this.mProductItem=mProductItem;

        EventBus.getDefault().register(this);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.newvp_youhuijuan, null);
        nnerscrollview= (PullableRecyclerView) view.findViewById(R.id.nnerscrollview);
        nnerscrollview2= (PullableRecyclerView) view.findViewById(R.id.nnerscrollview2);
        final TextView key_title= (TextView) view.findViewById(R.id.key_title);//可用的优惠卷
        final View key_title_v= (View) view.findViewById(R.id.key_title_v);//可用的优惠卷 view
        final TextView bukey_title= (TextView) view.findViewById(R.id.bukey_title);//不可用的优惠卷
        final View bukey_title_v= (View) view.findViewById(R.id.bukey_title_v);//不可用的优惠卷 view
//        final ImageView zaixian_iv= (ImageView) view.findViewById(R.id.zaixian_iv);
//        final ImageView hdfk_iv= (ImageView) view.findViewById(R.id.hdfk_iv);//货到付款
//        final TextView hdfktext= (TextView) view.findViewById(R.id.hdfktext);//货到付款
       Button submit_bt2= (Button) view.findViewById(R.id.submit_bt2);//
        key_title_v.setVisibility(View.VISIBLE);
        bukey_title_v.setVisibility(View.GONE);
        nnerscrollview.setVisibility(View.VISIBLE);
        nnerscrollview2.setVisibility(View.GONE);
        key_title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                nnerscrollview.setVisibility(View.VISIBLE);
                nnerscrollview2.setVisibility(View.GONE);
                key_title_v.setVisibility(View.VISIBLE);
                bukey_title_v.setVisibility(View.GONE);
//                if(arrProduct1!=null){
//                    MyAdapter mNoLoginHomeAdapter=new MyAdapter(mContext,arrProduct1, MainActivity.HOME_EVENT);
//                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
//                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                    nnerscrollview.setLayoutManager(linearLayoutManager);
//                    nnerscrollview.setAdapter(mNoLoginHomeAdapter);
//                }else{
//                    loadDatanew1(ids);
//
//                }

            }
        });
        bukey_title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                bukey_title_v.setVisibility(View.VISIBLE);
                key_title_v.setVisibility(View.GONE);
                nnerscrollview2.setVisibility(View.VISIBLE);
                nnerscrollview.setVisibility(View.GONE);
//                if(arrProduct2!=null){
//
//                    MyAdapter mNoLoginHomeAdapter=new MyAdapter(mContext,arrProduct2, MainActivity.HOME_EVENT);
//                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
//                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                    nnerscrollview.setLayoutManager(linearLayoutManager);
//                    nnerscrollview.setAdapter(mNoLoginHomeAdapter);
//
//                }else{
//                    loadDatanew2(ids);
//                }

            }
        });


//        zaixian_iv.setImageResource(R.drawable.check_sel);
//        hdfk_iv.setImageResource(R.drawable.check);
//        zaixian_ll.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                zaixian_iv.setImageResource(R.drawable.check_sel);
//                hdfk_iv.setImageResource(R.drawable.check);
//                curr=1;
//            }
//        });
//        if(hdfkFlag){
//            hdfk_ll.setEnabled(true);
//        }else {
//            hdfk_ll.setEnabled(false);
//            hdfktext.setText(hdfkFlagStr);
//        }
//        hdfk_ll.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hdfk_iv.setImageResource(R.drawable.check_sel);
//                zaixian_iv.setImageResource(R.drawable.check);
//                curr=2;
//            }
//        });
        submit_bt2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mAdapter!=null && mAdapter.currPos!=-1){
                    mRefresh.queding(arrProduct1.get( mAdapter.currPos));
                    dismiss();
                }else {
                    dismiss();
                }

            }
        });
//        initCartData(jiage_list);
        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.take_photo_anim);
//        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
//        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        loadDatanew1(ids);
        loadDatanew2(ids);

    }

    MyAdapter mAdapter;
    public void loadDatanew1(String ids) {
        AppLoading.show(mContext);
        HttpUtil.getInstance().getCouponForOrderAppY(ids,new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppLoading.close();
                ToasAlert.toastCenter("连接服务器失败，请稍后再试！");
            }
            @Override
            public void onResponse(String response, int id) {

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
                    arrProduct1 = new Gson().fromJson(list, new TypeToken<ArrayList<Coupon>>(){}.getType());

                      mAdapter=new MyAdapter(mContext,arrProduct1, MainActivity.HOME_EVENT);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                     nnerscrollview.setLayoutManager(linearLayoutManager);
                     nnerscrollview.setAdapter(mAdapter);

                    if(arrProduct1.size()<=0){
                        ToasAlert.toastCenter("没有查询到可用的优惠卷！");

                    }


                }

            }
        });


    }
    List<Coupon> arrProduct2;
    List<Coupon> arrProduct1;
    public void loadDatanew2(String ids) {
        AppLoading.show(mContext);
        HttpUtil.getInstance().getCouponForOrderAppN(ids,new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppLoading.close();
                ToasAlert.toastCenter("连接服务器失败，请稍后再试！");
            }
            @Override
            public void onResponse(String response, int id) {

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
                    arrProduct2 = new Gson().fromJson(list, new TypeToken<ArrayList<Coupon>>(){}.getType());


                    MyAdapter2 mNoLoginHomeAdapter=new MyAdapter2(mContext,arrProduct2, MainActivity.HOME_EVENT);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    nnerscrollview2.setLayoutManager(linearLayoutManager);
                    nnerscrollview2.setAdapter(mNoLoginHomeAdapter);

                    if(arrProduct2.size()<=0){
//                        ToasAlert.toastCenter("没有查询到可用的优惠卷！");

                    }



                }

            }
        });


    }





//    public String  countPrice(String num ,String price){
//            Double amount=0.00;
//
//             Double numNum=Double.parseDouble(num);
//            Double priceNUm=Double.parseDouble(price);
//                  amount  = HttpBase.mul(numNum,priceNUm);
//            return "￥"+new DecimalFormat("0.00").format(amount) ;
//
//    }
//
//
    Refresh mRefresh;
    public  void setOnRefresh(Refresh mRefresh){
        this.mRefresh=mRefresh;
    }
    public interface     Refresh{
        void queding(Coupon mCoupon);
    }





    public class MyAdapter extends  RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        Context context;
        LayoutInflater inflate;
        JSONArray array;
        List<Coupon> arrCoupon;

        Dao<Product,Integer> cartDao;
        int currPos;

        public MyAdapter(Context context,    List<Coupon>  	   arrCoupon, String eventMessage) {
            this.context=context;
            this.arrCoupon=arrCoupon;
            this.currPos =isIndex(idYHJ);// 默认选中当前
//            ToasAlert.toastCenter(this.currPos+" "+idYHJ);
            try {
                cartDao = DatabaseHelper.getHelper(context).getCartDao();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=   LayoutInflater.from(context).inflate(R.layout.vp_submit_cart_notuse_adapter, null, false);
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
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(context,VpNewDetailNewActivity.class);
//                    intent.putExtra("productid",dish.id);
//                    context.startActivity(intent);


                    currPos=position;
                    holder.  xuanznhong_iv.setImageResource(R.drawable.check_sel);
                 notifyDataSetChanged();

                }
            });
            if(position==currPos){//可用
                holder.  xuanznhong_iv.setImageResource(R.drawable.check_sel);
            }else{
                holder.  xuanznhong_iv.setImageResource(R.drawable.check);
            }
            if(position<=isIndex(idYHJ)){//可用
                holder. img_rl.setBackgroundResource(R.mipmap.card_active);
                holder. img_rl.setEnabled(true);
                holder.img_rl.setEnabled(true);
                holder.itemView.setEnabled(true);
                holder. img_rl.setAlpha(1.0f);
                holder.  xuanznhong_iv.setAlpha(1.0f);
                holder.itemView.setAlpha(1.0f);
            }else{
                holder. img_rl.setBackgroundResource(R.mipmap.card_disabled);
                holder. img_rl.setEnabled(false);
                holder.itemView.setEnabled(false);
                holder. img_rl.setAlpha(0.5f);
                holder.itemView.setAlpha(0.5f);
                holder. xuanznhong_iv.setAlpha(0.1f);
            }


//            holder.sy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    EventBus.getDefault().post("to2");
//                    ((Activity)context).finish();
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
            public ImageView xuanznhong_iv;
            public RelativeLayout img_rl;
//            public TextView sy;

            public   MyViewHolder(View view) {
                super(view);
                ButterKnife.inject(this, view);
//                img = (ImageView)view.findViewById(R.id.img);
//
                cart_name = (TextView)view.findViewById(R.id.cart_name);
                money_cart = (TextView)view.findViewById(R.id.money_cart);
                man_user = (TextView)view.findViewById(R.id.man_user);
                user_time = (TextView)view.findViewById(R.id.user_time);
                xuanznhong_iv = (ImageView)view.findViewById(R.id.xuanznhong_iv);
                img_rl = (RelativeLayout)view.findViewById(R.id.img_rl);
//                sy = (TextView)view.findViewById(R.id.sy);
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


        public int isIndex(String idyYHJ){
            for (int i=0;i<arrCoupon.size();i++){
                if(arrCoupon.get(i).ID.equals(idyYHJ)){
                    return i;
                }

            }
            return -1;
        }
    }

    public class MyAdapter2 extends  RecyclerView.Adapter<MyAdapter2.MyViewHolder> {
        Context context;
        LayoutInflater inflate;
        JSONArray array;
        List<Coupon> arrCoupon;

        Dao<Product,Integer> cartDao;

        public MyAdapter2(Context context,    List<Coupon>  	   arrCoupon, String eventMessage) {
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
            View view=   LayoutInflater.from(context).inflate(R.layout.vp_submit_notuse_adapter, null, false);
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

//            holder.sy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    EventBus.getDefault().post("to2");
//                    ((Activity)context).finish();
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
//            public TextView sy;

            public   MyViewHolder(View view) {
                super(view);
                ButterKnife.inject(this, view);
//                img = (ImageView)view.findViewById(R.id.img);
//
                cart_name = (TextView)view.findViewById(R.id.cart_name);
                money_cart = (TextView)view.findViewById(R.id.money_cart);
                man_user = (TextView)view.findViewById(R.id.man_user);
                user_time = (TextView)view.findViewById(R.id.user_time);
//                sy = (TextView)view.findViewById(R.id.sy);
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