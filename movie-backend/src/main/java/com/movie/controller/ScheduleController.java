package com.movie.controller;

import com.github.pagehelper.PageInfo;
import com.movie.common.Result;
import com.movie.entity.*;
import com.movie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 场次管理控制器
 * 处理场次相关的HTTP请求：场次列表、增删改查等
 * 
 * @author Movie Team
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private CinemaService cinemaService;

    @Autowired
    private HallService hallService;

    /**
     * 根据场次ID查询场次
     * 
     * @param scheduleId 场次ID
     * @return 场次信息
     */
    @GetMapping("/findScheduleById")
    public Result<Schedule> findScheduleById(@RequestParam("scheduleId") Long scheduleId) {
        Schedule schedule = scheduleService.findScheduleById(scheduleId);
        return Result.success(schedule);
    }

    /**
     * 分页按状态查询场次
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param scheduleState 场次状态
     * @return 场次分页列表
     */
    @GetMapping("/findAllScheduleByState")
    public Result<Map<String, Object>> findAllScheduleByState(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                 @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                                                 @RequestParam("scheduleState") int scheduleState) {
        PageInfo<Schedule> info = scheduleService.findAllScheduleByState(page, limit, scheduleState);
        
        List<Integer> incomeList = new ArrayList<>();
        for (Schedule schedule : info.getList()) {
            int income = 0;
            List<Order> orderList = schedule.getOrderList();
            if (orderList != null) {
                for (Order order : orderList) {
                    income += order.getOrderPrice();
                }
            }
            incomeList.add(income);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", info.getList());
        result.put("count", info.getTotal());
        result.put("incomeList", incomeList);
        
        return Result.success(result);
    }

    /**
     * 分页查询所有场次
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @return 场次分页列表
     */
    @GetMapping("/findAllSchedule")
    public Result<Map<String, Object>> findAllSchedule(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        PageInfo<Schedule> info = scheduleService.findAllSchedule(page, limit);
        List<Movie> movieList = movieService.findAllMovies(1);
        List<Cinema> cinemaList = cinemaService.findAllCinemas();
        
        List<String> movieNameList = new ArrayList<>();
        for (Movie movie : movieList) {
            movieNameList.add(movie.getMovieCnName());
        }
        
        List<Map<String, Object>> cinemaInfoList = new ArrayList<>();
        for (Cinema cinema : cinemaList) {
            Map<String, Object> cinemaMap = new HashMap<>();
            List<Hall> hallList = hallService.findHallByCinemaId(cinema.getCinemaId());
            cinemaMap.put(cinema.getCinemaName(), hallList);
            cinemaInfoList.add(cinemaMap);
        }
        
        List<Integer> incomeList = new ArrayList<>();
        for (Schedule schedule : info.getList()) {
            int income = 0;
            List<Order> orderList = schedule.getOrderList();
            if (orderList != null) {
                for (Order order : orderList) {
                    income += order.getOrderPrice();
                }
            }
            incomeList.add(income);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", info.getList());
        result.put("count", info.getTotal());
        result.put("movieNameList", movieNameList);
        result.put("cinemaInfoList", cinemaInfoList);
        result.put("incomeList", incomeList);
        
        return Result.success(result);
    }

    /**
     * 根据电影名称模糊查询场次
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param movieName 电影名称
     * @return 场次分页列表
     */
    @GetMapping("/findScheduleByMovieName")
    public Result<Map<String, Object>> findScheduleByMovieName(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                  @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                                                  @RequestParam("movieName") String movieName) {
        PageInfo<Schedule> info = scheduleService.findScheduleByMovieName(page, limit, movieName);
        
        List<Integer> incomeList = new ArrayList<>();
        for (Schedule schedule : info.getList()) {
            int income = 0;
            List<Order> orderList = schedule.getOrderList();
            if (orderList != null) {
                for (Order order : orderList) {
                    income += order.getOrderPrice();
                }
            }
            incomeList.add(income);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", info.getList());
        result.put("count", info.getTotal());
        result.put("incomeList", incomeList);
        
        return Result.success(result);
    }

    /**
     * 根据电影名称模糊查询已下架场次
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param movieName 电影名称
     * @return 场次分页列表
     */
    @GetMapping("/findOffScheduleByMovieName")
    public Result<Map<String, Object>> findOffScheduleByMovieName(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                     @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                                                     @RequestParam("movieName") String movieName) {
        PageInfo<Schedule> info = scheduleService.findOffScheduleByMovieName(page, limit, movieName);
        
        List<Integer> incomeList = new ArrayList<>();
        for (Schedule schedule : info.getList()) {
            int income = 0;
            List<Order> orderList = schedule.getOrderList();
            if (orderList != null) {
                for (Order order : orderList) {
                    income += order.getOrderPrice();
                }
            }
            incomeList.add(income);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", info.getList());
        result.put("count", info.getTotal());
        result.put("incomeList", incomeList);
        
        return Result.success(result);
    }

    /**
     * 根据影院ID和电影ID查询场次
     * 
     * @param cinemaId 影院ID
     * @param movieId 电影ID
     * @return 场次列表
     */
    @GetMapping("/findScheduleByCinemaAndMovie")
    public Result<List<Schedule>> findScheduleByCinemaAndMovie(@RequestParam("cinemaId") Long cinemaId,
                                                                  @RequestParam("movieId") Long movieId) {
        List<Schedule> list = scheduleService.findScheduleByCineamIdAndMovieId(cinemaId, movieId);
        return Result.success((long) list.size(), list);
    }

    /**
     * 新增场次
     * 
     * @param movieName 电影名称
     * @param hallName 放映厅名称
     * @param cinemaName 影院名称
     * @param schedulePrice 票价
     * @param scheduleStartTime 放映时间
     * @return 新增结果
     */
    @PostMapping("/addSchedule")
    public Result<String> addSchedule(@RequestParam("movieName") String movieName,
                                        @RequestParam("hallName") String hallName,
                                        @RequestParam("cinemaName") String cinemaName,
                                        @RequestParam("schedulePrice") int schedulePrice,
                                        @RequestParam("scheduleStartTime") String scheduleStartTime) {
        Movie movie = movieService.findMovieByName(movieName);
        if (movie == null) {
            return Result.error("电影不存在");
        }
        
        Hall hall = hallService.findHallByCinemaAndHallName(cinemaName, hallName);
        if (hall == null) {
            return Result.error("放映厅不存在");
        }
        
        Schedule schedule = new Schedule();
        schedule.setMovieId(movie.getMovieId());
        schedule.setHallId(hall.getHallId());
        schedule.setSchedulePrice(schedulePrice);
        schedule.setScheduleStartTime(scheduleStartTime);
        schedule.setScheduleRemain(hall.getHallCapacity());
        
        Integer result = scheduleService.addSchedule(schedule);
        if (result > 0) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    /**
     * 更新场次价格
     * 
     * @param scheduleId 场次ID
     * @param schedulePrice 新票价
     * @return 更新结果
     */
    @PostMapping("/updateSchedulePrice")
    public Result<String> updateSchedulePrice(@RequestParam("scheduleId") Long scheduleId,
                                                @RequestParam("schedulePrice") int schedulePrice) {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(scheduleId);
        schedule.setSchedulePrice(schedulePrice);
        
        Integer result = scheduleService.updateSchedule(schedule);
        if (result > 0) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    /**
     * 下架场次
     * 
     * @param scheduleId 场次ID
     * @return 下架结果
     */
    @PostMapping("/offlineSchedule")
    public Result<String> offlineSchedule(@RequestParam("scheduleId") Long scheduleId) {
        Integer result = scheduleService.deleteSchedule(scheduleId);
        if (result > 0) {
            return Result.success("下架成功");
        }
        return Result.error("下架失败");
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
}
