/*
 * Comet4J-JS
 * Copyright(c) 2011, jinghai.xiao@gamil.com.
 * 
 * This code is licensed under BSD license. Use it as you wish, 
 * but keep this copyright intact.
 */


JS.ns("JS.Connector");JS.Connector=JS.extend(JS.Observable,{version:'0.0.2',SYSCHANNEL:'c4j',LLOOPSTYLE:'lpool',STREAMSTYLE:'stream',CMDTAG:'cmd',url:'',param:'',cId:'',channels:[],workStyle:'',emptyUrlError:'URL为空',runningError:'连接正在运行',dataFormatError:'数据格式有误',running:false,_xhr:null,lastReceiveMessage:'',constructor:function(){JS.Connector.superclass.constructor.apply(this,arguments);this.addEvents(['beforeConnect','connect','beforeStop','stop','message','revival']);if(JS.isIE7){this._xhr=new JS.XMLHttpRequest({specialXHR:'Msxml2.XMLHTTP.6.0'});}else{this._xhr=new JS.XMLHttpRequest();}
this._xhr.addListener('readyStateChange',this.onReadyStateChange,this);this._xhr.addListener('timeout',this.revivalConnect,this);this.addListener('beforeStop',this.doDrop,this);JS.on(window,'beforeunload',this.doDrop,this);},doDrop:function(url,cId,conn,xhr){if(!this.running||!this.cId){return;}
try{var xhr=new JS.XMLHttpRequest();var url=this.url+'?'+this.CMDTAG+'=drop&cid='+this.cId;xhr.open('GET',url,false);xhr.send(null);xhr=null;}catch(e){};},dispatchServerEvent:function(msg){this.fireEvent('message',msg.channel,msg.data,msg.time,this);},translateStreamData:function(responseText){var str=responseText;if(this.lastReceiveMessage&&str){str=str.split(this.lastReceiveMessage);str=str.length?str[str.length-1]:"";}
this.lastReceiveMessage=responseText;return str;},decodeMessage:function(msg){var json=null;if(JS.isString(msg)&&msg!=""){if(msg.charAt(0)=="<"&&msg.charAt(msg.length-1)==">"){msg=msg.substring(1,msg.length-1);}
try{json=eval("("+msg+")");}catch(e){this.stop('JSON转换异常');}}
return json;},onReadyStateChange:function(readyState,status,xhr){if(!this.running){return;}
if(readyState<3){}else if(readyState==3&&(status>=200&&status<300)){if(this.workStyle===this.STREAMSTYLE){var str=this.translateStreamData(xhr.responseText);var json=this.decodeMessage(str);if(json){this.dispatchServerEvent(json);}
return;}}else if(readyState==4){if(status==0){if(!JS.isFirefox){this.stop('暂停服务');}}else if(status>=200&&status<300){if(this.workStyle===this.LLOOPSTYLE){var json=this.decodeMessage(xhr.responseText);if(json){this.dispatchServerEvent(json);}}
this.revivalConnect();}else if(status==408){}else if(status>400){this.stop('服务器异常');}}},startConnect:function(){if(this.running){var url=this.url+'?'+this.CMDTAG+'=conn&cv='+this.version+this.param;JS.AJAX.get(url,'',function(xhr){var msg=this.decodeMessage(xhr.responseText);if(!msg){this.stop('连接错误');return;}
var data=msg.data;this.cId=data.cId;this.channels=data.channels;this.workStyle=data.ws;this._xhr.timeout=data.timeout;this.fireEvent('connect',data.cId,data.channels,data.ws,data.timeout,this);this.revivalConnect();},this);}},revivalConnect:function(){var self=this;if(this.running){setTimeout(revival,100);}
function revival(){var xhr=self._xhr;var url=self.url+'?'+self.CMDTAG+'=revival&cid='+self.cId+self.param;xhr.open('GET',url,true);xhr.send(null);self.fireEvent('revival',self.url,self.cId,self);}},start:function(url,param){var self=this;setTimeout(function(){if(!self.url&&!url){throw new Error(self.emptyUrlError);}
if(self.running){return;}
if(url){self.url=url;}
if(param&&JS.isString(param)){if(param.charAt(0)!='&'){param='&'+param;}
self.param=param;}
if(self.fireEvent('beforeConnect',self.url,self)===false){return;}
self.running=true;self.startConnect();},1000);},stop:function(cause){if(!this.running){return;}
if(this.fireEvent('beforeStop',this.cId,this.url,this)===false){return;}
this.running=false;var cId=this.cId;this.cId='';this.param='';this.adml=[];this.workStyle='';try{this._xhr.abort();}catch(e){};this.fireEvent('stop',cause,cId,this.url,this);},getId:function(){return this.cId;}});