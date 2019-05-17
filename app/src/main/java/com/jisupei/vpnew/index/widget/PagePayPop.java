package com.jisupei.vpnew.index.widget;

/**
 * Created by xiayumo on 16/5/11.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jisupei.R;
import com.jisupei.activity.base.event.CartEvent;
import com.jisupei.vpnew.index.model.ProductVpNewSearch;

import de.greenrobot.event.EventBus;

public class PagePayPop extends PopupWindow {


    private View view;

    private Context mContext;
    ProductVpNewSearch  mProductItem;

    EditText jian;
//    EditText dai_num;
  TextView now_price;
    int  curr=1;
    public void onEventMainThread(CartEvent event) {

    }
    public PagePayPop(final Activity context, boolean hdfkFlag, String hdfkFlagStr) {
        super(context);
        this.mContext=context;

        EventBus.getDefault().register(this);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.pay_zaixian, null);
        RelativeLayout zaixian_ll= (RelativeLayout) view.findViewById(R.id.zaixian_ll);
        RelativeLayout hdfk_ll= (RelativeLayout) view.findViewById(R.id.hdfk_ll);//货到付款
        final ImageView zaixian_iv= (ImageView) view.findViewById(R.id.zaixian_iv);
        final ImageView hdfk_iv= (ImageView) view.findViewById(R.id.hdfk_iv);//货到付款
        final TextView hdfktext= (TextView) view.findViewById(R.id.hdfktext);//货到付款
        Button submit_bt2= (Button) view.findViewById(R.id.submit_bt2);//

        zaixian_iv.setImageResource(R.drawable.check_sel);
        hdfk_iv.setImageResource(R.drawable.check);
        zaixian_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                zaixian_iv.setImageResource(R.drawable.check_sel);

                hdfk_iv.setImageResource(R.drawable.check);
                curr=1;
            }
        });
        if(hdfkFlag){
            hdfk_ll.setEnabled(true);
//            hdfk_ll.setAlpha(1.0f);
            hdfk_iv.setAlpha(1.0f);
            hdfktext.setAlpha(1.0f);
        }else {
            hdfk_ll.setEnabled(false);
//            hdfk_ll.setAlpha(0.5f);
            hdfk_iv.setAlpha(0.1f);
            hdfktext.setAlpha(0.5f);
            hdfktext.setText(hdfkFlagStr);
        }
        hdfk_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hdfk_iv.setImageResource(R.drawable.check_sel);
                zaixian_iv.setImageResource(R.drawable.check);
                curr=2;
            }
        });
        submit_bt2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mRefresh.queding(curr);
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

    }




//
    Refresh mRefresh;
    public  void setOnRefresh(Refresh mRefresh){
        this.mRefresh=mRefresh;
    }
    public interface     Refresh{
        void queding(int curr);
    }

}