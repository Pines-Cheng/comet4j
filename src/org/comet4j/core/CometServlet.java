package org.comet4j.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.CometEvent;
import org.apache.catalina.CometProcessor;

/**
 * 消息连接器，负责与客户端建立连接
 */
public class CometServlet extends HttpServlet implements CometProcessor {
	private static final long serialVersionUID = 1L;
	public CometServlet() {
		super();
	}
	/*
	 * @see
	 * org.apache.catalina.CometProcessor#event(org.apache.catalina.CometEvent)
	 */
	public void event(CometEvent event) throws IOException, ServletException {
		HttpServletRequest request = event.getHttpServletRequest();
		HttpServletResponse response = event.getHttpServletResponse();
		request.setAttribute("org.apache.tomcat.comet.timeout", CometContext.getInstance().getTimeout());
		
		this.doRequest(request, response, event);
		if (event.getEventType() == CometEvent.EventType.BEGIN) {
			String action = request.getParameter(CometProtocol.FLAG_ACTION);
			CometContext.getInstance().log("BEGIN:"+action);
			this.doBegin(request, response, event);
			if (CometProtocol.CMD_CONNECT.equals(action)) {
				CometContext.getInstance().log("BEGIN:CONNECT");
				CometContext.getInstance().getEngine().connect(request, response);
			}else if (CometProtocol.CMD_REVIVAL.equals(action)) {
				CometContext.getInstance().log("BEGIN:RECEIVE:"+action);
				CometContext.getInstance().getEngine().revival(request, response);
			}else if (CometProtocol.CMD_DROP.equals(action)) {
				CometContext.getInstance().log("BEGIN:DROP");
				CometContext.getInstance().getEngine().drop(request, response);
			}
		} else if (event.getEventType() == CometEvent.EventType.ERROR) {
			if (event.getEventSubType() == CometEvent.EventSubType.TIMEOUT) {
				CometContext.getInstance().log("BEGIN:TIMEOUT");
				CometContext.getInstance().getEngine().dying(request, response);
				
				/*
				//this.doTimeout(request, response, event);
				//event.close();
				response.setStatus(408);
				response.getWriter().close();
				//CometContext.getConnector().timeout(request, response);
				 */				
			}else{
				CometContext.getInstance().log("ERROR");
				this.doError(request, response, event);
				CometContext.getInstance().getEngine().drop(request, response);
			}
			
		} else if (event.getEventType() == CometEvent.EventType.END) {
			String action = event.getHttpServletRequest().getParameter(CometProtocol.FLAG_ACTION);
			CometContext.getInstance().log("END");
			this.doEnd(request, response, event);
			CometContext.getInstance().getEngine().dying(request, response);
			//response.getWriter().close();
			//event.getHttpServletResponse().getWriter().close();
			//CometContext.getEngine().drop(request, response);//end不代表连接断开
			
		} else if (event.getEventType() == CometEvent.EventType.READ) {
			CometContext.getInstance().log("READ");
			this.doRead(request, response, event);
		}

	}
	public void doRequest(HttpServletRequest request,HttpServletResponse response,CometEvent event) throws UnsupportedOperationException, IOException, ServletException{};
	public void doBegin(HttpServletRequest request,HttpServletResponse response,CometEvent event) throws UnsupportedOperationException, IOException, ServletException{};
	public void doEnd(HttpServletRequest request,HttpServletResponse response,CometEvent event) throws UnsupportedOperationException, IOException, ServletException{};
	public void doRead(HttpServletRequest request,HttpServletResponse response,CometEvent event) throws UnsupportedOperationException, IOException, ServletException{};
	public void doError(HttpServletRequest request,HttpServletResponse response,CometEvent event) throws UnsupportedOperationException, IOException, ServletException{};
	public void doTimeout(HttpServletRequest request,HttpServletResponse response,CometEvent event) throws UnsupportedOperationException, IOException, ServletException{};

	public void destroy() {
		super.destroy();
	}
	
}
