package com.mujiubai.train.business.req;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.mujiubai.train.common.req.PageReq;

public class DailyTrainCarriageQueryReq extends PageReq {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String trainCode;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    @Override
    public String toString() {
        return "DailyTrainCarriageQueryReq [date=" + date + ", trainCode=" + trainCode + "]";
    }
}
