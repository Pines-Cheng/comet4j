/**
 * 
 * 服务端消息引擎
 * 1.负责在“链路层”的基础上将服务器消息进一步转化，之使具有业务意义。
 * 2.此类
 * @author jinghai.xiao@gmail.com
 * depands : Engine.js
 */
JS.ns("JS.EventEngine");
JS.EventEngine = function(){
	var EventEngine = JS.extend(JS.Observable,{
		//config
		url : '',
		//propoty
		running : false,
		connector : null,
		lastReceiveMessage : '',
		constructor:function(){
			this.addEvents([
				'start',
				'stop',
				'join',
				'drop',
				'show',
				'hide',
				'message',
				'beat',
				'rename'
			]);
			EventEngine.superclass.constructor.apply(this,arguments);
			this.connector = new JS.Engine();
			var self = this;
			this.connector.on({
				start : function(url){
					self.running = true;
					self.fireEvent('start',url,this);
				},
				stop : function(){
					self.running = false;
					self.fireEvent('stop',this);
				},
				event : function(eventContent,xhr, target){
					self.onEvent(eventContent, xhr, target);
				}
			});
		},
		//private listenner
		onEvent : function(eventContent, xhr, target){
			var event = null;
			try{
				//eventContent = eventContent.replace(/\n/g,"\\n");
				event = eval("("+eventContent+")");
			}catch(e){
				//忽略
			}
			if(event){
				this.fireEvent(event.type,event,this);
			}

		},
		//public
		start : function(url){
			this.url = url || this.url;
			this.running = true;
			this.connector.start(this.url);
		},
		//public
		stop : function(){
			this.running = false;
			this.connector.stop();
		}
		
	});
	return new EventEngine();
}();