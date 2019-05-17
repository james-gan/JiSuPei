package com.jisupei.vpnew.index.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.jisupei.R;
import com.jisupei.utils.db.DatabaseHelper;
import com.jisupei.http.HttpUtil;
import com.jisupei.activity.base.model.Product;
import com.jisupei.utils.AppUtils;
import com.jisupei.vpnew.index.activity.VpNewDetailNewActivity;
import com.jisupei.vpnew.index.model.IndexData;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/26.
 */
public class VpIndexAdapter1 extends  RecyclerView.Adapter<VpIndexAdapter1.MyViewHolder> {
    Context context;
    LayoutInflater inflate;
    List<IndexData.ProductNew> mProductNews;

    Dao<Product,Integer> cartDao;

    public VpIndexAdapter1(Context context, List<IndexData.ProductNew> mProductNews ) {
        this.context=context;
        this.mProductNews=mProductNews;
//
        try {
            cartDao = DatabaseHelper.getHelper(context).getCartDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View view=   LayoutInflater.from(context).inflate(R.layout.newvp_product_item_nologin, null, false);
//        AutoUtils.auto(view);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final IndexData.ProductNew mProductNew   =  mProductNews.get(position);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToasAlert.toastCenter(""+position);
                Intent intent = new Intent(context,VpNewDetailNewActivity.class);
             intent.putExtra("productid",mProductNew.id);
                context.startActivity(intent);
            }
        });



        holder.name_text.setText(mProductNew.sku_name);
    Picasso.with(context).load(HttpUtil.HOST_STRING +"/"+ mProductNew.img_path).error(R.mipmap.error).resize(AppUtils.dp2px(context,120),AppUtils.dp2px(context,120)).into(holder.img);
        holder.price_text.setText("￥"+mProductNew.unit_price+"/"+mProductNew.unit); //价格

        holder.supplier_name.setText(mProductNew.supplier_name); //价格
        holder.styleno.setText("规格:"+mProductNew.styleno); //价格
    }
    @Override
    public int getItemCount() {

        return  mProductNews.size();
    }
    static class MyViewHolder  extends RecyclerView.ViewHolder{


            public TextView name_text;
        public ImageView img;
        public TextView price_text;
        public TextView supplier_name;
        public TextView styleno;
        public   MyViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            name_text=(TextView)view.findViewById(R.id.name_text);
            img = (ImageView)view.findViewById(R.id.product_img);
            price_text = (TextView)view.findViewById(R.id.price_text);
            styleno = (TextView)view.findViewById(R.id.styleno);
            supplier_name = (TextView)view.findViewById(R.id.supplier_name);
        }
    }


}

