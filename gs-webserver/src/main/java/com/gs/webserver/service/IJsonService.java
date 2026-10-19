package com.gs.webserver.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gs.webserver.entity.to.request.JsonTo;

/**
 * @author Administator
 */
public interface IJsonService {
    void add(JsonTo jsonTo) throws Exception;

    JSONArray getJSONArray(String filePath);

    JSONObject getJSONObject(String filePath);
}
