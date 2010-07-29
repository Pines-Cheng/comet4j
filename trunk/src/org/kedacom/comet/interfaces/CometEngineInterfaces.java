package org.kedacom.comet.interfaces;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kedacom.comet.CometConnection;

public interface CometEngineInterfaces {
	//public ServerInfoDTO getServerInfo(HttpServletRequest request, HttpServletResponse response);
	public void connect(HttpServletRequest request, HttpServletResponse response);
	public void revival(HttpServletRequest request, HttpServletResponse response);
	public void drop(HttpServletRequest request, HttpServletResponse response);
	public void timeout(HttpServletRequest request, HttpServletResponse response);
	public List<CometConnection> getConnections();
	public void send (String id); 
	public void publish (String id); 
}
