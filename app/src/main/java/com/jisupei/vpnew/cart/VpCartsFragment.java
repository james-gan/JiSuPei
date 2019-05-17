package com.jisupei.vpnew.cart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableRecyclerView_swipe;
import com.jisupei.activity.base.MainActivity;
import com.jisupei.R;
import com.jisupei.activity.homepage2.MainActivity11;
import com.jisupei.utils.db.DatabaseHelper;
import com.jisupei.ywy.BaseFragment;
import com.jisupei.http.HttpBase;
import com.jisupei.http.HttpUtil;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.AutoUtils;
import com.jisupei.vpnew.db.VpNewProductDb;
import com.jisupei.vpnew.index.activity.VpNewConfirmNewActivity;
import com.jisupei.vpnew.index.activity.VpNewDetailNewActivity;
import com.jisupei.vpnew.index.model.ProductVpNewSearch;
import com.jisupei.vpnew.index.widget.AddNewVpIndexSearchPop;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/18.
 */
public class VpCartsFragment extends BaseFragment {
    @InjectView(R.id.pullToRefreshLayout)
    public PullToRefreshLayout pullToRefreshLayout;
    @InjectView(R.id.id_stickynavlayout_innerscrollview)
    PullableRecyclerView_swipe id_stickynavlayout_innerscrollview;
    @InjectView(R.id.quanxuan)
    public   CheckBox quanxuan;
    @InjectView(R.id.count_sum)
    TextView count_sum;
    @InjectView(R.id.count_money)
    TextView count_money;
    @InjectView(R.id.empty)
    TextView empty;
    @InjectView(R.id.sumint_bt)
    Button sumint_bt;
    public static VpCartsFragment getVpVpNewCartFragmentInstance() {
        VpCartsFragment instance = new VpCartsFragment();
        return instance;
    }
    public void setingDefault() {
    }
    @Override
    public View initView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.newvp_cart_fragment, null);
        ButterKnife.inject(this, rootView);
        EventBus.getDefault().register(this);
//        findViewIdHead(rootView);
//        include_title_tv.setText("订单");
//        AutoUtils.auto(rootView);
        pullToRefreshLayout.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                count_money.setText("0.0");
                count_sum.setText("0");
                quanxuan .setChecked(true);
                queryCartNum(pullToRefreshLayout);
            }
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            }
        });
        pullToRefreshLayout.setPullUpEnable(false);
        AutoUtils.auto(pullToRefreshLayout.defaultRefreshView);
        // 设置菜单创建器。
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity())
                        .setBackgroundColor(Color.parseColor("#ff671d"))
//                        .setImage(R.mipmap.add) // 图标。
                        .setText("删除") // 文字。
                        .setTextColor(Color.WHITE) // 文字颜色。
                        .setTextSize(16) // 文字大小。
                        .setWidth(AppUtils.dp2px(getActivity(), 75))
                        .setHeight(AppUtils.dp2px(getActivity(), 90));
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
            }
        };
        id_stickynavlayout_innerscrollview.setSwipeMenuCreator(swipeMenuCreator);
        id_stickynavlayout_innerscrollview.setSwipeMenuItemClickListener(new OnSwipeMenuItemClickListener() {
            @Override
            public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
                 closeable.smoothCloseMenu();
                  deleateProduct(arrProduct.get(adapterPosition).id);
                   mAdapter.arrProduct.remove(adapterPosition);
                   mAdapter.map.clear();
                   mAdapter.notifyDataSetChanged();
                EventBus.getDefault().post(MainActivity11.CARTREFRESH);
            }
        });
        quanxuan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!buttonView.isPressed())return;	//加这一条，否则当我setChecked()时会触发此listener
                if(mAdapter==null||arrProduct==null||arrProduct.size()==0)return;
                if (isChecked == true) {
                    mAdapter.map.clear();
                    double sum=0;
                     for (int i = 0; i < mAdapter.arrProduct.size(); i++) {
                            mAdapter.map.put(i,true);
                          ProductVpNewSearch mProductVpNewSearch=arrProduct.get(i);
                           double  productItemSum =HttpBase.mul(Double.parseDouble(mProductVpNewSearch.unit_price),Double.parseDouble(mAdapter.queryProductDbNum(mProductVpNewSearch.id))) ;
                          sum=HttpBase.add(productItemSum,sum);

                     }
                    count_money.setText(sum+"");
                    mAdapter.notifyDataSetChanged();
                    count_sum.setText(arrProduct.size()+"");
                } else { //反选
                    if(mAdapter!=null){
                        mAdapter.map.clear();
                        mAdapter.notifyDataSetChanged();
                        count_money.setText("0.0");
                        count_sum.setText("0");
                    }
                }
            }
        });

        sumint_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(empty.getVisibility()==View.VISIBLE){
                    return;
                }
                if(mAdapter!=null){
                    if(mAdapter.map.size()==0||mAdapter.map==null){
                        ToasAlert.toastCenter("请您勾选商品！");
                        return;
                    }else {
                        List<ProductVpNewSearch> arrProductTemp=new ArrayList<ProductVpNewSearch>();
                        for (int key: mAdapter.map.keySet()) {
                            arrProductTemp.add( 0,arrProduct.get(key));
                        }

//                        Intent intent = new Intent(getActivity(),VpNewConfirmActivity.class);
                        Intent intent = new Intent(getActivity(),VpNewConfirmNewActivity.class);
                        intent.putExtra("arrProduct", (Serializable) arrProductTemp);
                        intent.putExtra("summoney", count_money.getText().toString());
                        startActivity(intent);

                    }
                }else {

                }

            }
        });
        return rootView;
    }


    @Override
    public void initData() {

        queryCartNum(null);
    }
    public void onEventMainThread(String  refresh) {
        if(MainActivity11.CARTREFRESH.equals(refresh)){
            queryCartNum(null);
        }
    }
    MyAdapter mAdapter;
    Dao<VpNewProductDb, Integer> mVpNewProductDbDaoDao;
    List<ProductVpNewSearch> arrProduct;
    public void queryCartNum(final PullToRefreshLayout pullToRefreshLayoutTemp) {
        try {
            try {
                mVpNewProductDbDaoDao = DatabaseHelper.getHelper(getActivity()).getVpNewProductDbDaoDao();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //查询数据库
            List<VpNewProductDb> mProductList = mVpNewProductDbDaoDao.queryBuilder().orderBy("id1", false).query();

            if (mProductList != null && mProductList.size() > 0) {
                empty.setVisibility(View.GONE);
                pullToRefreshLayout.setVisibility(View.VISIBLE);
                String ids = "";
                for (VpNewProductDb mVpNewProductDb : mProductList) {

                    ids += mVpNewProductDb.id + ",";

                }
                ids = ids.substring(0, ids.length() - 1);


                AppLoading.show(getActivity());
                HttpUtil.getInstance().vpshoppingCart(ids, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        AppLoading.close();
                        ToasAlert.toastCenter("连接服务器失败，请稍后再试！");
                        if (pullToRefreshLayoutTemp != null)
                            pullToRefreshLayoutTemp.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (pullToRefreshLayoutTemp != null)
                            pullToRefreshLayoutTemp.refreshFinish(PullToRefreshLayout.SUCCEED);
                        Logger.d("app2.0", response);
                        AppLoading.close();
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String optFlag = object.optString("optFlag");
                        if ("yes".equals(optFlag)) {
                            String list = object.optString("res");
                           arrProduct = new Gson().fromJson(list, new TypeToken<ArrayList<ProductVpNewSearch>>() {
                            }.getType());

                            if (arrProduct.size() <= 0) {
                                ToasAlert.toastCenter("没有查询到数据！");

                                empty.setVisibility(View.VISIBLE);
                                pullToRefreshLayout.setVisibility(View.GONE);
                                sumint_bt.setBackgroundColor(Color.parseColor("#999999"));
                                count_sum.setText("0");
                                count_money.setText("￥0.00");


                                Dao<VpNewProductDb,Integer> mVpNewProductDbDao = null;
                                try {
                                    mVpNewProductDbDao = DatabaseHelper.getHelper(getActivity()).getVpNewProductDbDaoDao();
                                    List<VpNewProductDb> mVpNewProductDbList= mVpNewProductDbDao.queryBuilder().orderBy("id1", false).query();
                                    if(mVpNewProductDbList!=null && mVpNewProductDbList.size()>0){
                                        mVpNewProductDbDao.delete(mVpNewProductDbList);
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                return;
                            }

                            mAdapter = new MyAdapter(getActivity(), arrProduct, MainActivity.HOME_EVENT);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            id_stickynavlayout_innerscrollview.setLayoutManager(linearLayoutManager);

                            id_stickynavlayout_innerscrollview.setAdapter(mAdapter);//数据

                            count_money.setText(sumMoney());
                            sumint_bt.setBackgroundColor(Color.parseColor("#318eff"));




                        }

                    }
                });
            } else {
                empty.setVisibility(View.VISIBLE);
                pullToRefreshLayout.setVisibility(View.GONE);
                count_sum.setText("0");
                count_money.setText("￥0.00");
                sumint_bt.setBackgroundColor(Color.parseColor("#999999"));
            }


        } catch (SQLException e) {
            e.printStackTrace();

        }

    }
    //     RecyclerView.Adapter
    public class MyAdapter extends SwipeMenuAdapter<MyAdapter.MyViewHolder> {
//        private Map<Integer, Boolean> map = new HashMap<>();
        Map<Integer, Boolean> map = new TreeMap<Integer, Boolean>(
                new Comparator<Integer>() {
                    public int compare(Integer obj1, Integer obj2) {
                        // 降序排序
                        return obj2.compareTo(obj1);
                    }
                });
        Context context;
        LayoutInflater inflate;
        JSONArray array;
       public  List<ProductVpNewSearch> arrProduct;


        Dao<VpNewProductDb, Integer> mVpNewProductDbDaoDao;

        public MyAdapter(Context context, List<ProductVpNewSearch> arrProduct, String eventMessage) {
            this.context = context;
            this.arrProduct = arrProduct;

            for (int i = 0; i < arrProduct.size(); i++) {
                map.put(i,true);
            }
//            quanxuan.setChecked(true);
            try {
                mVpNewProductDbDaoDao = DatabaseHelper.getHelper(context).getVpNewProductDbDaoDao();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

//       @Override
//       public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//           View view=   LayoutInflater.from(context).inflate(R.layout.vp_cart_adapter_index_good, null, false);
//            MyViewHolder holder = new MyViewHolder(view);
//          return holder;
//
//      }


        @Override
        public View onCreateContentView(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.vp_cart_adapter_index_good, null, false);
            return view;
        }

        @Override
        public MyViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
            MyViewHolder holder = new MyViewHolder(realContentView);
            return holder;
        }
//        @Override
//        public View onCreateContentView(ViewGroup parent, int viewType) {
//            View view=   LayoutInflater.from(context).inflate(R.layout.vp_cart_adapter_index_good, null, false);
//            return view;
//        }
//
//
//        @Override
//        public MyViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
////            View view=   LayoutInflater.from(context).inflate(R.layout.vp_cart_adapter_index_good, null, false);
////        AutoUtils.auto(view);
//
//        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final ProductVpNewSearch dish = arrProduct.get(position);
            holder.product_name.setText(dish.sku_name);
            holder.sp_guig_tv.setText("规格:" + dish.styleno);
            holder.gys_name.setText(dish.supplier_name);

            Picasso.with(context).load(HttpUtil.HOST_STRING + "/" + dish.img_path).error(R.mipmap.error).resize(AppUtils.dp2px(context, 50), AppUtils.dp2px(context, 50)).into(holder.img);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, VpNewDetailNewActivity.class);
                    intent.putExtra("productid", dish.id);
                    context.startActivity(intent);
                }
            });
            holder.jian.setText(queryProductDbNum(dish.id));
            holder.jian_danwei.setText(dish.unit);

            holder.jian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backgroundAlpha(0.5f);
                    AddNewVpIndexSearchPop mPopupWindow = new AddNewVpIndexSearchPop(getActivity(),dish);
                    ((Activity)context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
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






            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked == true) {
                        map.put(position, true);

                        count_sum.setText(map.size()+"");

                        count_money.setText(sumMoney());

                        if(map.size()==arrProduct.size()){

                            quanxuan.setChecked(true);

                        }

                    } else {    //
                        map.remove(position);
                        count_sum.setText(map.size()+"");   //数量
                        count_money.setText(sumMoney());

                        if(map.size()!=arrProduct.size()){

                            quanxuan.setChecked(false);

                        }
                    }
                }
            });

            if (map != null && map.containsKey(position)) {
                holder.mCheckBox.setChecked(true);
            } else {
                holder.mCheckBox.setChecked(false);
            }



            //区间价格
            if(dish.rkpriceList!=null && dish.rkpriceList.size()>0){
                  String currPrice=  getInsertPrice(holder.jian.getText().toString(),dish.rkpriceList);
                currPrice= TextUtils.isEmpty(currPrice)?dish.unit_price:currPrice;
                holder.sp_jiage.setText("￥" + currPrice + "/" + dish.unit);
                 dish.unit_price=currPrice;
                count_money.setText(sumMoney());//总价

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                holder.jiage_list.setLayoutManager(linearLayoutManager);
                holder.qujian_ll.setVisibility(View.GONE);
                holder.jiage_list.setVisibility(View.GONE);
                holder.qujian_bq.setVisibility(View.VISIBLE);
                holder.hxian.setVisibility(View.VISIBLE);

                QujianAdapter adapter = new QujianAdapter(context,dish.rkpriceList,dish.unit);
                holder.jiage_list.setAdapter(adapter);
                holder.jiage_list.setNestedScrollingEnabled(false);

            }else {
                holder.sp_jiage.setText("￥" + dish.unit_price+"/" + dish.unit);
                count_money.setText(sumMoney());//总价

                  holder.qujian_ll.setVisibility(View.GONE);
                 holder.jiage_list.setVisibility(View.GONE);
                 holder.qujian_bq.setVisibility(View.GONE);
                 holder.hxian.setVisibility(View.GONE);
            }
            holder.qujian_bq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(  holder.qujian_ll.getVisibility()==View.VISIBLE){
                        holder.qujian_ll.setVisibility(View.GONE);
                        holder.jiage_list.setVisibility(View.GONE);

                        Animation180(holder.jiantou);

                    }else {
                        holder.qujian_ll.setVisibility(View.VISIBLE);
                        holder.jiage_list.setVisibility(View.VISIBLE);
                        Animation(holder.jiantou);

                    }

                }
            });
            if(iskucun(dish)){
                holder.hint.setVisibility(View.VISIBLE);

            }else {
                holder.hint.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {

            return arrProduct.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            public ImageView img;


            public LinearLayout num_ll;
            public TextView product_name;
            public TextView sp_guig_tv;
            public TextView gys_name;
            public TextView sp_jiage;
            public TextView jian_danwei;
            public TextView jian;
            public CheckBox mCheckBox;
            RecyclerView jiage_list;
            LinearLayout qujian_bq; //价格区间按钮
            LinearLayout qujian_ll;
            View hxian;
            ImageView jiantou;
            TextView hint;

            public MyViewHolder(View view) {
                super(view);
                ButterKnife.inject(this, view);
                img = (ImageView) view.findViewById(R.id.img);

                product_name = (TextView) view.findViewById(R.id.product_name);
                hint = (TextView) view.findViewById(R.id.hint);
                sp_guig_tv = (TextView) view.findViewById(R.id.sp_guig_tv);
                gys_name = (TextView) view.findViewById(R.id.gys_name);
                sp_jiage = (TextView) view.findViewById(R.id.sp_jiage);
                jian_danwei = (TextView) view.findViewById(R.id.jian_danwei);
                jian = (TextView) view.findViewById(R.id.jian);
                num_ll = (LinearLayout) view.findViewById(R.id.num_ll);
                mCheckBox = (CheckBox) view.findViewById(R.id.CheckBox);
                 jiage_list= (RecyclerView) view.findViewById(R.id.jiage_list);
                qujian_bq= (LinearLayout) view.findViewById(R.id.qujian_bq);
                qujian_ll= (LinearLayout) view.findViewById(R.id.qujian_ll);
                hxian= (View) view.findViewById(R.id.hxian);
                jiantou= (ImageView) view.findViewById(R.id.jiantou);


            }
        }


        //根据id查询单个数量
        public String queryProductDbNum(String id) {
            try {

                List<VpNewProductDb> mVpNewProductDbList = mVpNewProductDbDaoDao.queryBuilder().where().eq("id", id).query();
                if (mVpNewProductDbList != null && mVpNewProductDbList.size() > 0) {
                    String pro_num = mVpNewProductDbList.get(0).pro_num;
                    int jianNumDb = (int) HttpBase.div(Double.parseDouble(pro_num), Double.parseDouble(mVpNewProductDbList.get(0).equation_factor));
                    return jianNumDb + "";

                }
                return "";
            } catch (SQLException e) {
                e.printStackTrace();
                return "";
            }

        }
        //库存
        public boolean iskucun( ProductVpNewSearch dish) {
            try {

                List<VpNewProductDb> mVpNewProductDbList = mVpNewProductDbDaoDao.queryBuilder().where().eq("id", dish.id).query();
                if (mVpNewProductDbList != null && mVpNewProductDbList.size() > 0) {
                    String pro_num = mVpNewProductDbList.get(0).pro_num;
                    if(Double.parseDouble(pro_num)>Double.parseDouble(dish.qty)){
                        return true;
                    }
                }
                return false;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

        }


        public void backgroundAlpha(float bgAlpha)
        {
            WindowManager.LayoutParams lp =((Activity)context). getWindow().getAttributes();
            lp.alpha = bgAlpha; //0.0-1.0
            ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            ((Activity)context).getWindow().setAttributes(lp);
        }



        public class QujianAdapter extends  RecyclerView.Adapter<QujianAdapter.MyViewHolder> {
            Context context;
            String unit;
            List<ProductVpNewSearch.RkpriceListItem>  rkpriceList;

            public QujianAdapter(Context context , List<ProductVpNewSearch.RkpriceListItem> rkpriceList, String unit) {
                this.context=context;
                this.rkpriceList=rkpriceList;
                this.unit=unit;

            }
            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view=   LayoutInflater.from(context).inflate(R.layout.vp_cart_jiage_qj, null, false);
                AutoUtils.auto(view);
                MyViewHolder holder = new MyViewHolder(view);
                return holder;
            }
            @Override
            public void onBindViewHolder(final MyViewHolder holder, int position) {
                final ProductVpNewSearch.RkpriceListItem mRkpriceListItem   =  rkpriceList.get(position);

                holder.price.setText( "￥"+mRkpriceListItem.qty_price);
                if(TextUtils.isEmpty(mRkpriceListItem.end_qty)){
                    holder.qujian.setText( "≥"+mRkpriceListItem.begin_qty+unit);
                }else {
                    holder.qujian.setText( mRkpriceListItem.begin_qty+"-"+mRkpriceListItem.end_qty+unit);
                }

            }
            @Override
            public int getItemCount() {
                return  rkpriceList.size();
            }
            class MyViewHolder  extends RecyclerView.ViewHolder{
                @InjectView(R.id.price)
                TextView price;
                @InjectView(R.id.qujian)
                TextView qujian;

                public   MyViewHolder(View view) {
                    super(view);
                    ButterKnife.inject(this, view);
                }
            }



        }




        //根据数量获得区间的价格
        public String getInsertPrice(String num, List<ProductVpNewSearch .RkpriceListItem> rkpriceList){

            num=TextUtils.isEmpty(num)?"0":num;

            String curr="";
            for(ProductVpNewSearch.RkpriceListItem mRkpriceListItem:rkpriceList){
                double  numDouble =Double.parseDouble(num);
                if(!TextUtils.isEmpty(mRkpriceListItem. end_qty) ){//不是最后的

                    double   begin_qtyDouble=Double.parseDouble(mRkpriceListItem.begin_qty);
                    double  end_qtyDouble =Double.parseDouble(mRkpriceListItem.end_qty);
                    if(begin_qtyDouble <= numDouble && numDouble<= end_qtyDouble){
                        curr= mRkpriceListItem .qty_price;
                        break;
                    }else if(begin_qtyDouble >numDouble ){
                        curr= "";
                        break;
                    }
                }else { //最后
                    curr= mRkpriceListItem .qty_price;

                }
            }
            return  curr;

        }


        //控制动画
        public void Animation(ImageView mImageView){
            RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            //默认为0，为-1时一直循环动画
            rotate.setRepeatCount(0);
            //添加匀速加速器
            //匀速加速器
            LinearInterpolator lir = new LinearInterpolator();
            rotate.setInterpolator(lir);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            mImageView.startAnimation(rotate);
        }
        //控制动画
        public void Animation180(ImageView mImageView){
            RotateAnimation rotate = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            //默认为0，为-1时一直循环动画
            rotate.setRepeatCount(0);
            //添加匀速加速器
            //匀速加速器
            LinearInterpolator lir = new LinearInterpolator();
            rotate.setInterpolator(lir);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            mImageView.startAnimation(rotate);
        }

    }//adapter
    //合计
    public String sumMoney( ) {
        double sum=0;

        for (int key : mAdapter.map.keySet()) {
            ProductVpNewSearch mProductVpNewSearch=mAdapter.arrProduct.get(key);
            double  productItemSum =HttpBase.mul(Double.parseDouble(mProductVpNewSearch.unit_price),Double.parseDouble(TextUtils.isEmpty(mAdapter.queryProductDbNum(mProductVpNewSearch.id))?"0":mAdapter.queryProductDbNum(mProductVpNewSearch.id))) ;
            sum=HttpBase.add(productItemSum,sum);
        }
        return sum+"";

    }
    public void deleateProduct(String id) {
        try {

            List<VpNewProductDb> mVpNewProductDbList = mVpNewProductDbDaoDao.queryBuilder().where().eq("id", id).query();
            if (mVpNewProductDbList != null && mVpNewProductDbList.size() > 0) {

                for (VpNewProductDb mVpNewProductDb : mVpNewProductDbList) {
                    mVpNewProductDbDaoDao.deleteById(mVpNewProductDb.id1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }
    public String queryProductCount() {
        try {

            List<VpNewProductDb> mVpNewProductDbList = mVpNewProductDbDaoDao.queryBuilder().query();
            if (mVpNewProductDbList != null && mVpNewProductDbList.size() > 0) {

                return mVpNewProductDbList.size()+"";
            }
            return "0";
        } catch (SQLException e) {
            e.printStackTrace();
            return "0";

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }


}
