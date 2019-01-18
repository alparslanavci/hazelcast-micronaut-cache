package com.hazelcast.integration.micronaut;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.integration.micronaut.configuration.HazelcastCacheConfiguration;
import com.hazelcast.integration.micronaut.util.TypeUtils;
import io.micronaut.cache.AsyncCache;
import io.micronaut.cache.SyncCache;
import io.micronaut.context.annotation.EachBean;
import io.micronaut.context.annotation.Primary;
import io.micronaut.core.type.Argument;

import java.util.Optional;
import java.util.function.Supplier;

/**
 *
 * The implementation of {@link SyncCache} for Hazelcast. It uses {@link IMap} as cache backend and works for both
 * client and embedded setup of Hazelcast.
 *
 * Its bean is created for each {@link HazelcastCacheConfiguration} bean in the context.
 *
 */
@EachBean(HazelcastCacheConfiguration.class)
@Primary
public class HazelcastSyncCache implements SyncCache<IMap<?, ?>>{

    private final HazelcastCacheConfiguration cacheConfiguration;
    private final IMap<Object, Object> map;

    public HazelcastSyncCache(HazelcastCacheConfiguration cacheConfiguration,
                              HazelcastInstance hazelcastInstance) {
        this.cacheConfiguration = cacheConfiguration;
        map = hazelcastInstance.getMap(cacheConfiguration.getCacheName());
    }

    @Override
    public <T> Optional<T> get(Object key, Argument<T> requiredType) {
        Object value = map.get(key);
        Class<T> type = TypeUtils.wrap(requiredType.getType());
        if (value != null && type.isInstance(value)){
            return Optional.of(type.cast(value));
        }
        return Optional.empty();
    }

    @Override
    public <T> T get(Object key, Argument<T> requiredType, Supplier<T> supplier) {
        Optional<T> optional = get(key, requiredType);
        if (optional.isPresent()){
            return optional.get();
        }

        T value = supplier.get();
        put(key, value);
        return value;
    }

    @Override
    public <T> Optional<T> putIfAbsent(Object key, T value) {
        Object ifAbsent = map.putIfAbsent(key, value);
        if (ifAbsent == null) {
            return Optional.empty();
        }

        return Optional.of((T)ifAbsent);
    }

    @Override
    public void put(Object key, Object value) {
        map.put(key, value);
    }

    @Override
    public void invalidate(Object key) {
        map.remove(key);
    }

    @Override
    public void invalidateAll() {
        map.clear();
    }

    @Override
    public <T> T get(Object key, Class<T> requiredType, Supplier<T> supplier) {
        return get(key, Argument.of(requiredType), supplier);
    }

    @Override
    public <T> Optional<T> get(Object key, Class<T> requiredType) {
        return get(key, Argument.of(requiredType));
    }

    @Override
    public AsyncCache<IMap<?, ?>> async() {
        throw new RuntimeException("Async cache is not yet supported for Hazelcast.");
    }

    @Override
    public String getName() {
        return cacheConfiguration.getCacheName();
    }

    @Override
    public IMap<?, ?> getNativeCache() {
        return map;
    }
}
