package com.jisupei.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.jisupei.application.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpBase {

    public static Boolean islogin = false;
    public static String account = "";
    public static String token = "";
    public static String shopName = "";
    public static String companyName = "";
    public static String owner="";
    public static String shopId="";
    public static String wmCode="";
    public static String companyId="";
    public static String contactName="";
    public static String contactPhone="";
    public static String contactName2="";
    public static String contactPhone2="";
    public static String contactName3="";
    public static String contactPhone3="";
    public static String address="";
    public static String provinceName="";
    public static String companyType="";
    public static String cityName="";

    public static String roleId="";
    public static String realName="";
    public static String cid="";
    public static String typeId="";
    public static String isSelf=""; //，isSelf  1、自营 为空或者2为非自营

    //保存门店的登录信息
    public static void Login(Context context,String response) {
            HttpBase. islogin = true;
        try {
                JSONObject object = new JSONObject(response);
                JSONObject resObject=object.getJSONObject("res");
                String account=resObject.optJSONObject("account").optString("account");
               JSONObject  companyJSONObject  =resObject.optJSONObject("company");
                String companyName=companyJSONObject==null?"":companyJSONObject.optString("nm");
                String isSelf=companyJSONObject==null?"":companyJSONObject.optString("isSelf");
                String shopId=resObject.optJSONObject("account").optString("shopId");
                String roleId=resObject.optJSONObject("account").optString("role");
                 String realName=resObject.optJSONObject("account").optString("realName");
                //
                String owner=companyJSONObject==null?"":companyJSONObject.optString("owner");
                String loginToken=resObject.optString("logintoken");
            if("1".equals(roleId)){
                String shopName=resObject.optJSONObject("shop").optString("shopName");
                String companyId=resObject.optJSONObject("shop").optString("companyId");
                String contactName=resObject.optJSONObject("shop").optString("contactName");
                String contactPhone=resObject.optJSONObject("shop").optString("contactPhone");
                String wmCode=resObject.optJSONObject("shop").optString("wmCode");
                String contactName2=resObject.optJSONObject("shop").optString("contactName2");
                String contactPhone2=resObject.optJSONObject("shop").optString("contactPhone2");
                String contactName3=resObject.optJSONObject("shop").optString("contactName3");
                String contactPhone3=resObject.optJSONObject("shop").optString("contactPhone3");
                String address=resObject.optJSONObject("shop").optString("address");
                String cityName=resObject.optJSONObject("shop").optString("cityName");
                String provinceName=resObject.optJSONObject("shop").optString("provinceName");
                String typeId=resObject.optJSONObject("shop").optString("typeId");
                HttpBase.shopName=shopName;
                HttpBase.companyId=companyId;
                HttpBase.wmCode=wmCode;
                HttpBase.contactName=contactName;
                HttpBase.contactPhone=contactPhone;
                HttpBase.contactName2=contactName2;
                HttpBase.contactPhone2=contactPhone2;
                HttpBase.contactName3=contactName3;
                HttpBase.contactPhone3=contactPhone3;
                HttpBase.address=address;
                HttpBase.cityName=cityName;
                HttpBase.provinceName=provinceName;
                HttpBase.typeId=typeId;
            }
            //2是生产 厂商
            String companyType=companyJSONObject==null?"":companyJSONObject.optString("companyType");
            //本地静态存一份
            HttpBase.token=loginToken;
            HttpBase.account=account;
            HttpBase.roleId=roleId;
            HttpBase.realName=realName;
            HttpBase.companyName=companyName;
//            HttpBase.phone=phone;
            HttpBase.owner=owner;
            HttpBase.shopId=shopId;

            HttpBase.companyType=companyType;
            HttpBase.isSelf=isSelf;
            //缓存
            Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putBoolean("islogin", true);
            editor.putString("token", HttpBase.token);
            editor.putString("account", HttpBase.account);
            editor.putString("shopName", HttpBase.shopName);
            editor.putString("companyId", HttpBase.companyId);
            editor.putString("companyName", HttpBase.companyName);
            editor.putString("owner", HttpBase.owner);
            editor.putString("shopId", HttpBase.shopId);
            editor.putString("wmCode", HttpBase.wmCode);
            editor.putString("contactName", HttpBase.contactName);
            editor.putString("contactPhone", HttpBase.contactPhone);
            editor.putString("contactName2", HttpBase.contactName2);
            editor.putString("contactPhone2", HttpBase.contactPhone2);
            editor.putString("contactName3", HttpBase.contactName3);
            editor.putString("contactPhone3", HttpBase.contactPhone3);
            editor.putString("address", HttpBase.address);

            editor.putString("cityName", HttpBase.cityName);
            editor.putString("provinceName", HttpBase.provinceName);

            editor.putString("companyType", HttpBase.companyType);
            editor.putString("roleId", HttpBase.roleId);
            editor.putString("realName", HttpBase.realName);
            editor.putString("isSelf", HttpBase.isSelf);
            editor.putString("typeId", HttpBase.typeId);
            editor.commit();


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    //保存业务员的登录的信息
    public static void Login_2(Context context,String response) {
        islogin = true;
        try {
            JSONObject object_2 = new JSONObject(response);
            JSONObject resObject=object_2.getJSONObject("res");
            String account_2=resObject.optJSONObject("account").optString("account");
            JSONObject  companyJSONObject  =resObject.optJSONObject("company");
            String companyName=companyJSONObject==null?"":companyJSONObject.optString("nm");
            String isSelf_2=companyJSONObject==null?"":companyJSONObject.optString("isSelf");
               String phone_2=resObject.getJSONObject("account").getString("phone");
//            String shopId=resObject.optJSONObject("account").optString("shopId");
            String roleId=resObject.optJSONObject("account").optString("role");
            String realName=resObject.optJSONObject("account").optString("realName");
            String companyId_2=resObject.optJSONObject("account").optString("companyId");
            //
            String owner=companyJSONObject==null?"":companyJSONObject.optString("owner");
            String loginToken=resObject.optString("logintoken");
//            if("1".equals(roleId)){
//                String shopName=resObject.optJSONObject("shop").optString("shopName");
//                String companyId=resObject.optJSONObject("shop").optString("companyId");
//                String contactName=resObject.optJSONObject("shop").optString("contactName");
//                String contactPhone=resObject.optJSONObject("shop").optString("contactPhone");
//                String wmCode=resObject.optJSONObject("shop").optString("wmCode");
//                String contactName2=resObject.optJSONObject("shop").optString("contactName2");
//                String contactPhone2=resObject.optJSONObject("shop").optString("contactPhone2");
//                String contactName3=resObject.optJSONObject("shop").optString("contactName3");
//                String contactPhone3=resObject.optJSONObject("shop").optString("contactPhone3");
//                String address=resObject.optJSONObject("shop").optString("address");
//                String cityName=resObject.optJSONObject("shop").optString("cityName");
//                String provinceName=resObject.optJSONObject("shop").optString("provinceName");
                HttpBase.shopName=shopName;
                HttpBase.companyId=companyId;
                HttpBase.wmCode=wmCode;
                HttpBase.contactName=contactName;
                HttpBase.contactPhone=contactPhone;
                HttpBase.contactName2=contactName2;
                HttpBase.contactPhone2=contactPhone2;
                HttpBase.contactName3=contactName3;
                HttpBase.contactPhone3=contactPhone3;
                HttpBase.address=address;
                HttpBase.cityName=cityName;
                HttpBase.provinceName=provinceName;
//
//            }
            //2是生产 厂商
            String companyType=companyJSONObject==null?"":companyJSONObject.optString("companyType");
            //本地静态存一份
            HttpBase.token_2=loginToken;
            HttpBase.account_2=account_2;
            HttpBase.roleId=roleId;
            HttpBase.realName_2=realName;
            HttpBase.phone_2=phone_2;
            HttpBase.companyName_2=companyName;
            HttpBase.owner_2=owner;
            HttpBase.companyType_2=companyType;
            HttpBase.isSelf_2=isSelf_2;
            HttpBase.companyId_2=companyId_2;
            //缓存
            Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putBoolean("islogin", true);
            editor.putString("token_2", HttpBase.token_2);
            editor.putString("account_2", HttpBase.account_2);
            editor.putString("companyName_2", HttpBase.companyName_2);
            editor.putString("owner_2", HttpBase.owner_2);
//            editor.putString("shopId", HttpBase.shopId);
            editor.putString("companyType_2", HttpBase.companyType_2);
            editor.putString("roleId", HttpBase.roleId);
            editor.putString("realName_2", HttpBase.realName_2);
            editor.putString("phone_2", HttpBase.phone_2);
            editor.putString("isSelf_2", HttpBase.isSelf_2);
            editor.putString("companyId_2", HttpBase.companyId_2);
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public static String token_2="";
    public static String account_2="";
    public static String companyName_2="";
    public static String owner_2="";
    public static String companyType_2="";
//    public static String roleId_2="";
    public static String realName_2="";
    public static String phone_2="";
    public static String isSelf_2="";
    public static String companyId_2="";

    //业务员数据
    public static void Resume_2(Context context){
        //恢复
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        HttpBase.token_2=preferences.getString("token_2","");
        HttpBase.islogin=preferences.getBoolean("islogin", false);
//        if(!TextUtils.isEmpty(token)&&HttpBase.islogin){
            HttpBase.account_2=preferences.getString("account_2","");
            HttpBase.companyName_2=preferences.getString("companyName_2","");
//           HttpBase.phone=phone;
            HttpBase.owner_2=preferences.getString("owner_2","");
            HttpBase.companyType_2=preferences.getString("companyType_2","");
            HttpBase.roleId=preferences.getString("roleId","");
            HttpBase.realName_2=preferences.getString("realName_2","");
            HttpBase.phone_2=preferences.getString("phone_2","");
            HttpBase.isSelf_2=preferences.getString("isSelf_2","");
            HttpBase.companyId_2=preferences.getString("companyId_2","");
//        }

    }
    //----------------门店登录的数据
    public static void Resume(Context context){
        //恢复
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        HttpBase.token=preferences.getString("token","");
        HttpBase.islogin=preferences.getBoolean("islogin", false);
        if(!TextUtils.isEmpty(token)&&HttpBase.islogin){

            HttpBase.account=preferences.getString("account","");
            HttpBase.shopName=preferences.getString("shopName","");
            HttpBase.companyId=preferences.getString("companyId","");
            HttpBase.companyName=preferences.getString("companyName","");
//           HttpBase.phone=phone;
            HttpBase.owner=preferences.getString("owner","");
            HttpBase.shopId=preferences.getString("shopId","");
            HttpBase.wmCode=preferences.getString("wmCode","");
            HttpBase.contactName=preferences.getString("contactName","");
            HttpBase.contactPhone=preferences.getString("contactPhone","");
            HttpBase.contactName2=preferences.getString("contactName2","");
            HttpBase.contactPhone2=preferences.getString("contactPhone2","");
            HttpBase.contactName3=preferences.getString("contactName3","");
            HttpBase.contactPhone3=preferences.getString("contactPhone3","");
            HttpBase.address=preferences.getString("address","");

            HttpBase.cityName=preferences.getString("cityName","");
            HttpBase.provinceName=preferences.getString("provinceName","");

            HttpBase.companyType=preferences.getString("companyType","");
            HttpBase.roleId=preferences.getString("roleId","");
            HttpBase.realName=preferences.getString("realName","");
            HttpBase.isSelf=preferences.getString("isSelf","");
            HttpBase.typeId=preferences.getString("typeId","");
        }

    }
    public static  String getAccount(Context context){
        //恢复
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.instance);
        HttpBase.token=preferences.getString("token","");
        HttpBase.islogin=preferences.getBoolean("islogin", false);
        if(!TextUtils.isEmpty(token)&&HttpBase.islogin){
            String account=preferences.getString("account","");
            return account;
        }
        return null;

    }

    //判断是门店登录还是业务员登录
    public static  String isShopOrYWy(){
        //恢复
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.instance);
         boolean islog=preferences.getBoolean("islogin", false);
         String token_2=preferences.getString("token_2","");
         String token=preferences.getString("token","");
        if(islog){//已经登录
            String role_Id=preferences.getString("roleId", "");
              if(!TextUtils.isEmpty(token_2)&& !TextUtils.isEmpty(token)){
                  role_Id="01";
              }
            return  role_Id;

        }
        return "";
    }


    //退出更新
    public static void Exit(Context context) {
        islogin = false;
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("islogin", false);
        editor.clear();
        editor.commit();
    }

    //判断手机号
    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }


    // MD5编码
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuff.toString();
    }

    /**
     * 提供精确的加法运算。
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();

    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1,double v2){
        return div(v1,v2,10);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1,double v2,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }


}