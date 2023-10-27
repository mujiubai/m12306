package com.mujiubai.train.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mujiubai.train.common.execption.BussinessExecption;
import com.mujiubai.train.common.execption.BussinessExecptionEnum;
import com.mujiubai.train.member.domain.Member;
import com.mujiubai.train.member.domain.MemberExample;
import com.mujiubai.train.member.mapper.MemberMapper;

import jakarta.annotation.Resource;

@Service
public class MemberService {
    @Resource
    private MemberMapper memberMapper;

    public int count(){
        return (int) memberMapper.countByExample(null);
    }

    public long register(String mobile){
        MemberExample memberExample=new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list=memberMapper.selectByExample(memberExample);
        if(!list.isEmpty()){
            throw new BussinessExecption(BussinessExecptionEnum.MEMBER_MOBILE_EXIST);
        }
        Member member=new Member();
        member.setId(System.currentTimeMillis());
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }
}
