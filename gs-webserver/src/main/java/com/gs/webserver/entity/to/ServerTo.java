package com.gs.webserver.entity.to;

import com.gs.common.entity.Cpu;
import com.gs.common.entity.Memory;
import com.gs.common.entity.ServerInfo;
import lombok.Data;

@Data
public class ServerTo {
    private ServerInfo server;
    private Memory memory;
    private Cpu cpu;

}
