package com.hazelcast.integration.micronaut.configuration;

import io.micronaut.cache.CacheConfiguration;
import io.micronaut.runtime.ApplicationConfiguration;

/**
 *
 * {@link CacheConfiguration} implementation for Hazelcast caches.
 *
 */
public class HazelcastCacheConfiguration extends CacheConfiguration{
    protected HazelcastCacheConfiguration(String cacheName, ApplicationConfiguration applicationConfiguration) {
        super(cacheName, applicationConfiguration);
    }
}
