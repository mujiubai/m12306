package com.mujiubai.train.business.req;

import com.mujiubai.train.common.req.PageReq;

public class TrainSeatQueryReq extends PageReq {
    private String trainCode;

    @Override
    public String toString() {
        return "TrainSeatQueryReq [trainCode=" + trainCode + "]";
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }
}
