package com.gs.webserver.entity.to.request;

import lombok.Data;

/**
 * @Author Zhang Juan
 * @Date 2024/4/24 16:01
 */
@Data
public class JsonTo {
    private String filePath;
    private int index;
    private Object data;
}
