package com.project.car.controller.type;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.car.domain.CarType;
import com.project.car.domain.Result;
import com.project.car.enums.ResultCode;
import com.project.car.service.CarTypeService;
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
 * @description: 汽车类型controller
 * @date 2024/03/13 09:01
 */
@Controller
@ResponseBody
@RequestMapping("type")
public class CarTypeController {

    @Autowired
    private CarTypeService carTypeService;

    /** 分页获取汽车类型 */
    @PostMapping("getCarTypePage")
    public Result getCarTypePage(@RequestBody CarType carType) {
        Page<CarType> page = new Page<>(carType.getPageNumber(),carType.getPageSize());
        QueryWrapper<CarType> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(carType.getName()),CarType::getName,carType.getName());
        Page<CarType> carTypePage = carTypeService.page(page, queryWrapper);
        return Result.success(carTypePage);
    }

    @GetMapping("getCarTypeList")
    public Result getCarTypeList() {
        List<CarType> typeList = carTypeService.list();
        return Result.success(typeList);
    }

    /** 根据id获取汽车类型 */
    @GetMapping("getCarTypeById")
    public Result getCarTypeById(@RequestParam("id")String id) {
        CarType carType = carTypeService.getById(id);
        return Result.success(carType);
    }

    /** 保存汽车类型 */
    @PostMapping("saveCarType")
    public Result saveCarType(@RequestBody CarType carType) {
        boolean save = carTypeService.save(carType);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑汽车类型 */
    @PostMapping("editCarType")
    public Result editCarType(@RequestBody CarType carType) {
        boolean save = carTypeService.updateById(carType);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除汽车类型 */
    @GetMapping("removeCarType")
    public Result removeCarType(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                carTypeService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("汽车类型id不能为空！");
        }
    }

}
