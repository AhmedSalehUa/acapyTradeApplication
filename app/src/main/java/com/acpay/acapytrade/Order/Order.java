package com.acpay.acapytrade.Order;

import android.opengl.Visibility;
import android.view.View;


public class Order {
    private String orderNum;
 private String userName;
    private String Date;
    private String Time;
    private String Place;
    private String location;
    private String fixType;
    private String classMatter;
    private String DliverCost;
    private String notes;
    private String file;

    private View.OnClickListener PendingBtnClickListener;
    private View.OnClickListener addNotesBtnClickListener;
    private View.OnClickListener EnableBtnClickListener;
    private View.OnClickListener DeleteBtnClickListener;

    private Visibility requestBtn;
    private Visibility addNotes;
    private Visibility EnableBtn;
    private Visibility DeleteBtn;

    public View.OnClickListener getAddNotesBtnClickListener() {
        return addNotesBtnClickListener;
    }

    public void setAddNotesBtnClickListener(View.OnClickListener addNotesBtnClickListener) {
        this.addNotesBtnClickListener = addNotesBtnClickListener;
    }

    public View.OnClickListener getEnableBtnClickListener() {
        return EnableBtnClickListener;
    }

    public void setEnableBtnClickListener(View.OnClickListener enableBtnClickListener) {
        EnableBtnClickListener = enableBtnClickListener;
    }

    public View.OnClickListener getDeleteBtnClickListener() {
        return DeleteBtnClickListener;
    }

    public void setDeleteBtnClickListener(View.OnClickListener deleteBtnClickListener) {
        DeleteBtnClickListener = deleteBtnClickListener;
    }

    public Visibility getRequestBtn() {
        return requestBtn;
    }

    public void setRequestBtn(Visibility requestBtn) {
        this.requestBtn = requestBtn;
    }

    public Visibility getAddNotes() {
        return addNotes;
    }

    public void setAddNotes(Visibility addNotes) {
        this.addNotes = addNotes;
    }

    public Visibility getEnableBtn() {
        return EnableBtn;
    }

    public void setEnableBtn(Visibility enableBtn) {
        EnableBtn = enableBtn;
    }

    public Visibility getDeleteBtn() {
        return DeleteBtn;
    }

    public void setDeleteBtn(Visibility deleteBtn) {
        DeleteBtn = deleteBtn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public Order(String orderNum, String Date, String Time, String Place, String location, String fixType, String classMatter, String DliverCost, String notes, String file,String username) {
        this.orderNum = orderNum;
        this.Date = Date;
        this.Time = Time;
        this.Place = Place;
        this.location = location;
        this.fixType = fixType;
        this.classMatter = classMatter;
        this.DliverCost = DliverCost;
        this.notes = notes;
        this.file = file;
        this.userName=username;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFixType() {
        return fixType;
    }

    public void setFixType(String fixType) {
        this.fixType = fixType;
    }

    public String getClassMatter() {
        return classMatter;
    }

    public void setClassMatter(String classMatter) {
        this.classMatter = classMatter;
    }

    public String getDliverCost() {
        return DliverCost;
    }

    public void setDliverCost(String dliverCost) {
        DliverCost = dliverCost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public View.OnClickListener getPendingBtnClickListener() {
        return PendingBtnClickListener;
    }

    public void setPendingBtnClickListener(View.OnClickListener pendingBtnClickListener) {
        this.PendingBtnClickListener = pendingBtnClickListener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order item = (Order) o;

        if (orderNum != null ? !orderNum.equals(item.orderNum) : item.orderNum != null) {
            return false;
        }
        if (Date != null ? !Date.equals(item.Date) : item.Date != null) {
            return false;
        }
        if (Time != null ? !Time.equals(item.Time) : item.Time != null) {
            return false;
        }
        if (Place != null ? !Place.equals(item.Place) : item.Place != null) {
            return false;
        }
        if (location != null ? !location.equals(item.location) : item.location != null) {
            return false;
        }
        if (DliverCost != null ? !DliverCost.equals(item.DliverCost) : item.DliverCost != null) {
            return false;
        }
        if (notes != null ? !notes.equals(item.notes) : item.notes != null) {
            return false;
        }

        if (classMatter != null ? !classMatter.equals(item.classMatter) : item.classMatter != null) {
            return false;
        }
        if (file != null ? !file.equals(item.file) : item.file != null) {
            return false;
        }
        return !(fixType != null ? !fixType.equals(item.fixType) : item.fixType != null);

    }

    @Override
    public int hashCode() {
        int result = orderNum != null ? orderNum.hashCode() : 0;
        result = 31 * result + (Date != null ? Date.hashCode() : 0);
        result = 31 * result + (Time != null ? Time.hashCode() : 0);
        result = 31 * result + (Place != null ? Place.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (DliverCost != null ? DliverCost.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (classMatter != null ? notes.hashCode() : 0);
        result = 31 * result + (file != null ? notes.hashCode() : 0);
        result = 31 * result + (fixType != null ? notes.hashCode() : 0);
        return result;
    }

}
