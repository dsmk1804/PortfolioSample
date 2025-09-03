package com.bytearchive.config;

import com.bytearchive.converter.MultipartListToFileListConverter;
import com.bytearchive.converter.MultipartToFileListConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MainConfig implements WebMvcConfigurer {
    @Autowired private MultipartListToFileListConverter multipartListToFileListConverter;
    @Autowired private MultipartToFileListConverter multipartToFileListConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(multipartListToFileListConverter);
        registry.addConverter(multipartToFileListConverter);
    }
}
