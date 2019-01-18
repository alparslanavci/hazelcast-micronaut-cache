package com.hazelcast.integration.micronaut.configuration;

import io.micronaut.cache.CacheConfiguration;
import io.micronaut.context.annotation.EachProperty;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.runtime.ApplicationConfiguration;

/**
 *
 * {@link CacheConfiguration} implementation for Hazelcast caches. The bean is created for each property in
 * configuration as {@code HazelcastProperty.CACHES}.
 *
 */
@EachProperty(HazelcastProperty.CACHES)
public class HazelcastCacheConfiguration extends CacheConfiguration{
    public HazelcastCacheConfiguration(@Parameter String cacheName, ApplicationConfiguration applicationConfiguration) {
        super(cacheName, applicationConfiguration);
    }
}
