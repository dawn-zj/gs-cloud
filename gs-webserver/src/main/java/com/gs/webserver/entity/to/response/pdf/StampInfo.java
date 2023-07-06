package com.gs.webserver.entity.to.response.pdf;

import lombok.Data;

import java.util.List;

@Data
public class StampInfo {
    private boolean result;
    private String certDn;
    private long certNotBefore;
    private long certNotAfter;
    private long signTime;
    private String signSubFilter;
    private String signHashAlg;
    private List signLocation;
}
