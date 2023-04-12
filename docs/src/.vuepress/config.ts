import {defineUserConfig} from "vuepress";
import {hopeTheme} from "vuepress-theme-hope";

// @ts-ignore
export default defineUserConfig({
    base: "/",
    lang: "zh-CN",
    locales: {
        "/": {
            lang: "zh-CN",
            title: "短信聚合💖",
            description: "短信聚合文档",
        },
    },
    theme: hopeTheme({
        logo:"/dlogo.png",
        iconAssets: "//at.alicdn.com/t/c/font_3977841_tfbu1j5yn.js",
        themeColor: {
            blue: "#2196f3",
            red: "#f26d6d",
            green: "#3eaf7c",
            orange: "#fb9b5f",
        },
        navbar: [
            {
                text: "🏡首页",
                link: "/README.md",
                icon: "lightbulb",
                // // 仅在 `/zh/guide/` 激活
                // activeMatch: "^/zh/guide/$",
            },
            {text: "📖文档", link: "/doc/start/README.md", icon: "config"},
            {
                text: "📒javaDoc",
                link: "https://apidoc.gitee.com/the-wind-is-like-a-song/sms_aggregation",
                icon: "config"
            },
            {text: "🏮gitee", link: "https://gitee.com/dromara/sms_aggregation", icon: "config"},
            {text: "🪀github", link: "https://github.com/dromara/SMSAggregation", icon: "config"},
            {text: "🤝Dromara组织", link: "https://dromara.org/zh/", icon: "config"},
            {text: "🔍常见问题", link: "/doc/start/issue.md", icon: "config"},
            {text: "👪加入交流群", link: "/doc/start/group.md", icon: "config"},

        ],
        sidebar: [
            {
                text: "开始",
                // 可选的, 分组标题对应的图标
                icon: "tip",
                // 可选的, 分组标题对应的链接
                link: "/doc/start/",
                // 可选的, 设置分组是否可以折叠，默认值是 false,
                collapsible: false,
                // 必要的，分组的子项目
                children: [
                    "/doc/start/README.md" /* /foo/index.html */,
                    "/doc/start/springboot.md" /* /foo/geo.html */,
                    "/doc/start/jinjiepeizhi.md",
                ],
            },
            {
                text: "支持厂商",
                // 可选的, 分组标题对应的图标
                icon: "tip",
                // 可选的, 分组标题对应的链接
                link: "/doc/supplier/",
                // 可选的, 设置分组是否可以折叠，默认值是 false,
                collapsible: false,
                // 必要的，分组的子项目
                children: [
                    "/doc/supplier/README.md" /* /foo/index.html */,
                    "/doc/supplier/aliyun.md" /* /foo/geo.html */,
                    "/doc/supplier/unisms.md",
                    "/doc/supplier/tencent.md",
                    "/doc/supplier/yunpian.md",
                    "/doc/supplier/huawei.md",
                    "/doc/supplier/jdcloud.md",
                    "/doc/supplier/cloopen.md",
                    "/doc/supplier/emay.md",

                ],
            },
            {
                text: "API手册",
                // 可选的, 分组标题对应的图标
                icon: "tip",
                // 可选的, 分组标题对应的链接
                link: "/doc/api/",
                // 可选的, 设置分组是否可以折叠，默认值是 false,
                collapsible: false,
                // 必要的，分组的子项目
                children: [
                    "/doc/api/README.md" /* /foo/index.html */,
                    "/doc/api/tool.md" /* /foo/index.html */,
                ],
            },
        ],
        plugins:{
            components:{
                rootComponents:{
                    notice:[
                        {
                            path:"/",
                            title:"📢公告",
                            content:"🎉SMS-Aggregator正式加入dromara开源组织🎉\n\t为往圣继绝学，一个人或许能走的更快，但一群人会走的更远。",
                            actions:[
                                {
                                    text:"gitee",
                                    link:"https://gitee.com/the-wind-is-like-a-song/sms_aggregation",
                                    type:"primary"
                                },
                                {
                                    text:"github",
                                    link:"https://github.com/fengruge/sms_aggregation",
                                    type:"default"
                                }
                            ]
                        },
                        {
                            path:"/doc/start/",
                            title:"📫征集令",
                            content:"🎀🎀在这里我们面向全员征集参与者和使用者🎀🎀如果你的公司或项目正在使用我们的工具，请在lssues中告诉我们，我们将会在征求您的同意后展示在官网",
                            actions:[
                                {
                                    text:"lssues",
                                    link:"https://gitee.com/the-wind-is-like-a-song/sms_aggregation/issues",
                                    type:"primary"
                                },
                            ]
                        }
                    ]
                }
            }
        }

    }),
    shouldPrefetch: false,
});
