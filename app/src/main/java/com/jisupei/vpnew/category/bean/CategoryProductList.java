package com.jisupei.vpnew.category.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */
public class CategoryProductList implements Serializable {

    public String ID;
    public String NAME;
    public List<NewVpIndexProductItem> list;
    public class NewVpIndexProductItem  implements Serializable {
               public String equation_factor;
               public String supplier_code;
               public String unit_cost;
               public String classname;
               public String supplier_name;
               public String sku_dec;
               public String is_enable;
               public String unit_price;
               public String id;
               public String img_path;
               public String unit;
               public String class_id;
               public String styleno;
               public String price;
               public String sku_name;
               public String owner;
               public String sku_code;
               public String uom_default;
              public List<RkpriceListItem> rkpriceList;
    }

    public class RkpriceListItem implements Serializable {
        public String qty_price;
        public String begin_qty;
        public String end_qty;
    }

}
