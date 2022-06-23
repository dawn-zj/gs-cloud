package com.gs.common.util;

import com.gs.common.entity.Cpu;
import com.gs.common.entity.Disk;
import com.gs.common.entity.Memory;
import com.gs.common.entity.ServerInfo;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
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

            // 开发环境测试
            // File f = new File("E:/etc/ip-centos.txt");
            // FileInputStream fis = new FileInputStream(f);
            // BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            if (FileUtil.checkPath("/etc/centos-release"))
                hWaddr = getHostMacCentos(networkCard, reader);
            else if (FileUtil.checkPath("/etc/neokylin-release"))
                hWaddr = getHostMacNeoKylin(networkCard, reader);
            else
                throw new Exception("not support current os(must centos、neokylin)");

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

    /**
     * 获取centos的指定网口Mac信息
     * @param networkCard
     * @param reader
     * @return
     * @throws Exception
     */
    private static String getHostMacCentos(String networkCard, BufferedReader reader) throws Exception {
        String hWaddr = null;
        // centos
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
    }

    /**
     * 获取中标麒麟系统的指定网口Mac信息
     * @param networkCard
     * @param reader
     * @return
     * @throws Exception
     */
    private static String getHostMacNeoKylin(String networkCard, BufferedReader reader) throws Exception {
        String hWaddr = null;
        // 中标麒麟
        String section = "";
        String temp = reader.readLine().trim();

        while (temp != null) {
            temp = temp.trim();
            if (!temp.equalsIgnoreCase("")) {
                section = section + temp + " ";
            } else {
                if (!"".equalsIgnoreCase(section) && section.startsWith(networkCard)) {
                    int start = section.indexOf("ether ");
                    int end = section.indexOf("txqueuelen");
                    hWaddr = section.substring(start + 6, end);
                    hWaddr = hWaddr.replace(":", "").toLowerCase().trim();
                    break;
                }
                section = "";
            }
            temp = reader.readLine();
        }
        return hWaddr;
    }

    /**
     * 使用JDK自带的方法获取系统信息
     * @return
     * @throws Exception
     */
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

    /**
     * 使用oshi获取内存信息
     * @return
     */
    public static Memory memoryInfo() {
        HardwareAbstractionLayer hardware = new SystemInfo().getHardware();
        GlobalMemory mem = hardware.getMemory();// 内存单位 b

        Memory memory = new Memory();
        memory.setTotal(Arith.round(b2GB(mem.getTotal()), 2));
        memory.setFree(Arith.round(b2GB(mem.getAvailable()), 2));
        memory.setUsed(Arith.round(b2GB(mem.getTotal() -  mem.getAvailable()), 2));
        return memory;
    }

    /**
     * 使用oshi获取cpu信息
     * @return
     */
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

    public static List<Disk> diskInfo() {
        List<Disk> disks = new ArrayList<>();

        OperatingSystem operatingSystem = new SystemInfo().getOperatingSystem();
        FileSystem fileSystem = operatingSystem.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace(); // 单位 b
            long total = fs.getTotalSpace();
            long used = total - free;
            Disk disk = new Disk();
            disk.setDirName(fs.getMount());
            disk.setSysTypeName(fs.getType());
            disk.setTypeName(fs.getName());
            disk.setTotal(Arith.round(b2GB(total), 2));
            disk.setFree(Arith.round(b2GB(free), 2));
            disk.setUsed(Arith.round(b2GB(used), 2));
            disk.setUsage(Arith.mul(Arith.div(used, total, 2), 100));
            disks.add(disk);
        }

        return disks;
    }

    private static double b2GB(long b) {
        return Arith.div(b2MB(b), 1024, 2);
    }

    private static double b2MB(long b) {
        return Arith.div(b, (1024 * 1024), 2);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getHostMac("eth0"));
    }
}
