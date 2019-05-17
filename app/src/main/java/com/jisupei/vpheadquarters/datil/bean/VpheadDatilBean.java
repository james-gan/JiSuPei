package com.jisupei.vpheadquarters.datil.bean;

import java.io.Serializable;
import java.util.List;

public class VpheadDatilBean implements Serializable {

		public String  optFlag;
		public OrderLine  orderLine;
		public List<OrderSkuTemp> orderSku;
		public class OrderLine{

				public String sum_amount;
				public String create_time;
				public String wm_name;
				public String returnStatus;
				public String sendtype;
				public String order_code;
				public String create_by;

				public String couponAmount;
				public String pay_status;
				public String rel_account;
				public String delaypay_flag;
				public String realamount;
				public String returnPayType;
				public String receive_name;
				public String receive_phone;
				public String receive_name2;
				public String receive_phone2;
				public String receive_name3;
				public String receive_phone3;
				public String handle_status;
				public String account;
				public String receive_address;
				public String wms_status;
				public String returnSumAmount;
				public String content2;
				public String couponValue;
				public String car_name;


			public List<Audit> auditList;


	 }
	public class Audit  implements Serializable {

		public String  create_by;
		public String  opt_time;
		public String  remark;
		public String  real_name;
		public String  audit_status;

	}
	public class OrderSkuTemp{

				public String  summoney;
				public String  sum_amount;
				public String  orderSumAmount;
				public String  styleno;
				public String  qty_flag;
				public String  recivedqty;
				public String  couponAmount;
				public String  recived_qty;
				public String  sku_name;
				public String  id;
				public String  handle_status;
				public String  supplier_name;
				public String  unitqty;
				public String  delivery_phone;
				public String  send_qty;
				public String  owner;
				public String  sku_type;
				public String  unit_qty;
				public String  unit_price;
				public String  sendqty;
				public String  real_amount;
				public String  lock_qty;
				public String  uom_default;
				public String  unit;
				public String  product_type;
				public String  img_path;
				public String  wmsoutqyt;
				public String  qty;
				public String  supplier_tel;
				public String  equation_factor;
				public String  sku_code;
	}
}
