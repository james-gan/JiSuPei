package com.jisupei.vpnew.category.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.jisupei.R;
import com.jisupei.activity.homepage2.MainActivity11;
import com.jisupei.activity.homepage2.SectionedBaseAdapter;
import com.jisupei.utils.db.DatabaseHelper;
import com.jisupei.http.HttpBase;
import com.jisupei.http.HttpUtil;
import com.jisupei.activity.base.model.Product;
import com.jisupei.utils.AppUtils;
import com.jisupei.utils.SPUtils;
import com.jisupei.vpnew.category.bean.CategoryProductList;
import com.jisupei.vpnew.category.widget.AddNewVpNumCartCategoryPop;
import com.jisupei.vpnew.index.activity.VpNewDetailNewActivity;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.List;

import de.greenrobot.event.EventBus;


public class VpNewSectionedRightAdapter extends SectionedBaseAdapter {

	private Context mContext;
	private String[] leftStr;
	private String[][] rightStr;
	private List<CategoryProductList> mIndexProductListArray;
    Dao<Product,Integer> cartDao;

	public VpNewSectionedRightAdapter(Context context, String[] leftStr, String[][] rightStr){
		this.mContext = context;
		this.leftStr = leftStr;
		this.rightStr = rightStr;
	}
	public VpNewSectionedRightAdapter(Context context, List<CategoryProductList> mIndexProductListArray){
		this.mContext = context;
		this.mIndexProductListArray = mIndexProductListArray;
        try {
            cartDao = DatabaseHelper.getHelper(mContext).getCartDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
    @Override
    public Object getItem(int section, int position) {
        return mIndexProductListArray.get(section).list.get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
         return mIndexProductListArray==null?0: mIndexProductListArray.size();
    }

    @Override
    public int getCountForSection(int section) {
        return mIndexProductListArray.get(section).list.size();
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            layout = (LinearLayout) inflator.inflate(R.layout.list_item, null);
            convertView = inflator.inflate(R.layout.vpnew_adapter_category_good, null);
            holder. right_dish_name_tv = (TextView)convertView.findViewById(R.id.right_dish_name);

            holder. xuangou = (ImageView)convertView.findViewById(R.id.xuangou);
            //供应商
            holder. gys = (TextView)convertView.findViewById(R.id.gys);
            //价格
            holder.  sp_jiage = (TextView)convertView.findViewById(R.id.sp_jiage);
            //规格
            holder.  sp_guig_tv = (TextView)convertView.findViewById(R.id.sp_guig_tv);

            //图片
            holder.   img = (ImageView)convertView.findViewById(R.id.img);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final CategoryProductList.NewVpIndexProductItem dish = mIndexProductListArray.get(section).list.get(position);

     holder.gys.setText(TextUtils.isEmpty(dish.supplier_name)?"":dish.supplier_name);


        holder.right_dish_name_tv.setText(dish.sku_name);
        if("0".equals(dish.price)||"0.0".equals(dish.price)||"0.00".equals(dish.price)){
            holder.sp_jiage.setVisibility(View.GONE);
            holder.sp_jiage.setText(""); //价格

        }else {

             //批发商
            if("2".equals(HttpBase.typeId)){
                holder.sp_jiage.setText("￥"+dish.unit_price+"/"+dish.unit); //价格
            }else {
                holder.sp_jiage.setText("￥"+dish.price+"/"+dish.uom_default); //价格
            }

            holder.sp_jiage.setVisibility(View.VISIBLE);

        }



        boolean  ishasimage= (boolean)SPUtils.get(mContext,"ishasimage",true);
        if(ishasimage){ //有图
            //加载图
            holder.img.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(HttpUtil.HOST_STRING +"/"+ dish.img_path).resize(AppUtils.dp2px(mContext,50),AppUtils.dp2px(mContext,50)).error(R.mipmap.error).into(holder.img);
        }else {
            holder.img.setVisibility(View.GONE);
        }


        if("0".equals(dish.equation_factor)||"1".equals(dish.equation_factor)||"1.0".equals(dish.equation_factor)|| TextUtils.isEmpty(dish.equation_factor)){

            holder.sp_guig_tv.setText("规格:"+dish.styleno);//规格

        }else {  //2 个单位
            holder.sp_guig_tv.setText("规格:"+dish.styleno+"(1"+dish.unit+"="+dish.equation_factor+dish.uom_default+")");//规格

        }

        //辅助单位的点击
        holder.xuangou.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                backgroundAlpha(0.5f);
                AddNewVpNumCartCategoryPop mPopupWindow = new AddNewVpNumCartCategoryPop((Activity) mContext,dish);
                ((Activity)mContext). getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                        WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                 int[] location = new int[2];
                 v.getLocationOnScreen(location);
                mPopupWindow.showAtLocation(holder.xuangou,  Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//				  mPopupWindow.showAsDropDown(right_listview);
                mPopupWindow.setOnRefresh(new AddNewVpNumCartCategoryPop.Refresh() {
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

        convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
                Intent intent  = new Intent(mContext,VpNewDetailNewActivity.class);
                intent.putExtra("productid",mIndexProductListArray.get(section).list.get(position).id);

                mContext.startActivity(intent);

			}
		});
        return convertView;
    }
    public final class ViewHolder {
        public TextView right_dish_name_tv;
        public TextView gys;
        public TextView sp_jiage;
        public TextView sp_guig_tv;
        public ImageView xuangou;
        public ImageView img;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.header_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        layout.setClickable(false);
        ((TextView) layout.findViewById(R.id.textItem)).setText(mIndexProductListArray.get(section).NAME);
        return layout;
    }

    //根据id查询
    public   Product queryProduct(String  id){
        try {
//            List<Product> mProductList  =cartDao.queryBuilder().where().eq("id", id).and().eq("account", HttpBase.getAccount(mContext)).query();
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
        WindowManager.LayoutParams lp =((Activity)mContext). getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity)mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((Activity)mContext).getWindow().setAttributes(lp);
    }

}
