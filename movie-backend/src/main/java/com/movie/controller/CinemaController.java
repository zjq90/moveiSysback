package com.movie.controller;

import com.movie.common.Result;
import com.movie.entity.Cinema;
import com.movie.entity.Hall;
import com.movie.service.CinemaService;
import com.movie.service.HallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 影院管理控制器
 * 处理影院相关的HTTP请求
 * 
 * @author Movie Team
 */
@RestController
@RequestMapping("/cinema")
public class CinemaController {

    @Autowired
    private CinemaService cinemaService;

    @Autowired
    private HallService hallService;

    /**
     * 查询所有影院
     * 
     * @return 影院列表
     */
    @GetMapping("/findAllCinemas")
    public Result<List<Cinema>> findAllCinemas() {
        List<Cinema> list = cinemaService.findAllCinemas();
        return Result.success((long) list.size(), list);
    }

    /**
     * 根据影院ID查询影院
     * 
     * @param cinemaId 影院ID
     * @return 影院信息
     */
    @GetMapping("/findCinemaById")
    public Result<Cinema> findCinemaById(@RequestParam("cinemaId") Long cinemaId) {
        Cinema cinema = cinemaService.findCinemaById(cinemaId);
        if (cinema != null) {
            List<Hall> hallList = hallService.findHallByCinemaId(cinemaId);
            cinema.setHallList(hallList);
            return Result.success(cinema);
        }
        return Result.error("影院不存在");
    }

    /**
     * 新增影院
     * 
     * @param cinema 影院对象
     * @return 新增结果
     */
    @PostMapping("/addCinema")
    public Result<String> addCinema(@RequestBody Cinema cinema) {
        Integer result = cinemaService.addCinema(cinema);
        if (result > 0) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    /**
     * 更新影院信息
     * 
     * @param cinema 影院对象
     * @return 更新结果
     */
    @PostMapping("/updateCinema")
    public Result<String> updateCinema(@RequestBody Cinema cinema) {
        Integer result = cinemaService.updateCinema(cinema);
        if (result > 0) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    /**
     * 删除影院
     * 
     * @param cinemaId 影院ID
     * @return 删除结果
     */
    @PostMapping("/deleteCinema")
    public Result<String> deleteCinema(@RequestParam("cinemaId") Long cinemaId) {
        Integer result = cinemaService.deleteCinema(cinemaId);
        if (result > 0) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 根据电影ID查询放映该电影的影院列表
     * 
     * @param movieId 电影ID
     * @return 影院列表
     */
    @GetMapping("/findCinemasByMovieId")
    public Result<List<Cinema>> findCinemasByMovieId(@RequestParam("movieId") Long movieId) {
        List<Cinema> list = cinemaService.findCinemasByMovieId(movieId);
        return Result.success((long) list.size(), list);
    }

    /**
     * 查询所有影院及放映厅信息
     * 
     * @return 影院及放映厅列表
     */
    @GetMapping("/findCinemasWithHalls")
    public Result<List<Map<String, Object>>> findCinemasWithHalls() {
        List<Cinema> cinemaList = cinemaService.findAllCinemas();
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Cinema cinema : cinemaList) {
            Map<String, Object> cinemaMap = new HashMap<>();
            cinemaMap.put("cinemaId", cinema.getCinemaId());
            cinemaMap.put("cinemaName", cinema.getCinemaName());
            cinemaMap.put("cinemaAddress", cinema.getCinemaAddress());
            
            List<Hall> hallList = hallService.findHallByCinemaId(cinema.getCinemaId());
            cinemaMap.put("hallList", hallList);
            
            result.add(cinemaMap);
        }
        
        return Result.success((long) result.size(), result);
    }
}
