package com.gs.webserver.entity.to.response.pdf;

import com.gs.common.entity.pdf.StampInfo;
import lombok.Data;

import java.util.List;

/**
 * @author Administator
 */
@Data
public class PdfStampVerifyResTo {
    /**
     * 验证结果
     */
    private boolean result;
    /**
     * 签章列表
     */
    private List<StampInfo> signList;
}
