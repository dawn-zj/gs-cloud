package com.gs.webserver.entity.to.response.key;

import lombok.Data;

/**
 * @Author Zhang Juan
 * @Date 2024/3/22 11:21
 */
@Data
public class KeyResTo {
    /**
     * 公钥
     */
    private String publicKeyB64;
    /**
     * 私钥
     */
    private String privateKeyB64;
}
