/**
 * 提供对事件模型的支持
 * @author jinghai.xiao@gmail.com
 */
JS.ns("JS.Observable");
JS.Observable = function(o){
	JS.apply(this,o || JS.toArray(arguments)[0]);
	if(this.events){
		this.addEvents(this.events)
	}
    if(this.listeners){
        this.on(this.listeners);
        delete this.listeners;
    }
};
JS.Observable.prototype = {
	on : function(eventName, fn, scope, o){
		if(JS.isString(eventName)){
			this.addListener(eventName, fn, scope, o);
		}else if(JS.isObject(eventName)){
			this.addListeners(eventName);
		}
	},
	/*
	 *
	 * @param {String},事件名称
	 * @param {Function}，事件处理函数
	 * @param {Object},作用域
	 * @param {[AnyType...]}回调参数，任意数量、任意类型
	 */
	fireEvent : function(){
		var arg = JS.toArray(arguments),
		    eventName = arg[0].toLowerCase(),
			e = this.events[eventName];
		if(e && !JS.isBoolean(e)){
			return e.fire.apply(e,arg.slice(1));
		}
	},
	addEvent : function(eventName){
		if(!JS.isObject(this.events)){
			this.events = {};
		}
		//Fixed:重复添加相同事件
		if(this.events[eventName]){
			return;
		}
		if(JS.isString(eventName)){
			this.events[eventName.toLowerCase()] = true;
		}else if(eventName instanceof JS.Event){
			this.events[eventName.name.toLowerCase()] = eventName;
		}
	},
	/**
	 * 添加事件
	 * ['beforeAction','action']
	 * param@ {Array<String>||Array<Event>} 可以是String或Event数组。
	 */
	addEvents : function(arr){
		if(JS.isArray(arr)){
			for(var i = 0,len = arr.length; i < len; i++){
				this.addEvent(arr[i]);
			}
		}
	},
	addListener : function(eventName, fn, scope, o){
		eventName = eventName.toLowerCase();
		var e = this.events[eventName];
		if(e){
			if(JS.isBoolean(e)){
				e = this.events[eventName] = new JS.Event(eventName,this);
			}
			e.addListener(fn, scope , o);
		}
	},
	addListeners : function(obj){
		if(JS.isObject(obj)){
			for(var p in obj){
				this.addListener(p,obj[p]);
			}
		}
	},
	removeListener : function(eventName, fn, scope){
		eventName = eventName.toLowerCase();
		var e = this.events[eventName];
		if(e && !JS.isBoolean(e)){
			e.removeListener(fn, scope);
		}
	},
	clearListeners : function(){
		var events = this.events,
			e;
		for(var p in events){
			e = events[p];
			if(!JS.isBoolean(e)){
				e.clearListeners();
			}
		}
	},
	clearEvents : function(){
		var events = this.events;
		this.clearListeners();
		for(var p in events){
			this.removeEvent(p);
		}
	},
	removeEvent : function(eventName){
		var events = this.events,
			e;
		if(events[eventName]){
			e = events[eventName];
			if(!JS.isBoolean(e)){
				e.clearListeners();
			}
			delete events[eventName];
		}
		
	},
	removeEvents : function(eventName){
		if(JS.isString(eventName)){
			this.removeEvent(eventName);
		}else if(JS.isArray(eventName) && eventName.length > 0){
			for(var i=0, len=eventName.length; i<len ;i++){
				this.removeEvent(eventName[i]);
			}
		}
	},
	hasEvent : function(eventName){
		return this.events[eventName]?true:false;
	},
	hasListener : function(eventName,fn,scope){
		var events = this.events,
			e = events[eventName];
		if(!JS.isBoolean(e)){
			return e.hasListener(fn,scope);
		}
		return false;
	},
	suspendEvents : function(){
		//TODO:
	},
	resumeEvents : function(){
		//TODO:
	}
}
/**
 * 事件源，代表一类事件。
 * 负责管理并存放一类事件的监听
 */
JS.Event = function(name,caller){
	this.name = name.toLowerCase();
	this.caller = caller;
	this.listeners = [];
}
JS.Event.prototype = {
	fire : function(){
		var 
			listeners = this.listeners,
			//len = listeners.length,
			i = listeners.length-1
		for(; i > -1; i--){//TODO:fix 倒序
			if(listeners[i].execute.apply(listeners[i],arguments) === false){
				return false;
			}
		}
		return true;
	},
	addListener : function(fn, scope,o){
        scope = scope || this.caller;
        if(this.hasListener(fn, scope) == -1){
            this.listeners.push(new JS.Listener(fn, scope ,o));
        }
    },
	removeListener : function(fn, scope){
        var index = this.hasListener(fn, scope);
		alert(index);
		if(index!=-1){
			this.listeners.splice(index, 1);
		}
    },
	hasListener : function(fn, scope){
		//alert(scope.id);
		var i = 0,
			listeners = this.listeners,
			len = listeners.length;
		for(; i<len; i++){
			if(listeners[i].equal(fn, scope)){
				return i;
			}
		}
		return -1;
	},
	clearListeners : function(){
		var i = 0,
			listeners = this.listeners,
			len = listeners.length;
		for(; i<len; i++){
			listeners[i].clear();
		}
		this.listeners.splice(0);
	}

}
/**
 * 侦听器
 * 负责存放和执行一个侦听函数
 */
JS.Listener = function(fn, scope,o){
	this.handler = fn;
	this.scope = scope;
	this.o = o;//配置项，delay,buffer,once,
}
JS.Listener.prototype = {
	execute : function(){
		return JS.callBack(this.handler,this.scope,arguments);
	},
	equal : function(fn, scope){
		return this.handler === fn && this.scope === scope ? true : false;
	},
	clear : function(){
		delete this.handler;
		delete this.scope ;
		delete this.o ;
	}
}