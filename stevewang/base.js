STK.register("historyM",function(d){if(typeof $CONFIG=="undefined"||$CONFIG.bigpipe!="true"){return}var g=d.core.util.browser,q=d.core.util.hideContainer;var o=[];var p,r=true,a=!!history.pushState,c=("onhashchange" in window)&&((typeof document.documentMode==="undefined")||document.documentMode==8);var k,m,s;if(!c&&(g.IE6||g.IE7)){(k=d.C("iframe")).style.display="none";q.appendChild(k);k=k.contentWindow;m=function(t){t=encodeURIComponent(t);k.document.open("text/html");k.document.write("<html><head></head><body onload=\"parent.STK&&parent.STK.historyM&&parent.STK.historyM.hashFrameChangeHash&&parent.STK.historyM.hashFrameChangeHash('"+t+"');\"></body></html>");k.document.close()};s=function(t){t=decodeURIComponent(t);if(!s.first){s.first=1;return}window.location.hash=t;if(t!="#!"+p){h(p=t.substr(2),true)}}}var h=function(t,u){m&&(!u)&&m("#!"+t);if(r){for(var v=0;v<o.length;v++){try{o[v](t)}catch(w){d.log(w)}}}r=true};var j=function(u){var t=d.parseURL(u||window.location);return{host:t.host,path:"/"+t.path,query:t.query&&("?"+t.query),hash:t.hash&&("#"+t.hash)}};var i=function(){var t=d.parseURL(window.location);if(/^!/.test(t.hash)){return d.parseURL("http://"+t.host+t.hash.substr(1))}return t};var l=function(t,v){if(!/^\//.test(t)){d.log("地址:("+t+") 不为正确的路径,请使用绝对路径; 例：/directory/page.php?params#hash");return}v=d.parseParam({callChange:true,callChangeOnSame:true},v);r=v.callChange;if(a){if(p==t){if(!v.callChangeOnSame){return}}else{history.pushState(null,null,p=t)}h(t);return}var u="#!"+t;if(p==t){if(v.callChangeOnSame){h(t)}}else{window.location.hash=u}};var n=function(x,u){if(!x){return}u=u||false;var t=i();var w=d.queryToJson(t.query);for(var v in x){if(x[v]===null){delete w[v]}else{w[v]=x[v]}}t.query=d.jsonToQuery(w);result=["/",t.path,(t.query=="")?"":"?",t.query,(t.hash=="")?"":"#",t.hash].join("");l(result,{callChange:u})};var b=function(t){t&&o.push(t);return e};p=window.location.toString().replace(/^http:\/\/.*?\//,"/").replace(/#$/,"");if(a){d.addEvent(window,"popstate",function(){setTimeout(function(){var t=j(),u=t.path+t.query+t.hash;if(p!=u){h(p=u)}})})}else{var f=function(){var u,t=j();if(/^#!\//.test(t.hash)){u=t.hash.substr(2)}else{u=t.path+t.query+t.hash}if(p!=u){h(p=u)}};if(c){d.addEvent(window,"hashchange",function(){f()})}else{setInterval(function(){f()},200)}}m&&m("#!"+p);var e={parseURL:i,getURL:j,pushState:l,setQuery:n,onpopstate:b,hashFrameChangeHash:s};return e});STK.register("bigpipeM",function(g){if(typeof $CONFIG=="undefined"||$CONFIG.bigpipe!="true"){return}var c;var a;var d=function(){d.timer=setTimeout(function(){g.core.util.hideContainer.removeChild(a);document.body.style.cursor="";if(c){c.style.cursor=""}d.timer=a=null})};var b=function(h){h=h.replace(/#.*$/,"");if(h.indexOf("?")!=-1){h=h+"&ajaxpagelet=1"}else{h=h+"?ajaxpagelet=1"}h+="&_t="+new Date().getTime();if(a){d.timer&&clearTimeout(d.timer);g.removeEvent(a,"load",d);g.core.util.hideContainer.removeChild(a)}a=g.C("iframe");a.src=h;a.style.display="none";g.core.util.hideContainer.appendChild(a);document.body.style.cursor="progress";c&&(c.style.cursor="progress");g.addEvent(a,"load",d)};var f=/img|span/;var e=function(){g.core.evt.addEvent(document,"click",function(n){n=g.fixEvent(n);var o=n.target,k=o.tagName.toLowerCase(),h=(k=="a");if(!h&&f.test(k)){for(var m=0;m<2;m++){if(!(o=o.parentNode)){return}if((k=o.tagName.toLowerCase())=="a"){h=true;break}}}if(h){if(o.getAttribute("bpfilter")&&!n.ctrlKey&&!n.shiftKey&&n.button==0){g.log("bigpipeM: 点击行为拦截成功，使用Bigpipe模式加载");var j=o.href;var l=g.historyM.getURL();var p=g.historyM.getURL(j);if(p.host!=l.host){return}c&&(c.style.cursor="");c=o;g.historyM.pushState(p.path+p.query+p.hash)}else{if(!/^#/.test(o.href)){return}}g.preventDefault(n)}})};g.historyM.onpopstate(function(h){g.pageletM.clear();g.scrollTo(document.body,{step:10});b(h)});e();return{ajaxpipeLoader:b}});