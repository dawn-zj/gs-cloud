package com.gs.common.entity;

import com.gs.common.util.date.DateUtil;

import java.lang.management.ManagementFactory;

public class ServerInfo {
    private String jvmName; // java.vm.name
    private String jvmVersion; // java.vm.version
    private Long jvmStartTime;
    private String jvmRunTime;
    private String javaVersion; // java.version
    private String javaHome; // java.home

    private String osName; // os.name
    private String osArch; // os.arch

    private String serverName; // InetAddress.getLocalHost().getHostName()
    private String serverIp; // InetAddress.getLocalHost().getHostAddress()

    private String userDir; // user.dir

    public String getJvmName() {
        return jvmName;
    }

    public void setJvmName(String jvmName) {
        this.jvmName = jvmName;
    }

    public String getJvmVersion() {
        return jvmVersion;
    }

    public void setJvmVersion(String jvmVersion) {
        this.jvmVersion = jvmVersion;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public String getJavaHome() {
        return javaHome;
    }

    public void setJavaHome(String javaHome) {
        this.javaHome = javaHome;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsArch() {
        return osArch;
    }

    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Long getJvmStartTime() {
        return ManagementFactory.getRuntimeMXBean().getStartTime();
    }

    public String getJvmRunTime() {
        long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        long currentTime = DateUtil.getCurrentTime();

        return DateUtil.formatDuring(currentTime - startTime);
    }

    public String getUserDir() {
        return userDir;
    }

    public void setUserDir(String userDir) {
        this.userDir = userDir;
    }
}
