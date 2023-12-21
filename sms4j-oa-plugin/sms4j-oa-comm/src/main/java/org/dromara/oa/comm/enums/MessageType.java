package org.dromara.oa.comm.enums;

public enum MessageType {
    // 钉钉支持类型
    DINGTALK_TEXT("text"),

    DINGTALK_MARKDOWN("markdown"),

    DINGTALK_LINK("link"),
    // 飞书支持类型

    BYTETALK_TEXT("text"),
//    BYTETALK_MARKDOWN("markdown"),
//    BYTETALK_LINK("link"),

    // 企业微信支持类型
    WETETALK_TEXT("text"),

    WETETALK_MARKDOWN("markdown");
    //暂未支持
//    WETETALK_IMAGE("image"),
//    WETETALK_NEWS("news"),
//    WETETALK_FILE("file"),
//    WETETALK_VOICE("voice"),
//    WETETALK_TEMPLATE_CARD("template_card");

    MessageType(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }
}
