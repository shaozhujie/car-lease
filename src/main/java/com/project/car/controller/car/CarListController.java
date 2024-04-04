package com.project.car.controller.car;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * @description: 汽车信息controller
 * @date 2024/03/13 09:20
 */
@Controller
@ResponseBody
@RequestMapping("list")
public class CarListController {

    @Autowired
    private CarListService carListService;
    @Autowired
    private CarOrderService carOrderService;
    @Autowired
    private CarReturnService carReturnService;
    @Autowired
    private CarFaultService carFaultService;
    @Autowired
    private CarFineService carFineService;
    @Autowired
    private CarServiceService carServiceService;

    /** 分页获取汽车信息 */
    @PostMapping("getCarListPage")
    public Result getCarListPage(@RequestBody CarList carList) {
        Page<CarList> page = new Page<>(carList.getPageNumber(),carList.getPageSize());
        QueryWrapper<CarList> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(carList.getName()),CarList::getName,carList.getName())
                .like(StringUtils.isNotBlank(carList.getBrand()),CarList::getBrand,carList.getBrand())
                .like(StringUtils.isNotBlank(carList.getType()),CarList::getType,carList.getType())
                .like(StringUtils.isNotBlank(carList.getColor()),CarList::getColor,carList.getColor())
                .like(StringUtils.isNotBlank(carList.getCarNumber()),CarList::getCarNumber,carList.getCarNumber())
                .eq(StringUtils.isNotBlank(carList.getYear()),CarList::getYear,carList.getYear())
                .eq(carList.getState() != null,CarList::getState,carList.getState())
                .orderByDesc(CarList::getCreateTime);
        Page<CarList> carListPage = carListService.page(page, queryWrapper);
        return Result.success(carListPage);
    }

    @GetMapping("getCarListThree")
    public Result getCarListThree() {
        QueryWrapper<CarList> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CarList::getState,0).orderByDesc(CarList::getCreateTime).last("limit 4");
        List<CarList> listList = carListService.list(queryWrapper);
        return Result.success(listList);
    }

    @GetMapping("getCarList")
    public Result getCarList() {
        List<CarList> listList = carListService.list();
        return Result.success(listList);
    }

    /** 根据id获取汽车信息 */
    @GetMapping("getCarListById")
    public Result getCarListById(@RequestParam("id")String id) {
        CarList carList = carListService.getById(id);
        return Result.success(carList);
    }

    /** 保存汽车信息 */
    @PostMapping("saveCarList")
    public Result saveCarList(@RequestBody CarList carList) {
        boolean save = carListService.save(carList);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑汽车信息 */
    @PostMapping("editCarList")
    public Result editCarList(@RequestBody CarList carList) {
        boolean save = carListService.updateById(carList);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除汽车信息 */
    @GetMapping("removeCarList")
    @Transactional(rollbackFor = Exception.class)
    public Result removeCarList(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                carListService.removeById(id);
                QueryWrapper<CarOrder> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(CarOrder::getCarId,id);
                carOrderService.remove(queryWrapper);

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
            return Result.fail("汽车信息id不能为空！");
        }
    }

}
