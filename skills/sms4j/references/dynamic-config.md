# 动态配置

## 使用场景

- 短信账号存在数据库、配置中心或租户配置中。
- 需要运行时按 `configId` 创建或刷新短信实例。
- 不希望把密钥写在 `application.yml`。

## Spring Boot 自定义配置来源

实现 `SmsReadConfig`：

```java
import org.dromara.sms4j.core.datainterface.SmsReadConfig;
import org.dromara.sms4j.provider.config.BaseConfig;
import org.dromara.sms4j.tencent.config.TencentConfig;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class SmsConfigReader implements SmsReadConfig {

    /**
     * 根据配置标识读取短信厂商配置。
     *
     * @param configId 配置标识
     * @return 短信厂商配置
     */
    @Override
    public BaseConfig getSupplierConfig(String configId) {
        TencentConfig config = new TencentConfig();
        config.setConfigId(configId);
        config.setAccessKeyId("您的accessKey");
        config.setAccessKeySecret("您的accessKeySecret");
        config.setSignature("您的短信签名");
        config.setTemplateId("您的模板ID");
        config.setSdkAppId("您的sdkAppId");
        return config;
    }

    /**
     * 读取全部短信厂商配置。
     *
     * @return 短信厂商配置列表
     */
    @Override
    public List<BaseConfig> getSupplierConfigList() {
        return Collections.emptyList();
    }
}
```

创建实例：

```java
import org.dromara.sms4j.core.factory.SmsFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SmsInitializer {

    private final SmsConfigReader smsConfigReader;

    public SmsInitializer(SmsConfigReader smsConfigReader) {
        this.smsConfigReader = smsConfigReader;
    }

    /**
     * 应用启动后创建短信实例。
     *
     * @param event Spring 容器刷新事件
     */
    @EventListener
    public void init(ContextRefreshedEvent event) {
        SmsFactory.createSmsBlend(smsConfigReader, "tx1");
    }
}
```

## JavaSE 动态初始化

JavaSE 可以直接构造 `SmsConfig` 和厂商 `SupplierConfig` 列表：

```java
SmsConfig smsConfig = new SmsConfig();
TencentConfig tencentConfig = new TencentConfig();
tencentConfig.setConfigId("tx1");
tencentConfig.setAccessKeyId("您的accessKey");
tencentConfig.setAccessKeySecret("您的accessKeySecret");
tencentConfig.setSignature("您的短信签名");
tencentConfig.setTemplateId("您的模板ID");
tencentConfig.setSdkAppId("您的sdkAppId");

SEInitializer.initializer().fromConfig(smsConfig, Collections.singletonList(tencentConfig));
```

也可以使用 `SEInitializer.initializer().fromYaml(yaml)` 或 `fromJson(json)`。

## 刷新与注销

- 已创建实例会被 `SmsFactory` 持有。
- 可以用 `SmsFactory.unregister(configId)` 注销旧实例。
- 重新读取配置后再调用 `SmsFactory.createSmsBlend(...)` 创建新实例。
