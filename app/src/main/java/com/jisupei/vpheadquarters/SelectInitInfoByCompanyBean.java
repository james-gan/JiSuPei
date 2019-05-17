package com.jisupei.vpheadquarters;

import java.io.Serializable;
import java.util.List;

public class SelectInitInfoByCompanyBean implements Serializable {

	public List<AreaListItem> areaList;
	public List<CustomerTypeListItem> customerTypeList;
	public List<MywarehouseListItem> mywarehouseList;
	public List<ThirdwarehouseListItem> thirdwarehouseList;


	public class AreaListItem implements Serializable {

		public String companyId;
		public String createBy;
		public String createTime ;
		public String iId ;
		public String id ;
		public String nm ;
		public String sort ;
		public String updateBy ;
		public String updateTime ;

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
	public class ThirdwarehouseListItem implements Serializable {

				public String  id;
				public String  companyId;
				public String  wmCode;
				public String  outCode;
				public String  wmNm;
				public String  wmType;
				public String  wmProvince;
				public String  wmProvinceName;
				public String  wmCity;
				public String  wmCityName;
				public String  wmAddr;
				public String  wmContact;
				public String  wmContactTel;
				public String  enable;
				public String  createTime;
				public String  createBy;
				public String  updateTime;
				public String  updateBy;
				public String  isShow;
				public String  mainWm;
				public String  qtyFlag;
				public String  operateable;
	}

}
