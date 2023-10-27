package com.mujiubai.train.common.execption;

public enum BussinessExecptionEnum {

    MEMBER_MOBILE_EXIST("手机号已注册");
    
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private BussinessExecptionEnum(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }

    
}
