package com.example.miaoshatest.miaosha.service.impl;

import com.example.miaoshatest.miaosha.entity.User;
import com.example.miaoshatest.miaosha.mapper.UserMapper;
import com.example.miaoshatest.miaosha.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
