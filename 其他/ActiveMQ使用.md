1.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

2.connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

第一个参数代表是否缓存，如果第一个参数为true，那么第二个参数无效，

AUTO_ACKNOWLEDGE 意思是消息发送给接收端后，就自动确认，不管接收端是否收到

CLIENT_ACKNOWLEDGE  这个只能用在接收端，表示接收端是否成功接收到消息，成功接收到后消息离别就会自动删除这条消息，注意，你必须要提交确认message.acknowledge();

2、message = session.createTextMessage("测试发送的消息"+i);

createTextMessage：字符串形式的消息，还可以发送Map,以及对象，这里很简单，主要是不想打架错过这个地方。