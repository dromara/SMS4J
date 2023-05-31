package org.dromara.sms4j.unisms.core;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.json.JSONUtil;
import com.dtflys.forest.config.ForestConfiguration;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.factory.BeanFactory;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

public class UniClient {

    public static final String USER_AGENT = "uni-java-sdk" + "/" + Uni.VERSION;
    private final ForestConfiguration http = BeanFactory.getForestConfiguration();

    private final String accessKeyId;
    private final String accessKeySecret;
    private final String endpoint;
    private final String signingAlgorithm;

    protected UniClient(Builder b) {
        this.accessKeyId = b.accessKeyId;
        this.accessKeySecret = b.accessKeySecret;
        this.endpoint = b.endpoint;
        this.signingAlgorithm = b.signingAlgorithm;
    }

    private static String getSignature(final String message, final String secretKey) {
        try {
            HMac hMac = new HMac(HmacAlgorithm.HmacSHA256, secretKey.getBytes());
            byte[] bytes =  hMac.digest(message.getBytes());
            return Base64.encode(bytes);
        } catch (Exception e) {
            return null;
        }
    }

    private static String queryStringify(final Map<String, Object> params) {
        Map<String, Object> sortedMap = new TreeMap<>(new MapKeyComparator());
        sortedMap.putAll(params);
        StringBuilder sb = new StringBuilder();
        Iterator<?> iter = sortedMap.entrySet().iterator();

        while (iter.hasNext()) {
            if (sb.length() > 0) {
                sb.append('&');
            }
            Entry<?, ?> entry = (Entry<?, ?>) iter.next();
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }

        return sb.toString();
    }

    private Map<String, Object> sign(final Map<String, Object> query) {
        if (this.accessKeySecret != null) {
            query.put("algorithm", this.signingAlgorithm);
            query.put("timestamp", new Date().getTime());
            query.put("nonce", UUID.randomUUID().toString().replaceAll("-", ""));

            String strToSign = UniClient.queryStringify(query);
            query.put("signature", UniClient.getSignature(strToSign, this.accessKeySecret));
        }
        return query;
    }

    /**
     * request
     * <p>向 uni-sms发送请求
     *
     * @param action 接口名称
     * @author :Wind
     */
    public UniResponse request(final String action, final Map<String, Object> data) throws SmsBlendException {
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("action", action);
        query.put("accessKeyId", this.accessKeyId);

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", USER_AGENT);
        headers.put("Content-Type", "application/json;charset=utf-8");
        headers.put("Accept", "application/json");
        String str = http.post(this.endpoint)
                .addHeader(headers)
                .addQuery(this.sign(query))
                .addBody(JSONUtil.toJsonStr(data))
                .successWhen(((req, res) -> {
                    return res.noException() &&   // 请求过程没有异常
                            res.statusIsNot(500); // 不能是 500
                }))
                .onError((ex, req, res) -> {
                    throw new SmsBlendException(ex.getMessage());
                })
                .executeAsString();
        return new UniResponse(JSONUtil.parseObj(str));
    }

    public static class Builder {
        private String accessKeyId;
        private String accessKeySecret;
        private String endpoint;
        private String signingAlgorithm;

        public Builder(final String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public Builder(final String accessKeyId, final String accessKeySecret) {
            this.accessKeyId = accessKeyId;
            this.accessKeySecret = accessKeySecret;
        }

        public Builder accessKeySecret(final String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
            return this;
        }

        public Builder endpoint(final String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder signingAlgorithm(final String signingAlgorithm) {
            this.signingAlgorithm = signingAlgorithm;
            return this;
        }

        public UniClient build() {
            return new UniClient(this);
        }
    }
}

class MapKeyComparator implements Comparator<String> {
    @Override
    public int compare(String str1, String str2) {
        return str1.compareTo(str2);
    }
}
