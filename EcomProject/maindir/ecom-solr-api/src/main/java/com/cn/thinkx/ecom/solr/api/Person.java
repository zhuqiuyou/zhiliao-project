package com.cn.thinkx.ecom.solr.api;

import org.apache.solr.client.solrj.beans.Field;

public class Person implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 320133024604044265L;
	
	 @Field
	private String id;
	
	 @Field
	private String name;
	
	 @Field
	private String description;
	
	public Person() {
		super();
	}

	public Person(String id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Person [Id=" + this.id + ", name=" + name + ", description=" + description + "]";
	}
	
}
