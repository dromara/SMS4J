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
//    BYTE_TALK_MARKDOWN("markdown"),
//    BYTE_TALK_LINK("link"),

    // 企业微信支持类型
    WE_TALK_TEXT("text"),

    WE_TALK_MARKDOWN("markdown"),
    WE_TALK_NEWS("news");
    //暂未支持
//    WE_TALK_IMAGE("image"),
//    WE_TALK_FILE("file"),
//    WE_TALK_VOICE("voice"),
//    WE_TALK_TEMPLATE_CARD("template_card");

    MessageType(String name) {
        this.name = name;
    }

    private final String name;

}
