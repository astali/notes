http://map.yanue.net/  根据经纬度查地址



https://blog.csdn.net/u011001084/article/details/76067119  Redis深入之道：原理解析、场景使用以及视频解读



//根据百度提供坐标API 免费申请 PL6XXXXz

```
Map<String, Double> getLngLat(String address) {
    String url = "http://api.map.baidu.com/geocoder/v2/?address=" + address + "&output=json&ak=百度密钥"; //GET请求;
    System.out.println(CreateWebRequest.GetResponseByGetMethod(url, ""));
    String json = CreateWebRequest.GetResponseByGetMethod(url, "");

    JSONObject jsonObject = JSONObject.fromObject(json);
    JSONObject resultJson = jsonObject.getJSONObject("result");
    JSONObject jsonObject1 = JSONObject.fromObject(resultJson);
    JSONObject locationJson = jsonObject1.getJSONObject("location");
    Map map = new HashMap();
    String lng = locationJson.get("lng").toString();
    String lat = locationJson.get("lat").toString();
    map.put("lng",Double.parseDouble(lng));
    map.put("lat",Double.parseDouble(lat));
    return map;
}
```

Redis  GEO 实例

| 命令                                                         | 描述                                                      |
| ------------------------------------------------------------ | --------------------------------------------------------- |
| [Redis GEOADD 命令](http://www.redis.net.cn/order/3685.html) | 将指定的地理空间位置（纬度、经度、名称）添加到指定的key中 |
| [Redis GEODIST 命令](http://www.redis.net.cn/order/3686.html) | 返回两个给定位置之间的距离                                |
| [Redis GEOHASH 命令](http://www.redis.net.cn/order/3687.html) | 返回一个或多个位置元素的 Geohash 表示                     |
| [Redis GEOPOS 命令](http://www.redis.net.cn/order/3688.html) | 从key里返回所有给定位置元素的位置（经度和纬度）           |
| [Redis GEORADIUS 命令](http://www.redis.net.cn/order/3689.html) | 以给定的经纬度为中心， 找出某一半径内的元素               |
| [Redis GEORADIUSBYMEMBER 命令](http://www.redis.net.cn/order/3690.html) | 找出位于指定范围内的元素，中心点是由给定的位置元素决定    |

```
String addr1 = "上海浦东软件园";
String addr2 = "上海天地软件园";
String addr3 = "上海陆家嘴软件园";
String addr4 = "上海长江软件园";
String addr5 = "上海金蝶软件园"; //最终定位到南京金蝶软件园
String addr6 = "上海金桥软件园";
String addr7 = "北京天安门广场";
String addr8 = "湖南衡山县贯塘乡";

Map map = getLngLat(addr1);
System.out.println(map.get("lng"));
System.out.println(map.get("lat"));
JedisUtil.geoadd("geotest", 3,(Double)map.get("lng"),(Double)map.get("lat"),addr1);
System.out.println();
List<GeoRadiusResponse> list2 = JedisUtil.georadius("geotest",3,(Double) map.get("lng"),(Double)map.get("lat"),10);
for (int i = 0, len = list2.size(); i < len; i++) {
    System.out.println(list2.get(i).getMemberByString());
}
map = getLngLat(addr2);
JedisUtil.geoadd("geotest", 3,(Double)map.get("lng"),(Double)map.get("lat"),addr2);
map = getLngLat(addr3);
JedisUtil.geoadd("geotest", 3,(Double)map.get("lng"),(Double)map.get("lat"),addr3);
map = getLngLat(addr4);
JedisUtil.geoadd("geotest", 3,(Double)map.get("lng"),(Double)map.get("lat"),addr4);
map = getLngLat(addr5);
JedisUtil.geoadd("geotest", 3,(Double)map.get("lng"),(Double)map.get("lat"),addr5);
map = getLngLat(addr6);
JedisUtil.geoadd("geotest", 3,(Double)map.get("lng"),(Double)map.get("lat"),addr6);
map = getLngLat(addr7);
JedisUtil.geoadd("geotest", 3,(Double)map.get("lng"),(Double)map.get("lat"),addr7);
map = getLngLat(addr8);
JedisUtil.geoadd("geotest", 3,(Double)map.get("lng"),(Double)map.get("lat"),addr8);

System.out.println(JedisUtil.geodist("geotest", 3,addr1,addr7));
System.out.println(JedisUtil.geodist("geotest", 3,addr1,addr8));
System.out.println(JedisUtil.geopos("geotest",3,"上海浦东软件园"));

List<GeoRadiusResponse> list1 = JedisUtil.georadiusByMember("geotest", 3, addr1, 50);
for (int i = 0, len = list1.size(); i < len; i++) {
    System.out.println(list1.get(i).getMemberByString());
}
System.out.println(JedisUtil.georadiusByMember("geotest", 3, addr1, 50).size());
System.out.println(JedisUtil.georadiusByMember("geotest", 3,addr2,10).size());
System.out.println(JedisUtil.georadiusByMember("geotest", 3,addr3,3).size());
System.out.println(JedisUtil.georadiusByMember("geotest", 3,addr4,2).size());
System.out.println(JedisUtil.georadiusByMember("geotest", 3,addr5,1).size());
System.out.println(JedisUtil.georadiusByMember("geotest", 3,addr6,5).size());
System.out.println(JedisUtil.geohash("geotest", 3,addr6));
```