package com.mujiubai.train.business.controller;

import com.mujiubai.train.common.resp.CommonResp;
import com.mujiubai.train.business.resp.StationQueryResp;
import com.mujiubai.train.business.service.StationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/station")
public class StationController {
    @Resource
    private StationService stationService;

    @GetMapping("/query-all")
    public CommonResp<List<StationQueryResp>> queryList() {
        List<StationQueryResp> list = stationService.queryAll();
        return new CommonResp<>(list);
    }
}
