package com.cn.thinkx.pms.mina;

public interface AppObjectProcessor {
	
	public Object msg2obj(byte[] messageBytes) throws Exception;
	
	public byte[] obj2msg(Object messageObject) throws Exception;
}
