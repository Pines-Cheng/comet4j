package org.comet4j.core.demo.talker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.comet4j.core.CometConnection;
import org.comet4j.core.CometContext;
import org.comet4j.core.CometEngine;
import org.comet4j.core.util.JSONUtil;

/**
 * (用一句话描述类的主要功能)
 * 
 * @author jinghai.xiao@gmail.com
 * @date 2011-3-3
 */

public class WebServlet extends HttpServlet {

	/** serialVersionUID */
	private static final long serialVersionUID = -1311176251844328163L;
	private static final String CMD_FLAG = "cmd";
	private static final String RENAME_CMD = "rename";
	private static final String TALK_CMD = "talk";
	private static final String LIST_CMD = "list";
	private static final CometContext context = CometContext.getInstance();
	private static final CometEngine engine = context.getEngine();

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String cmd = request.getParameter(CMD_FLAG);
		// 改名
		if (RENAME_CMD.equals(cmd)) {
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			name = new String(request.getParameter("name")
					.getBytes("ISO8859-1"), "utf-8");
			AppStore.getInstance().put(id, name);
			RenameDTO dto = new RenameDTO(id, name);
			engine.sendToAll(Constant.APP_MODULE_KEY, dto);
		}
		// 发送信息
		if (TALK_CMD.equals(cmd)) {
			String from = request.getParameter("from");
			String to = request.getParameter("to");
			String text = request.getParameter("text");
			text = new String(request.getParameter("text")
					.getBytes("ISO8859-1"), "utf-8");
			TalkDTO dto = new TalkDTO(from, to, text);
			if ("".equals(to)) {
				engine.sendToAll(Constant.APP_MODULE_KEY, dto);
			} else {
				CometConnection toConn = engine.getConnection(to);
				CometConnection fromConn = engine.getConnection(from);
				engine.sendTo(Constant.APP_MODULE_KEY, toConn, dto);
				engine.sendTo(Constant.APP_MODULE_KEY, fromConn, dto);
			}

		}
		// 在线列表
		if (LIST_CMD.equals(cmd)) {
			List<UserVO> userList = new ArrayList<UserVO>();
			/*
			 * List<CometConnection> connList = engine.getConnections(); if
			 * (connList != null && !connList.isEmpty()) { for (CometConnection
			 * conn : connList) { UserVO user = new UserVO(conn.getId(),
			 * conn.getId()); userList.add(user); } }
			 */
			Map<String, String> map = AppStore.getInstance().getMap();
			Iterator iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String id = (String) entry.getKey();
				String name = (String) entry.getValue();
				userList.add(new UserVO(id, name));
			}

			String json = JSONUtil.convertToJson(userList);
			response.getWriter().print(json);
		}
		// super.service(request, response);
	}
}
