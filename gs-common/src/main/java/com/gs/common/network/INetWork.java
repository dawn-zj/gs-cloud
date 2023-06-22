package com.gs.common.network;

import com.gs.common.entity.NetworkCard;

import java.io.BufferedReader;
import java.util.List;

public interface INetWork {
    /**
     * 获取网口列表
     * @param list
     * @return
     * @throws Exception
     */
    List<NetworkCard> getNetworkList(List<String> list) throws Exception;

    /**
     * 获取指定网口MAC信息
     * @param networkCard
     * @param reader
     * @return
     * @throws Exception
     */
    String getMac(String networkCard, BufferedReader reader) throws Exception;

}
