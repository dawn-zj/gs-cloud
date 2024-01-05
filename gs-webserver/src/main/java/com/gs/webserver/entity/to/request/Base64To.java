package com.gs.webserver.entity.to.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Administator
 */
@Data
public class Base64To {

    @Data
    public static class Base64ReqTo {

        /**
         * 原文
         */
        @NotNull
        private String content;

    }

}
