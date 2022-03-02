package com.example.miaoshatest.miaosha.mapper;

import com.example.miaoshatest.miaosha.entity.Stock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Chaofan
 * @since 2022-03-01
 */
@Mapper
public interface StockMapper extends BaseMapper<Stock> {
     int updateByOptimistic(Stock record);


}
