package com.jisupei.vpnew.index.adapter;

/**
 * Created by xiayumo on 16/5/12.
 */

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.jisupei.R;
import com.jisupei.utils.db.DatabaseHelper;
import com.jisupei.http.HttpBase;
import com.jisupei.http.HttpUtil;
import com.jisupei.vpnew.db.VpNewProductDb;
import com.jisupei.vpnew.index.model.ProductVpNewSearch;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by xiayumo on 16/5/11.
 */
public class ProductConfirmAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

//    private List<Product> data;

    Context context;
    List<ProductVpNewSearch> listObj;

    public ProductConfirmAdapter(Context context,List<ProductVpNewSearch> listObj){

        this.context=context;
        this.listObj=listObj;
        this.mInflater = LayoutInflater.from(context);

//        try {
////            Dao<Product,Integer> cartDao = DatabaseHelper.getHelper(context).getCartDao();
//       //     data = cartDao.queryForAll();
////            data=  cartDao.queryBuilder().orderBy("id1", false).where().eq("account",HttpBase.getAccount(context)).query();
////            data=  cartDao.queryBuilder().orderBy("id1", false).query();
////            if(data==null)data=new ArrayList<>();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }

    public final class ViewHolder{
        public ImageView img;
        public TextView name;
        public TextView text2;
        public TextView styleno;
        public TextView price_text;
        public TextView sl;
        public TextView gys;//

    }

    @Override
    public int getCount() {
        return listObj.size();
    }

    @Override
    public Object getItem(int position) {
        return listObj.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder ;
        ProductVpNewSearch mProductVpNewSearch=listObj.get(position);
        if (convertView == null) {

            holder=new ViewHolder();

            convertView = mInflater.inflate(R.layout.vpnew_product_item_conmit, null);
            holder.img = (ImageView)convertView.findViewById(R.id.product_img);
            holder.name = (TextView)convertView.findViewById(R.id.name_text);

            holder.styleno = (TextView)convertView.findViewById(R.id.rank_text);
            holder.price_text = (TextView)convertView.findViewById(R.id.price_text);
            holder.sl = (TextView)convertView.findViewById(R.id.sl);
            holder.gys = (TextView)convertView.findViewById(R.id.cc_text);

            convertView.setTag(holder);

        }else {

            holder = (ViewHolder)convertView.getTag();
        }

        // 图片
         Picasso.with(context).load(HttpUtil.HOST_STRING+"/"+mProductVpNewSearch.img_path) .error(R.mipmap.error).into(holder.img);
        holder.img.setVisibility(View.VISIBLE);
        holder.name.setText(mProductVpNewSearch.sku_name);
        holder.gys.setText(mProductVpNewSearch.supplier_name);

        if( "0.0".equals(mProductVpNewSearch.unit_price)||TextUtils.isEmpty(mProductVpNewSearch.unit_price)||"0".equals(mProductVpNewSearch.unit_price)){
            holder.price_text.setText("");
            holder.price_text.setVisibility(View.GONE);
        }else{
           holder.price_text.setText("￥" + new DecimalFormat("0.00").format(Double.parseDouble(mProductVpNewSearch.unit_price))+"/"+mProductVpNewSearch.unit);
            holder.price_text.setVisibility(View.VISIBLE);
        }

        String unit2 = mProductVpNewSearch.unit;
        if(TextUtils.isEmpty(unit2)){

            mProductVpNewSearch.equation_factor="1";

        }

        if("0".equals(mProductVpNewSearch.equation_factor)||"1".equals(mProductVpNewSearch.equation_factor)||"1.0".equals(mProductVpNewSearch.equation_factor) ||TextUtils.isEmpty(mProductVpNewSearch.equation_factor)){//一个单位

            holder.styleno.setText("规格:"+mProductVpNewSearch.styleno);


        }else { //2个单位
            holder.styleno.setText("规格:"+mProductVpNewSearch.styleno+"(1"+mProductVpNewSearch.unit+"="+mProductVpNewSearch.equation_factor+mProductVpNewSearch.uom_default+")");

        }
        String mDbNum   = queryProductDbNum(mProductVpNewSearch.id);

        holder.sl.setText("请货量:"+mDbNum+mProductVpNewSearch.unit);
        return convertView;
    }

    //得到件
//    Dao<Product,Integer> cartDao;
//    public   Product queryProduct(int  id){
//        try {
////            List<Product> mProductList  =cartDao.queryBuilder().where().eq("id", id).and().eq("account",HttpBase.getAccount(context)).query();
//            List<Product> mProductList  =cartDao.queryBuilder().where().eq("id", id).query();
//            if(mProductList!=null&&mProductList.size()>0){
//                return mProductList.get(0);
//            }
//            return null;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//    }
    //根据id查询单个数量
    public String queryProductDbNum(String id) {
        try {

            Dao<VpNewProductDb, Integer> mVpNewProductDbDaoDao = DatabaseHelper.getHelper(context).getVpNewProductDbDaoDao();

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

}