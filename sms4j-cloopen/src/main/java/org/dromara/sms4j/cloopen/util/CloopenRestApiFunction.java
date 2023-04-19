package org.dromara.sms4j.cloopen.util;

import java.io.Serializable;

/**
 * 容联云 REST API 函数式接口
 *
 * @param <P> 请求参数
 * @param <R> 响应
 * @author Charles7c
 * @since 2023/4/17 20:57
 */
@FunctionalInterface
public interface CloopenRestApiFunction<P, R> extends Serializable {
    R apply(P param);
}
