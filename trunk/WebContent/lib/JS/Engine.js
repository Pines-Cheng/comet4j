/**
 * 
 * 消息引擎
 * 负责解释接器上的消息，将其转化为应用模块事件
 * @author jinghai.xiao@gmail.com
 * depands : Connector.js
 */
JS.ns("JS.Engine");
JS.Engine = function(){
	var Engine = JS.extend(JS.Observable,{
		//propoty
		running : false,
		connector : null,
		constructor:function(){
			this.addEvents([
				'start',
				'connect',
				'stop'
			]);
			Engine.superclass.constructor.apply(this,arguments);
			this.connector = new JS.Connector();
			var self = this;
			this.connector.on({
				connect : function(url, param, data, conn, xhr){
					self.running = true;
					self.fireEvent('start', url, param, data, conn, xhr);
				},
				stop : function(url,cId, conn, xhr){
					self.running = false;
					self.fireEvent('stop',url,cId, conn, xhr);
				},
				message : function(msg){
					switch(msg.amk)
					{
						//连接成功
						case 'c4':
							var data = msg.data;
							this.cId = data.cId;
							this.aml = data.aml;
							this.fireEvent('connect',this.url, this.param, data, this, this._xhr);
							break;
						default :
							this.fireEvent('message', msg.data, responseText, this, this._xhr);
							break;
					}
				}
			});
		},
		//private listenner
		onEvent : function(eventContent, xhr, target){
			
		},
		//public
		start : function(url){
			if(this.running){
				return;
			}
			this.running = true;
			this.connector.start(url);
		},
		//public
		stop : function(){
			this.running = false;
			this.connector.stop();
		},
		getConnector : function(){
			return this.connector;
		}
		
	});
	return new Engine();
}();