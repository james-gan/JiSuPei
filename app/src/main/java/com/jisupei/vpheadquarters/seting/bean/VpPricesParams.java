package com.jisupei.vpheadquarters.seting.bean;

import com.jisupei.http.HttpBase;

import java.io.Serializable;
import java.util.List;

public class VpPricesParams implements Serializable {

			public String owne=HttpBase.owner_2;
			public String account=HttpBase.account_2;
			public String realName=HttpBase.realName_2;
			public String companyId=HttpBase.companyId_2;
			public String productId;
			public String unitPrice;
			public String marketUnitprice;
			public String unitCost;
			public String purPrice;
			public String price;
			public String hasHomePage;

	    public List<PriceListTemp> priceList;

		public class PriceListTemp implements Serializable {

			public String key;
			public  Value  value;

		}

	public class Value implements Serializable {

				public String  id;
				public String  begin_qty;
				public String  end_qty;
				public String  qty_price;
				public String  sku_code;
				public String  type;


	}

}
