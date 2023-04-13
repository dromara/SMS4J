---
title: 🪛API详解
index: false
icon: play
---

## 🪚工厂获取
以下仅为示例
```java
@RestController
public class Demo{
    /** 华为短信实现*/
    private final SmsBlend huaweiSms = SmsFactory.createSmsBlend(SupplierType.HUAWEI);
}
```
:::tip
在实际使用中并不建议提前在属性中声明，因为spring的注入可能会先注入项目中的内容再注入其他的，
有些厂商的SDK（比如阿里）内部的变量并不会随外部改变，所以可能会导致配置未获取到，如果出现这样的情况，请调用一次刷新方法即可
或在方法内部使用工厂获取短信实现对象
:::
## 工厂枚举

```java
public enum SupplierType {
    /** 阿里云*/
    ALIBABA("阿里云短信"),
    /** 华为云*/
    HUAWEI("华为云短信"),
    /** 云片*/
    YUNPIAN("云片短信"),
    /** 腾讯云*/
    TENCENT("腾讯云短信"),
    /** 合一短信*/
    UNI_SMS("合一短信"),
    /** 京东云短信 */
    JD_CLOUD("京东云短信"),
    /** 容联云短信 */
    CLOOPEN("容联云短信"),
    /** 亿美软通短信 */
    EMAY("亿美软通短信"),
    ;
}
```

## 📨标准短信

#### 发送固定消息模板短信
此方法将使用配置文件中预设的短信模板进行短信发送
该方法指定的模板变量只能存在一个（配置文件中）
如使用的是腾讯的短信，参数字符串中可以同时存在多个参数，使用 & 分隔例如：您的验证码为{1}在{2}分钟内有效，可以传为 `message="1234"+"&"+"5"` 
```java
SmsResponse sendMessage(String phone,String message);
```
#### 使用自定义模板发送短信 
使用自定义模板进行短信发送，templateId为模板ID，messages为短信内容，在messages中可以存在一个或多个变量，取决于模板
key为模板的变量名称，value为模板变量的值
```java
SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String,String> messages);
```
#### 群发短信，固定模板短信
此方法使用配置文件中预设的短信模板进行短信发送
该方法指定的模板变量只能存在一个（配置文件中）
phones 为要群发的手机号
```java
SmsResponse massTexting(List<String> phones, String message);
```
:::warning
考虑到性能和大部分的厂商支持，不建议一次性发送超过1000个手机号
如果有大量短信需要进行群体发送，建议使用异步群发短信或分批次多次发送
:::

#### 使用自定义模板群发短信
与单体自定义模板短信类似，区别为此方法是群发短信，可以同时向多个手机号进行发送
```java
SmsResponse massTexting(List<String> phones,String templateId, LinkedHashMap<String, String> messages);
```
## 🥾异步短信
#### 异步发送固定模板短信
此方法使用配置文件中预设的短信模板进行短信发送
该方法指定的模板变量只能存在一个（配置文件中）
```java
void sendMessageAsync(String phone, String message, CallBack callBack);
```
:::tip
callBack为发送结果回调，可以使用lambda表达式进行接收
如果不关注发送结果，该方法有一个不接收回调的重载方法
例如：
```java
sms.sendMessageAsync("18888888888", "123456", e->log.info(e.toString()));
```
该回调方法中接收的参数为 SmsResponse
:::

#### 异步发送自定义模板短信
该方法与发送自定义模板短信的方法使用一致，区别在于末尾接收一个callBack回调参数用于接收短信的发送结果
如果不关注发送结果，可以使用重载方法
```java
void sendMessageAsync(String phone, String templateId, LinkedHashMap<String,String> messages, CallBack callBack);
```
## ⏱延时短信
在项目中，有时会用到延时短信，在日常中通常会选择定时任务或中间件的方式进行，这也导致了简简单单的一个需求，却充斥着大量的代码支撑
为了方便日常使用，减少冗余的代码，我们在工具中添加了延迟短信的支持。
:::warning
在当前的版本中，考虑到延迟短信本身在异步的定时队列中等待执行，所以并没有设置回调等方式去接收短信的发送结果。
在后续的版本中我们将考虑用其他的方式去获取到短信的发送结果
:::

#### 使用固定模板发送延时短信
使用配置文件中定义的模板和变量发送延时短信，delayedTime为延迟时间，单位为毫秒
```java
void delayedMessage(String phone ,String message,Long delayedTime);
```

#### 使用自定义模板发送延时短信
该方法使用方式与标准方法中的自定义模板发送方式一致，末尾添加了一个delayedTime延迟时间参数，单位为毫秒
```java
void delayedMessage(String phone ,String templateId, LinkedHashMap<String,String> messages,Long delayedTime);
```
#### 群发固定模板延迟短信
```java
void delayMassTexting(List<String> phones, String message,Long delayedTime);
```
#### 群发自定义模板延迟短信
```java
void delayMassTexting(List<String> phones,String templateId, LinkedHashMap<String, String> messages,Long delayedTime);
```
