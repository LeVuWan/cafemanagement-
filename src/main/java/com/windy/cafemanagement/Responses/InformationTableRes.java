package com.windy.cafemanagement.Responses;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class InformationTableRes {
    private String nameCustomer;
    private LocalTime orderTime;
    private LocalDate orderDate;
    private List<InfoMenuRes> infoMenuRes;

    public InformationTableRes(String nameCustomer, LocalTime orderTime, LocalDate orderDate,
            List<InfoMenuRes> infoMenuRes) {
        this.nameCustomer = nameCustomer;
        this.orderTime = orderTime;
        this.orderDate = orderDate;
        this.infoMenuRes = infoMenuRes;
    }

    public InformationTableRes() {
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public LocalTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalTime orderTime) {
        this.orderTime = orderTime;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public List<InfoMenuRes> getInfoMenuRes() {
        return infoMenuRes;
    }

    public void setInfoMenuRes(List<InfoMenuRes> infoMenuRes) {
        this.infoMenuRes = infoMenuRes;
    }

    @Override
    public String toString() {
        return "InformationTableRes [nameCustomer=" + nameCustomer + ", orderTime=" + orderTime + ", orderDate="
                + orderDate + ", infoMenuRes=" + infoMenuRes + "]";
    }

}
