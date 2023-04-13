---
# 这是文章的标题
title: 🎡容联云国内短信
# 这是页面的图标
icon: 
# 这是侧边栏的顺序
order: 5
# 设置作者
author: Charles7c
# 设置写作时间
date: 2023-04-11
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
  cloopen:
    # 访问键标识
    accessKeyId: your accessKeyId
    # 访问键秘钥
    accessKeySecret: your accessKeySecret
    # 应用 ID
    appId: your appId
    # 模板 ID（发送固定模板短信时使用的此配置）
    templateId: your templateId
    # Rest URL 域名
    serverIp: app.cloopen.com
    # Rest URL 端口
    serverPort: 8883
```
### 数据库风格配置
```json
{
  "accessKeyId": "your accessKeyId",
  "accessKeySecret": "your accessKeySecret",
  "appId": "your appId",
  "templateId": "your templateId",
  "serverIp": "app.cloopen.com",
  "serverPort": "8883"
}
```
### 手动写入配置文件风格
```java
@Configuration
public class AliConfiguration{
    
    @Bean
    public void setConfiguration(){
        CloopenConfig cloopenConfig = SupplierFactory.getCloopenConfig();
        cloopenConfig.setAccessKeyId("your accessKeyId");
        cloopenConfig.setAppId("your appId");
        cloopenConfig.setAccessKeySecret("your accessKeySecret");
        cloopenConfig.setServerIp("app.cloopen.com");
        cloopenConfig.setTemplateId("your templateId");
        cloopenConfig.setServerPort("8883");
    }
}
```
### 其他方式
如果你想在某个环节动态的改变配置中的值，可以随时通过`SupplierFactory.getCloopenConfig()`
获取容联云的单例配置对象，并且修改他的值，但是要注意的是， 如果你修改了荣联云配置的值在发送短信前必须至少调用一次
`SmsFactory.refresh(SupplierType.CLOOPEN);`方法进行配置刷新