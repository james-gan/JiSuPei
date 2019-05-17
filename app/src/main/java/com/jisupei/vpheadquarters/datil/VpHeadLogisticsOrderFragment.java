package com.jisupei.vpheadquarters.datil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingchen.pulltorefresh.PullableExpandableListView;
import com.jisupei.activity.base.LogisticsDetailDeliveryActivity;
import com.jisupei.R;
import com.jisupei.activity.datail.bean.LogisData;
import com.jisupei.activity.datail.wiget.NoScrolListView;
import com.jisupei.ywy.BaseFragment;
import com.jisupei.http.HttpUtil;
import com.jisupei.activity.base.model.ChildItem;
import com.jisupei.activity.base.model.GroupItem;
import com.jisupei.utils.AutoUtils;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/18.
 */
public class VpHeadLogisticsOrderFragment extends BaseFragment {

   @InjectView(R.id.mExpandableListView)
   PullableExpandableListView mExpandableListView;
    @InjectView(R.id.tishi)
    LinearLayout tishi;
    public static VpHeadLogisticsOrderFragment getChildOrderFragment() {
        VpHeadLogisticsOrderFragment instance = new VpHeadLogisticsOrderFragment();
        return instance;
    }
    @Override
    public View initView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_info_exception, null);

        ButterKnife.inject(this, rootView);
        return rootView;
    }
public String orderCode;
public String orderType;
    @Override
    public void initData() {
        mExpandableListView.setEmptyView(tishi);
        mExpandableListView.setVisibility(View.VISIBLE);
        tishi.setVisibility(View.GONE);
        AppLoading.getInstance().show(getActivity());
        HttpUtil.getInstance().VpViewLogisGroupPage(orderCode, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppLoading.close();
                ToasAlert.toast("连接服务器失败");
            }
            @Override
            public void onResponse(String response, int id) {
                Logger.e("tag", "返回订单列表 ->" + response);
                AppLoading.close();
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String optFlag = object.optString("optFlag");

                if ( "yes".equals(optFlag)) {
                    JSONArray res = object.optJSONArray("res");
                    if(res==null || res.length()==0){
//                        ToasAlert.toastCenter("没有查询到物流数据！");
                        mExpandableListView.setVisibility(View.GONE);
                        tishi.setVisibility(View.VISIBLE);
                        return;
                    }
                    final List<LogisData> mLogisDataList = new Gson().fromJson(res.toString(), new TypeToken<ArrayList<LogisData>>() {
                    }.getType());

                    if(mLogisDataList!=null && mLogisDataList.size()>0){
                        for (LogisData mWuLiuData:mLogisDataList ) {
                            if(mWuLiuData.orderLocalLink!=null&& mWuLiuData.orderLocalLink.size()>0){
                                for (LogisData.RrderLocalLink mRrderLocalLink : mWuLiuData.orderLocalLink) {
                                    LogisData.SiteLink mSiteLink1 = new LogisData().new  SiteLink();
                                    mSiteLink1.value=mRrderLocalLink.optDesc;
                                    mSiteLink1.column5=mRrderLocalLink.optTime;
                                 mSiteLink1.column4="发货客户";
                                    mWuLiuData.siteLinkListReal.add(mSiteLink1);
                                }
                            }
                            if(mWuLiuData.siteList!=null&&mWuLiuData.siteList.size()>0){

                                for (LogisData.Site mSite : mWuLiuData.siteList) {
                                    if(mSite.siteLinkList!=null&&mSite.siteLinkList.size()>0){
                                        for (LogisData.SiteLink mSiteLink: mSite.siteLinkList) {
                                            mWuLiuData.siteLinkListReal.add(mSiteLink);
                                        }

                                    }

                                }

                            }

                        }

                    }



                    MyBaseExpandableListAdapter mMyBaseExpandableListAdapter =new MyBaseExpandableListAdapter(getActivity(),mLogisDataList,res);
                    mExpandableListView.setAdapter(mMyBaseExpandableListAdapter);
                    mExpandableListView.setGroupIndicator(null);
                     if(mLogisDataList.size()==1){
                         mExpandableListView.expandGroup(0);
                         mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                             @Override
                             public boolean onGroupClick(ExpandableListView parent, View v,
                                                         int groupPosition, long id) {
                                 // TODO Auto-generated method stub
                                 return true;
                             }
                         });

                     }

                }else {
                    ToasAlert.toastCenter(object.optString("optDesc"));
                }
            }
        });

    }

    /**
     * ExpandListView的适配器，继承自BaseExpandableListAdapter
     *
     */
    public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {
        private Context mContext;
        List<LogisData> groupData;
        JSONArray res;
        //子项是一个map，key是group的id，每一个group对应一个ChildItem的list
//        private List< List<ChildItem>> childDataList;
        private Button groupButton;//group上的按钮

        public MyBaseExpandableListAdapter(Context context, List<LogisData>   groupData, JSONArray  res) {
            this.mContext = context;
            this.groupData = groupData;
            this.res = res;

        }
        public  void  addList( List<GroupItem> groupData, List< List<ChildItem>> childMap){
//            this.groupData.addAll(groupData);
//            this.childDataList.addAll(childMap);
//            notifyDataSetChanged();
        }
        /*
         *  Gets the data associated with the given child within the given group
         */
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            //我们这里返回一下每个item的名称，以便单击item时显示
//            return childDataList.get(groupPosition).get(childPosition).getTitle();
//            return groupData.get(groupPosition).siteLinkListReal.get(childPosition);
            return groupData.get(groupPosition);
        }
        /*
         * 取得给定分组中给定子视图的ID. 该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）
         */
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }
        /*
         *  Gets a View that displays the data for the given child within the given group
         */
        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView,
                                 ViewGroup parent) {
            ChildHolder childHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.child_logistics_detail_new, null);
                AutoUtils.auto(convertView);
                childHolder = new ChildHolder();
//                childHolder.childImg = (ImageView) convertView.findViewById(R.id.img_child);
                childHolder.list = (NoScrolListView) convertView.findViewById(R.id.list);
                childHolder.title_layout2 = (RelativeLayout) convertView.findViewById(R.id.title_layout2);

                //间隔
                childHolder.dsas = convertView.findViewById(R.id.dsas);
                convertView.setTag(childHolder);
            }else {
                childHolder = (ChildHolder) convertView.getTag();
            }


           TimeLineAdapter mTimeLineAdapter= new TimeLineAdapter(mContext,groupData.get(groupPosition).siteLinkListReal);
////
            childHolder.list.setAdapter(mTimeLineAdapter);
            if(groupData.size()==1){
                childHolder.dsas.setVisibility(View.GONE);
            }else {
                childHolder.dsas.setVisibility(View.VISIBLE);
            }
            childHolder.title_layout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,LogisticsDetailDeliveryActivity.class);
                    try {
                        intent.putExtra("batchSkus", ((JSONObject)res.get(groupPosition)).optString("groupSkuList"));
                        intent.putExtra("group_code", groupData.get(groupPosition).group_code);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mContext.startActivity(intent);
                }
            });


       // 查看异常

            return convertView;
        }

        /*
         * 取得指定分组的子元素数
         */
        @Override
        public int getChildrenCount(int groupPosition) {
            // TODO Auto-generated method stub
//            return groupData.get(groupPosition).groupSkuList.size();
            return 1;
        }

        /**
         * 取得与给定分组关联的数据
         */
        @Override
        public Object getGroup(int groupPosition) {
            return groupData.get(groupPosition);
        }

        /**
         * 取得分组数
         */
        @Override
        public int getGroupCount() {
            return groupData.size();
        }
        /**
         * 取得指定分组的ID.该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）
         */
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }
        /*
         *Gets a View that displays the given group
         *return: the View corresponding to the group at the specified position
         */
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder groupHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.wuliu_groupitem, null);
                AutoUtils.auto(convertView);
                groupHolder = new GroupHolder();
                groupHolder.groupImg = (ImageView) convertView.findViewById(R.id.img_indicator);
                groupHolder.groupText = (TextView) convertView.findViewById(R.id.grr_code);
                groupHolder.status = (TextView) convertView.findViewById(R.id.status);

                groupHolder.content = (LinearLayout) convertView.findViewById(R.id.content);
                groupHolder.grr_cimage = (ImageView) convertView.findViewById(R.id.grr_cimage);

                convertView.setTag(groupHolder);
            }else {
                groupHolder = (GroupHolder) convertView.getTag();
            }

            if(groupData.size()==1){
                groupHolder.groupImg.setVisibility(View.GONE);
                groupHolder.grr_cimage.setImageResource(R.mipmap.ckwl);
            }else {
                groupHolder.groupImg.setVisibility(View.VISIBLE);
                groupHolder.grr_cimage.setImageResource(R.mipmap.wldh);
            }

            if (isExpanded) {   //展开
//                groupHolder.groupImg.setBackgroundResource(R.mipmap.under_icon);
//                convertView.setBackgroundResource(R.mipmap.bjj);
                groupHolder.content.setBackgroundResource(R.mipmap.bjj);//背景图片

                groupHolder.groupImg.setBackgroundResource(R.mipmap.bottom);
                //展开
                groupHolder.status.setTextColor(Color.parseColor("#ffffffff"));
                groupHolder.groupText.setTextColor(Color.parseColor("#ffffffff"));

                groupHolder.grr_cimage.setImageResource(R.mipmap.ckwl);

            }else {  //关闭
                groupHolder.content.setBackgroundColor(Color.parseColor("#ffffffff"));

                groupHolder.groupImg.setBackgroundResource(R.mipmap.back_button11);

                groupHolder.status.setTextColor(Color.parseColor("#666666"));
                groupHolder.groupText.setTextColor(Color.parseColor("#666666"));

                groupHolder.grr_cimage.setImageResource(R.mipmap.wldh);
            }
                groupHolder.groupText.setText("运单编号:"+ groupData.get(groupPosition).group_code);


            if("3".equals(groupData.get(groupPosition).groupState)){
                groupHolder.status.setText("已签收");
            }else {
                groupHolder.status.setText("配送中");

            }

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            // Indicates whether the child and group IDs are stable across changes to the underlying data
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
        /**
         * show the text on the child and group item
         */
        private class GroupHolder
        {
            ImageView groupImg;
            ImageView grr_cimage;
            TextView groupText;
            TextView status;
            LinearLayout content;

        }
        private class ChildHolder
        {

            NoScrolListView list;
            RelativeLayout title_layout2;
            private View dsas;

        }

    }

//里面的线
    public class TimeLineAdapter extends BaseAdapter {
        private Context context;
//        WuLiuData mWuLiuData;
    List<LogisData.SiteLink> siteLinkListReal;
        private LayoutInflater inflater;

        public TimeLineAdapter(Context context,  List<LogisData.SiteLink> siteLinkListReal) {
            super();
            this.context = context;

            this.siteLinkListReal = siteLinkListReal;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return siteLinkListReal.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LogisData.SiteLink mSiteLink=    siteLinkListReal.get(position);

            ViewHolder viewHolder = null;
            if (convertView == null) {
                inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.logistics_detail_new_adapter, null);
                viewHolder = new ViewHolder();
                viewHolder.titile_tt = (TextView) convertView.findViewById(R.id.titile_tt);
                viewHolder.view_0 = (View) convertView.findViewById(R.id.view_0);
                viewHolder.view_1 = (View) convertView.findViewById(R.id.view_1);
                viewHolder.fhr_et = (ImageView) convertView.findViewById(R.id.fhr_et);
                viewHolder.order_id_tt = (TextView) convertView.findViewById(R.id.order_id_tt);
                viewHolder.sj_tt = (TextView) convertView.findViewById(R.id.sj_tt);

                //间隔
                viewHolder.dsas = convertView.findViewById(R.id.dsas);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }//【
            viewHolder.order_id_tt.setText(mSiteLink.value+":"+mSiteLink.column5);
            if(!TextUtils.isEmpty(mSiteLink.column2)){
                viewHolder.sj_tt.setVisibility(View.VISIBLE);
                viewHolder.sj_tt.setText("(司机:"+mSiteLink.column1+" 联系电话:"+mSiteLink.column2+")");

            }else {
               // viewHolder.sj_tt.setVisibility(View.VISIBLE);
                viewHolder.sj_tt.setVisibility(View.INVISIBLE);
            }
            //不是主站
            if(position!=0 && siteLinkListReal.get(position-1).column4.equals(mSiteLink.column4)){
                viewHolder.titile_tt.setVisibility(View.INVISIBLE);

                if(position==  siteLinkListReal.size()-1){
                    viewHolder.fhr_et.setImageResource(R.mipmap.xiaxiangnew);
                }else {
                    viewHolder.fhr_et.setImageResource(R.mipmap.xiaxiang);
                }

            }else {  //主站
                viewHolder.titile_tt.setVisibility(View.VISIBLE);
                viewHolder.fhr_et.setImageResource(R.mipmap.zhuzhan);
            }
            if(position==  siteLinkListReal.size()-1){
                viewHolder.titile_tt.setTextColor(Color.parseColor("#25AE5F"));
                viewHolder.order_id_tt.setTextColor(Color.parseColor("#25AE5F"));
                viewHolder.sj_tt.setTextColor(Color.parseColor("#25AE5F"));
                viewHolder.view_1.setVisibility(View.INVISIBLE);
             //间隔
                viewHolder.dsas.setVisibility(View.GONE);

            }else{
                viewHolder.titile_tt.setTextColor(Color.parseColor("#318eff"));
                viewHolder.order_id_tt.setTextColor(Color.parseColor("#333333"));
                viewHolder.sj_tt.setTextColor(Color.parseColor("#333333"));
                viewHolder.view_1.setVisibility(View.VISIBLE);

                //间隔
                viewHolder.dsas.setVisibility(View.GONE);
            }
            if(position== 0){

                viewHolder.view_0.setVisibility(View.INVISIBLE);
            }else{

                viewHolder.view_0.setVisibility(View.VISIBLE);
            }
            return convertView;

        }

        class ViewHolder{
            private TextView titile_tt ;
            private View view_0,view_1;
            private ImageView fhr_et;
            private TextView order_id_tt ;
            private TextView sj_tt ;

            private View dsas ;
        }

    }
}
