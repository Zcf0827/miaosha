package com.example.miaoshatest.miaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.miaoshatest.miaosha.entity.Stock;
import com.example.miaoshatest.miaosha.mapper.StockMapper;
import com.example.miaoshatest.miaosha.service.StockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Chaofan
 * @since 2022-03-01
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {

    @Autowired
    private StockMapper stockMapper;

    @Override
    public int updateStockByOptimistic(Stock stock) {
        return stockMapper.updateByOptimistic(stock);
    }
}
