获取链接?后的参数 `window.location.search.substr(1)`

获取radio选择值： ` $('input:radio:checked').val()`
获取单选框选择值： `$("#rememberMe").is(':checked')`
正则验证第一位是数字

```
var tempreg = /^([A-Za-z]{1,1})[A-Za-z0-9_]{3,25}$/;
tempreg.test(strValue)

 var obj = eval('(' + str + ')');  //由JSON字符串转换为JSON对象
 var obj = str.parseJSON(); //由JSON字符串转换为JSON对象
 var obj = JSON.parse(str); //由JSON字符串转换为JSON对象
var json = JSON.stringify(ob); //json对象转化为string
```
- `JSON.stringify() `方法可以将任意的 JavaScript 值序列化成 JSON 字符串

input 标签的监听事件总结
1.onfocus  当input 获取到焦点时触发
2.onblur  当input失去焦点时触发，注意：这个事件触发的前提是已经获取了焦点再失去焦点的时候会触发相应的js
3.onchange 当input失去焦点并且它的value值发生变化时触发
4.onkeydown 在 input中有键按住的时候执行一些代码
5.onkeyup 在input中有键抬起的时候触发的事件，在此事件触发之前一定触发了onkeydown事件
6.onclick  主要是用于 input type=button，当被点击时触发此事件
7.onselect  当input里的内容文本被选中后执行一段，只要选择了就会触发，不是非得全部选中
8.oninput  当input的value值发生变化时就会触发，不用等到失去焦点（与onchange的区别）
以上事件可以直接放到input的属性里，例如：<input type="text" onfocus="a();" onblur="b()" onchange="c();" onkeydown="d();" />，也可以通过js给input dom元素添加相应的事件，如：document.getElementByTagName('input').onfocus = function();

微信公众号关闭页面回到聊天窗口
```javascript
网上找到的解决方法，但是比较单一，没有处理掉我的问题
<script>
document.addEventListener('WeixinJSBridgeReady', function(){ WeixinJSBridge.call('closeWindow'); }, false);
或
setTimeout(function(){WeixinJSBridge.call('closeWindow');},2000);
</script>
针对个人问题，IOS手机和安卓手机都能关闭(两个的前后顺序不能调换)
<script>
setTimeout(function(){
  //这个可以关闭安卓系统的手机
  document.addEventListener('WeixinJSBridgeReady', function(){ WeixinJSBridge.call('closeWindow'); }, false);
  //这个可以关闭ios系统的手机
  WeixinJSBridge.call('closeWindow');
}, 1000)
</script>
```

为select动态添加option  设置选中
```javascript
  $.each(eData, function(gueIndex, eGuid) {
     document.getElementById("gameGuid").options.add(new Option(eGuid.GUIDName, eGuid.GameGUID));
                console.log("index_",gueIndex,gameguid ,eGuid.GameGUID)
                //设置选中
                if(eGuid.GameGUID == gameguid){
                    document.getElementById("gameGuid")[gueIndex+1].selected=true;
                }
            });
```
```