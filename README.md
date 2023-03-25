<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">sms-aggregation v1.0.0</h1>
<h2 align="center" style="margin: 30px 0 30px; font-weight: bold;">短信聚合工具</h2>
<h4 align="center" style="margin: 30px 0 30px; font-weight: bold;">让发送短信变动更简单</h4>
<p align="center">
<a href="https://gitee.com/the-wind-is-like-a-song/sms_aggregation/stargazers"><img src="https://gitee.com/the-wind-is-like-a-song/sms_aggregation/badge/star.svg?theme=gvp"></a>
<a href="https://gitee.com/the-wind-is-like-a-song/sms_aggregation/master/LICENSE"><img src="https://img.shields.io/badge/license-Apache--2.0-green"></a>
<a href="https://gitee.com/the-wind-is-like-a-song/sms_aggregation"><img src="https://img.shields.io/badge/version-v1.0.0-blue"></a>
</p>  

## 前言
在日常的开发过程中，短信的发送经常使用（尤其是中小型的外包公司），毕竟不是每个公司都有阿里腾讯一样的实力，
也不是每个都像银行联通等公司一样有内部的短信规程。第三方的短信往往是最常见的解决方案，但是市面上第三方短信服务商众多，
各家都有不同的方式和标准，每次需要使用时候，都需要花费时间去阅读文档和编写相应的工具，为一个短信浪费了太多的精力和时间。
这个工具的目的就是为了统一下各个厂商的短信发送工具的标准，甚至于更换短信厂商只需要更改yml配置文件即可。  
新人上路，还望各位大佬多多支持，如果你觉得还算值得鼓励，请用你发财的小手帮助点上一个start  
[gitee](https://gitee.com/the-wind-is-like-a-song/sms_aggregation)
[github](https://github.com/fengruge/sms_aggregation)

#### [在线文档](https://apidoc.gitee.com/the-wind-is-like-a-song/sms_aggregation)

## 支持厂商一览
目前刚刚发布第一版本，支持尚少，后续会集成更多的厂商
- **阿里云国内短信**
- **腾讯云国内短信**
- **合一短信**
- **云片短信**

## 在SpringBoot环境集成
1. maven引入
```xml
<dependency>
    <groupId>kim.wind</groupId>
    <artifactId>sms-aggregation-spring-boot-starter</artifactId>
    <version> version </version>
</dependency>
```
2. 设置配置文件
```yaml
sms:
  # 短信服务商 
  supplier: alibaba
  # 是否开启短信发送限制 默认false
  restricted: true
  # 以下设置仅在开启短信发送限制后生效
  # 是否使用redis进行缓存 默认false
  redisCache: true
  # 单账号每日最大发送量
  accountMax: 20
  # 单账号每分钟最大发送
  minuteMax: 2
    
```
阿里云配置示意
```yaml
sms:
  # 短信服务商
  supplier: alibaba
  alibaba:
    #阿里云的accessKey
    accessKeyId: 您的accessKey
    #阿里云的accessKeySecret
    accessKeySecret: 您的accessKeySecret
    #短信签名
    signature: 测试签名
    #模板ID 用于发送固定模板短信使用
    templateId: SMS_215125134
    #模板变量 上述模板的变量
    templateName: code
    #请求地址 默认为dysmsapi.aliyuncs.com 如无特殊改变可以不用设置
    requestUrl: dysmsapi.aliyuncs.com
```
3. 方法使用
```java
public class Demo{
    //此处作为演示使用，推荐使用构造注入或set注入
    @Autowired
    private final SmsBlend sms;
    
    public void test() {
        //发送固定模板短信
        SmsResponse smsResponse = sms.sendMessage("18888888888","测试固定模板短信");
        System.out.println(smsResponse);
    }
    
}
```

## 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request
