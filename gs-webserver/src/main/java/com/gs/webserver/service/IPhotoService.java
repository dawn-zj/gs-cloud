package com.gs.webserver.service;

import com.gs.webserver.entity.to.request.PhotoTo;

public interface IPhotoService {
    byte[] viewStamp(PhotoTo photoTo) throws Exception;
}
