package com.gs.webserver.controller.schedule;

import com.gs.webserver.entity.to.response.ResponseTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseTo<?> listTask() {
        // 内置的任务类型、类路径
        return ResponseTo.success("");
    }

}
