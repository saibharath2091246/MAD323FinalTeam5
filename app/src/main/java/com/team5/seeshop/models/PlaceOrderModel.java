package com.team5.seeshop.models;

import java.io.Serializable;
import java.util.List;

public class PlaceOrderModel implements Serializable {

    private String id, order_id,user_id,user_name,total_amount,order_date,order_time,order_status;
    private List<CartModel> cartItems;

    List<String>seller_id_list;

    private  int cash_on_delivery;


    /*-----address---------*/
    String address,city,postal_code,phone_number;

    /*-------debit--------*/
    private String card,exp,cvv;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public List<CartModel> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartModel> cartItems) {
        this.cartItems = cartItems;
    }



    public int getCash_on_delivery() {
        return cash_on_delivery;
    }

    public void setCash_on_delivery(int cash_on_delivery) {
        this.cash_on_delivery = cash_on_delivery;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public List<String> getSeller_id_list() {
        return seller_id_list;
    }

    public void setSeller_id_list(List<String> seller_id_list) {
        this.seller_id_list = seller_id_list;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
