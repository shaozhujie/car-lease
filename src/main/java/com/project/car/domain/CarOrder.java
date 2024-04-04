package com.project.car.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 租凭订单
 * @date 2024/03/13 10:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("car_order")
public class CarOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 汽车id
     */
    private String carId;

    /**
     * 汽车名称
     */
    private String name;

    /**
     * 汽车品牌
     */
    private String brand;

    /**
     * 汽车类型
     */
    private String type;

    /**
     * 牌照
     */
    private String carNumber;

    /**
     * 小时单价
     */
    private Float price;

    /**
     * 用户账号
     */
    private String loginAccount;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 联系方式
     */
    private String tel;

    private String images;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 驾驶证
     */
    private String image;

    /**
     * 状态
     */
    private Integer state;

    private Float deposit;

    /**
     * 还车时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date returnTime;

    /**
     * 金额
     */
    private Float money;

    /**
     * 租凭审核
     */
    private Integer bound;

    /**
     * 还车审核
     */
    private Integer back;

    private Float durat;

    /**
     * 创建者
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private Integer pageNumber;

    @TableField(exist = false)
    private Integer pageSize;
}
