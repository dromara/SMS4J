package org.dromara.sms4j.core.load;

import org.dromara.sms4j.api.SmsBlend;

public class LoadServer {
    private SmsBlend smsServer;
    private int weight;
    private int currentWeight;

    protected LoadServer(SmsBlend smsServer, int weight, int currentWeight) {
        this.smsServer = smsServer;
        this.weight = weight;
        this.currentWeight = currentWeight;
    }

    protected SmsBlend getSmsServer() {
        return smsServer;
    }

    protected int getWeight() {
        return weight;
    }

    protected int getCurrentWeight() {
        return currentWeight;
    }

    /**
     * 设置 smsServer
     */
    protected void setSmsServer(SmsBlend smsServer) {
        this.smsServer = smsServer;
    }

    /**
     * 设置 weight
     */
    protected void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * 设置 currentWeight
     */
    protected void setCurrentWeight(int currentWeight) {
        this.currentWeight = currentWeight;
    }
}
