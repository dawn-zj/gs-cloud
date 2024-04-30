package com.gs.webserver.entity.to.request;

import lombok.Data;

/**
 * @Author Zhang Juan
 * @Date 2024/4/24 16:01
 */
@Data
public class LoginLogTo {
    private String username;
    private Long loginTime;

    /** 登录状态 0成功 1失败 */
    private String status = "0";

    /** 登录IP地址 */
    private String ipaddr;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;
}
