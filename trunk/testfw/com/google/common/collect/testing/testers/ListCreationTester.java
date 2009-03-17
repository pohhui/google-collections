/*
 * Copyright (C) 2008 Google Inc.
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

package com.google.common.collect.testing.testers;

import com.google.common.collect.testing.features.CollectionSize;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

/**
 * A generic JUnit test which tests creation (typically through a constructor or
 * static factory method) of a list. Can't be invoked directly; please see
 * {@link com.google.common.collect.testing.ListTestSuiteBuilder}.
 * 
 * @author Chris Povirk
 */
public class ListCreationTester<E> extends AbstractListTester<E> {
  @CollectionSize.Require(absent = {ZERO, ONE})
  public void testCreateWithDuplicates() {
    E[] array = createSamplesArray();
    array[1] = samples.e0;
    collection = getSubjectGenerator().create(array);

    expectContents(array);
  }
}
