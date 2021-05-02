<img align="right" src="https://raw.githubusercontent.com/siddarthsreeni/lazy-cache/master/docs/logo.PNG?raw=true" />

# lazy-cache #

[![Build Status](https://travis-ci.com/siddarthsreeni/lazy-cache.svg?branch=main)](https://travis-ci.com/siddarthsreeni/lazy-cache)
[![Github All Releases](https://img.shields.io/github/downloads/siddarthsreeni/lazy-cache/total.svg)]()


Simple LazyCache Implementation of a Map.class which has a TTL for each Object defined inside in the `Cache.class` The cache works on the implementation of a simple FIFO Queue & 
Lazy removal of Objects from Cache.

## Sample Code ##

```
  Cache<Object, Object> cache = new Cache.CacheBuilder<>().withExpiry(1000)
                .withTimeStore(new ArrayDeque<KeyPair<Object, Long>>()).build();
  cache.put("hello", "world");
  Assert.assertEquals(cache.get("hello"), "world");
  Thread.sleep(1001);
  Assert.assertEquals(cache.size(), 0);
``` 

## Quick Start ##

```
   //maven 
   <dependency>
     <groupId>io.github.siddarthsreeni</groupId>
     <artifactId>lazy-cache</artifactId>
     <version>1.0.0</version>
   </dependency>

   // scala-sbt 
   libraryDependencies += "io.github.siddarthsreeni" % "lazy-cache" % "1.0.0"

```
