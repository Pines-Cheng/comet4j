/**
 * 
 * @author jinghai.xiao@gmail.com
 * @version 0.0.1
 * depands : JS.js, Observable.js
 */
JS.ns("JS.HTTPStatus","JS.XMLHttpRequest");
 /**
  * FC 2616 HTTP1.1规范的HTTP Status状态常量
  * http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10
  */

JS.HTTPStatus = {
	//Informational 1xx
	'100' : 'Continue',
	'101' : 'Switching Protocols',
	//Successful 2xx
	'200' : 'OK',
	'201' : 'Created',
	'202' : 'Accepted',
	'203' : 'Non-Authoritative Information',
	'204' : 'No Content',
	'205' : 'Reset Content',
	'206' : 'Partial Content',
	//Redirection 3xx
	'300' : 'Multiple Choices',
	'301' : 'Moved Permanently',
	'302' : 'Found',
	'303' : 'See Other',
	'304' : 'Not Modified',
	'305' : 'Use Proxy',
	'306' : 'Unused',
	'307' : 'Temporary Redirect',
	//Client Error 4xx
	'400' : 'Bad Request',
	'401' : 'Unauthorized',
	'402' : 'Payment Required',
	'403' : 'Forbidden',
	'404' : 'Not Found',
	'405' : 'Method Not Allowed',
	'406' : 'Not Acceptable',
	'407' : 'Proxy Authentication Required',
	'408' : 'Request Timeout',
	'409' : 'Conflict',
	'410' : 'Gone',
	'411' : 'Length Required',
	'412' : 'Precondition Failed',
	'413' : 'Request Entity Too Large',
	'414' : 'Request-URI Too Long',
	'415' : 'Unsupported Media Type',
	'416' : 'Requested Range Not Satisfiable',
	'417' : 'Expectation Failed',
	//Server Error 5xx
	'500' : 'Internal Server Error',
	'501' : 'Not Implemented',
	'502' : 'Bad Gateway',
	'503' : 'Service Unavailable',
	'504' : 'Gateway Timeout',
	'505' : 'HTTP Version Not Supported'
}
JS.HTTPStatus.OK = 200;
JS.HTTPStatus.BADREQUEST = 400;
JS.HTTPStatus.FORBIDDEN = 403;
JS.HTTPStatus.NOTFOUND = 404;
JS.HTTPStatus.TIMEOUT = 408;
JS.HTTPStatus.SERVERERROR = 500;

/**
 * 
 * 封装一个跨浏览器的XMLHTTPRequest对象，并支持readyStateChange事件监听
 * 封装标准：http://www.w3.org/TR/XMLHttpRequest/
 * 1.不改变传统XMLHTTPRequest用法，即适应于传统XMLHTTPRequest的操作此对象都适用。
 * 2.可使用事件监听方式侦听readyStateChange,如xhr.on('readyStateChange',function(){//doSomething});
 * 
 */
JS.XMLHttpRequest = JS.extend(JS.Observable,{
	//config
	enableCache : false,
	timeout : 0,//default never time out
	//propoty
	_xhr : null,
	//--------request propoty--------
	readyState : 0,
	//--------response propoty--------
	status : 0,
	statusText : '',
	responseText : '',
	responseXML : null,
	//private method
	constructor : function(){
		var self = this;
		this.addEvents([
			/**
			 * @param this.readyState, this.status,this,xhr
			 */
			'readyStateChange',
			/**
			 * 超时之后触发,回调参数：this,xhr
			 * @evnet timeout
			 */
			'timeout',
			/**
			 * abort方法之后触发,回调参数：this,xhr
			 * @evnet abort
			 */
			'abort',
			//TODO:need to do
			'error',
			'load',
			'progress'
		]);
		JS.XMLHttpRequest.superclass.constructor.apply(this,arguments);
		this._xhr = this.createXmlHttpRequestObject();
		this._xhr.onreadystatechange = function(){
			self.doReadyStateChange();
		};
	},
	//private
	//超时任务
	timeoutTask : null,
	//延迟执行超时任务(timeoutTask)
	delayTimeout : function(){
		if(this.timeout){
			if(!this.timeoutTask){
				this.timeoutTask = new JS.DelayedTask(function(){
					//readyState=4已经停止，由doReadyStateChange来判断为何停止
					if(this._xhr.readyState != 4){
						this.fireEvent('timeout', this, this._xhr);
					}else{
						this.cancelTimeout();
					}
				},this);
			}
			this.timeoutTask.delay(this.timeout);
		}
	},
	//取消超时任务
	cancelTimeout : function(){
		if(this.timeoutTask){
			this.timeoutTask.cancel();
		}
	},
	createXmlHttpRequestObject : function(){
		var activeX = [
			'Msxml2.XMLHTTP.6.0', 
			'Msxml2.XMLHTTP.5.0', 
			'Msxml2.XMLHTTP.4.0', 
			'Msxml2.XMLHTTP.3.0', 
			'Msxml2.XMLHTTP', 
			'Microsoft.XMLHTTP'],
		xhr;

		try {
			xhr = new XMLHttpRequest();                
		} catch(e) {
			for (var i = 0; i < activeX.length; ++i) {	            
				try {
					xhr = new ActiveXObject(activeX[i]);                        
					break;
				} catch(e) {}
			}
		} finally {
			return xhr;
		}
	},
	//private
	doReadyStateChange : function(){
		this.delayTimeout();
		var xhr = this._xhr;
		try{
			this.readyState = xhr.readyState;
		}catch(e){
			this.readyState = 0
		}
		try{
			this.status = xhr.status;
		}catch(e){
			this.status = 0
		}
		try{
			this.statusText = xhr.statusText;
		}catch(e){
			this.statusText = "";
		}
		try {
			this.responseText = xhr.responseText;
		}catch(e){
			this.responseText = "";
		}
		try {
			this.responseXML = xhr.responseXML;
		}catch(e){
			this.responseXML = null;
		}
		
		this.fireEvent('readyStateChange',this.readyState, this.status, this, xhr );
		
		if(this.readyState == 3 && (this.status >= 200 && this.status < 300)){
			this.fireEvent('progress', this, xhr);
		}
		
		if(this.readyState == 4){
			this.cancelTimeout();
			var status = this.status ;
			if(status == 0){
				this.fireEvent('error', this, xhr);
			}else if(status >= 200 && status < 300){
				this.fireEvent('load', this, xhr);
			}else if(status >= 400 && status != 408){
				this.fireEvent('error', this, xhr);
			}else if(status == 408){
				this.fireEvent('timeout', this, xhr);
			}
			
		}
		this.onreadystatechange();
	},
	//兼容传统XMLHttpRequest对象
	onreadystatechange : function(){
	},
	//--------request--------
	//public
	open : function(method, url, async, username, password){
		if(!url){
			return;
		}
		if(!this.enableCache){
			if(url.indexOf('?') != -1){
				url += '&ram='+Math.random();
			}else{
				url += '?ram='+Math.random();
			}
		}
		this._xhr.open(method, url, async, username, password);
	},
	//public
	send : function(content){
		this.delayTimeout();
		this._xhr.send(content);
	},
	//public
	abort : function(){
		this.cancelTimeout();
		this._xhr.abort();
		if(JS.isIE){//IE在abort后会清空侦听
			var self = this;
			self._xhr.onreadystatechange = function(){
				self.doReadyStateChange();
			};
		}
		this.fireEvent('abort',this,this._xhr);
	},
	//public
	setRequestHeader : function(header, value){
		this._xhr.setRequestHeader(header,value);
	},
	//--------request--------
	getResponseHeader : function(header){
		return this._xhr.getResponseHeader(header);
	},
	getAllResponseHeaders : function(){
		return this._xhr.getAllResponseHeaders();
	}

});