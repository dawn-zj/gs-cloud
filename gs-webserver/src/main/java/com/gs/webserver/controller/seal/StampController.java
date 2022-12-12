package com.gs.webserver.controller.seal;

import com.alibaba.fastjson.JSONObject;
import com.gs.webserver.entity.to.response.ResponseTo;
import com.gs.webserver.service.IStampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/seal/stamp")
public class StampController {
    @Autowired
    @Qualifier("pdfStampServiceImpl")
    private IStampService stampService;

    @PostMapping("/verify")
    public ResponseTo verify(@RequestParam("file") MultipartFile file) {
        JSONObject verify = stampService.verify(file);
        return ResponseTo.success(verify);
    }
}
