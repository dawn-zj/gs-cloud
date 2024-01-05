package com.gs.common.util;

public class HexUtil {

	public static byte[] hex2Byte(String hex) {
		byte[] bs = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length() / 2; i++) {
			String temp = hex.substring(i * 2, i * 2 + 2);
			bs[i] = Integer.valueOf(temp, 16).byteValue();
		}
		return bs;
	}

	public static String byte2Hex(byte[] bs) {
		String hex = "";
		for (int i = 0; i < bs.length; i++) {
			byte b = bs[i];
			String temp = Integer.toHexString(b & 0xff);
			if (temp.length() == 1)
				temp = "0" + temp;

			hex += temp;
		}
		return hex;
	}

	/**
	 * 整型占4个字节
	 * 
	 * @param bs
	 *            4个字节
	 * @return
	 */
	public static int byte2Int(byte[] bs) {
		int i = 0;
		i = i | bs[0] & 0xff;
		i = i << 8 | bs[1] & 0xff;
		i = i << 8 | bs[2] & 0xff;
		i = i << 8 | bs[3] & 0xff;

		return i;
	}

	public static byte[] int2Byte(int i) {
		byte[] bs = new byte[4];
		bs[3] = (byte) (i & 0xff);
		bs[2] = (byte) (i >>> 8 & 0xff);
		bs[1] = (byte) (i >>> 16 & 0xff);
		bs[0] = (byte) (i >>> 24 & 0xff);

		return bs;
	}

	/**
	 * 长整型占8个字节
	 * 
	 * @param bs
	 *            8个字节
	 * @return
	 */
	public static long byte2Long(byte[] bs) {
		long i = 0;
		i = i | bs[0] & 0xff;
		i = i << 8 | bs[1] & 0xff;
		i = i << 8 | bs[2] & 0xff;
		i = i << 8 | bs[3] & 0xff;

		i = i << 8 | bs[4] & 0xff;
		i = i << 8 | bs[5] & 0xff;
		i = i << 8 | bs[6] & 0xff;
		i = i << 8 | bs[7] & 0xff;

		return i;
	}

	public static byte[] long2Byte(long i) {
		byte[] bs = new byte[8];
		bs[7] = (byte) (i & 0xff);
		bs[6] = (byte) (i >>> 8 & 0xff);
		bs[5] = (byte) (i >>> 16 & 0xff);
		bs[4] = (byte) (i >>> 24 & 0xff);

		bs[3] = (byte) (i >>> 32 & 0xff);
		bs[2] = (byte) (i >>> 40 & 0xff);
		bs[1] = (byte) (i >>> 48 & 0xff);
		bs[0] = (byte) (i >>> 56 & 0xff);

		return bs;
	}

	public static String format(String hex) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < hex.length(); i++) {
			result.append(hex.charAt(i));

			// 每隔2个字符添加一个空格
			if ((i + 1) % 2 == 0) {
				result.append(" ");
			}

			// 每隔32个字符添加一个回车
			if ((i + 1) % 32 == 0) {
				result.append("\n");
			}

		}

		return result.toString();
	}

	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		System.out.println(time);
		System.out.println(HexUtil.byte2Hex(HexUtil.long2Byte(time)));
		System.out.println(HexUtil.byte2Long(HexUtil.long2Byte(time)));

		String str = "1234567812345678123456781234567812345678";
		System.out.println(HexUtil.byte2Hex(str.getBytes()));
		System.out.println(HexUtil.format(HexUtil.byte2Hex(str.getBytes())));
	}

}
