package com.gs.common.util;

import com.gs.common.entity.Cpu;
import com.gs.common.entity.Memory;
import com.gs.common.entity.ServerInfo;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
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

    public static ServerInfo systemInfo() throws Exception {
        ServerInfo systemInfo = new ServerInfo();
        systemInfo.setServerName(InetAddress.getLocalHost().getHostName());
        systemInfo.setServerIp(InetAddress.getLocalHost().getHostAddress());

        Properties prop = System.getProperties();
        systemInfo.setJvmName(prop.getProperty("java.vm.name"));
        systemInfo.setJvmVersion(prop.getProperty("java.vm.version"));
        systemInfo.setJavaHome(prop.getProperty("java.home"));
        systemInfo.setJavaVersion(prop.getProperty("java.version"));
        systemInfo.setOsName(prop.getProperty("os.name"));
        systemInfo.setOsArch(prop.getProperty("os.arch"));
        systemInfo.setUserDir(prop.getProperty("user.dir"));

        return systemInfo;
    }

    public static Memory memoryInfo() {
        HardwareAbstractionLayer hardware = new SystemInfo().getHardware();
        GlobalMemory mem = hardware.getMemory();// 内存单位 b

        Memory memory = new Memory();
        memory.setTotal(mem.getTotal());
        memory.setFree(mem.getAvailable());
        memory.setUsed(mem.getTotal() -  mem.getAvailable());
        return memory;
    }

    public static Cpu cpuInfo() {
        HardwareAbstractionLayer hardware = new SystemInfo().getHardware();
        CentralProcessor processor = hardware.getProcessor();

        Cpu cpu = new Cpu();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(1000);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        cpu.setCpuNum(processor.getLogicalProcessorCount());
        cpu.setTotal(totalCpu);
        cpu.setSys(cSys);
        cpu.setUsed(user);
        cpu.setWait(iowait);
        cpu.setFree(idle);
        return cpu;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getHostMac("eth0"));
    }
}
