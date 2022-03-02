package com.example.miaoshatest.miaosha.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.example.miaoshatest.miaosha.entity.Stock;
import com.example.miaoshatest.miaosha.entity.TradeOrder;
import com.example.miaoshatest.miaosha.entity.Vedios;
import com.example.miaoshatest.miaosha.service.SeckkillService;
import com.example.miaoshatest.miaosha.service.StockService;
import com.example.miaoshatest.miaosha.service.TradeOrderService;
import com.example.miaoshatest.miaosha.service.VediosService;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SeckkillServiceImpl implements SeckkillService {



    @Autowired
    private VediosService vediosService;

    @Autowired
    private StockService stockService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private TradeOrderService tradeOrderService;

    private final String VIDEOS_CACHE_PREFIX = "seckill:videos";

    private final String VIDEOS_STOCK_PREFIX = "seckill:stock";


    @Override
    public void uploadVideos() {
        //从videos中获取数据 上传到redis中
        List<Vedios> videos = vediosService.list();

        videos.stream().forEach(vedio -> {
            int id = vedio.getId();
            String vediovalue = JSON.toJSONString(vedio);
            String key = VIDEOS_CACHE_PREFIX + "_" + id;
            boolean check = stringRedisTemplate.hasKey(key);
            if(!check){
                stringRedisTemplate.opsForValue().set(key,vediovalue);

                RSemaphore semaphore = redissonClient.getSemaphore(VIDEOS_STOCK_PREFIX + id);

                semaphore.trySetPermits(vedio.getCount());
            }
        });

    }

    @Override
    public String Kill(String uid, String vid) {
        //获取商品信息
        String vediosStr = stringRedisTemplate.opsForValue().get(VIDEOS_CACHE_PREFIX + "_" + vid);
        Vedios sekkvedio = JSON.parseObject(vediosStr, Vedios.class);
        //获取库存
        RSemaphore semaphore = redissonClient.getSemaphore(VIDEOS_STOCK_PREFIX + vid);
        try {
            boolean semaphoreCount = semaphore.tryAcquire(1, 100, TimeUnit.MILLISECONDS);
            //下单
            if(semaphoreCount){
                tradeOrderService.saveSeckkill(uid, vid, sekkvedio.getName());
            }else{
                return "秒杀失败";
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "库存剩余" + semaphore.availablePermits() + "";
    }

    @Override
    public String killCAS(String uid, String vid) {

        //校验库存
        Stock stock = stockService.getById(vid);
        if(stock == null || stock.getCount() == stock.getSale()){
            return "秒杀失败";
        }
        boolean success = saleStockOptimistic(stock);
        if(!success){
            return "秒杀失败";
        }
        //tradeOrderService.saveSeckkill(uid, vid, stock.getName());
        return "秒杀成功，库存还剩" + (stock.getCount()- stock.getSale()-1) + "件";
    }

    private boolean saleStockOptimistic(Stock stock) {
        int count = stockService.updateStockByOptimistic(stock);
        return count != 0;
    }
}
