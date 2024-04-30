package com.gs.webserver.entity.to.request;

import lombok.Data;

/**
 * @Author Zhang Juan
 * @Date 2024/4/30 14:27
 */
@Data
public class SealInfo {
    /**
     * 图片base64
     */
    private String base64;
    /**
     * 图片dpi
     */
    private String dpi;
    /**
     * 图片宽度
     */
    private float height;
    /**
     * 图片高度
     */
    private float width;
    /**
     * 印章名称
     */
    private String name;
}
