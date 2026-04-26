package com.movie.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * 放映厅实体类
 * 对应数据库表 hall
 * 
 * @author Movie Team
 */
@Data
public class Hall implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 放映厅ID
     */
    private Long hallId;

    /**
     * 放映厅名称
     */
    private String hallName;

    /**
     * 放映厅容量
     * 默认144（12x12）
     */
    private Integer hallCapacity;

    /**
     * 所属影院ID
     */
    private Long cinemaId;

    /**
     * 所属影院（非数据库字段）
     */
    private Cinema cinema;
}
