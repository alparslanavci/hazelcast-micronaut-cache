package com.hazelcast.integration.micronaut;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.integration.micronaut.configuration.HazelcastInstanceConfiguration;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;
import io.micronaut.context.annotation.*;
import javax.inject.Singleton;


/**
 *
 * Factory class for generating {@code HazelcastInstance}'s. Its bean requires a {@link HazelcastInstanceConfiguration} bean
 * and created as singleton.
 *
 */
@Requires(beans = HazelcastInstanceConfiguration.class)
@Singleton
@Factory
public class HazelcastInstanceFactory {
    private final ILogger log = Logger.getLogger(getClass());

    /**
     *
     * {@code HazelcastInstance} factory method. It generates a singleton bean at application initialization.
     *
     * @param hazelcastInstanceConfiguration the configuration for creating {@code HazelcastInstance}
     *
     * @return generated {@code HazelcastInstance}
     */
    @Bean(preDestroy = "shutdown")
    @Singleton
    @Primary
    @Context
    public HazelcastInstance hazelcastInstance(@Primary HazelcastInstanceConfiguration hazelcastInstanceConfiguration) {
        String configMessage = "the classpath";
        boolean configuredAsClient = hazelcastInstanceConfiguration.isClient();
        String xmlConfigPath = hazelcastInstanceConfiguration.getXmlConfigPath();
        ClientConfig clientConfig = null;
        Config config = null;

        if (xmlConfigPath != null && !xmlConfigPath.isEmpty()) {
            try {
                if (configuredAsClient) {
                    clientConfig = new XmlClientConfigBuilder(xmlConfigPath).build();
                }else {
                    config = new XmlConfigBuilder(xmlConfigPath).build();
                }
                configMessage = "the configured XML path: " + xmlConfigPath;
            } catch (Exception e) {
                log.warning("Exception when loading the configuration from the configured XML path: " + xmlConfigPath, e);
            }
        } else {
            log.warning("Configuration XML path is empty.");
        }

        log.info("Hazelcast is starting with using the configuration in " + configMessage);
        return configuredAsClient ?
                HazelcastClient.newHazelcastClient(clientConfig) :
                Hazelcast.newHazelcastInstance(config);
    }
}
