package com.mujiubai.train.member.controller;


import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mujiubai.train.member.req.PassengerQueryReq;
import com.mujiubai.train.member.req.PassengerSaveReq;
import com.mujiubai.train.member.resp.MemberLoginResp;
import com.mujiubai.train.member.resp.PassengerQueryResp;
import com.mujiubai.train.member.service.PassengerService;
import com.mujiubai.train.common.context.LoginMemberContext;
import com.mujiubai.train.common.resp.CommonResp;
import com.mujiubai.train.common.resp.PageResp;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    @Resource
    private PassengerService passengerService;

    @PostMapping("/save")
    public CommonResp<MemberLoginResp> login(@Valid @RequestBody PassengerSaveReq req){
        
        CommonResp<MemberLoginResp> commonResp=new CommonResp<>();
        passengerService.save(req);
        return commonResp;
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<PassengerQueryResp>> queryList(@Valid PassengerQueryReq req){        
        CommonResp<PageResp<PassengerQueryResp>> commonResp=new CommonResp<>();
        req.setMemberId(LoginMemberContext.getId());
        commonResp.setContent(passengerService.queryList(req));;
        return commonResp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id){
        passengerService.delete(id);
        return new CommonResp<>();
    }

    @GetMapping("/query-mine")
    public CommonResp<List<PassengerQueryResp>> queryMine() {
        List<PassengerQueryResp> list = passengerService.queryMine();
        return new CommonResp<>(list);
    }

}
