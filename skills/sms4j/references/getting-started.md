# 快速接入

## 版本选择

- 当前仓库 master 版本为 `3.3.5`；官网示例通常写 `{version}` 或“最新版本”。
- 给业务项目示例时优先使用明确版本，例如 `3.3.5`；如果用户项目已有版本，保持原版本并按其 API 校准。

## Spring Boot

Maven 依赖：

```xml
<dependency>
    <groupId>org.dromara.sms4j</groupId>
    <artifactId>sms4j-spring-boot-starter</artifactId>
    <version>3.3.5</version>
</dependency>
```

`application.yml` 最小示例：

```yaml
sms:
  config-type: yaml
  blends:
    tx1:
      supplier: tencent
      access-key-id: ${SMS_ACCESS_KEY_ID}
      access-key-secret: ${SMS_ACCESS_KEY_SECRET}
      signature: 您的短信签名
      template-id: 您的模板ID
      sdk-app-id: 您的腾讯云SdkAppId
```

发送示例：

```java
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {

    /**
     * 发送验证码短信。
     *
     * @return 发送结果
     */
    @GetMapping("/sms/test")
    public SmsResponse testSend() {
        SmsBlend smsBlend = SmsFactory.getSmsBlend("tx1");
        return smsBlend.sendMessage("18888888888", "123456");
    }
}
```

## JavaSE

Maven 依赖：

```xml
<dependency>
    <groupId>org.dromara.sms4j</groupId>
    <artifactId>sms4j-javase-plugin</artifactId>
    <version>3.3.5</version>
</dependency>
```

资源目录创建 `sms4j.yml`，配置格式同 Spring Boot。启动时先初始化：

```java
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.javase.config.SEInitializer;
import org.dromara.sms4j.core.factory.SmsFactory;

public class SmsDemo {

    /**
     * JavaSE 环境发送短信。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SEInitializer.initializer().fromYaml();
        SmsBlend smsBlend = SmsFactory.getSmsBlend("tx1");
        smsBlend.sendMessage("18888888888", "123456");
    }
}
```

## Solon

Maven 依赖使用 `sms4j-solon-plugin`。配置文件可参考 `app.yml`：

```yaml
sms:
  config-type: yaml
  blends:
    tx1:
      supplier: tencent
      access-key-id: ${SMS_ACCESS_KEY_ID}
      access-key-secret: ${SMS_ACCESS_KEY_SECRET}
      signature: 您的短信签名
      template-id: 您的模板ID
      sdk-app-id: 您的腾讯云SdkAppId
```

发送仍使用 `SmsFactory.getSmsBlend("tx1")`。
