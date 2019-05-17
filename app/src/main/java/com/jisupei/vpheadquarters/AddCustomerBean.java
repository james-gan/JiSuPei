package com.jisupei.vpheadquarters;

import java.io.Serializable;
import java.util.List;

public class AddCustomerBean implements Serializable {

		public List<ProvinceListItem> provinceList;
	public List<CompanyListItem> companyList;
	public List<CustomerTypeListItem> customerTypeList;


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
