package com.gs.webserver.controller.tool;

import com.gs.common.define.Constants;
import com.gs.common.entity.pdf.StampVerify;
import com.gs.common.util.FileUtil;
import com.gs.common.util.base64.Base64Util;
import com.gs.common.util.date.DateUtil;
import com.gs.common.util.pdf.PdfStampUtil;
import com.gs.common.util.pdf.PdfUtil;
import com.gs.common.util.pdf.RemovePdfStampUtil;
import com.gs.webserver.entity.to.response.CommonResTo;
import com.gs.webserver.entity.to.response.ResponseTo;
import com.gs.webserver.entity.to.response.pdf.PdfStampResTo;
import com.gs.webserver.service.IStampService;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;

/**
 * PDF
 * @author Administator
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
     * @return 签章结构列表
     */
    @PostMapping("/getStamp")
    public ResponseTo<PdfStampResTo> getStampFromPdf(@RequestParam("file") MultipartFile file) throws Exception {
        Map<String, String> stampMap = stampService.getStampFromPdf(file);
        PdfStampResTo pdfStampResTo = new PdfStampResTo();
        pdfStampResTo.setStampMap(stampMap);
        return ResponseTo.success(pdfStampResTo);
    }

    /**
     * 获取pdf模板文本域
     * @param file pdf模板
     * @throws Exception
     */
    @PostMapping("/getPdfFields")
    public ResponseTo<List> getPdfFieldsTest(@RequestParam("file") MultipartFile file) throws Exception {
        List list = PdfUtil.getTemplateFields(file.getBytes());
        return ResponseTo.success(list);
    }

    /**
     * pdf插入图片
     * @param pdfFile pdf文件
     * @param imgFile 图片文件
     * @param pageNumber 页码
     * @param x          x坐标
     * @param y          y坐标
     * @param w          图片宽度
     * @param h          图片高度
     * @param type 单位类型，1厘米，2像素
     * @throws Exception
     */
    @PostMapping("/pdfAddImage")
    public ResponseTo<CommonResTo> pdfAddImageTest(@RequestParam("pdfFile") MultipartFile pdfFile, @RequestParam("imgFile") MultipartFile imgFile,
                                int pageNumber, float x, float y, float w, float h, int type) throws Exception {
        // 可指定图片大小和单位，1为厘米，2为像素
        byte[] addImagePdf = PdfUtil.pdfAddImage(pdfFile.getBytes(), imgFile.getBytes(), pageNumber, x, y, w, h, type);
        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(Base64Util.encode(addImagePdf));
        return ResponseTo.success(commonTo);
    }

    /**
     * pdf坐标签章
     * @param pdfFile pdf文件
     * @param photoFile 印模图片
     * @param pfxFile pfx文件
     * @param password pfx文件密码
     * @param pageNumber 页码
     * @param x x坐标，单位像素
     * @param y y坐标，单位像素
     * @return
     * @throws Exception
     */
    @PostMapping("/pdfStamp")
    public ResponseTo<CommonResTo> pdfStampTest(@RequestParam("pdfFile") MultipartFile pdfFile,
                                                @RequestParam("photoFile") MultipartFile photoFile,
                                                @RequestParam("file") MultipartFile pfxFile, String password,
                                                int pageNumber, float x, float y) throws Exception {
        String path = Constants.TMP_PATH + DateUtil.getDateDir() + FileUtil.getFileName() + Constants.PFX_SUFFIX;
        FileUtil.storeFile(path, pfxFile.getBytes());

        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(path), password.toCharArray());
        String alias = ks.aliases().nextElement();
        PrivateKey pk = (PrivateKey) ks.getKey(alias, password.toCharArray());
        // 得到证书链
        Certificate[] chain = ks.getCertificateChain(alias);

        //签章: PDF的RSA签章 = 图片+签名，没有印章和签章结构
        byte[] pdfData = pdfFile.getBytes();
        byte[] photoData = photoFile.getBytes();
        PdfStampUtil pdfUtil = new PdfStampUtil();
        // todo 摘要算法从证书中取
        byte[] signedData = pdfUtil.sign(pdfData, photoData,pageNumber,x, y, chain, pk, DigestAlgorithms.SHA1);

        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(Base64Util.encode(signedData));
        return ResponseTo.success(commonTo);
    }

    /**
     * pdf验签章
     * @param pdfFile 签章文件
     * @return
     * @throws Exception
     */
    @PostMapping("/pdfVerify")
    public ResponseTo<StampVerify> pdfVerifyTest(@RequestParam("pdfFile") MultipartFile pdfFile) throws Exception {
        PdfStampUtil pdfUtil = new PdfStampUtil();
        StampVerify verify = pdfUtil.verifySign(pdfFile.getBytes());
        return ResponseTo.success(verify);
    }

    /**
     * pdf撤章(最后一个)
     * @param pdfFile 签章文件
     * @return
     * @throws Exception
     */
    @PostMapping("/pdfStampRemove")
    public ResponseTo<CommonResTo> pdfStampRemoveTest(@RequestParam("pdfFile") MultipartFile pdfFile) throws Exception {
        PdfStampUtil pdfUtil = new PdfStampUtil();
        byte[] data = RemovePdfStampUtil.removeStamp(pdfFile.getBytes());

        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(Base64Util.encode(data));
        return ResponseTo.success(commonTo);
    }
}
