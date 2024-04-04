package com.project.car.controller.car;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.car.domain.CarList;
import com.project.car.domain.CarOrder;
import com.project.car.domain.CarReturn;
import com.project.car.domain.Result;
import com.project.car.enums.ResultCode;
import com.project.car.service.CarListService;
import com.project.car.service.CarOrderService;
import com.project.car.service.CarReturnService;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 汽车归还controller
 * @date 2024/03/13 02:08
 */
@Controller
@ResponseBody
@RequestMapping("return")
public class CarReturnController {

    @Autowired
    private CarReturnService carReturnService;
    @Autowired
    private CarListService carListService;
    @Autowired
    private CarOrderService carOrderService;

    /** 分页获取汽车归还 */
    @PostMapping("getCarReturnPage")
    public Result getCarReturnPage(@RequestBody CarReturn carReturn) {
        Page<CarReturn> page = new Page<>(carReturn.getPageNumber(),carReturn.getPageSize());
        QueryWrapper<CarReturn> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(carReturn.getOrderNumber()),CarReturn::getOrderNumber,carReturn.getOrderNumber())
                .like(StringUtils.isNotBlank(carReturn.getName()),CarReturn::getName,carReturn.getName())
                .like(StringUtils.isNotBlank(carReturn.getBrand()),CarReturn::getBrand,carReturn.getBrand())
                .like(StringUtils.isNotBlank(carReturn.getType()),CarReturn::getType,carReturn.getType())
                .like(StringUtils.isNotBlank(carReturn.getCarNumber()),CarReturn::getCarNumber,carReturn.getCarNumber());
        Page<CarReturn> carReturnPage = carReturnService.page(page, queryWrapper);
        return Result.success(carReturnPage);
    }

    @GetMapping("getCarReturnByOrder")
    public Result getCarReturnByOrder(@RequestParam("orderNumber")String orderNumber) {
        QueryWrapper<CarReturn> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CarReturn::getOrderNumber,orderNumber);
        CarReturn aReturn = carReturnService.getOne(queryWrapper);
        if (aReturn != null) {
            return Result.success(aReturn);
        } else {
            QueryWrapper<CarOrder> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(CarOrder::getOrderNumber,orderNumber);
            CarOrder order = carOrderService.getOne(wrapper);
            CarReturn carReturn = new CarReturn();
            carReturn.setId(IdWorker.getIdStr());
            carReturn.setCarId(order.getCarId());
            carReturn.setOrderNumber(orderNumber);
            carReturn.setName(order.getName());
            carReturn.setBrand(order.getBrand());
            carReturn.setType(order.getType());
            carReturn.setCarNumber(order.getCarNumber());
            carReturn.setPrice(order.getPrice());
            carReturn.setReturnTime(new Date());
            carReturn.setTime(order.getDurat());
            carReturn.setMoney(order.getDurat() * order.getPrice());
            carReturn.setState(0);

            boolean save = carReturnService.save(carReturn);
            return Result.success(carReturn);
        }
    }

    /** 根据id获取汽车归还 */
    @GetMapping("getCarReturnById")
    public Result getCarReturnById(@RequestParam("id")String id) {
        CarReturn carReturn = carReturnService.getById(id);
        return Result.success(carReturn);
    }

    /** 保存汽车归还 */
    @PostMapping("saveCarReturn")
    public Result saveCarReturn(@RequestBody CarReturn carReturn) {
        boolean save = carReturnService.save(carReturn);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑汽车归还 */
    @PostMapping("editCarReturn")
    @Transactional(rollbackFor = Exception.class)
    public Result editCarReturn(@RequestBody CarReturn carReturn) {
        CarReturn aReturn = carReturnService.getById(carReturn.getId());
        if (carReturn.getState() == 1) {
            CarList list = carListService.getById(aReturn.getCarId());
            list.setState(0);
            carListService.updateById(list);

            QueryWrapper<CarOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(CarOrder::getOrderNumber,aReturn.getOrderNumber()).last("limit 1");
            CarOrder order = carOrderService.getOne(queryWrapper);
            order.setState(1);
            carOrderService.updateById(order);
        }
        boolean save = carReturnService.updateById(carReturn);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除汽车归还 */
    @GetMapping("removeCarReturn")
    public Result removeCarReturn(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                carReturnService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("汽车归还id不能为空！");
        }
    }

}
