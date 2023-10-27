package com.mujiubai.train.member.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mujiubai.train.member.req.MemberRegisterReq;
import com.mujiubai.train.member.service.MemberService;
import com.mujiubai.train.common.resp.CommonResp;


import jakarta.annotation.Resource;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private MemberService memberService;

    @GetMapping("/hello")
    public CommonResp<String> hello() {
        CommonResp<String> commonResp=new CommonResp<>();
        commonResp.setContent("hello world123");
        return commonResp;
    }

    @GetMapping("/count")
    public CommonResp<Integer> count(){
        CommonResp<Integer> commonResp=new CommonResp<>();
        commonResp.setContent(memberService.count());
        return commonResp;
    }

    @PostMapping("/register")
    public CommonResp<Long> register(@Valid MemberRegisterReq req){
        String mobile=req.getMobile();
        CommonResp<Long> commonResp=new CommonResp<>();
        commonResp.setContent(memberService.register(mobile));
        return commonResp;
    }

}
