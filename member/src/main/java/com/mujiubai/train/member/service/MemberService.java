package com.mujiubai.train.member.service;

import java.util.List;

import org.bouncycastle.pqc.math.linearalgebra.RandUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mujiubai.train.common.aspect.LogAspect;
import com.mujiubai.train.common.execption.BussinessExecption;
import com.mujiubai.train.common.execption.BussinessExecptionEnum;
import com.mujiubai.train.common.util.SnowUtil;
import com.mujiubai.train.member.domain.Member;
import com.mujiubai.train.member.domain.MemberExample;
import com.mujiubai.train.member.mapper.MemberMapper;
import com.mujiubai.train.member.req.MemberLoginReq;
import com.mujiubai.train.member.req.MemberRegisterReq;
import com.mujiubai.train.member.req.MemberSendCodeReq;
import com.mujiubai.train.member.resp.MemberLoginResp;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;

@Service
public class MemberService {
    private final static Logger LOG = LoggerFactory.getLogger(MemberService.class);

    @Resource
    private MemberMapper memberMapper;

    public int count() {
        return (int) memberMapper.countByExample(null);
    }

    public long register(MemberRegisterReq req) {
        String mobile = req.getMobile();
        Member memberDB = selectByMoblie(mobile);
        if (memberDB!=null) {
            throw new BussinessExecption(BussinessExecptionEnum.MEMBER_MOBILE_EXIST);
        }
        Member member = new Member();
        member.setId(SnowUtil.getSnowFlakeNextId());
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }

    public void sendCode(MemberSendCodeReq req) {
        String mobile = req.getMobile();
        Member memberDB = selectByMoblie(mobile);

        //手机号不存在，插入手机号
        if (memberDB==null) {
            Member member = new Member();
            member.setId(SnowUtil.getSnowFlakeNextId());
            member.setMobile(mobile);
            memberMapper.insert(member);
            LOG.info("手机号不存在，插入手机号");
        }else{
            LOG.info("手机号已存在");
        }

        //生成验证码
        String code=RandomUtil.randomString(4);
        code="1234";
        LOG.info("验证码：{}",code);

        //保存短信记录表：手机号、验证码、有效期、业务类型、是否已使用、发送时间、使用时间
        LOG.info("保存短信记录表");

        //对接短信通道
        LOG.info("对接短信通道");
    }

    public MemberLoginResp login(MemberLoginReq req) {
        String mobile = req.getMobile();
        Member memberDB = selectByMoblie(mobile);

        //手机号不存在
        if (memberDB==null) {
            throw new BussinessExecption(BussinessExecptionEnum.MEMBER_MOBILE_NOT_EXIST);
        }

        if(!"1234".equals(req.getCode())){
            throw new BussinessExecption(BussinessExecptionEnum.MEMBER_CODE_ERROR);
        }
        
        LOG.info("通过验证码登录成功！");
        return BeanUtil.copyProperties(memberDB, MemberLoginResp.class);
    }

    private Member selectByMoblie(String mobile) {
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);
        if (list.isEmpty()) {
            return null;
        }else{
            return list.get(0);
        }
    }
}
