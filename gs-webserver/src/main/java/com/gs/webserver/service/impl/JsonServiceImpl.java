package com.gs.webserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gs.common.define.Constants;
import com.gs.common.util.FileUtil;
import com.gs.common.util.StringUtil;
import com.gs.webserver.entity.to.request.JsonTo;
import com.gs.webserver.service.IJsonService;
import org.springframework.stereotype.Service;

/**
 * @Author Zhang Juan
 * @Date 2024/4/30 10:46
 */
@Service
public class JsonServiceImpl implements IJsonService {
    @Override
    public void add(JsonTo jsonTo) throws Exception {
        String filePath = Constants.JSON_PATH + jsonTo.getFilePath();
        FileUtil.checkPath(filePath, true, true);

        JSONArray arr = getJSONArray(filePath);
        arr.add(jsonTo.getData());

        FileUtil.storeFile(filePath, arr.toJSONString().getBytes());
    }
    @Override
    public JSONArray getJSONArray(String filePath) {
        String content = StringUtil.getString(FileUtil.getFile(filePath));
        JSONArray arr = JSON.parseArray(content);
        if (arr == null) {
            arr = new JSONArray();
        }
        return arr;
    }

    @Override
    public JSONObject getJSONObject(String filePath) {
        String content = StringUtil.getString(FileUtil.getFile(filePath));
        return JSON.parseObject(content);
    }
}
