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
  # 短信服务商
  supplier: uni-sms
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