### 互亿无线 --- IHuYi

配置类:`org.dromara.sms4j.huyi.config.HuYiConfig`

| 字段名称              | 作用                                  | 默认值                                        |
|-------------------|-------------------------------------|--------------------------------------------|
| `baseUrl`         | 互亿无线短信服务前缀链接                        | `https://106.ihuyi.com/webservice/sms.php` |
| `singleMsgUrl`    | 单条短信链接(该链接拼接到baseUrl后)              | `?method=Submit`                           |
| `massMsgUrl`      | 批量链接(该链接拼接到baseUrl后)                | `?method=SubmitBatch`                      |
| `enableMd5`       | 选择加密方式<br/>(true为使用MD5加密，false为不使用) | `false`                                    |
| `accessKeyId`     | 账号                                  | `null`                                     |
| `accessKeySecret` | 密码                                  | `null`                                     |