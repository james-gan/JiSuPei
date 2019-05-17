package com.jisupei.vpheadquarters.seting;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.jisupei.R;
import com.jisupei.activity.homepage2.MainActivity11;
import com.jisupei.application.MyApplication;
import com.jisupei.utils.db.DatabaseHelper;
import com.jisupei.http.HttpUtil;
import com.jisupei.activity.base.model.Product;
import com.jisupei.activity.base.model.RankPriceItem;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.AutoUtils;
import com.jisupei.utils.StatusBarUtil;
import com.jisupei.vpheadquarters.seting.bean.VpGood;
import com.jisupei.vpnew.db.VpNewProductDb;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by xiayumo on 16/5/11.
 */
public class VpHeadGoodDetailNewActivity extends AppCompatActivity {
    Dao<Product,Integer> cartDao;
    public static final String PRODUCT_DETAIL_REQUEST = "/appProduct/productInfo.do?";

    RecyclerView jiage_list;

//    IndexProductList.IndexProductItem mIndexProductItem;

     TextView guige;
    TextView name;
    TextView gys;
    TextView price;
    TextView sc_price;
    TextView ck;
    TextView kucun;
//    TextView cart_num;
    TextView product_jiesuao;
//    LinearLayout soucang;
    RelativeLayout go_cart;
    LinearLayout qujian_ll;
//    Button add_cart;
    ImageView sc_iv;
    View jiage_view;
    View jiage_view2;
    Dao<VpNewProductDb,Integer> mVpNewProductDbDaoDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            mVpNewProductDbDaoDao = DatabaseHelper.getHelper(VpHeadGoodDetailNewActivity.this).getVpNewProductDbDaoDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.vp_headhuopin_detail);
        StatusBarUtil.setStatusBarColor(this,R.color.white);
        int a2=  StatusBarUtil.StatusBarLightMode(VpHeadGoodDetailNewActivity.this);
        if(a2==0){
            StatusBarUtil.setStatusBarColor(VpHeadGoodDetailNewActivity.this,R.color.black);
        }
        jiage_list= (RecyclerView) findViewById(R.id.jiage_list);
        name= (TextView) findViewById(R.id.name);
        jiage_view= (View) findViewById(R.id.jiage_view);
        jiage_view2= (View) findViewById(R.id.jiage_view2);
//        cart_num= (TextView) findViewById(R.id.cart_num);
        guige= (TextView) findViewById(R.id.guige);
        gys= (TextView) findViewById(R.id.gys);
        price= (TextView) findViewById(R.id.price);
        sc_price= (TextView) findViewById(R.id.sc_price);
        product_jiesuao= (TextView) findViewById(R.id.product_jiesuao);
        ck= (TextView) findViewById(R.id.ck);
        kucun= (TextView) findViewById(R.id.kucun);

//        soucang= (LinearLayout) findViewById(R.id.soucang);
//        go_cart= (RelativeLayout) findViewById(R.id.go_cart);
        qujian_ll= (LinearLayout) findViewById(R.id.qujian_ll);
//        add_cart= (Button) findViewById(R.id.add_cart);
        sc_iv= (ImageView) findViewById(R.id.sc_iv);

        MyApplication.instance.addActivity(this);

        EventBus.getDefault().register(this);
         auto_view_pager=(AutoScrollViewPager)findViewById(R.id.auto_view_pager);
        auto_view_pager.getLayoutParams().height=AutoUtils.getDisplayHeightValue(660);

        findViewById(R.id.back_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        AppUtils.expandViewTouchDelegate( findViewById(R.id.back_bt), 30, 30, 50, 50);
         final String productId=    getIntent().getStringExtra("productid");
//         mIndexProductItem= (IndexProductList.IndexProductItem) getIntent().getSerializableExtra("dish");

        //加载数据
        productInfoData(productId);

    }

    VpGood mDefaultContactShop;
  String   isFavory="N";
    //查询商品的详情的接口
     public void productInfoData(String productid){

         AppLoading.show(this);
         HttpUtil.getInstance().productIndexH5One(productid, new StringCallback() {
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
//                       mDefaultContactShop=  new Gson().fromJson(object.optString("res"),ProductDatilData.class);

                     JSONObject object1 = null;
                     try {
                         object1 = object.getJSONObject("res");
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                     final List<VpGood> orderListTemp = new Gson().fromJson(object1.optString("list").toString(), new TypeToken<ArrayList<VpGood>>() {
                     }.getType());

                       mDefaultContactShop=orderListTemp.get(0);





                     if("0".equals(mDefaultContactShop.equation_factor)||"1".equals(mDefaultContactShop.equation_factor)||"1.0".equals(mDefaultContactShop.equation_factor)|| TextUtils.isEmpty(mDefaultContactShop.equation_factor)){

                         guige.setText("规格:"+mDefaultContactShop.styleno);//规格

                     }else {  //2 个单位
                         if((!"0".equals(mDefaultContactShop.equation_factor)&&!"1".equals(mDefaultContactShop.equation_factor)))
                             guige.setText("规格:"+mDefaultContactShop.styleno+"(1"+mDefaultContactShop.unit+"="+mDefaultContactShop.equation_factor+mDefaultContactShop.uom_default+")");//规格

                     }
                     name.setText(mDefaultContactShop.sku_name);
                     gys.setText(mDefaultContactShop.supplier_name);
                     price.setText("￥"+mDefaultContactShop.unit_price+"/"+mDefaultContactShop.unit);
                     ck.setText("良中行武汉肉联仓库");
                     kucun.setText("库存："+mDefaultContactShop.sumQty+mDefaultContactShop.unit);
//                     sc_price.setText("市场价:"+"￥"+mDefaultContactShop.market_unitprice+"/"+mDefaultContactShop.unit);
                     sc_price.setVisibility(View.GONE);
                     sc_price.setPaintFlags(sc_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                     product_jiesuao.setText(Html.fromHtml(TextUtils.isEmpty(mDefaultContactShop.sku_dec)?"暂无":mDefaultContactShop.sku_dec));

//                     TextView product_jiesuao;
//                     LinearLayout soucang;
//                     LinearLayout go_cart;
//                     Button add_cart;

                     List<String> imgList=new ArrayList<String>();

                     if(mDefaultContactShop.imgList!=null &&mDefaultContactShop.imgList.size()>0){
                         for ( VpGood.ImgListItem mImg :mDefaultContactShop.imgList) {
                             imgList.add(mImg.img_path);
                         }
                     }else {
                         imgList.add(mDefaultContactShop.img_path);
                     }
                     setBannerImages(imgList);




                  if(mDefaultContactShop.rkPriceList!=null && mDefaultContactShop.rkPriceList.size()>0){
                        jiage_list.setVisibility(View.VISIBLE);
                      qujian_ll.setVisibility(View.VISIBLE);
                      jiage_view.setVisibility(View.VISIBLE);
                      jiage_view2.setVisibility(View.VISIBLE);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VpHeadGoodDetailNewActivity.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        jiage_list.setLayoutManager(linearLayoutManager);
                        QujianAdapter adapter = new QujianAdapter(VpHeadGoodDetailNewActivity.this,mDefaultContactShop.rkPriceList,mDefaultContactShop.unit);
                        jiage_list.setAdapter(adapter);
                    }else {
                        jiage_list.setVisibility(View.GONE);
                       qujian_ll.setVisibility(View.GONE);
                      jiage_view.setVisibility(View.GONE);
                      jiage_view2.setVisibility(View.GONE);
                    }

                 }else{
                     ToasAlert.toastCenter(object.optString("optDesc"));

                 }

             }
         });

     }

    //详情页面被首页以及搜索页面启动且不缓存,所以详情页面只接收购物车发送的消息更新
    //更新的部分包括底部的购物车信息栏以及数量加减的布局
    public void onEventMainThread(String refresh) {
        if(MainActivity11.CARTREFRESH.equals(refresh)){

            int count=  queryCountNum();
//            cart_num.setText(count+"");

      }

    }

    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
        auto_view_pager.removeAllViews();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
    }





    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //开启自动轮播，延时时间为getInterval()

        auto_view_pager.startAutoScroll();
        //开启自动轮播，并设置延时
//        auto_view_pager.startAutoScroll(delayTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //停止轮播
        auto_view_pager.stopAutoScroll();
    }
    AutoScrollViewPager auto_view_pager;
    private void setBannerImages(final List<String>imageList) {

     if(imageList.size()==1){
         auto_view_pager.stopAutoScroll();
     }
        auto_view_pager.setAdapter(new MyAdapter(imageList));
//
        //设置延时时间
        auto_view_pager.setInterval(5000);
        //设置轮播的方向 AutoScrollViewPager.RIGHT/AutoScrollViewPager.LEFT
        auto_view_pager.setDirection(AutoScrollViewPager.RIGHT);
        //设置是否自动循环轮播，默认为true
        //注意:一旦设为true，则不能和ViewPagerIndicator一起使用
          auto_view_pager.setCycle(true);
        //设置切换动画的时长
        auto_view_pager.setScrollDurationFactor(5);
        //设置当滑动到最后一个或者第一个时，如何切换下一张
        /**
         * SLIDE_BORDER_MODE_NONE:不能再滑动
         * SLIDE_BORDER_MODE_TO_PARENT:移动父视图的Pager m
         * SLIDE_BORDER_MODE_CYCLE:循环
         * 默认为SLIDE_BORDER_MODE_NONE
         */
////        auto_view_pager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);
        //当滑动到最后一张或第一张时是否开启动画，默认为true
        auto_view_pager.setBorderAnimation(true);
        //当触摸的时候，停止轮播
        auto_view_pager.setStopScrollWhenTouch(true);

//
//        //解决最后一个跳转到第一个闪动问题
//         auto_view_pager.setCurrentItem((Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % imageList.size()));

        auto_view_pager.setCurrentItem(imageList.size()*10);

        final LinearLayout point_group = (LinearLayout)
                 findViewById(R.id.point_group);
        point_group.removeAllViews();
        for (int i = 0; i < imageList.size(); i++) {
            // 添加指示点
            ImageView point = new ImageView(VpHeadGoodDetailNewActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = 10;

            point.setLayoutParams(params);
            point.setBackgroundResource(R.drawable.point_bg);
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
            }
            point_group.addView(point);

        }

//
         auto_view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }
                @Override
                public void onPageSelected(int position) {
                   // Logger.e("TAG", "position" + position);
                    // 改变指示点的状态
                    // 把当前点enbale 为true
                    try {
                        point_group.getChildAt(position % imageList.size()).setEnabled(true);
                        // 把上一个点设为false
                        point_group.getChildAt(lastPosition).setEnabled(false);
                        lastPosition = position % imageList.size();
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

    }
    int lastPosition;




    class MyAdapter extends PagerAdapter {

         List<String> mSimpleDraweeViewList;
        public MyAdapter(List<String> mSimpleDraweeViewList) {
            this.mSimpleDraweeViewList=mSimpleDraweeViewList;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

//            Uri uri = Uri.parse(HttpUtil.HOST_STRING +"/"+ mSimpleDraweeViewList.get(position % mSimpleDraweeViewList.size()));

//            SimpleDraweeView draweeView = (SimpleDraweeView)View.inflate(VpNewDetailNewActivity.this,R.layout.layout_image,null).findViewById(R.id.image1);
//            draweeView.setScaleType( ImageView.ScaleType.FIT_XY);
//            ViewGroup.LayoutParams imagebtn_params = new ViewGroup.LayoutParams(
//                    AutoUtils.getDisplayWidthValue(720), AutoUtils.getDisplayHeightValue(440));
//            draweeView.setLayoutParams(imagebtn_params);
//
//            draweeView.setImageURI(uri);

            ImageView draweeView= new ImageView(VpHeadGoodDetailNewActivity.this);
            draweeView.setScaleType( ImageView.ScaleType.FIT_XY);
            Picasso.with(VpHeadGoodDetailNewActivity.this).load(HttpUtil.HOST_STRING +"/"+ mSimpleDraweeViewList.get(position % mSimpleDraweeViewList.size())).error(R.mipmap.error).into(draweeView);

            ViewParent parent = draweeView.getParent();
            // remove掉View之前已经加到一个父控件中，否则报异常
            if (parent != null) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(draweeView);
            }
            container.addView(draweeView);
            return draweeView;
//            container.addView(images.get(position % images.size()));
//            return images.get(position % images.size());
//            container.addView(draweeView);
//          return draweeView;
        }


    }

    public   int queryCountNum(){
        try {
//            List<Product> mProductList  =cartDao.queryBuilder().where().eq("id", id).and().eq("account",HttpBase.getAccount(getApplicationContext())).query();
            List<VpNewProductDb> mProductList  =mVpNewProductDbDaoDao.queryBuilder().query();
            if(mProductList!=null && mProductList.size()>0){
                return mProductList.size();
            }
            return 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }
    public class QujianAdapter extends  RecyclerView.Adapter<QujianAdapter.MyViewHolder> {
        Context context;
        String unit;
        public List<VpGood.RkPriceListItem> rkpriceList;

        public QujianAdapter(Context context , List<VpGood.RkPriceListItem> rkPriceList, String unit) {
            this.context=context;
            this.rkpriceList=rkPriceList;
            this.unit=unit;

        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=   LayoutInflater.from(context).inflate(R.layout.vp_acticity_jiage_qj, null, false);
            AutoUtils.auto(view);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final VpGood.RkPriceListItem   mRkpriceListItem =  rkpriceList.get(position);
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
    //动画
    public void startAnimator(ImageView view){
        ObjectAnimator scaleAnimatorX = new ObjectAnimator().ofFloat(view,"scaleX", 0.6f, 1.0f);
        ObjectAnimator scaleAnimatorY = new ObjectAnimator().ofFloat(view,"scaleY", 0.6f, 1.0f);
        scaleAnimatorX.setInterpolator(new AccelerateInterpolator());
        scaleAnimatorY.setInterpolator(new AccelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleAnimatorX).with(scaleAnimatorY) ;
        animatorSet.setDuration(800);
        animatorSet.start();

    }
    //根据数量获得区间的价格
    public String getPrice(String num,  List<RankPriceItem> rankPrice){

        num=TextUtils.isEmpty(num)?"0":num;
        String curr="";
        for(RankPriceItem mRankPriceItem:rankPrice){
            double  numDouble =Double.parseDouble(num);


            if(!TextUtils.isEmpty(mRankPriceItem. end_qty) ){//不是最后的

                double   begin_qtyDouble=Double.parseDouble(mRankPriceItem.begin_qty);
                double  end_qtyDouble =Double.parseDouble(mRankPriceItem.end_qty);
                if(begin_qtyDouble <= numDouble && numDouble<= end_qtyDouble){
                    curr= mRankPriceItem .qty_price;
                    break;
                }

            }else { //最后
                curr= mRankPriceItem .qty_price;

            }
        }
        return  curr;

    }
}
