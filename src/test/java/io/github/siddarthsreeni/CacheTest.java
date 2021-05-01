package io.github.siddarthsreeni;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

/**
 * created by Siddarth Sreeni on 01-05-2021
 */
public class CacheTest {

    private Cache<String, String> cache;

    private void clearCache() {
        cache.clear();
        Assert.assertEquals(cache.size(), 0);
    }

    @Before
    public void setCache() {
        cache = new Cache.CacheBuilder<String, String>()
                .withExpiry(1000)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderTest() {
        new Cache.CacheBuilder<>().build();
    }

    @Test
    public void builderTestWithTimeStore() throws InterruptedException {
        Cache<Object, Object> cache = new Cache.CacheBuilder<>().withExpiry(1000)
                .withTimeStore(new ArrayDeque<KeyPair<Object, Long>>()).build();
        cache.put("hello", "world");
        Assert.assertEquals(cache.get("hello"), "world");
        Thread.sleep(1001);
        Assert.assertEquals(cache.size(), 0);
        clearCache();
    }


    @Test
    public void buildCacheWithTTl() throws InterruptedException {
        cache.put("hello", "hello");
        Assert.assertEquals(cache.size(), 1);
        Thread.sleep(1001);
        Assert.assertEquals(cache.size(), 0);
        clearCache();
    }

    @Test
    public void publishMultipleObjects() throws InterruptedException {
        Map<String, String> dummyMap = new HashMap<>();
        dummyMap.put("1", "1");
        dummyMap.put("2", "1");
        dummyMap.put("3", "1");
        cache.putAll(dummyMap);
        Assert.assertEquals(cache.size(), 3);
        Thread.sleep(1001);
        Assert.assertEquals(cache.size(), 0);
        clearCache();
    }

    @Test
    public void getAndRead() {
        cache.put("hello", "world");
        Assert.assertEquals(cache.get("hello"), "world");
        clearCache();
    }
}
