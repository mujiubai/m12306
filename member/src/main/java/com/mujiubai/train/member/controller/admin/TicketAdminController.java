package com.mujiubai.train.member.controller.admin;

import com.mujiubai.train.common.resp.CommonResp;
import com.mujiubai.train.common.resp.PageResp;
import com.mujiubai.train.member.req.TicketQueryReq;
import com.mujiubai.train.member.req.TicketSaveReq;
import com.mujiubai.train.member.resp.TicketQueryResp;
import com.mujiubai.train.member.service.TicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/ticket")
public class TicketAdminController {
    @Resource
    private TicketService ticketService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody TicketSaveReq req) {
        ticketService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TicketQueryResp>> queryList(@Valid TicketQueryReq req) {
        PageResp<TicketQueryResp> list = ticketService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        ticketService.delete(id);
        return new CommonResp<>();
    }
}
