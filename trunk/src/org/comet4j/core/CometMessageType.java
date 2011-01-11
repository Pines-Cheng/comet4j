package org.comet4j.core;

/**
 * 标识CometMessage的含义
 * 
 */
public class CometMessageType {
	/**
	 * 标识Data中是配置信息
	 */
	public static int CONFIG = 0;
	/**
	 * 标识Data中是连接ID
	 */
	public static int CONNECTION = 1; 
	/**
	 * 标识Data中是用户数据信息
	 */
	public static int DATA = 2; 
}
