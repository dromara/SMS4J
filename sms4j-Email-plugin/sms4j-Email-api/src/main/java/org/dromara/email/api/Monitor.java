package org.dromara.email.api;

import org.dromara.email.comm.entity.MonitorMessage;

/**
 * Monitor
 * <p> 监听接口，实现此接口用于监听并获取邮件消息
 * @author :Wind
 * 2023/7/18  15:57
 **/
public interface Monitor {

    /**
     *  monitor
     * <p> 监听系统的邮件消息，
     * @param monitorMessage 系统监听到的消息内容
     * @return true为标记已读，否则不标记
     * @author :Wind
    */
     boolean monitor(MonitorMessage monitorMessage);
}
