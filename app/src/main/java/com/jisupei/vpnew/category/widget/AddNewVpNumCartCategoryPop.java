package com.jisupei.vpnew.category.widget;

/**
 * Created by xiayumo on 16/5/11.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.jisupei.R;
import com.jisupei.utils.db.DatabaseHelper;
import com.jisupei.activity.base.event.CartEvent;
import com.jisupei.http.HttpBase;
import com.jisupei.http.HttpUtil;
import com.jisupei.activity.base.model.Product;
import com.jisupei.activity.base.model.RankPriceItem;
import com.jisupei.utils.AutoUtils;
import com.jisupei.vpnew.category.bean.CategoryProductList;
import com.jisupei.vpnew.db.VpNewProductDb;
import com.jisupei.utils.widget.ToasAlert;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class AddNewVpNumCartCategoryPop extends PopupWindow {
    private View view;
    private Context mContext;
    CategoryProductList.NewVpIndexProductItem mCategoryProductItem;
    Dao<Product,Integer> cartDao;
    Dao<RankPriceItem,Integer> getRankPriceDao;
    EditText jian;
//    EditText dai_num;
  TextView now_price;
    public void onEventMainThread(CartEvent event) {
    }
    public AddNewVpNumCartCategoryPop(final Activity context, CategoryProductList.NewVpIndexProductItem mCategoryProductItem) {
        super(context);
        this.mContext=context;
        this.mCategoryProductItem=mCategoryProductItem;
        try {
            cartDao = DatabaseHelper.getHelper(mContext).getCartDao();
            getRankPriceDao = DatabaseHelper.getHelper(mContext).getRankPriceDao();
            mVpNewProductDbDaoDao = DatabaseHelper.getHelper(mContext).getVpNewProductDbDaoDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        EventBus.getDefault().register(this);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.vpnew_pop_add_cart_num, null);
        RecyclerView jiage_list= (RecyclerView) view.findViewById(R.id.jiage_list);
        initCartData(jiage_list);
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

    }

    private void initCartData( RecyclerView jiage_list) {
        //底部购物车
        ImageView product_image= (ImageView) view.findViewById(R.id.product_image);
        LinearLayout qujian_ll= (LinearLayout) view.findViewById(R.id.qujian_ll);
        View hxian= (View) view.findViewById(R.id.hxian);
        TextView product_name= (TextView) view.findViewById(R.id.product_name);
        TextView product_guige= (TextView) view.findViewById(R.id.product_guige);
        TextView supplier_name= (TextView) view.findViewById(R.id.supplier_name);
        TextView price= (TextView) view.findViewById(R.id.price);
        now_price= (TextView) view.findViewById(R.id.now_price); //当前价格
        final TextView heji= (TextView) view.findViewById(R.id.heji); //当前合计价格

        Picasso.with(mContext).load(HttpUtil.HOST_STRING +"/"+ mCategoryProductItem.img_path).error(R.mipmap.error).into(product_image);
        product_name.setText(mCategoryProductItem.sku_name);
        product_guige.setText("规格:"+mCategoryProductItem.styleno);
        supplier_name.setText(TextUtils.isEmpty(mCategoryProductItem.supplier_name)?"":mCategoryProductItem.supplier_name);
        price.setText("￥"+mCategoryProductItem.price); //价格


        //输入框
          jian = (EditText)view.findViewById(R.id.jian);
        TextView  jian_danwei = (TextView)view.findViewById(R.id.jian_danwei);
        jian_danwei.setText(mCategoryProductItem.unit);
        Button submit_bt2 = (Button)view.findViewById(R.id.submit_bt2);
        submit_bt2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String idNew = mCategoryProductItem.id ;
                 updateNew(idNew,mCategoryProductItem);
            }
        });

        if(("0".equals(mCategoryProductItem.equation_factor)||"1".equals(mCategoryProductItem.equation_factor)||"1.0".equals(mCategoryProductItem.equation_factor)|| TextUtils.isEmpty(mCategoryProductItem.equation_factor))){

            product_guige.setText("规格:"+mCategoryProductItem.styleno);//规格

            //判断是不是批发商
        }else {  //2 个单位

                product_guige.setText("规格:"+mCategoryProductItem.styleno+"(1"+mCategoryProductItem.unit+"="+mCategoryProductItem.equation_factor+mCategoryProductItem.uom_default+")");//规格

        }
        VpNewProductDb  bean =queryProduct(mCategoryProductItem.id  );
        if(bean!=null){//是购物车有数据
            int jianNumDb= (int) HttpBase.div(Double.parseDouble(bean.pro_num),Double.parseDouble(bean.equation_factor));
            jian.setText(jianNumDb+"");

            if(mCategoryProductItem.rkpriceList!=null && mCategoryProductItem.rkpriceList.size()>0){//有区间

                String  nowCurrPrice =   getInsertPrice(TextUtils.isEmpty(jianNumDb+"")?"0":(jianNumDb+""),mCategoryProductItem.rkpriceList);
                if(!TextUtils.isEmpty(nowCurrPrice)){
                    now_price.setText("￥" + nowCurrPrice+"/"+mCategoryProductItem.unit);
                }else {//没这价格
                    now_price.setText("￥" + mCategoryProductItem.unit_price+"/"+mCategoryProductItem.unit);
                }

            }else{
                now_price.setText("￥" + mCategoryProductItem.unit_price+"/"+mCategoryProductItem.unit);
            }


        }else {
            jian.setText("");
            now_price.setText("￥" + mCategoryProductItem.unit_price+"/"+mCategoryProductItem.unit);

        }
        // 当前的价格
        String nowPriceTemp= now_price.getText().toString().contains("￥")?now_price.getText().toString().substring(1).split("/")[0]:now_price.getText().toString().split("/")[0];
        heji.setText(countPrice(TextUtils.isEmpty(jian.getText().toString())?"0":jian.getText().toString(),nowPriceTemp));

        //

//价格end
        View view2_hx=  view.findViewById(R.id.view2_hx);
        //区间列表


        if(mCategoryProductItem.rkpriceList!=null && mCategoryProductItem.rkpriceList.size()>0){
               jiage_list.setVisibility(View.VISIBLE);
            view2_hx.setVisibility(View.VISIBLE);
            qujian_ll.setVisibility(View.VISIBLE);
            hxian.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            jiage_list.setLayoutManager(linearLayoutManager);
            QujianAdapter adapter = new QujianAdapter(mContext,mCategoryProductItem.rkpriceList,mCategoryProductItem.unit);
            jiage_list.setAdapter(adapter);
        }else {
             jiage_list.setVisibility(View.GONE);
             view2_hx.setVisibility(View.GONE);
             qujian_ll.setVisibility(View.GONE);
            hxian.setVisibility(View.GONE);
        }

        view.findViewById(R.id.shopping_cart_bottom2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        jian.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("TAG", "输入文字中的状态，count是输入字符数");

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                Log.i("TAG", "输入文本之前的状态");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("TAG", "输入文字后的状态");
                  //
                if(mCategoryProductItem.rkpriceList!=null && mCategoryProductItem.rkpriceList.size()>0){

                        String  nowCurrPrice =   getInsertPrice(TextUtils.isEmpty(jian.getText().toString())?"0":jian.getText().toString(),mCategoryProductItem.rkpriceList);
                        if(!TextUtils.isEmpty(nowCurrPrice)){
                            now_price.setText("￥" + nowCurrPrice+"/"+mCategoryProductItem.unit);
                        }else {
                            now_price.setText("￥" + mCategoryProductItem.unit_price+"/"+mCategoryProductItem.unit);
                        }
                }else {
                    now_price.setText("￥" + mCategoryProductItem.unit_price+"/"+mCategoryProductItem.unit);
                }
                 String nowPriceTemp= now_price.getText().toString().contains("￥")?now_price.getText().toString().substring(1).split("/")[0]:now_price.getText().toString().split("/")[0];
                    heji.setText(countPrice(TextUtils.isEmpty(jian.getText().toString())?"0":jian.getText().toString(),nowPriceTemp));

            }
        });
        view.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }


    public class QujianAdapter extends  RecyclerView.Adapter<QujianAdapter.MyViewHolder> {
        Context context;
        String unit;
        List<CategoryProductList.RkpriceListItem>  rkpriceList;

        public QujianAdapter(Context context , List<CategoryProductList.RkpriceListItem> rkpriceList, String unit) {
            this.context=context;
            this.rkpriceList=rkpriceList;
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
             final CategoryProductList.RkpriceListItem mRkpriceListItem   =  rkpriceList.get(position);

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

    //根据id查询
    public   VpNewProductDb queryProduct(String  id){
        try {
            List<VpNewProductDb> mProductList  =mVpNewProductDbDaoDao.queryBuilder().where().eq("id", id).query();
            if(mProductList!=null&&mProductList.size()>0){
                return mProductList.get(0);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
    Dao<VpNewProductDb,Integer>  mVpNewProductDbDaoDao;
    //首页，添加修改购物车数据
    private void updateNew(String idNew, CategoryProductList.NewVpIndexProductItem mCategoryProductItem) {
        try {
           String jianNumStr= TextUtils.isEmpty(jian.getText().toString().toString().trim())?"0":jian.getText().toString().toString();
//           String daiNumStr= TextUtils.isEmpty(dai_num.getText().toString().toString())?"0":dai_num.getText().toString().toString();
            mVpNewProductDbDaoDao = DatabaseHelper.getHelper(mContext).getVpNewProductDbDaoDao();
            VpNewProductDb   mVpNewProductDb= queryProduct(idNew);
            if(mVpNewProductDb==null){ //无这数据
                if("0".equals(jianNumStr))return;
                String  equation_factor1=mCategoryProductItem.equation_factor;

                if(TextUtils.isEmpty(equation_factor1)||"0".equals(equation_factor1)){
                    equation_factor1="1";
                }
                if(jianNumStr.length()>8){
                    ToasAlert.toast("添加购物车失败，设置数量太大！");
                    return;
                }
                //首页添加购物车
                addCartNew(jianNumStr,  mCategoryProductItem);

            }else {  //有这数据
                String  equation_factor2 = mCategoryProductItem.equation_factor;
                if(TextUtils.isEmpty(equation_factor2)||"0".equals(equation_factor2)){
                    equation_factor2="1";
                }

                double pro_numtemp=       HttpBase.mul(Double.parseDouble(jianNumStr),Double.parseDouble(equation_factor2) );

                if(pro_numtemp!=0&& pro_numtemp!=0.00 && pro_numtemp!=0.0){

                       if(jian.getText().toString().toString().length()>8){

                            ToasAlert.toast("添加购物车失败，设置数量太大！");
                            return;
                      }

                    //现在的价格
                    String nowPriceTemp= now_price.getText().toString().contains("￥")?now_price.getText().toString().substring(1).split("/")[0]:now_price.getText().toString().split("/")[0];
                    mVpNewProductDb.curr_price=nowPriceTemp;
                    mVpNewProductDb.pro_num=(pro_numtemp+"");
                    mVpNewProductDbDaoDao.update(mVpNewProductDb);
                } else {//为0
                    mVpNewProductDbDaoDao.deleteById(mVpNewProductDb.id1);
                }

            }
            this.dismiss();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(mRefresh!=null)
         mRefresh.refresh();
        ToasAlert.toastCenter("添加购物车成功！");
    }


    //首页添加购物车
    public void  addCartNew(String jian1,CategoryProductList.NewVpIndexProductItem mCategoryProductItem){
        try {
            VpNewProductDb bean=new VpNewProductDb();
            bean.id=mCategoryProductItem.id;
            String  equation_factor=mCategoryProductItem.equation_factor;

            if(TextUtils.isEmpty(equation_factor)){
                equation_factor="1";
            }
            double pro_numTemp=       HttpBase.mul(Double.parseDouble(jian1),Double.parseDouble(equation_factor) );
            bean.pro_num=pro_numTemp + "";
            bean.styleno=mCategoryProductItem.styleno;
            bean.sku_name=mCategoryProductItem.sku_name;
            bean.sku_code=mCategoryProductItem.sku_code;
            bean.img_path=mCategoryProductItem.img_path;
            String nowPriceTemp= now_price.getText().toString().contains("￥")?now_price.getText().toString().substring(1).split("/")[0]:now_price.getText().toString().split("/")[0];
            bean.curr_price=nowPriceTemp;
            bean.price=mCategoryProductItem.price;
            bean.unit=mCategoryProductItem.unit;
            bean.equation_factor=mCategoryProductItem.equation_factor;
            bean.uom_default=mCategoryProductItem.uom_default;
            bean.class_id=mCategoryProductItem.class_id;  //分类的ID
            bean.unit_price=mCategoryProductItem.unit_price;

//            bean.setAccount(HttpBase.getAccount(mContext));


           if(mCategoryProductItem.rkpriceList!=null && mCategoryProductItem.rkpriceList.size()>0){
              Gson gson  =new Gson();
                  String rkpriceListJson=  gson.toJson(mCategoryProductItem.rkpriceList);
                   bean.rkpriceListJson=rkpriceListJson;

            }
            mVpNewProductDbDaoDao.create(bean);

        }  catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String  countPrice(String num ,String price){
            Double amount=0.00;

             Double numNum=Double.parseDouble(num);
            Double priceNUm=Double.parseDouble(price);
                  amount  = HttpBase.mul(numNum,priceNUm);
            return "￥"+new DecimalFormat("0.00").format(amount) ;

    }
    //根据数量获得区间的价格
    public String getInsertPrice(String num, List<CategoryProductList.RkpriceListItem> rkpriceList){

        num=TextUtils.isEmpty(num)?"0":num;

        String curr="";
        for(CategoryProductList.RkpriceListItem mRkpriceListItem:rkpriceList){
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

    Refresh mRefresh;
    public  void setOnRefresh(Refresh mRefresh){
        this.mRefresh=mRefresh;
    }
    public interface     Refresh{
        void refresh();
    }

}