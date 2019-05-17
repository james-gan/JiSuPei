package com.jisupei.vpnew.index.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/13.
 */
public class IndexData implements Serializable {

       public List<ProductNew> discountList;
       public List<ProductNew> hotList;
       public List<List<ProductNew>> groupList=new ArrayList<List<ProductNew>>();


     public class ProductNew implements Serializable {
            public String class_id;
            public String styleno;
            public String unit_price;
            public String uom_default;
            public String unit;
            public String classname;
            public String img_path;
            public String equation_factor;
            public String sku_name;
            public String id;
            public String supplier_name;
            public String sku_code;
               public String imgList;
            public String rkpriceList;
       public List<RkpriceListItem> rkpriceListArr;
     }

       public class RkpriceListItem implements Serializable {
              public String qty_price;
              public String begin_qty;
              public String end_qty;
       }

}
