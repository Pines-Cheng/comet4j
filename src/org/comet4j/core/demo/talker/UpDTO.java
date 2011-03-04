package org.comet4j.core.demo.talker;

public class UpDTO {

	private String type;
	private String id;
	private String name;

	public UpDTO(String id, String name) {
		this.type = Constant.UP;
		this.id = id;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
