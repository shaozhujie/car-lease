package com.project.car.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.car.domain.CarService;
import com.project.car.mapper.CarServiceMapper;
import com.project.car.service.CarServiceService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 维修管理service实现类
 * @date 2024/03/13 03:12
 */
@Service
public class CarServiceServiceImpl extends ServiceImpl<CarServiceMapper, CarService> implements CarServiceService {
}
