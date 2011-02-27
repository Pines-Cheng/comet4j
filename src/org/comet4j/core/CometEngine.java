package org.comet4j.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.comet4j.core.dto.ConnectionDTO;
import org.comet4j.core.event.BeforeConnectEvent;
import org.comet4j.core.event.BeforeDropEvent;
import org.comet4j.core.event.BeforeRemoveEvent;
import org.comet4j.core.event.ConnectedEvent;
import org.comet4j.core.event.DroppedEvent;
import org.comet4j.core.event.DyingEvent;
import org.comet4j.core.event.ErrorEvent;
import org.comet4j.core.event.MessageEvent;
import org.comet4j.core.event.RemovedEvent;
import org.comet4j.core.event.RevivalEvent;
import org.comet4j.core.exception.CometException;
import org.comet4j.event.Observable;

@SuppressWarnings("unchecked")
public class CometEngine extends Observable {

	private CometConnector ct;
	private CometSender sender;

	public CometEngine() {
		this.addEvent(BeforeConnectEvent.class);
		this.addEvent(ConnectedEvent.class);
		this.addEvent(BeforeDropEvent.class);
		this.addEvent(DroppedEvent.class);
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
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	void connect(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// CometContext.getInstance().log("connect");
		CometConnection conn = new CometConnection(request, response);
		BeforeConnectEvent be = new BeforeConnectEvent(this, request, response);
		if (!this.fireEvent(be)) {
			return;
		}
		ct.addConnection(conn);
		ConnectedEvent e = new ConnectedEvent(this, conn);
		this.fireEvent(e);
		ConnectionDTO cdto = new ConnectionDTO(conn.getId(), CometContext
				.getInstance().getAppModules());
		/*
		 * CometMessage msg = new CometMessage();
		 * msg.setState(CometMessageType.CONNECTION);
		 * msg.setStateText(Language.getConnectSuccess());
		 * msg.setData(conn.getId());
		 */
		sendTo(Constant.Comet4JAppModuleKey, conn, cdto);
		sendCacheMessage(conn);
	}

	private void sendCacheMessage(CometConnection conn) {
		List<CometMessage> list = sender.getCacheMessage(conn);
		List<Object> dataList = new ArrayList<Object>();
		if (list != null && !list.isEmpty()) {
			for (CometMessage msg : list) {
				dataList.add(msg.getData());
			}
		}
		if (!dataList.isEmpty()) {
			sendTo(Constant.Comet4JAppModuleKey, conn, dataList);
		}
	}

	void dying(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// CometContext.getInstance().log("dying");
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

	void revival(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// CometContext.getInstance().log("revival");
		String cId = getConnectionId(request);
		if (cId == null) {
			drop(request, response);
			throw new CometException("无法复活，断开连接。");
		}
		CometConnection conn = ct.getConnection(cId);
		if (conn != null /* && CometProtocol.STATE_DYING.equals(conn.getState()) */) {
			conn.setRequest(request);
			conn.setResponse(response);
			conn.setDyingTime(System.currentTimeMillis());
			conn.setState(CometProtocol.STATE_ALIVE);
			RevivalEvent e = new RevivalEvent(this, conn);
			this.fireEvent(e);
			sendCacheMessage(conn);
		} else {
			drop(request, response);
			throw new CometException("非正常复活，断开连接。conn=" + conn);
		}

		// if (conn == null) {
		// throw new CometException("无法复活，错误的连接ID");
		// } else if (conn.getResponse() != null) {
		// throw new CometException("无法复活，响应对象已存在");
		// } else {
		// conn.setRequest(request);
		// conn.setResponse(response);
		// conn.setDyingTime(System.currentTimeMillis());
		// conn.setState(CometProtocol.STATE_ALIVE);
		// RevivalEvent e = new RevivalEvent(this, conn);
		// this.fireEvent(e);
		// sendCacheMessage(conn);
		// }
	}

	public void drop(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// CometContext.getInstance().log("drop");
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
		DroppedEvent e = new DroppedEvent(this, conn);
		this.fireEvent(e);
	}

	void remove(CometConnection aConn) {
		// CometContext.getInstance().log("remove");
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
		RemovedEvent e = new RemovedEvent(this, aConn);
		this.fireEvent(e);
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

	public void sendTo(String appMouldKey, List<CometConnection> list,
			Object data) {
		for (CometConnection c : list) {
			sendTo(appMouldKey, c, data);
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
