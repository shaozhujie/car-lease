package com.project.car.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.car.domain.CarOrder;
import com.project.car.mapper.CarOrderMapper;
import com.project.car.service.CarOrderService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 租凭订单service实现类
 * @date 2024/03/13 10:28
 */
@Service
public class CarOrderServiceImpl extends ServiceImpl<CarOrderMapper, CarOrder> implements CarOrderService {
}
