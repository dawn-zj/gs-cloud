package com.gs.webserver.controller.tool;

import com.gs.common.util.photo.Circle;
import com.gs.webserver.entity.to.PhotoTo;
import com.gs.webserver.entity.to.ResponseTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tool/photo/")
public class PhotoController {

    @PostMapping("viewStamp")
    public ResponseTo viewStamp(@RequestBody PhotoTo photoTo) throws Exception {
        Circle circle = new Circle(photoTo.getWidth(), photoTo.getCompany(), photoTo.getCompanyFontSize(),
                photoTo.getName(), photoTo.getNameFontSize(), photoTo.getNumber(), photoTo.getNumberFontSize());
        byte[] photoData = circle.draw();
        return ResponseTo.success(photoData);
    }

}
