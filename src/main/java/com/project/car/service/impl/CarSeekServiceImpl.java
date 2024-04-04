package com.project.car.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.car.domain.CarSeek;
import com.project.car.mapper.CarSeekMapper;
import com.project.car.service.CarSeekService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 咨询service实现类
 * @date 2024/03/13 08:09
 */
@Service
public class CarSeekServiceImpl extends ServiceImpl<CarSeekMapper, CarSeek> implements CarSeekService {
}
