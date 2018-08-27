搞定跨域请求问题: JSONP & CORS

## **什么是跨域问题**

同源策略指的是**域名（或IP）**，**协议**，**端口**都相同，不同源的客户端脚本在没明确授权的情况下，不能读写对方的资源。

比如：http://www.astali.com/index.html 这个网站，协议是http，域名是www.astali.com，端口是80(默认端口)，它的同源情况如下：

- http://www.astali.com/other.html：同源
- http://astali.com/other.html：不同源（域名不同） 
- https://www.astali.com/other.html：不同源（协议不同）
- http://www.astali.com:81/other.html：不同源（端口不同）

同源策略的目的是为了保证用户信息安全，防止恶意的网站窃取数据。

## **跨域异常提示**

```
XMLHttpRequest cannot load http://www.astali.com/index. No 'Access-Control-Allow-Origin' header is present on the requested resource. Origin 'null' is therefore not allowed access. The response had HTTP status code 404.
```

看到Access-Control-Allow-Origin，第一个就要想到跨域问题。

## **跨域解决方式：JSONP & CORS**

## JSONP

jsonp 的原理很简单，利用了【前端请求静态资源的时候不存在跨域问题】这个思路。

但是 **只支持 get，只支持 get，只支持 get**。就算写成post,也会转为get

注意一点，既然这个方法叫 jsonp，后端数据一定要使用 json 数据，不能随便的搞个字符串什么的，不然你会觉得结果莫名其妙的。

### 前端 jQuery 写法

```
$.ajax({
  type: "get",
  url: baseUrl + "/jsonp/get",
  dataType: "jsonp",
  jsonp: "callback",//传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
  jsonpCallback: "xx", //返回后调用的处理函数
  success: function(response) {
    $("#response").val(JSON.stringify(response));
  }
});
```

> dataType: "jsonp"。除了这个，其他配置和普通的请求是一样的。

### 后端 SpringMVC 配置

如果你也使用 SpringMVC，那么配置一个 jsonp 的 Advice 就可以了，这样我们写的每一个 Controller 方法就完全不需要考虑客户端到底是不是 jsonp 请求了，Spring 会自动做相应的处理。

```
@ControllerAdvice
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
    public JsonpAdvice(){
        // 这样如果请求中带 callback 参数，Spring 就知道这个是 jsonp 的请求了
        super("callback");
    }
}
```

*以上写法要求 \**SpringMVC 版本不低于 3.2**，低于 3.2 的我只能说，你们该升级了。*

### 后端Servlet配置

有些项目还有用的原生Servlet，偷懒一下，这里贴个伪代码吧，在我们的方法返回前端之前调一下 returnData方法：

```java
public Object returnData(HttpServletRequest request){
	//默认是callback，如果需配置可以从前端改
    String callback = request.getParameter("callback");
    if(StringUtils.isBlank(callback)){
        return result;
    } else {
        return callback+"("+JSON.toJSONString(result)+")";
    }
}
```

## CORS

CORS是一个W3C标准，全称是"跨域资源共享"（Cross-origin resource sharing）。
它允许浏览器向跨源服务器，发出XMLHttpRequest请求，从而克服了AJAX只能同源使用的限制。

CORS需要浏览器和服务器同时支持。

1. 所有浏览器都支持该功能，IE浏览器不能低于IE10。
   **整个CORS通信过程，都是浏览器自动完成，不需要用户参与。** 对于开发者来说，CORS通信与同源的AJAX通信没有差别，代码完全一样。浏览器一旦发现AJAX请求跨源，就会自动添加一些附加的头信息，有时还会多出一次附加的请求，但用户不会有感觉。
2. 实现CORS通信的关键是服务器。只要服务器实现了CORS接口，就可以跨源通信。

**即CORS与普通请求代码一样。**

CORS与JSONP相比

1. **JSONP只能实现GET请求，而CORS支持所有类型的HTTP请求。**
2. 使用CORS，开发者可以使用普通的XMLHttpRequest发起请求和获得数据，比起JSONP有更好的错误处理。
3. JSONP主要被老的浏览器支持，它们往往不支持CORS，而绝大多数现代浏览器都已经支持了CORS。

### 前端 jQuery 写法

直接看代码吧：

```
$.ajax({
    type: "POST",
    url: baseUrl + "/jsonp/post",
    dataType: 'json',
    crossDomain: true,
    xhrFields: {
        withCredentials: true
    },
    data: {
        name: "name_from_frontend"
    },
    success: function (response) {
        console.log(response)// 返回的 json 数据
        $("#response").val(JSON.stringify(response));
    }
});
```

> dataType: "json"，这里是 json，不是 jsonp，不是 jsonp，不是 jsonp。
>
> crossDomain: true，这里代表使用跨域请求
>
> xhrFields: {withCredentials: true}，这样配置就可以把 cookie 带过去了，不然我们连 session 都没法维护，很多人都栽在这里。当然，如果你没有这个需求，也就不需要配置这个了。

### 后端 SpringMVC 配置

对于大部分的 web 项目，一般都会有 mvc 相关的配置类，此类继承自 WebMvcConfigurerAdapter。如果你也使用 SpringMVC 4.2 以上的版本的话，直接像下面这样添加这个方法就可以了：

```
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**/*").allowedOrigins("*");
    }
}
```

如果很不幸你的项目中 SpringMVC 版本低于 4.2，那么需要「曲线救国」一下：

```
public class CrossDomainFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");// 如果提示 * 不行，请往下看
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        filterChain.doFilter(request, response);
    }
}
```

在 web.xml 中配置下 filter：

```java
<filter>
    <filter-name>CrossDomainFilter</filter-name>
    <filter-class>com.astali.filters.CrossDomainFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>CrossDomainFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

注意了，我说的是很笼统的配置，对于大部分项目是可以这么笼统地配置的。文中类似 “*” 这种配置读者应该都能知道怎么配。

如果读者发现浏览器提示不能用 ‘*’ 符号，那读者可以在上面的 filter 中根据 request 对象拿到请求头中的 referer（request.getHeader("referer")），然后动态地设置 "Access-Control-Allow-Origin"：

```
String referer = request.getHeader("referer");
if (StringUtils.isNotBlank(referer)) {
    URL url = new URL(referer);
    String origin = url.getProtocol() + "://" + url.getHost();
    response.addHeader("Access-Control-Allow-Origin", origin);
} else {
    response.addHeader("Access-Control-Allow-Origin", "*");
}
```

> 终于知道为什么有时候会提示我们 * 不支持了，原来是只要前端写了 withCredentials: true 那么浏览器就会提示这个，一种办法就是这里说的使用动态构造 origin 的方式，另一种办法就是跨域不传 cookie，让前端把 cookie 要传的信息(如 sessionId/accessKey) 放到 header 中或者直接写在 request 的参数里。

### 前端非 jQuery 写法

jQuery 一招鲜吃遍天的日子是彻底不在了，这里就说说如果不使用 jQuery 的话，怎么解决 post 跨域的问题。大部分的 js 库都会提供相应的方案的，大家直接找相应的文档看看就知道怎么用了。

来一段原生 js 介绍下：

```
function createCORSRequest(method, url) {
    var xhr = new XMLHttpRequest();
    if ("withCredentials" in xhr) {
        // 如果有 withCredentials 这个属性，那么可以肯定是 XMLHTTPRequest2 对象。看第三个参数
        xhr.open(method, url, true);
    } else if (typeof XDomainRequest != "undefined") {
        // 此对象是 IE 用来跨域请求的
        xhr = new XDomainRequest();
        xhr.open(method, url);
    } else {
        // 如果是这样，很不幸，浏览器不支持 CORS
        xhr = null;
    }
    return xhr;
}

var xhr = createCORSRequest('GET', url);
if (!xhr) {
    throw new Error('CORS not supported');
}
```

其中，Chrome，Firefox，Opera，Safari 这些「程序员友好」的浏览器使用的是 XMLHTTPRequest2 对象。IE 使用的是 XDomainRequest。