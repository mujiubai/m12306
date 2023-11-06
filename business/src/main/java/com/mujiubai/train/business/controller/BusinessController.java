package com.mujiubai.train.business.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mujiubai.train.common.resp.CommonResp;


import jakarta.annotation.Resource;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/business")
public class BusinessController {

    // @Resource
    // private BusinessController memberService;

    @GetMapping("/hello")
    public CommonResp<String> hello() {
        CommonResp<String> commonResp=new CommonResp<>();
        commonResp.setContent("hello world! business");
        return commonResp;
    }

}
