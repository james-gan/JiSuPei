package com.jisupei.vpheadquarters.order.bean;

import com.jisupei.http.HttpBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PaidInParams implements Serializable {

	 public String account= HttpBase.account_2;

	public String orderCode;
	public List<SkuTemp> skus=new ArrayList<SkuTemp>();


	public  class SkuTemp{
		public String	real_amount;
		public String	sum_amount;
		public String	id;

	}

}
