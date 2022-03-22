package com.gs.webserver.service.impl;

import com.gs.common.entity.Photo;
import com.gs.common.exception.NetGSRuntimeException;
import com.gs.common.util.photo.Circle;
import com.gs.common.util.photo.Ellipse;
import com.gs.webserver.entity.to.PhotoTo;
import com.gs.webserver.service.IPhotoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class PhotoServiceImpl implements IPhotoService {
    @Override
    public byte[] viewStamp(PhotoTo photoTo) throws Exception {
        Integer stampStyle = photoTo.getStampStyle();

        Photo photo = new Photo();
        BeanUtils.copyProperties(photoTo, photo);
        switch (stampStyle) {
            case 1:
                return new Circle(photo).draw();
            case 2:
                return new Ellipse(photo).draw();
            default:
                throw new NetGSRuntimeException("不支持的图章类型：" + stampStyle);
        }
    }

}
