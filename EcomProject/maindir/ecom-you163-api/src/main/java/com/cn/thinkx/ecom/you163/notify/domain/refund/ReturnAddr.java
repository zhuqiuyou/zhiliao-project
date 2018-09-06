package com.cn.thinkx.ecom.you163.notify.domain.refund;


/**
 * 退货地址详情信息
 * @author zhuqiuyou
 *
 */
public class ReturnAddr implements java.io.Serializable {
	
	private static final long serialVersionUID = 6075486968887544231L;
	
	private String provinceName;//	 		省份名称 
	private String 	cityName;// 	 	城市名称
	private String	districtName;// 	 区域名称	
	private String	address;// 	 	具体街道地址
	private String 	fullAddress;// 	 	完整地址
	private String	zipCode;// 	 	邮政编码
	private String 	name;// 	 	收件人姓名 	
	private String 	mobile;// 	收件人手机
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFullAddress() {
		return fullAddress;
	}
	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
