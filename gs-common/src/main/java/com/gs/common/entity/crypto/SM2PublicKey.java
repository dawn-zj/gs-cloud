package com.gs.common.entity.crypto;

import java.io.Serializable;
import java.security.PublicKey;

/**
 * @Author Zhang Juan
 * @Date 2024/6/19 11:34
 */
public class SM2PublicKey implements PublicKey, Serializable {
    private int bits = 256;
    private byte[] x;
    private byte[] y;
    private byte[] encoded;
    private byte[] head = new byte[]{48, 89, 48, 19, 6, 7, 42, -122, 72, -50, 61, 2, 1, 6, 8, 42, -127, 28, -49, 85, 1, -126, 45, 3, 66, 0, 4};
    private int headLength;

    public SM2PublicKey() {
        this.headLength = this.head.length;
        this.x = new byte[32];
        this.y = new byte[32];
    }

    public SM2PublicKey(byte[] pubk) {
        this.headLength = this.head.length;
        this.encoded = new byte[pubk.length];
        System.arraycopy(pubk, 0, this.encoded, 0, pubk.length);
        this.x = new byte[32];
        this.y = new byte[32];
        System.arraycopy(pubk, pubk.length - 64, this.x, 0, 32);
        System.arraycopy(pubk, pubk.length - 32, this.y, 0, 32);
        this.encoded = new byte[this.head.length + 64];
        System.arraycopy(this.head, 0, this.encoded, 0, this.head.length);
        System.arraycopy(this.x, 0, this.encoded, this.head.length, 32);
        System.arraycopy(this.y, 0, this.encoded, this.head.length + 32, 32);
    }

    public int getBits() {
        return this.bits;
    }

    public void setBits(int bits) {
        this.bits = bits;
    }

    public byte[] getX() {
        return this.x;
    }

    public void setX(byte[] x) {
        this.encoded = null;
        System.arraycopy(x, 0, this.x, 0, 32);
    }

    public byte[] getY() {
        return this.y;
    }

    public void setY(byte[] y) {
        this.encoded = null;
        System.arraycopy(y, 0, this.y, 0, 32);
    }

    @Override
    public byte[] getEncoded() {
        if (this.encoded != null) {
            return this.encoded;
        } else {
            this.encoded = new byte[91];
            System.arraycopy(this.head, 0, this.encoded, 0, 27);
            System.arraycopy(this.x, 0, this.encoded, 27, 32);
            System.arraycopy(this.y, 0, this.encoded, 59, 32);
            return this.encoded;
        }
    }

    public byte[] getEncoded4ex() {
        byte[] encoded = new byte[65];
        encoded[0] = 4;
        System.arraycopy(this.x, 0, encoded, 1, 32);
        System.arraycopy(this.y, 0, encoded, 33, 32);
        return encoded;
    }

    @Override
    public String getAlgorithm() {
        return "SM2";
    }

    @Override
    public String getFormat() {
        return "X.509";
    }
}