package com.gs.common.algorithm;

import java.security.InvalidParameterException;
import java.security.SecureRandom;

/**
 * SM4对称加密算法
 * 国家密码局2012年发布的分组密码标准，密钥长度和分组长度均为128位
 * 它将明文分成多个等长的模块（block），使用确定的算法和对称密钥对每组分别加密解密
 * 原理：参考https://blog.csdn.net/weixin_37569048/article/details/94983841
 */
public class SM4 {
    private static final int[] fk = new int[]{-1548633402, 1453994832, 1736282519, -1301273892};
    private static final byte[] Sbox = new byte[]{-42, -112, -23, -2, -52, -31, 61, -73, 22, -74, 20, -62, 40, -5, 44, 5, 43, 103, -102, 118, 42, -66, 4, -61, -86, 68, 19, 38, 73, -122, 6, -103, -100, 66, 80, -12, -111, -17, -104, 122, 51, 84, 11, 67, -19, -49, -84, 98, -28, -77, 28, -87, -55, 8, -24, -107, -128, -33, -108, -6, 117, -113, 63, -90, 71, 7, -89, -4, -13, 115, 23, -70, -125, 89, 60, 25, -26, -123, 79, -88, 104, 107, -127, -78, 113, 100, -38, -117, -8, -21, 15, 75, 112, 86, -99, 53, 30, 36, 14, 94, 99, 88, -47, -94, 37, 34, 124, 59, 1, 33, 120, -121, -44, 0, 70, 87, -97, -45, 39, 82, 76, 54, 2, -25, -96, -60, -56, -98, -22, -65, -118, -46, 64, -57, 56, -75, -93, -9, -14, -50, -7, 97, 21, -95, -32, -82, 93, -92, -101, 52, 26, 85, -83, -109, 50, 48, -11, -116, -79, -29, 29, -10, -30, 46, -126, 102, -54, 96, -64, 41, 35, -85, 13, 83, 78, 111, -43, -37, 55, 69, -34, -3, -114, 47, 3, -1, 106, 114, 109, 108, 91, 81, -115, 27, -81, -110, -69, -35, -68, 127, 17, -39, 92, 65, 31, 16, 90, -40, 10, -63, 49, -120, -91, -51, 123, -67, 45, 116, -48, 18, -72, -27, -76, -80, -119, 105, -105, 74, 12, -106, 119, 126, 101, -71, -15, 9, -59, 110, -58, -124, 24, -16, 125, -20, 58, -36, 77, 32, 121, -18, 95, 62, -41, -53, 57, 72};
    private static final int SM4ROUND = 32;
    private static final int[] CK = new int[]{462357, 472066609, 943670861, 1415275113, 1886879365, -1936483679, -1464879427, -993275175, -521670923, -66909679, 404694573, 876298825, 1347903077, 1819507329, -2003855715, -1532251463, -1060647211, -589042959, -117504499, 337322537, 808926789, 1280531041, 1752135293, -2071227751, -1599623499, -1128019247, -656414995, -184876535, 269950501, 741554753, 1213159005, 1684763257};
    public static final int SM4_BAD_KEY = 2;
    public static final int SM4_SUCCESS = 0;
    public static final int SM4_BLOCK_SIZE = 16;

    public SM4() {
    }

    private static int trans0(int A) {
        return (Sbox[A >>> 24 & 255] << 24 & -16777216) + (Sbox[A >>> 16 & 255] << 16 & 16711680) + (Sbox[A >>> 8 & 255] << 8 & '\uff00') + (Sbox[A & 255] & 255);
    }

    private static int ROL(int x, int y) {
        return (x << y | (x & -1) >>> 32 - y) & -1;
    }

    private static int trans1(int x_param) {
        int temp = trans0(x_param);
        return temp ^ ROL(temp, 2) ^ ROL(temp, 10) ^ ROL(temp, 18) ^ ROL(temp, 24);
    }

    private static int trans2(int input) {
        int temp = trans0(input);
        return temp ^ ROL(temp, 13) ^ ROL(temp, 23);
    }

    private static int ansio2i(byte[] xarray, int offset) {
        return (xarray[4 * offset] & 255) << 24 & -16777216 | (xarray[4 * offset + 1] & 255) << 16 & 16711680 | (xarray[4 * offset + 2] & 255) << 8 & '\uff00' | xarray[4 * offset + 3] & 255 & 255;
    }

    public static void sm4_key_exp(byte[] omk, int[] rk) {
        int[] mk = new int[4];
        int[] k = new int[4];
        mk[0] = ansio2i(omk, 0);
        mk[1] = ansio2i(omk, 1);
        mk[2] = ansio2i(omk, 2);
        mk[3] = ansio2i(omk, 3);
        k[0] = mk[0] ^ fk[0];
        k[1] = mk[1] ^ fk[1];
        k[2] = mk[2] ^ fk[2];
        k[3] = mk[3] ^ fk[3];

        for(int i = 0; i < 32; ++i) {
            k[i & 3] ^= trans2(k[i + 1 & 3] ^ k[i + 2 & 3] ^ k[i + 3 & 3] ^ CK[i]);
            rk[i] = k[i & 3];
        }

    }

    public static void sm4_encrypt_rk(byte[] p_block, int[] rk, byte[] c_block) {
        sm4_encrypt_rk(p_block, 0, rk, c_block, 0);
    }

    private static void sm4_encrypt_rk(byte[] p_block, int p_offset, int[] rk, byte[] c_block, int c_offset) {
        int[] temp_block = new int[4];
        temp_block[0] = p_block[p_offset] << 24 & -16777216 | p_block[1 + p_offset] << 16 & 16711680 | p_block[2 + p_offset] << 8 & '\uff00' | p_block[3 + p_offset] & 255;
        temp_block[1] = p_block[4 + p_offset] << 24 & -16777216 | p_block[5 + p_offset] << 16 & 16711680 | p_block[6 + p_offset] << 8 & '\uff00' | p_block[7 + p_offset] & 255;
        temp_block[2] = p_block[8 + p_offset] << 24 & -16777216 | p_block[9 + p_offset] << 16 & 16711680 | p_block[10 + p_offset] << 8 & '\uff00' | p_block[11 + p_offset] & 255;
        temp_block[3] = p_block[12 + p_offset] << 24 & -16777216 | p_block[13 + p_offset] << 16 & 16711680 | p_block[14 + p_offset] << 8 & '\uff00' | p_block[15 + p_offset] & 255;

        for(int i = 0; i < 32; ++i) {
            temp_block[i & 3] ^= trans1(temp_block[i + 1 & 3] ^ temp_block[i + 2 & 3] ^ temp_block[i + 3 & 3] ^ rk[i]);
        }

        c_block[c_offset] = (byte)(temp_block[3] >>> 24 & 255);
        c_block[1 + c_offset] = (byte)(temp_block[3] >>> 16 & 255);
        c_block[2 + c_offset] = (byte)(temp_block[3] >>> 8 & 255);
        c_block[3 + c_offset] = (byte)(temp_block[3] & 255);
        c_block[4 + c_offset] = (byte)(temp_block[2] >>> 24 & 255);
        c_block[5 + c_offset] = (byte)(temp_block[2] >>> 16 & 255);
        c_block[6 + c_offset] = (byte)(temp_block[2] >>> 8 & 255);
        c_block[7 + c_offset] = (byte)(temp_block[2] & 255);
        c_block[8 + c_offset] = (byte)(temp_block[1] >>> 24 & 255);
        c_block[9 + c_offset] = (byte)(temp_block[1] >>> 16 & 255);
        c_block[10 + c_offset] = (byte)(temp_block[1] >>> 8 & 255);
        c_block[11 + c_offset] = (byte)(temp_block[1] & 255);
        c_block[12 + c_offset] = (byte)(temp_block[0] >>> 24 & 255);
        c_block[13 + c_offset] = (byte)(temp_block[0] >>> 16 & 255);
        c_block[14 + c_offset] = (byte)(temp_block[0] >>> 8 & 255);
        c_block[15 + c_offset] = (byte)(temp_block[0] & 255);
    }

    public static void sm4_decrypt_rk(byte[] c_block, int[] rk, byte[] p_block) {
        sm4_decrypt_rk(c_block, 0, rk, p_block, 0);
    }


    private static void sm4_decrypt_rk(byte[] c_block, int c_offset, int[] rk, byte[] p_block, int p_offset) {
        int[] temp_block = new int[4];
        temp_block[0] = c_block[c_offset] << 24 & -16777216 | c_block[1 + c_offset] << 16 & 16711680 | c_block[2 + c_offset] << 8 & '\uff00' | c_block[3 + c_offset] & 255;
        temp_block[1] = c_block[4 + c_offset] << 24 & -16777216 | c_block[5 + c_offset] << 16 & 16711680 | c_block[6 + c_offset] << 8 & '\uff00' | c_block[7 + c_offset] & 255;
        temp_block[2] = c_block[8 + c_offset] << 24 & -16777216 | c_block[9 + c_offset] << 16 & 16711680 | c_block[10 + c_offset] << 8 & '\uff00' | c_block[11 + c_offset] & 255;
        temp_block[3] = c_block[12 + c_offset] << 24 & -16777216 | c_block[13 + c_offset] << 16 & 16711680 | c_block[14 + c_offset] << 8 & '\uff00' | c_block[15 + c_offset] & 255;

        for(int i = 0; i < 32; ++i) {
            temp_block[i & 3] ^= trans1(temp_block[i + 1 & 3] ^ temp_block[i + 2 & 3] ^ temp_block[i + 3 & 3] ^ rk[31 - i]);
        }

        p_block[p_offset] = (byte)(temp_block[3] >>> 24 & 255);
        p_block[1 + p_offset] = (byte)(temp_block[3] >>> 16 & 255);
        p_block[2 + p_offset] = (byte)(temp_block[3] >>> 8 & 255);
        p_block[3 + p_offset] = (byte)(temp_block[3] & 255);
        p_block[4 + p_offset] = (byte)(temp_block[2] >>> 24 & 255);
        p_block[5 + p_offset] = (byte)(temp_block[2] >>> 16 & 255);
        p_block[6 + p_offset] = (byte)(temp_block[2] >>> 8 & 255);
        p_block[7 + p_offset] = (byte)(temp_block[2] & 255);
        p_block[8 + p_offset] = (byte)(temp_block[1] >>> 24 & 255);
        p_block[9 + p_offset] = (byte)(temp_block[1] >>> 16 & 255);
        p_block[10 + p_offset] = (byte)(temp_block[1] >>> 8 & 255);
        p_block[11 + p_offset] = (byte)(temp_block[1] & 255);
        p_block[12 + p_offset] = (byte)(temp_block[0] >>> 24 & 255);
        p_block[13 + p_offset] = (byte)(temp_block[0] >>> 16 & 255);
        p_block[14 + p_offset] = (byte)(temp_block[0] >>> 8 & 255);
        p_block[15 + p_offset] = (byte)(temp_block[0] & 255);
    }


    public static int sm4_encrypt(byte[] p_block, byte[] key, int key_len_bytes, byte[] c_block) {
        int[] rk = new int[32];
        if (key_len_bytes != 16) {
            return 2;
        } else {
            sm4_key_exp(key, rk);
            sm4_encrypt_rk(p_block, rk, c_block);
            return 0;
        }
    }

    public static int sm4_decrypt(byte[] c_block, byte[] key, int key_len_bytes, byte[] p_block) {
        int[] rk = new int[32];
        if (key_len_bytes != 16) {
            return 2;
        } else {
            sm4_key_exp(key, rk);
            sm4_decrypt_rk(c_block, rk, p_block);
            return 0;
        }
    }

    public static final int sm4_cbc_encrypt(byte[] in, int in_len_bytes, byte[] out, byte[] key, int key_len_bytes, byte[] ivec) {
        byte[] tr = new byte[16];
        byte[] iv = new byte[16];
        int[] rk = new int[32];
        int pc = in_len_bytes & 15;
        int numBlocks = in_len_bytes - pc >>> 4;
        pc = 16 - pc;
        if (key_len_bytes != 16) {
            return 2;
        } else {
            sm4_key_exp(key, rk);

            int j;
            for(j = 0; j < 16; ++j) {
                iv[j] = ivec[j];
            }

            int i;
            for(i = 0; i < numBlocks; ++i) {
                for(j = 0; j < 16; ++j) {
                    tr[j] = (byte)(in[j + i * 16] ^ iv[j]);
                }

                sm4_encrypt_rk(tr, rk, tr);

                for(j = 0; j < 16; ++j) {
                    out[j + i * 16] = tr[j];
                    iv[j] = tr[j];
                }
            }

            j = in_len_bytes - numBlocks * 16;
            if (pc == 0) {
                pc = 16;
            }

            for(i = 0; i < j; ++i) {
                tr[i] = in[numBlocks * 16 + i];
            }

            for(i = j; i < 16; ++i) {
                tr[i] = (byte)pc;
            }

            for(i = 0; i < 16; ++i) {
                tr[i] ^= iv[i];
            }

            sm4_encrypt_rk(tr, 0, rk, out, numBlocks * 16);
            return 0;
        }
    }

    public static final byte[] sm4_cbc_decrypt(byte[] in, int in_len_bytes, byte[] out2, int out_len_bytes, byte[] key, int key_len_bytes, byte[] ivec) {
        byte[] tr = new byte[16];
        byte[] iv = new byte[16];
        int[] rk = new int[32];
        byte[] out = new byte[in_len_bytes];
        int numBlocks = in_len_bytes >>> 4;
        if (key_len_bytes != 16) {
            throw new InvalidParameterException("SM4 Key Length Ilegal");
        } else {
            sm4_key_exp(key, rk);

            int i;
            for(i = 0; i < 16; ++i) {
                iv[i] = ivec[i];
            }

            for(i = 0; i < numBlocks; ++i) {
                int j;
                for(j = 0; j < 16; ++j) {
                    tr[j] = in[j + i * 16];
                }

                sm4_decrypt_rk(tr, rk, tr);

                for(j = 0; j < 16; ++j) {
                    out[j + i * 16] = (byte)(tr[j] ^ iv[j]);
                    iv[j] = in[j + i * 16];
                }
            }

            out_len_bytes = in_len_bytes - (out[in_len_bytes - 1] & 255);
            byte[] ct = new byte[out_len_bytes];
            System.arraycopy(out, 0, ct, 0, out_len_bytes);
            return ct;
        }
    }

    /**
     * SM4 ECB模式加密
     * @param plain 原文
     * @param key 对称密钥
     * @return
     */
    public static byte[] sm4_ecb_encrypt(byte[] plain, byte[] key) {
        int encLength = plain.length + (16 - plain.length % 16);
        byte[] enced = new byte[encLength];
        return sm4_ecb_encrypt(plain, plain.length, enced, key, key.length) == 0 ? enced : null;
    }

    /**
     * SM4 ECB模式加密
     * @param in 原文
     * @param in_len_bytes 原文长度
     * @param out 加密后存放位置
     * @param key 对称密钥
     * @param key_len_bytes 对称密钥长度
     * @return
     */
    private static final int sm4_ecb_encrypt(byte[] in, int in_len_bytes, byte[] out, byte[] key, int key_len_bytes) {
        byte[] tr = new byte[16];
        int[] rk = new int[32];
        int pc = in_len_bytes & 15;
        int numBlocks = in_len_bytes - pc >>> 4;
        pc = 16 - pc;
        if (key_len_bytes != 16) {
            return 2;
        } else {
            sm4_key_exp(key, rk);

            int i;
            int j;
            for(i = 0; i < numBlocks; ++i) {
                for(j = 0; j < 16; ++j) {
                    tr[j] = in[i * 16 + j];
                }

                sm4_encrypt_rk(tr, 0, rk, out, i * 16);
            }

            j = in_len_bytes - numBlocks * 16;
            if (pc == 0) {
                pc = 16;
            }

            for(i = 0; i < j; ++i) {
                tr[i] = in[numBlocks * 16 + i];
            }

            for(i = j; i < 16; ++i) {
                tr[i] = (byte)pc;
            }

            sm4_encrypt_rk(tr, 0, rk, out, numBlocks * 16);
            return 0;
        }
    }

    /**
     * SM4 ECB模式解密
     * @param enced 加密的数据
     * @param key 对称密钥
     * @return
     */
    public static byte[] sm4_ecb_decrypt(byte[] enced, byte[] key) {
        byte[] plain = new byte[enced.length];
        if (sm4_ecb_decrypt(enced, enced.length, plain, 0, key, key.length) == 0) {
            byte[] tmp = new byte[enced.length - (plain[plain.length - 1] & 255)];
            System.arraycopy(plain, 0, tmp, 0, tmp.length);
            return tmp;
        } else {
            return null;
        }
    }

    /**
     * SM4 ECB模式解密
     * @param in 加密的数据
     * @param in_len_bytes 加密的数据长度
     * @param out 解密后存放位置
     * @param out_len_bytes 解密后数据长度
     * @param key 对称密钥
     * @param key_len_bytes 对称密钥长度
     * @return
     */
    private static final int sm4_ecb_decrypt(byte[] in, int in_len_bytes, byte[] out, int out_len_bytes, byte[] key, int key_len_bytes) {
        byte[] tr = new byte[16];
        int[] rk = new int[32];
        int numBlocks = in_len_bytes / 16;
        if (key_len_bytes != 16) {
            return 2;
        } else {
            sm4_key_exp(key, rk);

            for(int i = 0; i < numBlocks; ++i) {
                for(int j = 0; j < 16; ++j) {
                    tr[j] = in[j + i * 16];
                }

                sm4_decrypt_rk(tr, 0, rk, out, i * 16);
            }

            int var10000 = in_len_bytes - (out[in_len_bytes - 1] & 255);
            return 0;
        }
    }

    private static final void validation() {
        byte[] key = new byte[]{1, 35, 69, 103, -119, -85, -51, -17, -2, -36, -70, -104, 118, 84, 50, 16};
        byte[] pt = new byte[16];
        byte[] ct = new byte[16];
        int[] rk = new int[32];
        sm4_key_exp(key, rk);

        int i;
        for(i = 0; i < 16; ++i) {
            pt[i] = key[i];
        }

        sm4_encrypt_rk(pt, rk, ct);
        System.out.print("\nCorrect Vector     :68 1e df 34 d2 06 96 5e 86 b3 e9 4f 53 6e 42 46\nVerification Vector:");
        sm4_decrypt_rk(ct, rk, pt);
        System.out.println("\nplaintext:");

        for(i = 0; i < 500000; ++i) {
            sm4_encrypt_rk(pt, rk, ct);
            sm4_encrypt_rk(ct, rk, pt);
        }

        System.out.println("\n1M iteration Vaildation vector:\n59 52 98 c7 c6 fd 27 1f 04 02 f8 04 c3 3d 3f 66");
        System.out.println();
    }

    public static void main(String[] args) {
        byte[] key = new byte[]{-27, -72, 123, 77, -11, -24, 72, 32, 84, 20, 72, -79, 31, 90, -87, 63};
        byte[] IV = new byte[]{18, -92, -73, 57, -18, -102, -62, 67, -105, 36, -40, -64, -15, -89, 57, 113};
        byte[] p1k = new byte[1024];
        byte[] c1k = new byte[1040];
        byte[] p8k = new byte[8192];
        byte[] c8k = new byte[8208];
        int max = 1024;
        System.out.println(Integer.toBinaryString(-16711936) + "\n" + Integer.toBinaryString(ROL(-16711936, 13)));

        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");

            int i;
            for(i = 0; i < max; ++i) {
                p1k[i] = (byte)(sr.nextInt() & 255);
            }

            for(i = 0; i < 8192; ++i) {
                p8k[i] = (byte)(sr.nextInt() & 255);
            }

            max = 100000;
            long start = System.currentTimeMillis();

            for(i = 0; i < max; ++i) {
                sm4_cbc_encrypt(p1k, 1024, c1k, key, 16, IV);
            }

            long end = System.currentTimeMillis();
            start = System.currentTimeMillis();

            for(i = 0; i < max; ++i) {
                sm4_cbc_encrypt(p8k, 8192, c8k, key, 16, IV);
            }

            end = System.currentTimeMillis();
            System.out.println(start + "\n" + end);
            validation();
        } catch (Exception var14) {
            var14.printStackTrace();
        }

    }
}

