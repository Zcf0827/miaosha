package com.example.miaoshatest;

import com.example.miaoshatest.miaosha.entity.User;
import com.example.miaoshatest.miaosha.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@SpringBootTest
class MiaoshatestApplicationTests {

    @Autowired
    UserService userService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @Test
    void contextLoads() {
    }

    @Test
    void finduser(){
        List<User> list = userService.list();
        System.out.println(list);

    }
    @Test
    void redistest(){
        stringRedisTemplate.opsForValue().set("a","a");
    }

}
