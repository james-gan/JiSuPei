package com.jisupei.vpnew.index.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/17.
 */
public class ProductVpNewSearch implements Serializable {
           public String equation_factor;
           public String supplier_code;
           public String unit_cost;
           public String classname;
           public String supplier_name;
           public String is_enable;
           public String unit_price;
           public String id;
           public String img_path;
           public String unit;
           public String class_id;
           public String price;
           public String styleno;
           public String sku_name;
           public String owner;
           public String sku_code;
           public String uom_default;
           public String pinyin_text;



            public String market_unitprice;
            public String wm_name;
            public String is_discount;
            public String OWNER;
            public String is_hot;
            public String qty;
            public String wm_code;



            public List<RkpriceListItem> rkpriceList;
            public List<Img> imgList;
            public class RkpriceListItem implements Serializable {
                public String qty_price;
                public String begin_qty;
                public String end_qty;

            }
            public class Img implements Serializable {
                public String img_path;


            }


}
