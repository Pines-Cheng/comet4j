package org.kedacom.comet.event;

import javax.servlet.http.HttpServletRequest;

import org.kedacom.comet.CometEngine;
import org.kedacom.event.Event;


public class BeforeDropEvent extends Event<CometEngine> {
	private HttpServletRequest request;
	public BeforeDropEvent(CometEngine target,HttpServletRequest aRequest) {
		super(target);
		request = aRequest;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public void destroy() {
		super.destroy();
		request = null;
	}
}
