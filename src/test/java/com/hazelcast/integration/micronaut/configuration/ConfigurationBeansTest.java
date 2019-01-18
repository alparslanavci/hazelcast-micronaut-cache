package com.hazelcast.integration.micronaut.configuration;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;
import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConfigurationBeansTest {
    private static ApplicationContext applicationContext;

    private static final String cacheName = "test-cache";

    @BeforeClass
    public static void setupServer() {
        applicationContext = Micronaut.run("-Dhazelcast.caches." +
                cacheName +
                ".enabled=true");
    }

    @AfterClass
    public static void stopServer() {
        if (applicationContext != null) {
            applicationContext.stop();
        }
    }

    @Test
    public void testCacheConfigurationBean(){
        assertNotNull(applicationContext.getBean(HazelcastInstanceConfiguration.class));
    }

    @Test
    public void testHazelcastClientBean(){
        assertNotNull(applicationContext.getBean(HazelcastInstance.class));
    }

    @Test
    public void testHazelcastCacheConfigurationBean(){
        HazelcastCacheConfiguration hazelcastCacheConfiguration =
                applicationContext.getBean(HazelcastCacheConfiguration.class);
        assertNotNull(hazelcastCacheConfiguration);
        assertEquals(cacheName, hazelcastCacheConfiguration.getCacheName());
    }
}
