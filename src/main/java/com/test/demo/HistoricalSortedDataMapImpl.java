package com.test.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class HistoricalSortedDataMapImpl<K, V extends Timestamp> implements HistoricalDataMap<K, V> {

  private volatile Map<K, NavigableMap<Long, V>> stateObjects = new HashMap<>();

  @Override
  public synchronized void add(K key, V value) {
    if (stateObjects.get(key) != null) {
      stateObjects.get(key).put(value.getTimestamp(), value);
      return;
    }
    NavigableMap<Long, V> timestampObject = new TreeMap<>();
    timestampObject.put(value.getTimestamp(), value);
    stateObjects.put(key, timestampObject);
  }

  @Override
  public V get(K key, long instant) {
    return stateObjects.get(key).floorEntry(instant).getValue();
  }

}
