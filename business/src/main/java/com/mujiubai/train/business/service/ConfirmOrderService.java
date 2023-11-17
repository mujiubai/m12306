package com.mujiubai.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mujiubai.train.common.context.LoginMemberContext;
import com.mujiubai.train.common.execption.BussinessExecption;
import com.mujiubai.train.common.execption.BussinessExecptionEnum;
import com.mujiubai.train.common.resp.PageResp;
import com.mujiubai.train.common.util.SnowUtil;
import com.mujiubai.train.business.domain.ConfirmOrder;
import com.mujiubai.train.business.domain.ConfirmOrderExample;
import com.mujiubai.train.business.domain.DailyTrainSeat;
import com.mujiubai.train.business.domain.DailyTrainTicket;
import com.mujiubai.train.business.enums.ConfirmOrderStatusEnum;
import com.mujiubai.train.business.enums.SeatColEnum;
import com.mujiubai.train.business.enums.SeatTypeEnum;
import com.mujiubai.train.business.mapper.ConfirmOrderMapper;
import com.mujiubai.train.business.req.ConfirmOrderDoReq;
import com.mujiubai.train.business.req.ConfirmOrderQueryReq;
import com.mujiubai.train.business.resp.ConfirmOrderQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ConfirmOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmOrderService.class);

    @Resource
    private ConfirmOrderMapper confirmOrderMapper;

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

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

        // 预减余票，并判断是否足够
        ticketReduce(req, dailyTrainTicket);

        // 判断是否选座，若选座则生成所有座位相对于第一个座位的偏移值数组
        if (StrUtil.isNotBlank(req.getTickets().get(0).getSeat())) {
            LOG.info("用户设置选座");
            List<SeatColEnum> seatColEnums = SeatColEnum.getColsByType(req.getTickets().get(0).getSeatTypeCode());
            LOG.info("本次选座的列：{}", seatColEnums);
            List<String> refSeatList = new ArrayList<>();
            for (int i = 1; i <= 2; ++i) {
                for (var seat : seatColEnums) {
                    refSeatList.add(seat.getCode() + i);
                }
            }
            LOG.info("生成的参考座位：{}", refSeatList);
            List<Integer> offsetList = new ArrayList<>();
            int refPointer = 0, seatPointer = 0;
            while (refPointer < refSeatList.size() && seatPointer < req.getTickets().size()) {
                if (req.getTickets().get(seatPointer).getSeat().equals(refSeatList.get(refPointer))) {
                    offsetList.add(refPointer);
                    ++seatPointer;
                }
                ++refPointer;
            }
            LOG.info("各座位绝对偏移值：{}", offsetList);
            for (int i = offsetList.size() - 1; i >= 0; --i) {
                offsetList.set(i, offsetList.get(i) - offsetList.get(0));
            }
            LOG.info("各座位相对偏移值：{}", offsetList);

            getSeat(req.getDate(), req.getTrainCode(), req.getTickets().get(0).getSeatTypeCode(),
                    req.getTickets().get(0).getSeat().split("")[0], offsetList, dailyTrainTicket.getStartIndex(),
                    dailyTrainTicket.getEndIndex());
        } else {
            LOG.info("用户未设置选座");
            for (var ticketRq : req.getTickets()) {
                getSeat(req.getDate(), req.getTrainCode(), ticketRq.getSeatTypeCode(),
                        null, null, dailyTrainTicket.getStartIndex(), dailyTrainTicket.getEndIndex());
            }
        }

    }

    private void getSeat(Date date, String trainCode, String seatType, String column, List<Integer> offsetList,
            int startIndex, int endIndex) {
        var carriageList = dailyTrainCarriageService.selectByCarriage(date, trainCode, seatType);
        LOG.info("共查出{}个满足条件的车厢", carriageList.size());
        // 一个车厢一个车厢查找座位
        for (var carriage : carriageList) {
            LOG.info("从车厢{}中挑选座位", carriage.getIndex());
            var seatList = dailyTrainSeatService.selectBySeat(date, trainCode, carriage.getIndex());
            LOG.info("在车厢{}查出{}个满足条件的座位", carriage.getIndex(), seatList.size());
            for (var seat : seatList) {
                boolean sellSuccess = isSeatSell(seat, startIndex, endIndex);
                if (sellSuccess) {
                    LOG.info("选座成功");
                    return;
                }

            }
        }
    }

    /**
     * 判断当前作为是否可以被售卖：通过截取的字符串来进行判断，如果可以售卖，使用chars数组进行更改（而非像参考代码进行大量转换操作）
     * 
     * @param seat
     * @param startIndex
     * @param endIndex
     * @return
     */
    private boolean isSeatSell(DailyTrainSeat seat, int startIndex, int endIndex) {
        String sell = seat.getSell();
        String goalStationStr = sell.substring(startIndex, endIndex);
        if (Integer.parseInt(goalStationStr) != 0) {
            LOG.info("座位{}的{}~{}已被售卖，不可选取", seat.getCarriageSeatIndex(), startIndex, endIndex);
            return false;
        } else {
            LOG.info("座位{}的{}~{}未被售卖，可选取", seat.getCarriageSeatIndex(), startIndex, endIndex);
            char[] sellChars = sell.toCharArray();
            for (int i = startIndex; i < endIndex; ++i) {
                sellChars[i] = '1';
            }
            String newSell = new String(sellChars);
            seat.setSell(newSell);
            LOG.info("座位{}选中成功，原始售票状态：{}, 修改后的售票状态：{}", seat.getCarriageSeatIndex(), sell, newSell);
            return true;
        }
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
