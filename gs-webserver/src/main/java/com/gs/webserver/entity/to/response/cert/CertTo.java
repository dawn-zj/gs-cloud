package com.gs.webserver.entity.to.response.cert;

import lombok.Data;

@Data
public class CertTo {
    private String sn;
    private String dn;
    private String cn;
    private String issueDn;
    private String issueCn;
    private Integer keyUsage;
    private Long notBefore;
    private Long notAfter;
    private String keyId;
}
