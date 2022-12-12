package com.gs.webserver.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface IStampService {
    JSONObject verify(MultipartFile file);
}
