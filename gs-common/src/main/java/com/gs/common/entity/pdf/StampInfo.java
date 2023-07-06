package com.gs.common.entity.pdf;

import java.util.List;

public class StampInfo {
    private boolean result;
    private String certDn;
    private long certNotBefore;
    private long certNotAfter;
    private long signTime;
    private String signSubFilter;
    private String signHashAlg;
    private List signLocation;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getCertDn() {
        return certDn;
    }

    public void setCertDn(String certDn) {
        this.certDn = certDn;
    }

    public long getCertNotBefore() {
        return certNotBefore;
    }

    public void setCertNotBefore(long certNotBefore) {
        this.certNotBefore = certNotBefore;
    }

    public long getCertNotAfter() {
        return certNotAfter;
    }

    public void setCertNotAfter(long certNotAfter) {
        this.certNotAfter = certNotAfter;
    }

    public long getSignTime() {
        return signTime;
    }

    public void setSignTime(long signTime) {
        this.signTime = signTime;
    }

    public String getSignSubFilter() {
        return signSubFilter;
    }

    public void setSignSubFilter(String signSubFilter) {
        this.signSubFilter = signSubFilter;
    }

    public String getSignHashAlg() {
        return signHashAlg;
    }

    public void setSignHashAlg(String signHashAlg) {
        this.signHashAlg = signHashAlg;
    }

    public List getSignLocation() {
        return signLocation;
    }

    public void setSignLocation(List signLocation) {
        this.signLocation = signLocation;
    }
}
