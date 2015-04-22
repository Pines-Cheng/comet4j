

# 准备工作 #
## 1.下载服务端jar文件 ##
> Comet4J目前仅支持Tomcat6、7版本，根据您所使用的Tomcat版本下载【comet4j-tomcat6.jar】或【comet4j-tomcat7.jar】文件放置到WEB项目的WEB-INF\lib目录下。
## 2.下载客户端js文件 ##
> 下载【comet4j.js】到您的项目中，比如：WebContent\js目录下。
## 3.修改服务器配置文件 ##
> 因为Comet4J工作在NIO方式下，所以我们需要调整服务器连接器配置，更换为NOI连接器。
> 打开server.xml文件将找到原先的连接器配置：
```
  <Connector executor="tomcatThreadPool" port="8080" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" />
```
> 替换为：
```
  <Connector URIEncoding="UTF-8" connectionTimeout="20000" port="8080" protocol="org.apache.coyote.http11.Http11NioProtocol" redirectPort="8443"/>
```

# 客户端 #
我们利用Comet4J开发一个每隔一秒向所有客户端推送服务器的剩余内存大小。
helloworld.html
```
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Comet4J Hello World</title>
<script type="text/javascript" src="js/comet4j-0.0.2.js"></script>
<script type="text/javascript">
function init(){
	var kbDom = document.getElementById('kb');
	JS.Engine.on({
		hello : function(kb){//侦听一个channel
			kbDom.innerHTML = kb;
		}
	});
	JS.Engine.start('conn');
}
</script>
</head>
<body onload="init()">
	剩余内存：<span id="kb">...</span>KB
</body>
</html>
```
# 服务端 #
helloworld.java
```
package org.comet4j.demo.helloworld;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.comet4j.core.CometContext;
import org.comet4j.core.CometEngine;

public class HelloWorld implements ServletContextListener {
	private static final String CHANNEL = "hello";
	public void contextInitialized(ServletContextEvent arg0) {
		CometContext cc = CometContext.getInstance();
		cc.registChannel(CHANNEL);//注册应用的channel
		Thread helloAppModule = new Thread(new HelloAppModule(), "Sender App Module");
		helloAppModule.setDaemon(true);
		helloAppModule.start();

	}

	class HelloAppModule implements Runnable {
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				CometEngine engine = CometContext.getInstance().getEngine();
				engine.sendToAll(CHANNEL, Runtime.getRuntime().freeMemory()/1024);
			}
		}
	}

	public void contextDestroyed(ServletContextEvent arg0) {

	}
}


```
# 配置 #
web.xml

```
<listener>
		<description>Comet4J容器侦听</description>
		<listener-class>org.comet4j.core.CometAppListener</listener-class>
	</listener>
	<servlet>
		<description>Comet连接[默认:org.comet4j.core.CometServlet]</description>
		<display-name>CometServlet</display-name>
		<servlet-name>CometServlet</servlet-name>
		<servlet-class>org.comet4j.core.CometServlet</servlet-class>
	</servlet>
	<servlet-mapping>
		<servlet-name>CometServlet</servlet-name>
		<url-pattern>/conn</url-pattern>
	</servlet-mapping>

<listener>
  <description>HelloWorld</description>
  <listener-class>org.comet4j.demo.helloworld.HelloWorld</listener-class>
</listener>
```