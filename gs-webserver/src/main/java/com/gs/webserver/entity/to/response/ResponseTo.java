package com.gs.webserver.entity.to.response;

import com.gs.common.util.StringUtil;
import lombok.Data;

/**
 * 响应类
 * @author Administator
 */
@Data
public class ResponseTo<T> {
    /**
     * 状态码(状态码异常时,返回结果不含body)
     */
    private String code;

    /**
     * 状态描述
     */
    private String msg;

    /**
     * 内容体
     */
    private T data;

    /**
     * 初始化一个新创建的对象，使其表示一个空消息。
     */
    public ResponseTo() {
    }

    /**
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param body 数据对象
     */
    public ResponseTo(String code, String msg, T body) {
        this.code = code;
        this.msg = msg;
        if (StringUtil.isNotNull(body)) {
            this.data = body;
        }
    }

    private static <T> ResponseTo<T> getResult(String code, String msg, T body) {
        ResponseTo<T> apiResult = new ResponseTo<>(code, msg, body);
        return apiResult;
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param body 数据对象
     * @return 成功消息
     */
    public static <T> ResponseTo<T> success(T body) {
        return getResult("200", "操作成功", body);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param body 数据对象
     * @return 成功消息
     */
    public static <T> ResponseTo<T> success() {
        return getResult("200", "操作成功", null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param body 数据对象
     * @return 成功消息
     */
    public static <T> ResponseTo<T> success(String msg, T body) {
        return getResult("200", msg, body);
    }
    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static <T> ResponseTo<T> error(String msg) {
        return getResult("-1", msg, null);
    }

}