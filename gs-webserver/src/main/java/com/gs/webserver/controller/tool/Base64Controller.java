package com.gs.webserver.controller.tool;

import com.gs.common.util.StringUtil;
import com.gs.common.util.base64.Base64Util;
import com.gs.webserver.entity.to.Base64To;
import com.gs.webserver.entity.to.ResponseTo;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tool/base64/")
public class Base64Controller {

    @PostMapping("encode")
    public ResponseTo encode(@RequestBody Base64To base64To) {
        byte[] encode = Base64Util.encode(base64To.getContent());
        Map<String, Object> map = new HashMap<>();
//        encode.toString();
        map.put("result", StringUtil.getString(encode));
        return ResponseTo.success(map);
    }

    @PostMapping("decode")
    public ResponseTo decode(@RequestBody Base64To base64To) {
        byte[] decode = Base64Util.decode(base64To.getContentB64());
        Map<String, Object> map = new HashMap<>();
        map.put("result", StringUtil.getString(decode));
        return ResponseTo.success(map);
    }

}
