package com.gs.webserver.controller.tool;

import com.gs.webserver.entity.to.PhotoTo;
import com.gs.webserver.entity.to.ResponseTo;
import com.gs.webserver.service.IPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tool/photo")
public class PhotoController {
    @Autowired
    private IPhotoService photoService;

    @PostMapping("/viewStamp")
    public ResponseTo viewStamp(@RequestBody PhotoTo photoTo) throws Exception {
        byte[] photoData = photoService.viewStamp(photoTo);
        return ResponseTo.success(photoData);
    }

}
