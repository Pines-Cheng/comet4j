package org.comet4j.core.demo.talker;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.comet4j.core.CometConnection;
import org.comet4j.core.demo.talker.dto.UpDTO;
import org.comet4j.core.event.ConnectEvent;
import org.comet4j.core.listener.ConnectListener;

/**
 * 上线侦听
 * @author jinghai.xiao@gmail.com
 * @date 2011-3-3
 */

public class UpListener extends ConnectListener {

	/*
	 * (non-Jsdoc)
	 * @see org.comet4j.event.Listener#handleEvent(org.comet4j.event.Event)
	 */
	@Override
	public boolean handleEvent(ConnectEvent anEvent) {
		CometConnection conn = anEvent.getConn();
		HttpServletRequest request = conn.getRequest();
		String userName = getCookieValue(request.getCookies(), "userName", "");
		try {
			userName = URLDecoder.decode(userName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO 尚未处理异常
			e.printStackTrace();
		}
		UpDTO dto = new UpDTO(conn.getId(), userName);
		AppStore.getInstance().put(conn.getId(), userName);
		anEvent.getTarget().sendToAll(Constant.APP_MODULE_KEY, dto);
		return true;
	}

	public String getCookieValue(Cookie[] cookies, String cookieName, String defaultValue) {
		String result = defaultValue;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookieName.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return result;
	}

}
