/**
 * @(#)WalleAPPListenner.java 2011-2-25 Copyright 2011 it.kedacom.com, Inc. All
 *                            rights reserved.
 */

package org.comet4j.core.demo.talker;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.comet4j.core.CometContext;
import org.comet4j.core.CometEngine;
import org.comet4j.core.event.ConnectEvent;
import org.comet4j.core.event.DropEvent;

/**
 * 注册模块和事件侦听
 * @author jinghai.xiao@gmail.com
 * @date 2011-2-25
 */

public class AppInit implements ServletContextListener {

	/**
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */

	@SuppressWarnings("unchecked")
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		CometContext cc = CometContext.getInstance();
		CometEngine engine = cc.getEngine();
		// 注册模块
		cc.registAppModule(Constant.APP_MODULE_KEY);
		// 绑定事件侦听
		engine.addListener(ConnectEvent.class, new UpListener());
		engine.addListener(DropEvent.class, new DownListener());
	}

	/**
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO 该方法尚未实现

	}

}
