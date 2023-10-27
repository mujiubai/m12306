package com.mujiubai.train.common.execption;

public class BussinessExecption extends RuntimeException{
    BussinessExecptionEnum e;

    public BussinessExecptionEnum getE() {
        return e;
    }

    public void setE(BussinessExecptionEnum e) {
        this.e = e;
    }

    public BussinessExecption(BussinessExecptionEnum e) {
        this.e = e;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        // TODO Auto-generated method stub
        return this;
    }
    
}
