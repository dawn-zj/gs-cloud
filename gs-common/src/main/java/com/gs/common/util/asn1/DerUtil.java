package com.gs.common.util.asn1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DerUtil {
    private static final long DATE_2050 = 2524579200000L;
    public static final byte[] DERNULL = new byte[]{5, 0};
    public static final byte[] DERTRUE = new byte[]{1, 1, -1};
    public static final int DER_TYPE_SEQUENCE = 48;
    public static final int DER_TYPE_CONTEXT = 160;
    public static final int DER_TYPE_SET = 49;
    public static final int DER_TYPE_BOOLEAN = 1;
    public static final int DER_TYPE_INTEGER = 2;
    public static final int DER_TYPE_BIT_STRING = 3;
    public static final int DER_TYPE_OCTET_STRING = 4;
    public static final int DER_TYPE_OID = 6;
    public static final int DER_TYPE_UTCTIME = 17;
    public static final int DER_TYPE_UT8STRING = 12;
    public static final int DER_TYPE_PRINTABLESTRING = 19;
    public static final int MAX_SEGMENT_SIZE = 209715200;
    private static SimpleDateFormat formatShort8;
    private static SimpleDateFormat formatLong8;
    private static SimpleDateFormat formatShort;
    private static SimpleDateFormat formatLong;

    static {
        TimeZone tz = TimeZone.getDefault();
        tz.setRawOffset(8);
        formatShort8 = new SimpleDateFormat("yyMMddHHmmss");
        formatShort8.setTimeZone(tz);
        formatLong8 = new SimpleDateFormat("yyyyMMddHHmmss");
        formatLong8.setTimeZone(tz);
        formatShort = new SimpleDateFormat("yyMMddHHmmss");
        formatLong = new SimpleDateFormat("yyyyMMddHHmmss");
    }

    public DerUtil() {
    }

    public static byte[] cutFirstItem(byte[] der) {
        return cutHeadItems(der, 1);
    }

    public static byte[] cutHeadItems(byte[] der, int index) {
        int derStart = 0;
        int length = 0;
        int headLength = 2;

        for(int i = 0; i < index; ++i) {
            int ab = der[derStart + 1] & 255;
            if (ab >= 128) {
                int lengthOfLength = ab - 128;
                headLength = 2 + lengthOfLength;
                byte[] lengthBytes = new byte[lengthOfLength];
                System.arraycopy(der, derStart + 2, lengthBytes, 0, lengthOfLength);
                length = generateInt(lengthBytes);
            } else {
                length = ab;
            }

            if (i != index - 1) {
                derStart = derStart + length + headLength;
            }
        }

        byte[] bs = new byte[der.length - (derStart + length + headLength)];
        System.arraycopy(der, derStart + length + headLength, bs, 0, der.length - (derStart + length + headLength));
        return bs;
    }

    public static byte[] connect(byte[][] bs) {
        int lengthTotal = 0;

        for(int i = 0; i < bs.length; ++i) {
            lengthTotal += bs[i].length;
        }

        byte[] total = new byte[lengthTotal];
        int start = 0;

        for(int i = 0; i < bs.length; ++i) {
            System.arraycopy(bs[i], 0, total, start, bs[i].length);
            start += bs[i].length;
        }

        return total;
    }

    public static String getCRLDPURI(byte[] CRLDistributionPoints) {
        byte[] data = getDERInnerData(CRLDistributionPoints);
        data = getDERInnerData(data);
        data = getDERInnerData(data);
        data = getDERInnerData(data);
        data = getDERInnerData(data);
        data = getDERInnerData(data);
        return new String(data);
    }

    public static byte[] getDERInnerData(byte[] der) {
        return getDERInnerData(der, 1);
    }

    public static byte[] getDERInnerData(byte[] der, int index) {
        int derStart = 0;
        int length = 0;
        int headLength = 2;

        for(int i = 0; i < index; ++i) {
            int ab = der[derStart + 1] & 255;
            if (ab >= 128) {
                int lengthOfLength = ab - 128;
                headLength = 2 + lengthOfLength;
                byte[] lengthBytes = new byte[lengthOfLength];
                System.arraycopy(der, derStart + 2, lengthBytes, 0, lengthOfLength);
                length = generateInt(lengthBytes);
            } else {
                length = ab;
            }

            if (i != index - 1) {
                derStart = derStart + length + headLength;
            }
        }

        byte[] bs = new byte[length];
        System.arraycopy(der, derStart + headLength, bs, 0, length);
        return bs;
    }

    public static byte[] getFirstDERItem(byte[] der) {
        return getDERItem(der, 1);
    }

    public static byte[] getDERItem(byte[] der, int index) {
        int derStart = 0;
        int length = 0;
        int headLength = 2;

        for(int i = 0; i < index; ++i) {
            int ab = der[derStart + 1] & 255;
            if (ab >= 128) {
                int lengthOfLength = ab - 128;
                headLength = 2 + lengthOfLength;
                byte[] lengthBytes = new byte[lengthOfLength];
                System.arraycopy(der, derStart + 2, lengthBytes, 0, lengthOfLength);
                length = generateInt(lengthBytes);
            } else {
                length = ab;
            }

            if (i != index - 1) {
                derStart = derStart + length + headLength;
            }
        }

        byte[] bs = new byte[length + headLength];
        System.arraycopy(der, derStart, bs, 0, length + headLength);
        return bs;
    }

    public static byte[] generateDERCode(int type, byte[] content) {
        int length = content.length;
        byte[] lengthBs = null;
        byte[] all;
        if (length >= 128) {
            all = int2Bytes(length);
            lengthBs = new byte[1 + all.length];
            lengthBs[0] = (byte)(128 + all.length);
            System.arraycopy(all, 0, lengthBs, 1, all.length);
        } else {
            lengthBs = new byte[]{(byte)length};
        }

        all = new byte[1 + lengthBs.length + content.length];
        all[0] = (byte)type;
        System.arraycopy(lengthBs, 0, all, 1, lengthBs.length);
        System.arraycopy(content, 0, all, 1 + lengthBs.length, content.length);
        return all;
    }

    public static byte[] int2Bytes(int i) {
        byte l;
        if (i <= 255) {
            l = 1;
        } else if (i <= 65535) {
            l = 2;
        } else if (i <= 16777215) {
            l = 3;
        } else {
            l = 4;
        }

        byte[] bs = new byte[l];

        for(int x = 0; x < l; ++x) {
            bs[x] = (byte)(i >> (l - 1 - x) * 8);
        }

        return bs;
    }

    public static int generateInt(byte[] bytes) {
        int tr = 0;

        for(int i = bytes.length - 1; i > -1; --i) {
            int x = (bytes[bytes.length - 1 - i] & 255) << i * 8;
            tr += x;
        }

        return tr;
    }

    public static byte[] oid2ASN1(String oid) {
        byte[] oidbs = null;
        if (oidbs != null) {
            return (byte[])oidbs;
        } else {
            String[] pieces = oid.split("\\.");
            byte b = (byte)(Integer.parseInt(pieces[0]) * 40 + Integer.parseInt(pieces[1]));
            oidbs = new byte[]{b};

            for(int i = 2; i < pieces.length; ++i) {
                long num = Long.parseLong(pieces[i]);
                int pow = maxPow128(num);
                byte[] id = new byte[pow + 1];
                genid(pow, num, id);
                byte[] tmp = new byte[oidbs.length + id.length];
                System.arraycopy(oidbs, 0, tmp, 0, oidbs.length);
                System.arraycopy(id, 0, tmp, oidbs.length, id.length);
                oidbs = tmp;
            }

            return oidbs;
        }
    }

    public static String ASN12OID(byte[] oid) {
        String oidStr = null;
        if (oidStr != null) {
            return oidStr;
        } else {
            oidStr = "";
            int asegment = 0;
            int index = 0;

            int s1;
            int x;
            for(s1 = oid.length - 1; s1 >= 0 && s1 != 0; --s1) {
                x = oid[s1] & 255;
                if (x < 128) {
                    if (asegment != 0) {
                        oidStr = "." + asegment + oidStr;
                        asegment = 0;
                        index = 0;
                    }
                } else {
                    x -= 128;
                }

                if (index == 0) {
                    asegment = x;
                } else {
                    asegment = (int)((double)asegment + Math.pow(128.0D, (double)index) * (double)x);
                }

                ++index;
            }

            s1 = (oid[0] & 255) / 40;
            x = (oid[0] & 255) % 40;
            oidStr = s1 + "." + x + "." + asegment + oidStr;
            return oidStr;
        }
    }

    private static int maxPow128(long num) {
        int i;
        for(i = 0; num >= 1L << (i + 1) * 7; ++i) {
        }

        return i;
    }

    private static void genid(int pow, long num, byte[] id) {
        if (pow == 0) {
            id[id.length - 1] = (byte)((int)num);
        } else {
            id[id.length - 1 - pow] = (byte)((int)(128.0D + (double)num / Math.pow(128.0D, (double)pow)));
        }

        if (pow - 1 >= 0) {
            genid(pow - 1, (long)((int)((double)num % Math.pow(128.0D, (double)pow))), id);
        }

    }

    public static byte[] connect(byte[] a, byte[] b) {
        if (a == null) {
            a = new byte[0];
        }

        byte[] tmp = new byte[a.length + b.length];
        System.arraycopy(a, 0, tmp, 0, a.length);
        System.arraycopy(b, 0, tmp, a.length, b.length);
        return tmp;
    }

    public static synchronized byte[] date2ASN1(Date date) {
        String time;
        if (date.getTime() < 2524579200000L) {
            time = formatShort8.format(date) + "Z";
            return generateDERCode(23, time.getBytes());
        } else {
            time = formatLong8.format(date) + "Z";
            return generateDERCode(24, time.getBytes());
        }
    }

    public static synchronized Date ASN12Date(byte[] date) throws ParseException {
        String dateStr = new String(date);
        dateStr = dateStr.substring(0, dateStr.length() - 1);
        return dateStr.length() == 12 ? formatShort8.parse(dateStr) : formatLong8.parse(dateStr);
    }

    public static void main(String[] args) {
        System.out.println(DERNULL.length);
    }
}
