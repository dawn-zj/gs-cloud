package com.gs.webserver.service.impl.stamp;

import com.alibaba.fastjson.JSONObject;
import com.gs.webserver.service.IStampService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StampServiceImpl implements IStampService {
    @Override
    public JSONObject verify(MultipartFile file) {
        // 交给子类具体实现
        return null;
    }
}
