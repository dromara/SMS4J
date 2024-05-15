package org.dromara.sms4j.core.datainterface;



/**
 * SmsReadConfig
 * <p> 读取配置接口，实现该接口中的方法则可以按照自己的形式进行配置的读取
 * <p> 这样只关注最终的配置数据而不关注配置的来源，用户可以自由的选择数据来源的方式</p>
 * <p> SmsBlendsSelectedConfig继承而来的取配置并非在启动阶段完成，而是在方法第一次调用期间完成</p>
 * <p> SmsBlendsBeanConfig继承而来的取配置在启动阶段完成</p>
 * @author :Wind
 * 2023/8/1  12:06
 **/
public interface SmsReadConfig extends SmsBlendsBeanConfig,SmsBlendsSelectedConfig{
}
