/*
 * Comet4J Copyright(c) 2011, http://code.google.com/p/comet4j/ This code is
 * licensed under BSD license. Use it as you wish, but keep this copyright
 * intact.
 */
package org.comet4j.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.comet.CometEvent;
import org.apache.catalina.comet.CometProcessor;

/**
 * 连接前端Servlet，负责处理连接请求，并转交给引擎处理。
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
		event.setTimeout(CometContext.getInstance().getTimeout());
		if (event.getEventType() == CometEvent.EventType.BEGIN) {
			String action = request.getParameter(CometProtocol.FLAG_ACTION);

			if (CometProtocol.CMD_CONNECT.equals(action)) {

				CometContext.getInstance().getEngine().connect(request, response);
			} else if (CometProtocol.CMD_REVIVAL.equals(action)) {

				CometContext.getInstance().getEngine().revival(request, response);

			} else if (CometProtocol.CMD_DROP.equals(action)) {
				CometContext.getInstance().getEngine().drop(request, response);
			}
		} else if (event.getEventType() == CometEvent.EventType.ERROR) {
			if (event.getEventSubType() == CometEvent.EventSubType.TIMEOUT) {
				CometContext.getInstance().getEngine().dying(request, response);
			} else {

				CometContext.getInstance().getEngine().drop(request, response);
			}

		} else if (event.getEventType() == CometEvent.EventType.END) {

			CometContext.getInstance().getEngine().dying(request, response);
		} else if (event.getEventType() == CometEvent.EventType.READ) {

		}

	}

	@Override
	public void destroy() {
		super.destroy();
	}

}
