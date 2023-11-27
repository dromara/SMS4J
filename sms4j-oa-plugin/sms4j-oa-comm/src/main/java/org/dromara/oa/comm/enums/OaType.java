package org.dromara.oa.comm.enums;

public enum OaType {
    DINGTALK("dingding", "https://oapi.dingtalk.com/robot/send?access_token=", true),
    WETALK("wetalk", "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=", true),
    BYTETALK("feishu", "https://open.feishu.cn/open-apis/bot/v2/hook/", true),
    ;

    private String type;
    private String robotUrl;


    OaType(String type, String robotUrl, boolean enabled) {
        this.type = type;
        this.robotUrl = robotUrl;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return robotUrl;
    }
}
