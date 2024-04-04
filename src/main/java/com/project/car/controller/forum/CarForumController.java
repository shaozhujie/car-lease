package com.project.car.controller.forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.car.domain.CarForum;
import com.project.car.domain.CarForumItem;
import com.project.car.domain.Result;
import com.project.car.enums.ResultCode;
import com.project.car.service.CarFineService;
import com.project.car.service.CarForumItemService;
import com.project.car.service.CarForumService;
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
 * @description: 论坛controller
 * @date 2024/03/13 04:37
 */
@Controller
@ResponseBody
@RequestMapping("forum")
public class CarForumController {

    @Autowired
    private CarForumService carForumService;
    @Autowired
    private CarForumItemService carForumItemService;

    /** 分页获取论坛 */
    @PostMapping("getCarForumPage")
    public Result getCarForumPage(@RequestBody CarForum carForum) {
        Page<CarForum> page = new Page<>(carForum.getPageNumber(),carForum.getPageSize());
        QueryWrapper<CarForum> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(carForum.getUserId()),CarForum::getUserId,carForum.getUserId())
                .eq(StringUtils.isNotBlank(carForum.getTitle()),CarForum::getTitle,carForum.getTitle())
                .orderByDesc(CarForum::getCreateTime);
        Page<CarForum> carForumPage = carForumService.page(page, queryWrapper);
        return Result.success(carForumPage);
    }

    /** 根据id获取论坛 */
    @GetMapping("getCarForumById")
    public Result getCarForumById(@RequestParam("id")String id) {
        CarForum carForum = carForumService.getById(id);
        return Result.success(carForum);
    }

    /** 保存论坛 */
    @PostMapping("saveCarForum")
    public Result saveCarForum(@RequestBody CarForum carForum) {
        carForum.setUserId(TokenUtils.getUserIdByToken());
        boolean save = carForumService.save(carForum);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑论坛 */
    @PostMapping("editCarForum")
    public Result editCarForum(@RequestBody CarForum carForum) {
        carForum.setUserId(TokenUtils.getUserIdByToken());
        boolean save = carForumService.updateById(carForum);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除论坛 */
    @GetMapping("removeCarForum")
    @Transactional(rollbackFor = Exception.class)
    public Result removeCarForum(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                QueryWrapper<CarForumItem> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(CarForumItem::getForumId,id);
                carForumItemService.remove(queryWrapper);
                carForumService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("论坛id不能为空！");
        }
    }

    @GetMapping("addLiked")
    public Result addLiked(@RequestParam("id") String id) {
        CarForum item = carForumService.getById(id);
        item.setLiked(item.getLiked() + 1);
        carForumService.updateById(item);
        return Result.success(item);
    }

}
