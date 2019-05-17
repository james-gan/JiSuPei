package com.jisupei.http;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.jisupei.activity.order.bean.ReqConFirm;
import com.jisupei.application.MyApplication;
import com.jisupei.ywy.order.bean.RequestParamDealWith;
import com.jisupei.ywy.order.bean.RequestParameterAudit;
import com.jisupei.activity.base.model.LogisticsItem;
import com.jisupei.activity.base.model.RequestConFirm;
import com.jisupei.activity.base.model.RequestRegPam;
import com.jisupei.vpheadquarters.order.bean.PaidInParams;
import com.jisupei.vpheadquarters.seting.bean.VpBIanjiParams;
import com.jisupei.vpheadquarters.seting.bean.VpPricesParams;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
public class HttpUtil {
    // 生产地址
    public static final String HOST_STRING = "http://scm.lbd99.com/scm";
    //测试环境

    static HttpUtil mHttpUtil;
    private HttpUtil() {
    }
    public static HttpUtil getInstance() {
        if (mHttpUtil == null) {
            mHttpUtil = new HttpUtil();
        }
        return mHttpUtil;
    }
    public void baseHttp(String murl, String jsonString, StringCallback mCallback) {
        JSONObject json = null;
        try {
            json = new JSONObject(jsonString);
            json.put("deviceOS", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpUtils
                .post()
                .url(HOST_STRING + murl)
                .addParams("content", json != null ? json.toString() : jsonString)
                .addParams("version", "1")
                .addParams("deviceOS", "android-" + android.os.Build.VERSION.RELEASE)
                .addParams("login_token", HttpBase.token)
                .build()
                .execute(mCallback);
    }

    public void baseHttpNoCheck(String murl, String jsonString, StringCallback mCallback) {
        JSONObject json = null;
        try {
            json = new JSONObject(jsonString);
            json.put("deviceOS", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .post()
                .url(HOST_STRING + murl)
                .addParams("content", json != null ? json.toString() : jsonString)
                .addParams("deviceOS", "android-" + android.os.Build.VERSION.RELEASE)
                .addParams("version", "1")
                .addParams("login_token", "123")
                .build()
                .execute(mCallback);
    }

    //修改联系人的信息
    public void updateContactNameDate(String jsonString, StringCallback mCallback) {

        baseHttp(updateContactName, jsonString, mCallback);
    }

    //修改联系人的联系电话，获取验证码
    public void sendYzmDate(String phone, StringCallback mCallback) {
        // ‘{phone:18164012587}’
        JSONObject object = new JSONObject();

        try {
            object.put("phone", phone);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        baseHttp(sendYzm, object.toString(), mCallback);
    }

    //修改联系人的联系电话，验证验证码
    public void valYzmDate(String phone, String yzm, StringCallback mCallback) {
        //  ‘{phone:18164012587,yzm:1234}’
        JSONObject object = new JSONObject();

        try {
            object.put("phone", phone);
            object.put("yzm", yzm);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        baseHttp(valYzm, object.toString(), mCallback);
    }

    //修改联系人的联系电话
    public void updateContactPhoneDate(String contactPhone, String yzm, StringCallback mCallback) {
        // ‘{ contactPhone:电话,account:123}’
        JSONObject object = new JSONObject();

        try {
            object.put("contactPhone", contactPhone);
            object.put("account", HttpBase.account);
            object.put("phone", contactPhone);
            object.put("yzm", yzm);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        baseHttp(updateContactPhone, object.toString(), mCallback);
    }

    //分类的搜索
    public void searchOrderDate(String state, int pageno, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("shopId", HttpBase.shopId);
            object.put("account", HttpBase.account);
            object.put("pageNO", pageno);
            object.put("pageSize", 50);

            if (!TextUtils.isEmpty(state)) {
                object.put("handleStatus", state);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(searchOrder, object.toString(), mCallback);
    }

    //分类的搜索,关键字
    public void searchOrderVal(int pageno, String searchContent, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("shopId", HttpBase.shopId);
            object.put("account", HttpBase.account);
            object.put("pageNO", pageno);
            object.put("pageSize", 30);
            object.put("searchContent", searchContent);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(searchOrder, object.toString(), mCallback);
    }

    //详情的数据
    public void productInfoData(String id, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("id", id);
            object.put("account", HttpBase.account);
            object.put("shopId", HttpBase.shopId);
            object.put("wmCode", HttpBase.wmCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(PRODUCT_DETAIL_REQUEST, object.toString(), mCallback);
    }

    //没登录的数据
    public static final String hotProduct = "/appHqProduct/hotProduct.do";

    //没登录的数据
    public void hotProduct(String skuName, String page, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("skuName", skuName);
            object.put("pageSize", "1000");

            object.put("page", page);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttpNoCheck(hotProduct, object.toString(), mCallback);
    }

    //没登录详情的数据
    public static final String productDetail_noLogin = "/appHqProduct/productDetail.do";

    //没登录详情的数据
    public void productDetail_noLogin(String productid, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("sku_code", "");
            object.put("productid", productid);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttpNoCheck(productDetail_noLogin, object.toString(), mCallback);
    }

    public static final String appVerification = "/appRegister/appVerification.do";

    public void appVerification(String phone, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {

            object.put("phone", phone);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttpNoCheck(appVerification, object.toString(), mCallback);
    }

    public static final String appCheckCode = "/appRegister/appCheckCode.do";

    public void appCheckCode(String phone, String phCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("phone", phone);
            object.put("phCode", phCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttpNoCheck(appCheckCode, object.toString(), mCallback);
    }

    public static final String appCheckPhone = "/appRegister/appCheckPhone.do";

    public void appCheckPhone(String phone, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("contactPhone", phone);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttpNoCheck(appCheckPhone, object.toString(), mCallback);
    }

    //订单列表
    public void orderList(int pageno, String state, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("shopId", HttpBase.shopId);
            object.put("account", HttpBase.account);
            object.put("pageNO", pageno);
            object.put("pageSize", 25);

            if (!TextUtils.isEmpty(state)) {
                object.put("handleStatus", state);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(ORDER_LIST_REQUEST, object.toString(), mCallback);
    }

    //次数统计
    public void purchaseStatistics(String time, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {

            object.put("shopId", HttpBase.shopId);
            object.put("month", time);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(purchaseStatistics, object.toString(), mCallback);
    }

    //次数统计
    public void cisuStatistics(String time, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {

            object.put("shopId", HttpBase.shopId);
            object.put("month", time);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(purchaseAnalysis, object.toString(), mCallback);
    }

    //费用总统计
    public void feiyongStatistics(String time, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {

            object.put("shopId", HttpBase.shopId);
            object.put("month", time);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(purchaseFeeAnalysis, object.toString(), mCallback);
    }

    //取物流单下的所有货品信息
    public void getGroupSkus2(String groupCode, String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {

            object.put("groupCode", groupCode); //运单号(订单发货批次号)
            object.put("orderCode", orderCode); //订单编号

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getGroupSkus2, object.toString(), mCallback);
    }
    //tplId	String	t_order_tpl表主键
    //    image	MultipartFile 	文件流
    //    createBy	String	上传人账号

    //
    //上传图片
    public void upErrorImg(File file, String tplId, StringCallback mCallback) {
//        OkHttpUtils
//                .postFile()
//                .url(HOST_STRING+upErrorImg)
//                .file(file)
//                .build()
//                .execute(mCallback);
        JSONObject object = new JSONObject();
        try {
            object.put("createBy", HttpBase.account); //帐号
            object.put("tplId", tplId); //t_order_tpl表主键
            object.put("deviceOS", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.post()
                .addFile("image", file.getName(), file)
                .url(HOST_STRING + upErrorImg)
                .addParams("content", object.toString())
                .addParams("version", "1")
                .addParams("login_token", HttpBase.token)
                .build()//
                .execute(mCallback);
    }

    //异常信息提交
    public void submitErrorInfo(String tplId, String errorStatus, String remark, String imgIds, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("tplId", tplId); //运单号(订单发货批次号)
            object.put("errorStatus", errorStatus); //态,用逗号分隔（1.货品解冻、2.货品错发、3.货品损坏、4.货品缺失、5.其他）
            object.put("remark", remark); //运单号(订单发货批次号)
            object.put("imgIds", imgIds); //运单号(订单发货批次号)
            object.put("createBy", HttpBase.account); //运单号(订单发货批次号)
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(submitErrorInfo, object.toString(), mCallback);
    }

    //异常信息删除（包括对应的图片）
    public void deleteErrorInfo(String tplId, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("tplId", tplId); //运单号(订单发货批次号)


        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(deleteErrorInfo, object.toString(), mCallback);
    }

    //确认收货接口
    public void confirmRevSku(RequestConFirm mRequestConFirm, StringCallback mCallback) {
        Gson gons = new Gson();
        String json = gons.toJson(mRequestConFirm);

        baseHttp(confirmRevSku, json, mCallback);
    }


    // 端查看异常信息
    public void getGroupSkusAndError(String groupCode, String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("groupCode", groupCode); //运单号(订单发货批次号)
            object.put("orderCode", orderCode); //订单编号
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getGroupSkusAndError, object.toString(), mCallback);
    }


    //提交留言的接口
    public void submitMessageBoard(String title, String contentVal, String mType, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("title", title); //运单号(订单发货批次号)
            object.put("contentVal", contentVal); //运单号(订单发货批次号)
            object.put("shopId", HttpBase.shopId); //运单号(订单发货批次号)
            object.put("companyId", HttpBase.companyId); //运单号(订单发货批次号)
            object.put("account", HttpBase.account); //运单号(订单发货批次号)
            object.put("mType", mType); //
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(submitMessageBoard, object.toString(), mCallback);
    }

    //查询留言的接口
    public void messageBoardListByShopId(String pageNO, String pageSize, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("pageNO", pageNO); //运单号(订单发货批次号)
            object.put("pageSize", pageSize); //运单号(订单发货批次号)
            object.put("shopId", HttpBase.shopId); //运单号(订单发货批次号)
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(messageBoardListByShopId, object.toString(), mCallback);
    }
    public static final String updateContactName = "/appAccount/updateContactName.do";//
    public static final String sendYzm = "/appAccount/sendYzm.do";//
    public static final String valYzm = "/appAccount/valYzm.do";//
    public static final String updateContactPhone = "/appAccount/updateContactPhone.do ";//
    //商品详情的数据
    public static final String PRODUCT_DETAIL_REQUEST = "/appProduct/productInfo.do";
    public static final String searchOrder = "/appOrder/searchOrder.do";//
    //订单的列表
    public static final String ORDER_LIST_REQUEST = "/appOrder/orderList.do?";
    // 统计接口
    public static final String purchaseStatistics = "/appShopPurchase/purchaseStatistics.do";

    // 次数
    public static final String purchaseAnalysis = "/appShopPurchase/purchaseAnalysis.do";
    // 费用
    public static final String purchaseFeeAnalysis = "/appShopPurchase/purchaseFeeAnalysis.do";

    //取物流单下的所有货品信息
    public static final String getGroupSkus2 = "/appOrder2/getGroupSkus2.do";
    //上传异常图片（单独提交，一次一个图片）
    public static final String upErrorImg = "/appOrder2/upErrorImg.do";

    //异常信息提交
    public static final String submitErrorInfo = "/appOrder2/submitErrorInfo.do";

    //删除异常信息
    public static final String deleteErrorInfo = "/appOrder2/deleteErrorInfo.do";
    //确认收货接口
    public static final String confirmRevSku = "/appOrder2/confirmRevSku.do";

    //查看异常信息
    public static final String getGroupSkusAndError = "/appOrder2/getGroupSkusAndError.do";

    //提交留言的 接口

    public static final String submitMessageBoard = "/appMessageBoard/submitMessageBoard.do";
    //查询留言
    public static final String messageBoardListByShopId = "/appMessageBoard/messageBoardListByShopId.do";

    //根据邀请码查询出企业信息接口
    public static final String getCompanyByCode = "/appRegister/getCompanyByCode.do";
    //省
    public static final String provinceList = "/appRegister/provinceList.do";
    //市
    public static final String cityList = "/appRegister/cityList.do";
    //保存注册信息
    public static final String saveRegister = "/appRegister/saveRegister.do";
    //检查账户名是否存在
    public static final String getIsAccount = "/appRegister/getIsAccount.do";

    //订单详情
    public static final String orderDetail = "/appOrder/orderDetail.do";
    //
    public static final String deleteErrorImg = "/appOrder2/deleteErrorImg.do";
    //
    public static final String getSalemanByName = "/appRegister/getSalemanByName.do";
    //照片凭证保存
    public static final String submitReceiptImg = "/appOrder2/submitReceiptImg.do";

    // 上传照片
    public static final String upreceiptimg = "/appOrder2/upReceiptImg.do";//

    public void upreceiptimg(File file, LogisticsItem mLogisticsItem, String image_type, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("createby", HttpBase.account); //帐号
            object.put("order_code", mLogisticsItem.orderCode); //t_order_tpl表主键
            object.put("group_code", mLogisticsItem.groupCode); //t_order_tpl表主键
            object.put("image_type", image_type); // 1:回执单图片2:货物图片
            object.put("deviceOS", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.post()
                .addFile("image", file.getName(), file)
                .url(HttpUtil.HOST_STRING + upreceiptimg)
                .addParams("content", object.toString())
                .addParams("version", "1")
                .addParams("login_token", HttpBase.token)

                .build()//
                .execute(mCallback);
    }

    //照片凭证保存
    public void submitReceiptImg(String order_code, String group_code, String imgIds, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("order_code", order_code); //运单号(订单发货批次号)
            object.put("group_code", group_code); //运单号(订单发货批次号)
            object.put("imgIds", imgIds); //运单号(订单发货批次号)
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(submitReceiptImg, object.toString(), mCallback);
    }


    //根据邀请码查询出企业信息接口
    public void getCompanyByCode(String code, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code); //运单号(订单发货批次号)

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttpNoCheck(getCompanyByCode, object.toString(), mCallback);
    }

    //企业所在地初始化接口
    public void provinceList(String company_id, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("company_id", company_id); //运单号(订单发货批次号)

        } catch (JSONException e) {
            e.printStackTrace();
        }

        baseHttpNoCheck(provinceList, object.toString(), mCallback);
    }

    //市
    public void cityList(String parentid, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("parentid", parentid); //运单号(订单发货批次号)

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttpNoCheck(cityList, object.toString(), mCallback);
    }


    //发送验证码接口
    public void sendCode(String phone, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("phone", phone); //运单号(订单发货批次号)

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(sendYzm, object.toString(), mCallback);
    }

    //保存信息
    public void saveRegister(RequestRegPam mRequestRegPam, StringCallback mCallback) {


        Gson gons = new Gson();
        String json = gons.toJson(mRequestRegPam);
        baseHttpNoCheck(saveRegister, json, mCallback);
    }

    //检查用户名
    public void getIsAccount(String accunt, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", accunt);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttpNoCheck(getIsAccount, object.toString(), mCallback);
    }


    //订单详情
    public void orderDetail(String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("orderCode", orderCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(orderDetail, object.toString(), mCallback);
    }

    //删除照片
    public void deleteErrorImg(String imgId, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("imgId", imgId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(deleteErrorImg, object.toString(), mCallback);
    }

    //查询业务员
    public void getSalemanByName(String saleManName, String company_id, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("saleManName", saleManName);
            object.put("company_id", company_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getSalemanByName, object.toString(), mCallback);
    }

    //采购联系人
    public static final String shopContactList = "/appAccount/shopContactList.do";

    //获取
    public void shopContactList(StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("shopId", HttpBase.shopId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(shopContactList, object.toString(), mCallback);
    }

    //APP新增第二联系人信息
    public static final String saveShopContact = "/appAccount/saveShopContact.do";

    public void saveShopContact(String contactName, String contactPhone, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("shopId", HttpBase.shopId);
            object.put("contactName", contactName);
            object.put("contactPhone", contactPhone);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(saveShopContact, object.toString(), mCallback);
    }

    //删除第二联系人信息
    public static final String deleteShopContact = "/appAccount/deleteShopContact.do";

    public void deleteShopContact(String contactId, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("flag", contactId);
            object.put("shopId", HttpBase.shopId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(deleteShopContact, object.toString(), mCallback);
    }

    //修改第二联系人信息
    public static final String updateShopContact = "/appAccount/updateShopContact.do";

    public void updateShopContact(String contactId, String contactName, String contactPhone, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("flag", contactId);
            object.put("contactName", contactName);
            object.put("contactPhone", contactPhone);
            object.put("shopId", HttpBase.shopId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(updateShopContact, object.toString(), mCallback);
    }

    //    //修改第1联系人信息
//    public static final String updateFirstShopContact = "/appAccount/updateFirstShopContact.do";
//    public  void  updateFirstShopContact( String  contactId, String  contactName,String  contactPhone,  StringCallback mCallback){
//        JSONObject object = new JSONObject();
//        try {
//            object.put("contactId", contactId);
//            object.put("contactName", contactName);
//            object.put("contactPhone", contactPhone);
//            object.put("shopId", HttpBase.shopId);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        baseHttp(updateFirstShopContact,  object.toString(),  mCallback);
//    }
    //修改第1联系人信息
    public static final String saveOpinion = "/appOpinionController/saveOpinion.do";

    public void saveOpinion(List<String> pathList, String contents, String contactType, String contact, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("companyId", HttpBase.companyId); //t_order_tpl表主键
            object.put("shopId", HttpBase.shopId); //t_order_tpl表主键
            object.put("contents", contents); //投诉建议的内容
            if (!TextUtils.isEmpty(contactType)) {
                object.put("contactType", contactType); //联系方式类型: 0-电话  1-QQ
                object.put("contact", contact); //联系电话或QQ
            }
            object.put("account", HttpBase.account); //t_order_tpl表主键
            object.put("deviceOS", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostFormBuilder mPostFormBuilder = OkHttpUtils.post();
        if (pathList != null && pathList.size() > 0) {
            for (int i = 0; i < pathList.size(); i++) {
                File pathFile = new File(pathList.get(i));
                mPostFormBuilder.addFile("image" + (i + 1), pathFile.getName(), pathFile);
            }


        } else {

        }
        mPostFormBuilder
                .url(HOST_STRING + saveOpinion)
                .addParams("content", object.toString())
                .addParams("version", "1")
                .addParams("login_token", HttpBase.token)
                .addParams("deviceOS", "android-" + android.os.Build.VERSION.RELEASE)
                .build()//
                .execute(mCallback);
    }


    //查询地址    appShippingAddress/appShopAddressList.do
    public static final String appShopAddressList = "/appShippingAddress/appShopAddressList.do";

    public void appShopAddressList(StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {

            object.put("shopId", HttpBase.shopId); //t_order_tpl表主键
            object.put("account", HttpBase.account); //运单号(订单发货批次号)

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(appShopAddressList, object.toString(), mCallback);
    }
    public static final String appCompanyWm = "/vpCompanyWm/appCompanyWm.do";

    public void appCompanyWm(StringCallback mCallback) {
//        JSONObject object = new JSONObject();
//
//        try {
//
//            object.put("shopId", HttpBase.shopId); //t_order_tpl表主键
//            object.put("account", HttpBase.account); //运单号(订单发货批次号)
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        baseHttp(appCompanyWm, "{}", mCallback);
    }

    public static final String getMaxValueCouponApp = "/appCounpon/getMaxValueCouponApp.do";

    public void getMaxValueCouponApp(String tClasses,String totalPrice,StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("tClasses", tClasses); //t_order_tpl表主键
            object.put("account", HttpBase.account); //运单号(订单发货批次号)
            object.put("totalPrice", totalPrice); //运单号(订单发货批次号)

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getMaxValueCouponApp, object.toString(), mCallback);
    }
    public static final String checkPaymentMethod = "/appVpOrder/checkPaymentMethod.do";

    public void checkPaymentMethod(StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("shopId", HttpBase.shopId); //t_order_tpl表主键


        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(checkPaymentMethod, object.toString(), mCallback);
    }
//    shopId:门店ID（必填）
//    contactName:联系人（必填）
//    contactPhone:联系人电话（必填）
//    contactName2:其他联系人
//    contactPhone2:其他联系人电话
//    contactName3:其他联系人
//    contactPhone2:其他联系人电话
//    contactProvince:省份（必填）
//    contactCity:城市
//    contactAddress:详细地址（必填）

    //保存地址   /appShippingAddress/saveShopAddress.do
    public static final String saveShopAddress = "/appShippingAddress/saveShopAddress.do";

    public void saveShopAddress(String contactName, String contactPhone, String contactName2
            , String contactPhone2, String contactName3, String contactPhone3
            , String contactProvince, String contactCity, String contactAddress, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {

            object.put("shopId", HttpBase.shopId);
            object.put("contactName", contactName);
            object.put("contactPhone", contactPhone);
            object.put("contactName2", contactName2);
            object.put("contactPhone2", contactPhone2);
            object.put("contactName3", contactName3);
            object.put("contactPhone3", contactPhone3);
            object.put("contactProvince", contactProvince);
            object.put("contactCity", contactCity);
            object.put("contactAddress", contactAddress); //运单号(订单发货批次号)

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(saveShopAddress, object.toString(), mCallback);
    }

    //省市
    public static final String initCity = "/appShippingAddress/initCity.do";

    public void initCity(StringCallback mCallback) {

        baseHttp(initCity, "{}", mCallback);
    }

    //删除
    public static final String deleteShopAddress = "/appShippingAddress/deleteShopAddress.do";

    public void deleteShopAddress(String shopAddressId, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("shopAddressId", shopAddressId); //t_order_tpl表主键


        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(deleteShopAddress, object.toString(), mCallback);
    }

    //        shopAddress{
//             id:地址ID(必填)
//            shopId:门店ID
//            contactName:联系人
//            contactPhone:联系人电话
//            contactName2:其他联系人
//            contactPhone2:其他联系人电话
//            contactName3:其他联系人
//            contactPhone2:其他联系人电话
//            contactProvince:省份
//            contactCity:城市
//            contactAddress:详细地址
//        }
    //修改地址
    public static final String updateShopAddress = "/appShippingAddress/updateShopAddress.do";

    public void updateShopAddress(String id, String shopId, String contactName
            , String contactPhone, String contactName2, String contactPhone2
            , String contactName3, String contactPhone3, String contactProvince, String contactCity
            , String contactAddress, StringCallback mCallback) {

        JSONObject objectChild = new JSONObject();
        try {
            objectChild.put("id", id); //t_order_tpl表主键
            objectChild.put("shopId", shopId);
            objectChild.put("contactName", contactName);
            objectChild.put("contactPhone", contactPhone);
            objectChild.put("contactName2", contactName2);
            objectChild.put("contactPhone2", contactPhone2);
            objectChild.put("contactName3", contactName3);
            objectChild.put("contactPhone3", contactPhone3);

            if (!TextUtils.isEmpty(contactProvince)) {
                objectChild.put("contactProvince", contactProvince);
            }
            if (!TextUtils.isEmpty(contactCity)) {
                objectChild.put("contactCity", contactCity);
            }

            objectChild.put("contactAddress", contactAddress);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject object = new JSONObject();
        try {
            object.put("shopAddress", objectChild.toString());
            object.put("flag", "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        baseHttp(updateShopAddress, object.toString(), mCallback);
    }

    //修改地址
//    shop{
//        id:门店ID（必填）
//        contactName:联系人
//        contactPhone:联系电话
//        province:省
//        city:市
//        address:详细地址
//        contactName2:其他联系人
//        contactPhone2:其他联系电话
//        contactName3:其他联系人
//        contactPhone3:其他联系电话
//    }
    public void updateShopAddressDefault(String id, String contactName
            , String contactPhone, String contactName2, String contactPhone2
            , String contactName3, String contactPhone3, String contactProvince, String contactCity
            , String contactAddress, StringCallback mCallback) {

        JSONObject objectChild = new JSONObject();
        try {
            objectChild.put("id", id); //t_order_tpl表主键
            objectChild.put("contactName", contactName);
            objectChild.put("contactPhone", contactPhone);
            objectChild.put("contactName2", contactName2);
            objectChild.put("contactPhone2", contactPhone2);
            objectChild.put("contactName3", contactName3);
            objectChild.put("contactPhone3", contactPhone3);
            if (!TextUtils.isEmpty(contactProvince)) {

                objectChild.put("province", contactProvince);
            }
            if (!TextUtils.isEmpty(contactCity)) {
                objectChild.put("city", contactCity);
            }

            objectChild.put("address", contactAddress);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject object = new JSONObject();
        try {
            object.put("shop", objectChild.toString());
            object.put("flag", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        baseHttp(updateShopAddress, object.toString(), mCallback);
    }


    //修改密码
    public static final String updatePassWard = "/appAccount/updatePassWard.do";

    public void updatePassWard(String oldPwd, String newPwd, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {

            object.put("oldPwd", oldPwd); //t_order_tpl表主键
            object.put("newPwd", newPwd); //t_order_tpl表主键
            object.put("account", HttpBase.account); //运单号(订单发货批次号)

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(updatePassWard, object.toString(), mCallback);
    }


    //合并订单
    public static final String mergeOrder = "/appOrder/mergeOrder.do";

    public void mergeOrder(String shopIdOrderCodeList, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {

            object.put("shopIdOrderCodeList", shopIdOrderCodeList); //t_order_tpl表主键
            object.put("account", HttpBase.account); //t_order_tpl表主键

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(mergeOrder, object.toString(), mCallback);
    }

    //*********************************************************************************************************************************

    //订单别表
    public static final String orderList = "/appOrder3/orderList.do";

    public void orderList(String handleStatus, int pageNo, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
//                    shopId: 门店编号,
//                    account: 下单账户,
//                    pageNo:页码可不传默认是1,
//                    handleStatus:可为空 1待配送2配送中3已收货4已取消,
//                    pageSize :页面记录数，可不传默认为10

            object.put("shopId", HttpBase.shopId);
            object.put("account", HttpBase.account);
            object.put("pageNo", pageNo);
            if (!TextUtils.isEmpty(handleStatus))
                object.put("handleStatus", handleStatus);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(orderList, object.toString(), mCallback);
    }

    //订单搜索
    public static final String searchOrderappOrder3 = "/appOrder3/searchOrder.do";

    public void searchOrderappOrder3(String searchContent, String handleStatus, int pageNo, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
//                    shopId: 门店编号,
//                    account: 下单账户,
//                    pageNo:页码可不传默认是1,
//                    handleStatus:可为空 1待配送2配送中3已收货4已取消,
//                    pageSize :页面记录数，可不传默认为10

            object.put("shopId", HttpBase.shopId);
            object.put("account", HttpBase.account);
            object.put("pageNo", pageNo);
            object.put("searchContent", searchContent);
            if (!TextUtils.isEmpty(handleStatus))
                object.put("handleStatus", handleStatus);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(searchOrderappOrder3, object.toString(), mCallback);
    }

    // 多个上传回单运单列表的接口
    public static final String getUpRecpGroupList = "/appOrder3/getUpRecpGroupList.do";

    public void getUpRecpGroupList(String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {

            object.put("orderCode", orderCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getUpRecpGroupList, object.toString(), mCallback);
    }


    public static final String upReceiptByGroupCode = "/appOrder3/upReceiptByGroupCode.do";

    // 上传回执单
    // 图片
    public void upReceiptByGroupCode(File huiDanfile, List<File> fileList, String order_code, String group_code, StringCallback mCallback) {

        JSONObject object = new JSONObject();
        try {

            object.put("order_code", order_code);
            object.put("group_code", group_code);
            object.put("createby", HttpBase.account); //帐号
            object.put("deviceOS", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PostFormBuilder mPostFormBuilder = OkHttpUtils.post();
        mPostFormBuilder.addFile("recImage", huiDanfile.getName(), huiDanfile);


        if (fileList != null && fileList.size() > 0) {
            for (File mFile : fileList) {
                mPostFormBuilder.addFile("skuImages", mFile.getName(), mFile);
            }

        }
        mPostFormBuilder.url(HOST_STRING + upReceiptByGroupCode)
                .addParams("content", object.toString())
                .addParams("version", "1")
                .addParams("login_token", HttpBase.token)
                .build()//
                .execute(mCallback);
    }


    //查看回单

    public static final String viewReceiptImages = "/appOrder3/viewReceiptImages.do";

    public void viewReceiptImages(String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("orderCode", orderCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(viewReceiptImages, object.toString(), mCallback);
    }
    //查看回单

    public static final String viewAndUpRecList = "/appOrder3/viewAndUpRecList.do";

    public void viewAndUpRecList(String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("orderCode", orderCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(viewAndUpRecList, object.toString(), mCallback);
    }


    //多个异常运单列表接口
    public static final String getUpErrGroupList = "/appOrder3/getUpErrGroupList.do";

    public void getUpErrGroupList(String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("orderCode", orderCode);
            object.put("optType", "up");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getUpErrGroupList, object.toString(), mCallback);
    }

    //上传/查看异常商品列表接口
    public static final String toGroupErrorPage = "/appOrder3/toGroupErrorPage.do";

    public void toGroupErrorPage(String orderCode, String group_code, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("order_code", orderCode);
            object.put("group_code", group_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(toGroupErrorPage, object.toString(), mCallback);
    }

    // 上传单个异常商品接口 图片（新增加，数据见下图）
    public static final String upErrorImages = "/appOrder3/upErrorImages.do";

    public void upErrorImages(List<File> fileList, String order_code, String group_code, String tpl_id, String error_status, String remark, StringCallback mCallback) {

        JSONObject object = new JSONObject();
        try {
            object.put("order_code", order_code);
            object.put("group_code", group_code);
            object.put("tpl_id", tpl_id);
            object.put("error_status", error_status);  //异常类型 （用逗号分隔） 1 .货品解冻、2.货品错发、3.货品损坏、4.货品缺失、5.其他
            object.put("remark", remark);

            object.put("createby", HttpBase.account); //帐号
            object.put("deviceOS", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PostFormBuilder mPostFormBuilder = OkHttpUtils.post();
//        mPostFormBuilder  .addFile("recImage", huiDanfile.getName(), huiDanfile);

        if (fileList != null && fileList.size() > 0) {
            for (File mFile : fileList) {
                mPostFormBuilder.addFile("errImages", mFile.getName(), mFile);
            }

        }
        mPostFormBuilder.url(HOST_STRING + upErrorImages)
                .addParams("content", object.toString())
                .addParams("version", "1")
                .addParams("login_token", HttpBase.token)
                .addParams("deviceOS", "android-" + android.os.Build.VERSION.RELEASE)
                .build()//
                .execute(mCallback);
    }


    //多个确认收货运单
    public static final String getConfirmGroupList = "/appOrder3/getConfirmGroupList.do";

    public void getConfirmGroupList(String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("orderCode", orderCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getConfirmGroupList, object.toString(), mCallback);
    }

    //取物流单下的所有货品信息
    public static final String toConfirmGroup = "/appOrder3/toConfirmGroup.do";

    public void toConfirmGroup(String groupCode, String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {

            object.put("orderCode", orderCode); //订单编号
            object.put("groupCode", groupCode); //运单号(订单发货批次号)

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(toConfirmGroup, object.toString(), mCallback);
    }

    //确认收货
    public static final String confirmGroup = "/appOrder3/confirmGroup.do";

    public void confirmGroup(ReqConFirm mReqConFirm, StringCallback mCallback) {
        Gson gons = new Gson();
        String json = gons.toJson(mReqConFirm);
        baseHttp(confirmGroup, json, mCallback);
    }


    // 物流多个运单
    public static final String getLogisGroupList = "/appOrder3/getLogisGroupList.do";

    public void getLogisGroupList(String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("orderCode", orderCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getLogisGroupList, object.toString(), mCallback);
    }

    // 物流详情
    public static final String viewLogis = "/appOrder3/viewLogis.do";

    public void viewLogis(String orderCode, String groupCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("orderCode", orderCode);
            object.put("groupCode", groupCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(viewLogis, object.toString(), mCallback);
    }

    //多个异常查看运单
    public void getUpErrGroupListView(String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("orderCode", orderCode);
            object.put("optType", "view");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getUpErrGroupList, object.toString(), mCallback);
    }

    // 查看回复的异常
    public void toGroupErrorPageView(String orderCode, String group_code, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("order_code", orderCode);
            object.put("group_code", group_code);
            object.put("optType", "view");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(toGroupErrorPage, object.toString(), mCallback);
    }

    //订单的详情接口
    public static final String toOrderDetail = "/appOrder3/toOrderDetail.do";

    public void toOrderDetail(String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("orderCode", orderCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(toOrderDetail, object.toString(), mCallback);
    }

    //微信支付
    public static final String scanCodePayMent = "/wxPay/scanCodePayMent.do";

    public void scanCodePayMent(String orderCode, StringCallback mCallback) {
//        JSONObject object = new JSONObject();
//
//        try {
//            object.put("out_trade_no", orderCode);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        OkHttpUtils
                .post()
                .url(HOST_STRING + scanCodePayMent)
                .addParams("content", "")
                .addParams("out_trade_no", orderCode)
                .addParams("version", "1")
                .addParams("deviceOS", "android-" + android.os.Build.VERSION.RELEASE)
                .addParams("login_token", HttpBase.token)
                .build()
                .execute(mCallback);
    }

    //支付宝支付
    public static final String aplAlipay = "/aliPay/aplAlipay.do";

    public void aplAlipay(String orderCode, StringCallback mCallback) {
//        JSONObject object = new JSONObject();
//
//        try {
//            object.put("out_trade_no", orderCode);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        OkHttpUtils
                .post()
                .url(HOST_STRING + aplAlipay)
                .addParams("content", "")
                .addParams("out_trade_no", orderCode)
                .addParams("version", "1")
                .addParams("deviceOS", "android-" + android.os.Build.VERSION.RELEASE)
                .addParams("login_token", HttpBase.token)
                .build()
                .execute(mCallback);
    }

    //历史订单的详情接口
    public static final String toArcOrderDetail = "/appOrder3/toArcOrderDetail.do";

    public void toArcOrderDetail(String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("orderCode", orderCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(toArcOrderDetail, object.toString(), mCallback);
    }

    //订单详情，运单查看/上传异常-商品页面（
    public static final String toViewUpErrGroupPage = "/appOrder3/toViewUpErrGroupPage.do";

    public void toViewUpErrGroupPage(String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("orderCode", orderCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(toViewUpErrGroupPage, object.toString(), mCallback);
    }

    //订单详情，物流页面（
    public static final String toViewLogisGroupPage = "/appOrder3/toViewLogisGroupPage.do";

    public void toViewLogisGroupPage(String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("orderCode", orderCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(toViewLogisGroupPage, object.toString(), mCallback);
    }

    //加载首页的数据
    public static final String getClassProductList = "/appProduct/getClassProductList.do";

    //    public static final String  getVpClassProductList= "/appVpProduct/classProductList.do";
    public void getClassProductList(StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("owner", HttpBase.owner);
            object.put("shopId", HttpBase.shopId);
            object.put("wmCode", HttpBase.wmCode);
            object.put("companyId", HttpBase.companyId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if ("1".equals(HttpBase.isSelf)) {
            baseHttp(getVpClassProductList, object.toString(), mCallback);
        } else {
            baseHttp(getClassProductList, object.toString(), mCallback);
        }

    }

    // 查看历史订单
    public static final String viewArcOrder = "/appOrder/viewArcOrder.do";

    public void viewArcOrder(String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("orderCode", orderCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(viewArcOrder, object.toString(), mCallback);
    }


    //vp的接口
    //业务远 注册
    public static final String saveChildRegister = "/appHqProduct/saveChildRegister.do";

    public void saveChildRegister(String jsonParamsStr, StringCallback mCallback) {
        baseHttp(saveChildRegister, jsonParamsStr, mCallback);
    }

    //搜添加客户
    public static final String appBoundShopList = "/appAreaCustomer/appBoundShopList.do";

    public void appBoundShopList(String shopName, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {  //"cid": "838","shopName":"张磊","account":"沈馨"}
            object.put("cid", HttpBase.companyId_2);
            object.put("shopName", shopName);
            object.put("account", HttpBase.account_2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(appBoundShopList, object.toString(), mCallback);
    }

    //搜添加客户
    public static final String appBoundShop = "/appAreaCustomer/appBoundShop.do";

    public void appBoundShop(String shopId, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {  //"cid": "838","shopName":"张磊","account":"沈馨"}

            object.put("shopId", shopId);
            object.put("account", HttpBase.account_2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(appBoundShop, object.toString(), mCallback);
    }


    public static final String initAddShop = "/appShopManage/initAddShop.do";

    public void initAddShop( StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {  //"cid": "838","shopName":"张磊","account":"沈馨"}
            object.put("companyId", HttpBase.companyId_2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(initAddShop, object.toString(), mCallback);
    }


    public static final String initUpdateShop = "/appShopManage/initUpdateShop.do";

    public void initUpdateShop( String shopId,StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {  //"cid": "838","shopName":"张磊","account":"沈馨"}
            object.put("companyId", HttpBase.companyId_2);
            object.put("shopId", shopId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(initUpdateShop, object.toString(), mCallback);
    }

    public static final String selectInitInfoByCompanyId = "/appShopManage/selectInitInfoByCompanyId.do";

    public void selectInitInfoByCompanyId( String companyId,StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {  //"cid": "838","shopName":"张磊","account":"沈馨"}
            object.put("companyId", companyId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(selectInitInfoByCompanyId, object.toString(), mCallback);
    }



    public static final String selectCityOrDistrictList = "/appShopManage/selectCityOrDistrictList.do";

    public void selectCityOrDistrictList(String province, String city,StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {  //"cid": "838","shopName":"张磊","account":"沈馨"}
            object.put("province", province);
            object.put("city", city);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(selectCityOrDistrictList, object.toString(), mCallback);
    }










    //添加客户的接口
    public static final String addShop = "/appHqProduct/addShop.do";

    public void addShop(String JsonParmsStr, List<File> fileList, StringCallback mCallback) {
        JSONObject json = null;
        try {
            json = new JSONObject(JsonParmsStr);
            json.put("deviceOS", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostFormBuilder mPostFormBuilder = OkHttpUtils.post();
//        mPostFormBuilder  .addFile("recImage", huiDanfile.getName(), huiDanfile);

        if (fileList != null && fileList.size() > 0) {
            for (File mFile : fileList) {
                mPostFormBuilder.addFile("skuImages", mFile.getName(), mFile);
            }

        }
        mPostFormBuilder.url(HOST_STRING + addShop)
                .addHeader("Content-Type", "text/html; charset=utf-8")
                .addParams("content", json != null ? json.toString() : JsonParmsStr)
                .addParams("version", "1")
                .addParams("login_token", HttpBase.token_2)
                .addParams("deviceOS", "android-" + android.os.Build.VERSION.RELEASE)
                .build()//
                .execute(mCallback);


    }


    //添加客户的接口
    public static final String vpHeadaddShop = "/appShopManage/addShop.do";

    public void vpHeadaddShop(String JsonParmsStr, List<File> fileList, StringCallback mCallback) {
        JSONObject json = null;
        try {
            json = new JSONObject(JsonParmsStr);
            json.put("deviceOS", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PostFormBuilder mPostFormBuilder = OkHttpUtils.post();
//        mPostFormBuilder  .addFile("recImage", huiDanfile.getName(), huiDanfile);

        if (fileList != null && fileList.size() > 0) {
            for (File mFile : fileList) {
                mPostFormBuilder.addFile("imgUrl", mFile.getName(), mFile);
            }

        }
        mPostFormBuilder.url(HOST_STRING + vpHeadaddShop)
                .addHeader("Content-Type", "text/html; charset=utf-8")
                .addParams("content", json != null ? json.toString() : JsonParmsStr)
                .addParams("version", "1")
                .addParams("login_token", HttpBase.token_2)
                .addParams("deviceOS", "android-" + android.os.Build.VERSION.RELEASE)
                .build()//
                .execute(mCallback);


    }
    //修改客户的接口
    public static final String updateShop = "/appShopManage/updateShop.do";

    public void updateShop(String JsonParmsStr, List<File> fileList, StringCallback mCallback) {
        JSONObject json = null;
        try {
            json = new JSONObject(JsonParmsStr);
            json.put("deviceOS", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PostFormBuilder mPostFormBuilder = OkHttpUtils.post();
//        mPostFormBuilder  .addFile("recImage", huiDanfile.getName(), huiDanfile);

//        if (fileList != null && fileList.size() > 0) {
//            for (File mFile : fileList) {
//                mPostFormBuilder.addFile("imgUrl", mFile.getName(), mFile);
//            }
//
//        }
        mPostFormBuilder.url(HOST_STRING + updateShop)
//               .addHeader("Content-Type", "multipart/form-data;")

//                .addHeader("Accept-Endoding","default")

                .addParams("content", json != null ? json.toString() : JsonParmsStr)
                .addParams("version", "1")
                .addParams("login_token", HttpBase.token_2)
                .addParams("deviceOS", "android-" + android.os.Build.VERSION.RELEASE)
              .addHeader("Content-Type", "''")
              .addHeader("Content-type", "''")
               .addParams("Content-Type", "''")

                .build()//

                .execute(mCallback);


    }








    public static final String registSupplier = "/appSupplierInfo/registSupplier.do";

    public void regGys(String JsonParmsStr, List<File> fileList, StringCallback mCallback) {

        PostFormBuilder mPostFormBuilder = OkHttpUtils.post();
//        mPostFormBuilder  .addFile("recImage", huiDanfile.getName(), huiDanfile);
        JSONObject json = null;
        try {
            json = new JSONObject(JsonParmsStr);
            json.put("deviceOS", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        json.put("deviceOS","android");
        if (fileList != null && fileList.size() > 0) {
            for (File mFile : fileList) {
                mPostFormBuilder.addFile("logoUrl", mFile.getName(), mFile);
            }

        }
        mPostFormBuilder.url(HOST_STRING + registSupplier)
                .addHeader("Content-Type", "text/html; charset=utf-8")
                .addParams("content", json != null ? json.toString() : JsonParmsStr)
                .addParams("version", "1")
                .addParams("login_token", HttpBase.token_2)
                .addParams("deviceOS", "android-" + android.os.Build.VERSION.RELEASE)
                .build()//
                .execute(mCallback);


    }

    //检查登录
    public static final String getIsCustomAccount = "/appHqProduct/getIsAccount.do";

    public void getIsCustomAccount(String accountLogin, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", accountLogin);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(getIsCustomAccount, object.toString(), mCallback);
    }

    //检查登录
    public static final String weChatAppPayment = "/wxPay/weChatAppPayment.do";

    public void weChatAppPayment(String orderid, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("out_trade_no", orderid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(weChatAppPayment, object.toString(), mCallback);
    }

    //仓库
    public static final String shopWm = "/appHqProduct/shopWm.do";

    public void shopWm(String companyid, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("companyid", companyid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(shopWm, object.toString(), mCallback);
    }

    //--------------------vp的接口-------------------------------
    public static final String hqSearchProduct = "/appHqProduct/hqSearchProduct.do";

    public void hqSearchProduct(String classId, String searchText, String page, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("page", page);
            object.put("owner", HttpBase.owner_2);
            if (!TextUtils.isEmpty(classId)) {
                object.put("classId", classId);
            }
            if (!TextUtils.isEmpty(searchText)) {
                object.put("searchText", searchText);
            }

//         object.put("pageSize", companyid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(hqSearchProduct, object.toString(), mCallback);
    }

    public static final String selectClass = "/appHqProduct/selectClass.do";

    public void selectClass(StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("companyId", HttpBase.companyId_2);

//         object.put("pageSize", companyid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(selectClass, object.toString(), mCallback);
    }

    public static final String vpcompanyList = "/appVpShop/companyList.do";

    public void vpcompanyList(StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("companyName", "");
            object.put("account", HttpBase.account_2);

//         object.put("pageSize", companyid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(vpcompanyList, object.toString(), mCallback);
    }

    //门店的列表
    public static final String companyList = "/appVpShop/companyList.do";

    public void companyList(StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account_2);
            object.put("companyId", HttpBase.companyId_2);
//         object.put("pageSize", companyid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(companyList, object.toString(), mCallback);
    }

    //商品详情
    public static final String productDetail = "/appHqProduct/productDetail.do";

    public void productDetail(String productid, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("productid", productid);
//         object.put("pageSize", companyid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(productDetail, object.toString(), mCallback);
    }

    //订单的列表
    public static final String Vplist = "/appVpOrder/list.do";

    public void Vplist(String order_code, String handle_status, int page, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("companyId", HttpBase.companyId_2);

            object.put("page", page + "");
            object.put("pageSize", "10");
//            if(!TextUtils.isEmpty(order_code)){
            if (order_code == null)
                order_code = "";
            object.put("order_code", order_code);
//            }
//            if(!TextUtils.isEmpty(handle_status)){
            if (handle_status == null)
                handle_status = "";
            object.put("handle_status", handle_status);
//            }

//         object.put("pageSize", companyid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(Vplist, object.toString(), mCallback);
    }

    //订单的详情接口
    public static final String vPorderDetail = "/appVpOrder/orderDetail.do";

    public void vPorderDetail(String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("order_code", orderCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(vPorderDetail, object.toString(), mCallback);
    }

    //供应商
    public static final String querySupplierInfo = "/appSupplierInfo/querySupplierInfo.do";

    public void querySupplierInfo(String serachText, String isEnable, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("companyId", HttpBase.companyId_2);
            object.put("sKey", serachText);
            object.put("isEnable", isEnable);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(querySupplierInfo, object.toString(), mCallback);
    }

    //启用和停用
    public static final String appEditStatus = "/appSupplierInfo/appEditStatus.do";

    public void appEditStatus(String id, String isEnable, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("companyId", HttpBase.companyId_2);
            object.put("id", id);
            object.put("isEnable", isEnable);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(appEditStatus, object.toString(), mCallback);
    }

    //    --------------------业务员的API接口-------------------------------
    public static final String provinceListNew = "/appRegister/provinceLists.do";

    //企业所在地初始化接口
    public void provinceListNew(String company_id, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("company_id", company_id); //运单号(订单发货批次号)

        } catch (JSONException e) {
            e.printStackTrace();
        }

        baseHttpNoCheck(provinceListNew, object.toString(), mCallback);
    }

    public static final String getShopInfo = "/appAccount/getShopInfo.do";

    //企业所在地初始化接口
    public void getShopInfo(StringCallback mCallback) {
        baseHttpNoCheck(getShopInfo, "{}", mCallback);
    }

    public static final String getCompanyInfo = "/company/getCompanyInfo.do";

    //企业所在地初始化接口
    public void getCompanyInfo(StringCallback mCallback) {
        baseHttpNoCheck(getCompanyInfo, "{}", mCallback);
    }


    //根据编码获取地址的
    public void gpsgetAddress(String latitude, String longitude, StringCallback mCallback) {
        String url = "http://api.map.baidu.com/geocoder/v2/";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("output", "json")
                .addParams("location", latitude + "," + longitude)
                .addParams("ak", "bjvgAD66hGbOUtGZgFqDa2iK6MFeUkGZ")
                .build()
                .execute(mCallback);


    }


    //业务员首页的接口
    public static final String myHomePage = "/appAreaOrder/myHomePage.do";

    public void myHomePage(StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account_2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(myHomePage, object.toString(), mCallback);
    }
    //总部
    public static final String manageHome = "/appOrderManage/manageHome.do";

    public void manageHome(StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("companyId", HttpBase.companyId_2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(manageHome, object.toString(), mCallback);
    }


    //客户id
    public static final String initShopPageMsg = "/appShopManage/initShopPageMsg.do";
    public void initShopPageMsg(StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("companyId", HttpBase.companyId_2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(initShopPageMsg, object.toString(), mCallback);
    }
    //客户ids
    public static final String selectShopList = "/appShopManage/selectShopList.do";
    public void selectShopList(int page,  String companyIds,   String shopName,StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("companyId", companyIds);
            object.put("shopName", shopName);
            object.put("pageSize", "10");
            object.put("pageNo", page+"");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(selectShopList, object.toString(), mCallback);
    }















    //客户列表的接口
    public static final String shopList = "/appAreaCustomer/shopList.do";
    public void shopList(StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account_2);
            object.put("pageNo", 1);
            object.put("pageSize", 1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(shopList, object.toString(), mCallback);
    }

    //授权门店登录
    public static final String appauthShop = "/appAreaCustomer/appauthShop.do";

    public void appauthShop(String shopId, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account_2);
            object.put("cid", TextUtils.isEmpty(PushManager.getInstance().getClientid(MyApplication.instance)) ? "1" : PushManager.getInstance().getClientid(MyApplication.instance));
            object.put("shopId", shopId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(appauthShop, object.toString(), mCallback);
    }


    //授权门店登录
    public static final String updateShopChangeStatus = "/appShopManage/updateShopChangeStatus.do";

    public void updateShopChangeStatus(String shopId,String isEnable, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account_2);
            object.put("isEnable", isEnable);
            object.put("shopId", shopId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(updateShopChangeStatus, object.toString(), mCallback);
    }

    //订单的详情接口
    public static final String appAreatoOrderDetail = "/appAreaOrder/toOrderDetail.do";

    public void appAreatoOrderDetail(String orderType, String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("orderCode", orderCode);
            if ("2".equals(orderType)) {
                object.put("order_type", orderType);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(appAreatoOrderDetail, object.toString(), mCallback);
    }

    //订单的详情接口
    public static final String appAreatoOrderDetail_sj = "/appAreaOrder/toOrderDetail.do";

    public void appAreatoOrderDetail_sj(String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("orderCode", orderCode);
            object.put("order_type", "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(appAreatoOrderDetail_sj, object.toString(), mCallback);
    }

    //订单详情，物流页面（
    public static final String appAreatoViewLogisGroupPage = "/appAreaOrder/toViewLogisGroupPage.do";

    public void appAreatoViewLogisGroupPage(String orderType, String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("orderCode", orderCode);
            if ("2".equals(orderType)) {
                object.put("order_type", orderType);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(appAreatoViewLogisGroupPage, object.toString(), mCallback);
    }



    //订单详情，物流页面（
    public static final String VpViewLogisGroupPage = "/appVpOrder/VpViewLogisGroupPage.do";

    public void VpViewLogisGroupPage( String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("orderCode", orderCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(VpViewLogisGroupPage, object.toString(), mCallback);
    }


    //查看回单

    public static final String appAreaviewReceiptImages = "/appAreaOrder/viewReceiptImages.do";

    public void appAreaviewReceiptImages(String orderType, String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("orderCode", orderCode);
            if ("2".equals(orderType)) {
                object.put("order_type", orderType);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(appAreaviewReceiptImages, object.toString(), mCallback);
    }

    //订单详情，运单查看/上传异常-商品页面（
    public static final String appAreatotoViewUpErrGroupPage = "/appAreaOrder/toViewUpErrGroupPage.do";

    public void appAreatotoViewUpErrGroupPage(String orderType, String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("orderCode", orderCode);
            if ("2".equals(orderType)) {
                object.put("order_type", orderType);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(appAreatotoViewUpErrGroupPage, object.toString(), mCallback);
    }

    //点击审核订单商品
    public static final String goToCheckOrder = "/appAreaOrder/goToCheckOrder.do";

    public void goToCheckOrder(String orderCode, String owner, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account_2);
            object.put("orderCode", orderCode);
            object.put("owner", owner);
            object.put("checkMan", "sale");//    checkMan:是谁审核（主管审核传参为:sale,财务审核传参为：finance）
            //	pageNo:页码可不传默认是1,
            //     pageSize :页面记录数，可不传默认为10
            object.put("pageNo", "1");
            object.put("pageSize", 1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(goToCheckOrder, object.toString(), mCallback);
    }

    //点击审核通过 ，拒绝
    public static final String checkOrder = "/appAreaOrder/checkOrder.do";

    public void checkOrder(RequestParameterAudit mRequestParameterAudit, StringCallback mCallback) {
        Gson gson = new Gson();
        String parameterStr = gson.toJson(mRequestParameterAudit);
        baseHttp_2(checkOrder, parameterStr, mCallback);
    }

    //处理
    public static final String updateSkuErrorHandle = "/appAreaOrder/replayException.do";

    public void updateSkuErrorHandle(RequestParamDealWith mRequestParamDealWith, StringCallback mCallback) {
        Gson gson = new Gson();
        String parameterStr = gson.toJson(mRequestParamDealWith);
        baseHttp_2(updateSkuErrorHandle, parameterStr, mCallback);
    }

    //待处理
    public static final String toViewErrGroupByShop = "/appAreaOrder/toViewErrGroupByShop.do";

    public void toViewErrGroupByShopDai(StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account_2);
            object.put("status", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(toViewErrGroupByShop, object.toString(), mCallback);
    }

    //已处理
    public void toViewErrGroupByShopYi(StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("account", HttpBase.account_2);
            object.put("status", "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        baseHttp_2(toViewErrGroupByShop, object.toString(), mCallback);
    }

    //订单别表
    public static final String orderListAppArea = "/appAreaOrder/orderList.do";
    public static final String orderListAppAreaVp = "/appVpOrder/opeOrderList.do";

    public void orderListAppArea(String shopName, String handleStatus, int pageNo, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
//                    shopId: 门店编号,
//                    account: 下单账户,
//                    pageNo:页码可不传默认是1,
//                    handleStatus:可为空 1待配送2配送中3已收货4已取消,
//                    pageSize :页面记录数，可不传默认为10
//         object.put("shopId", HttpBase.shopId);
            if (!TextUtils.isEmpty(shopName)) {

                object.put("shopName", shopName);
            }
            object.put("account", HttpBase.account_2);
            object.put("pageNo", pageNo);
            if (!TextUtils.isEmpty(handleStatus))
                object.put("handleStatus", handleStatus);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if ("1".equals(HttpBase.isSelf_2)) {
            baseHttp_2(orderListAppAreaVp, object.toString(), mCallback);
        } else {
            baseHttp_2(orderListAppArea, object.toString(), mCallback);
        }


    }


    public static final String appVpOrderList = "/appVpOrder/appVpOrderList.do";

    public void appVpOrderList(  String companyId,    String shopName, String handleStatus, int pageNo, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
//                    shopId: 门店编号,
//                    account: 下单账户,
//                    pageNo:页码可不传默认是1,
//                    handleStatus:可为空 1待配送2配送中3已收货4已取消,
//                    pageSize :页面记录数，可不传默认为10
//         object.put("shopId", HttpBase.shopId);
            if (!TextUtils.isEmpty(shopName)) {

                object.put("shopName", shopName);
            }
            object.put("companyId", companyId);//HttpBase.companyId_2
            object.put("orderCode", "");
            object.put("pageSize", "10");
            object.put("page", pageNo);
            if (!TextUtils.isEmpty(handleStatus))
                object.put("handleStatus", handleStatus);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(appVpOrderList, object.toString(), mCallback);

    }





    //出售中



    //订单完成
    public static final String orderCompletion = "/appVpOrder/orderCompletion.do";

    public void orderCompletion(String orderCode,  StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {


            object.put("orderCode", orderCode);//HttpBase.companyId_2
            object.put("account",HttpBase.account_2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(orderCompletion, object.toString(), mCallback);

    }


    //订单别表
//    public static final String orderListAppArea_sj = "/appAreaOrder/driveorderList.do";
    public static final String orderListAppArea_sj = "/appDriverOrder/driveOrderList.do";

    public void orderListAppArea_sj(String shopName, String handleStatus, int pageNo, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
//                    shopId: 门店编号,
//                    account: 下单账户,
//                    pageNo:页码可不传默认是1,
//                    handleStatus:可为空 1待配送2配送中3已收货4已取消,
//                    pageSize :页面记录数，可不传默认为10
//         object.put("shopId", HttpBase.shopId);
            if (!TextUtils.isEmpty(shopName)) {

                object.put("shopName", shopName);
            }
            object.put("account", HttpBase.account_2);
            object.put("owner", HttpBase.owner_2);
            object.put("pageNo", pageNo);
            object.put("pageSize", "10");
            if (!TextUtils.isEmpty(handleStatus))
                object.put("handleStatus", handleStatus);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(orderListAppArea_sj, object.toString(), mCallback);
    }

    //申请退货的界面
    public static final String getAbleOrderLinesByOrderCode = "/appReturnOrder/getAbleOrderLinesByOrderCode.do";

    public void getAbleOrderLinesByOrderCode(String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("orderCode", orderCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(getAbleOrderLinesByOrderCode, object.toString(), mCallback);
    }

    //    //完成收货的界面
//    public static final String getAbleOrderLinesByOrderCode = "/appReturnOrder/getAbleOrderLinesByOrderCode.do";
//    public  void  getAbleOrderLinesByOrderCode(String orderCode , StringCallback mCallback){
//        JSONObject object = new JSONObject();
//        try {
//            object.put("orderCode", orderCode);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        baseHttp_2(getAbleOrderLinesByOrderCode,  object.toString(),  mCallback);
//    }
    //退货
    public static final String saveReturnOrder = "/appReturnOrder/saveReturnOrder.do";


    //完成
//    public static final String submitDelivery = "/appOrder/submitDelivery.do";
//    public  void  submitDelivery(RequestParam mRequestParam , StringCallback mCallback){
//        Gson gson=  new Gson();
//        String params=     gson.toJson(mRequestParam);
//        baseHttp_2(submitDelivery, params,  mCallback);
//    }

    //退货图
    public static final String selectReturnOrderPicInfo = "/appReturnOrder/selectReturnOrderPicInfo.do";

    public void selectReturnOrderPicInfo(String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("orderCode", orderCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(selectReturnOrderPicInfo, object.toString(), mCallback);
    }

    //收货图
    public static final String selectReturnOrderImgInfo = "/appReturnOrder/selectReturnOrderImgInfo.do";

    public void selectReturnOrderImgInfo(String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("orderCode", orderCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(selectReturnOrderImgInfo, object.toString(), mCallback);
    }

    public static final String delImg = "/appReturnOrder/delImg.do";

    public void delImg(String id, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(delImg, object.toString(), mCallback);
    }


    public static final String appVpOrderLineList = "/appVpOrder/appVpOrderLineList.do";

    public void appVpOrderLineList(String ordercode, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("ordercode", ordercode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(appVpOrderLineList, object.toString(), mCallback);
    }



    //加载发货头部的信息
    public static final String initAppSendOrder = "/appVpOrder/initAppSendOrder.do";

    public void initAppSendOrder( StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("companyId", HttpBase.companyId_2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(initAppSendOrder, object.toString(), mCallback);
    }


    //确认实收
    public static final String saveAppRealAmount = "/appVpOrder/saveAppRealAmount.do";
    public void saveAppRealAmount(PaidInParams mPaidInParams, StringCallback mCallback) {
        Gson gson = new Gson();
       String params=     gson.toJson(mPaidInParams);

        baseHttp_2(saveAppRealAmount,params, mCallback);
    }

    //删除
    public static final String delDriverImg = "/appReturnOrder/delDriverImg.do";

    public void delDriverImg(String id, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(delDriverImg, object.toString(), mCallback);
    }

    //    public static final String uploadReturnOrderPic = "/appReturnOrder/uploadReturnOrderPic.do";
    public static final String uploadReturnOrderPic = "/appReturnOrder/uploadReturnOrderPicAPP.do";

    //申请退货上传图片
    public void uploadReturnOrderPic(File file, String orderCode, StringCallback mCallback) {

        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account_2); //帐号
            object.put("orderCode", orderCode); //t_order_tpl表主键
            object.put("deviceOS", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.post()
                .addFile("file", file.getName(), file)
                .url(HOST_STRING + uploadReturnOrderPic)
                .addParams("content", object.toString())
                .addParams("version", "1")
                .addParams("account", HttpBase.account_2)
                .addParams("orderCode", orderCode)
                .addParams("login_token", HttpBase.token_2)
                .build()//
                .execute(mCallback);
    }

    //
    public static final String uploadDriverPic = "/appReturnOrder/uploadDriverPic.do";

    //完成送货
    public void uploadDriverPic(File file, String orderCode, StringCallback mCallback) {

        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account_2); //帐号
            object.put("orderCode", orderCode); //t_order_tpl表主键
            object.put("deviceOS", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.post()
                .addFile("file", file.getName(), file)
                .url(HOST_STRING + uploadDriverPic)
                .addParams("content", object.toString())
                .addParams("version", "1")
                .addParams("account", HttpBase.account_2)
                .addParams("orderCode", orderCode)
                .addParams("login_token", HttpBase.token_2)
                .build()//
                .execute(mCallback);
    }


    public static final String submitDelivery = "/appOrder/submitDelivery.do";

    public void submitDelivery(String orderType, String orderCode, String gpsLongitude, String gpsLatitude, String subStatus, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            if ("2".equals(orderType)) {
                object.put("orderType", "2");
            }

            object.put("orderCode", orderCode);
            object.put("gpsLongitude", gpsLongitude);
            object.put("gpsLatitude", gpsLatitude);
            object.put("subStatus", subStatus);
            object.put("account", HttpBase.account_2);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp_2(submitDelivery, object.toString(), mCallback);
    }

    public void baseHttp_2(String murl, String jsonString, StringCallback mCallback) {
        JSONObject json = null;
        try {
            json = new JSONObject(jsonString);
            json.put("deviceOS", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .post()
                .tag(murl)
                .url(HOST_STRING + murl)
                .addParams("content", json != null ? json.toString() : jsonString)
                .addParams("version", "1")
                .addParams("deviceOS", "android-" + android.os.Build.VERSION.RELEASE)
                .addParams("login_token", HttpBase.token_2)
                .build()
                .execute(mCallback);
    }
//    --------------------业务员的API接口结束-------------------------------


    //  --------------------  vp的接口-------------------------------
// 首页商品
    public static final String productIndex = "/appVpProduct/productIndex.do";

    public void productIndex(StringCallback mCallback) {

        JSONObject object = new JSONObject();
        try {
            object.put("skuName", "");
            object.put("classid", "");
            object.put("owner", "");
            object.put("page", "1");
            object.put("pageSize", "6");
            object.put("flag", "F");
        } catch (Exception e) {
            e.printStackTrace();
        }
        baseHttp(productIndex, object.toString(), mCallback);
    }
    public static final String productIndexH5 = "/productH5/productIndex.do";
    public void productIndexH5(String classId, String status, String key, int pageNo, StringCallback mCallback) {
//        {"classId": "","status":"Y","key":"","companyId": "818","productId": "","zeroStock":"","pageSize": "10","pageNo": "1"}
        JSONObject object = new JSONObject();
        try {
            object.put("companyId", HttpBase.companyId_2);
            object.put("productId", "");
            object.put("zeroStock", "");
            object.put("pageNo", pageNo+"");
            object.put("pageSize", "10");
            object.put("classId", classId);
            object.put("status", status);
            object.put("key", key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        baseHttp_2(productIndexH5, object.toString(), mCallback);

    }

    public void productIndexH5Shouxin(String classId, String status, String key, int pageNo, StringCallback mCallback) {
//        {"classId": "","status":"Y","key":"","companyId": "818","productId": "","zeroStock":"","pageSize": "10","pageNo": "1"}
        JSONObject object = new JSONObject();
        try {
            object.put("companyId", HttpBase.companyId_2);
            object.put("productId", "");
            object.put("zeroStock", "1");
            object.put("pageNo", pageNo+"");
            object.put("pageSize", "10");
            object.put("classId", classId);
            object.put("status", status);
            object.put("key", key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        baseHttp_2(productIndexH5, object.toString(), mCallback);

    }
    public void productIndexH5Search(String classId, String status, String key, int pageNo, StringCallback mCallback) {
//        {"classId": "","status":"Y","key":"","companyId": "818","productId": "","zeroStock":"","pageSize": "10","pageNo": "1"}
        JSONObject object = new JSONObject();
        try {
            object.put("companyId", HttpBase.companyId_2);
            object.put("productId", "");
            object.put("zeroStock", "");
            object.put("pageNo", pageNo+"");
            object.put("pageSize", "10");
            object.put("classId", classId);
            object.put("status", status);
            object.put("key", key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        baseHttp_2(productIndexH5, object.toString(), mCallback);

    }


//
    public void productIndexH5One(String productId,StringCallback mCallback) {
//        {"classId": "","status":"Y","key":"","companyId": "818","productId": "","zeroStock":"","pageSize": "10","pageNo": "1"}
        JSONObject object = new JSONObject();
        try {
            object.put("companyId", HttpBase.companyId_2);
            object.put("productId", productId);
            object.put("zeroStock", "");
            object.put("pageNo", "1");
            object.put("pageSize", "1");
            object.put("classId", "");
            object.put("status", "");
            object.put("key", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        baseHttp_2(productIndexH5, object.toString(), mCallback);

    }
    public static final String initVpProductEdit = "/appVpProduct/initVpProductEdit.do";
    public void initVpProductEdit(String productId,StringCallback mCallback) {
//        {"classId": "","status":"Y","key":"","companyId": "818","productId": "","zeroStock":"","pageSize": "10","pageNo": "1"}
        JSONObject object = new JSONObject();
        try {
            object.put("companyId", HttpBase.companyId_2);
            object.put("productId", productId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        baseHttp_2(initVpProductEdit, object.toString(), mCallback);

    }


    public static final String vpEditProductPrice = "/appVpProduct/vpEditProductPrice.do";
    public void vpEditProductPrice(VpPricesParams mVpPricesParams, StringCallback mCallback) {
//        {"classId": "","status":"Y","key":"","companyId": "818","productId": "","zeroStock":"","pageSize": "10","pageNo": "1"}
                Gson gosn= new Gson();
         String gson= gosn.toJson(mVpPricesParams);

        baseHttp_2(vpEditProductPrice, gson, mCallback);

    }


    public static final String vpEditProduct = "/appVpProduct/vpEditProduct.do";
    public void vpEditProduct(VpBIanjiParams mVpBIanjiParams, StringCallback mCallback) { //编辑
        Gson  gson =new Gson();
          String mVpBIanjiParamsStr= gson.toJson(mVpBIanjiParams);
        baseHttp_2(vpEditProduct, mVpBIanjiParamsStr, mCallback);

    }



    //更多
    public void productMoreIndex(StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("skuName", "");
            object.put("classid", "");
            object.put("owner", HttpBase.owner);
            object.put("flag", "T");
        } catch (Exception e) {
            e.printStackTrace();
        }
        baseHttp(productIndex, object.toString(), mCallback);
    }

    //查看类别的接口
    public static final String selClass = "/appVpProductClass/selClass.do";

    public void selClass(StringCallback mCallback) {

        baseHttp(selClass, "{}", mCallback);
    }

    //查看优惠卷
    public static final String getAllCouponApp = "/appCounpon/getAllCouponApp.do";

    public void getAllCouponApp(StringCallback mCallback) {

        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account);
            object.put("pageSize", "6");
            object.put("pageNo", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getAllCouponApp, object.toString(), mCallback);
    }
    public void getAllCouponAppAll(StringCallback mCallback) {

        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account);
            object.put("pageSize", "30");
            object.put("pageNo", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getAllCouponApp, object.toString(), mCallback);
    }

    //领取
    public static final String receiveCouponApp = "/appCounpon/receiveCouponApp.do";

    public void getAllCouponApp(String parentId,String couponId, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account);
            object.put("parentId", parentId);
            object.put("couponId", couponId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(receiveCouponApp, object.toString(), mCallback);
    }

    //个人消息的接口
    public static final String getVpStateCount = "/appAccount/getStateCount.do";

    public void getVpStateCount(StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getVpStateCount, object.toString(), mCallback);
    }

    //消息接口
    public static final String messageCount = "/message/messageCount.do";

    public void messageCount(StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(messageCount, object.toString(), mCallback);
    }

    //搜索商品接口
    public static final String serarchProductList = "/appVpProduct/productList.do";

    public void serarchProductList(String keyStr, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("skuname", keyStr);
            object.put("owner", HttpBase.owner);
            object.put("skucode", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(serarchProductList, object.toString(), mCallback);
    }


    //提交订单可用优惠卷
    public static final String getCouponForOrderApp = "/appCounpon/getCouponForOrderApp.do";
    public void getCouponForOrderAppY(String tClasses,StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("tClasses", tClasses);
            object.put("account", HttpBase.account);
            object.put("state", "Y");
            object.put("pageSize", "100");
            object.put("pageNo", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getCouponForOrderApp, object.toString(), mCallback);
    }

    //提交订单不可用优惠卷
    public void getCouponForOrderAppN(String tClasses,StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("tClasses", tClasses);
            object.put("account", HttpBase.account);
            object.put("state", "N");
            object.put("pageSize", "100");
            object.put("pageNo", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getCouponForOrderApp, object.toString(), mCallback);
    }



    //查询未使用的优惠卷
    public static final String getMyCouponApp = "/appCounpon/getMyCouponApp.do";
    public void getMyCouponApp(StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account);
            object.put("state", "N");
            object.put("pageSize", "100");
            object.put("pageNo", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getMyCouponApp, object.toString(), mCallback);
    }

    public void getMyCouponAppY(StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account);
            object.put("state", "Y");
            object.put("pageSize", "100");
            object.put("pageNo", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getMyCouponApp, object.toString(), mCallback);
    }

    public void getMyCouponAppD(StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account);
            object.put("state", "D");
            object.put("pageSize", "100");
            object.put("pageNo", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getMyCouponApp, object.toString(), mCallback);
    }

    //收藏商品的接口
    public static final String vpSelectFavoryList = "/appVpProduct/vpSelectFavoryList.do";

    public void vpSelectFavoryList(StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account);
            object.put("page", "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(vpSelectFavoryList, object.toString(), mCallback);
    }

    //查询购物车的商品的
    public static final String vpshoppingCart = "/appVpProduct/shoppingCart.do";

    public void vpshoppingCart(String ids, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("ids", ids);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(vpshoppingCart, object.toString(), mCallback);
    }

    //查询商品的详情
    public static final String vpnewProductInfo = "/appVpProduct/productInfo.do";

    public void vpnewProductInfo(String productid, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("productid", productid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(vpnewProductInfo, object.toString(), mCallback);
    }

    //加载vp分类首页的数据
    public static final String getVpClassProductList = "/appVpProduct/classProductList.do";

    public void getVpClassProductList(StringCallback mCallback) {
        JSONObject object = new JSONObject();
//        try {
////            object.put("owner", HttpBase.owner);
////            object.put("shopId", HttpBase.shopId);
////            object.put("wmCode", HttpBase.wmCode);
////            object.put("companyId", HttpBase.companyId);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        baseHttp(getVpClassProductList, object.toString(), mCallback);
    }

    //加载vp收藏
    public static final String getVpAddFavory = "/appVpProduct/vpAddFavory.do";

    public void getVpAddFavory(String id, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account);
            object.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getVpAddFavory, object.toString(), mCallback);
    }

    //取消vp收藏
    public static final String getvpDelFavory = "/appVpProduct/vpDelFavory.do";

    public void getvpDelFavory(String id, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account);
            object.put("id", id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getvpDelFavory, object.toString(), mCallback);
    }

    //查看是不是收藏了
    public static final String getvpisFavoryProduct = "/appVpProduct/isFavoryProduct.do";

    public void getvpisFavoryProduct(String id, StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", HttpBase.account);
            object.put("id", id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(getvpisFavoryProduct, object.toString(), mCallback);
    }

    //订单别表
    public static final String vporderList = "/appVpOrder/orderList.do";

    public void vporderList(String handleStatus, String nopay, int pageNo, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
//                    shopId: 门店编号,
//                    account: 下单账户,
//                    pageNo:页码可不传默认是1,
//                    handleStatus:可为空 1待配送2配送中3已收货4已取消,
//                    pageSize :页面记录数，可不传默认为10

            object.put("shopId", HttpBase.shopId);
            object.put("account", HttpBase.account);
            object.put("pageNo", pageNo);
            object.put("nopay", nopay);
            if (!TextUtils.isEmpty(handleStatus))
                object.put("handleStatus", handleStatus);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(vporderList, object.toString(), mCallback);
    }

    //订单的详情接口
    public static final String toVpOrderDetail = "/appVpOrder/toOrderDetail.do";

    public void toVpOrderDetail(String orderCode, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("ordercode", orderCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(toVpOrderDetail, object.toString(), mCallback);
    }

    //修改名称
    public static final String setAccountShopName = "/appVpShop/setAccountShopName.do";

    public void setAccountShopName(String shopName, StringCallback mCallback) {
        JSONObject object = new JSONObject();

        try {
            object.put("account", HttpBase.account);
            object.put("shopId", HttpBase.shopId);
            object.put("shopName", shopName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(setAccountShopName, object.toString(), mCallback);
    }


    public static final String selectSearchCategory = "/appProduct/selectSearchCategory.do";

    public void selectSearchCategory(StringCallback mCallback) {
        JSONObject object = new JSONObject();
        try {
            object.put("owner", HttpBase.owner);
            object.put("account", HttpBase.account);
            object.put("shopId", HttpBase.shopId);
            object.put("wmCode ", HttpBase.wmCode);
            object.put("companyId  ", HttpBase.companyId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseHttp(selectSearchCategory, object.toString(), mCallback);
    }


}
