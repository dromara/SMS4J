---
# 这是文章的标题
title: 🐶京东云国内短信
# 这是页面的图标
icon: 
# 这是侧边栏的顺序
order: 8
# 设置作者
author: Charles7c
# 设置写作时间
date: 2023-04-11
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
  jdcloud:
    # 访问键标识
    accessKeyId: your accessKeyId
    # 访问键秘钥
    accessKeySecret: your accessKeySecret
    # 短信签名 ID
    signature: your signId
    # 模板 ID（发送固定模板短信时使用的此配置）
    templateId: your templateId
    # 地域信息
    region: cn-north-1
```