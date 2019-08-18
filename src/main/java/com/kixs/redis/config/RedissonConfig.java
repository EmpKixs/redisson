package com.kixs.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author wangbing
 * @version 1.0, 2018/11/9
 */
@Configuration
public class RedissonConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        String schema = redisProperties.isSsl() ? "rediss://" : "redis://";
        // redis集群模式配置
        if (redisProperties.getCluster() != null
                && !CollectionUtils.isEmpty(redisProperties.getCluster().getNodes())) {
            ClusterServersConfig clusterServersConfig = config.useClusterServers();
            clusterServersConfig.setScanInterval(2000);
            redisProperties.getCluster().getNodes().forEach(node ->
                    clusterServersConfig.addNodeAddress(schema + node));
        } else if (redisProperties.getSentinel() != null
                && !StringUtils.isEmpty(redisProperties.getSentinel().getMaster())) {
            // redis哨兵模式配置
            SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
            sentinelServersConfig.setMasterName(redisProperties.getSentinel().getMaster());
            if (!CollectionUtils.isEmpty(redisProperties.getSentinel().getNodes())) {
                redisProperties.getSentinel().getNodes().forEach(node ->
                        sentinelServersConfig.addSentinelAddress(schema + node));
            }
        } else {
            // redis单节点配置
            SingleServerConfig singleServerConfig = config.useSingleServer();
            singleServerConfig.setAddress(schema + redisProperties.getHost() + ":" + redisProperties.getPort());
            singleServerConfig.setDatabase(redisProperties.getDatabase());
            if (!StringUtils.isEmpty(redisProperties.getPassword())) {
                singleServerConfig.setPassword(redisProperties.getPassword());
            }
        }

        // 其他配置项均采用默认值
        return Redisson.create(config);
    }
}
