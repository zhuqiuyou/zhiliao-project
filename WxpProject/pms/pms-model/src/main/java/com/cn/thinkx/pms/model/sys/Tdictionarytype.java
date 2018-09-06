package com.cn.thinkx.pms.model.sys;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.cn.thinkx.pms.model.base.IdEntity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotBlank;


@Entity
@Table(name = "tb_web_dictionarytype", schema = "")
@SequenceGenerator(name="webDictionarytypeSeq",sequenceName="web_dictionarytype_seq")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Tdictionarytype extends IdEntity implements java.io.Serializable{
	
	private String code;
	private String name;
	private Integer seq;
	private String description;
	private Tdictionarytype dictionarytype;
	
	public Tdictionarytype(){
		
	}
	
	public Tdictionarytype(String code, String name, Integer seq,
			String description) {
		super();
		this.code = code;
		this.name = name;
		this.seq = seq;
		this.description = description;
	}

	@NotBlank
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pid")
	public Tdictionarytype getDictionarytype() {
		return dictionarytype;
	}

	public void setDictionarytype(Tdictionarytype dictionarytype) {
		this.dictionarytype = dictionarytype;
	}


}
