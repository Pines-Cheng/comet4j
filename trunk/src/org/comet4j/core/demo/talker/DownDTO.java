package org.comet4j.core.demo.talker;

public class DownDTO {

	private String type;
	private String id;

	public DownDTO(String id) {
		this.type = Constant.DOWN;
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
