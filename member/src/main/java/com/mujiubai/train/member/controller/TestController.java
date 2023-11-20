package com.mujiubai.train.member.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RefreshScope
public class TestController {

    @Value("${test.nacos}")
    private String testNacos;

    @GetMapping("/helloa")
    public CommonResp<String> hello() {
        CommonResp<String> commonResp = new CommonResp<>();
        commonResp.setContent("hello " + testNacos);
        return commonResp;
    }

}
