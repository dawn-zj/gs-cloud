package com.gs.webserver.controller.monitor;

import com.gs.webserver.entity.to.ResponseTo;
import com.gs.webserver.entity.to.ServerTo;
import com.gs.webserver.service.IMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor")
public class MonitorController {
    @Autowired
    private IMonitorService monitorService;

    @GetMapping
    public ResponseTo server() throws Exception {
        ServerTo serverTo = monitorService.server();
        return ResponseTo.success(serverTo);
    }
}
