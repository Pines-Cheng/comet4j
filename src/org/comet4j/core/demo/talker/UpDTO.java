package org.comet4j.core.demo.talker;

public class UpDTO {

	private String type;
	private String id;

	public UpDTO(String id) {
		this.type = Constant.UP;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
