package com.gs.webserver.entity.to.request;

import lombok.Data;

import java.util.List;

/**
 * @Author Zhang Juan
 * @Date 2024/4/30 14:03
 */
@Data
public class PdfStampTo {
    /**
     * pdf文件base64
     */
    private String pdfBase64;
    /**
     * 签署参数
     */
    private List<StampTo> list;
}