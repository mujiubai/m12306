package com.mujiubai.train.common.execption;

public enum BussinessExecptionEnum {

    MEMBER_MOBILE_EXIST("手机号已注册"),
    MEMBER_MOBILE_NOT_EXIST("手机号不存在"),
    MEMBER_CODE_ERROR("验证码错误"),

    BUSINESS_STATION_EXIST("车站已存在"),
    BUSINESS_TRAIN_CODE_UNIQUE_ERROR("车次编号已存在"),
    BUSINESS_TRAIN_STATION_INDEX_UNIQUE_ERROR("同车次站序已存在"),
    BUSINESS_TRAIN_STATION_NAME_UNIQUE_ERROR("同车次站名已存在"),
    BUSINESS_TRAIN_CARRIAGE_INDEX_UNIQUE_ERROR("同车次厢号已存在"),

    CONFIRM_ORDER_SEAT_ERROE("余票不足");

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
        return super.toString();
    }

}
