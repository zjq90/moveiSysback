package com.movie.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 影院实体类
 * 对应数据库表 cinema
 * 
 * @author Movie Team
 */
@Data
public class Cinema implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 影院ID
     */
    private Long cinemaId;

    /**
     * 影院名称
     */
    private String cinemaName;

    /**
     * 影院地址
     */
    private String cinemaAddress;

    /**
     * 影院所属放映厅列表（非数据库字段）
     */
    private List<Hall> hallList;
}
