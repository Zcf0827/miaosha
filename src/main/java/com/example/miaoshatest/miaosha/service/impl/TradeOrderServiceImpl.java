package com.example.miaoshatest.miaosha.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.example.miaoshatest.miaosha.entity.TradeOrder;
import com.example.miaoshatest.miaosha.mapper.TradeOrderMapper;
import com.example.miaoshatest.miaosha.service.TradeOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author Chaofan
 * @since 2022-03-01
 */
@Service
public class TradeOrderServiceImpl extends ServiceImpl<TradeOrderMapper, TradeOrder> implements TradeOrderService {

    @Override
    public void saveSeckkill(String uid, String vid, String name) {
        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setMemberId(uid);
        tradeOrder.setCourseId(vid);
        tradeOrder.setOrderNo(IdWorker.getTimeId().substring(10));
        tradeOrder.setNickname(name);
        tradeOrder.setGmtCreate(new Date());
        tradeOrder.setGmtModified(new Date());
        this.save(tradeOrder);
    }
}
