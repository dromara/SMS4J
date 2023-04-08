---
# 这是文章的标题
title: 🪐在SpringBoot环境集成
# 这是页面的图标
icon: <svg t="1679837826543" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1201" width="200" height="200"><path d="M763.136 958.72H262.656c-50.944 0-92.16-41.216-92.16-92.16V518.144H107.776c-25.6 0-48.128-15.36-57.6-39.168s-3.584-50.432 14.848-68.096c0.256-0.256 0.512-0.512 0.768-0.512l344.064-307.2c56.576-53.248 145.408-53.76 202.496-1.28l346.624 307.2 0.512 0.512c18.944 17.408 25.088 44.288 15.616 68.352-9.472 24.064-32 39.424-57.856 39.424h-61.696v348.928c-0.256 50.944-41.472 92.416-92.416 92.416zM107.52 456.704h93.696c16.896 0 30.72 13.824 30.72 30.72v379.136c0 16.896 13.824 30.72 30.72 30.72h500.48c16.896 0 30.72-13.824 30.72-30.72V486.656c0-16.896 13.824-30.72 30.72-30.72H917.504s0.256-0.512 0.256-0.768l-0.256-0.256-346.368-307.2-0.512-0.512c-33.536-30.976-86.016-30.72-119.04 0.768-0.256 0.256-0.512 0.512-0.768 0.512L107.264 455.68c0 0.256-0.256 0.256-0.256 0.256s0.256 0.512 0.512 0.768c-0.256 0 0 0 0 0z m0 0z" fill="#040000" p-id="1202"></path><path d="M644.608 897.024h-61.44v-218.112c0-16.64-13.824-29.952-30.72-29.952H471.04c-16.896 0-30.72 13.568-30.72 29.952v218.112h-61.44v-218.112c0-50.432 41.216-91.392 92.16-91.392h81.408c50.944 0 92.16 40.96 92.16 91.392v218.112z" fill="#D63123" p-id="1203"></path></svg>
# 这是侧边栏的顺序
order: 2
# 设置作者
author: wind
# 设置写作时间
date: 2023-03-26
# 此页面会在文章列表置顶
sticky: true
# 此页面会出现在文章收藏中
star: true
# 你可以自定义页脚
footer: © 2022 wind <a href="https://beian.miit.gov.cn/#/Integrated/index" target="_blank">冀ICP备2021004949号-3</a>
# 你可以自定义版权信息
# copyright: 无版权
---

<!-- more -->

## 在SpringBoot环境集成
### 1.创建项目
在 IDE 中新建一个 SpringBoot 项目，例如：sms-demo-springboot

### 2.添加依赖
在项目中添加maven依赖：

 _最新版本请查看首页_

   ```xml
   <dependency>
    <groupId>kim.wind</groupId>
    <artifactId>sms-aggregation-spring-boot-starter</artifactId>
    <version> version </version>
   </dependency>
   ```
### 3.设置配置文件
自 V1.1.0版本开始，sms-aggregator开始支持多厂商共用，可以同时配置多个厂商使用
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
   ```
### 4.创建测试controller
为应对多厂商的不同实现自 V1.1.0版本开始不再使用springBoot注入对象的形式进行实现类的获取，改为工厂模式获取单例的实现对象
```java
@RestController
@RequestMapping("/test/")
public class DemoController {
    
    /** 阿里云短信实现*/
    private final SmsBlend alibabaSms = SmsFactory.createSmsBlend(SupplierType.ALIBABA);
    
    /** 华为短信实现*/
    private final SmsBlend huaweiSms = SmsFactory.createSmsBlend(SupplierType.HUAWEI);

    // 测试发送固定模板短信
    @RequestMapping("/")
    public void doLogin(String username, String password) {
         //阿里云向此手机号发送短信
        alibabaSms.sendMessage("18888888888","123456");
        //华为短信向此手机号发送短信
        huaweiSms.sendMessage("16666666666","000000");
    }
}
```

### 5.运行
启动代码，从浏览器访问 `localhost:8080/test`
等待手机收到短信
