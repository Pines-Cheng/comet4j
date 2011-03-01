package org.comet4j.core.demo.eventmonitor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.comet4j.core.CometContext;

/**
 * 应用初始化
 * @author jinghai.xiao@gmail.com
 * @date 2011-2-25
 */

public class AppInit implements ServletContextListener {

	// ServletContextListener
	public void contextInitialized(ServletContextEvent event) {
		CometContext cc = CometContext.getInstance();
		cc.registAppModule(Constant.AppModuleKey);
	}

	public void contextDestroyed(ServletContextEvent event) {

	}

}
