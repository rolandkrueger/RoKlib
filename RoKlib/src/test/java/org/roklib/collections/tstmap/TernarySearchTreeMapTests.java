/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 02.11.2010
 *
 * Author: Roland Krueger (www.rolandkrueger.info)
 *
 * This file is part of RoKlib.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.roklib.collections.tstmap;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.roklib.collections.tstmap.test.CompareTSTMapToTreeMapTest;
import org.roklib.collections.tstmap.test.PrefixSubtreeIteratorTest;
import org.roklib.collections.tstmap.test.TSTKeySetTest;
import org.roklib.collections.tstmap.test.TSTMapCaseInsensitiveMapEntrySetTest;
import org.roklib.collections.tstmap.test.TSTMapCaseInsensitiveSubMapTest;
import org.roklib.collections.tstmap.test.TSTMapCaseInsensitiveTest;
import org.roklib.collections.tstmap.test.TSTMapEntrySetTest;
import org.roklib.collections.tstmap.test.TSTMapSortedMapInterfaceTest;
import org.roklib.collections.tstmap.test.TSTMapValuesTest;
import org.roklib.collections.tstmap.test.TSTMapWithEmptyStringKeyTest;
import org.roklib.collections.tstmap.test.TSTSetCaseInsensitiveTest;
import org.roklib.collections.tstmap.test.TSTSetSubSetTest;
import org.roklib.collections.tstmap.test.TSTSetTest;
import org.roklib.collections.tstmap.test.TSTSubMapTest;
import org.roklib.collections.tstmap.test.TernarySearchTreeMapTest;

@RunWith (Suite.class)
@SuiteClasses ({ CompareTSTMapToTreeMapTest.class, PrefixSubtreeIteratorTest.class, TernarySearchTreeMapTest.class,
    TSTKeySetTest.class, TSTMapEntrySetTest.class, TSTMapSortedMapInterfaceTest.class, TSTMapValuesTest.class,
    TSTMapWithEmptyStringKeyTest.class, TSTSubMapTest.class, TSTMapCaseInsensitiveTest.class,
    TSTMapCaseInsensitiveMapEntrySetTest.class, TSTMapCaseInsensitiveSubMapTest.class, TSTSetTest.class,
    TSTSetSubSetTest.class, TSTSetCaseInsensitiveTest.class })
public class TernarySearchTreeMapTests
{
}
