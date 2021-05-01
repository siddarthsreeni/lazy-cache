# lazy-cache

Simple LazyCache Implementation of a Map.class 
which has a TTL for each Object defined inside in the Cache.class

The cache works on the implementation of a simple FIFO Queue & 
Lazy removal of Objects from Cache.

Simple Demo

```
  Cache<Object, Object> cache = new Cache.CacheBuilder<>().withExpiry(1000)
                .withTimeStore(new ArrayDeque<KeyPair<Object, Long>>()).build();
  cache.put("hello", "world");
  Assert.assertEquals(cache.get("hello"), "world");
  Thread.sleep(1001);
  Assert.assertEquals(cache.size(), 0);
``` 
