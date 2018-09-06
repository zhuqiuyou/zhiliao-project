package com.cn.thinkx.ecom.cash.wxtrans.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.beans.CtrlSystem;

@Mapper
public interface CtrlSystemMapper {

	/**
	 * 得到唯一日切信息
	 * 
	 * @return
	 */
	CtrlSystem getCtrlSystem();
}
