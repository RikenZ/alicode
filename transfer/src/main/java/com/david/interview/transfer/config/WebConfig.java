package com.david.interview.transfer.config;


import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;
import java.util.concurrent.Executor;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Bean
    public Executor reverseSyncThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setDaemon(false);
        executor.setThreadPriority(7);
        executor.setCorePoolSize(5);//核心线程数
        executor.setAllowCoreThreadTimeOut(true);
        executor.setMaxPoolSize(7);//最大线程数
        executor.setQueueCapacity(256);//队列大小
        executor.setKeepAliveSeconds(60);//线程最大空闲时间
        executor.setThreadNamePrefix("poll-");//指定用于新创建的线程名称的前缀
        executor.setAwaitTerminationSeconds(30);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setRejectedExecutionHandler((r, executor1) -> {
            if (!executor1.isShutdown()) {
                try {
                    executor1.getQueue().put(r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        executor.initialize();
        return executor;
    }

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
