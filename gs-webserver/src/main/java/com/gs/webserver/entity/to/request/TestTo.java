package com.gs.webserver.entity.to.request;

import lombok.Data;

/**
 * @author Administator
 */
public class TestTo {

    @Data
    public static class GetInfoParams {
        /**
         * id
         */
        private Long id;
        /**
         * 名称
         */
        private String name;
        /**
         * 状态
         */
        private Integer status;
    }

    @Data
    public static class AddParams {
        /**
         * 根证base64
         */
        private String fileBase64;
    }

    @Data
    public static class UpdateParams {
        /**
         * 根证base64
         */
        private String fileBase64;
    }

    @Data
    public static class DeleteParams {
        /**
         * id
         */
        private Long id;
    }

}
