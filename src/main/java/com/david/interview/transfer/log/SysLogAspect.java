/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.david.interview.transfer.log;

import com.david.interview.transfer.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;


/**
 * 系统日志，切面处理类
 */
@Slf4j
@Aspect
@Component
public class SysLogAspect {

    @Pointcut("@annotation(com.david.interview.transfer.log.SysLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = null;
        try {
            result = point.proceed();
            //打印log执行
            log.info("exec success:{} ip:{} spend:{} result:{}",
                    ((MethodSignature) point.getSignature()).getMethod().getAnnotation(SysLog.class).value(),
                    HttpUtils.getIp(),
                    System.currentTimeMillis() - beginTime,
                    result);
        } catch (Throwable throwable) {
            //打印log执行
            log.error("exec error:{} ip:{} spend:{} result:{}",
                    ((MethodSignature) point.getSignature()).getMethod().getAnnotation(SysLog.class).value(),
                    HttpUtils.getIp(),
                    System.currentTimeMillis() - beginTime,
                    result);
        }
        return result;
    }
}
