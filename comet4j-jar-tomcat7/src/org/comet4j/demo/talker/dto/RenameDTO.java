package org.comet4j.demo.talker.dto;

import org.comet4j.demo.talker.Constant;

public class RenameDTO {

	private String type;
	private String id;
	private String name;

	public RenameDTO(String id, String name) {
		this.type = Constant.RENAME;
		this.id = id;
		this.name = name;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
