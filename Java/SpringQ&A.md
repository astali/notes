### 1.Redis 不传密码异常解决
```
 <constructor-arg name="password"  value="#{'${redisPassword}'!=''?'${redisPassword}':null}"/>
```
###2.Application Context获取的几种方式
```
1.直接注入
@Resource
private ApplicationContext ctx;
2.实现ApplicationContextAware接口
3.使用WebApplicationContextUtils
ApplicationContext ctx=WebApplicationUtils.getWebApplicationContext(request.getSession().getServletContext());
4.从当前线程绑定获取（spring boot不支持）
ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
```
