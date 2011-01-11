package org.kedacom.walle;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.CometEvent;
import org.comet4j.core.CometServlet;

/**
 * 消息连接器，负责与客户端建立连接
 */
public class WallEServlet extends CometServlet  {
	private static final long serialVersionUID = -5792842970197490460L;

	@Override
	public void doBegin(HttpServletRequest request,
			HttpServletResponse response, CometEvent event)
			throws UnsupportedOperationException, IOException, ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doEnd(HttpServletRequest request, HttpServletResponse response,
			CometEvent event) throws UnsupportedOperationException,
			IOException, ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doError(HttpServletRequest request,
			HttpServletResponse response, CometEvent event)
			throws UnsupportedOperationException, IOException, ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doRead(HttpServletRequest request,
			HttpServletResponse response, CometEvent event)
			throws UnsupportedOperationException, IOException, ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doRequest(HttpServletRequest request,
			HttpServletResponse response, CometEvent event)
			throws UnsupportedOperationException, IOException, ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doTimeout(HttpServletRequest request,
			HttpServletResponse response, CometEvent event)
			throws UnsupportedOperationException, IOException, ServletException {
		// TODO Auto-generated method stub
		
	}

}
