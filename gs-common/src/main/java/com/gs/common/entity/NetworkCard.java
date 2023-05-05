package com.gs.common.entity;

/**
 * 网卡
 */
public class NetworkCard {
    /**
     * 网卡名称
     */
    private String name;

    /**
     * 网卡可读性名称
     */
    private String displayName;

    /**
     * 网卡ip
     */
    private String ip;

    private String ipv6;

    /**
     * 网卡Mac
     */
    private String mac;

    /**
     * 子网掩码
     */
    private String mask;

    private String bcast;

    private boolean up;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        up = up;
    }

    public String getBcast() {
        return bcast;
    }

    public void setBcast(String bcast) {
        this.bcast = bcast;
    }

    public String getIpv6() {
        return ipv6;
    }

    public void setIpv6(String ipv6) {
        this.ipv6 = ipv6;
    }
}
