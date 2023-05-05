package com.gs.common.network.impl;

import com.gs.common.entity.NetworkCard;
import com.gs.common.util.NetWorkUtil;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class CentOS extends Base {

    /**
     * 获取网口列表
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
        String mac= "";
        boolean status = false;

        String nameKey = "Link encap";
        String ipKey = "inet addr:";
        String bcastKey = "Bcast:";
        String ipv6Key = "inet6 addr:";
        String maskKey = "Mask:";
        String macKey = "HWaddr ";
        int macLength = 17;

        boolean virtualMachine = NetWorkUtil.isVirtualMachine(list.get(0));
        if (virtualMachine) {
            return new VirtualOS().getNetworkList(list);
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

    @Override
    public String getMac(String networkCard, BufferedReader reader) throws Exception {
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
            }
        }
        return hWaddr;
    }
}
