package com.acpay.acapytrade.costs;

public class costs {
    private String orderNum;
    private String amount;
    private String details;
    private String date;
    public costs(String orderNum,String amount, String details, String date) {
        this.orderNum=orderNum;
         this.details=details;
         this.amount=amount;
         this.date =date;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
