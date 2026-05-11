# 配置与 API

## 核心配置结构

```yaml
sms:
  config-type: yaml
  blends:
    configId:
      supplier: tencent
      access-key-id: 您的accessKey
      access-key-secret: 您的accessKeySecret
      signature: 您的短信签名
      template-id: 您的模板ID
```

- `configId` 是业务侧自定义标识，建议用英文或数字，例如 `tx1`、`aliyun-main`。
- `supplier` 是短信厂商标识。未配置时，部分初始化流程会把 `configId` 当作 supplier；业务项目建议显式配置。
- 同一个厂商多账号时，写多个 `configId`，`supplier` 保持相同即可。

## 常用 supplier

`alibaba`、`tencent`、`huawei`、`jdcloud`、`cloopen`、`netease`、`ctyun`、`qiniu`、`unisms`、`yunpian`、`zhutong`、`lianlu`、`dingzhong`、`chuanglan`、`jiguang`、`buding_v2`、`mas`、`baidu`、`luosimao`、`mysubmail`、`danmi`、`yixintong`。

## 公共字段

- `access-key-id` / `accessKeyId`
- `access-key-secret` / `accessKeySecret`
- `sdk-app-id` / `sdkAppId`
- `signature`
- `template-id` / `templateId`
- `weight`
- `max-retries` / `maxRetries`
- `retry-interval` / `retryInterval`
- `maximum`
- `proxy`

字段可用 kebab-case 或 camelCase；在 YAML 示例中优先使用 kebab-case。

## HTTP 代理

```yaml
sms:
  blends:
    tx1:
      supplier: tencent
      proxy:
        enable: true
        host: 127.0.0.1
        port: 8080
```

## 发送 API

标准短信、异步短信、延时短信的完整用法见 `sending-methods.md`。本节只保留最常用入口。

```java
SmsBlend smsBlend = SmsFactory.getSmsBlend("tx1");
```

固定模板单参数：

```java
smsBlend.sendMessage("18888888888", "123456");
```

固定模板多参数：

```java
LinkedHashMap<String, String> params = new LinkedHashMap<>();
params.put("code", "123456");
params.put("minute", "5");
smsBlend.sendMessage("18888888888", params);
```

指定模板发送：

```java
LinkedHashMap<String, String> params = new LinkedHashMap<>();
params.put("code", "123456");
smsBlend.sendMessage("18888888888", "SMS_模板ID", params);
```

群发：

```java
smsBlend.massTexting(Arrays.asList("18888888888", "16666666666"), "123456");
```

异步无回调：

```java
smsBlend.sendMessageAsync("18888888888", "123456");
```

延迟：

```java
smsBlend.delayedMessage("18888888888", "123456", 3000L);
```

无参负载均衡获取：

```java
SmsBlend smsBlend = SmsFactory.getSmsBlend();
```

适合多配置随机/权重发送；指定账号发送时优先用 `getSmsBlend(configId)`。
