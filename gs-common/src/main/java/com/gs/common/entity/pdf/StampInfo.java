package com.gs.common.entity.pdf;

import java.util.List;

/**
 * @author Administator
 */
public class StampInfo {
    /**
     * 验证结果
     */
    private boolean result;
    /**
     * 签章人证书主题
     */
    private String certDn;
    /**
     * 签章人证书起始日期
     */
    private long certNotBefore;
    /**
     * 签章人证书结束日期
     */
    private long certNotAfter;
    /**
     * 签章时间
     */
    private long signTime;
    /**
     * 签章格式
     */
    private String signSubFilter;
    /**
     * 签章摘要算法
     */
    private String signHashAlg;
    /**
     * 签章位置
     */
    private List signLocation;
    /**
     * 签章图片
     */
    private String signPhotoB64;

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

    public String getSignPhotoB64() {
        return signPhotoB64;
    }

    public void setSignPhotoB64(String signPhotoB64) {
        this.signPhotoB64 = signPhotoB64;
    }
}
