package org.dromara.sms4j.unisms.core;


/**
 * 初始化统一环境的单例类.
 */
public class Uni {
    /** 模仿SDK版本*/
    public static final String VERSION = "0.0.4";

    public static String signingAlgorithm = "hmac-sha256";
    public static String endpoint = System.getenv().getOrDefault("UNI_ENDPOINT", "https://uni.apistd.com");
    public static String accessKeyId = System.getenv("UNI_ACCESS_KEY_ID");

    private static String accessKeySecret = System.getenv("UNI_ACCESS_KEY_SECRET");
    private static volatile UniClient client;

    private Uni() {
    }

    /**
     * 初始化Uni环境(简单验证模式).
     *
     * @param accessKeyId access key ID
     * @author :Wind
     */
    public static void init(final String accessKeyId) {
        Uni.setAccessKeyId(accessKeyId);
    }

    /**
     * 初始化Uni环境(HMAC验证模式).
     *
     * @param accessKeyId     access key ID
     * @param accessKeySecret access key secret
     * @author :Wind
     */
    public static void init(final String accessKeyId, final String accessKeySecret) {
        Uni.setAccessKeyId(accessKeyId);
        Uni.setAccessKeySecret(accessKeySecret);
    }


    public static void setAccessKeyId(final String accessKeyId) {
        Uni.accessKeyId = accessKeyId;
    }


    public static void setAccessKeySecret(final String accessKeySecret) {
        Uni.accessKeySecret = accessKeySecret;
    }


    public static void setEndpoint(final String endpoint) {
        Uni.endpoint = endpoint;
    }

    /**
     * 返回(如果未初始化则初始化)统一客户端.
     *
     * @return the Uni Client
     * @author :Wind
     */
    public static UniClient getClient(int retryInterval, int maxRetries) {
        if (Uni.client == null) {
            synchronized (Uni.class) {
                if (Uni.client == null) {
                    Uni.client = buildClient(retryInterval, maxRetries);
                }
            }
        }
        return Uni.client;
    }

    public static void setClient(final UniClient client) {
        synchronized (Uni.class) {
            Uni.client = client;
        }
    }

    private static UniClient buildClient(int retryInterval, int maxRetries) {
        UniClient.Builder builder = new UniClient.Builder(Uni.accessKeyId);
        builder.isSimple(true);
        if (Uni.accessKeySecret != null) {
            builder.accessKeySecret(Uni.accessKeySecret);
            builder.isSimple(false);
        }
        builder.endpoint(Uni.endpoint);
        builder.signingAlgorithm(Uni.signingAlgorithm);
        builder.setRetryInterval(retryInterval);
        builder.setMaxRetries(maxRetries);
        return builder.build();
    }
}
