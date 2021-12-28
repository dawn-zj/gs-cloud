package com.gs.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Properties;

public class NetWorkUtil {

    /**
     * 是否是linux系统
     * @return
     */
    public static boolean isLinux() {
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        if (os != null && os.toLowerCase().indexOf("linux") > -1) {
            return true;
        }
        return false;
    }

    /**
     * 网卡的Mac信息
     * @param networkCard
     * @return 十六进制Mac值
     * @throws Exception
     */
    public static String getHostMac(String networkCard) throws Exception {
        if(isLinux()) {
            String hWaddr = null;
            Runtime runt = Runtime.getRuntime();
            Process p = runt.exec("ifconfig -a");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = null;
            while ((line = reader.readLine()) != null) {
                String tmp = line.trim();
                if (tmp.startsWith(networkCard)) {
                    int start = tmp.indexOf("HWaddr");
                    int end = tmp.length();
                    hWaddr = tmp.substring(start + 7, end);
                    hWaddr = hWaddr.replace(":", "").toLowerCase();
                }
            }
            return hWaddr;
        } else {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) networkInterfaces.nextElement();
                if (ni.getName().equalsIgnoreCase(networkCard) && ni.getHardwareAddress() != null)
                    return HexUtil.byte2Hex(ni.getHardwareAddress());

            }
            throw new Exception("没有找到指定的网卡");
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getHostMac("eth0"));
    }
}
