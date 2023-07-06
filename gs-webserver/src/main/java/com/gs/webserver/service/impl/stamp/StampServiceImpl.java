package com.gs.webserver.service.impl.stamp;

import com.alibaba.fastjson.JSONObject;
import com.gs.webserver.entity.to.response.pdf.PdfStampVerifyResTo;
import com.gs.webserver.service.IStampService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class StampServiceImpl implements IStampService {
    @Override
    public PdfStampVerifyResTo verify(MultipartFile file) {
        // 交给子类具体实现
        return null;
    }

    @Override
    public Map<String, String> getStampFromPdf(MultipartFile file) throws Exception {
        return null;
    }
}
