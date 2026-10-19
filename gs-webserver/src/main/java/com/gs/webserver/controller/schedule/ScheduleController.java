package com.gs.webserver.controller.schedule;

import com.gs.schedule.config.entity.ScheduleTask;
import com.gs.schedule.util.PackageUtil;
import com.gs.webserver.entity.to.response.ResponseTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 定时任务
 * @author Administator
 */
@RestController
@RequestMapping("/schedule/")
public class ScheduleController {

    /**
     * 定时任务列表
     * @return 定时任务列表
     */
    @GetMapping("list")
    public ResponseTo<?> list() {
        // 任务名称、任务类型、类路径、启停状态、执行时机
        return ResponseTo.success("");
    }

    /**
     * 内置任务列表
     * @return 内置任务列表
     */
    @GetMapping("listTask")
    public ResponseTo<?> listTask() throws Exception {
        ArrayList<ScheduleTask> scheduleTaskList = new ArrayList<>();
        // 内置的任务类型、类路径
        // com.gs.schedule.task包下均为内置任务类
        String pkgName = "com.gs.schedule.task";
        List<Class<?>> classes = PackageUtil.getClasses(pkgName);
        for (Class<?> clazz : classes) {
            String beanName = clazz.getName();
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                String methodName = method.getName();
                ScheduleTask scheduleTask = new ScheduleTask(beanName, methodName, null, null);
                scheduleTaskList.add(scheduleTask);
            }
        }
        return ResponseTo.success(scheduleTaskList);
    }

}
