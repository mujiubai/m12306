package com.mujiubai.train.member.service;

import org.springframework.stereotype.Service;

import com.mujiubai.train.member.mapper.MemberMapper;

import jakarta.annotation.Resource;

@Service
public class MemberService {
    @Resource
    private MemberMapper memberMapper;
    public int count(){
        return memberMapper.count();
    }
}
