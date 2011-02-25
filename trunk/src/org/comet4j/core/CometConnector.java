package org.comet4j.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.comet4j.event.Observable;

/**
 * 负责对连接进行管理 增加、删除、获取等
 */
@SuppressWarnings("unchecked")
public class CometConnector extends Observable {

	private final long timespan;
	private final long frequency;
	private boolean init = false;
	private Thread cleanner;

	private List<CometConnection> connections = Collections.synchronizedList(new ArrayList<CometConnection>());

	// private List<Connection> connections = new
	// Vector<Connection>();//线程安全,网上资料介绍synchronizedList更快
	public CometConnector(long aTimespan, long aFrequency) {
		init = true;
		frequency = aFrequency;
		timespan = aTimespan;
		cleanner = new Thread(new CacheCleaner(), "CometConnectorCleaner Thread");
		cleanner.setDaemon(true);
		cleanner.start();
	}

	CometConnection getConnection(HttpServletRequest request) {
		for (int i = 0; i < connections.size(); i++) {
			CometConnection conn = connections.get(i);
			if (conn.getRequest() == request) {
				return conn;
			}
		}
		return null;
	}

	CometConnection getConnection(String id) {
		for (int i = 0; i < connections.size(); i++) {
			CometConnection conn = connections.get(i);
			if (conn.getId().equals(id)) {
				return conn;
			}
		}
		return null;
	}

	void addConnection(CometConnection connection) {
		connections.add(connection);
	}

	void removeConnection(CometConnection connection) {
		connections.remove(connection);
		;
	}

	void removeConnection(String id) {
		for (CometConnection c : connections) {
			if (c.getId().equals(id)) {
				connections.remove(c);
				break;
			}
		}
	}

	boolean contains(String anId) {
		boolean result = false;
		for (CometConnection c : connections) {
			if (c.getId().equals(anId)) {
				result = true;
				break;
			}
		}
		return result;
	}

	boolean contains(CometConnection conn) {
		return connections.contains(conn);
	}

	List<CometConnection> getConnections() {
		return connections;
	}

	// ----------定时清理过期连接---------------
	class CacheCleaner implements Runnable {

		private final List<CometConnection> toDeleteList = new ArrayList<CometConnection>();

		public void run() {
			while (init) {
				try {
					Thread.sleep(frequency);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				checkExpires();
			}
		}

		// 过期检查
		private void checkExpires() {
			CometEngine engine = CometContext.getInstance().getEngine();
			CometContext.getInstance().log("连接数量：" + connections.size());
			for (CometConnection c : connections) {
				long expireMillis = c.getDyingTime() + timespan;

				if (CometProtocol.STATE_DYING.equals(c.getState()) && expireMillis < System.currentTimeMillis()) {
					// 加入另一列表为了避免ConcurrentModificationException
					toDeleteList.add(c);
				}
			}

			if (!toDeleteList.isEmpty()) {
				for (CometConnection c : toDeleteList) {
					engine.remove(c);
				}
				toDeleteList.clear();
			}

			/*
			 * if(CometContext.getInstance().isDebug()){
			 * engine.sendTo(connections, "Test Data"); }
			 */

		}
	}

	@Override
	public void destroy() {
		init = false;
		cleanner = null;
		connections.clear();
		connections = null;
	}
}
