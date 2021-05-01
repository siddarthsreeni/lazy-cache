package io.github.siddarthsreeni;

/**
 * created by Siddarth Sreeni on 01-05-2021
 * The KeyPair Class.
 * This stores an instance of T as Key and a V value
 */
public class KeyPair<K, V> {
    private final K key;
    private final V value;

    public KeyPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
