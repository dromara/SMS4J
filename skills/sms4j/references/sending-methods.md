# 短信发送方式

本页基于 sms4j 3.x 官网的标准短信、异步短信、延时短信文档整理。用户问“怎么发短信”“异步发送”“延时发送”“群发”时优先读取本页。

## 获取短信实例

```java
SmsBlend sms = SmsFactory.getSmsBlend("tx1");
```

`tx1` 是 `sms.blends.tx1` 的配置标识。多账号或多厂商时，通过不同 `configId` 获取不同实例。

## 标准短信

### 固定模板单变量

使用配置文件中预设的 `template-id` 和模板变量发送。该便捷方法适合模板只有一个变量的场景。

```java
SmsResponse response = sms.sendMessage("18888888888", "123456");
```

接口：

```java
SmsResponse sendMessage(String phone, String message);
```

腾讯短信模板如果存在多个占位符，也可以按官网说明把多个值用 `&` 拼接，例如验证码和有效分钟数：

```java
sms.sendMessage("18888888888", "123456" + "&" + "5");
```

### 固定模板多变量

适合模板含多个变量，或模板变量名需要显式对应的场景。若模板没有变量，`messages` 可以传 `null`，但业务代码中更推荐传空 `LinkedHashMap`，可读性更好。

```java
LinkedHashMap<String, String> messages = new LinkedHashMap<>();
messages.put("code", "123456");
messages.put("minute", "5");
SmsResponse response = sms.sendMessage("18888888888", messages);
```

接口：

```java
SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages);
```

### 指定模板发送

适合同一个账号需要按业务选择不同模板。`templateId` 是厂商模板 ID，`messages` 的 key 是模板变量名，value 是变量值。

```java
LinkedHashMap<String, String> messages = new LinkedHashMap<>();
messages.put("code", "123456");
SmsResponse response = sms.sendMessage("18888888888", "SMS_123456", messages);
```

接口：

```java
SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages);
```

### 群发固定模板

```java
List<String> phones = Arrays.asList("18888888888", "16666666666");
SmsResponse response = sms.massTexting(phones, "123456");
```

接口：

```java
SmsResponse massTexting(List<String> phones, String message);
```

官网提醒：考虑性能和大多数厂商支持，不建议一次群发超过 1000 个手机号。大量群发优先拆批，或使用异步群发。

### 群发指定模板

```java
List<String> phones = Arrays.asList("18888888888", "16666666666");
LinkedHashMap<String, String> messages = new LinkedHashMap<>();
messages.put("code", "123456");
SmsResponse response = sms.massTexting(phones, "SMS_123456", messages);
```

接口：

```java
SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages);
```

## 异步短信

异步方法适合发送动作不应该阻塞当前请求的场景。回调参数接收 `SmsResponse`；如果不关心结果，可以使用无回调重载。

### 异步固定模板

```java
sms.sendMessageAsync("18888888888", "123456", response -> log.info(response.toString()));
```

接口：

```java
void sendMessageAsync(String phone, String message, CallBack callBack);
```

无回调：

```java
sms.sendMessageAsync("18888888888", "123456");
```

### 异步指定模板

```java
LinkedHashMap<String, String> messages = new LinkedHashMap<>();
messages.put("code", "123456");
sms.sendMessageAsync("18888888888", "SMS_123456", messages, response -> log.info(response.toString()));
```

接口：

```java
void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages, CallBack callBack);
```

无回调：

```java
sms.sendMessageAsync("18888888888", "SMS_123456", messages);
```

## 延时短信

延时短信会进入异步定时队列等待执行，`delayedTime` 单位为毫秒。官网说明当前版本没有提供回调接收延时短信发送结果。

### 固定模板延时发送

```java
sms.delayedMessage("18888888888", "123456", 3000L);
```

接口：

```java
void delayedMessage(String phone, String message, Long delayedTime);
```

### 指定模板延时发送

```java
LinkedHashMap<String, String> messages = new LinkedHashMap<>();
messages.put("code", "123456");
sms.delayedMessage("18888888888", "SMS_123456", messages, 3000L);
```

接口：

```java
void delayedMessage(String phone, String templateId, LinkedHashMap<String, String> messages, Long delayedTime);
```

### 群发固定模板延时短信

```java
List<String> phones = Arrays.asList("18888888888", "16666666666");
sms.delayMassTexting(phones, "123456", 3000L);
```

接口：

```java
void delayMassTexting(List<String> phones, String message, Long delayedTime);
```

### 群发指定模板延时短信

```java
List<String> phones = Arrays.asList("18888888888", "16666666666");
LinkedHashMap<String, String> messages = new LinkedHashMap<>();
messages.put("code", "123456");
sms.delayMassTexting(phones, "SMS_123456", messages, 3000L);
```

接口：

```java
void delayMassTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages, Long delayedTime);
```

## 选择建议

- 当前请求必须知道发送结果：用标准短信。
- 不希望阻塞业务请求，且需要结果：用异步短信带回调。
- 不关心即时结果：用异步短信无回调。
- 需要几秒或几分钟后发送：用延时短信；若要求可靠持久化、重启不丢任务，应使用业务定时任务或消息队列。
- 大批量群发：拆批处理，避免一次提交过多手机号。
