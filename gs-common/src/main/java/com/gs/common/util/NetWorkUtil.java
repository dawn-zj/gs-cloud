package com.gs.common.util;

import com.alibaba.fastjson.JSON;
import com.gs.common.entity.*;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.io.*;
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

    private static List<String> networkCardInfo2OneLine(BufferedReader reader) throws Exception {
        List<String> list = new ArrayList<>();

        // 一块网卡，多行拼成一行
        String section = "";
        // 按行读
        String temp = reader.readLine();

        while (temp != null) {
            temp = temp.trim();
            if (!temp.equalsIgnoreCase("")) {
                section = section + temp + " ";
            } else {
                list.add(section);
                section = "";
            }
            temp = reader.readLine();
        }

        return list;

    }

    public static boolean isVirtualMachine(String line) throws Exception {
        if (line.indexOf("flags=") > -1)
            return true;
        else if (line.indexOf("Link encap") > -1)
            return false;

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
     * 获取中标麒麟系统的指定网口Mac信息
     * @param networkCard
     * @param reader
     * @return
     * @throws Exception
     */
    private static String getHostMacNeoKylin(String networkCard, BufferedReader reader) throws Exception {
        return getHostMacVirtualMachine(networkCard, reader);
    }

    /**
     * 获取centos的指定网口Mac信息
     * @param networkCard
     * @param reader
     * @return
     * @throws Exception
     */
    private static String getHostMacCentos(String networkCard, BufferedReader reader) throws Exception {
        /**
         * eth1      Link encap:Ethernet  HWaddr AC:1F:6B:E3:70:C7
         *           inet addr:10.20.87.66  Bcast:10.20.87.255  Mask:255.255.255.0
         *           inet6 addr: fe80::ae1f:6bff:fee3:70c7/64 Scope:Link
         *           UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1
         *           RX packets:2095245 errors:0 dropped:0 overruns:0 frame:0
         *           TX packets:3912825 errors:0 dropped:0 overruns:0 carrier:0
         *           collisions:0 txqueuelen:1000
         *           RX bytes:244099733 (232.7 MiB)  TX bytes:3521028936 (3.2 GiB)
         *           Memory:fb600000-fb67ffff
         */
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

    private static String getHostMacVirtualMachine(String networkCard, BufferedReader reader) throws Exception {
        /**
         * enp0s3: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
         *         inet 10.20.61.55  netmask 255.255.255.240  broadcast 10.20.61.63
         *         inet6 fe80::a00:27ff:fecb:7dab  prefixlen 64  scopeid 0x20<link>
         *         ether 08:00:27:cb:7d:ab  txqueuelen 1000  (Ethernet)
         *         RX packets 4672  bytes 2614935 (2.4 MiB)
         *         RX errors 0  dropped 0  overruns 0  frame 0
         *         TX packets 3003  bytes 2625698 (2.5 MiB)
         *         TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
         */
        String hWaddr = null;
        List<String> list = networkCardInfo2OneLine(reader);
        for (String line : list) {
            if (StringUtil.isNotBlank(line) && line.startsWith(networkCard)) {
                int start = line.indexOf("ether ");
                int end = line.indexOf("txqueuelen");
                hWaddr = line.substring(start + 6, end);
                hWaddr = hWaddr.replace(":", "").toLowerCase().trim();
                return hWaddr;
            }
        }
        return "";
    }

    public static List<NetworkCard> getNetworkList() throws Exception {
        List<NetworkCard> networkCards = new ArrayList<>();
        if (isLinux()) {
            Runtime runt = Runtime.getRuntime();
            Process p = runt.exec("ifconfig -a");
            // 流只能被读一次
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            // 开发环境测试
            // File f = new File("E:/etc/ip-85.29.txt");
            // FileInputStream fis = new FileInputStream(f);
            // BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            List<String> list = networkCardInfo2OneLine(reader);

            if (FileUtil.checkPath("/etc/centos-release"))
                networkCards = getNetworkListCentos(list);
            else if (FileUtil.checkPath("/etc/neokylin-release"))
                networkCards = getNetworkListNeoKylin(list);
            else
                throw new Exception("not support current os(must centos、neokylin)");
        } else {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) networkInterfaces.nextElement();
                if (ni.getHardwareAddress() != null) {
                    NetworkCard card = new NetworkCard();
                    card.setName(ni.getName());
                    card.setDisplayName(ni.getDisplayName());
                    card.setMac(HexUtil.byte2Hex(ni.getHardwareAddress()));
                }

            }
        }
        return networkCards;
    }

    private static List<NetworkCard> getNetworkListCentos(List<String> list) throws Exception {
        List<NetworkCard> networkCards = new ArrayList<>();

        String name = "";
        String ip = "";
        String bcast = "";
        String mask = "";
        String ipv6 = "";
        String mac= "";
        boolean status = false;

        String nameKey = "Link encap";
        String ipKey = "inet addr:";
        String bcastKey = "Bcast:";
        String ipv6Key = "inet6 addr:";
        String maskKey = "Mask:";
        String macKey = "HWaddr ";
        int macLength = 17;

        boolean virtualMachine = isVirtualMachine(list.get(0));
        if (virtualMachine) {
            return getNetworkListVirtual(list);
        }

        for (String section : list) {
            int l = section.indexOf(nameKey);
            name = section.substring(0, l).trim();
            if (!section.startsWith("lo")) {
                int ipIndex = section.indexOf(ipKey);
                int bcastIndex = section.indexOf(bcastKey);
                int maskIndex = section.indexOf(maskKey);
                int ipv6Index = section.indexOf(ipv6Key);
                int upIndex = section.indexOf(" UP ");
                int broadcastIndex = section.indexOf(" BROADCAST ");
                int macIndex = section.indexOf(macKey);

                if (ipIndex != -1) {
                    ip = section.substring(ipIndex + ipKey.length(), bcastIndex);
                } else {
                    ip = "";
                }

                if (bcastIndex != -1) {
                    bcast = section.substring(bcastIndex + bcastKey.length(), maskIndex);
                } else {
                    bcast = "";
                }

                if (maskIndex != -1) {
                    if (ipv6Index == -1) {
                        if (upIndex == -1) {
                            mask = section.substring(maskIndex + maskKey.length(), broadcastIndex);
                        } else {
                            mask = section.substring(maskIndex + maskKey.length(), upIndex);
                        }
                    } else {
                        ipv6 = section.substring(ipv6Index + ipv6Key.length(), upIndex);
                        mask = section.substring(maskIndex + maskKey.length(), ipv6Index);
                    }
                } else {
                    mask = "";
                }

                if (upIndex == -1) {
                    status = false;
                } else {
                    status = true;
                }

                if (macIndex != -1) {
                    mac = section.substring(macIndex + macKey.length(), macIndex + macKey.length() + macLength);
                    mac = mac.replace(":", "").toLowerCase().trim();
                }

                NetworkCard networkCard = new NetworkCard();
                networkCard.setName(name.trim());
                networkCard.setIp(ip.trim());
                networkCard.setBcast(bcast.trim());
                networkCard.setMask(mask.trim());
                networkCard.setUp(status);
                networkCard.setIpv6(ipv6.trim());
                networkCard.setMac(mac.trim());

                networkCards.add(networkCard);

            }
        }

        return networkCards;
    }

    private static List<NetworkCard> getNetworkListNeoKylin(List<String> list) throws Exception {
        return getNetworkListVirtual(list);
    }

    private static List<NetworkCard> getNetworkListVirtual(List<String> list) throws Exception {
        List<NetworkCard> networkCards = new ArrayList<>();

        String name = "";
        String ip = "";
        String bcast = "";
        String mask = "";
        String ipv6 = "";
        String mac = "";
        boolean status = false;

        String nameKey = "flags=";
        String ipKey = "inet";
        String bcastKey = "broadcast";
        String ipv6SKey = "inet6";
        String ipv6EKey = "prefixlen";
        String maskKey = "netmask";
        String macKey = "ether ";
        int macLength = 17;

        for (String section : list) {
            int l = section.indexOf(nameKey);
            name = section.substring(0, l).trim();
            if (name.lastIndexOf(":") == name.length() - 1) {
                name = name.substring(0, name.lastIndexOf(":"));
            }

            if (!section.startsWith("lo")) {
                int ipIndex = section.indexOf(ipKey);
                int bcastIndex = section.indexOf(bcastKey);
                int maskIndex = section.indexOf(maskKey);
                int ipv6Index = section.indexOf(ipv6SKey);
                int ipv6IndexEnd = section.indexOf(ipv6EKey);
                int macIndex = section.indexOf(macKey);


                if (ipIndex != -1) {
                    ip = section.substring(ipIndex + ipKey.length(), maskIndex);
                } else {
                    ip = "";
                }

                if (bcastIndex != -1) {
                    bcast = section.substring(bcastIndex + bcastKey.length(), ipv6Index);
                } else {
                    bcast = "";
                }

                if (maskIndex != -1) {
                    mask = section.substring(maskIndex + maskKey.length(), bcastIndex);
                } else {
                    mask = "";
                }

                if (ipv6Index != -1) {
                    ipv6 = section.substring(ipv6Index + ipv6SKey.length(), ipv6IndexEnd);
                } else {
                    ipv6 = "";
                }


                if (macIndex != -1) {
                    mac = section.substring(macIndex + macKey.length(), macIndex + macKey.length() + macLength);
                    mac = mac.replace(":", "").toLowerCase().trim();
                } else {
                    mac = "";
                }



                NetworkCard networkCard = new NetworkCard();
                networkCard.setName(name.trim());
                networkCard.setIp(ip.trim());
                networkCard.setBcast(bcast.trim());
                networkCard.setMask(mask.trim());
                networkCard.setUp(status);
                networkCard.setIpv6(ipv6.trim());
                networkCard.setMac(mac.trim());

                networkCards.add(networkCard);

            }
        }

        return networkCards;
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
        // System.out.println(getHostMac("eth0"));
        System.out.println(JSON.toJSONString(getNetworkList()));
    }
}
