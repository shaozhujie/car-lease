package com.project.car.controller.seek;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.car.domain.CarSeek;
import com.project.car.domain.Result;
import com.project.car.domain.User;
import com.project.car.enums.ResultCode;
import com.project.car.service.CarSeekService;
import com.project.car.service.UserService;
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
 * @description: 咨询controller
 * @date 2024/03/13 08:09
 */
@Controller
@ResponseBody
@RequestMapping("seek")
public class CarSeekController {

    @Autowired
    private CarSeekService carSeekService;
    @Autowired
    private UserService userService;

    /** 分页获取咨询 */
    @PostMapping("getCarSeekPage")
    public Result getCarSeekPage(@RequestBody CarSeek carSeek) {
        Page<CarSeek> page = new Page<>(carSeek.getPageNumber(),carSeek.getPageSize());
        QueryWrapper<CarSeek> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(carSeek.getType() != null,CarSeek::getType,carSeek.getType())
                .eq(StringUtils.isNotBlank(carSeek.getUserId()),CarSeek::getUserId,carSeek.getUserId())
                .eq(StringUtils.isNotBlank(carSeek.getCreateBy()),CarSeek::getCreateBy,carSeek.getCreateBy())
                .orderByDesc(CarSeek::getCreateTime);
        Page<CarSeek> carSeekPage = carSeekService.page(page, queryWrapper);
        return Result.success(carSeekPage);
    }

    @GetMapping("getCarSeekList")
    public Result getCarSeekList() {
        QueryWrapper<CarSeek> query = new QueryWrapper<>();
        query.lambda().eq(CarSeek::getUserId,TokenUtils.getUserIdByToken()).orderByAsc(CarSeek::getCreateTime);
        List<CarSeek> seekList = carSeekService.list(query);
        return Result.success(seekList);
    }

    /** 根据id获取咨询 */
    @GetMapping("getCarSeekById")
    public Result getCarSeekById(@RequestParam("id")String id) {
        CarSeek carSeek = carSeekService.getById(id);
        return Result.success(carSeek);
    }

    /** 保存咨询 */
    @PostMapping("saveCarSeek")
    public Result saveCarSeek(@RequestBody CarSeek carSeek) {
        String id = TokenUtils.getUserIdByToken();
        User user = userService.getById(id);
        if (carSeek.getType() == 0) {
            carSeek.setUserId(user.getId());
            carSeek.setAvatar(user.getAvatar());
        } else {
            carSeek.setAvatar(user.getAvatar());
        }
        boolean save = carSeekService.save(carSeek);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑咨询 */
    @PostMapping("editCarSeek")
    public Result editCarSeek(@RequestBody CarSeek carSeek) {
        String id = TokenUtils.getUserIdByToken();
        User user = userService.getById(id);
        if (carSeek.getType() == 0) {
            carSeek.setUserId(user.getId());
            carSeek.setAvatar(user.getAvatar());
        } else {
            carSeek.setAvatar(user.getAvatar());
        }
        boolean save = carSeekService.updateById(carSeek);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除咨询 */
    @GetMapping("removeCarSeek")
    public Result removeCarSeek(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                carSeekService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("咨询id不能为空！");
        }
    }

}
