/**
 * 简单的事件系统
 * 最后修改 张实唯2014.7.12
*/

//用于保存各事件的函数
var _events = {};

//检查是不是指定了重复次数
var testreg = /^[\d]*\|/;

//event接受一个字符串和一个函数。如果函数存在则将函数注册到事件，如果不存在则触发该事件。
//字符串以'-'开头表示取消该事件中绑定的函数f，若不指定f则全部取消
//字符串以数字+'|'开头表示该函数只执行多少次，之后自动解除绑定，若省略数字则默认为1
//发布事件时可带一参数(参数不能是函数，但可以用包含一个函数的对象来作为参数)。绑定的函数可以直接接受该参数。
//注意:如果要将某个可能将来会改变的函数添加入事件，请为其套一层壳，如:event('fuck',function(){varfunc();})，而不要直接用event('fuck',varfunc)
var event = function(s,f){
	if(typeof(s) == 'string' && s){
		if(s[0] == '-'){
			s=s.slice(1);
			if(s){
				if(!_events[s]){_events[s] = [];}
				if(typeof(f) == 'function'){
					event.unbind(s,f);
				}else if(typeof(f) == 'undefined'){
					event.unbindall(s);
				}
			}
			return;
		}else if(testreg.test(s)){
			a = s.split("|");
			if(a[1]){
				if(!_events[a[1]]){_events[a[1]] = [];}
				if(typeof(f) == 'function'){
					if(!a[0]){a[0] = '1';}
					return event.bind(a[1],f,parseInt(a[0]));
				}
			}
			return;
		}
		if(!_events[s]){_events[s] = [];}
		if(typeof(f) == 'function'){
			return event.bind(s,f,-1);
		} else {
			event.trigger(s,f);
		}
	}
};
//以下函数请勿直接调用
event.bind = function(s,f,t){
	_events[s].push({f:f,t:t});
	return f;
};
event.trigger = function(s,a){
	l = _events[s].length;
	for(i=0;i<l;i++){
		if(_events[s][i]){
			_events[s][i].f(a,_events[s][i].t-1);
			_events[s][i].t--;
			if(_events[s][i].t == 0)
			{
				if(event.unbind(s,_events[s][i].f))
				{
					i--;
					l--;
				}
			}
		}
	}
};
event.unbind = function(s,f){
	if(_events[s].length == 0){delete _events[s];return false;}
	l = _events[s].length;
	for(i=0;i<l;i++){
		if(_events[s][i].f == f){
			_events[s].splice(i,1);
			return true;
		}
	}
	return false;
};
event.unbindall = function(s){
	delete _events[s];
};
