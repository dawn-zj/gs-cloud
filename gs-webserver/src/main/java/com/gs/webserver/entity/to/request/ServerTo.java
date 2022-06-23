package com.gs.webserver.entity.to.request;

import com.gs.common.entity.Cpu;
import com.gs.common.entity.Disk;
import com.gs.common.entity.Memory;
import com.gs.common.entity.ServerInfo;
import lombok.Data;

import java.util.List;

@Data
public class ServerTo {
    private ServerInfo server;
    private Memory memory;
    private Cpu cpu;
    private List<Disk> disks;

}
