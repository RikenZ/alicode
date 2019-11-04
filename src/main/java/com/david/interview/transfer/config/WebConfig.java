package com.david.interview.transfer.config;


import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(stringConverter());
        converters.add(converter());
        addDefaultHttpMessageConverters(converters);
    }


    @Bean
    FastJsonHttpMessageConverter converter() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        return converter;
    }

    @Bean
    StringHttpMessageConverter stringConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter();
        return converter;
    }

}
