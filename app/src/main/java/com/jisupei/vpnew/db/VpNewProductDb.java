package com.jisupei.vpnew.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiayumo on 16/5/11.
 */


@DatabaseTable(tableName = "tb_vp_product")
public class VpNewProductDb implements Serializable {

    private static final long serialVersionUID = -5683263669918171030L;
    @DatabaseField(generatedId=true)
    public int id1;
    @DatabaseField
    public String equation_factor;
    @DatabaseField
    public String supplier_code;
    @DatabaseField
    public String unit_cost;
    @DatabaseField
    public String classname;
    @DatabaseField
    public String supplier_name;
    @DatabaseField
    public String is_discount;
    @DatabaseField
    public String qty;
    @DatabaseField
    public String sku_dec;
    @DatabaseField
    public String is_enable;
    @DatabaseField
    public String unit_price;
    @DatabaseField
    public String OWNER;
    @DatabaseField
    public String img_path;
    @DatabaseField
    public String id;
    @DatabaseField
    public String unit;
    @DatabaseField
    public String class_id;
    @DatabaseField
    public String price;
    @DatabaseField
    public String styleno;
    @DatabaseField
    public String sku_name;
    @DatabaseField
    public String is_hot;
    @DatabaseField
    public String sku_code;
    @DatabaseField
    public String uom_default;
    @DatabaseField
    public String market_unitprice;
    @DatabaseField
    public String rkpriceListJson; //价格区间
    @DatabaseField
    public String pro_num; //商品的数量
    @DatabaseField
    public String curr_price; //当前的价格


    public List<RkpriceListItemDb> rkpriceList;
    public class RkpriceListItemDb implements Serializable {
        public String qty_price;
        public String begin_qty;
        public String end_qty;

    }


}

