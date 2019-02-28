package com.test.demo;

public interface HistoricalDataMap<K, V> {

  void add(K key, V value);

  V get(K key, long instant);

  int size();

}
