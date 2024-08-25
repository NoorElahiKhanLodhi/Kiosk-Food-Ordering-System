package com.example.kioskapp;

public class ItemModel {


    // string course_name for storing course_name
    // and imgid for storing image id.
    private String imgid;
    private String item_name;
    private String item_price;
    private String item_dis;
    private String item_unit;
    private String item_des;
    private String item_id;

    public ItemModel(String imgid, String item_name, String item_price,  String item_dis, String item_unit,  String item_des, String item_id) {
        this.imgid = imgid;
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_dis = item_dis;
        this.item_des = item_des;
        this.item_id = item_id;
        this.item_unit = item_unit;
    }


    public String getImgid() {
        return imgid;
    }

    public void setImgid(String imgid) {
        this.imgid = imgid;
    }

    public String get_name() {
        return item_name;
    }

    public void set_name(String item_name) {
        this.item_name = item_name;
    }

    public String get_price() {
        return item_price;
    }

    public void set_price(String item_price) {
        this.item_price = item_price;
    }


    public String get_dis() {
        return item_dis;
    }

    public void set_dis(String item_dis) {
        this.item_dis = item_dis;
    }

    public String get_unit() {
        return item_unit;
    }

    public void set_unit(String item_unit) {
        this.item_unit = item_unit;
    }


    public String get_des() {
        return item_des;
    }

    public void set_des(String item_des) {
        this.item_des = item_des;
    }

    public String get_id() {
        return item_id;
    }

    public void set_id(String item_id) {
        this.item_id = item_id;
    }

}
