/*
 * Comet4J Copyright(c) 2011, http://code.google.com/p/comet4j/ This code is
 * licensed under BSD license. Use it as you wish, but keep this copyright
 * intact.
 */
package org.comet4j.core;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 负责框架的启动
 * @author jinghai.xiao@gmail.com
 * @date 2011-2-25
 */

public class CometAppListener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener,
		ServletRequestListener {

	// ServletContextListener
	public void contextInitialized(ServletContextEvent event) {
		CometContext cct = CometContext.getInstance();
		cct.init(event);
	}

	public void contextDestroyed(ServletContextEvent event) {
		CometContext.getInstance().destroy();
	}

	// HttpSessionListener
	public void sessionCreated(HttpSessionEvent event) {

	}

	public void sessionDestroyed(HttpSessionEvent event) {

	}

	// HttpSessionAttributeListener
	public void attributeAdded(HttpSessionBindingEvent event) {

	}

	public void attributeRemoved(HttpSessionBindingEvent event) {

	}

	public void attributeReplaced(HttpSessionBindingEvent event) {

	}

	// ServletRequestListener
	public void requestInitialized(ServletRequestEvent event) {

	}

	public void requestDestroyed(ServletRequestEvent event) {

	}

}
