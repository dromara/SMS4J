---
name: sms4j
description: Use when helping a user integrate and use sms4j in their own Java application, including Spring Boot, JavaSE, Solon, Maven dependencies, YAML configuration, multi-provider or multi-account setup, SmsFactory/SmsBlend sending APIs, async or delayed sending, dynamic SmsReadConfig configuration, blacklist/restriction settings, proxy settings, custom interceptors, and troubleshooting based on sms4j.com documentation and the current sms4j 3.x source behavior.
---

# sms4j

## 使用原则

- 先确认用户项目类型：Spring Boot、JavaSE、Solon，或其他 Maven Java 项目。
- 优先给出业务项目可直接复制的依赖、配置和调用代码；不要讲 sms4j 源码开发流程，除非用户要自定义厂商或贡献源码。
- 以 sms4j 3.x 用法为默认：`sms.config-type: yaml`、`sms.blends.<configId>`、`supplier`、`SmsFactory.getSmsBlend(configId)`。
- 官网 https://sms4j.com 是公开使用文档来源；当前仓库 master 源码用于校准字段、API 和边界。
- 真实密钥、手机号、签名、模板 ID 必须用占位符或提醒用户替换，不要编造可用凭证。

## 快速流程

1. 选依赖：Spring Boot 用 `sms4j-spring-boot-starter`，JavaSE 用 `sms4j-javase-plugin`，Solon 用 `sms4j-solon-plugin`。
2. 写配置：在 `application.yml` 或 `sms4j.yml` 中配置 `sms.blends`，每个子节点是一个 `configId`。
3. 发送短信：通过 `SmsFactory.getSmsBlend("configId")` 获取 `SmsBlend`，再调用 `sendMessage(...)`、`massTexting(...)`、异步或延迟方法。
4. 按需扩展：动态配置读 `references/dynamic-config.md`；拦截器、限制、黑名单读 `references/advanced-usage.md`。
5. 排错时先检查：依赖版本、`supplier` 是否正确、配置字段是否能映射到厂商配置、`template-id/templateName` 是否符合厂商模板。

## 引用资料

- 入门依赖、Spring Boot/JavaSE/Solon 配置与发送示例：`references/getting-started.md`
- 厂商标识、公共字段、多账号、代理、常用发送 API：`references/config-and-api.md`
- 标准短信、异步短信、延时短信接口与使用场景：`references/sending-methods.md`
- 动态配置、自定义配置来源、JavaSE 初始化：`references/dynamic-config.md`
- 拦截器、限制、黑名单、常见问题：`references/advanced-usage.md`

## 回答风格

- 用户要“接入”时，直接给最小可运行方案。
- 用户已有项目代码时，先读项目依赖和配置文件，再按现有框架改。
- 用户没有说明厂商时，默认用腾讯或阿里云做示例，并明确 `supplier` 可替换。
- 用户问报错时，先解释可能原因，再给检查顺序和修复建议。
