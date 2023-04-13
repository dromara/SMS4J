---
# 这是文章的标题
title: 🍭亿美软通国内短信
# 这是页面的图标
icon: 
# 这是侧边栏的顺序
order: 9
# 设置作者
author: Richard
# 设置写作时间
date: 2023-04-12
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
  emay:
    # 访问键标识
    appid: your appId
    # 访问键秘钥
    secretKey: your secretKey
    # 短信发送请求地址
    requestUrl: api url
```
### 数据库风格配置
```json
{
  "appid": "your appid",
  "secretKey": "your secretKey",
  "requestUrl": "your requestUrl"
}

```
### 配置说明
```
亿美软通不支持通过配置短信模版的方式进行发送，所以没有templateId的概念。
短信内容使用者可以自定义，在调用需传参tmeplateId的方法时，此参数将不会生效
```

```java
public class EmaSendTest{
    
    private final SmsBlend smsBlend = SmsFactory.createSmsBlend(SupplierType.EMAY);
    
    private String message = "这是一条测试短信";

    /**
     * 发送手机号
     * @param phone
     * @return
     */
    @Test
    public SmsResponse sendMessage(String phone){
        smsBlend.sendMessage(phone, messages);
    }

    /**
     * 
     * @param phone 发送手机号
     * @param templateId 参数将不会生效
     * @return
     */
    @Test
    public SmsResponse sendMessage(String phone,Sting templateId){
        smsBlend.sendMessage(phone, messages);
    }
}

```

### 手动写入配置文件风格

```java
@Configuration
public class AliConfiguration{
    
    @Bean
    public void setConfiguration(){
        EmayConfig emayConfig = SupplierFactory.getEmayConfig();
        emayConfig.setAppId("your appid");
        emayConfig.setSecretKey("your secretKey");
        emayConfig.setRequestUrl("your requestUrl");
    }
}

```
### 其他方式
如果你想在某个环节动态的改变配置中的值，可以随时通过
`SupplierFactory.getEmayConfig()` 获取亿美软通的单例配置对象，并且修改他的值，亿美软通短信可以不调用刷新方法的情况下随时拿到配置的值，
但是我们还是建议使用`SmsFactory.refresh(SupplierType.EMAY);`方法进行配置刷新
