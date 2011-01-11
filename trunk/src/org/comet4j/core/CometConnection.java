package org.comet4j.core;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.comet4j.core.util.ExplorerUtil;
import org.comet4j.core.util.NetUtil;

public class CometConnection {
	private String id;
	
	private String clientIp;
	
	private String clientVersion;
	
	private String workStyle ;
	
	private String state = CometProtocol.STATE_ALIVE ;
	
	private long dyingTime = 0L; //最新活动时间
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	public CometConnection(HttpServletRequest anRequest,HttpServletResponse anResponse) {
		clientIp = NetUtil.getIpAddr(anRequest);
		clientVersion = anRequest.getParameter(CometProtocol.FLAG_CLIENTVERSION);
		if(CometContext.getInstance().getWorkStyle().equals(CometProtocol.WORKSTYLE_AUTO)){
			if(ExplorerUtil.canStreamingXHR(anRequest)){
				workStyle = CometProtocol.WORKSTYLE_STREAM;
			}else{
				workStyle = CometProtocol.WORKSTYLE_LLOOP;
			}
		}else{
			workStyle = CometContext.getInstance().getWorkStyle();
		}
		request = anRequest;
		response = anResponse;
		id = UUID.randomUUID().toString(); 
	}
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

	public String getWorkStyle() {
		return workStyle;
	}

	public void setWorkStyle(String workStyle) {
		this.workStyle = workStyle;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getDyingTime() {
		return dyingTime;
	}

	public void setDyingTime(long dyingTime) {
		this.dyingTime = dyingTime;
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
		this.request = null;
		this.response = null;
	}
	
}
