package com.gs.webserver.entity.to.response.pdf;

import lombok.Data;

import java.util.List;

@Data
public class PdfStampVerifyResTo {
    private boolean result;
    private List<StampInfo> signList;
}
