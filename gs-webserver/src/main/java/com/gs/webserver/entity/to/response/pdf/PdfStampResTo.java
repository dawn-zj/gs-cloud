package com.gs.webserver.entity.to.response.pdf;

import lombok.Data;

import java.util.Map;

/**
 * @author Administator
 */
@Data
public class PdfStampResTo {
    /**
     * 签章结构列表
     */
    private Map<String, String> stampMap;
}
