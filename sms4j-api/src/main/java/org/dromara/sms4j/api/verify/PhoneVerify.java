package org.dromara.sms4j.api.verify;

/**
 * PhoneVerify
 * <p> 实现校验手机号合规的接口
 * @author :Wind
 * 2024/3/28  14:15
 **/
public interface PhoneVerify{

    /**
     *  verifyPhone
     *  <p>用于校验手机号是否合理的规则方法，可以尝试重写此方法以改变规则，例如你可以选择使用正则表达式来进行
     * 一系列更加精准和严格的校验，此校验优先级最高，会在黑名单和其他拦截之前执行。
     * 当此校验触发时候，将会直接以异常形式进行抛出，并终止后续向厂商请求的动作，故而不会有返回值。
     * <p>当校验手机号合格时应返回 true 否则返回 false
     * @param phone 被校验的手机号
     * @author :Wind
    */
    default boolean verifyPhone(String phone){
        return phone.length() == 11;
    }
}
