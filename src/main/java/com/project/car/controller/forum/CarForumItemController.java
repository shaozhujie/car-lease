package com.project.car.controller.forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.car.domain.CarForumItem;
import com.project.car.domain.Result;
import com.project.car.domain.User;
import com.project.car.enums.ResultCode;
import com.project.car.service.CarForumItemService;
import com.project.car.service.UserService;
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
 * @description: 论坛评论controller
 * @date 2024/03/13 05:00
 */
@Controller
@ResponseBody
@RequestMapping("item")
public class CarForumItemController {

    @Autowired
    private CarForumItemService carForumItemService;
    @Autowired
    private UserService userService;

    /** 分页获取论坛评论 */
    @PostMapping("getCarForumItemPage")
    public Result getCarForumItemPage(@RequestBody CarForumItem carForumItem) {
        Page<CarForumItem> page = new Page<>(carForumItem.getPageNumber(),carForumItem.getPageSize());
        QueryWrapper<CarForumItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(carForumItem.getForumId()),CarForumItem::getForumId,carForumItem.getForumId())
                .eq(StringUtils.isNotBlank(carForumItem.getContent()),CarForumItem::getContent,carForumItem.getContent());
        Page<CarForumItem> carForumItemPage = carForumItemService.page(page, queryWrapper);
        return Result.success(carForumItemPage);
    }

    /** 根据id获取论坛评论 */
    @GetMapping("getCarForumItemById")
    public Result getCarForumItemById(@RequestParam("id")String id) {
        CarForumItem carForumItem = carForumItemService.getById(id);
        return Result.success(carForumItem);
    }

    /** 保存论坛评论 */
    @PostMapping("saveCarForumItem")
    public Result saveCarForumItem(@RequestBody CarForumItem carForumItem) {
        String id = TokenUtils.getUserIdByToken();
        User user = userService.getById(id);
        carForumItem.setUserId(id);
        carForumItem.setAvatar(user.getAvatar());
        boolean save = carForumItemService.save(carForumItem);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑论坛评论 */
    @PostMapping("editCarForumItem")
    public Result editCarForumItem(@RequestBody CarForumItem carForumItem) {
        boolean save = carForumItemService.updateById(carForumItem);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除论坛评论 */
    @GetMapping("removeCarForumItem")
    public Result removeCarForumItem(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                carForumItemService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("论坛评论id不能为空！");
        }
    }

}
