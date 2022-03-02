package com.example.miaoshatest.miaosha.service;

import com.example.miaoshatest.miaosha.entity.TradeOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author Chaofan
 * @since 2022-03-01
 */
public interface TradeOrderService extends IService<TradeOrder> {

    void saveSeckkill(String uid, String vid, String name);
}
