<template>
  <div>
    <div class="header">
      <div class="header-left">
        <img class="logo" src="../assets/logo.png" alt="" />
        <span class="sms">sms aggregation</span>
      </div>
      <div class="header-right">
        <input type="text" />

        <a href=""><img src="../assets/首页2.png" alt="" />首页</a>
        <a href=""><img src="../assets/项目2.png" />文档</a>
        <a href=""><img src="../assets/项目2.png" />插件</a>
        <a href=""><img src="../assets/项目2.png" />案例</a>
        <a href=""><img src="../assets/项目2.png" />支持</a>
        <a href=""><img src="../assets/项目2.png" />更新记录</a>
        <a href=""><img src="../assets/项目2.png" />开发团队</a>
        <a href=""><img src="../assets/项目2.png" />参与贡献</a>
        <a href=""><img src="../assets/项目2.png" />推荐</a>
        <a href=""><img src="../assets/项目2.png" />源码</a>
      </div>
    </div>
    <div class="main">
      <img class="biglogo" src="../assets/logo.png" alt="" />
      <h1>sms aggregation</h1>
      <p>聚合式短信发送工具,降低开发难度,统一使用标准</p>
      <button>开始使用</button>
    </div>
    <div class="gongneng">
      <div><h3>容易上手</h3></div>
      <div><h3>简单优雅</h3></div>
      <div><h3>拓展灵活</h3></div>
    </div>
    <div>
      <h2>特别赞助</h2>
    </div>
    <div>
      <h2>特性</h2>
    </div>
    <div>
      <h2>最新版本</h2>
    </div>
    <div>
      <h2>代码托管</h2></h2>
    </div>
    <div>
      <h2>知识星球</h2>
    </div>
    <div>
      <h2>开源社区平台</h2>
    </div>
    <div>
      <h2>友情链接</h2>
    </div>
   
    <div class="footer">
      <p>
        © 2022 wind 冀ICP备2021004949号-3 | 图标引用自阿里矢量图库个人使用
        如侵删
      </p>
    </div>
  </div>
</template>

<script></script>
<style>
a {
  margin-left: 10px;
  margin-right: 5px;
  /* line-height: 50px; */
  display: flex;
  justify-content: center;
  align-items: center;
}

.header {
  height: 50px;
  display: flex;
  justify-content: space-between;
  width: 100%;
  background: rgb(240, 240, 240);
  line-height: 50px;
}
.header-left {
  display: flex;
  justify-content: center;
  align-items: center;
}
.header-right {
  display: flex;
  justify-content: center;
  align-items: center;
}
.sms {
  font-size: 20px;
  font-weight: 1000;
}
.logo {
  width: 50px;
}
img {
  width: 16px;
}
input {
  height: 16px;
  border-radius: 50px;
}
.biglogo {
  width: 300px;
  /* display: block;
  margin: 0 auto;
  margin-top: 100px; */
}
.main {
  width: 500px;
  height: 500px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  margin: 0 auto;
  margin-top: 100px;
}
.gongneng {
  margin-top: 100px;
  display: flex;
  justify-content: space-around;
}
.footer {
  text-align: center;
}
</style>
