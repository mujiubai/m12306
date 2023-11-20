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
import com.mujiubai.train.business.domain.DailyTrainExample;
import com.mujiubai.train.business.domain.DailyTrainSeat;
import com.mujiubai.train.business.domain.DailyTrainSeatExample;
import com.mujiubai.train.business.domain.DailyTrainTicket;
import com.mujiubai.train.business.enums.ConfirmOrderStatusEnum;
import com.mujiubai.train.business.enums.SeatColEnum;
import com.mujiubai.train.business.enums.SeatTypeEnum;
import com.mujiubai.train.business.mapper.ConfirmOrderMapper;
import com.mujiubai.train.business.mapper.DailyTrainSeatMapper;
import com.mujiubai.train.business.mapper.cust.DailyTrainTicketMapperCust;
import com.mujiubai.train.business.req.ConfirmOrderDoReq;
import com.mujiubai.train.business.req.ConfirmOrderQueryReq;
import com.mujiubai.train.business.resp.ConfirmOrderQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AfterConfirmOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(AfterConfirmOrderService.class);

    @Resource
    private ConfirmOrderMapper confirmOrderMapper;

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;

    @Resource
    private DailyTrainTicketMapperCust dailyTrainTicketMapperCust;

    /***
     * 完成后续事情：
     * 数据库中保存售票信息
     * 更新余票信息
     * 增加购票记录
     * 确认订单成功
     * 
     * @param finalSeatList
     */
    @Transactional
    public void afterDoConfirm(DailyTrainTicket dailyTrainTicket, List<DailyTrainSeat> finalSeatList) {
        for (DailyTrainSeat dailyTrainSeat : finalSeatList) {
            DailyTrainSeat seatUpdate = new DailyTrainSeat();
            seatUpdate.setUpdateTime(new Date());
            seatUpdate.setSell(dailyTrainSeat.getSell());
            seatUpdate.setId(dailyTrainSeat.getId());
            dailyTrainSeatMapper.updateByPrimaryKeySelective(seatUpdate);

            // 计算这个站卖出去后，影响了哪些站的余票库存
            // 参照2-3节 如何保证不超卖、不少卖，还要能承受极高的并发 10:30左右
            // 影响的库存：本次选座之前没卖过票的，和本次购买的区间有交集的区间
            // 假设10个站，本次买4~7站
            // 原售：001000001
            // 购买：000011100
            // 新售：001011101
            // 影响：XXX11111X
            char[] sellChars = dailyTrainSeat.getSell().toCharArray();
            int startIndex = dailyTrainTicket.getStartIndex();
            int endIndex = dailyTrainTicket.getEndIndex();
            // 注意这里的Index含义，其与sellchars中指代的票有差距，会有个offset（start存在offset，而end则刚好抵消offset）
            int minStartIndex = startIndex, maxStartIndex = endIndex - 1;
            // 找到往前碰到的最后一个0
            while (minStartIndex >= 1 && sellChars[minStartIndex - 1] == '0') {
                --minStartIndex;
            }
            LOG.info("影响出发站区间：" + minStartIndex + "-" + maxStartIndex);
            int minEndIndex = startIndex + 1, maxEndIndex = endIndex;
            // 往后碰到的最后一个0

            while (maxEndIndex < sellChars.length && sellChars[maxEndIndex] == '0') {
                ++maxEndIndex;
            }
            LOG.info("影响到达站区间：" + minEndIndex + "-" + maxEndIndex);
            dailyTrainTicketMapperCust.updateCountBySell(dailyTrainSeat.getDate(), dailyTrainSeat.getTrainCode(),
                    dailyTrainSeat.getSeatType(), minStartIndex, maxStartIndex, minEndIndex, maxEndIndex);

        }

    }

}
