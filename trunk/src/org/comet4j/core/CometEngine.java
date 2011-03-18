package org.comet4j.core;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.comet4j.core.dto.ConnectionDTO;
import org.comet4j.core.event.BeforeConnectEvent;
import org.comet4j.core.event.BeforeDropEvent;
import org.comet4j.core.event.BeforeRemoveEvent;
import org.comet4j.core.event.ConnectEvent;
import org.comet4j.core.event.DropEvent;
import org.comet4j.core.event.DyingEvent;
import org.comet4j.core.event.ErrorEvent;
import org.comet4j.core.event.MessageEvent;
import org.comet4j.core.event.RemovedEvent;
import org.comet4j.core.event.RevivalEvent;
import org.comet4j.event.Observable;

@SuppressWarnings({
		"unchecked", "rawtypes"
})
public class CometEngine extends Observable {

	private CometConnector ct;
	private CometSender sender;

	public CometEngine() {
		this.addEvent(BeforeConnectEvent.class);
		this.addEvent(ConnectEvent.class);
		this.addEvent(BeforeDropEvent.class);
		this.addEvent(DropEvent.class);
		this.addEvent(DyingEvent.class);
		this.addEvent(RevivalEvent.class);
		this.addEvent(MessageEvent.class);
		this.addEvent(ErrorEvent.class);// TODO:
		CometContext cc = CometContext.getInstance();
		sender = new CometSender(cc.getCacheExpires(), cc.getCacheFrequency());
		ct = new CometConnector(cc.getConnExpires(), cc.getConnFrequency());
	}

	/*
	 * public ServerInfoDTO getServerInfo(HttpServletRequest request,
	 * HttpServletResponse response) throws IOException{
	 * //TODO：应先得到协议(metaData)再得到配置值 response.getWriter().close(); }
	 */
	/**
	 * 建立用户连接
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	void connect(HttpServletRequest request, HttpServletResponse response) throws IOException {
		CometConnection conn = new CometConnection(request, response);
		BeforeConnectEvent be = new BeforeConnectEvent(this, request, response);
		if (!this.fireEvent(be)) {
			return;
		}
		ct.addConnection(conn);
		CometContext cc = CometContext.getInstance();
		ConnectionDTO cdto = new ConnectionDTO(conn.getId(), conn.getWorkStyle(), cc.getAppModules(), cc.getTimeout());
		sendTo(CometProtocol.SYS_MODULE_KEY, conn, cdto);
		dying(request, response);
		sender.sendCacheMessage(conn);
		ConnectEvent e = new ConnectEvent(this, conn);
		this.fireEvent(e);
	}

	void dying(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setStatus(CometProtocol.HTTPSTATUS_TIMEOUT);
		CometConnection conn = ct.getConnection(request);
		try {
			conn.getResponse().getWriter().close();
		} catch (Exception exc) {
			try {
				response.getWriter().close();
			} catch (Exception excp) {
				excp.printStackTrace();
			}

		}
		if (conn != null) {
			conn.setState(CometProtocol.STATE_DYING);
			conn.setResponse(null);
			conn.setDyingTime(System.currentTimeMillis());
			DyingEvent e = new DyingEvent(this, conn);
			this.fireEvent(e);
		}
	}

	void revival(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String cId = getConnectionId(request);
		if (cId == null) {
			drop(request, response);
			// throw new CometException("无法复活，断开连接。");
		}
		CometConnection conn = ct.getConnection(cId);
		if (conn != null /* && CometProtocol.STATE_DYING.equals(conn.getState()) */) {
			conn.setRequest(request);
			conn.setResponse(response);
			conn.setDyingTime(System.currentTimeMillis());
			conn.setState(CometProtocol.STATE_ALIVE);
			RevivalEvent e = new RevivalEvent(this, conn);
			this.fireEvent(e);
			sender.sendCacheMessage(conn);
		} else {
			drop(request, response);
			// throw new CometException("非正常复活，断开连接。conn=" + conn);
		}
	}

	public void drop(HttpServletRequest request, HttpServletResponse response) throws IOException {
		BeforeDropEvent be = new BeforeDropEvent(this, request);
		if (!this.fireEvent(be)) {
			return;
		}

		String cId = getConnectionId(request);
		CometConnection conn = null;
		if (cId != null) {
			conn = ct.getConnection(cId);
		} else {
			conn = ct.getConnection(request);
		}
		remove(conn);
		response.setStatus(CometProtocol.HTTPSTATUS_ERROR);
		response.getWriter().close();
		// DroppedEvent e = new DroppedEvent(this, conn);
		// this.fireEvent(e);
	}

	void remove(CometConnection aConn) {

		BeforeRemoveEvent be = new BeforeRemoveEvent(this, aConn);
		if (!this.fireEvent(be)) {
			return;
		}
		ct.removeConnection(aConn);
		try {
			aConn.getResponse().getWriter().close();
		} catch (Exception exc) {
			// 连接有可能是dying，此时getResponse为空是正常的，这里仅保证对有效的Response做出回应
		}
		RemovedEvent re = new RemovedEvent(this, aConn);
		this.fireEvent(re);
		DropEvent de = new DropEvent(this, aConn);
		this.fireEvent(de);
		// CometContext.getInstance().log("删除连接:" + aConn.getId());

	}

	public CometConnection getConnection(String id) {
		return ct.getConnection(id);
	}

	public CometConnection getConnection(HttpServletRequest request) {
		String cId = getConnectionId(request);
		CometConnection conn = null;
		if (cId != null) {
			conn = ct.getConnection(cId);
		} else {
			conn = ct.getConnection(request);
		}
		return conn;
	}

	public List<CometConnection> getConnections() {
		return ct.getConnections();
	}

	public void sendTo(String appMouldKey, CometConnection c, Object data) {
		CometMessage msg = new CometMessage(data, appMouldKey);
		sender.sendTo(c, msg);
		MessageEvent e = new MessageEvent(this, c, msg);
		this.fireEvent(e);
	}

	public void sendTo(String appMouldKey, CometConnection c, List<Object> data) {
		for (Object o : data) {
			sendTo(appMouldKey, c, o);
		}
	}

	public void sendTo(String appMouldKey, List<CometConnection> list, Object data) {
		if (list.isEmpty()) {
			return;
		}
		for (CometConnection c : list) {
			sendTo(appMouldKey, c, data);
		}
	}

	// 发送给所有连接
	public void sendToAll(String appMouldKey, Object data) {
		List<CometConnection> list = this.getConnections();
		if (list == null) {
			return;
		}
		synchronized (list) {
			for (CometConnection c : list) {
				sendTo(appMouldKey, c, data);
			}
		}
	}

	public String getConnectionId(HttpServletRequest request) {
		String id = request.getParameter(CometProtocol.FLAG_ID);
		if (id == null || "".equals(id)) {
			id = null;
		}
		return id;
	}

	@Override
	public void destroy() {
		super.destroy();
		ct.destroy();
		sender.destroy();
		ct = null;
		sender = null;

	}

}
