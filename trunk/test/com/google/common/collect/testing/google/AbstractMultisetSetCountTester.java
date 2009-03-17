/*
 * Copyright (C) 2009 Google Inc.
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

package com.google.common.collect.testing.google;

import com.google.common.collect.Multiset;
import com.google.common.collect.testing.features.CollectionFeature;
import static com.google.common.collect.testing.features.CollectionFeature.ALLOWS_NULL_VALUES;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import com.google.common.collect.testing.features.CollectionSize;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Common superclass for {@link MultisetSetCountUnconditionallyTester} and
 * {@link MultisetSetCountConditionallyTester}. It is used by those testers to
 * test calls to the unconditional {@code setCount()} method and calls to the
 * conditional {@code setCount()} method when the expected present count is
 * correct.
 * 
 * @author Chris Povirk
 */
public abstract class AbstractMultisetSetCountTester<E>
    extends AbstractMultisetTester<E> {
  /*
   * TODO: consider adding MultisetFeatures.SUPPORTS_SET_COUNT. Currently we
   * assume that using setCount() to increase the count is permitted iff add()
   * is permitted and similarly for decrease/remove(). We assume that a
   * setCount() no-op is permitted if either add() or remove() is permitted,
   * though we also allow it to "succeed" if neither is permitted.
   */

  private void assertSetCount(E element, int count) {
    setCountCheckReturnValue(element, count);

    assertEquals(
        "multiset.count() should return the value passed to setCount()",
        count, getMultiset().count(element));

    int size = 0;
    for (Multiset.Entry<E> entry : getMultiset().entrySet()) {
      size += entry.getCount();
    }
    assertEquals(
        "multiset.size() should be the sum of the counts of all entries",
        size, getMultiset().size());
  }

  /**
   * Call the {@code setCount()} method under test, and check its return value.
   */
  abstract void setCountCheckReturnValue(E element, int count);

  /**
   * Call the {@code setCount()} method under test, but do not check its return
   * value. Callers should use this method over
   * {@link #setCountCheckReturnValue(Object, int)} when they expect
   * {@code setCount()} to throw an exception, as checking the return value
   * could produce an incorrect error message like
   * "setCount() should return the original count" instead of the message passed
   * to a later invocation of {@code fail()}, like "setCount should throw
   * UnsupportedOperationException."
   */
  abstract void setCountNoCheckReturnValue(E element, int count);

  private void assertSetCountIncreasingFailure(E element, int count) {
    try {
      setCountNoCheckReturnValue(element, count);
      fail("a call to multiset.setCount() to increase an element's count "
          + "should throw");
    } catch (UnsupportedOperationException expected) {
    }
  }

  private void assertSetCountDecreasingFailure(E element, int count) {
    try {
      setCountNoCheckReturnValue(element, count);
      fail("a call to multiset.setCount() to decrease an element's count "
          + "should throw");
    } catch (UnsupportedOperationException expected) {
    }
  }

  // Unconditional setCount no-ops.

  private void assertZeroToZero() {
    assertSetCount(samples.e3, 0);
  }

  private void assertOneToOne() {
    assertSetCount(samples.e0, 1);
  }

  private void assertThreeToThree() {
    initThreeCopies();
    assertSetCount(samples.e0, 3);
  }

  @CollectionFeature.Require(SUPPORTS_ADD)
  public void testSetCount_zeroToZero_addSupported() {
    assertZeroToZero();
  }

  @CollectionFeature.Require(SUPPORTS_REMOVE)
  public void testSetCount_zeroToZero_removeSupported() {
    assertZeroToZero();
  }

  @CollectionFeature.Require(absent = {SUPPORTS_ADD, SUPPORTS_REMOVE})
  public void testSetCount_zeroToZero_unsupported() {
    try {
      assertZeroToZero();
    } catch (UnsupportedOperationException tolerated) {
    }
  }

  @CollectionSize.Require(absent = ZERO)
  @CollectionFeature.Require(SUPPORTS_ADD)
  public void testSetCount_oneToOne_addSupported() {
    assertOneToOne();
  }

  @CollectionSize.Require(absent = ZERO)
  @CollectionFeature.Require(SUPPORTS_REMOVE)
  public void testSetCount_oneToOne_removeSupported() {
    assertOneToOne();
  }

  @CollectionSize.Require(absent = ZERO)
  @CollectionFeature.Require(absent = {SUPPORTS_ADD, SUPPORTS_REMOVE})
  public void testSetCount_oneToOne_unsupported() {
    try {
      assertOneToOne();
    } catch (UnsupportedOperationException tolerated) {
    }
  }

  @CollectionSize.Require(SEVERAL)
  @CollectionFeature.Require(SUPPORTS_ADD)
  public void testSetCount_threeToThree_addSupported() {
    assertThreeToThree();
  }

  @CollectionSize.Require(SEVERAL)
  @CollectionFeature.Require(SUPPORTS_REMOVE)
  public void testSetCount_threeToThree_removeSupported() {
    assertThreeToThree();
  }

  @CollectionSize.Require(SEVERAL)
  @CollectionFeature.Require(absent = {SUPPORTS_ADD, SUPPORTS_REMOVE})
  public void testSetCount_threeToThree_unsupported() {
    try {
      assertThreeToThree();
    } catch (UnsupportedOperationException tolerated) {
    }
  }

  // Unconditional setCount size increases:

  @CollectionFeature.Require(SUPPORTS_ADD)
  public void testSetCount_zeroToOne_supported() {
    assertSetCount(samples.e3, 1);
  }

  @CollectionFeature.Require(SUPPORTS_ADD)
  public void testSetCount_zeroToThree_supported() {
    assertSetCount(samples.e3, 3);
  }

  @CollectionSize.Require(absent = ZERO)
  @CollectionFeature.Require(SUPPORTS_ADD)
  public void testSetCount_oneToThree_supported() {
    assertSetCount(samples.e0, 3);
  }

  @CollectionFeature.Require(absent = SUPPORTS_ADD)
  public void testSetCount_zeroToOne_unsupported() {
    assertSetCountIncreasingFailure(samples.e3, 1);
  }

  @CollectionFeature.Require(absent = SUPPORTS_ADD)
  public void testSetCount_zeroToThree_unsupported() {
    assertSetCountIncreasingFailure(samples.e3, 3);
  }

  @CollectionSize.Require(absent = ZERO)
  @CollectionFeature.Require(absent = SUPPORTS_ADD)
  public void testSetCount_oneToThree_unsupported() {
    assertSetCountIncreasingFailure(samples.e3, 3);
  }

  // Unconditional setCount size decreases:

  @CollectionSize.Require(absent = ZERO)
  @CollectionFeature.Require(SUPPORTS_REMOVE)
  public void testSetCount_oneToZero_supported() {
    assertSetCount(samples.e0, 0);
  }

  @CollectionSize.Require(SEVERAL)
  @CollectionFeature.Require(SUPPORTS_REMOVE)
  public void testSetCount_threeToZero_supported() {
    initThreeCopies();
    assertSetCount(samples.e0, 0);
  }

  @CollectionSize.Require(SEVERAL)
  @CollectionFeature.Require(SUPPORTS_REMOVE)
  public void testSetCount_threeToOne_supported() {
    initThreeCopies();
    assertSetCount(samples.e0, 1);
  }

  @CollectionSize.Require(absent = ZERO)
  @CollectionFeature.Require(absent = SUPPORTS_REMOVE)
  public void testSetCount_oneToZero_unsupported() {
    assertSetCountDecreasingFailure(samples.e0, 0);
  }

  @CollectionSize.Require(SEVERAL)
  @CollectionFeature.Require(absent = SUPPORTS_REMOVE)
  public void testSetCount_threeToZero_unsupported() {
    initThreeCopies();
    assertSetCountDecreasingFailure(samples.e0, 0);
  }

  @CollectionSize.Require(SEVERAL)
  @CollectionFeature.Require(absent = SUPPORTS_REMOVE)
  public void testSetCount_threeToOne_unsupported() {
    initThreeCopies();
    assertSetCountDecreasingFailure(samples.e0, 1);
  }

  // setCount with nulls:

  @CollectionSize.Require(absent = ZERO)
  @CollectionFeature.Require({SUPPORTS_REMOVE, ALLOWS_NULL_VALUES})
  public void testSetCount_removeNull_nullSupported() {
    initCollectionWithNullElement();
    assertSetCount(null, 0);
  }

  @CollectionFeature.Require({SUPPORTS_ADD, ALLOWS_NULL_VALUES})
  public void testSetCount_addNull_nullSupported() {
    assertSetCount(null, 1);
  }

  @CollectionFeature.Require(value = SUPPORTS_ADD, absent = ALLOWS_NULL_VALUES)
  public void testSetCount_addNull_nullUnsupported() {
    try {
      setCountNoCheckReturnValue(null, 1);
      fail("adding null with setCount() should throw NullPointerException");
    } catch (NullPointerException expected) {
    }
  }

  @CollectionFeature.Require(ALLOWS_NULL_VALUES)
  public void testSetCount_noOpNull_nullSupported() {
    try {
      assertSetCount(null, 0);
    } catch (UnsupportedOperationException tolerated) {
    }
  }

  @CollectionFeature.Require(absent = ALLOWS_NULL_VALUES)
  public void testSetCount_noOpNull_nullUnsupported() {
    try {
      assertSetCount(null, 0);
    } catch (NullPointerException tolerated) {
    } catch (UnsupportedOperationException tolerated) {
    }
  }

  @CollectionSize.Require(absent = ZERO)
  @CollectionFeature.Require(ALLOWS_NULL_VALUES)
  public void testSetCount_existingNoNopNull_nullSupported() {
    initCollectionWithNullElement();
    try {
      assertSetCount(null, 1);
    } catch (UnsupportedOperationException tolerated) {
    }
  }

  // Negative count.

  @CollectionFeature.Require(SUPPORTS_REMOVE)
  public void testSetCount_negative_removeSupported() {
    try {
      setCountNoCheckReturnValue(samples.e3, -1);
      fail("calling setCount() with a negative count should throw "
          + "IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
    }
  }

  @CollectionFeature.Require(absent = SUPPORTS_REMOVE)
  public void testSetCount_negative_removeUnsupported() {
    try {
      setCountNoCheckReturnValue(samples.e3, -1);
      fail("calling setCount() with a negative count should throw "
          + "IllegalArgumentException or UnsupportedOperationException");
    } catch (IllegalArgumentException expected) {
    } catch (UnsupportedOperationException expected) {
    }
  }

  // TODO: test adding element of wrong type

  /**
   * Returns {@link Method} instances for the {@code setCount()} tests that
   * assume multisets support duplicates so that the test of {@code
   * Multisets.forSet()} can suppress them.
   */
  public static List<Method> getSetCountDuplicateInitializingMethods() {
    try {
      return Arrays.asList(
          getMethod("testSetCount_threeToThree_removeSupported"),
          getMethod("testSetCount_threeToZero_supported"),
          getMethod("testSetCount_threeToOne_supported"));
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private static Method getMethod(String methodName)
      throws NoSuchMethodException {
    return AbstractMultisetSetCountTester.class.getMethod(methodName);
  }
}
