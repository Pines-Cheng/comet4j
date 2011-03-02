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
		//propoty
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
		},
		//private 
		initEvent : function(){
			var self = this;
			this.connector.on({
				connect : function(cId, aml, conn){
					self.addEvents(aml);
					self.fireEvent('start', cId, aml, self);
				},
				stop : function(url,cId, conn, xhr){
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
			this.running = true;
			this.initEvent();
			this.connector.start(url);
		},
		//public
		stop : function(){
			if(!this.running){
				return;
			}
			this.running = false;
			this.connector.stop();
		},
		getConnector : function(){
			return this.connector;
		},
		getCid : function(){
			return this.connector.cId;
		}
		
	});
	return new Engine();
}());