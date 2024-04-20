package com.gs.webserver.controller.tool;

import com.gs.common.util.StringUtil;
import com.gs.common.util.base64.Base64Util;
import com.gs.webserver.entity.to.request.Base64To;
import com.gs.webserver.entity.to.response.CommonResTo;
import com.gs.webserver.entity.to.response.ResponseTo;
import org.springframework.web.bind.annotation.*;

/**
 * 编解码
 * @author Administator
 */
@RestController
@RequestMapping("/tool/base64")
public class Base64Controller {

    /**
     * 编码
     * @param base64To 请求数据
     * @return 编码结果
     */
    @PostMapping("/encode")
    public ResponseTo<CommonResTo> encode(@RequestBody Base64To.Base64ReqTo base64To) {
        String encode = Base64Util.encode(base64To.getContent());
        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(encode);
        return ResponseTo.success(commonTo);
    }

    /**
     * 解码
     * @param base64To 请求数据
     * @return 解码结果
     */
    @PostMapping("/decode")
    public ResponseTo<CommonResTo> decode(@RequestBody Base64To.Base64ReqTo base64To) {
        byte[] decode = Base64Util.decode(base64To.getContent());
        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(StringUtil.getString(decode));
        return ResponseTo.success(commonTo);
    }

}
