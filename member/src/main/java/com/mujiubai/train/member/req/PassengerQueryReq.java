package com.mujiubai.train.member.req;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;

public class PassengerQueryReq {

    private Long memberId;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "PassengerQueryReq [memberId=" + memberId + "]";
    }

    
}