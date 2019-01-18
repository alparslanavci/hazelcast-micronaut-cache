package com.hazelcast.integration.micronaut.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;

/**
 *
 * The class for keeping the {@link com.hazelcast.core.HazelcastInstance} configuration.
 *
 */
@ConfigurationProperties(HazelcastProperty.PREFIX)
@Primary
@Requires(property = HazelcastProperty.PREFIX)
public class HazelcastInstanceConfiguration {
    private boolean client = Boolean.FALSE;
    private String xmlConfigPath;

    public String getXmlConfigPath() {
        return xmlConfigPath;
    }

    public void setXmlConfigPath(String xmlConfigPath) {
        this.xmlConfigPath = xmlConfigPath;
    }

    public boolean isClient() {
        return client;
    }

    public void setClient(boolean client) {
        this.client = client;
    }
}
