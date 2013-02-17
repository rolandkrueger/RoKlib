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
package info.rolandkrueger.roklib.util.tstmap;

import info.rolandkrueger.roklib.util.tstmap.test.CompareTSTMapToTreeMapTest;
import info.rolandkrueger.roklib.util.tstmap.test.PrefixSubtreeIteratorTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTKeySetTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTMapCaseInsensitiveMapEntrySetTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTMapCaseInsensitiveSubMapTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTMapCaseInsensitiveTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTMapEntrySetTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTMapSortedMapInterfaceTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTMapValuesTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTMapWithEmptyStringKeyTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTSetCaseInsensitiveTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTSetSubSetTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTSetTest;
import info.rolandkrueger.roklib.util.tstmap.test.TSTSubMapTest;
import info.rolandkrueger.roklib.util.tstmap.test.TernarySearchTreeMapTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith (Suite.class)
@SuiteClasses ({ CompareTSTMapToTreeMapTest.class, PrefixSubtreeIteratorTest.class, TernarySearchTreeMapTest.class,
    TSTKeySetTest.class, TSTMapEntrySetTest.class, TSTMapSortedMapInterfaceTest.class, TSTMapValuesTest.class,
    TSTMapWithEmptyStringKeyTest.class, TSTSubMapTest.class, TSTMapCaseInsensitiveTest.class,
    TSTMapCaseInsensitiveMapEntrySetTest.class, TSTMapCaseInsensitiveSubMapTest.class, TSTSetTest.class,
    TSTSetSubSetTest.class, TSTSetCaseInsensitiveTest.class })
public class TernarySearchTreeMapTests
{
}
