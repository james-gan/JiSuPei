package com.jisupei.vpheadquarters;

import java.io.Serializable;
import java.util.List;

public class EditsCustomerBean implements Serializable {

		public List<CityListItem> cityList;
		public List<ProvinceListItem> provinceList;
	public List<CompanyListItem> companyList;
	public List<CustomerTypeListItem> customerTypeList;
	public List<DistrictListItem> districtList;
	public List<MywarehouseListItem> mywarehouseList;
	public List<ThirdwarehouseListItem> thirdwarehouseList;
	public  ShopMap  shopMap;
	public class ThirdwarehouseListItem implements Serializable {
				public String id;
				public String companyId;
				public String wmCode;
				public String outCode;
				public String wmNm;
				public String wmType;
				public String wmProvince;
				public String wmProvinceName;
				public String wmCity;
				public String wmCityName;
				public String wmAddr;
				public String wmContact;
				public String wmContactTel;
				public String enable;
				public String createTime;
				public String createBy;
				public String updateTime;
				public String updateBy;
				public String isShow;
				public String mainWm;
				public String qtyFlag;
				public String operateable;

	}
	public class ShopMap implements Serializable {
			public String contact_name;
			public String address;
			public String contact_phone;
			public String company_id;
			public String city;
			public String type_id;
			public String shop_name;
			public String sid;
			public String province;
			public String district;
			public String wm_nm;
			public String shop_type;
			public String wm_code;
			public String auto_days;

	}
	public class MywarehouseListItem implements Serializable {
				public String id;
				public String companyId;
				public String wmCode;
				public String outCode;
				public String wmNm;
				public String wmType;
				public String wmProvince;
				public String wmProvinceName;
				public String wmCity;
				public String wmCityName;
				public String wmAddr;
				public String wmContact;
				public String wmContactTel;
				public String enable;
				public String createTime;
				public String createBy;
				public String updateTime;
				public String updateBy;
				public String isShow;
				public String mainWm;
				public String qtyFlag;
				public String operateable;

	}
	public class CityListItem implements Serializable {

				public String id;
				public String title;
				public String level;
				public String parentid;
				public String sort;
				public String createBy;
				public String createTime;
				public String updateBy;
				public String updateTime;
				public String children;

	}
	public class DistrictListItem implements Serializable {

				public String  id;
				public String  title;
				public String  level;
				public String  parentid;
				public String  sort;
				public String  createBy;
				public String  createTime;
				public String  updateBy;
				public String  updateTime;
				public String  children;

	}
	public class ProvinceListItem implements Serializable {

			public  String id;
			public  String title;
			public  String level;
			public  String parentid;
			public  String sort;
			public  String createBy;
			public  String createTime;
			public  String updateBy;
			public  String updateTime;
			public  String children;

	}
	public class CompanyListItem implements Serializable {

			public String address;
			public String id;
			public String nm;

	}
	public class CustomerTypeListItem implements Serializable {

				public String id;
				public String typeName;
				public String createBy;
				public String createTime;
				public String updateBy;
				public String updateTime;
				public String owner;
				public String typeCode;

	}
}
