package com.movie.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * 静态资源配置类
 * 配置图片等静态资源的访问路径
 * 
 * @author Movie Team
 */
@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectRoot = System.getProperty("user.dir");
        File projectDir = new File(projectRoot);
        
        String uploadPath = null;
        
        File uploadDir = new File(projectDir, "upload");
        if (uploadDir.exists() && uploadDir.isDirectory()) {
            uploadPath = uploadDir.getAbsolutePath() + File.separator;
        }
        
        if (uploadPath == null) {
            File parentDir = projectDir.getParentFile();
            if (parentDir != null) {
                File movieMasterUpload = new File(parentDir, "Movie-master" + File.separator + "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "upload");
                if (movieMasterUpload.exists() && movieMasterUpload.isDirectory()) {
                    uploadPath = movieMasterUpload.getAbsolutePath() + File.separator;
                }
            }
        }
        
        if (uploadPath != null) {
            registry.addResourceHandler("/upload/**")
                    .addResourceLocations("file:" + uploadPath);
        }
        
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
    }
}
