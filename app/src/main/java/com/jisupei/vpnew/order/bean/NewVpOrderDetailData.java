package com.jisupei.vpnew.order.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
public class NewVpOrderDetailData implements Serializable {
          public  String shop_name; //名称
          public  String createTime;
          public  String skuNum;
          public  String orderSubmitTime;
          public  String orderCode;
          public  String receive_phone2;
          public  String receive_phone3;
          public  String receivePhone;
          public List<SkuListItem> skuList;
           public String handleStatus;
           public String receive_name3;
           public String city_cn;
           public String receive_name2;
           public String receiveName;
           public String receiveAddress;
           public String orderSubmitAccount;
           public String sumAmount;
           public String note;
           public String province_cn;
           public String freight_charges; //运费
           public String coupon_amount; //运费
    public String returnSumAmount;
    public String couponValue;

    public List<Audit> auditList;
        public class Audit  implements Serializable {

            public String  create_by;
            public String  opt_time;
            public String  remark;
            public String  real_name;
            public String  audit_status;
        }


    public class SkuListItem  implements Serializable {

                   public String  equation_factor;
                   public String  price;
                   public String  send_qty;
                   public String  styleno;
                   public String  assist_unit;
                   public String  sku_type;
                   public String  img_path;
                   public String  skuName;
                   public String  thisQty;
                   public String  uom_default;
                   public String  skuCode;
                   public String  recived_qty;
                   public String  qty;
                   public String  unit_qty;
                   public String  unit;
                   public String  giftQty;

                   public String  price_type; //1是批发  0是零售
                   public String  unit_price;




        }


}
