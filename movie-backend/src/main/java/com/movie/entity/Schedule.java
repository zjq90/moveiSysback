package com.movie.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 场次实体类
 * 对应数据库表 schedule
 * 
 * @author Movie Team
 */
@Data
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 场次ID
     */
    private Long scheduleId;

    /**
     * 所属放映厅ID
     */
    private Long hallId;

    /**
     * 放映的电影ID
     */
    private Long movieId;

    /**
     * 电影放映时间
     */
    private String scheduleStartTime;

    /**
     * 票价
     */
    private Integer schedulePrice;

    /**
     * 剩余座位数
     */
    private Integer scheduleRemain;

    /**
     * 场次状态
     * 1：上映中
     * 0：下架
     */
    private Integer scheduleState;

    /**
     * 所属放映厅（非数据库字段）
     */
    private Hall scheduleHall;

    /**
     * 放映的电影（非数据库字段）
     */
    private Movie scheduleMovie;

    /**
     * 订单列表（非数据库字段）
     */
    private List<Order> orderList;
}
