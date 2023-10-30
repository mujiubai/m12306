package com.mujiubai.train.member.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mujiubai.train.member.req.MemberLoginReq;
import com.mujiubai.train.member.req.MemberRegisterReq;
import com.mujiubai.train.member.req.MemberSendCodeReq;
import com.mujiubai.train.member.resp.MemberLoginResp;
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
        
        CommonResp<Long> commonResp=new CommonResp<>();
        commonResp.setContent(memberService.register(req));
        return commonResp;
    }

    @PostMapping("/send-code")
    public CommonResp<Long> sendCode(@Valid MemberSendCodeReq req){
        
        CommonResp<Long> commonResp=new CommonResp<>();
        memberService.sendCode(req);
        return commonResp;
    }

    @PostMapping("/login")
    public CommonResp<MemberLoginResp> login(@Valid MemberLoginReq req){
        
        CommonResp<MemberLoginResp> commonResp=new CommonResp<>();
        commonResp.setContent(memberService.login(req));
        return commonResp;
    }

}
