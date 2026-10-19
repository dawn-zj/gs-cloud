package com.gs.common.entity.crypto;

import java.security.PrivateKey;

/**
 * @Author Zhang Juan
 * @Date 2024/6/19 16:04
 */
public class SM2PrivateKey implements PrivateKey {
    private int bits = 256;
    private byte[] D = new byte[32];

    public SM2PrivateKey() {
    }

    public SM2PrivateKey(byte[] prik) {
        System.arraycopy(prik, prik.length - 32, this.D, 0, 32);
    }

    public int getBits() {
        return this.bits;
    }

    public void setBits(int bits) {
        this.bits = bits;
    }

    public byte[] getD() {
        return this.D;
    }

    public void setD(byte[] d) {
        System.arraycopy(d, 0, this.D, 0, 32);
    }

    public String getAlgorithm() {
        return "SM2";
    }

    public byte[] getEncoded() {
        return this.D;
    }

    public String getFormat() {
        return "X.509";
    }
}
