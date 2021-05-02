package io.github.siddarthsreeni;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * created by Siddarth Sreeni on 01-05-2021
 */
public class Cache<K, V> implements Map<K, V> {

    private final long ttl;
    private final Queue<KeyPair<K, Long>> timeStore;
    private final Map<K, V> objectStore;
    private final long period;

    private Cache(long ttl, Queue<KeyPair<K, Long>> timeStore, Map<K, V> objectStore, long cleanUpPeriod) {
        this.ttl = ttl;
        this.timeStore = timeStore;
        this.objectStore = objectStore;
        this.period = cleanUpPeriod;
        if (period > 0) {
            Timer timer = new Timer(this.getClass().getSimpleName(), true);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    lazyRun();
                }
            };
            timer.scheduleAtFixedRate(task, ttl, cleanUpPeriod);

        }
    }

    public long getTtl() {
        return ttl;
    }

    public long getPeriod() {
        return period;
    }

    private int lazyRun() {
        int count = 0;
        while (timeStore.size() > 0) {
            if ((System.currentTimeMillis() - timeStore.peek().getValue() > ttl)) {
                KeyPair<K, Long> ref = timeStore.poll();
                objectStore.remove(ref.getKey());
                count += 1;
            } else {
                break;
            }
        }
        return count;
    }


    public int size() {
        lazyRun();
        return objectStore.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsKey(Object key) {
        lazyRun();
        return objectStore.containsKey(key);
    }

    public boolean containsValue(Object value) {
        lazyRun();
        return objectStore.containsValue(value);
    }

    public V get(Object key) {
        lazyRun();
        return objectStore.get(key);
    }

    private V add(K key, V value, long t) {
        timeStore.add(new KeyPair<K, Long>(key, t));
        return objectStore.put(key, value);
    }

    public V put(K key, V value) {
        return add(key, value, System.currentTimeMillis());
    }

    public V remove(Object key) {
        return objectStore.remove(key);
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        long t = System.currentTimeMillis();
        for (Entry<? extends K, ? extends V> kvEntry : m.entrySet()) {
            add(kvEntry.getKey(), kvEntry.getValue(), t);
        }
    }

    public void clear() {
        timeStore.clear();
        objectStore.clear();
    }

    public Set<K> keySet() {
        lazyRun();
        return objectStore.keySet();
    }

    public Collection<V> values() {
        lazyRun();
        return objectStore.values();
    }

    public Set<Entry<K, V>> entrySet() {
        lazyRun();
        return objectStore.entrySet();
    }


    public static class CacheBuilder<K, V> {
        private Queue<KeyPair<K, Long>> timeStore = new LinkedList<>();
        private Map<K, V> objectStore = new ConcurrentHashMap<>();
        private long ttl = Long.MAX_VALUE;
        private long cleanUpPeriod = 0;

        /**
         * set the expiry of objects in ttl.
         *
         * @param ttl indicates milli seconds. The Default expiry is indefinite.
         * @return instance of this CacheBuilder
         */
        public CacheBuilder<K, V> withExpiry(long ttl) {
            this.ttl = ttl;
            return this;
        }

        /**
         * Override the default storing mechanism of time of an instance
         * of T in cache. By default this is LinkedList.class
         *
         * @param timeStore stores the key-time in an FIFO Queue.
         * @return instance of this CacheBuilder
         */
        public CacheBuilder<K, V> withTimeStore(Queue<KeyPair<K, Long>> timeStore) {
            this.timeStore = timeStore;
            return this;
        }

        public CacheBuilder<K, V> withCleanUpPeriod(long cleanUpPeriod) {
            this.cleanUpPeriod = cleanUpPeriod;
            return this;
        }

        public Cache<K, V> build() {
            if (ttl == Long.MAX_VALUE) {
                throw new IllegalArgumentException("no ttl is provided.");
            }
            if (cleanUpPeriod > ttl) {
                throw new IllegalArgumentException("CleanUp Period cannot be more than ttl," +
                        " to disable timed clean up, set 0. Suggested value is " + ttl / 2);
            }
            return new Cache<K, V>(ttl, timeStore, objectStore, cleanUpPeriod);
        }

    }
}
