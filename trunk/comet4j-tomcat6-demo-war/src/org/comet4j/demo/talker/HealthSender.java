/*
 * Comet4J Copyright(c) 2011, http://code.google.com/p/comet4j/ This code is
 * licensed under BSD license. Use it as you wish, but keep this copyright
 * intact.
 */
package org.comet4j.demo.talker;

import org.comet4j.core.CometContext;
import org.comet4j.core.CometEngine;
import org.comet4j.demo.talker.dto.HealthDTO;

/**
 * 系统健康信息发送器
 * @author xiaojinghai
 * @date 2011-4-7
 */

public class HealthSender implements Runnable {

	private static final CometEngine engine = CometContext.getInstance().getEngine();
	private static final HealthDTO healthDto = new HealthDTO();
	private static final long startup = System.currentTimeMillis();

	@Override
	public void run() {

		while (true) {
			try {
				Thread.sleep(5000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			long totalMemory = Runtime.getRuntime().totalMemory();
			long freeMemory = Runtime.getRuntime().freeMemory();
			long maxMemory = Runtime.getRuntime().maxMemory();
			long usedMemory = totalMemory - freeMemory;
			Integer connectorCount = engine.getConnections().size();
			healthDto.setConnectorCount(connectorCount.toString());
			healthDto.setFreeMemory(freeMemory);
			healthDto.setMaxMemory(maxMemory);
			healthDto.setTotalMemory(totalMemory);
			healthDto.setUsedMemory(usedMemory);
			healthDto.setStartup(startup);
			engine.sendToAll(Constant.APP_CHANNEL, healthDto);

		}

	}
}
