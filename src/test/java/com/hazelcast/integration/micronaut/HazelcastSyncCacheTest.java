package com.hazelcast.integration.micronaut;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.integration.micronaut.HazelcastSyncCache;
import com.hazelcast.integration.micronaut.SomeKey;
import com.hazelcast.integration.micronaut.SomeValue;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;
import org.junit.*;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HazelcastSyncCacheTest {
    private static ApplicationContext applicationContext;
    private static HazelcastSyncCache hazelcastSyncCache;

    private static HazelcastInstance client;
    private static final String cacheName = "test-cache";
    private SomeKey key;
    private SomeValue value;

    @BeforeClass
    public static void setupServer() {
        applicationContext = Micronaut.run("-Dhazelcast.caches." + cacheName + ".enabled=true");
        hazelcastSyncCache = applicationContext.getBean(HazelcastSyncCache.class);
        client = HazelcastClient.newHazelcastClient();
    }

    @AfterClass
    public static void stopServer() {
        if (client != null){
            client.shutdown();
        }
        if (applicationContext != null) {
            applicationContext.stop();
        }
    }

    @Before
    public void before(){
        Random random = new Random();
        key = new SomeKey(UUID.randomUUID().toString(), random.nextInt());
        value = new SomeValue(UUID.randomUUID().toString(), random.nextInt());
    }

    @After
    public void  after(){
        client.getMap(cacheName).clear();
    }

    @Test
    public void testGet(){
        client.getMap(cacheName).put(key, value);
        Optional<SomeValue> actualValue = hazelcastSyncCache.get(key, SomeValue.class);
        assertTrue(actualValue.isPresent());
        assertEquals(value, actualValue.get());
    }

    @Test
    public void testGetWithSupplier(){
        SomeValue suppliedValue = new SomeValue("foo", 1);
        SomeValue actualValue = hazelcastSyncCache.get(key, SomeValue.class, () -> suppliedValue);
        assertEquals(suppliedValue, actualValue);
    }

    @Test
    public void testPutIfAbsent(){
        SomeValue someOtherValue = new SomeValue("foo", 1);
        Optional<SomeValue> actualValue = hazelcastSyncCache.putIfAbsent(key, value);
        assertTrue(!actualValue.isPresent());
        actualValue = hazelcastSyncCache.putIfAbsent(key, someOtherValue);
        assertTrue(actualValue.isPresent());
        assertEquals(value, actualValue.get());
    }

    @Test
    public void testPut(){
        hazelcastSyncCache.put(key, value);
        SomeValue actualValue = (SomeValue) client.getMap(cacheName).get(key);
        assertNotNull(actualValue);
        assertEquals(value, actualValue);
    }

    @Test
    public void testPutAndGet(){
        Optional<SomeValue> actualValue = hazelcastSyncCache.get(key, SomeValue.class);
        assertTrue(!actualValue.isPresent());
        hazelcastSyncCache.put(key, value);
        actualValue = hazelcastSyncCache.get(key, SomeValue.class);
        assertTrue(actualValue.isPresent());
        assertEquals(value, actualValue.get());
    }


    @Test
    public void testPutGetWithPrimitiveTypes(){
        int primitiveKey = 1;
        int primitiveValue = 1;
        Optional<Integer> actualValue = hazelcastSyncCache.get(primitiveKey, int.class);
        assertTrue(!actualValue.isPresent());
        hazelcastSyncCache.put(primitiveKey, primitiveValue);
        actualValue = hazelcastSyncCache.get(primitiveKey, int.class);
        assertTrue(actualValue.isPresent());
        assertEquals(primitiveValue, actualValue.get().intValue());
    }

    @Test
    public void testInvalidate(){
        client.getMap(cacheName).put(key, value);
        Optional<SomeValue> actualValue = hazelcastSyncCache.get(key, SomeValue.class);
        assertTrue(actualValue.isPresent());
        hazelcastSyncCache.invalidate(key);
        actualValue = hazelcastSyncCache.get(key, SomeValue.class);
        assertTrue(!actualValue.isPresent());
    }

    @Test
    public void testInvalidateAll(){
        IMap<Object, Object> map = client.getMap(cacheName);
        map.put(key, value);
        map.put(new SomeKey("foo", 1),
                new SomeValue("bar", 2));
        assertEquals(2, map.size());
        hazelcastSyncCache.invalidateAll();
        assertEquals(0, map.size());
    }

}
