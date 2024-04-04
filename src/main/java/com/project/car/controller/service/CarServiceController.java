package com.project.car.controller.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.car.domain.CarService;
import com.project.car.domain.Result;
import com.project.car.enums.ResultCode;
import com.project.car.service.CarServiceService;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 维修管理controller
 * @date 2024/03/13 03:12
 */
@Controller
@ResponseBody
@RequestMapping("service")
public class CarServiceController {

    @Autowired
    private CarServiceService carServiceService;

    /** 分页获取维修管理 */
    @PostMapping("getCarServicePage")
    public Result getCarServicePage(@RequestBody CarService carService) {
        Page<CarService> page = new Page<>(carService.getPageNumber(),carService.getPageSize());
        QueryWrapper<CarService> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(carService.getOrderNumber()),CarService::getOrderNumber,carService.getOrderNumber())
                .like(StringUtils.isNotBlank(carService.getContent()),CarService::getContent,carService.getContent())
                .like(StringUtils.isNotBlank(carService.getName()),CarService::getName,carService.getName())
                .like(StringUtils.isNotBlank(carService.getBrand()),CarService::getBrand,carService.getBrand());
        Page<CarService> carServicePage = carServiceService.page(page, queryWrapper);
        return Result.success(carServicePage);
    }

    /** 根据id获取维修管理 */
    @GetMapping("getCarServiceById")
    public Result getCarServiceById(@RequestParam("id")String id) {
        CarService carService = carServiceService.getById(id);
        return Result.success(carService);
    }

    /** 保存维修管理 */
    @PostMapping("saveCarService")
    public Result saveCarService(@RequestBody CarService carService) {
        boolean save = carServiceService.save(carService);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑维修管理 */
    @PostMapping("editCarService")
    public Result editCarService(@RequestBody CarService carService) {
        boolean save = carServiceService.updateById(carService);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除维修管理 */
    @GetMapping("removeCarService")
    public Result removeCarService(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                carServiceService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("维修管理id不能为空！");
        }
    }

}
