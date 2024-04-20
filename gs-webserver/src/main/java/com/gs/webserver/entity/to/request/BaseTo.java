package com.gs.webserver.entity.to.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Administator
 */
@Data
public class BaseTo {
    /**
     * 内容
     */
    @NotNull
    private String content;
}
