package com.sinotrans.sh.model;

import java.io.Serializable;
import java.sql.Date;

public class Order implements Serializable{

	private Long cid;
	private Integer nid;
	public Integer getNid() {
		return nid;
	}
	public void setNid(Integer nid) {
		this.nid = nid;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	//项目名称
	private String projectName;
	//司机名称
	private String driverName;
	//车牌号
	private String carNum;
	//订单编号
	private String orderCode;
	//收货地址
	private String receiveAddress;
	//收货方
	private String receiveName;
	//数量
	private Double num;
	//重量
	private Double weight;
	//体积
	private Double volume;
	//备注
	private String remark;
	
	//配送日期
	private Date distributionDate;
	//标记（有效删除等）
	private String flag;
	
	
	public Date getDistributionDate() {
		return distributionDate;
	}
	public void setDistributionDate(Date distributionDate) {
		this.distributionDate = distributionDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getReceiveAddress() {
		return receiveAddress;
	}
	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}
	public String getReceiveName() {
		return receiveName;
	}
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	public Double getNum() {
		return num;
	}
	public void setNum(Double num) {
		this.num = num;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Double getVolume() {
		return volume;
	}
	public void setVolume(Double volume) {
		this.volume = volume;
	}


}
