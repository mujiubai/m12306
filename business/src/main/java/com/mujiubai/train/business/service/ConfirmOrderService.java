package com.mujiubai.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mujiubai.train.common.context.LoginMemberContext;
import com.mujiubai.train.common.execption.BussinessExecption;
import com.mujiubai.train.common.execption.BussinessExecptionEnum;
import com.mujiubai.train.common.resp.PageResp;
import com.mujiubai.train.common.util.SnowUtil;
import com.mujiubai.train.business.domain.ConfirmOrder;
import com.mujiubai.train.business.domain.ConfirmOrderExample;
import com.mujiubai.train.business.domain.DailyTrainTicket;
import com.mujiubai.train.business.enums.ConfirmOrderStatusEnum;
import com.mujiubai.train.business.enums.SeatTypeEnum;
import com.mujiubai.train.business.mapper.ConfirmOrderMapper;
import com.mujiubai.train.business.req.ConfirmOrderDoReq;
import com.mujiubai.train.business.req.ConfirmOrderQueryReq;
import com.mujiubai.train.business.resp.ConfirmOrderQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfirmOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmOrderService.class);

    @Resource
    private ConfirmOrderMapper confirmOrderMapper;

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    public void save(ConfirmOrderDoReq req) {
        DateTime now = DateTime.now();
        ConfirmOrder confirmOrder = BeanUtil.copyProperties(req, ConfirmOrder.class);
        if (ObjectUtil.isNull(confirmOrder.getId())) {
            confirmOrder.setId(SnowUtil.getSnowFlakeNextId());
            confirmOrder.setCreateTime(now);
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.insert(confirmOrder);
        } else {
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.updateByPrimaryKey(confirmOrder);
        }
    }

    public PageResp<ConfirmOrderQueryResp> queryList(ConfirmOrderQueryReq req) {
        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        confirmOrderExample.setOrderByClause("id desc");
        ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();

        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<ConfirmOrder> confirmOrderList = confirmOrderMapper.selectByExample(confirmOrderExample);

        PageInfo<ConfirmOrder> pageInfo = new PageInfo<>(confirmOrderList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<ConfirmOrderQueryResp> list = BeanUtil.copyToList(confirmOrderList, ConfirmOrderQueryResp.class);

        PageResp<ConfirmOrderQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        confirmOrderMapper.deleteByPrimaryKey(id);
    }

    public void doConfirm(ConfirmOrderDoReq req) {
        // 省略业务数据校验，如：车次是否存在，余票是否存在，车次是否在有效期内，tickets条数>0，同乘客同车次是否已买过

        // 保存初始化订单
        DateTime now = DateTime.now();
        ConfirmOrder confirmOrder = new ConfirmOrder();
        confirmOrder.setCreateTime(now);
        confirmOrder.setUpdateTime(now);
        confirmOrder.setDailyTrainTicketId(req.getDailyTrainTicketId());
        confirmOrder.setDate(req.getDate());
        confirmOrder.setEnd(req.getEnd());
        confirmOrder.setId(SnowUtil.getSnowFlakeNextId());
        confirmOrder.setMemberId(LoginMemberContext.getId());
        confirmOrder.setStart(req.getStart());
        confirmOrder.setStatus(ConfirmOrderStatusEnum.INIT.getCode());
        confirmOrder.setTickets(com.alibaba.fastjson.JSON.toJSONString(req.getTickets()));
        confirmOrder.setTrainCode(req.getTrainCode());
        confirmOrderMapper.insert(confirmOrder);

        // 查询余票
        DailyTrainTicket dailyTrainTicket = dailyTrainTicketService.selectByUnique(req.getDate(), req.getTrainCode(),
                req.getStart(),
                req.getEnd());
        LOG.info("查询余票：{}", dailyTrainTicket);

        //预减余票，并判断是否足够
        ticketReduce(req, dailyTrainTicket);

    }

    private void ticketReduce(ConfirmOrderDoReq req, DailyTrainTicket dailyTrainTicket) {
        for (var ticket : req.getTickets()) {
            var seatTypeEnum = EnumUtil.getBy(SeatTypeEnum::getCode, ticket.getSeatTypeCode());
            switch (seatTypeEnum) {
                case YDZ -> {
                    int seatLeft = dailyTrainTicket.getYdz() - 1;
                    if (seatLeft < 0) {
                        throw new BussinessExecption(BussinessExecptionEnum.CONFIRM_ORDER_SEAT_ERROE);
                    }
                    dailyTrainTicket.setYdz(seatLeft);
                }
                case EDZ -> {
                    int seatLeft = dailyTrainTicket.getEdz() - 1;
                    if (seatLeft < 0) {
                        throw new BussinessExecption(BussinessExecptionEnum.CONFIRM_ORDER_SEAT_ERROE);
                    }
                    dailyTrainTicket.setEdz(seatLeft);
                }
                case YW -> {
                    int seatLeft = dailyTrainTicket.getYw() - 1;
                    if (seatLeft < 0) {
                        throw new BussinessExecption(BussinessExecptionEnum.CONFIRM_ORDER_SEAT_ERROE);
                    }
                    dailyTrainTicket.setYw(seatLeft);
                }
                case RW -> {
                    int seatLeft = dailyTrainTicket.getRw() - 1;
                    if (seatLeft < 0) {
                        throw new BussinessExecption(BussinessExecptionEnum.CONFIRM_ORDER_SEAT_ERROE);
                    }
                    dailyTrainTicket.setRw(seatLeft);
                }
            }
        }
    }
}
