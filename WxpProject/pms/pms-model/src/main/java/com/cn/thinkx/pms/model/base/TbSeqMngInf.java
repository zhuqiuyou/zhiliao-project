package com.cn.thinkx.pms.model.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TB_SEQ_MNG_INF")
public class TbSeqMngInf {
	private String seqId;
	private String tableName;
	private String curValue;
	private String maxValue;
	private String minValue;
	private String disp;
	private String remarks;

	@Id
	@Column(name = "seq_id", unique = true, nullable = false, length = 22)
	public String getSeqId() {
		return this.seqId;
	}

	public void setSeqId(String seqId) {
		this.seqId = seqId;
	}

	@Column(name = "table_name", length = 64)
	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Column(name = "cur_value", length = 20)
	public String getCurValue() {
		return this.curValue;
	}

	public void setCurValue(String curValue) {
		this.curValue = curValue;
	}

	@Column(name = "max_value", length = 20)
	public String getMaxValue() {
		return this.maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	@Column(name = "min_value", length = 20)
	public String getMinValue() {
		return this.minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	@Column(name = "disp", length = 128)
	public String getDisp() {
		return this.disp;
	}

	public void setDisp(String disp) {
		this.disp = disp;
	}

	@Column(name = "remarks", length = 256)
	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}