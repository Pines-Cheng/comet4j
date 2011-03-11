package org.comet4j.core.demo.talker.dto;

import org.comet4j.core.demo.talker.Constant;

public class TalkDTO {

	private String type;
	private String from;
	private String to;
	private String text;

	public TalkDTO(String from, String to, String text) {
		this.type = Constant.TALK;
		this.from = from;
		this.to = to;
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
