package com.movie.controller;

import com.movie.common.Result;
import com.movie.entity.Cinema;
import com.movie.entity.Hall;
import com.movie.service.CinemaService;
import com.movie.service.HallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 放映厅管理控制器
 * 处理放映厅相关的HTTP请求
 * 
 * @author Movie Team
 */
@RestController
@RequestMapping("/hall")
public class HallController {

    @Autowired
    private HallService hallService;

    @Autowired
    private CinemaService cinemaService;

    /**
     * 根据影院ID查询所有放映厅
     * 
     * @param cinemaId 影院ID
     * @return 放映厅列表
     */
    @GetMapping("/findHallByCinemaId")
    public Result<List<Hall>> findHallByCinemaId(@RequestParam("cinemaId") Long cinemaId) {
        List<Hall> list = hallService.findHallByCinemaId(cinemaId);
        return Result.success((long) list.size(), list);
    }

    /**
     * 根据放映厅ID查询放映厅
     * 
     * @param hallId 放映厅ID
     * @return 放映厅信息
     */
    @GetMapping("/findHallById")
    public Result<Hall> findHallById(@RequestParam("hallId") Long hallId) {
        Hall hall = hallService.findHallById(hallId);
        if (hall != null) {
            Cinema cinema = cinemaService.findCinemaById(hall.getCinemaId());
            hall.setCinema(cinema);
            return Result.success(hall);
        }
        return Result.error("放映厅不存在");
    }

    /**
     * 新增放映厅
     * 
     * @param hall 放映厅对象
     * @return 新增结果
     */
    @PostMapping("/addHall")
    public Result<String> addHall(@RequestBody Hall hall) {
        if (hall.getHallCapacity() == null) {
            hall.setHallCapacity(144);
        }
        
        Integer result = hallService.addHall(hall);
        if (result > 0) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    /**
     * 更新放映厅信息
     * 
     * @param hall 放映厅对象
     * @return 更新结果
     */
    @PostMapping("/updateHall")
    public Result<String> updateHall(@RequestBody Hall hall) {
        Integer result = hallService.updateHall(hall);
        if (result > 0) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    /**
     * 删除放映厅
     * 
     * @param hallId 放映厅ID
     * @return 删除结果
     */
    @PostMapping("/deleteHall")
    public Result<String> deleteHall(@RequestParam("hallId") Long hallId) {
        Integer result = hallService.deleteHall(hallId);
        if (result > 0) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 根据影院名称和放映厅名称查询放映厅
     * 
     * @param cinemaName 影院名称
     * @param hallName 放映厅名称
     * @return 放映厅信息
     */
    @GetMapping("/findHallByCinemaAndHallName")
    public Result<Hall> findHallByCinemaAndHallName(@RequestParam("cinemaName") String cinemaName,
                                                       @RequestParam("hallName") String hallName) {
        Hall hall = hallService.findHallByCinemaAndHallName(cinemaName, hallName);
        if (hall != null) {
            return Result.success(hall);
        }
        return Result.error("放映厅不存在");
    }
}
