package com.gs.webserver.service.impl.stamp;

import com.alibaba.fastjson.JSONObject;
import com.gs.common.define.Constants;
import com.gs.common.exception.BaseException;
import com.gs.common.exception.NetGSRuntimeException;
import com.gs.common.util.FileUtil;
import com.gs.common.util.pdf.PdfStampUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfStampServiceImpl extends StampServiceImpl {

    @Override
    public JSONObject verify(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            String fileType = FileUtil.getFileSuffix(filename);
            if (!Constants.PDF_SUFFIX.equals(fileType))
                throw new BaseException("文件不是PDF类型");

            PdfStampUtil pdfUtil = new PdfStampUtil();
            // todo 验签名，验签章待实现
            JSONObject verify = pdfUtil.verifySign(file.getBytes());
            return verify;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new NetGSRuntimeException("PDF验签章发生错误", e);
        }
    }
}
