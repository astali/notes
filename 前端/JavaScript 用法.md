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