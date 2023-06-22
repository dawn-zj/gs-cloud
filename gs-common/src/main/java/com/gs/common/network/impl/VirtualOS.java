package com.gs.common.network.impl;

import com.gs.common.entity.NetworkCard;
import com.gs.common.util.NetWorkUtil;
import com.gs.common.util.StringUtil;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class VirtualOS extends Base {
    /**
     * 获取网口列表
     *
     * @return
     */
    @Override
    public List<NetworkCard> getNetworkList(List<String> list) throws Exception {
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

    @Override
    public String getMac(String networkCard, BufferedReader reader) throws Exception {
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
        List<String> list = NetWorkUtil.networkCardInfo2OneLine(reader);
        for (String line : list) {
            if (StringUtil.isNotBlank(line) && line.startsWith(networkCard)) {
                int start = line.indexOf("ether ");
                int end = line.indexOf("txqueuelen");
                hWaddr = line.substring(start + 6, end);
                return hWaddr;
            }
        }
        return "";
    }
}
