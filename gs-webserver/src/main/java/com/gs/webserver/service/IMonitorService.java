package com.gs.webserver.service;

import com.gs.webserver.entity.to.request.ServerTo;

public interface IMonitorService {
    ServerTo server() throws Exception;
}
