package com.team5.seeshop.models;

import java.io.Serializable;
import java.util.List;

public class ProductModel implements Serializable {

    private String seller_id,product_id,title;

    int price,quantity;

    String description,brand;
    int product_enable;

    List<RatingModel> ratingModelList;

    float average_rating;



    List<String> images;

    int hard_disk,ram,graphic_card;

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }




    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getProduct_enable() {
        return product_enable;
    }

    public void setProduct_enable(int product_enable) {
        this.product_enable = product_enable;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



    public List<RatingModel> getRatingModelList() {
        return ratingModelList;
    }

    public void setRatingModelList(List<RatingModel> ratingModelList) {
        this.ratingModelList = ratingModelList;
    }

    public float getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(float average_rating) {
        this.average_rating = average_rating;
    }


    public int getHard_disk() {
        return hard_disk;
    }

    public void setHard_disk(int hard_disk) {
        this.hard_disk = hard_disk;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public int getGraphic_card() {
        return graphic_card;
    }

    public void setGraphic_card(int graphic_card) {
        this.graphic_card = graphic_card;
    }
}
