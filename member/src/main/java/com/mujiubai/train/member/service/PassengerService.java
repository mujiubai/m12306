package com.mujiubai.train.member.service;

import java.util.Date;
import java.util.List;

import org.bouncycastle.pqc.math.linearalgebra.RandUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mujiubai.train.common.aspect.LogAspect;
import com.mujiubai.train.common.context.LoginMemberContext;
import com.mujiubai.train.common.execption.BussinessExecption;
import com.mujiubai.train.common.execption.BussinessExecptionEnum;
import com.mujiubai.train.common.resp.PageResp;
import com.mujiubai.train.common.util.JwtUtil;
import com.mujiubai.train.common.util.SnowUtil;
import com.mujiubai.train.member.domain.Member;
import com.mujiubai.train.member.domain.MemberExample;
import com.mujiubai.train.member.domain.Passenger;
import com.mujiubai.train.member.domain.PassengerExample;
import com.mujiubai.train.member.domain.PassengerExample.Criteria;
import com.mujiubai.train.member.mapper.MemberMapper;
import com.mujiubai.train.member.mapper.PassengerMapper;
import com.mujiubai.train.member.req.MemberLoginReq;
import com.mujiubai.train.member.req.MemberRegisterReq;
import com.mujiubai.train.member.req.MemberSendCodeReq;
import com.mujiubai.train.member.req.PassengerQueryReq;
import com.mujiubai.train.member.req.PassengerSaveReq;
import com.mujiubai.train.member.resp.MemberLoginResp;
import com.mujiubai.train.member.resp.PassengerQueryResp;

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
        passenger.setMemberId(LoginMemberContext.getMember().getId());
        passenger.setId(SnowUtil.getSnowFlakeNextId());
        passenger.setCreateTime(now);
        passenger.setUpdateTime(now);
        passengerMapper.insert(passenger);
    }

    public PageResp<PassengerQueryResp> quertList(PassengerQueryReq req){
        PassengerExample passengerExample=new PassengerExample();
        Criteria criteria=passengerExample.createCriteria();
        if(req.getMemberId()!=null){
            criteria.andMemberIdEqualTo(req.getMemberId());
        }
        passengerExample.setOrderByClause("id desc");
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Passenger> listPassenger=passengerMapper.selectByExample(passengerExample);
        List<PassengerQueryResp> list=BeanUtil.copyToList(listPassenger,PassengerQueryResp.class);
        PageInfo<Passenger> pageInfo=new PageInfo<>(listPassenger);
        PageResp<PassengerQueryResp> resp=new PageResp<>();
        resp.setList(list);
        resp.setTotal(pageInfo.getTotal());

        LOG.info("总行数：{}",pageInfo.getTotal());
        LOG.info("总页数：{}",pageInfo.getPages());
        return resp;
    }
}
