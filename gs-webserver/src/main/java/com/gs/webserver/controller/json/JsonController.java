package com.gs.webserver.controller.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gs.common.define.Constants;
import com.gs.common.util.FileUtil;
import com.gs.common.util.StringUtil;
import com.gs.webserver.entity.to.request.JsonTo;
import com.gs.webserver.entity.to.response.ResponseTo;
import org.springframework.web.bind.annotation.*;

import java.io.File;

/**
 * JSON处理
 * @author Administator
 */
@RestController
@RequestMapping("/json")
public class JsonController {

    /**
     * 获取列表
     * @return
     */
    @PostMapping("/list")
    public ResponseTo<Object> list(@RequestBody JsonTo jsonTo) throws Exception {
        String filePath = Constants.JSON_PATH + jsonTo.getFilePath();
        String content = StringUtil.getString(FileUtil.getFile(filePath));
        Object obj = JSON.parse(content);
        return ResponseTo.success(obj);
    }

    /**
     * 获取
     * @return 测试结果
     */
    @PostMapping("/get")
    public ResponseTo<Object> get(@RequestBody JsonTo jsonTo) throws Exception {
        String filePath = Constants.JSON_PATH + jsonTo.getFilePath();
        JSONArray arr = getJSONArray(filePath);
        Object obj = arr.get(jsonTo.getIndex());
        return ResponseTo.success(obj);
    }

    /**
     * 添加
     * @param jsonTo 数据
     * @return
     * @throws Exception
     */
    @PostMapping("/add")
    public ResponseTo<Object> add(@RequestBody JsonTo jsonTo) throws Exception {
        String filePath = Constants.JSON_PATH + jsonTo.getFilePath();
        FileUtil.checkPath(filePath, true, true);

        JSONArray arr = getJSONArray(filePath);
        arr.add(jsonTo.getData());

        FileUtil.storeFile(filePath, arr.toJSONString().getBytes());
        return ResponseTo.success();
    }

    /**
     * 修改
     * @param jsonTo 数据
     * @return
     * @throws Exception
     */
    @PostMapping("/update")
    public ResponseTo<Object> update(@RequestBody JsonTo jsonTo) throws Exception {
        String filePath = Constants.JSON_PATH + jsonTo.getFilePath();
        JSONArray arr = getJSONArray(filePath);
        arr.set(jsonTo.getIndex(), jsonTo.getData());

        FileUtil.storeFile(filePath, arr.toJSONString().getBytes());
        return ResponseTo.success();
    }

    /**
     * 删除
     * @param jsonTo 数据
     * @return
     * @throws Exception
     */
    @PostMapping("/delete")
    public ResponseTo<Object> delete(@RequestBody JsonTo jsonTo) throws Exception {
        String filePath = Constants.JSON_PATH + jsonTo.getFilePath();
        JSONArray arr = getJSONArray(filePath);
        arr.remove(jsonTo.getIndex());

        FileUtil.storeFile(filePath, arr.toJSONString().getBytes());
        return ResponseTo.success();
    }

    public JSONArray getJSONArray(String filePath) {
        String content = StringUtil.getString(FileUtil.getFile(filePath));
        JSONArray arr = JSON.parseArray(content);
        if (arr == null) {
            arr = new JSONArray();
        }
        return arr;
    }

    public JSONObject getJSONObject(String filePath) {
        String content = StringUtil.getString(FileUtil.getFile(filePath));
        return JSON.parseObject(content);
    }

}
