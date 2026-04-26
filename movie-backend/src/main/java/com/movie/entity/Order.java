package com.movie.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 订单实体类
 * 对应数据库表 orderinfo
 * 
 * @author Movie Team
 */
@Data
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 所属用户ID
     */
    private Long userId;

    /**
     * 所属场次ID
     */
    private Long scheduleId;

    /**
     * 电影票座位信息
     * 格式：x排x座
     */
    private String orderPosition;

    /**
     * 订单状态
     * 0：退票中
     * 1：已支付
     * 2：退票成功
     */
    private Integer orderState;

    /**
     * 订单价格
     */
    private Integer orderPrice;

    /**
     * 订单支付时间
     */
    private Date orderTime;

    /**
     * 所属用户（非数据库字段）
     */
    private User orderUser;

    /**
     * 所属场次（非数据库字段）
     */
    private Schedule orderSchedule;
}
