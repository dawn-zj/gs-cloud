package com.gs.webserver.controller.tool;

import com.gs.common.util.ImageUtil;
import com.gs.common.util.base64.Base64Util;
import com.gs.webserver.entity.to.request.BaseTo;
import com.gs.webserver.entity.to.response.CommonResTo;
import com.gs.webserver.entity.to.request.PhotoTo;
import com.gs.webserver.entity.to.response.ResponseTo;
import com.gs.webserver.service.IPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * 图片处理
 * @author Administator
 */
@RestController
@RequestMapping("/tool/photo")
public class PhotoController {
    @Autowired
    private IPhotoService photoService;

    /**
     * 制作图章
     * @param photoTo 图章信息
     * @return 图章数据
     * @throws Exception 异常
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
     * @param baseTo 二维码内容信息
     * @return 二维码图片数据
     */
    @PostMapping("/genBarcode")
    public ResponseTo<CommonResTo> genBarcode(@RequestBody BaseTo baseTo) {
        byte[] photoData = ImageUtil.genBarcodeImage(baseTo.getContent());
        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(Base64Util.encode(photoData));
        return ResponseTo.success(commonTo);
    }

    /**
     * 制作图片验证码
     * @param width 宽度，单位像素 | 180
     * @param height 高度，单位像素 | 50
     * @param codeCount 验证码个数 | 4
     * @param lineCount 干扰线个数 | 100
     * @return
     * @throws Exception
     */
    @GetMapping("/genCodeImg")
    public ResponseTo<CommonResTo> genAuthCodeTest(@NotNull int width, @NotNull int height,
                                                   @NotNull int codeCount, @NotNull int lineCount) throws Exception {
        byte[] photoData = ImageUtil.genAuthCodeImage(width, height, codeCount, lineCount);
        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(Base64Util.encode(photoData));
        return ResponseTo.success(commonTo);
    }

    /**
     * 去背景
     * @param file 图片文件
     * @param imgR 指定颜色的R值 |255
     * @param imgG 指定颜色的G值 |255
     * @param imgB 指定颜色的B值 |255
     * @return
     * @throws Exception
     */
    @PostMapping("/cleanBGColor")
    public ResponseTo<CommonResTo> cleanBGColorTest(@RequestParam("file") MultipartFile file,
                                                    @NotNull int imgR, @NotNull int imgG, @NotNull int imgB) throws Exception {
        // cleanBGColor内部针对指定颜色背景进行处理，适用于大部分图片
        byte[] photoData = ImageUtil.cleanBGColor(file.getBytes(), imgR, imgG, imgB);
        CommonResTo commonTo = new CommonResTo();
        commonTo.setResult(Base64Util.encode(photoData));
        return ResponseTo.success(commonTo);
    }

}
