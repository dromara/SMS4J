---
title: 🔧常用工具
index: false
icon: play
---

在日常的项目中我们发现，会有很多常用的方法需要重复的编写，而且这些方法在常见的工具类中并没有存在，我们在这里整理了一部分，后续将会继续添加
这些工具我们把他放置在 `kim.wind.sms.comm.utils` 包中
`SmsUtil`中集成了多数的静态方法


#### 获取一个指定长度的随机字符串，包含数字和大小写字母，不包含符号和空格
```java
SmsUtil.getRandomString(6)
```
#### 获取一个六位长度的随机字符串，包含数字和大小写字母，不包含符号和空格
```java
SmsUtil.getRandomString()
```
#### 获取一个指定长度的随机纯数字组成的字符串
```java
SmsUtil.getRandomInt(4)
```