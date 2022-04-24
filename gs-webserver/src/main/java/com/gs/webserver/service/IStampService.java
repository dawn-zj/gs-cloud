package com.gs.webserver.service;

import org.springframework.web.multipart.MultipartFile;

public interface IStampService {
    boolean verify(MultipartFile file);
}
