package com.project.car.controller.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.car.domain.*;
import com.project.car.enums.ResultCode;
import com.project.car.service.*;
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
 * @description: 租凭订单controller
 * @date 2024/03/13 10:28
 */
@Controller
@ResponseBody
@RequestMapping("order")
public class CarOrderController {

    @Autowired
    private CarOrderService carOrderService;
    @Autowired
    private CarListService carListService;
    @Autowired
    private CarReturnService carReturnService;
    @Autowired
    private CarFaultService carFaultService;
    @Autowired
    private CarFineService carFineService;
    @Autowired
    private CarServiceService carServiceService;

    /** 分页获取租凭订单 */
    @PostMapping("getCarOrderPage")
    public Result getCarOrderPage(@RequestBody CarOrder carOrder) {
        Page<CarOrder> page = new Page<>(carOrder.getPageNumber(),carOrder.getPageSize());
        QueryWrapper<CarOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(carOrder.getOrderNumber()),CarOrder::getOrderNumber,carOrder.getOrderNumber())
                .like(StringUtils.isNotBlank(carOrder.getName()),CarOrder::getName,carOrder.getName())
                .like(StringUtils.isNotBlank(carOrder.getCarNumber()),CarOrder::getCarNumber,carOrder.getCarNumber())
                .like(StringUtils.isNotBlank(carOrder.getLoginAccount()),CarOrder::getLoginAccount,carOrder.getLoginAccount())
                .eq(carOrder.getState() != null,CarOrder::getState,carOrder.getState());
        Page<CarOrder> carOrderPage = carOrderService.page(page, queryWrapper);
        return Result.success(carOrderPage);
    }

    @GetMapping("getCarOrderList")
    public Result getCarOrderList() {
        List<CarOrder> orderList = carOrderService.list();
        return Result.success(orderList);
    }

    /** 根据id获取租凭订单 */
    @GetMapping("getCarOrderById")
    public Result getCarOrderById(@RequestParam("id")String id) {
        CarOrder carOrder = carOrderService.getById(id);
        return Result.success(carOrder);
    }

    /** 保存租凭订单 */
    @PostMapping("saveCarOrder")
    public Result saveCarOrder(@RequestBody CarOrder carOrder) {
        QueryWrapper<CarOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CarOrder::getCarId,carOrder.getCarId())
                .eq(CarOrder::getState,0).eq(CarOrder::getBound,0);
        int count = carOrderService.count(queryWrapper);
        if (count > 0) {
            return Result.fail("车辆已租赁,无法重复租凭");
        }
        CarList car = carListService.getById(carOrder.getCarId());
        carOrder.setName(car.getName());
        carOrder.setType(car.getType());
        carOrder.setBrand(car.getBrand());
        carOrder.setCarNumber(car.getCarNumber());
        carOrder.setPrice(car.getPrice());
        carOrder.setImages(car.getImages());
        String uuid = IdWorker.get32UUID();
        carOrder.setOrderNumber(uuid);
        boolean save = carOrderService.save(carOrder);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑租凭订单 */
    @PostMapping("editCarOrder")
    public Result editCarOrder(@RequestBody CarOrder carOrder) {
        boolean save = carOrderService.updateById(carOrder);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除租凭订单 */
    @GetMapping("removeCarOrder")
    public Result removeCarOrder(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                carOrderService.removeById(id);
                QueryWrapper<CarReturn> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.lambda().eq(CarReturn::getCarId,id);
                carReturnService.remove(queryWrapper1);

                QueryWrapper<CarFault> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.lambda().eq(CarFault::getCarId,id);
                carFaultService.remove(queryWrapper2);

                QueryWrapper<CarFine> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.lambda().eq(CarFine::getCarId,id);
                carFineService.remove(queryWrapper3);

                QueryWrapper<CarService> queryWrapper4 = new QueryWrapper<>();
                queryWrapper4.lambda().eq(CarService::getCarId,id);
                carServiceService.remove(queryWrapper4);
            }
            return Result.success();
        } else {
            return Result.fail("租凭订单id不能为空！");
        }
    }

}
