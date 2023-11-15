package com.mujiubai.train.business.req;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mujiubai.train.common.req.PageReq;

public class DailyTrainTicketQueryReq extends PageReq {

    private String trainCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String startStation;

    private String endStation;

    public String getTrainCode() {
        return trainCode;
    }

    @Override
    public String toString() {
        return "DailyTrainTicketQueryReq [trainCode=" + trainCode + ", date=" + date + ", startStation=" + startStation
                + ", endStation=" + endStation + "]";
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    
}
