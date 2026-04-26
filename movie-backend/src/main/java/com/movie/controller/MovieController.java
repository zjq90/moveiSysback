package com.movie.controller;

import com.movie.common.Result;
import com.movie.entity.Cinema;
import com.movie.entity.Movie;
import com.movie.service.CinemaService;
import com.movie.service.MovieService;
import com.movie.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 电影管理控制器
 * 处理电影相关的HTTP请求：电影列表、详情、增删改查等
 * 
 * @author Movie Team
 */
@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private CinemaService cinemaService;

    /**
     * 根据电影ID查询电影详情
     * 同时查询放映该电影的影院列表
     * 
     * @param movieId 电影ID
     * @return 电影详情和影院列表
     */
    @GetMapping("/findMovieById")
    public Result<Map<String, Object>> findMovieById(@RequestParam("movieId") Long movieId) {
        Movie movie = movieService.findMovieById(movieId);
        List<Cinema> cinemaList = cinemaService.findCinemasByMovieId(movieId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("movie", movie);
        result.put("cinemaList", cinemaList);
        result.put("cinemaCount", cinemaList.size());
        
        return Result.success(result);
    }

    /**
     * 查询所有电影
     * 包括在线电影、即将上映电影、票房排行
     * 
     * @return 电影列表数据
     */
    @GetMapping("/findAllMovies")
    public Result<Map<String, Object>> findAllMovies() {
        List<Movie> onlineList = movieService.findAllMovies(1);
        List<Movie> upcomingList = movieService.findAllMovies(0);
        List<Movie> boxOfficeList = movieService.sortMovieByBoxOffice();
        
        String[] types = {"喜剧", "动作", "爱情", "动画", "科幻", "惊悚", "冒险", "犯罪", "悬疑"};
        List<Map<String, Float>> typeStatistics = new ArrayList<>();
        
        for (String type : types) {
            List<Movie> movieList = movieService.findMoviesLikeType(type);
            float totalBoxOffice = 0;
            for (Movie movie : movieList) {
                totalBoxOffice += movie.getMovieBoxOffice();
            }
            
            Map<String, Float> typeMap = new HashMap<>();
            typeMap.put(type, totalBoxOffice);
            typeStatistics.add(typeMap);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("onlineList", onlineList);
        result.put("onlineCount", onlineList.size());
        result.put("upcomingList", upcomingList);
        result.put("upcomingCount", upcomingList.size());
        result.put("boxOfficeList", boxOfficeList);
        result.put("typeStatistics", typeStatistics);
        
        return Result.success(result);
    }

    /**
     * 根据电影名称模糊查询电影
     * 
     * @param name 电影名称关键词
     * @return 电影列表
     */
    @GetMapping("/findMoviesByName")
    public Result<List<Movie>> findMoviesByName(@RequestParam("name") String name) {
        List<Movie> list = movieService.findMoviesLikeName(name);
        return Result.success((long) list.size(), list);
    }

    /**
     * 根据电影类型查询电影
     * 
     * @param type 电影类型
     * @return 电影列表
     */
    @GetMapping("/findMoviesByType")
    public Result<List<Movie>> findMoviesByType(@RequestParam("type") String type) {
        List<Movie> list = movieService.findMoviesLikeType(type);
        return Result.success((long) list.size(), list);
    }

    /**
     * 排序查询电影
     * 
     * @param order 排序方式：热门(commentCount)、时间(releaseDate)、评价(score)
     * @return 排序后的电影列表
     */
    @GetMapping("/sortAllMovies")
    public Result<List<Movie>> sortAllMovies(@RequestParam("order") String order) {
        List<Movie> list;
        switch (order) {
            case "热门":
                list = movieService.sortMovieByCount();
                break;
            case "时间":
                list = movieService.sortMovieByDate();
                break;
            case "评价":
                list = movieService.sortMovieByScore();
                break;
            default:
                list = movieService.sortMovieByDate();
        }
        return Result.success((long) list.size(), list);
    }

    /**
     * 删除电影（下架）
     * 
     * @param movieId 电影ID
     * @return 删除结果
     */
    @PostMapping("/deleteMovie")
    public Result<String> deleteMovie(@RequestParam("movieId") Long movieId) {
        Integer result = movieService.deleteMovie(movieId);
        if (result > 0) {
            return Result.success("下架成功");
        }
        return Result.error("下架失败");
    }

    /**
     * 新增电影
     * 
     * @param file 电影海报文件
     * @param movie 电影对象
     * @param request HTTP请求对象
     * @return 新增结果
     * @throws IOException IO异常
     */
    @PostMapping("/addMovie")
    public Result<String> addMovie(@RequestParam(value = "file", required = false) MultipartFile file,
                                    Movie movie,
                                    HttpServletRequest request) throws IOException {
        if (file != null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUIDUtil.getUUID() + suffix;
            
            String uploadDir = request.getServletContext().getRealPath("/upload/movies");
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            String filePath = uploadDir + "/" + fileName;
            file.transferTo(new File(filePath));
            
            movie.setMoviePicture("/upload/movies/" + fileName);
        }
        
        Date releaseDate = new Date(System.currentTimeMillis());
        movie.setMovieReleaseDate(releaseDate);
        
        Random random = new Random();
        float score = 5 + random.nextFloat() * 4;
        DecimalFormat df = new DecimalFormat("##0.0");
        movie.setMovieScore(Float.parseFloat(df.format(score)));
        
        Integer result = movieService.addMovie(movie);
        if (result > 0) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    /**
     * 更新电影信息
     * 
     * @param file 电影海报文件（可选）
     * @param movie 电影对象
     * @param request HTTP请求对象
     * @return 更新结果
     * @throws IOException IO异常
     */
    @PostMapping("/updateMovie")
    public Result<String> updateMovie(@RequestParam(value = "file", required = false) MultipartFile file,
                                       Movie movie,
                                       HttpServletRequest request) throws IOException {
        if (file != null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUIDUtil.getUUID() + suffix;
            
            String uploadDir = request.getServletContext().getRealPath("/upload/movies");
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            String filePath = uploadDir + "/" + fileName;
            file.transferTo(new File(filePath));
            
            movie.setMoviePicture("/upload/movies/" + fileName);
        } else {
            Movie oldMovie = movieService.findMovieById(movie.getMovieId());
            if (oldMovie != null) {
                movie.setMoviePicture(oldMovie.getMoviePicture());
            }
        }
        
        Integer result = movieService.updateMovie(movie);
        if (result > 0) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    /**
     * 查询票房排行
     * 
     * @return 票房排行列表
     */
    @GetMapping("/findBoxOfficeMovies")
    public Result<List<Movie>> findBoxOfficeMovies() {
        List<Movie> list = movieService.sortMovieByBoxOffice();
        return Result.success((long) list.size(), list);
    }
}
