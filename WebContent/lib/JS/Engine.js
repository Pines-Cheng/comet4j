/**
 * 
 * 消息引擎
 * 负责解释接器上的消息，将其转化为应用模块事件
 * @author jinghai.xiao@gmail.com
 * depands : Connector.js
 */
JS.ns("JS.Engine");
JS.Engine = (function(){
	var Engine = JS.extend(JS.Observable,{
		lStore : [],//用于存放没启动状态下用户增加的侦听
		running : false,
		connector : null,
		constructor:function(){
			this.addEvents([
				/**
				 * 当引擎开始工作时触发 cId, aml, engine
				 * @evnet start
				 * @param 请求地址
				 * @param 发出事件的
				 */   
				'start',
				/**
				 * 当引擎停止工作时触发 url,cId, engine
				 * @evnet stop
				 * @param 请求地址
				 * @param 发出事件的
				 */
				'stop'
			]);
			Engine.superclass.constructor.apply(this,arguments);
			this.connector = new JS.Connector();
			this.initEvent();
		},
		//重载addListener函数
		addListener : function(eventName, fn, scope, o){
			if(this.running){
				Engine.superclass.addListener.apply(this,arguments);
			}else{
				this.lStore.push({
					eventName : eventName,
					fn : fn,
					scope : scope,
					o : o
				});
			}
		},
		//private 
		initEvent : function(){
			var self = this;
			this.connector.on({
				connect : function(cId, aml, conn){
					self.running = true;
					self.addEvents(aml);
					for(var i=0,len=self.lStore.length; i<len; i++){
						var e = self.lStore[i];
						self.addListener(e.eventName,e.fn,e.scope);
					}
					self.fireEvent('start', cId, aml, self);
				},
				stop : function(url,cId, conn, xhr){
					self.running = false;
					self.fireEvent('stop',url,cId, self);
					self.clearListeners();
				},
				message : function(amk, data, time){
					self.fireEvent(amk, data, time, self);
				}
			});
		},
		//public
		start : function(url){
			if(this.running){
				return;
			}
			this.connector.start(url);
		},
		//public
		stop : function(){
			if(!this.running){
				return;
			}
			this.connector.stop();
		},
		getConnector : function(){
			return this.connector;
		},
		getId : function(){
			return this.connector.cId;
		}
		
	});
	return new Engine();
}());