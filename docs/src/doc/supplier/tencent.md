---
# 这是文章的标题
title: 🎮腾讯云短信
# 这是页面的图标
icon: <svg t="1679837826543" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1201" width="200" height="200"><path d="M763.136 958.72H262.656c-50.944 0-92.16-41.216-92.16-92.16V518.144H107.776c-25.6 0-48.128-15.36-57.6-39.168s-3.584-50.432 14.848-68.096c0.256-0.256 0.512-0.512 0.768-0.512l344.064-307.2c56.576-53.248 145.408-53.76 202.496-1.28l346.624 307.2 0.512 0.512c18.944 17.408 25.088 44.288 15.616 68.352-9.472 24.064-32 39.424-57.856 39.424h-61.696v348.928c-0.256 50.944-41.472 92.416-92.416 92.416zM107.52 456.704h93.696c16.896 0 30.72 13.824 30.72 30.72v379.136c0 16.896 13.824 30.72 30.72 30.72h500.48c16.896 0 30.72-13.824 30.72-30.72V486.656c0-16.896 13.824-30.72 30.72-30.72H917.504s0.256-0.512 0.256-0.768l-0.256-0.256-346.368-307.2-0.512-0.512c-33.536-30.976-86.016-30.72-119.04 0.768-0.256 0.256-0.512 0.512-0.768 0.512L107.264 455.68c0 0.256-0.256 0.256-0.256 0.256s0.256 0.512 0.512 0.768c-0.256 0 0 0 0 0z m0 0z" fill="#040000" p-id="1202"></path><path d="M644.608 897.024h-61.44v-218.112c0-16.64-13.824-29.952-30.72-29.952H471.04c-16.896 0-30.72 13.568-30.72 29.952v218.112h-61.44v-218.112c0-50.432 41.216-91.392 92.16-91.392h81.408c50.944 0 92.16 40.96 92.16 91.392v218.112z" fill="#D63123" p-id="1203"></path></svg>
# 这是侧边栏的顺序
order: 3
# 设置作者
author: wind
# 设置写作时间
date: 2023-03-27
# 此页面会在文章列表置顶
sticky: true
# 此页面会出现在文章收藏中
star: true
# 你可以自定义页脚
footer: © 2022 wind <a href="https://beian.miit.gov.cn/#/Integrated/index" target="_blank">冀ICP备2021004949号-3</a>
# 你可以自定义版权信息
# copyright: 无版权
---

### 基础配置
```yaml
sms:
  tencent:
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
    #请求超时时间 默认60秒
    connTimeout: 60
    #短信sdkAppId
    sdkAppId: 短信sdkAppId
    #地域信息默认为 ap-guangzhou 如无特殊改变可不用设置
    territory: ap-guangzhou
```
### 数据库风格配置
```json
{
  "accessKeyId": "your accessKeyId",
  "accessKeySecret": "your accessKeySecret",
  "sdkAppId": "your sdkAppId",
  "signature": "短信签名",
  "templateId": "your TemplateId",
  "territory": "地域信息",
  "connTimeout": 60
}
```
### 手动写入配置文件风格
```java
@Configuration
public class AliConfiguration{
    
    @Bean
    public void setConfiguration(){
        TencentConfig tencentConfig = SupplierFactory.getTencentConfig();
        tencentConfig.setAccessKeyId("your accessKeyId");
        tencentConfig.setAccessKeySecret("your accessKeySecret");
        tencentConfig.setSdkAppId("your sdkAppId");
        tencentConfig.setSignature("短信签名");
        tencentConfig.setTemplateId("your TemplateId");
        tencentConfig.setTerritory("地域信息");
        tencentConfig.setConnTimeout(60);
    }
}
```
### 其他方式
如果你想在某个环节动态的改变配置中的值，可以随时通过 
`SupplierFactory.getHuaweiConfig()` 获取腾讯云的单例配置对象，并且修改他的值， 如果你修改了腾讯云配置的值在发送短信前必须至少调用一次
`SmsFactory.refresh(SupplierType.TENCENT);`方法进行配置刷新