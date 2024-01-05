package com.gs.webserver.entity.to.request;

import com.gs.common.entity.Cpu;
import com.gs.common.entity.Disk;
import com.gs.common.entity.Memory;
import com.gs.common.entity.ServerInfo;
import lombok.Data;

import java.util.List;

/**
 * @author Administator
 */
@Data
public class ServerTo {
    /**
     * 服务信息
     */
    private ServerInfo server;

    /**
     * 内存信息
     */
    private Memory memory;

    /**
     * CPU信息
     */
    private Cpu cpu;

    /**
     * 磁盘信息
     */
    private List<Disk> disks;

}
