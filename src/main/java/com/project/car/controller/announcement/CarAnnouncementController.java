package com.project.car.controller.announcement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.car.domain.CarAnnouncement;
import com.project.car.domain.Result;
import com.project.car.enums.ResultCode;
import com.project.car.service.CarAnnouncementService;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 公告controller
 * @date 2024/03/13 05:13
 */
@Controller
@ResponseBody
@RequestMapping("announcement")
public class CarAnnouncementController {

    @Autowired
    private CarAnnouncementService carAnnouncementService;

    /** 分页获取公告 */
    @PostMapping("getCarAnnouncementPage")
    public Result getCarAnnouncementPage(@RequestBody CarAnnouncement carAnnouncement) {
        Page<CarAnnouncement> page = new Page<>(carAnnouncement.getPageNumber(),carAnnouncement.getPageSize());
        QueryWrapper<CarAnnouncement> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(carAnnouncement.getTitle()),CarAnnouncement::getTitle,carAnnouncement.getTitle());
        Page<CarAnnouncement> carAnnouncementPage = carAnnouncementService.page(page, queryWrapper);
        return Result.success(carAnnouncementPage);
    }

    @GetMapping("getCarAnnouncementFive")
    public Result getCarAnnouncementFive() {
        QueryWrapper<CarAnnouncement> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(CarAnnouncement::getCreateTime).last("limit 5");
        List<CarAnnouncement> announcementList = carAnnouncementService.list(queryWrapper);
        return Result.success(announcementList);
    }

    /** 根据id获取公告 */
    @GetMapping("getCarAnnouncementById")
    public Result getCarAnnouncementById(@RequestParam("id")String id) {
        CarAnnouncement carAnnouncement = carAnnouncementService.getById(id);
        return Result.success(carAnnouncement);
    }

    /** 保存公告 */
    @PostMapping("saveCarAnnouncement")
    public Result saveCarAnnouncement(@RequestBody CarAnnouncement carAnnouncement) {
        boolean save = carAnnouncementService.save(carAnnouncement);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑公告 */
    @PostMapping("editCarAnnouncement")
    public Result editCarAnnouncement(@RequestBody CarAnnouncement carAnnouncement) {
        boolean save = carAnnouncementService.updateById(carAnnouncement);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除公告 */
    @GetMapping("removeCarAnnouncement")
    public Result removeCarAnnouncement(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                carAnnouncementService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("公告id不能为空！");
        }
    }

    @GetMapping("addLiked")
    public Result addLiked(@RequestParam("id") String id) {
        CarAnnouncement announcement = carAnnouncementService.getById(id);
        announcement.setLiked(announcement.getLiked() + 1);
        carAnnouncementService.updateById(announcement);
        return Result.success();
    }

}
