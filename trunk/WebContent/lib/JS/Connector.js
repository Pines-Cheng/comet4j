/**
 * 连接器
 * 负责建立、维持连接，如接收到信息并发接收到信息事件。
 * @author jinghai.xiao@gmail.com
 * @depands : XMLHttpRequest.js
 */
JS.ns("JS.Connector");
JS.Connector = JS.extend(JS.Observable,{
	version : '0.0.2',
	url : '',
	param : '', //连接参数
	cId : '', //连接ID，连接后有效
	aml : [], //应用模块列表，连接后有效
	emptyUrlError : 'URL为空',
	runningError : '连接正在运行',
	dataFormatError : '数据格式有误',
	running : false,
	_xhr : null,
	lastReceiveMessage : '',
	constructor:function(){
		JS.Connector.superclass.constructor.apply(this,arguments);
		this.addEvents([
			/**
			 * 调用beforeConnect方法之前触发,回调参数url, conn
			 * @evnet beforeConnect
			 * @param 请求地址
			 * @param 发出事件的messageEngine
			 */
			'beforeConnect',
			/**
			 * 连接成功后触发,回调参数cId, aml, conn
			 * @evnet connect
			 * @param 连接ID
			 * @param 请求地址
			 * @param 发出事件的messageEngine
			 * @param xmlHttpRequest对象
			 */
			'connect',
			/**
			 * 调用stop方法之前触发,回调参数：url, cId, conn
			 * @evnet beforeStop
			 * @param 发出事件的messageEngine
			 * @param xmlHttpRequest对象
			 */
			'beforeStop',
			/**
			 * 调用stop方法之后触发,回调参数： url, cId, conn
			 * @evnet stop
			 * @param 发出事件的messageEngine
			 * @param xmlHttpRequest对象
			 */
			'stop',
			/**
			 * 当有服务器端消息发生后触发,回调参数：amk, data, time, conn
			 * @evnet message
			 * @param 发出事件内容
			 * @param xmlHttpRequest对象
			 * @param this
			 */
			'message',
			/**
			 * 当连接请求复活时触发,回调参数：url, cId, conn
			 * @evnet revival
			 * @param 发出事件内容
			 * @param xmlHttpRequest对象
			 * @param this
			 */
			'revival'
		]);
		this._xhr = new JS.XMLHttpRequest();
		this._xhr.addListener('readyStateChange',this.onReadyStateChange,this);
		this.addListener('beforeStop',this.doDrop,this);
		JS.on(window,'beforeunload',this.doDrop,this);

	},
	//private
	doDrop : function(url,cId,conn,xhr){
		if(!this.running || !this.cId){
			return;
		}
		try {
			var xhr = new JS.XMLHttpRequest();
			var url = this.url + '?cat=drop&cid=' + this.cId;
			xhr.open('GET', url, false);
			xhr.send(null);
			xhr = null;
		}catch(e){};
	},
	//private lisenner
	onReadyStateChange : function(readyState,status,xhr){
		if(!this.running){
			return;
		}
		//alert( xhr.responseText);
		if(readyState < 3){	//初始阶段
			
		}else if(readyState == 3 && (status >= 200 && status < 300)){//正常接收
			var responseText = xhr.responseText;//TODO:IE6及以下版本在3时不能使用responseText
			if(this.lastReceiveMessage && responseText){
				responseText = responseText.split(this.lastReceiveMessage);
				responseText = responseText.length?responseText[responseText.length-1]:"";
			}
			this.lastReceiveMessage = xhr.responseText;
			if(responseText!==""){
				if(responseText.charAt(0)=="<" && responseText.charAt(responseText.length-1)==">"){
					responseText = responseText.substring(1,responseText.length-1);
				}
				var msg = null;
				try{
					msg = eval("("+responseText+")");
				}catch(e){
					throw new Error(this.dataFormatError);
					this.stop();
					return;
				}
				switch(msg.amk)
				{
					//连接成功
					case 'c4':
						var data = msg.data;
						this.cId = data.cId;
						this.aml = data.aml;
						this.fireEvent('connect', data.cId, data.aml, this);
						break;
					default :
						this.fireEvent('message', msg.amk, msg.data, msg.time, this);
						break;
				}
				
			}
			return;
		}else if(readyState == 4 ){ //连接停止
			if(status == 0){//未知异常，一般为服务器异常停止服务
				if(JS.isFirefox){ //超时状态下只有FF返回0 ,这与其自动重试10次有关,还没有找到有效办法能够确识别408
					this.revivalConnect();
				}else{
					this.stop();
				}
			}else if(status >= 200 && status < 300){ //正常情况下的最后一条成功信息
				this.revivalConnect();
			}else if(status == 408){ //超时
				this.revivalConnect();
			}else if(status > 400){
				this.stop();
			}
			
		}

	},
	/**
	 * 开始连接
	 * @private
	 */
	startConnect : function(){
		if(this.running){
			var xhr = this._xhr;
			var url = this.url+'?cat=conn&cv='+this.version+this.param;
			xhr.open('GET', url, true);
			xhr.send(null);
		}
	},

	/**
	 * 复活连接
	 * @private
	 */
	revivalConnect : function(){
		if(this.running){
			var xhr = this._xhr;
			if(!JS.isIE){
				xhr.abort();//IE abort后xhr对象不可再次使用，FireFox下确定
			}
			var url = this.url + '?cat=revival&cid=' + this.cId + this.param;
			xhr.open('GET', url, true);
			xhr.send(null);
		}
		this.fireEvent('revival',this.url, this.cId, this);
	},
	/**
	 * 开启连接
	 */
	start : function(url,param){
		if(!this.url && !url){
			throw new Error(this.emptyUrlError);
		}
		
		if(this.running){
			throw new Error(this.runningError);
		}
		if(url){
			this.url = url;
		}
		
		if(param && JS.isString(param)){
			if(param.charAt(0) != '&'){
				param = '&'+param;
			}
			this.param = param;
		}
		if(this.fireEvent('beforeConnect', this.url, this) === false){
			return;
		}

		this.running = true;
		this.startConnect();
	},
	/**
	 * 断开连接
	 */
	stop : function(){
		if(!this.running){
			return;
		}
		if(this.fireEvent('beforeStop', this.url, this.cId, this) === false){
			return;
		}
		this.running = false;
		var cId = this.cId;
		this.cId = '';
		this.param = '';
		this.adml = [];
		try{
			if(!JS.isIE){//IE8及以前版本abort之后xhr对象无法再次使用
				this._xhr.abort();
			}
			
		}catch(e){};
		this.fireEvent('stop',this.url, cId, this);
	},
	/**
	 * 获取连接Id,连接状态下有效
	 */
	getId : function(){
		return this.cId;
	}
});
