package com.gs.webserver.controller.monitor;

import com.gs.webserver.entity.to.response.ResponseTo;
import com.gs.webserver.entity.to.request.ServerTo;
import com.gs.webserver.service.IMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统监控
 * @author Administator
 */
@RestController
@RequestMapping("/monitor")
public class MonitorController {
    @Autowired
    private IMonitorService monitorService;

    /**
     * 服务信息
     * @return 服务信息
     * @throws Exception 异常
     */
    @GetMapping
    public ResponseTo<ServerTo> server() throws Exception {
        ServerTo serverTo = monitorService.server();
        return ResponseTo.success(serverTo);
    }
}
