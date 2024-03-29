/*
 * Copyright (C) 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.collect;

/**
 * Unit tests for {@link HashMultimap}.
 *
 * @author Jared Levy
 */
public class HashMultimapTest extends AbstractSetMultimapTest {
  @Override protected Multimap<String, Integer> create() {
    return HashMultimap.create();
  }

  /*
   * The behavior of toString() is tested by TreeMultimap, which shares a
   * lot of code with HashMultimap and has deterministic iteration order.
   */

  public void testCreate() {
    HashMultimap<String, Integer> multimap = HashMultimap.create();
    multimap.put("foo", 1);
    multimap.put("bar", 2);
    multimap.put("foo", 3);
    assertEquals(ImmutableSet.of(1, 3), multimap.get("foo"));
    assertEquals(8, multimap.expectedValuesPerKey);
  }

  public void testCreateFromMultimap() {
    Multimap<String, Integer> multimap = createSample();
    HashMultimap<String, Integer> copy = HashMultimap.create(multimap);
    assertEquals(multimap, copy);
    assertEquals(8, copy.expectedValuesPerKey);
  }

  public void testCreateFromSizes() {
    HashMultimap<String, Integer> multimap = HashMultimap.create(20, 15);
    multimap.put("foo", 1);
    multimap.put("bar", 2);
    multimap.put("foo", 3);
    assertEquals(ImmutableSet.of(1, 3), multimap.get("foo"));
    assertEquals(15, multimap.expectedValuesPerKey);
  }

  public void testCreateFromIllegalSizes() {
    try {
      HashMultimap.create(-20, 15);
      fail();
    } catch (IllegalArgumentException expected) {}

    try {
      HashMultimap.create(20, -15);
      fail();
    } catch (IllegalArgumentException expected) {}
  }

  public void testEmptyMultimapsEqual() {
    Multimap<String, Integer> setMultimap = HashMultimap.create();
    Multimap<String, Integer> listMultimap = ArrayListMultimap.create();
    assertTrue(setMultimap.equals(listMultimap));
    assertTrue(listMultimap.equals(setMultimap));
  }
}
