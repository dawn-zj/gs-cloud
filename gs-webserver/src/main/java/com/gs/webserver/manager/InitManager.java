package com.gs.webserver.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Administator
 */
@Slf4j
@Component
public class InitManager {

    /**
     * 项目启动时执行的初始化
     */
    @PostConstruct
    public void serverInit() {
        try {
            log.info("初始化完成");
        } catch (Throwable e) {
            log.error("服务初始化发生错误", e);
        }
    }
}
