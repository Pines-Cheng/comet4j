

# 简介 #
Comet4J是一个微型的即时推送框架，它分为服务端与客户端两部分，你只要将服务器端(JAR文件，目前仅支持Tomcat6、7)放入WEB-INF\lib，客户端(JavaScript文件)引入到页面，那么你的应用就具备了向客户端推送信息的能力，而你仅需要在服务器端调用Comet4J所提供发送方法，信息就会被主动的推送到客户的浏览器上。
# 准备工作 #
## 下载服务端jar文件 ##
> Comet4J目前仅支持Tomcat6、7版本，根据您所使用的Tomcat版本下载【comet4j-tomcat6.jar】或【comet4j-tomcat7.jar】文件放置到WEB项目的WEB-INF\lib目录下。
## 下载客户端js文件 ##
> 下载【comet4j.js】到您的项目中，比如：WebContent\js目录下。
## 修改服务器配置文件 ##
> 因为Comet4J工作在NIO方式下，所以我们需要调整服务器连接器配置，更换为NOI连接器。
> 打开server.xml文件将找到原先的连接器配置：
```
  <Connector executor="tomcatThreadPool" port="8080" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" />
```
> 替换为：
```
  <Connector URIEncoding="UTF-8" connectionTimeout="20000" port="8080" protocol="org.apache.coyote.http11.Http11NioProtocol" redirectPort="8443"/>
```
## 在web.xml中加载Comet4J框架 ##
> 最后我们需要在web.xml配置侦听和comet连接地址，以使Comet4J生效：
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
```
> 这里是最小化配置，更多配置请参见【更多资料】。至此所有的准备工作已经就绪，现在让我们来开发一个HelloWorld吧！


# 客户端使用简介 #
客户端是一个JavaScript文件（comet4j-0.0.2.js），其中最重要的是JS.Connector和JS.Engine两个类。JS.Connector负责与服务器建立并保持连接，而JS.Engine类负责将服务器推送过来的消息转化为开发人员可以处理的消息事件，并分发出去，关于客户端的API请参见：http://doc.comet4j.tk/jsdocs/ 。大多数情况下，我们仅需要使用JS.Engine类就可以完成多数的开发工作。

JS.Engine类是一个静态类，在一个页面中只有一个JS.Engine类的实例。它除了负责把服务器推过来的消息转化为事件分发以外，与服务器的连接与断开也由此类负责。
## JS.Engine.start方法 ##
JS.Engine.start(String str)和JS.Engine.stop(String str)分别控制连接和断开动作，start方法需要传入一个字符串参数，用来指定您配置的Comet4J连接地址。比如按前面准备工作的配置了CometServlet的地址为/conn，那么可以这样写:
```
JS.Engine.start('/conn');
```
上段代码我们让浏览器与服务器进行连接，当连接成功以后JS.Engine类会发出"start"事件,如何进行事件的处理我们稍后介绍。
## JS.Engine.stop方法 ##
我们也能够让连接断开：
```
JS.Engine.stop('主动断开');
```
上面代码我们让连接断开，并传入了一个“主动断开”这样一个断开的原因。如果您并不需要对断开的原因进行说明，也可以不传递参数：
```
JS.Engine.stop();
```
## JS.Engine类的事件处理 ##
上面我们介绍了如何使用start和stop方法来建立和断开连接，当成功建立连接已后JS.Engine会发出"start"事件，当断开后会发出“stop”事件，当收到某个通道推送过来的信息时也会发出与通道标识同名的事件。您可以事先在中使用JS.Engine.on方法来注册事件处理函数。例如：
```
JS.Engine.on('start',function(cId, channelList, engine){
    alert('连接已建立，连接ID为：' + cId);
});
JS.Engine.on('stop',function(cause, cId, url, engine){
    alert('连接已断开，连接ID为：' + cId + ',断开原因：' + cause + ',断开的连接地址：'+ url);
});
```
也可以将上段代码写成，下面代码与上段代码完全等效：
```
JS.Engine.on({
    start : function(cId, channelList, engine){
      alert('连接已建立，连接ID为：' + cId);
    },
    stop : function(cause, cId, url, engine){
      alert('连接已断开，连接ID为：' + cId + ',断开原因：' + cause + ',断开的连接地址：'+ url);
    }
});
```
接下来，介绍一下如何对服务器推送过来的消息进行处理。在介绍之前，我们假设后台已经注册了一个"hello"的应用通道标识，并且只向客户端推送简单的字符串信息。先看如下代码：
```
JS.Engine.on('hello',function(text){
    alert(text);
});
```
这样当服务器端使用"hello"通道标识推送过来的消息就可以由上段代码进行处理，将推送过来的信息弹出。


**特别注意：以上代码在事件处理函数中使用了alert仅为说明函数功能，实际使用中，在事件处理函数中切勿使用alert、prompt、confirm等可以中断脚本运行的函数，因为Engine需要实时的保持工作状态。**

# 服务器端使用简介 #
服务端由一个Jar包组成，其中最重的是CometContext和CometEngine两个类。

## Comet Context 类 ##

CometContext是一个单态类，通过其getInstance方法来获得实例，它主要负责框架的一些初始化工作保存着一些参数的配置值，除此之外它还有一个更重要的职责——负责注册应用通道标识。如果您想使用框架来实现自己的应用，那么您必需要为自己的应用分配一个唯一的通道标识，并将此通道标识在WEB容器启动时使用CometContext的registChannel方法进行注册，这样，客户端才可以正确接受此应用所推送的消息。注册一个通道标识非常简单：
```
CometContext.getInstance().registChannel("hello");
```
这样便注册了一个标识为“hello”的应用通道，而客户也可以通过JS.Engine.on('hello',function(msg){...})的形式来接收并处理来自此通道的消息。

## Comet Engine 类 ##

另一个重要的类是CometEngine，它除了负责对连接的处理之外，对于开发人员而言，更加常用的可能是它所提供的sendTo或sendToAll方法来向客户端发送消息：
```
String channel = "hello";
String someConnectionId = "1125-6634-888";
engine.sendToAll(channel , "我来了!");
engine.sendTo(channel , engine.getConnection(someConnectionId),“Hi,我是XXX”);
```
上面代码使用sendToAll方法向所有客户端在"hello"通道上发送了“我来了!”这样一条消息，然后又使用sendTo在同样的通道上向某一个连接发送了“Hi,我是XXX”消息。
CometEngine另外一个很重要的地方在于，它是框架工作的事件引擎的集散地，它提供了BeforeConnectEvent、BeforeDropEvent、ConnectEvent、DropEvent、MessageEvent等事件。通过对这些事件的处理来实现具体的功能：
```
class JoinListener extends ConnectListener {
	@Override
	public boolean handleEvent(ConnectEvent anEvent) {
		CometConnection conn = anEvent.getConn();
		CometContext.getInstance().getEngine().sendTo("hello", conn.getId(),"欢迎上线");
	}
}

CometEngine engine = CometContext.getInstance().getEngine();
engine.addConnectListener(new JoinListener());
```
上面先定义了一个JoinListener并实现了父类ConnectListener的handleEvent抽像方法，然后使用engine.addConnectListener来注册这个事件侦听。这样，在有客户与服务器成功建立连接已后，就可以向此客户端推送一条欢迎信息。


## 在线Demo体验 ##
http://www.comet4j.org:8080/comet4j/


更多资料等待与您的共同探索与分享...