package org.kedacom.walle;

import org.kedacom.comet.event.CometContextEvent;
import org.kedacom.comet.listener.CometContextListener;

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
