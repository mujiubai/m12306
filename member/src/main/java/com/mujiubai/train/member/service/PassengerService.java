package com.mujiubai.train.member.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mujiubai.train.common.context.LoginMemberContext;
import com.mujiubai.train.common.resp.PageResp;
import com.mujiubai.train.common.util.SnowUtil;
import com.mujiubai.train.member.domain.Passenger;
import com.mujiubai.train.member.domain.PassengerExample;
import com.mujiubai.train.member.domain.PassengerExample.Criteria;
import com.mujiubai.train.member.mapper.PassengerMapper;
import com.mujiubai.train.member.req.PassengerQueryReq;
import com.mujiubai.train.member.req.PassengerSaveReq;
import com.mujiubai.train.member.resp.PassengerQueryResp;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import jakarta.annotation.Resource;

@Service
public class PassengerService {
    private final static Logger LOG = LoggerFactory.getLogger(PassengerService.class);

    @Resource
    private PassengerMapper passengerMapper;

    public void save(PassengerSaveReq req) {
        DateTime now = DateTime.now();
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);
        passenger.setUpdateTime(now);
        // id存在则说明是更新乘车人，否则是插入新乘车人
        if (passenger.getId() == null) {
            passenger.setMemberId(LoginMemberContext.getMember().getId());
            passenger.setId(SnowUtil.getSnowFlakeNextId());
            passenger.setCreateTime(now);
            passengerMapper.insert(passenger);
        } else {
            passengerMapper.updateByPrimaryKey(passenger);
        }

    }

    public PageResp<PassengerQueryResp> queryList(PassengerQueryReq req) {
        PassengerExample passengerExample = new PassengerExample();
        Criteria criteria = passengerExample.createCriteria();
        if (req.getMemberId() != null) {
            criteria.andMemberIdEqualTo(req.getMemberId());
        }
        passengerExample.setOrderByClause("id desc");
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Passenger> listPassenger = passengerMapper.selectByExample(passengerExample);
        List<PassengerQueryResp> list = BeanUtil.copyToList(listPassenger, PassengerQueryResp.class);
        PageInfo<Passenger> pageInfo = new PageInfo<>(listPassenger);
        PageResp<PassengerQueryResp> resp = new PageResp<>();
        resp.setList(list);
        resp.setTotal(pageInfo.getTotal());

        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());
        return resp;
    }

    public void delete(Long id) {
        passengerMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询我的所有乘客
     */
    public List<PassengerQueryResp> queryMine() {
        PassengerExample passengerExample = new PassengerExample();
        passengerExample.setOrderByClause("name asc");
        PassengerExample.Criteria criteria = passengerExample.createCriteria();
        criteria.andMemberIdEqualTo(LoginMemberContext.getId());
        List<Passenger> list = passengerMapper.selectByExample(passengerExample);
        return BeanUtil.copyToList(list, PassengerQueryResp.class);
    }
}
