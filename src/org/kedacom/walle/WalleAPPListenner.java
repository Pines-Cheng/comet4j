/**
 * @(#)WalleAPPListenner.java 2011-2-25 Copyright 2011 it.kedacom.com, Inc. All
 *                            rights reserved.
 */

package org.kedacom.walle;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.comet4j.core.CometContext;
import org.comet4j.core.event.CometContextEvent;

/**
 * (用一句话描述类的主要功能)
 * @author xiaojinghai
 * @date 2011-2-25
 */

public class WalleAPPListenner implements ServletContextListener {

	/**
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		CometContext cct = CometContext.getInstance();
		cct.addListener(CometContextEvent.class, new CometContextListenner());
	}

	/**
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO 该方法尚未实现

	}

}
