package com.gs.webserver.entity.to.request;

import lombok.Data;

@Data
public class Base64To {

    /**
     * 原文
     */
    private String content;

    /**
     * 编码结果
     */
    private String contentB64;
}
