package com.gs.schedule.config.entity;

import lombok.Data;

import java.util.concurrent.ScheduledFuture;

/**
 * @author Administator
 */
@Data
public class ScheduleTask {
    private String beanName;
    private String methodName;
    private Object params;
    private String cron;

    public volatile ScheduledFuture<?> future;

    public ScheduleTask(String beanName, String methodName, Object params, String cron) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.params = params;
        this.cron = cron;
    }

    /**
     * 取消定时任务
     */
    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if(future != null) {
            future.cancel(true);
        }
    }
}
