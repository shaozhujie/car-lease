package com.project.car.controller.fine;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.car.domain.CarFine;
import com.project.car.domain.Result;
import com.project.car.enums.ResultCode;
import com.project.car.service.CarFineService;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 罚款controller
 * @date 2024/03/13 03:42
 */
@Controller
@ResponseBody
@RequestMapping("fine")
public class CarFineController {

    @Autowired
    private CarFineService carFineService;

    /** 分页获取罚款 */
    @PostMapping("getCarFinePage")
    public Result getCarFinePage(@RequestBody CarFine carFine) {
        Page<CarFine> page = new Page<>(carFine.getPageNumber(),carFine.getPageSize());
        QueryWrapper<CarFine> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(carFine.getLoginAccount()),CarFine::getLoginAccount,carFine.getLoginAccount())
                .like(StringUtils.isNotBlank(carFine.getOrderNumber()),CarFine::getOrderNumber,carFine.getOrderNumber())
                .like(StringUtils.isNotBlank(carFine.getName()),CarFine::getName,carFine.getName())
                .like(StringUtils.isNotBlank(carFine.getBrand()),CarFine::getBrand,carFine.getBrand())
                .like(StringUtils.isNotBlank(carFine.getCarNumber()),CarFine::getCarNumber,carFine.getCarNumber())
                .like(StringUtils.isNotBlank(carFine.getType()),CarFine::getType,carFine.getType());
        Page<CarFine> carFinePage = carFineService.page(page, queryWrapper);
        return Result.success(carFinePage);
    }

    /** 根据id获取罚款 */
    @GetMapping("getCarFineById")
    public Result getCarFineById(@RequestParam("id")String id) {
        CarFine carFine = carFineService.getById(id);
        return Result.success(carFine);
    }

    /** 保存罚款 */
    @PostMapping("saveCarFine")
    public Result saveCarFine(@RequestBody CarFine carFine) {
        boolean save = carFineService.save(carFine);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑罚款 */
    @PostMapping("editCarFine")
    public Result editCarFine(@RequestBody CarFine carFine) {
        boolean save = carFineService.updateById(carFine);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除罚款 */
    @GetMapping("removeCarFine")
    public Result removeCarFine(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                carFineService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("罚款id不能为空！");
        }
    }

}
