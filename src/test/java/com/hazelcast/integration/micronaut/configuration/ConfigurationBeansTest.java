package com.hazelcast.integration.micronaut.configuration;

import com.hazelcast.core.HazelcastInstance;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.exceptions.BeanInstantiationException;
import io.micronaut.inject.qualifiers.Qualifiers;
import io.micronaut.runtime.Micronaut;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConfigurationBeansTest {
    private static ApplicationContext applicationContext;

    private static final String enabledCacheName = "enabled-test-cache";
    private static final String disabledCacheName = "disabled-test-cache";

    @BeforeClass
    public static void setupServer() {
        applicationContext = Micronaut.run("-Dhazelcast.caches." + enabledCacheName + ".enabled=true",
                "-Dhazelcast.caches." + disabledCacheName + ".enabled=false");
    }

    @AfterClass
    public static void stopServer() {
        if (applicationContext != null) {
            applicationContext.stop();
        }
    }

    @Test
    public void testHazelcastInstanceConfigurationBean(){
        assertNotNull(applicationContext.getBean(HazelcastInstanceConfiguration.class));
    }

    @Test
    public void testHazelcastInstanceBean(){
        assertNotNull(applicationContext.getBean(HazelcastInstance.class));
    }

    @Test
    public void testEnabledHazelcastCacheConfigurationBean(){
        HazelcastCacheConfiguration hazelcastCacheConfiguration =
                applicationContext.getBean(HazelcastCacheConfiguration.class, Qualifiers.byName(enabledCacheName));
        assertNotNull(hazelcastCacheConfiguration);
        assertEquals(enabledCacheName, hazelcastCacheConfiguration.getCacheName());
    }

    @Test(expected = BeanInstantiationException.class)
    public void testDisabledHazelcastCacheConfigurationBean(){
        applicationContext.getBean(HazelcastCacheConfiguration.class, Qualifiers.byName(disabledCacheName));
    }
}
