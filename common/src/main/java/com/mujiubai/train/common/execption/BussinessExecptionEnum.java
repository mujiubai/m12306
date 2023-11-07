package com.mujiubai.train.common.execption;

public enum BussinessExecptionEnum {

    MEMBER_MOBILE_EXIST("手机号已注册"),
    MEMBER_MOBILE_NOT_EXIST("手机号不存在"),
    MEMBER_CODE_ERROR("验证码错误"),

    BUSINESS_STATION_EXIST("车站已存在"),;
    
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
