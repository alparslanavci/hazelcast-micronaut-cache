package com.hazelcast.integration.micronaut;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.integration.micronaut.configuration.HazelcastInstanceConfiguration;
import com.hazelcast.integration.micronaut.configuration.HazelcastProperty;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HazelcastInstanceFactoryTest {
    private static final String XML_CLIENT_CONFIG_PATH = "src/test/resources/another-hazelcast-client.xml";
    private static final String XML_MEMBER_CONFIG_PATH = "src/test/resources/another-hazelcast.xml";
    private static ApplicationContext applicationContext;

    private static HazelcastInstance server1;
    private static final String cacheName = "test-cache";

    @After
    public void after() {
        if (server1 != null){
            server1.shutdown();
        }
        if (applicationContext != null) {
            applicationContext.stop();
        }
    }

    @Test
    public void testHazelcastInstanceBeanAsClient() throws IOException {
        server1 = Hazelcast.newHazelcastInstance();
        applicationContext = Micronaut.run("-Dhazelcast.client=true",
                "-Dhazelcast.xmlConfigPath=" + XML_CLIENT_CONFIG_PATH,
                "-D" + HazelcastProperty.CACHES + "." + cacheName + ".enabled=true");

        ClientConfig clientConfig = new XmlClientConfigBuilder(XML_CLIENT_CONFIG_PATH).build();
        HazelcastInstance hazelcastInstance = applicationContext.getBean(HazelcastInstance.class);
        assertNotNull(hazelcastInstance);
        assertEquals(clientConfig.getInstanceName(), hazelcastInstance.getName());
    }

    @Test
    public void testHazelcastInstanceBeanAsEmbedded() throws IOException {
        applicationContext = Micronaut.run("-Dhazelcast.client=false",
                "-Dhazelcast.xmlConfigPath=" + XML_MEMBER_CONFIG_PATH,
                "-D" + HazelcastProperty.CACHES + "." + cacheName + ".enabled=true");

        Config config = new XmlConfigBuilder(XML_MEMBER_CONFIG_PATH).build();
        HazelcastInstance hazelcastInstance = applicationContext.getBean(HazelcastInstance.class);
        assertNotNull(hazelcastInstance);
        assertEquals(config.getInstanceName(), hazelcastInstance.getName());
    }
}
