package com.hazelcast.integration.micronaut.configuration;

import io.micronaut.context.annotation.*;
import io.micronaut.context.exceptions.BeanInstantiationException;
import io.micronaut.runtime.ApplicationConfiguration;

import javax.inject.Singleton;
import java.util.Map;

/**
 * The factory class for generating {@link HazelcastCacheConfiguration} beans.
 */
@Requires(property = HazelcastProperty.CACHES)
@Singleton
@Factory
public class HazelcastCacheConfigurationFactory {

    /**
     * The {@link HazelcastCacheConfiguration} bean is created for each enabled cache in configuration
     * with {@code HazelcastProperty.CACHES} property.
     *
     * @param cacheName Name of the cache
     * @param cacheConfigurations Configured properties of all caches
     * @param applicationConfiguration {@link ApplicationConfiguration} object
     * @return the generated {@link HazelcastCacheConfiguration} bean
     * @throws BeanInstantiationException if no cache is enabled with the specified cache name
     */
    @EachProperty(HazelcastProperty.CACHES)
    public HazelcastCacheConfiguration
    hazelcastCacheConfiguration(@Parameter String cacheName,
                                @Property(name = HazelcastProperty.CACHES) Map<String, String> cacheConfigurations,
                                ApplicationConfiguration applicationConfiguration){
        if (isCacheEnabled(cacheName, cacheConfigurations)) {
            return new HazelcastCacheConfiguration(cacheName, applicationConfiguration);
        }
        throw new BeanInstantiationException("No cache is enabled with this name: " + cacheName);
    }

    private boolean isCacheEnabled(String cacheName,
                                   Map<String, String> cacheConfigurations) {
        String cacheEnabledProperyName = cacheName + HazelcastProperty.ENABLED;
        return cacheConfigurations.containsKey(cacheEnabledProperyName)
                && cacheConfigurations.get(cacheEnabledProperyName).equals(Boolean.TRUE.toString());
    }
}
