---
# 这是文章的标题
title: 🎤合一短信
# 这是页面的图标
icon: 
# 这是侧边栏的顺序
order: 6
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
  uni-sms:
    # 访问键标识
    accessKeyId: your accessKeyId
    # 访问键秘钥 简易模式不需要配置
    accessKeySecret: your accessKeySecret
    #是否为简易模式 默认为true
    is-simple: true
    # 短信签名
    signature: 测试用短信签名
    # 模板Id 发送固定模板短信时使用的此配置
    templateId: your templateId
    # 模板变量名称 上述模板的变量名称
    templateName: your templateName
```
### 数据库风格配置
```json
{
  "accessKeyId": "your accessKeyId",
  "accessKeySecret": "your accessKeySecret",
  "isSimple": true,
  "signature": "短信签名",
  "templateId": "your TemplateId",
  "templateName": "code"
}
```
### 手动写入配置文件风格
```java
@Configuration
public class AliConfiguration{
    
    @Bean
    public void setConfiguration(){
        UniConfig uniConfig = SupplierFactory.getUniConfig();
        uniConfig.setAccessKeyId("your accessKeyId");
        uniConfig.setAccessKeySecret("your accessKeySecret");
        uniConfig.setIsSimple(true);
        uniConfig.setSignature("短信签名");
        uniConfig.setTemplateId("your TemplateId");
        uniConfig.setTemplateName("code");
    }
}
```
### 其他方式
如果你想在某个环节动态的改变配置中的值，可以随时通过
`SupplierFactory.getHuaweiConfig()` 获取合一短信的单例配置对象，并且修改他的值，
如果你修改了合一短信配置的值在发送短信前必须至少调用一次
`SmsFactory.refresh(SupplierType.UNI_SMS)`;方法进行配置刷新