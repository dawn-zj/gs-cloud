package com.gs.webserver.controller.tool;

import com.gs.webserver.entity.to.response.ResponseTo;
import com.gs.webserver.entity.to.response.pdf.PdfStampResTo;
import com.gs.webserver.service.IStampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * PDF
 */
@RestController
@RequestMapping("/tool/pdf")
public class PdfController {
    @Autowired
    @Qualifier("pdfStampServiceImpl")
    private IStampService stampService;

    /**
     * 获取签章文件中的签章结构
     * @param file 签章文件
     * @return
     * @throws Exception
     */
    @PostMapping("/getStamp")
    public ResponseTo<PdfStampResTo> getStampFromPdf(@RequestParam("file") MultipartFile file) throws Exception {
        Map<String, String> stampMap = stampService.getStampFromPdf(file);
        PdfStampResTo pdfStampResTo = new PdfStampResTo();
        pdfStampResTo.setStampMap(stampMap);
        return ResponseTo.success(pdfStampResTo);
    }
}
