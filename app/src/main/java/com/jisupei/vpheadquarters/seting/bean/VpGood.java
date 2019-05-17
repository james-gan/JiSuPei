package com.jisupei.vpheadquarters.seting.bean;

import java.io.Serializable;
import java.util.List;

public class VpGood implements Serializable {
		public String sku_dec;
		public String is_enable;
		public String class_id;
		public String styleno;
			public String sumQty;
			public String is_discount;
			public String update_time;
			public String resultQty;
			public String pinyin_text;
			public String price;
			public String is_hot;
			public String sku_name;
			public String id;
			public String supplier_name;
			public String owner;
			public String sumLock;
			public String lockQty;
			public String sort;

			public String unit_price;
			public String uom_default;
			public String unit;
			public String is_gift;
			public String classname;
			public String img_path;
			public String equation_factor;
			public String unit_cost;
			public String sku_code;
			public String supplier_code;

	public List<ImgListItem> imgList;
	public List<QtyListItem> qtyList;
	public List<RkPriceListItem> rkPriceList;

	public class ImgListItem implements Serializable {
		public String img_path;


	}

	public class QtyListItem implements Serializable {
				public String owner;
				public String create_by;
				public String lock_qty;
				public String create_time;
				public String qty;
				public String wm_code;
				public String id;
				public String sku_code;

	}
	public class RkPriceListItem implements Serializable {
		public String begin_qty;
		public String end_qty;
		public String qty_price;
	}

}
