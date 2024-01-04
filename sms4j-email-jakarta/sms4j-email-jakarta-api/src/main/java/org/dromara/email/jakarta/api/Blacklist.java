package org.dromara.email.jakarta.api;

import java.util.List;

/**
 * Blacklist
 * <p> 黑名单实现 实现此接口，发送邮件时将自动排除调黑名单中的收件人
 * @author :Wind
 * 2023/6/8  23:05
 **/
public interface Blacklist {

    List<String> getBlacklist();
}
