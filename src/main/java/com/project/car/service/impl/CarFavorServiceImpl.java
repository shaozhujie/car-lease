package com.project.car.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.car.domain.CarFavor;
import com.project.car.mapper.CarFavorMapper;
import com.project.car.service.CarFavorService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 收藏service实现类
 * @date 2024/03/19 03:25
 */
@Service
public class CarFavorServiceImpl extends ServiceImpl<CarFavorMapper, CarFavor> implements CarFavorService {
}
