package com.mujiubai.train.business.controller.admin;

import com.mujiubai.train.common.resp.CommonResp;
import com.mujiubai.train.common.resp.PageResp;
import com.mujiubai.train.business.req.DailyTrainQueryReq;
import com.mujiubai.train.business.req.DailyTrainSaveReq;
import com.mujiubai.train.business.resp.DailyTrainQueryResp;
import com.mujiubai.train.business.service.DailyTrainService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/daily-train")
public class DailyTrainAdminController {
    @Resource
    private DailyTrainService dailyTrainService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody DailyTrainSaveReq req) {
        dailyTrainService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainQueryResp>> queryList(@Valid DailyTrainQueryReq req) {
        PageResp<DailyTrainQueryResp> list = dailyTrainService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        dailyTrainService.delete(id);
        return new CommonResp<>();
    }
}
