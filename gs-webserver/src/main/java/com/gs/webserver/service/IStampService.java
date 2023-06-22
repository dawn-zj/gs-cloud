package com.gs.webserver.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IStampService {
    JSONObject verify(MultipartFile file);

    Map<String, String> getStampFromPdf(MultipartFile file) throws Exception;
}
