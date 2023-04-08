---
home: true
icon: home
title: SMS-Aggregator
heroImage: /logo1.png
heroImageDark: /logo.png
heroText: 短信聚合
tagline: 短信聚合    -- 让发送短信变的更简单。
details: V1.0.3
actions:
  - text: 开始 🏡
    link: /doc/start/
    type: primary
  - text: 🥭 V1.0.3
    link: https://gitee.com/the-wind-is-like-a-song/sms_aggregation
    details: 最新版本

features:
  - title: ☕开箱即用
    icon: markdown
    details: 极简单的配置，方便的使用，三分钟即可完成发送短信的功能

  - title: ⏰简单优雅
    icon: slides
    details: 统一各个厂商的发送方式和功能，省去学习不同短信厂商的差异化方法，做到极简使用

  - title: 🛫切换灵活
    icon: layout
    details: 只需要通过配置文件即可立即切换不同的服务商，省去查阅文档和ctrl c v，发送短信，有手就行

  - title: 📱功能丰富
    icon: comment
    details: 对于短信中常见的限制和规则，不需要编写额外的功能方法和模块，只需要开启配置即可，后续还将集成更多功能方便使用


copyright: false
footer:  © 2022 wind <a href="https://beian.miit.gov.cn/#/Integrated/index" target="_blank">冀ICP备2021004949号-3</a> 
---
<h4 align="center" style="margin: 0 0 0; font-weight: bold;">
<a align="center" href="https://gitee.com/the-wind-is-like-a-song/sms_aggregation/stargazers" ><img src="https://gitee.com/the-wind-is-like-a-song/sms_aggregation/badge/star.svg?theme=gvp"></a>
<a align="center" href="https://gitee.com/the-wind-is-like-a-song/sms_aggregation/master/LICENSE" style="padding-left: 5px"><img src="https://img.shields.io/badge/license-Apache--2.0-green"></a>
<a align="center" href="https://gitee.com/the-wind-is-like-a-song/sms_aggregation" style="padding-left: 5px"><img src="https://img.shields.io/badge/version-v1.0.3-blue"></a>
</h4>

## 📀maven安装
   ```xml
   <dependency>
    <groupId>kim.wind</groupId>
    <artifactId>sms-aggregation-spring-boot-starter</artifactId>
    <version> version </version>
   </dependency>
   ```
## 🛠️基础配置
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
## 🧿使用
```java
@RestController
@RequestMapping("/test/")
public class DemoController {

    //注入短信工具
    @Autowired
    private SmsBlend sms;

    // 测试发送固定模板短信
    @RequestMapping("/")
    public void doLogin(String username, String password) {
       sms.sendMessage("18888888888","测试发送固定模板短信");
    }
}
```
## 🤝 dromara 组织项目

<p id="dromtitle">
<b><a href="https://dromara.org/zh/projects/" target="_blank">为往圣继绝学，一个人或许能走的更快，但一群人会走的更远。</a></b>
</p>

<p >
<a class="friends-item" href="https://hutool.cn/" target="_blank" title="🍬小而全的Java工具类库，使Java拥有函数式语言般的优雅，让Java语言也可以“甜甜的">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/hutool2.png')" alt="🍬小而全的Java工具类库，使Java拥有函数式语言般的优雅，让Java语言也可以“甜甜的”。">
</a>
<a class="friends-item" href="https://sa-token.cc/" target="_blank" title="一个轻量级 java 权限认证框架，让鉴权变得简单、优雅！">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/sa-token.png')" alt="一个轻量级 java 权限认证框架，让鉴权变得简单、优雅！">
</a>
<a class="friends-item" href="https://liteflow.yomahub.com/" target="_blank" title="轻量，快速，稳定，可编排的组件式流程引擎">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/liteflow2.png')" alt="轻量，快速，稳定，可编排的组件式流程引擎">
</a>
<a class="friends-item" href="https://jpom.top/" target="_blank" title="一款简而轻的低侵入式在线构建、自动部署、日常运维、项目监控软件">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/jpom.png')" alt="一款简而轻的低侵入式在线构建、自动部署、日常运维、项目监控软件">
</a>
<a class="friends-item" href="https://gitee.com/dromara/TLog" target="_blank" title="一个轻量级的分布式日志标记追踪神器，10分钟即可接入，自动对日志打标签完成微服务的链路追踪">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/tlog2.png')" alt="一个轻量级的分布式日志标记追踪神器，10分钟即可接入，自动对日志打标签完成微服务的链路追踪">
</a>
<a class="friends-item" href="https://easy-es.cn/" target="_blank" title="🚀傻瓜级ElasticSearch搜索引擎ORM框架">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/easy-es2.png')" alt="🚀傻瓜级ElasticSearch搜索引擎ORM框架">
</a>
<a class="friends-item" href="https://gitee.com/dromara/hmily" target="_blank" title="高性能一站式分布式事务解决方案">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/hmily.png')" alt="高性能一站式分布式事务解决方案。">
</a>
<a class="friends-item" href="https://gitee.com/dromara/Raincat" target="_blank" title="强一致性分布式事务解决方案">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/raincat.png')" alt="强一致性分布式事务解决方案。">
</a>
<a class="friends-item" href="https://gitee.com/dromara/myth" target="_blank" title="可靠消息分布式事务解决方案">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/myth.png')" alt="可靠消息分布式事务解决方案。">
</a>
<a class="friends-item" href="https://cubic.jiagoujishu.com/" target="_blank" title="一站式问题定位平台，以agent的方式无侵入接入应用，完整集成arthas功能模块，致力于应用级监控，帮助开发人员快速定位问题">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/cubic.png')" alt="一站式问题定位平台，以agent的方式无侵入接入应用，完整集成arthas功能模块，致力于应用级监控，帮助开发人员快速定位问题">
</a>
<a class="friends-item" href="http://forest.dtflyx.com/" target="_blank" title="Forest能够帮助您使用更简单的方式编写Java的HTTP客户端">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/forest-logo.png')" alt="Forest能够帮助您使用更简单的方式编写Java的HTTP客户端" nf>
</a>
<a class="friends-item" href="https://su.usthe.com/" target="_blank" title="面向 REST API 的高性能认证鉴权框架">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/sureness.png')" alt="面向 REST API 的高性能认证鉴权框架">
</a>
<a class="friends-item" href="https://gitee.com/dromara/northstar" target="_blank" title="Northstar盈富量化交易平台">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/northstar_logo.png')" alt="Northstar盈富量化交易平台">
</a>
<a class="friends-item" href="https://www.jeesuite.com/" target="_blank" title="开源分布式云原生架构一站式解决方案">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/mendmix.png')" alt="开源分布式云原生架构一站式解决方案">
</a>
<a class="friends-item" href="https://www.x-easypdf.cn" target="_blank" title="企业生产级百亿日PV高可用可拓展的RPC框架">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/koalas-rpc2.png')" alt="企业生产级百亿日PV高可用可拓展的RPC框架。">
</a>
<a class="friends-item" href="https://dynamictp.cn/" target="_blank" title="🔥🔥🔥 基于配置中心的轻量级动态可监控线程池">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/dynamic-tp.png')" alt="🔥🔥🔥 基于配置中心的轻量级动态可监控线程池">
</a>
<a class="friends-item" href="https://hertzbeat.com/" target="_blank" title="易用友好的云监控系统">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/hertzbeat-logo.png')" alt="易用友好的云监控系统">
</a>
<a class="friends-item" href="https://maxkey.top/" target="_blank" title="业界领先的身份管理和认证产品">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/maxkey3.png')" alt="业界领先的身份管理和认证产品">
</a>
<a class="friends-item" href="https://plugins.sheng90.wang/fast-request/" target="_blank" title="Idea 版 Postman，为简化调试API而生">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/fast-request.gif')" alt="Idea 版 Postman，为简化调试API而生">
</a>
<a class="friends-item" href="https://async.sizegang.cn/" target="_blank" title="🔥 配置极简功能强大的异步任务动态编排框架">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/gobrsasync-logo.png')" alt="🔥 配置极简功能强大的异步任务动态编排框架">
</a>
<a class="friends-item" href="https://www.x-easypdf.cn" target="_blank" title="一个用搭积木的方式构建pdf的框架（基于pdfbox）">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/xeasypdf-logo.png')" alt="一个用搭积木的方式构建pdf的框架（基于pdfbox）">
</a>
<a class="friends-item" href="http://dromara.gitee.io/image-combiner" target="_blank" title="一个专门用于图片合成的工具，没有很复杂的功能，简单实用，却不失强大">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/imagecombiner-logo.png')" alt="一个专门用于图片合成的工具，没有很复杂的功能，简单实用，却不失强大">
</a>
<a class="friends-item" href="https://www.herodotus.cn/" target="_blank" title="Dante-Cloud 是一款企业级微服务架构和服务能力开发平台。">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/dantecloud-logo.png')" alt="Dante-Cloud 是一款企业级微服务架构和服务能力开发平台。">
</a>
<a class="friends-item" href="https://gitee.com/dromara/go-view" target="_blank" title="让每一位开源爱好者，体会到开源的快乐。">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/goview-logo.png')" alt="让每一位开源爱好者，体会到开源的快乐。">
</a>
<a class="friends-item" href="http://doc.zyplayer.com/doc-wiki#/integrate/zyplayer-doc" target="_blank" title="可私有化部署的在线知识库管理系统">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/wiki-logo.png')" alt="可私有化部署的在线知识库管理系统。">
</a>
<a class="friends-item" href="https://gitee.com/dromara/RuoYi-Vue-Plus" target="_blank" title="超越原生的若依后台管理系统">
	<img class="no-zoom friends-item-img hover-alt" :src="$withBase('/images/friends/link/ruoyi-plus.png')" alt="超越原生的若依后台管理系统。">
</a>
</p>

## 💏友情链接
<a href="https://www.coderutil.com/" style="padding-left: 30px"><img class="no-zoom friends-item-img hover-alt" src="/assets/icon/code.png"></a>
<a href="https://www.apipost.cn/" style="padding-left: 30px"><img class="no-zoom friends-item-img hover-alt" src="/assets/icon/apipost.png"></a>




## 💾代码托管

[![风如歌/sms_aggregation](https://gitee.com/the-wind-is-like-a-song/sms_aggregation/widgets/widget_card.svg?colors=eae9d7,2e2f29,272822,484a45,eae9d7,747571)](https://gitee.com/the-wind-is-like-a-song/sms_aggregation)
