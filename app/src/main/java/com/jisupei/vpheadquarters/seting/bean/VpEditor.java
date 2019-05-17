package com.jisupei.vpheadquarters.seting.bean;

import java.io.Serializable;
import java.util.List;

public class VpEditor implements Serializable {


	public List<SaveTypeItem> saveType;
	public List<TVclassListItem> tVclassList;
	public List<SupplierListItem> supplierList;
	public List<TclassListItem> tclassList;
	public Info info;

	public class Info implements Serializable {
		public InfoIn info;
		public List<ImgListItem> imgList;
		public List<PriceListItem> priceList;


		public class InfoIn implements Serializable {


			 		public String classId;
					public String className;
					public String code;
					public String cost;
					public String createBy;
					public String createTime;
					public String equationFactor;
					public String goodStatus;
					public String grossWeight;
					public String height;
					public String id;
					public String imgPath;
					public String isDiscount;
					public String isEnable;
					public String isGift;
					public String isHot;
					public String length;
					public String marketUnitprice;
					public String outcode;
					public String owner;
					public String pinyinText;
					public String price;
					public String prices;
					public String pubStatus;
					public String pubTime;
					public String purPrice;
					public String purPrices;
					public String s30;
					public String skuCode;
					public String skuDec;
					public String skuName;
					public String skuType;
					public String smallUnitId;
					public String sort;
					public String storageMode;
					public String storageModeId;
					public String styleno;
					public String supplierName;
					public String supplierCode;
					public String synTime;
					public String taxAmount;
					public String taxTate;
					public String taxType;
					public String unit;
					public String unitCost;
					public String unitCosts;
					public String unitId;
					public String unitPrice;
					public String unitPrices;
					public String uomDefault;
					public String updateBy;
					public String updateTime;
					public String validLength;
					public String validity;
					public String vclassCode;
					public String vclassName;
					public String volume;
					public String weight;
					public String width;


		}

		class ImgListItem implements Serializable {
					public String createBy;
					public String createTime;
					public String id;
					public String imgPath;
					public String productId;

		}

	}

	public class PriceListItem implements Serializable {

		public String begin_qty;
		public String qty_prices;
		public String end_qtys;
		public String end_qty;
		public String begin_qtys;
		public String id;
		public String qty_price;
		public String sku_code;


	}
	public class SaveTypeItem implements Serializable {
			public String item_id;
	 	public String item_value;


	}

	public class TVclassListItem implements Serializable {

				public String className;
				public String code;
				public String icon;
				public String id;
				public String isEnable;


	}
	public class SupplierListItem implements Serializable {
				public String supplier_name;
				public String supplier_code;

	}
	public class TclassListItem implements Serializable {
				public String channelId;
				public String classCode;
				public String createBy;
				public String createTime;
				public String creatorDept;
				public String id;
				public String isDel;
				public String isRecommend;
				public String name;
				public String parentid;
				public String sort;
				public String updateBy;
				public String updateTime;

	}

}
