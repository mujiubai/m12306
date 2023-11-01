package com.mujiubai.train.member.service;

import java.util.Date;
import java.util.List;

import org.bouncycastle.pqc.math.linearalgebra.RandUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mujiubai.train.common.aspect.LogAspect;
import com.mujiubai.train.common.execption.BussinessExecption;
import com.mujiubai.train.common.execption.BussinessExecptionEnum;
import com.mujiubai.train.common.util.JwtUtil;
import com.mujiubai.train.common.util.SnowUtil;
import com.mujiubai.train.member.domain.Member;
import com.mujiubai.train.member.domain.MemberExample;
import com.mujiubai.train.member.domain.Passenger;
import com.mujiubai.train.member.mapper.MemberMapper;
import com.mujiubai.train.member.mapper.PassengerMapper;
import com.mujiubai.train.member.req.MemberLoginReq;
import com.mujiubai.train.member.req.MemberRegisterReq;
import com.mujiubai.train.member.req.MemberSendCodeReq;
import com.mujiubai.train.member.req.PassengerSaveReq;
import com.mujiubai.train.member.resp.MemberLoginResp;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;

@Service
public class PassengerService {
    private final static Logger LOG = LoggerFactory.getLogger(PassengerService.class);

    @Resource
    private PassengerMapper passengerMapper;

    public void save(PassengerSaveReq req){
        DateTime now=DateTime.now();
        Passenger passenger=BeanUtil.copyProperties(req, Passenger.class);
        passenger.setId(SnowUtil.getSnowFlakeNextId());
        passenger.setCreateTime(now);
        passenger.setUpdateTime(now);
        passengerMapper.insert(passenger);
    }
}