package com.example.miaoshatest.miaosha.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.example.miaoshatest.miaosha.entity.TradeOrder;
import com.example.miaoshatest.miaosha.entity.Vedios;
import com.example.miaoshatest.miaosha.service.SeckkillService;
import com.example.miaoshatest.miaosha.service.TradeOrderService;
import com.example.miaoshatest.miaosha.service.VediosService;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SeckkillServiceImpl implements SeckkillService {



    @Autowired
    private VediosService vediosService;

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
                TradeOrder tradeOrder = new TradeOrder();
                tradeOrder.setMemberId(uid);
                tradeOrder.setCourseId(vid);
                tradeOrder.setOrderNo(IdWorker.getTimeId().substring(10));
                tradeOrder.setNickname(sekkvedio.getName());
                tradeOrder.setGmtCreate(new Date());
                tradeOrder.setGmtModified(new Date());
                tradeOrderService.save(tradeOrder);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "库存剩余" + semaphore.availablePermits() + "";
    }
}
