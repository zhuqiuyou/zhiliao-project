package com.cn.thinkx.pms.base.utils;

import java.nio.charset.Charset;

public class DeEnCode {
	private static final String key0 = "FECOI()*&<MNCXZPKL";
	private static final Charset charset = Charset.forName("UTF-8");
	private static byte[] keyBytes = key0.getBytes(charset);

	public static String encode(String enc) {
		byte[] b = enc.getBytes(charset);
		for (int i = 0, size = b.length; i < size; i++) {
			for (byte keyBytes0 : keyBytes) {
				b[i] = (byte) (b[i] ^ keyBytes0);
			}
		}
		return new String(b);
	}

	public static String decode(String dec) {
		byte[] e = dec.getBytes(charset);
		byte[] dee = e;
		for (int i = 0, size = e.length; i < size; i++) {
			for (byte keyBytes0 : keyBytes) {
				e[i] = (byte) (dee[i] ^ keyBytes0);
			}
		}
		return new String(e);
	}

	public static void main(String[] args) {
		String s = "mchntCode=120010100000001&shopCode=00000012";
		String enc = encode(s);
		String dec = decode(enc);
		System.out.println("加密后：" + enc);
		System.out.println("解密后：" + dec);
	}
}
