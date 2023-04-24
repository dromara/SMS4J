package org.dromara.sms4j.comm.utils;

import java.io.Serializable;

/**
 * REST API 函数式接口
 *
 * @param <P> 请求参数
 * @param <R> 响应
 * @author Charles7c
 * @since 2023/4/17 20:57
 */
@FunctionalInterface
public interface RestApiFunction<P, R> extends Serializable {
    R apply(P param);
}
