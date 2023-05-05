package com.gs.common.network.impl;

import com.gs.common.entity.NetworkCard;
import com.gs.common.network.INetWork;

import java.io.BufferedReader;
import java.util.List;

public class Base implements INetWork {
    @Override
    public List<NetworkCard> getNetworkList(List<String> list) throws Exception {
        return null;
    }

    @Override
    public String getMac(String networkCard, BufferedReader reader) throws Exception {
        return null;
    }
}
