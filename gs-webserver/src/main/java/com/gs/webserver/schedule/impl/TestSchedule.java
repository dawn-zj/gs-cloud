package com.gs.webserver.schedule.impl;

import com.gs.common.util.date.DateUtil;
import com.gs.webserver.schedule.ISchedule;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Administator
 */
@Component
@EnableScheduling
public class TestSchedule implements ISchedule, SchedulingConfigurer {

    @Override
    public void start() {
        System.out.println("定时执行了" + DateUtil.getDateTime());
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                try {
                    // 10秒一执行
                    String cron = "0/10 * * * * ?";
                    CronTrigger cronTrigger = new CronTrigger(cron);
                    Date date = cronTrigger.nextExecutionTime(triggerContext);
                    return date;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
}
