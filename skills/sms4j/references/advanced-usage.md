# 高级使用与排错

## 发送限制与黑名单

基础配置：

```yaml
sms:
  restricted: true
  account-max: 100
  minute-max: 10
```

- `restricted: true` 开启限制相关处理器。
- `account-max` 控制单账号每日最大发送量。
- `minute-max` 控制单账号每分钟最大发送量。
- 黑名单能力依赖 `SmsDao` 存储；默认实现适合简单场景，生产项目可按需要自定义。

## 自定义拦截器

实现 `SmsProcessor`：

```java
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.proxy.SmsProcessor;

import java.lang.reflect.Method;

public class AuditSmsProcessor implements SmsProcessor {

    /**
     * 发送前记录请求。
     *
     * @param method 发送方法
     * @param source 短信实例
     * @param param  方法参数
     * @return 处理后的参数，返回 null 表示不替换
     */
    @Override
    public Object[] preProcessor(Method method, Object source, Object[] param) {
        return null;
    }

    /**
     * 发送后记录响应。
     *
     * @param result 发送结果
     * @param param  方法参数
     * @return 处理后的结果，返回 null 表示不替换
     */
    @Override
    public Object postProcessor(SmsResponse result, Object[] param) {
        return null;
    }
}
```

注册：

```java
SmsProxyFactory.addPreProcessor(new AuditSmsProcessor());
```

注册必须发生在发送方法调用前。

## 常见问题

### `SmsFactory.getSmsBlend("xxx")` 返回 null

原因通常是配置没有被初始化、`configId` 写错、`supplier` 不支持，或动态配置没有调用 `createSmsBlend`。

修复建议：
- Spring Boot 检查 starter 依赖和 `application.yml` 是否被加载。
- JavaSE 检查是否先调用 `SEInitializer.initializer().fromYaml()`。
- 动态配置检查是否调用 `SmsFactory.createSmsBlend(configReader, configId)`。

### 提示“不支持当前供应商配置”

原因通常是 `supplier` 写错，或对应厂商依赖/工厂没有注册。JDCloud 还要求运行时存在京东云 SDK 类。

### 模板参数不生效

原因通常是 `templateName` 与厂商模板变量不一致，或使用了单参数发送但模板需要多个变量。

修复建议：多变量模板使用 `LinkedHashMap<String, String>` 发送，key 与厂商模板变量名保持一致。

### 需要切换短信厂商

业务代码尽量只依赖 `configId` 和 `SmsBlend`。切换厂商时新增或修改 `sms.blends.<configId>.supplier` 和厂商字段，发送代码不变。

### 生产安全

- 不要把真实密钥提交到 Git。
- 优先使用环境变量、配置中心或动态配置。
- 不要在日志中打印手机号全量、验证码、密钥。
- HTTP 代理只在明确需要时开启。
