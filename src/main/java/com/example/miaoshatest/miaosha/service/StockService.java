package com.example.miaoshatest.miaosha.service;

import com.example.miaoshatest.miaosha.entity.Stock;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Chaofan
 * @since 2022-03-01
 */
public interface StockService extends IService<Stock> {

    int updateStockByOptimistic(Stock stock);
}
