package com.jisupei.vpheadquarters;

import java.io.Serializable;
import java.util.List;

public class PovinceCityBean implements Serializable {

		public List<CityListItem> cityList;
		public List<DistrictListItem> districtList;


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

}
