/*
 * Comet4J Copyright(c) 2011, http://code.google.com/p/comet4j/ This code is
 * licensed under BSD license. Use it as you wish, but keep this copyright
 * intact.
 */
package org.comet4j.demo.talker.dto;

/**
 * 系统健康传输对象
 * @author jinghai.xiao@gmail.com
 * @date 2011-4-7
 */

public class SysHealthDTO {

	private String workStyle;
	private String userCount;
	private String connectCount;
	private String cacheCount;
	private String cup;
	private String usedMem;
	private String freeMem;
	private String maxMem;

	public String getWorkStyle() {
		return workStyle;
	}

	public void setWorkStyle(String workStyle) {
		this.workStyle = workStyle;
	}

	public String getUserCount() {
		return userCount;
	}

	public void setUserCount(String userCount) {
		this.userCount = userCount;
	}

	public String getConnectCount() {
		return connectCount;
	}

	public void setConnectCount(String connectCount) {
		this.connectCount = connectCount;
	}

	public String getCacheCount() {
		return cacheCount;
	}

	public void setCacheCount(String cacheCount) {
		this.cacheCount = cacheCount;
	}

	public String getCup() {
		return cup;
	}

	public void setCup(String cup) {
		this.cup = cup;
	}

	public String getUsedMem() {
		return usedMem;
	}

	public void setUsedMem(String usedMem) {
		this.usedMem = usedMem;
	}

	public String getFreeMem() {
		return freeMem;
	}

	public void setFreeMem(String freeMem) {
		this.freeMem = freeMem;
	}

	public String getMaxMem() {
		return maxMem;
	}

	public void setMaxMem(String maxMem) {
		this.maxMem = maxMem;
	}

}
