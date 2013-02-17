Android-MosilWeb
================

Mosil Web Tools for Connect to Web API or Web Resource of Android Project

連線取得 Web API 以及資源用

##Documentation
[Android Library – MosilWeb 說明文件](http://blog.mosil.biz/2013/02/mosilweb-documentation/)


##Usage
```java
/**
 * Basic
 */
MosilWeb mosilWeb = new MosilWeb(context);
mosilWeb.setUrl("your host", "your path or method", ...);

//Use GET
mosilWeb.actGet("query string");

//Use POST
mosilWeb.actPost(ContentType, "post data");

//Use HTTP
mosilWeb.actHttp("post data", "query string");

//Use HTTPS
mosilWeb.actHttps("post data", "query string");

//Get Response
mosilWeb.getResponse();       //String
mosilWeb.getResponseStatus(); //int, Http Status 

/**
 * Static Method : Parser Data to Query String/JSON String
 */
//Query String
MosilWeb.parseToQueryString(Map<String, Object>);
//JSON String
MosilWeb.parseToJsonString(Map<String, Object>);
```


##License

Copyright (c) 2013 [Mosil](http://mosil.biz)

Licensed under [The MIT License (MIT)](http://opensource.org/licenses/MIT)

採用 [MIT 授權](http://opensource.org/licenses/MIT)
