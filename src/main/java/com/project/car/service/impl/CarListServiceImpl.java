package com.project.car.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.car.domain.CarList;
import com.project.car.mapper.CarListMapper;
import com.project.car.service.CarListService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 汽车信息service实现类
 * @date 2024/03/13 09:20
 */
@Service
public class CarListServiceImpl extends ServiceImpl<CarListMapper, CarList> implements CarListService {
}