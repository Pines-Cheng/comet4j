/*
 * Comet4J-JS
 * Copyright(c) 2011, jinghai.xiao@gamil.com.
 * 
 * This code is licensed under BSD license. Use it as you wish, 
 * but keep this copyright intact.
 */


JS.ns("JS.Engine");JS.Engine=(function(){var Engine=JS.extend(JS.Observable,{lStore:[],running:false,connector:null,constructor:function(){this.addEvents(['start','stop']);Engine.superclass.constructor.apply(this,arguments);this.connector=new JS.Connector();this.initEvent();},addListener:function(eventName,fn,scope,o){if(this.running){Engine.superclass.addListener.apply(this,arguments);}else{this.lStore.push({eventName:eventName,fn:fn,scope:scope,o:o});}},initEvent:function(){var self=this;this.connector.on({connect:function(cId,aml,conn){self.running=true;self.addEvents(aml);for(var i=0,len=self.lStore.length;i<len;i++){var e=self.lStore[i];self.addListener(e.eventName,e.fn,e.scope);}
self.fireEvent('start',cId,aml,self);},stop:function(cause,cId,url,conn){self.running=false;self.fireEvent('stop',cause,cId,url,self);self.clearListeners();},message:function(amk,data,time){self.fireEvent(amk,data,time,self);}});},start:function(url){if(this.running){return;}
this.connector.start(url);},stop:function(cause){if(!this.running){return;}
this.connector.stop(cause);},getConnector:function(){return this.connector;},getId:function(){return this.connector.cId;}});return new Engine();}());