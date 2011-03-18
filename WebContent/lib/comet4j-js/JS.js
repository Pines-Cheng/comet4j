/**
 * JS命名空间以及一些快捷方法引用
 * @author jinghai.xiao@gmail.com
 */

var JS = {
	version : '0.0.1'
};
/**
 * 
 * 测试JavaScript支持环境
 * 1.客户端浏览器版本
 * 2.客户端操作系统
 * 
 * @author jinghai.xiao@gmail.com
 */
JS.Runtime = (function(){
	var ua = navigator.userAgent.toLowerCase(),
    check = function(r){
        return r.test(ua);
    },
	isOpera = check(/opera/),
	isFirefox = check(/firefox/),
	isChrome = check(/chrome/),
	isWebKit = check(/webkit/),
	isSafari = !isChrome && check(/safari/),
	isSafari2 = isSafari && check(/applewebkit\/4/),
	isSafari3 = isSafari && check(/version\/3/),
	isSafari4 = isSafari && check(/version\/4/),
	isIE = !isOpera && check(/msie/),
	isIE7 = isIE && check(/msie 7/),
	isIE8 = isIE && check(/msie 8/),
	isIE6 = isIE && !isIE7 && !isIE8,
	isGecko = !isWebKit && check(/gecko/),
	isGecko2 = isGecko && check(/rv:1\.8/),
	isGecko3 = isGecko && check(/rv:1\.9/),
	isWindows = check(/windows|win32/),
	isMac = check(/macintosh|mac os x/),
	isAir = check(/adobeair/),
	isLinux = check(/linux/);
	return {
		isOpera : isOpera,
		isFirefox : isFirefox,
	    isChrome : isChrome,
	    isWebKit : isWebKit,
	    isSafari : isSafari,
	    isSafari2 : isSafari2,
	    isSafari3 : isSafari3,
	    isSafari4 : isSafari4,
	    isIE : isIE,
	    isIE7 : isIE7,
	    isIE8 : isIE8,
	    isIE6 : isIE6,
	    isGecko : isGecko,
	    isGecko2 : isGecko2,
	    isGecko3 : isGecko3,
	    isWindows :isWindows,
	    isMac : isMac,
	    isAir : isAir,
	    isLinux : isLinux
	};
}());
JS.isOpera = JS.Runtime.isOpera;
JS.isFirefox = JS.Runtime.isFirefox;
JS.isChrome = JS.Runtime.isChrome;
JS.isWebKit = JS.Runtime.isWebKit;
JS.isSafari = JS.Runtime.isSafari;
JS.isSafari2 = JS.Runtime.isSafari2;
JS.isSafari3 = JS.Runtime.isSafari3;
JS.isSafari4 = JS.Runtime.isSafari4;
JS.isIE = JS.Runtime.isIE;
JS.isIE7 = JS.Runtime.isIE7;
JS.isIE8 = JS.Runtime.isIE8;
JS.isIE6 = JS.Runtime.isIE6;
JS.isGecko = JS.Runtime.isGecko;
JS.isGecko2 = JS.Runtime.isGecko2;
JS.isGecko3 = JS.Runtime.isGecko3;
JS.isWindows =JS.Runtime.isWindows;
JS.isMac = JS.Runtime.isMac;
JS.isAir = JS.Runtime.isAir;
JS.isLinux = JS.Runtime.isLinux;
/**
 * Syntax类负责实现JavaScript语言在语法特性上的一些扩展。
 * @author jinghai.xiao@gmail.com
 */
JS.Syntax = {
	nameSpace : function(){
		if(arguments.length){
			var o, d, v;
			for(var i=0,len=arguments.length; i<len; i++){
				v = arguments[i];
				if(!v){
					continue;
				}
				d = v.split(".");
				for(var j=0,len=d.length; j<len; j++){
					if(!d[j]){
						continue;
					}
					o = window[d[j]] = window[d[j]] || {};
				}
			}
		}
		return o;
	},
	apply : function(o, c, defaults){
		// no "this" reference for friendly out of scope calls
		if(defaults){
			JS.Syntax.apply(o, defaults);
		}
		if(o && c && typeof c == 'object'){
			for(var p in c){
				o[p] = c[p];
			}
		}
		return o;
	},
	override : function(origclass, overrides){
		if(overrides){
			var p = origclass.prototype;
			JS.Syntax.apply(p, overrides);
			if(JS.Runtime.isIE && overrides.hasOwnProperty('toString')){
				p.toString = overrides.toString;
			}
		}
	},
	extend : function(){
		// siple object copy
		var io = function(o){
			for(var m in o){
				this[m] = o[m];
			}
		};
		var oc = Object.prototype.constructor;

		return function(sb, sp, overrides){
			if(JS.Syntax.isObject(sp)){
				overrides = sp;
				sp = sb;
				sb = overrides.constructor != oc ? overrides.constructor : function(){sp.apply(this, arguments);};
			}
			var F = function(){},
				sbp,
				spp = sp.prototype;

			F.prototype = spp;
			sbp = sb.prototype = new F();
			sbp.constructor=sb;
			sb.superclass=spp;
			if(spp.constructor == oc){
				spp.constructor=sp;
			}
			sb.override = function(o){
				JS.Syntax.override(sb, o);
			};
			sbp.superclass = sbp.supr = (function(){
				return spp;
			});
			sbp.override = io;
			JS.Syntax.override(sb, overrides);
			sb.extend = function(o){return JS.Syntax.extend(sb, o);};
			return sb;
		};
	}(),
	callBack : function(fn,scope,arg){
		if(JS.isFunction(fn)){
			return fn.apply(scope || window,arg || []);
		}
	},
	/**
	 * <p>Returns true if the passed value is empty.</p>
	 * <p>The value is deemed to be empty if it is<div class="mdetail-params"><ul>
	 * <li>null</li>
	 * <li>undefined</li>
	 * <li>an empty array</li>
	 * <li>a zero length string (Unless the <tt>allowBlank</tt> parameter is <tt>true</tt>)</li>
	 * </ul></div>
	 * @param {Mixed} value The value to test
	 * @param {Boolean} allowBlank (optional) true to allow empty strings (defaults to false)
	 * @return {Boolean}
	 */
	isEmpty : function(v, allowBlank){
		return v === null || v === undefined || ((Ext.isArray(v) && !v.length)) || (!allowBlank ? v === '' : false);
	},

	/**
	 * Returns true if the passed value is a JavaScript array, otherwise false.
	 * @param {Mixed} value The value to test
	 * @return {Boolean}
	 */
	isArray : function(v){
		return Object.prototype.toString.apply(v) === '[object Array]';
	},

	/**
	 * Returns true if the passed object is a JavaScript date object, otherwise false.
	 * @param {Object} object The object to test
	 * @return {Boolean}
	 */
	isDate : function(v){
		return Object.prototype.toString.apply(v) === '[object Date]';
	},

	/**
	 * Returns true if the passed value is a JavaScript Object, otherwise false.
	 * @param {Mixed} value The value to test
	 * @return {Boolean}
	 */
	isObject : function(v){
		return !!v && Object.prototype.toString.call(v) === '[object Object]';
	},

	/**
	 * Returns true if the passed value is a JavaScript 'primitive', a string, number or boolean.
	 * @param {Mixed} value The value to test
	 * @return {Boolean}
	 */
	isPrimitive : function(v){
		return Ext.isString(v) || Ext.isNumber(v) || Ext.isBoolean(v);
	},

	/**
	 * Returns true if the passed value is a JavaScript Function, otherwise false.
	 * @param {Mixed} value The value to test
	 * @return {Boolean}
	 */
	isFunction : function(v){
		return Object.prototype.toString.apply(v) === '[object Function]';
	},

	/**
	 * Returns true if the passed value is a number. Returns false for non-finite numbers.
	 * @param {Mixed} value The value to test
	 * @return {Boolean}
	 */
	isNumber : function(v){
		return typeof v === 'number' && isFinite(v);
	},

	/**
	 * Returns true if the passed value is a string.
	 * @param {Mixed} value The value to test
	 * @return {Boolean}
	 */
	isString : function(v){
		return typeof v === 'string';
	},

	/**
	 * Returns true if the passed value is a boolean.
	 * @param {Mixed} value The value to test
	 * @return {Boolean}
	 */
	isBoolean : function(v){
		return typeof v === 'boolean';
	},

	/**
	 * Returns true if the passed value is an HTMLElement
	 * @param {Mixed} value The value to test
	 * @return {Boolean}
	 */
	isElement : function(v) {
		return !!v && v.tagName;
	},

	/**
	 * Returns true if the passed value is not undefined.
	 * @param {Mixed} value The value to test
	 * @return {Boolean}
	 */
	isDefined : function(v){
		return typeof v !== 'undefined';
	},
	 /**
	 * 将任何可迭代(带有length属性)的对象转换成Array对象
	 * 不要用它转换字符串.IE doesn't support "abc"[0] which this implementation depends on.
	 * For strings, use this instead: "abc".match(/./g) => [a,b,c];
	 * @param {Iterable} the iterable object to be turned into a true Array.
	 * @return (Array) array
	 */
	 toArray : function(){
		 return JS.isIE ?
			 function(a, i, j, res){
				 res = [];
				 for(var x = 0, len = a.length; x < len; x++) {
					 res.push(a[x]);
				 }
				 return res.slice(i || 0, j || res.length);
			 } :
			 function(a, i, j){
				 return Array.prototype.slice.call(a, i || 0, j || a.length);
			 };
	 }()
};
JS.ns = JS.Syntax.nameSpace;
JS.apply = JS.Syntax.apply;
JS.override = JS.Syntax.override;
JS.extend = JS.Syntax.extend;
JS.callBack = JS.Syntax.callBack;
JS.isEmpty = JS.Syntax.isEmpty;
JS.isArray = JS.Syntax.isArray;
JS.isDate = JS.Syntax.isDate;
JS.isObject = JS.Syntax.isObject;
JS.isPrimitive = JS.Syntax.isPrimitive;
JS.isFunction = JS.Syntax.isFunction;
JS.isNumber = JS.Syntax.isNumber;
JS.isString = JS.Syntax.isString;
JS.isBoolean = JS.Syntax.isBoolean;
JS.isElement = JS.Syntax.isElement;
JS.isDefined = JS.Syntax.isDefined;
JS.toArray = JS.Syntax.toArray;

JS.DomEvent = {
    on: function(el, name, fun, scope){
        if (el.addEventListener) {
        	//el.addEventListener(name, fun, false);
        	//TODO:若封闭成带有作用域的功能，如何删除事件，是否自己要记一下on的函数，然后在un下删除它
        	
			el.addEventListener(name, function(){
				JS.callBack(fun,scope,arguments);
			}, false);
			
		} else {
			//el.attachEvent('on' + name, fun);
			
			el.attachEvent('on' + name, function(){
				JS.callBack(fun,scope,arguments);
			});
			
		}
    },
    un: function(el, name, fun,scope){
        if (el.removeEventListener){
            el.removeEventListener(name, fun, false);
        } else {
            el.detachEvent('on' + name, fun);
        }
    },
    stop: function(e){
		e.returnValue = false;
		if (e.preventDefault) {
			e.preventDefault();
		}
		JS.DomEvent.stopPropagation(e);
    },
    stopPropagation: function(e){
        e.cancelBubble = true;
		if (e.stopPropagation) {
			e.stopPropagation();
		}
    }
};
JS.on = JS.DomEvent.on;
JS.un = JS.DomEvent.un;
/**
 * copy from the Ext JS Library 3.3.0
 * @class Ext.util.DelayedTask
 * <p> The DelayedTask class provides a convenient way to "buffer" the execution of a method,
 * performing setTimeout where a new timeout cancels the old timeout. When called, the
 * task will wait the specified time period before executing. If durng that time period,
 * the task is called again, the original call will be cancelled. This continues so that
 * the function is only called a single time for each iteration.</p>
 * <p>This method is especially useful for things like detecting whether a user has finished
 * typing in a text field. An example would be performing validation on a keypress. You can
 * use this class to buffer the keypress events for a certain number of milliseconds, and
 * perform only if they stop for that amount of time.  Usage:</p><pre><code>
var task = new Ext.util.DelayedTask(function(){
    alert(Ext.getDom('myInputField').value.length);
});
// Wait 500ms before calling our function. If the user presses another key 
// during that 500ms, it will be cancelled and we'll wait another 500ms.
Ext.get('myInputField').on('keypress', function(){
    task.{@link #delay}(500); 
});
 * </code></pre> 
 * <p>Note that we are using a DelayedTask here to illustrate a point. The configuration
 * option <tt>buffer</tt> for {@link Ext.util.Observable#addListener addListener/on} will
 * also setup a delayed task for you to buffer events.</p> 
 * @constructor The parameters to this constructor serve as defaults and are not required.
 * @param {Function} fn (optional) The default function to call.
 * @param {Object} scope The default scope (The <code><b>this</b></code> reference) in which the
 * function is called. If not specified, <code>this</code> will refer to the browser window.
 * @param {Array} args (optional) The default Array of arguments.
 */
JS.DelayedTask = function(fn, scope, args){
    var me = this,
    	id,    	
    	call = function(){
    		clearInterval(id);
	        id = null;
	        fn.apply(scope, args || []);
	    };
	    
    /**
     * Cancels any pending timeout and queues a new one
     * @param {Number} delay The milliseconds to delay
     * @param {Function} newFn (optional) Overrides function passed to constructor
     * @param {Object} newScope (optional) Overrides scope passed to constructor. Remember that if no scope
     * is specified, <code>this</code> will refer to the browser window.
     * @param {Array} newArgs (optional) Overrides args passed to constructor
     */
    me.delay = function(delay, newFn, newScope, newArgs){
        me.cancel();
        fn = newFn || fn;
        scope = newScope || scope;
        args = newArgs || args;
        id = setInterval(call, delay);
    };

    /**
     * Cancel the last queued timeout
     */
    me.cancel = function(){
        if(id){
            clearInterval(id);
            id = null;
        }
    };
};