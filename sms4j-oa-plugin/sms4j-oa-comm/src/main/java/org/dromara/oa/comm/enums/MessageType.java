package org.dromara.oa.comm.enums;

import lombok.Getter;

@Getter
public enum MessageType {
    // 钉钉支持类型
    DING_TALK_TEXT("text"),

    DING_TALK_MARKDOWN("markdown"),

    DING_TALK_LINK("link"),

    // 飞书支持类型
    BYTE_TALK_TEXT("text"),
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

}
