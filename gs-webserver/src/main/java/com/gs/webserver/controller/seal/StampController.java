package com.gs.webserver.controller.seal;

import com.gs.webserver.entity.to.response.ResponseTo;
import com.gs.webserver.entity.to.response.pdf.PdfStampVerifyResTo;
import com.gs.webserver.service.IStampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 签章
 * @author Administator
 */
@RestController
@RequestMapping("/seal/stamp")
public class StampController {
    @Autowired
    @Qualifier("pdfStampServiceImpl")
    private IStampService stampService;

    /**
     * 验证签章文件
     * @param file 签章文件
     * @return 验证结果
     */
    @PostMapping("/verify")
    public ResponseTo<PdfStampVerifyResTo> verify(@RequestParam("file") MultipartFile file) {
        PdfStampVerifyResTo verify = stampService.verify(file);
        return ResponseTo.success(verify);
    }
}
