package com.gs.webserver.schedule.task;

import com.gs.common.util.ConfigUtil;
import com.gs.schedule.util.ScheduleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Administator
 */
@Slf4j
@Component
public class ConfigSchedule {

    @Autowired
    private ScheduleManager scheduleManager;

    /**
     * 同步config信息，一分钟一次
     */
    @Scheduled(fixedDelay = 60000)
    private void loadConfig() {
        try {
            ConfigUtil.getInstance().reload();
        } catch (Exception e) {
            log.error("参数配置-任务发生错误", e);
        }
    }

    /**
     * 同步config信息
     */
    @Scheduled(cron = "* 0/1 * * * ?")
    private void loadSchedule() {
        try {
            String scheduleTask = ConfigUtil.getInstance().getScheduleTask();
            scheduleManager.registerScheduledTasks(scheduleTask);
        } catch (Exception e) {
            log.error("参数配置-任务发生错误", e);
        }
    }
}
