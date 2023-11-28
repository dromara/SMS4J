package org.dromara.oa.core.provider.service;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import org.dromara.oa.api.OaCallBack;
import org.dromara.oa.api.OaSender;
import org.dromara.oa.comm.config.OaSupplierConfig;
import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.comm.entity.Response;
import org.dromara.oa.comm.enums.MessageType;
import org.dromara.oa.core.provider.factory.OaBeanFactory;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author dongfeng
 * 2023-10-22 21:03
 */
public abstract class AbstractOaBlend<C extends OaSupplierConfig> implements OaSender {

    @Getter
    private final String configId;

    private final C config;

    protected final Executor pool;

    protected final PriorityBlockingQueue<Request> priorityQueueMap;

    protected AbstractOaBlend(C config, Executor pool) {
        this.configId = StrUtil.isEmpty(config.getConfigId()) ? getSupplier() : config.getConfigId();
        this.config = config;
        this.pool = pool;
        this.priorityQueueMap = OaBeanFactory.initPriorityBlockingQueue();
        priorityQueueMapThreadInit();
    }

    protected AbstractOaBlend(C config) {
        this.configId = StrUtil.isEmpty(config.getConfigId()) ? getSupplier() : config.getConfigId();
        this.config = config;
        this.pool = OaBeanFactory.getExecutor();
        this.priorityQueueMap = OaBeanFactory.initPriorityBlockingQueue();
        priorityQueueMapThreadInit();
    }

    protected C getConfig() {
        return config;
    }

    protected void priorityQueueMapThreadInit() {
        Boolean status = OaBeanFactory.getPriorityExecutorThreadStatus();
        if(Boolean.FALSE.equals(status)){
            OaBeanFactory.setPriorityExecutorThreadStatus(true);
            pool.execute(() -> {
                Thread.currentThread().setName("oa-priorityQueueMap-thread");
                while (!Thread.currentThread().isInterrupted()) {
                    Request request = priorityQueueMap.poll();
                    if (!Objects.isNull(request)) {
                        pool.execute(() -> {
                            System.out.println("优先级为"+request.getPriority()+"已发送");
                            sender(request, request.getMessageType());
                        });
                    }
                }
            });
        }
    }


    public final void senderAsync(Request request, MessageType messageType) {
        pool.execute(() -> sender(request, messageType));
    }

    public final void senderAsync(Request request, MessageType messageType, OaCallBack callBack) {
        CompletableFuture<Response> future = CompletableFuture.supplyAsync(() -> sender(request, messageType));
        future.thenAcceptAsync(callBack::callBack);
    }

    public final void senderAsyncByPriority(Request request, MessageType messageType) {
        request.setMessageType(messageType);
        priorityQueueMap.offer(request);
    }
}