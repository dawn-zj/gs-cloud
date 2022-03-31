package com.gs.webserver.controller.tool;

import com.gs.common.util.StringUtil;
import com.gs.common.util.base64.Base64Util;
import com.gs.webserver.entity.to.request.Base64To;
import com.gs.webserver.entity.to.response.CommonTo;
import com.gs.webserver.entity.to.response.ResponseTo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tool/base64")
public class Base64Controller {

    @PostMapping("/encode")
    public ResponseTo encode(@RequestBody Base64To base64To) {
        byte[] encode = Base64Util.encode(base64To.getContent());
        CommonTo commonTo = new CommonTo();
        commonTo.setResult(StringUtil.getString(encode));
        return ResponseTo.success(commonTo);
    }

    @PostMapping("/decode")
    public ResponseTo decode(@RequestBody Base64To base64To) {
        byte[] decode = Base64Util.decode(base64To.getContentB64());
        CommonTo commonTo = new CommonTo();
        commonTo.setResult(StringUtil.getString(decode));
        return ResponseTo.success(commonTo);
    }

}
