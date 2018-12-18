获取链接?后的参数 `window.location.search.substr(1)`

获取radio选择值： ` $('input:radio:checked').val()`
获取单选框选择值： `$("#rememberMe").is(':checked')`

正则验证第一位是数字

```
var tempreg = /^([A-Za-z]{1,1})[A-Za-z0-9_]{3,25}$/;
tempreg.test(strValue)
```

 var obj = eval('(' + str + ')');  //由JSON字符串转换为JSON对象
 var obj = str.parseJSON(); //由JSON字符串转换为JSON对象
 var obj = JSON.parse(str); //由JSON字符串转换为JSON对象
var json = JSON.stringify(ob); //json对象转化为string

- `JSON.stringify() `方法可以将任意的 JavaScript 值序列化成 JSON 字符串

input 标签的监听事件总结
最近在写一个手机端提交表单的项目，里面用了不少input标签，因为项目不太忙，所以，想做的完美点，但是遇到了一些问题，比如：页面中的必填项如果有至少一项为空，提交按钮就是不能提交的状态，所以需要对所有的input内容进行监听，刚开始我用了jquery的keyup事件解决问题，但是后来测试出一个bug，如果用户选择粘贴复制的话，keyup事件不能触发，也就不能通过判断input内容来改变提交按钮的状态。下面就这种问题做下总结，希望对自己和他人以后能有点帮助。

1.onfocus  当input 获取到焦点时触发

2.onblur  当input失去焦点时触发，注意：这个事件触发的前提是已经获取了焦点再失去焦点的时候会触发相应的js

3.onchange 当input失去焦点并且它的value值发生变化时触发

4.onkeydown 在 input中有键按住的时候执行一些代码

5.onkeyup 在input中有键抬起的时候触发的事件，在此事件触发之前一定触发了onkeydown事件

6.onclick  主要是用于 input type=button，当被点击时触发此事件

7.onselect  当input里的内容文本被选中后执行一段，只要选择了就会触发，不是非得全部选中

8.oninput  当input的value值发生变化时就会触发，不用等到失去焦点（与onchange的区别）

以上事件可以直接放到input的属性里，例如：<input type="text" onfocus="a();" onblur="b()" onchange="c();" onkeydown="d();" />，也可以通过js给input dom元素添加相应的事件，如：document.getElementByTagName('input').onfocus = function();