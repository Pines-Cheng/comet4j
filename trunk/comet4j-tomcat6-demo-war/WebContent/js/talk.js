var statebar,
	toolbar,
	logbox,
	inputbox,
	lastTalkId;
function windowResize(){
	var offset = 2;
	var other = statebar.offsetHeight + toolbar.offsetHeight + offset;
	logbox.style.height = document.documentElement.clientHeight-other+'px';
}
function init(){
	statebar = document.getElementById("statebar");
	toolbar = document.getElementById("toolbar");
	logbox = document.getElementById("logbox");
	inputbox = document.getElementById("inputbox");
	windowResize();
	window.onresize = windowResize;
	//引擎事件绑定
	JS.Engine.on({
		start : function(cId, aml, engine){
			
		},
		stop : function(cause, url, cId, engine){
			
		},
		talker : function(data, timespan, engine){
			switch(data.type)
			{
				case 'rename': //改名
					onRename(data,timespan);
					break;
				case 'talk': //收到聊天消息
					onMessage(data,timespan);
					break;
				case 'up': //上线
					onJoin(data,timespan);
					break;
				case 'down': //下线
					onLeft(data,timespan);
					break;
				default :
			}
		}
	});
	JS.Engine.start('conn');
	inputbox.focus();
}
//日期格式化
function dateFormat(date){
	return [date.getHours(),':',date.getMinutes(),].join('');
}
//用户改名通知
function onRename(data,timespan){
	var id = data.id;
	var newName = data.newName;
	var oldName = data.oldName;
	var t = dateFormat(new Date(timespan));
	var str = ['<div class="sysmessage">',
	           t,
	           '&emsp;【',
	           oldName,
	           '】改名为【',
	           newName,
	           '】</div>'];
	logbox.innerHTML += str.join('');
	lastTalkId = id;
}
//用户聊天通知
function onMessage(data,timespan){
	var id = data.id;
	var name = data.name;
	var text = data.text.HTMLEncode();
	var t = dateFormat(new Date(timespan));
	var str ;
	if(lastTalkId == id){
		str =  	['<div class="usermessage">',
				'<blockquote>',
				text,
				'</blockquote>',
				'</div>'];
	}else{
		str =  	['<div class="usermessage">',
		       	t,
				'&emsp;<span class="user">【',
				name,
				'】</span><blockquote>',
				text,
				'</blockquote>',
				'</div>'];
	}
	logbox.innerHTML += str.join('');
	lastTalkId = id;
}
//用户上线通知
function onJoin(data,timespan){
	var id = data.id;
	var name = data.name;
	var t = dateFormat(new Date(timespan));
	var str = ['<div class="sysmessage">',
	           t,
	           '&emsp;【',
	           name,
	           '】来了，欢迎体验 <a href="http://code.google.com/p/comet4j/" target="_new">Comet For Java</a>',
	           '</div>'];
	logbox.innerHTML += str.join('');
	lastTalkId = id;
}
//用户下线通知
function onLeft(data,timespan){
	var id = data.id;
	var name = data.name;
	var t = dateFormat(new Date(timespan));
	var str = ['<div class="sysmessage">',
	           t,
	           '&emsp;【',
	           name,
	           '】离开了',
	           '</div>'];
	logbox.innerHTML += str.join('');
	lastTalkId = id;
}
//发送聊天信息动作
function send(){
	if(!JS.Engine.running)return;
	var text = inputbox.value;
	var id = JS.Engine.getId();
	var param = "id="+id+'&text='+encodeURIComponent(text);
	JS.AJAX.post('talk.do?cmd=talk',param);
	inputbox.value = '';
}
//改名动作
function rename(data){
	var userName = prompt("请输入你的姓名", getCookie('userName'));
	var fromId = JS.Engine.getId();
	if(!fromId) return;
	var param = "id="+fromId+'&newName='+encodeURIComponent(userName) ;
	setCookie('userName',userName,365);
	JS.AJAX.post('talk.do?cmd=rename', param);
}
//回车事件
function onSendBoxEnter(event){
	if(event.keyCode==13){ 
		send();
		return false;
	} 
}
//设置Cookie
function setCookie(name,value,expireDay) {
	var exp  = new Date();
	exp.setTime(exp.getTime() + expireDay*24*60*60*1000);
	document.cookie = name + "="+ encodeURIComponent(value) + ";expires=" + exp.toGMTString();
}
//获得Cookie
function getCookie(name) {
	var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
	if(arr != null) return decodeURIComponent(arr[2]); return null;
}
//删除Cookie
function delCookie(name){
	var exp = new Date();
	exp.setTime(exp.getTime() - 1);
	var cval=getCookie(name);
	if(cval!=null) document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}
//HTML编码
String.prototype.HTMLEncode = function() { 
	var temp = document.createElement ("div"); 
	(temp.textContent != null) ? (temp.textContent = this) : (temp.innerText = this); 
	var output = temp.innerHTML; 
	temp = null; 
	return output; 
};
//HTML解码
String.prototype.HTMLDecode = function() { 
	var temp = document.createElement("div"); 
	temp.innerHTML = this; 
	var output = temp.innerText || temp.textContent; 
	temp = null; 
	return output; 
};