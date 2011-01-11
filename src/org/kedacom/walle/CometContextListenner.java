package org.kedacom.walle;

import org.comet4j.core.event.CometContextEvent;
import org.comet4j.core.listener.CometContextListener;

public class CometContextListenner extends CometContextListener {

	@Override
	public boolean onDestroyed(CometContextEvent event) {
		System.out.println("cometContextDestroyed已执行！");
		return true;
	}

	@Override
	public boolean onInitialized(CometContextEvent event) {
		System.out.println("cometContextInitialized已执行！");
		return true;
	}

}
