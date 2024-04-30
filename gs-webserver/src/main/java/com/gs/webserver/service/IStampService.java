package com.gs.webserver.service;

import com.gs.webserver.entity.to.request.StampTo;
import com.gs.webserver.entity.to.response.pdf.PdfStampVerifyResTo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author Administator
 */
public interface IStampService {
    /**
     * 签章
     * @param pdfData 原文件
     * @param pfxFilePath pfx文件路径
     * @param pfxFilePassword pfx文件密码
     * @param list 签署参数
     * @return
     * @throws Exception
     */
    byte[] stamp(byte[] pdfData, String pfxFilePath, String pfxFilePassword, List<StampTo> list) throws Exception;

    PdfStampVerifyResTo verify(MultipartFile file);

    Map<String, String> getStampFromPdf(MultipartFile file) throws Exception;
}
