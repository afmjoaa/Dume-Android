package io.dume.dume.student.homePage.adapter;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class HomePageRecyclerData implements Serializable {
    String title;
    String description;
    String sub_description;
    Date expirity;
    Date start_date;
    Integer max_dicount_percentage;
    Integer max_discount_credit;
    Integer max_tution_count;
    String promo_image;
    String product;
    String promo_code;
    String promo_for;
    boolean expired;
    Map<String, Object> criteria;

    public HomePageRecyclerData() {
    }

    public HomePageRecyclerData(String title, String description, String sub_description, Date expirity, Date start_date, Integer max_dicount_percentage, Integer max_discount_credit, Integer max_tution_count, String promo_image, String product, String promo_code, String promo_for, Map<String, Object> criteria) {
        this.title = title;
        this.description = description;
        this.sub_description = sub_description;
        this.expirity = expirity;
        this.start_date = start_date;
        this.max_dicount_percentage = max_dicount_percentage;
        this.max_discount_credit = max_discount_credit;
        this.max_tution_count = max_tution_count;
        this.promo_image = promo_image;
        this.product = product;
        this.promo_code = promo_code;
        this.promo_for = promo_for;
        this.criteria = criteria;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
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

    public String getSub_description() {
        return sub_description;
    }

    public void setSub_description(String sub_description) {
        this.sub_description = sub_description;
    }

    public Date getExpirity() {
        return expirity;
    }

    public void setExpirity(Date expirity) {
        this.expirity = expirity;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Integer getMax_dicount_percentage() {
        return max_dicount_percentage;
    }

    public void setMax_dicount_percentage(Integer max_dicount_percentage) {
        this.max_dicount_percentage = max_dicount_percentage;
    }

    public Integer getMax_discount_credit() {
        return max_discount_credit;
    }

    public void setMax_discount_credit(Integer max_discount_credit) {
        this.max_discount_credit = max_discount_credit;
    }

    public Integer getMax_tution_count() {
        return max_tution_count;
    }

    public void setMax_tution_count(Integer max_tution_count) {
        this.max_tution_count = max_tution_count;
    }

    public String getPromo_image() {
        return promo_image;
    }

    public void setPromo_image(String promo_image) {
        this.promo_image = promo_image;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPromo_code() {
        return promo_code;
    }

    public void setPromo_code(String promo_code) {
        this.promo_code = promo_code;
    }

    public String getPromo_for() {
        return promo_for;
    }

    public void setPromo_for(String promo_for) {
        this.promo_for = promo_for;
    }

    public Map<String, Object> getCriteria() {
        return criteria;
    }

    public void setCriteria(Map<String, Object> criteria) {
        this.criteria = criteria;
    }
}
