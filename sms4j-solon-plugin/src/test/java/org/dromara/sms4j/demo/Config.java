package org.dromara.sms4j.demo;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.SingleServerConfig;

import java.util.Properties;

/**
 * @author noear 2023/5/16 created
 */
@Configuration
public class Config {
    @Bean
    public RedissonClient redisInit(@Inject("${sms4j.redis}") Properties props) {
        return build(props);
    }

    public RedissonClient build(Properties prop) {
        String server_str = prop.getProperty("server");
        String db_str = prop.getProperty("db");
        String user_str = prop.getProperty("user");
        String password_str = prop.getProperty("password");


        int db = 0;

        if (Utils.isNotEmpty(db_str)) {
            db = Integer.parseInt(db_str);
        }

        //
        // 开始实例化 redissonClient
        //
        org.redisson.config.Config config = new org.redisson.config.Config();

        if (server_str.contains(",")) {
            //集群
            ClusterServersConfig serverConfig = config.useClusterServers();

            //注入一般配置
            Utils.injectProperties(serverConfig, prop);

            //设置关键配置
            String[] address = resolveServers(server_str.split(","));
            serverConfig.addNodeAddress(address)
                    .setUsername(user_str)
                    .setPassword(password_str);
        } else {
            //单例
            SingleServerConfig serverConfig = config.useSingleServer();

            //注入一般配置
            Utils.injectProperties(serverConfig, prop);

            //设置关键配置
            String[] address = resolveServers(server_str);
            serverConfig.setAddress(address[0])
                    .setUsername(user_str)
                    .setPassword(password_str)
                    .setDatabase(db);
        }

        return Redisson.create(config);
    }

    private String[] resolveServers(String... servers) {
        String[] uris = new String[servers.length];

        for (int i = 0; i < servers.length; i++) {
            String sev = servers[i];

            if (sev.contains("://")) {
                uris[i] = sev;
            } else {
                uris[i] = "redis://" + sev;
            }
        }

        return uris;
    }
}
