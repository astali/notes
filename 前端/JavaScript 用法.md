获取链接?后的参数 `window.location.search.substr(1)`

获取radio选择值： ` $('input:radio:checked').val()`
获取单选框选择值： `$("#rememberMe").is(':checked')`

正则验证第一位是数字

```
var tempreg = /^([A-Za-z]{1,1})[A-Za-z0-9_]{3,25}$/;
tempreg.test(strValue)
```