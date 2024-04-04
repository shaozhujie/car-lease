package com.project.car.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.car.domain.CarFine;
import com.project.car.mapper.CarFineMapper;
import com.project.car.service.CarFineService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 罚款service实现类
 * @date 2024/03/13 03:42
 */
@Service
public class CarFineServiceImpl extends ServiceImpl<CarFineMapper, CarFine> implements CarFineService {
}