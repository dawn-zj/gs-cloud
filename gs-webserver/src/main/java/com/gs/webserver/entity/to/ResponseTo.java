package com.gs.webserver.entity.to;

import com.gs.common.util.StringUtil;

import java.util.HashMap;

public class ResponseTo extends HashMap<String, Object> {
    /**
     * 状态码
     */
    public static final String CODE_TAG = "errorCode";

    /**
     * 返回内容
     */
    public static final String MSG_TAG = "errorMessage";

    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";

    public ResponseTo() {
    }

    public ResponseTo(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (StringUtil.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    public static ResponseTo success() {
        return new ResponseTo(200, "操作成功", null);
    }

    public static ResponseTo success(Object data) {
        return new ResponseTo(200, "操作成功", data);
    }

    public static ResponseTo error(String msg) {
        return new ResponseTo(500, msg, null);
    }
}
