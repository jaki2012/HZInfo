/*
 * 通用JS方法
 * 为便于查看，将方法的定义写在上方，实现写在下方。
 */
(function() {
	var hzcommon = hzcommon || {};
	window.hzcommon = hzcommon;
	/*	
	 * 用途：将时间戳转为时间字符串,带毫秒
	 * 说明：
	 * 返回：格式如2012-02-20 13:30:30.123
	 * 引用：
	 * 作者：hx
	*/
	hzcommon.timeStampToStringMil = function(ns){};
	/*	
	 * 用途：将时间戳转为时间字符串,
	 * 说明：
	 * 返回：格式如2012-02-20 13:30:30 
	 * 引用：
	 * 作者：hx
	*/
	hzcommon.timeStampToString = function(ns){};
	/*	
	 * 用途：显示浮动警告窗
	 * 说明：type可以为info或danger
	 * 返回： 
	 * 引用：
	 * bootstrap-notify/animate.css
	 * bootstrap-notify/bootstrap-notify.min.js
	 * 作者：hx
	*/
	hzcommon.showMsg = function(type, data){};
	/*	
	 * 用途：获取全局的key值
	 * 说明：
	 * 返回： 
	 * 引用：
	 * 作者：hx
	*/
	hzcommon.getKey = function(key){};
	/*	
	 * 用途：设置全局的key值
	 * 说明：
	 * 返回： 
	 * 引用：
	 * 作者：hx
	*/
	hzcommon.setKey = function(key){};
	/*
	 * 用途：cookie的设置
	 * 说明：
	 * 返回：
	 * 引用：
	 * 作者：ypj
	 * */
	hzcommon.setCookie = function(name,value,days){};
	/*
	 * 用途：cookie的删除
	 * 说明：
	 * 返回：
	 * 引用：
	 * 作者：ypj
	 * */
	hzcommon.delCookie = function(name){};
	/*
	 * 用途：cookie的读取 
	 * 说明：
	 * 返回：
	 * 引用：
	 * 作者：ypj
	 * */
	hzcommon.getCookie = function(name){};
	/*============================实现===============================*/
	hzcommon.test = function(){return 1;};
	hzcommon.showMsg = function(type, data) {
		$.notify({
			// options
			icon: 'glyphicon glyphicon-info-sign',
			message: data 
		},{
			// settings
			type: type,
			placement: {
				from: "bottom",
				align: "right"
			},
			allow_dismiss: true,
			delay: 2000,
			animate: {
			enter: 'animated fadeInRight',
			exit: 'animated fadeOutRight'
			}
		});
	};
	function numToFullNum(num){
		var numstr = "";
		if (num < 10)
			numstr = "0" + num;
		else
			numstr = num;
		return numstr;
	}
	function numToFullNum2(num){
		var numstr = "";
		if (num < 10)
			numstr = "00" + num;
		else if (num < 100)
			numstr = "0" + num;
		else
			numstr = num;
		return numstr;
	}
	hzcommon.timeStampToStringMil = function(ns) {
		var myDate = new Date(parseInt(ns));
		myDate.getDate();       //获取当前日(1-31)
		myDate.getDay();        //获取当前星期X(0-6,0代表星期天)
		myDate.getTime();       //获取当前时间(从1970.1.1开始的毫秒数)
		myDate.getHours();      //获取当前小时数(0-23)
		myDate.getMinutes();    //获取当前分钟数(0-59)
		myDate.getSeconds();    //获取当前秒数(0-59)
		myDate.getMilliseconds();   //获取当前毫秒数(0-999)
		return myDate.getFullYear() + "-" +
			numToFullNum(myDate.getMonth() + 1) + "-" + 
			numToFullNum(myDate.getDate()) + " " + 
			numToFullNum(myDate.getHours()) + ":" + 
			numToFullNum(myDate.getMinutes()) + ":" + 
			numToFullNum(myDate.getSeconds()) + "." + 
			numToFullNum2(myDate.getMilliseconds());
	};
	hzcommon.timeStampToString = function(ns) {
		var myDate = new Date(parseInt(ns));
		myDate.getDate();       //获取当前日(1-31)
		myDate.getDay();        //获取当前星期X(0-6,0代表星期天)
		myDate.getTime();       //获取当前时间(从1970.1.1开始的毫秒数)
		myDate.getHours();      //获取当前小时数(0-23)
		myDate.getMinutes();    //获取当前分钟数(0-59)
		myDate.getSeconds();    //获取当前秒数(0-59)
		myDate.getMilliseconds();   //获取当前毫秒数(0-999)
		return myDate.getFullYear() + "-" +
			numToFullNum(myDate.getMonth() + 1) + "-" + 
			numToFullNum(myDate.getDate()) + " " + 
			numToFullNum(myDate.getHours()) + ":" + 
			numToFullNum(myDate.getMinutes()) + ":" + 
			numToFullNum(myDate.getSeconds());
	};
	hzcommon.getKey = function(key){
		return top.parameter.get(key);
	};
	/*	
	 * 用途：设置全局的key值
	 * 说明：
	 * 返回： 
	 * 引用：
	 * 作者：hx
	*/
	hzcommon.setKey = function(key){
		top.parameter.put(key);
	};
	hzcommon.setCookie = function(name,value,days){		
		var exp = new Date();
		exp.setDate(exp.getDate()+days);
		document.cookie = name + "=" + escape(value) + ((days==null)?"":";expires="+exp.toGMTString());		
	};
	hzcommon.delCookie = function(name){
		var exp = new Date();  
   	    exp.setTime(exp.getTime() - 1);  
   	    var cval=getCookie(name);  
   	    if(cval!=null)  
   	        document.cookie= name + "="+cval+";expires="+exp.toGMTString();  
	};
	hzcommon.getCookie = function(name){
		if (document.cookie.length>0){
			var c_start=document.cookie.indexOf(name + "=");
			if (c_start!=-1){ 
				c_start=c_start + name.length+1;
				var c_end=document.cookie.indexOf(";",c_start);
				if (c_end==-1) 
  				c_end=document.cookie.length;
				return unescape(document.cookie.substring(c_start,c_end));
			}
		}
		return "";
	};
})();