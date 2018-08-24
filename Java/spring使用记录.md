### 1.Redis 不传密码异常解决

```
 <constructor-arg name="password"  value="#{'${redisPassword}'!=''?'${redisPassword}':null}"/>
```

   


