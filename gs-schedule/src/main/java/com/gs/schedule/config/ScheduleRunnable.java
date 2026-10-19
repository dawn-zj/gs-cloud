package com.gs.schedule.config;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @author Administator
 */
@Data
public class ScheduleRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleRunnable.class);

    private String beanName;
    private String methodName;
    private Object params;
    private String cron;

    public ScheduleRunnable(String beanName, String methodName, Object params, String cron) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.params = params;
        this.cron = cron;
    }

    @Override
    public void run() {
        logger.debug("定时任务开始执行 - bean：[{}]，方法：[{}]，参数：[{}]", beanName, methodName, params.toString());
        long startTime = System.currentTimeMillis();

        try {
            Object target = Class.forName(beanName).newInstance();
            Method method = target.getClass().getDeclaredMethod(methodName);
            ReflectionUtils.makeAccessible(method);
            method.invoke(target, null);
        } catch (Exception ex) {
            logger.error(String.format("定时任务执行异常 - bean：%s，方法：%s，参数：%s ", beanName, methodName, params), ex);
        }

        long times = System.currentTimeMillis() - startTime;
        logger.debug("定时任务执行结束 - bean：[{}]，方法：[{}]，参数：[{}]，耗时：[{}] 毫秒", beanName, methodName, params, times);
    }

}
