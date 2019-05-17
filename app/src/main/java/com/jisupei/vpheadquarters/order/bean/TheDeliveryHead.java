package com.jisupei.vpheadquarters.order.bean;

import java.io.Serializable;
import java.util.List;

public class TheDeliveryHead implements Serializable {
	public List<MyWmTemp> myWmList;
	public List<CarTemp>  carList;
	public   class MyWmTemp  implements Serializable{
				public String  company_id;
				public String  wm_province;
				public String  create_time;
				public String  wm_province_name;
				public String  wm_contact_tel;
				public String  create_by;
				public String  wm_type;
				public String  wm_city_name;
				public String  wm_addr;
				public String  wm_nm;
				public String  wm_code;
				public String  wm_contact;
				public String  id;
				public String  wm_city;
				public String  value;
				public String  key;
	}
	public   class CarTemp  implements Serializable{
				public String  car_name;
				public String  device_name;
				public String  frozen_type;
				public String  company_id;
				public String  device_code;
				public String  car_brand;
				public String  car_code;
				public String  id;
				public String  max_num;
				public String  max_volume;
				public String  car_type;

	}
}