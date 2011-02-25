package org.comet4j.core;

import org.comet4j.core.util.JSONUtil;

public class CometProtocol {

	/**
	 * 客户端连接器版本标识
	 */
	public static final String FLAG_CLIENTVERSION = "cv";
	/**
	 * 客户端连接ID标识
	 */
	public static final String FLAG_ID = "cid";
	/**
	 * 客户端连接器动作标识
	 */
	public static final String FLAG_ACTION = "cat";
	/**
	 * 数据开始标识
	 */
	public static final String FLAG_DataStart = "<";
	/**
	 * 数据结束结识
	 */
	public static final String FLAG_DataEnd = ">";
	/**
	 * 连接动作
	 */
	public static final String CMD_CONNECT = "conn";
	/**
	 * 复活动作
	 */
	public static final String CMD_REVIVAL = "revival";
	/**
	 * 断开连接
	 */
	public static final String CMD_DROP = "drop";
	/**
	 * 得到适配元数据
	 */
	public static final String CMD_ADAPTMETADATA = "AM";
	/**
	 * 长轮询工作状态
	 */
	public static final String WORKSTYLE_LLOOP = "lloop";
	/**
	 * 长连接工作状态(HTTP Streaming)
	 */
	public static final String WORKSTYLE_STREAM = "stream";
	/**
	 * 智能工作状态
	 */
	public static final String WORKSTYLE_AUTO = "auto";
	/**
	 * 连接可用状态
	 */
	public static final String STATE_ALIVE = "alive";
	/**
	 * 连接濒死状态
	 */
	public static final String STATE_DYING = "dying";
	/**
	 * HTTP超时状态值
	 */
	public static final int HTTPSTATUS_TIMEOUT = 408;
	/**
	 * HTTP异常状态
	 */
	public static final int HTTPSTATUS_ERROR = 500;
	/**
	 * 语言配置
	 */
	public static final String CONFIG_LANGUAGE = "Comet.Language";
	/**
	 * 工作方式配置
	 */
	public static final String CONFIG_WORKSTYLE = "Comet.WorkStyle";
	/**
	 * 引擎配置
	 */
	public static final String CONFIG_ENGINE = "Comet.CometEngine";
	/**
	 * 请求超时时间
	 */
	public static final String CONFIG_TIMEOUT = "Comet.Timeout";
	/**
	 * 缓存过期时间
	 */
	public static final String CONFIG_CACHEEXPIRES = "Comet.CacheExpires";
	/**
	 * 缓存过期检查频率
	 */
	public static final String CONFIG_CACHEFREQUENCY = "Comet.CacheFrequency";
	/**
	 * 连接过期时间
	 */
	public static final String CONFIG_CONNEXPIRES = "Comet.ConnExpires";
	/**
	 * 连接过期查频率
	 */
	public static final String CONFIG_CONNFREQUENCY = "Comet.ConnFrequency";
	/**
	 * 调试
	 */
	public static final String CONFIG_DEBUG = "Comet.Debug";

	/**
	 * @param data
	 * @return
	 */
	public static String encode(Object data) {
		StringBuffer sb = new StringBuffer();
		sb.append(FLAG_DataStart);
		sb.append(JSONUtil.convertToJson(data));
		sb.append(FLAG_DataEnd);
		return sb.toString();
	}

}
