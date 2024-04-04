package com.project.car.controller.fault;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.car.domain.CarFault;
import com.project.car.domain.CarOrder;
import com.project.car.domain.Result;
import com.project.car.enums.ResultCode;
import com.project.car.service.CarFaultService;
import com.project.car.service.CarOrderService;
import com.project.car.utils.TokenUtils;
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
 * @description: 车辆故障controller
 * @date 2024/03/13 02:38
 */
@Controller
@ResponseBody
@RequestMapping("fault")
public class CarFaultController {

    @Autowired
    private CarFaultService carFaultService;
    @Autowired
    private CarOrderService carOrderService;

    /** 分页获取车辆故障 */
    @PostMapping("getCarFaultPage")
    public Result getCarFaultPage(@RequestBody CarFault carFault) {
        Page<CarFault> page = new Page<>(carFault.getPageNumber(),carFault.getPageSize());
        QueryWrapper<CarFault> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(carFault.getState() != null,CarFault::getState,carFault.getState())
                .eq(StringUtils.isNotBlank(carFault.getUserId()),CarFault::getUserId,carFault.getUserId())
                .like(StringUtils.isNotBlank(carFault.getOrderNumber()),CarFault::getOrderNumber,carFault.getOrderNumber())
                .like(StringUtils.isNotBlank(carFault.getName()),CarFault::getName,carFault.getName())
                .like(StringUtils.isNotBlank(carFault.getBrand()),CarFault::getBrand,carFault.getBrand());
        Page<CarFault> carFaultPage = carFaultService.page(page, queryWrapper);
        return Result.success(carFaultPage);
    }

    @GetMapping("getCarFaultList")
    public Result getCarFaultList() {
        QueryWrapper<CarFault> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CarFault::getState,0);
        List<CarFault> faultList = carFaultService.list();
        return Result.success(faultList);
    }

    /** 根据id获取车辆故障 */
    @GetMapping("getCarFaultById")
    public Result getCarFaultById(@RequestParam("id")String id) {
        CarFault carFault = carFaultService.getById(id);
        return Result.success(carFault);
    }

    /** 保存车辆故障 */
    @PostMapping("saveCarFault")
    public Result saveCarFault(@RequestBody CarFault carFault) {
        String id = TokenUtils.getUserIdByToken();
        carFault.setUserId(id);
        QueryWrapper<CarOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CarOrder::getOrderNumber,carFault.getOrderNumber()).last("limit 1");
        CarOrder order = carOrderService.getOne(queryWrapper);
        carFault.setCarId(order.getCarId());
        boolean save = carFaultService.save(carFault);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑车辆故障 */
    @PostMapping("editCarFault")
    public Result editCarFault(@RequestBody CarFault carFault) {
        boolean save = carFaultService.updateById(carFault);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除车辆故障 */
    @GetMapping("removeCarFault")
    public Result removeCarFault(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                carFaultService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("车辆故障id不能为空！");
        }
    }

}
