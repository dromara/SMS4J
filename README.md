<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">sms4j v3.0.0</h1>
<h4 align="center" style="margin: 30px 0 30px; font-weight: bold;">sms4j -- 让发送短信变的更简单</h4>
<p align="center">
<a href="https://gitee.com/dromara/sms4j/stargazers"><img src="https://gitee.com/dromara/sms4j/badge/star.svg?theme=gvp"></a>
<a href="https://gitee.com/dromara/sms4j/blob/master/LICENSE"><img src="https://img.shields.io/badge/license-Apache--2.0-green"></a>
<a href="https://gitee.com/dromara/sms4j"><img src="https://img.shields.io/badge/version-v3.0.0-blue"></a>
</p>
<img src="/public/logo.png">

## 前言

在日常的开发过程中，短信的发送经常使用（尤其是中小型的外包公司），毕竟不是每个公司都有阿里腾讯一样的实力，
也不是每个都像银行联通等公司一样有内部的短信规程。第三方的短信往往是最常见的解决方案，但是市面上第三方短信服务商众多，
各家都有不同的方式和标准，每次需要使用时候，都需要花费时间去阅读文档和编写相应的工具，为一个短信浪费了太多的精力和时间。
这个工具的目的就是为了统一下各个厂商的短信发送工具的标准，甚至于更换短信厂商只需要更改yml配置文件即可。  
如果我们的项目对你产生了帮助，或者你觉得还算值得鼓励，请用你发财的小手帮助点上一个start  
[gitee](https://gitee.com/dromara/sms4j)
[github](https://github.com/dromara/sms4j)

#### [官方文档](http://wind.kim)
#### [JavaDoc文档](https://apidoc.gitee.com/dromara/sms4j/)

## 支持厂商一览
- **[亿美软通国内短信](https://www.emay.cn/article949.html)**
- **[阿里云国内短信](https://www.aliyun.com/product/sms)**
- **[腾讯云国内短信](https://cloud.tencent.com/product/sms)**
- **[华为云国内短信](https://www.huaweicloud.com/product/msgsms.html)**
- **[京东云国内短信](https://www.jdcloud.com/cn/products/text-message)**
- **[容联云国内短信（原云通讯）](https://www.yuntongxun.com/sms/note-inform)**
- **[网易云信短信](https://netease.im/sms)**
- **[天翼云短信](https://www.ctyun.cn/products/10020341)**
- **[合一短信](https://unisms.apistd.com/)**
- **[云片短信](https://www.yunpian.com/product/domestic-sms)**
- **[助通短信](https://www.ztinfo.cn/products/sms)**

## 在SpringBoot环境集成

1. maven引入
   
   ```xml
   <dependency>
    <groupId>org.dromara.sms4j</groupId>
    <artifactId>sms4j-spring-boot-starter</artifactId>
    <version>最新版本</version>
   </dependency>
   ```
2. 设置配置文件
   
```yaml
sms:
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
   huawei:
      #华为短信appKey
      appKey: 5N6fvXXXX920HaWhVXXXXXX7fYa
      #华为短信appSecret
      app-secret: Wujt7EYzZTBXXXXXXEhSP6XXXX
      #短信签名
      signature: 华为短信测试
      #通道号
      sender: 8823040504797
      #模板ID 如果使用自定义模板发送方法可不设定
      template-id: acXXXXXXXXc274b2a8263479b954c1ab5
      #华为回调地址，如不需要可不设置或为空
      statusCallBack:
      #华为分配的app请求地址
      url: https://XXXXX.cn-north-4.XXXXXXXX.com:443
   zhutong:
      #助通短信
      #助通终端用户管理的用户名 username 必填；非登录账号密码，请登录后台管理地址进行查看：http://mix2.zthysms.com/login
      accessKeyId: tushu1122XXX
      #助通终端用户管理的用户名 passwrod 必填；
      accessKeySecret: UbXXX4SL
      #短信签名，可选；可选的时候，只能使用自定义短信不能使用模板短信； 具体在这里查看审核过的短信签名：https://mix2.zthysms.com/index.html#/SignatureManagement
      signature: 上海千XXXX
```

3. 方法使用
   
```java
@RestController
@RequestMapping("/test/")
public class DemoController {

    // 测试发送固定模板短信
    @RequestMapping("/")
    public void doLogin(String username, String password) {
         //阿里云向此手机号发送短信
        SmsFactory.createSmsBlend(SupplierType.ALIBABA).sendMessage("18888888888","123456");
        //华为短信向此手机号发送短信
        SmsFactory.createSmsBlend(SupplierType.HUAWEI).sendMessage("16666666666","000000");
    }
}
```


## 配置详解

#### 线程池配置

每一家厂商都对于异步短信有不同的支持，有些甚至没有，为了统一存在一个异步短信，我们配置了一个线程池用于执行异步短信任务，线程池默认配置如下：  

```yaml
sms:
  #核心线程池大小
  corePoolSize: 10
  #最大线程数
  maxPoolSize: 30
  #队列容量
  queueCapacity: 50
  #活跃时间
  keepAliveSeconds: 60
  # 线程名字前缀
  threadNamePrefix: sms-executor-
  #设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
  shutdownStrategy: true
```

以上线程池为默认的配置，如果有需要可以跟随自己的需求在yml文件进行配置


## 参与贡献
```
1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request 到 dev-3.x 分支
```
## 贡献原则
- 我们原则上欢迎任何人为sms4j添加加瓦贡献代码
- 贡献代码应注释完备，按照javaDoc标准对 类，方法，变量，参数，返回值等信息说明
- 新增的方法模块不能破坏原有结构和兼容性
- 如果我们关闭了你的issues或者pr请查看回复内容，我们会在回复中做出解释

### 分支介绍
1. master 正式版分支，最终发布到maven中央仓库的版本
2. dev 开发分支，贡献的代码将合并到这里，无误后合并至preview
