package com.project.car.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.car.domain.CarReturn;
import com.project.car.mapper.CarReturnMapper;
import com.project.car.service.CarReturnService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 汽车归还service实现类
 * @date 2024/03/13 02:08
 */
@Service
public class CarReturnServiceImpl extends ServiceImpl<CarReturnMapper, CarReturn> implements CarReturnService {
}
