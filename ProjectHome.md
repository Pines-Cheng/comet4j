Comet4J(Comet for Java)是一个纯粹基于AJAX(XMLHTTPRequest)的服务器推送框架，消息以JSON方式传递，具备长轮询、长连接、自动选择三种工作模式。

## 在线Demo体验 ##

## 客户端API ##
http://doc.comet4j.tk/jsdocs/
## 服务端API ##
http://doc.comet4j.tk/apidocs/

## 功能特性 ##
  * 推送消息广播。
  * 推送定向消息。
  * 提供连接上线前、上线、下线前、下线、发送消息等多种可处理事件。
  * 消息缓存机制，确保长轮询工作模式下不丢失消息。
  * 客户端正常下线，服务端可立即感知。
  * 客户端异常停止工作，服务端可定时检查并感知。
  * 以注册通道应用的方式，让开发者对框架功能进行扩展，实现自己的应用。

## 框架特性 ##
  * 独立小巧，不依赖于第三方包。
  * 与应用紧密集成，无需独立应用或服务器。
  * 与Session无关的连接机制，为开发人员提供最大程度的功能可控性。
  * 面向事件编程，客户端与服务器端均为事件驱动开发模式，提供了良好的可扩展性机制。
  * 各项性能参数均可配置。
  * 支持多种主流浏览器，并支持Air应用环境。

## 服务器支持情况 ##

Tomcat6、Tomcat7

## 浏览器支持情况 ##

支持XMLHTTPRequest对象的浏览器均可支持长轮询工作模式，但不一定能够支持长连接。
| **浏览器/平台** | **版本** | **长轮询** | **长连接** |
|:---------------------|:-----------|:--------------|:--------------|
| Internet Explorer | 6,7,8,9 | √ | X |
| FireFox | 3.0+(更底版本未知) | √ | √ |
| Chrome | 7.0+(更底版本未知) | √ | √ |
| Safari | 5+(更底版本未知) | √ | √ |
| Opera | 11.10+(更底版本未知) | √ | X |
| Air | 1.5+(更底版本未知) | √ | √ |
| IOS(Iphone/Ipad)| 3.1+(更底版本未知) | √ | √ |
| Android | 未测试 | 未知 | 未知 |
| BlackBerry| 未测试 | 未知 | 未知 |

![http://www.sencha.com/img/browsers/safari.png](http://www.sencha.com/img/browsers/safari.png)
![http://www.sencha.com/img/browsers/firefox.png](http://www.sencha.com/img/browsers/firefox.png)
![http://www.sencha.com/img/browsers/ie.png](http://www.sencha.com/img/browsers/ie.png)
![http://www.sencha.com/img/browsers/chrome.png](http://www.sencha.com/img/browsers/chrome.png)
![http://www.sencha.com/img/browsers/opera.png](http://www.sencha.com/img/browsers/opera.png)
![http://www.adobe.com/images/shared/product_mnemonics/50x50/air_50x50.gif](http://www.adobe.com/images/shared/product_mnemonics/50x50/air_50x50.gif)

![http://images.apple.com/ios/images/icon_safari_20110302.jpg](http://images.apple.com/ios/images/icon_safari_20110302.jpg)

## 参与开发 ##
如果您喜欢它，希望共同推动Comet4J的发展，那么请邮件至comet4j@163.com