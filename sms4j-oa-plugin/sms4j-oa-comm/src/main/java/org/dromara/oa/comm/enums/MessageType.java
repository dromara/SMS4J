package org.dromara.oa.comm.enums;

public enum MessageType {
    // text 类型
    TEXT("text"),

    // md 格式
    MARKDOWN("markdown"),

    // link 格式
    LINK("link");

    MessageType(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }
}
