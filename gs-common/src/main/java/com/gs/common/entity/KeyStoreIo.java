package com.gs.common.entity;

public class KeyStoreIo {
    private String p10;
    private byte[] priKeyData;

    public KeyStoreIo(String p10, byte[] priKeyData) {
        this.p10 = p10;
        this.priKeyData = priKeyData;
    }

    public String getP10() {
        return p10;
    }

    public void setP10(String p10) {
        this.p10 = p10;
    }

    public byte[] getPriKeyData() {
        return priKeyData;
    }

    public void setPriKeyData(byte[] priKeyData) {
        this.priKeyData = priKeyData;
    }
}
