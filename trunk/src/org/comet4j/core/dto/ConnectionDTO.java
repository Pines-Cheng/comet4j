package org.comet4j.core.dto;

import java.util.List;

/**
 * @author jinghai.xiao@gmail.com 成功连接后的连接信息
 */
public class ConnectionDTO {

	public ConnectionDTO(String cId, String ws, List<String> aml, int timeout) {
		this.cId = cId;
		this.ws = ws;
		this.aml = aml;
		this.timeout = timeout;
	}

	/**
	 * 连接ID
	 */
	private String cId;
	/**
	 * 工作模式workStyle
	 */
	private String ws;
	/**
	 * 应用模块列表appModuesList
	 */
	private List<String> aml;
	/**
	 * 服务器超时时间
	 */
	private int timeout;

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public List<String> getAml() {
		return aml;
	}

	public void setAml(List<String> aml) {
		this.aml = aml;
	}

	public String getWs() {
		return ws;
	}

	public void setWs(String ws) {
		this.ws = ws;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
