package sms4j.interceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MySecIntercepterStrategy implements IMyIntercepterStrategy {

    public void doSomeThing(){
        System.out.println("替换过的自定义拦截器一顿处理");
    }

    @Override
    public Class<?> aPendingProblemWith() {
        return MyInterceptor.class;
    }
}
