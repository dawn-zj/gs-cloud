package com.gs.webserver.entity.to.request;

import lombok.Data;

/**
 * @Author Zhang Juan
 * @Date 2024/4/30 14:27
 */
@Data
public class StampTo {
    /**
     * x坐标，单位磅
     */
    private float x;
    /**
     * y坐标，单位磅
     */
    private float y;
    /**
     * 页码
     */
    private int pageNum;
    /**
     * 印章id
     */
    private Long sealId;
    /**
     * 印章信息
     */
    private SealInfo sealInfo;
}
