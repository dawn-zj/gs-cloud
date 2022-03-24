package com.gs.webserver.service.impl;

import com.gs.common.util.NetWorkUtil;
import com.gs.webserver.entity.to.ServerTo;
import com.gs.webserver.service.IMonitorService;
import org.springframework.stereotype.Service;

@Service
public class MonitorServiceImpl implements IMonitorService {
    @Override
    public ServerTo server() throws Exception {
        ServerTo serverTo = new ServerTo();

        serverTo.setServer(NetWorkUtil.systemInfo());
        serverTo.setMemory(NetWorkUtil.memoryInfo());
        serverTo.setCpu(NetWorkUtil.cpuInfo());
        return serverTo;
    }
}
