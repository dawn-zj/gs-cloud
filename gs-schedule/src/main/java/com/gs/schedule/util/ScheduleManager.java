package com.gs.schedule.util;

import com.alibaba.fastjson.JSON;
import com.gs.common.util.ConfigUtil;
import com.gs.schedule.config.ScheduleRunnable;
import com.gs.schedule.config.entity.ScheduleTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administator
 */
@Slf4j
@Component
public class ScheduleManager implements DisposableBean {

    private Map<ScheduleRunnable, ScheduleTask> scheduledTasksMap = new ConcurrentHashMap<>(16);

    @Autowired
    private TaskScheduler taskScheduler;


    public ScheduleTask scheduledCronTask(ScheduleRunnable runnable, ScheduleTask task) {
        // 一定要用spring的CronTask
        CronTask cronTask = new CronTask(runnable, task.getCron());
        task.future = this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
        return task;
    }
    /**
     * 添加任务
     * @param task
     */
    public void addTasks(ScheduleTask task) {
        ScheduleRunnable scheduleRunnable = new ScheduleRunnable(task.getBeanName(), task.getMethodName(), task.getParams(), task.getCron());
        scheduledTasksMap.put(scheduleRunnable, scheduledCronTask(scheduleRunnable, task));
        log.info("新增任务：bean：[{}]，方法：[{}]，参数：[{}]，时机：[{}]", task.getBeanName(), task.getMethodName(), task.getParams(), task.getCron());
    }

    /**
     * 更新指定任务的方法参数和执行时机
     * @param taskOld 旧任务
     * @param task 新任务
     */
    public void updateTasks(ScheduleRunnable taskOld, ScheduleTask task) {
        removeTasks(taskOld);

        ScheduleRunnable scheduleRunnable = new ScheduleRunnable(taskOld.getBeanName(), taskOld.getMethodName(), task.getParams(), task.getCron());
        scheduledTasksMap.put(scheduleRunnable, scheduledCronTask(scheduleRunnable, task));
        log.info("更新任务：bean：[{}]，方法：[{}]，参数：[{}]，时机：[{}]", task.getBeanName(), task.getMethodName(), task.getParams(), task.getCron());
    }

    /**
     * 删除任务
     * @param task
     */
    public void removeTasks(ScheduleRunnable task) {
        ScheduleTask scheduledTask = this.scheduledTasksMap.remove(task);
        if(scheduledTask != null) {
            scheduledTask.cancel();
        }
        log.info("删除任务：bean：[{}]，方法：[{}]，参数：[{}]", task.getBeanName(), task.getMethodName(), task.getParams());
    }

    /**
     * 读取properties配置文件，自动注册
     * @return
     */
    public Map<ScheduleRunnable, ScheduleTask> registerScheduledTasks() {
        String scheduleTask = ConfigUtil.getInstance().getScheduleTask();
        return registerScheduledTasks(scheduleTask);
    }

    /**
     * 注册定时任务
     * @param scheduleTask 任务详细
     * @return
     */
    public Map<ScheduleRunnable, ScheduleTask> registerScheduledTasks(String scheduleTask) {
        // scheduleTask = [{"beanName":"com.gs.schedule.task.TestTask","methodName":"test1","params":"","cron":"0/15 * * * * ?"}]
        List<ScheduleTask> list = JSON.parseArray(scheduleTask, ScheduleTask.class);

        // 已注册任务列表为空，全部注册
        if (this.scheduledTasksMap.size() == 0) {
            for (ScheduleTask task: list) {
                addTasks(task);
            }
            return scheduledTasksMap;
        }

        // 已注册任务列表不为空
        for (ScheduleTask task: list) {
            Set<ScheduleRunnable> scheduleRunnables = this.scheduledTasksMap.keySet();
            for (ScheduleRunnable taskOld : scheduleRunnables) {
                // 类名+方法名 标识唯一任务
                if (taskOld.getBeanName().equals(task.getBeanName()) && taskOld.getMethodName().equals(task.getMethodName())) {
                    // cron 变了：删除原任务，添加新任务
                    if (!taskOld.getCron().equals(task.getCron())) {
                        updateTasks(taskOld, task);
                    }
                } else {
                    addTasks(task);
                }
            }

        }
        return scheduledTasksMap;
    }

    @Override
    public void destroy() throws Exception {
        //项目停止时终止所有的定时任务
        for (ScheduleTask task : this.scheduledTasksMap.values()) {
            task.cancel();
        }
        this.scheduledTasksMap.clear();
    }
}
