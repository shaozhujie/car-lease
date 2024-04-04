package com.project.car.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.car.domain.CarAnnouncement;
import com.project.car.mapper.CarAnnouncementMapper;
import com.project.car.service.CarAnnouncementService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 公告service实现类
 * @date 2024/03/13 05:13
 */
@Service
public class CarAnnouncementServiceImpl extends ServiceImpl<CarAnnouncementMapper, CarAnnouncement> implements CarAnnouncementService {
}
