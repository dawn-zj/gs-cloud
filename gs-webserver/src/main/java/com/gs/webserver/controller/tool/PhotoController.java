package com.gs.webserver.controller.tool;

import com.gs.common.util.ImageUtil;
import com.gs.common.util.base64.Base64Util;
import com.gs.webserver.entity.to.request.BaseTo;
import com.gs.webserver.entity.to.response.CommonResTo;
import com.gs.webserver.entity.to.request.PhotoTo;
import com.gs.webserver.entity.to.response.ResponseTo;
import com.gs.webserver.service.IPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 图片处理
 */
@RestController
@RequestMapping("/tool/photo")
public class PhotoController {
    @Autowired
    private IPhotoService photoService;

    /**
     * 制作图章
     * @param photoTo
     * @return
     * @throws Exception
     */
    @PostMapping("/viewStamp")
    public ResponseTo<CommonResTo> viewStamp(@RequestBody PhotoTo photoTo) throws Exception {
        byte[] photoData = photoService.viewStamp(photoTo);
        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(Base64Util.encode(photoData));
        return ResponseTo.success(commonTo);
    }

    /**
     * 制作二维码
     * @param baseTo
     * @return
     * @throws Exception
     */
    @PostMapping("/genBarcode")
    public ResponseTo<CommonResTo> genBarcode(@RequestBody BaseTo baseTo) throws Exception {
        byte[] photoData = ImageUtil.genBarcodeImage(baseTo.getContent());
        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(Base64Util.encode(photoData));
        return ResponseTo.success(commonTo);
    }

}
