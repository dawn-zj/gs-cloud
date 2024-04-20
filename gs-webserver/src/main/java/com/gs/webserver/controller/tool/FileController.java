package com.gs.webserver.controller.tool;

import com.gs.common.define.Constants;
import com.gs.common.util.FileUtil;
import com.gs.common.util.ZipUtil;
import com.gs.common.util.analysis.impl.AnalysisP7;
import com.gs.common.util.analysis.impl.AnalysisProtectKey;
import com.gs.common.util.analysis.impl.AnalysisSeal;
import com.gs.common.util.analysis.impl.AnalysisStamp;
import com.gs.common.util.base64.Base64Util;
import com.gs.common.util.date.DateUtil;
import com.gs.common.util.ofd.OfdUtil;
import com.gs.common.util.pdf.PdfUtil;
import com.gs.webserver.entity.to.response.CommonResTo;
import com.gs.webserver.entity.to.response.ResponseTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 文件
 * @Author Zhang Juan
 * @Date 2024/3/22 10:44
 */
@RestController
@RequestMapping("/tool/ofd")
public class FileController {

    /**
     * pdf转图片
     * @param file pdf文件
     * @throws Exception
     */
    @PostMapping("/pdfToImg")
    public ResponseTo<CommonResTo> pdfToImg(@RequestParam("file") MultipartFile file) throws Exception {
        List<byte[]> list = PdfUtil.pdfToImg(file.getBytes());
        String fileName = FileUtil.getFileName();
        String imgDir = Constants.TMP_PATH + DateUtil.getDateDir() + fileName + Constants.SPLIT_DIR;
        for (int i = 0; i < list.size(); i++) {
            FileUtil.storeFile(imgDir + i + ".png", list.get(i));
        }

        String zipPath = Constants.TMP_PATH + DateUtil.getDateDir() + fileName + Constants.ZIP_SUFFIX;
        ZipUtil.zipDir(imgDir, zipPath, "");

        byte[] bytes = FileUtil.getFile(zipPath);
        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(Base64Util.encode(bytes));
        return ResponseTo.success(commonTo);
    }


    /**
     * ofd转图片
     * @param file ofd文件
     * @throws Exception
     */
    @PostMapping("/ofdToImg")
    public ResponseTo<CommonResTo> ofdToImg(@RequestParam("file") MultipartFile file) throws Exception {
        List<byte[]> list = OfdUtil.ofdToImg(file.getBytes());
        String fileName = FileUtil.getFileName();
        String imgDir = Constants.TMP_PATH + DateUtil.getDateDir() + fileName + Constants.SPLIT_DIR;
        for (int i = 0; i < list.size(); i++) {
            FileUtil.storeFile(imgDir + i + ".png", list.get(i));
        }

        String zipPath = Constants.TMP_PATH + DateUtil.getDateDir() + fileName + Constants.ZIP_SUFFIX;
        ZipUtil.zipDir(imgDir, zipPath, "");

        byte[] bytes = FileUtil.getFile(zipPath);
        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(Base64Util.encode(bytes));
        return ResponseTo.success(commonTo);
    }

    /**
     * 解析文件
     * @param file 文件
     * @param type 文件类型：1PkCS#7，2印章，3签章，4密钥对保护数据 |1
     * @return
     * @throws Exception
     */
    @PostMapping("/analysis")
    public ResponseTo<CommonResTo> analysis(@RequestParam("file") MultipartFile file,
                                            int type) throws Exception {
        switch (type) {
            case 2:
                new AnalysisSeal().analysis(file.getBytes());
                break;
            case 3:
                new AnalysisStamp().analysis(file.getBytes());
                break;
            case 4:
                new AnalysisProtectKey().analysis(file.getBytes());
                break;
            case 1:
            default:
                new AnalysisP7().analysis(file.getBytes());
                break;
        }

        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult("请到debug日志中查看");
        return ResponseTo.success(commonTo);
    }
}
