package org.dromara.email.jakarta.core.factory;

import org.dromara.email.jakarta.api.Monitor;
import org.dromara.email.jakarta.comm.config.MailImapConfig;
import org.dromara.email.jakarta.core.service.MonitorService;

import java.util.HashMap;
import java.util.Map;

/**
 * MonitorFactory
 * <p>  监听系统工厂，通过向工厂添加配置，可以启动或关闭监听
 * 监听器通过连接指定的imap服务器监听指定的邮箱，并以异步轮询的形式进行消息的监听，除去轮询时间开销外，个别imap服务器本身存在延迟，故而邮件的监听可能存在较大延迟，
 * 在配置中可以设置轮询的间隔时间，以增大或缩小监听灵敏度。灵敏度调节周期为秒级，默认周期为5秒。
 * <p>需要注意的是，监听器所使用的线程并非任何线程池中的线程，如想停止某邮箱的监听，只需要调用stop方法即可，他会在完成当前接收的任务后正常的终结线程。
 * 现成终结后可以通过调用start方法重新启用。
 * @author :Wind
 * 2023/7/18  17:06
 **/
public class MonitorFactory {

    private final static Map<String, MonitorService> SERVICES = new HashMap<>();

    /**
     *  put
     * <p> 添加一个配置至系统中，并绑定接收消息的对象
     * @param key 监听标识
     * @param config 监听配置
     * @param monitor 回调对象
     * @author :Wind
     */
    public static void put(String key, MailImapConfig config, Monitor monitor){
        SERVICES.put(key,new MonitorService(config,monitor));
    }

    /**
     *  start
     * <p> 开始监听指定标识的邮箱
     * @param  key 标识
     * @author :Wind
     */
    public static void start(String key){
        SERVICES.get(key).start();
    }

    /**
     *  stop
     * <p> 停止监听指定标识的邮箱
     * @param key 标识
     * @author :Wind
     */
    public static void stop(String key){
        SERVICES.get(key).stop();
    }

    /**
     *  getConfig
     * <p> 获取指定标识的配置信息
     * @param key 标识
     * @author :Wind
     */
    public static MailImapConfig getConfig(String key) {
        return SERVICES.get(key).getMailImapConfig();
    }
}
