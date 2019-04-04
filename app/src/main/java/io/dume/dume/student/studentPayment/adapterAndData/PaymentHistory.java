package io.dume.dume.student.studentPayment.adapterAndData;

import java.util.Date;

public class PaymentHistory {
    private String amount;
    private String uid;
    private String status;
    private Date transection_date;
    private String transection_id;
    private String payment_method;

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public PaymentHistory() {
    }

    public PaymentHistory(String amount, String uid, String status, Date transection_date, String transection_id) {
        this.amount = amount;
        this.uid = uid;
        this.status = status;
        this.transection_date = transection_date;
        this.transection_id = transection_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTransection_date() {
        return transection_date;
    }

    public void setTransection_date(Date transection_date) {
        this.transection_date = transection_date;
    }

    public String getTransection_id() {
        return transection_id;
    }

    public void setTransection_id(String transection_id) {
        this.transection_id = transection_id;
    }
}
