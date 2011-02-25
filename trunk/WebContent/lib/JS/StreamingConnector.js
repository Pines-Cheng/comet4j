/**
 * 
 * HTTP流连接器
 * 1.处于“链路层”，负责建立和断开连接。
 * 2.负责在链路上检测服务器端事件，并触发event事件。
 * @author jinghai.xiao@gmail.com
 * @depands : XMLHttpRequest.js
 */
JS.ns("JS.StreamingConnector");
//TODO:连接时同时得到连接列表，和应用ID列表
//TODO:需要检测在一段时间内没有状态变化时(onReadyStateChange)停止
JS.StreamingConnector = JS.extend(JS.Observable,{
	version : '0.0.2',
	url : '',
	param : '',
	cId : '',
	emptyUrlError : 'URL为空',
	runningError : '连接正在运行',
	dataFormatError : '数据格式有误',
	running : false,
	_xhr : null,
	lastReceiveMessage : '',
	constructor:function(){
		JS.StreamingConnector.superclass.constructor.apply(this,arguments);
		this.addEvents([
			/**
			 * 调用start方法之前触发,回调参数url, this
			 * @evnet beforeConnect
			 * @param 请求地址
			 * @param 发出事件的messageEngine
			 */
			'beforeConnect',
			/**
			 * 连接成功后触发,回调参数url, this, xmlHttpRequest对象
			 * @evnet connect
			 * @param 连接ID
			 * @param 请求地址
			 * @param 发出事件的messageEngine
			 * @param xmlHttpRequest对象
			 */
			'connect',
			/**
			 * 调用stop方法之前触发,回调参数：url,cid,this, xmlHttpRequest对象
			 * @evnet beforeStop
			 * @param 发出事件的messageEngine
			 * @param xmlHttpRequest对象
			 */
			'beforeStop',
			/**
			 * 调用stop方法之后触发,回调参数：url,cid,this, xmlHttpRequest对象
			 * @evnet stop
			 * @param 发出事件的messageEngine
			 * @param xmlHttpRequest对象
			 */
			'stop',
			/**
			 * 当有服务器端消息发生后触发,回调参数：data,this, xmlHttpRequest对象
			 * @evnet moduleEvent
			 * @param 发出事件内容
			 * @param xmlHttpRequest对象
			 * @param this
			 */
			'moduleEvent',
			/**
			 * 当连接请求复活时触发,回调参数：url,cid,this, xmlHttpRequest对象
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
			var responseText = xhr.responseText;
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
						this.cId = msg.data;
						this.fireEvent('connect',this.url,this.cId, this, this._xhr);
						break;
					default :
						this.fireEvent('moduleEvent', msg.data, responseText, this, this._xhr);
						break;
				}
				
			}
			return;
		}else if(readyState == 4 ){ //连接停止
			if(status == 0){//未知异常，一般为服务器异常停止服务
				if(JS.isFirefox){ //超时状态下只有FF返回0 ,这与其自动重试9次有关
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
			//xhr.abort();//IE和FirFox会有问题
			var url = this.url + '?cat=revival&cid=' + this.cId + this.param;
			xhr.open('GET', url, true);
			xhr.send(null);
		}
		this.fireEvent('revival',this.url,this.cId, this, this._xhr);
	},
	/**
	 * 开启连接
	 */
	start : function(param){
		if(!this.url){
			throw new Error(this.emptyUrlError);
		}
		if(this.running){
			throw new Error(this.runningError);
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
		if(this.fireEvent('beforeStop', this.url,this.cId, this,this._xhr) === false){
			return;
		}
		this.running = false;
		var cId = this.cId;
		this.cId = '';
		try{
			this._xhr.abort();
		}catch(e){};
		this.fireEvent('stop',this.url,cId, this, this._xhr);
	}
});
