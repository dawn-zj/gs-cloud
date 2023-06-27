package com.gs.webserver.entity.to.response.pdf;

import lombok.Data;

import java.util.Map;

@Data
public class PdfStampResTo {
    private Map<String, String> stampMap;
}
