package com.jisupei.vpnew.category;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.jisupei.R;
import com.jisupei.activity.homepage2.MainActivity11;
import com.jisupei.activity.homepage2.PinnedHeaderListView;
import com.jisupei.utils.db.DatabaseHelper;
import com.jisupei.ywy.BaseFragment;
import com.jisupei.http.HttpUtil;
import com.jisupei.vpnew.category.adapter.VpNewSectionedRightAdapter;
import com.jisupei.vpnew.category.bean.CategoryProductList;
import com.jisupei.vpnew.db.VpNewProductDb;
import com.jisupei.vpnew.index.activity.VpNewProductSearchActivity;
import com.jisupei.utils.widget.AppLoading;
import com.jisupei.utils.widget.ToasAlert;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/18.
 */
public class VpcategoryFragment extends BaseFragment {


     @InjectView(R.id.pinnedListView)
     PinnedHeaderListView right_listview;


    @InjectView(R.id.left_listview)
    ListView left_listView;
    @InjectView(R.id.serach_ll)
    LinearLayout serach_ll;



    public static VpcategoryFragment getVpcategoryFragmentInstance() {
        VpcategoryFragment instance = new VpcategoryFragment();
        return instance;
    }

    @Override
    public View initView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.vpnew_vpcategory_fragment, null);
        ButterKnife.inject(this, rootView);
        EventBus.getDefault().register(this);
        left_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                isScroll = false;
                adapter.currPosition=position;
                adapter.notifyDataSetChanged();
                int rightSection = 0;
                for (int i = 0; i < position; i++) {
                    rightSection += sectionedAdapter.getCountForSection(i) + 1;
                }
                right_listview.setSelection(rightSection);

            }

        });
        serach_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(),VpNewProductSearchActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }

    public void selectCurrLeft(int  pos){

        isScroll = false;
        adapter.currPosition=pos;
        adapter.notifyDataSetChanged();
        int rightSection = 0;
        for (int i = 0; i < pos; i++) {
            rightSection += sectionedAdapter.getCountForSection(i) + 1;
        }
        right_listview.setSelection(rightSection);

    }

    public void onEventMainThread(String  refresh) {

        if(MainActivity11.CARTREFRESH.equals(refresh)){
            //刷新左边的数据
            adapter.notifyDataSetChanged();
            //右边的数据
            sectionedAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void initData() {

            loadData();
    }
    private boolean isScroll = true;
    private MyAdapter adapter;
    VpNewSectionedRightAdapter sectionedAdapter;
    //加载首页的数据
    List<CategoryProductList> mIndexProductListArray;
    public  void  loadData(){
        AppLoading.show(getActivity());
        HttpUtil.getInstance().getVpClassProductList(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToasAlert.toastCenter("连接服务器失败，请稍后再试！");
                AppLoading.close();

            }
            @Override
            public void onResponse(String response, int id) {
                Logger.e("TAG", response);
                AppLoading.close();
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String flag = object.optString("optFlag");
                if("yes".equals(flag)) { //
                    mIndexProductListArray = new Gson().fromJson(object.optString("res"), new TypeToken<ArrayList<CategoryProductList>>() {
                    }.getType());
                    List<Integer> currIndexEmpty=new ArrayList<Integer>();
                    currIndexEmpty.clear();
                    if(mIndexProductListArray!=null&&mIndexProductListArray.size()>0) {
                        for (int i = 0; i < mIndexProductListArray.size(); i++) {
                            CategoryProductList mCategoryProductList = mIndexProductListArray.get(i);
                            if(mCategoryProductList.list==null||mCategoryProductList.list.size()<1){
                                currIndexEmpty.add(i);
                            }


                        }
                    }
                    if(currIndexEmpty!=null&&currIndexEmpty.size()>0) {
                        for(int i=currIndexEmpty.size()-1;i>=0;--i){
                            mIndexProductListArray.remove( mIndexProductListArray.get(  currIndexEmpty.get(i)));
                        }
                    }

                      //左边的数据
                    adapter = new MyAdapter(mIndexProductListArray);
                    left_listView.setAdapter(adapter);
                    //右边
                    sectionedAdapter = new VpNewSectionedRightAdapter(getActivity(),  mIndexProductListArray);
                    right_listview.setAdapter(sectionedAdapter);



                    right_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView arg0, int scrollState) {
                            switch (scrollState) {
                                // 当不滚动时
                                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                                    // 判断滚动到底部
                                    if (right_listview.getLastVisiblePosition() == (right_listview.getCount() - 1)) {
                                        left_listView.setSelection(ListView.FOCUS_DOWN);
                                    }

                                    // 判断滚动到顶部
                                    if (right_listview.getFirstVisiblePosition() == 0) {
                                    }
                                    break;
                            }
                        }

                        int y = 0;
                        int x = 0;
                        int z = 0;

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            if (isScroll) {
                                x = sectionedAdapter.getSectionForPosition(right_listview.getFirstVisiblePosition());
                                if(x!=adapter.currPosition){ //和当前不相等
                                    adapter.currPosition=x;
                                    adapter.notifyDataSetChanged();

                                    if(adapter.currPosition > left_listView.getLastVisiblePosition()){
                                        left_listView.setSelection(x );
                                    }
                                    if(adapter.currPosition < left_listView.getFirstVisiblePosition()){
                                        left_listView.setSelection(x );
                                    }
                                }

                            } else {
                                isScroll = true;
                            }
                        }
                    });
                    //
                }else {
                    String optDesc = object.optString("optDesc");
                    ToasAlert.toastCenter(optDesc);
                }
            }
        });

    }

    //左边的
    public class MyAdapter extends BaseAdapter {

        List<CategoryProductList> mIndexProductListArray;
        public		int currPosition = 0;

        public  MyAdapter (	List<CategoryProductList> mIndexProductListArray){
            this.mIndexProductListArray = mIndexProductListArray;

        }
        @Override
        public int getCount() {
            return mIndexProductListArray==null?0: mIndexProductListArray.size();
        }

        @Override
        public Object getItem(int arg0) {
            return mIndexProductListArray.get(arg0);
        }
        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View arg1, ViewGroup arg2) {
            Holder holder = null;
            if (arg1 == null) {
                holder = new Holder();
                arg1 = LayoutInflater.from(getActivity()).inflate(R.layout.left_list_item, null);
                holder.left_list_item = (TextView) arg1.findViewById(R.id.left_list_item);
                holder.cart_num = (TextView) arg1.findViewById(R.id.cart_num);
                holder.left_view =  arg1.findViewById(R.id.left_view);
                holder.rl =  (RelativeLayout)arg1.findViewById(R.id.rl);

                arg1.setTag(holder);
            } else {
                holder = (Holder) arg1.getTag();
            }
            holder.updataView(position);
            return arg1;
        }

        private class Holder {
            private TextView left_list_item;
            private View left_view;
            private TextView cart_num;
            private RelativeLayout rl;
            public void updataView(final int position) {
                left_list_item.setText(mIndexProductListArray.get(position).NAME);
                int count=	searchClassPriductCount(mIndexProductListArray.get(position).ID);
                if(count==0){
                    cart_num.setText("");
                    cart_num.setVisibility(View.GONE);

                }else{
                    cart_num.setText(count+"");
                    cart_num.setVisibility(View.VISIBLE);
                }
                //选中的效果
                if(currPosition==position){
                    rl.setBackgroundColor(Color.rgb(255, 255, 255));
                    left_view.setVisibility(View.VISIBLE);
                    left_list_item.setTextColor(getResources().getColor(R.color.blue_head));
                }else{  //没有选中的效果
                    rl.setBackgroundColor(Color.TRANSPARENT);
                    left_list_item.setTextColor(Color.parseColor("#666666"));
                    left_view.setVisibility(View.GONE);
                }
            }

        }
        public  int 	searchClassPriductCount(String class_id){
            Dao<VpNewProductDb,Integer>   mVpNewProductDbDao= null;
            try {
               mVpNewProductDbDao = DatabaseHelper.getHelper(getActivity()).getVpNewProductDbDaoDao();
//			 List<Product> data = cartDao.queryBuilder().orderBy("id1", false).where().eq("account", HttpBase.getAccount(getApplicationContext())).and().eq("class_id", class_id).query();
                List<VpNewProductDb> data = mVpNewProductDbDao.queryBuilder().orderBy("id1", false).where().eq("class_id", class_id).query();
                if(data==null){
                    return 0;
                }

                return data.size();

            } catch (SQLException e) {
                e.printStackTrace();
                return 0;
            }
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }



}
