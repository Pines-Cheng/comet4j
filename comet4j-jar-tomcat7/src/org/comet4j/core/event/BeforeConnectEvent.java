package org.comet4j.core.event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.comet4j.core.CometEngine;
import org.comet4j.event.Event;


public class BeforeConnectEvent extends Event<CometEngine> {
	private HttpServletRequest request;
	private HttpServletResponse response;
	public BeforeConnectEvent(CometEngine target,HttpServletRequest anRequest,HttpServletResponse anResponse) {
		super(target);
		request = anRequest;
		response = anResponse;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public void destroy() {
		super.destroy();
		request = null;
		response = null;
	}
}
