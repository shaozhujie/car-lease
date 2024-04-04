package com.project.car.controller.favor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.car.domain.CarFavor;
import com.project.car.domain.CarList;
import com.project.car.domain.Result;
import com.project.car.enums.ResultCode;
import com.project.car.service.CarFavorService;
import com.project.car.service.CarListService;
import com.project.car.utils.TokenUtils;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 收藏controller
 * @date 2024/03/19 03:25
 */
@Controller
@ResponseBody
@RequestMapping("favor")
public class CarFavorController {

    @Autowired
    private CarFavorService carFavorService;
    @Autowired
    private CarListService carListService;

    /** 分页获取收藏 */
    @PostMapping("getCarFavorPage")
    public Result getCarFavorPage(@RequestBody CarFavor carFavor) {
        Page<CarFavor> page = new Page<>(carFavor.getPageNumber(),carFavor.getPageSize());
        QueryWrapper<CarFavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(carFavor.getCarId()),CarFavor::getCarId,carFavor.getCarId())
                .eq(StringUtils.isNotBlank(carFavor.getImages()),CarFavor::getImages,carFavor.getImages())
                .eq(StringUtils.isNotBlank(carFavor.getName()),CarFavor::getName,carFavor.getName())
                .eq(StringUtils.isNotBlank(carFavor.getUserId()),CarFavor::getUserId,carFavor.getUserId());
        Page<CarFavor> carFavorPage = carFavorService.page(page, queryWrapper);
        return Result.success(carFavorPage);
    }

    /** 根据id获取收藏 */
    @GetMapping("getCarFavorById")
    public Result getCarFavorById(@RequestParam("id")String id) {
        CarFavor carFavor = carFavorService.getById(id);
        return Result.success(carFavor);
    }

    /** 保存收藏 */
    @PostMapping("saveCarFavor")
    public Result saveCarFavor(@RequestBody CarFavor carFavor) {
        CarList carList = carListService.getById(carFavor.getCarId());
        carFavor.setImages(carList.getImages());
        carFavor.setName(carList.getName());
        carFavor.setUserId(TokenUtils.getUserIdByToken());
        boolean save = carFavorService.save(carFavor);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑收藏 */
    @PostMapping("editCarFavor")
    public Result editCarFavor(@RequestBody CarFavor carFavor) {
        boolean save = carFavorService.updateById(carFavor);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除收藏 */
    @GetMapping("removeCarFavor")
    public Result removeCarFavor(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                carFavorService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("收藏id不能为空！");
        }
    }

    @GetMapping("getFavor")
    public Result getFavor(@RequestParam("userId")String userId,@RequestParam("carId")String carId) {
        QueryWrapper<CarFavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CarFavor::getUserId,userId).eq(CarFavor::getCarId,carId);
        int count = carFavorService.count(queryWrapper);
        if (count > 0) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

}
