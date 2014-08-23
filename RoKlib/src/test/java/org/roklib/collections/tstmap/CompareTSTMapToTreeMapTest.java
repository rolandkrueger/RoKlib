/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 10.07.2007
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

import org.junit.Before;
import org.junit.Test;
import org.roklib.collections.TernarySearchTreeMap;

import java.util.AbstractMap.SimpleEntry;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

/**
 * This test fixture will compare the method results of the various {@link SortedMap} methods if the respective methods
 * are invoked on {@link TreeMap} and {@link TernarySearchTreeMap}. For these tests, {@link TreeMap} will be used as the
 * prototype of a correct implementation of the {@link SortedMap} interface. Thus, it is expected, that
 * {@link TernarySearchTreeMap will show the same behavior with respect to the {@link SortedMap} interface as a
 * {@link TreeMap}.
 *
 * @author Roland Krueger
 */
public class CompareTSTMapToTreeMapTest {
    private TreeMap<CharSequence, String> mTreeMap;
    private TernarySearchTreeMap<String> mTSTMap;

    @Before
    public void setUp() {
        mTreeMap = new TreeMap<CharSequence, String>();
        mTSTMap = new TernarySearchTreeMap<String>();
    }

    private void fillWithFiveLowerCaseEntriesOrderingExpected() {
        MapData.TestDataFixture testData = MapData.getFiveLowerCaseEntriesOrderingExpected();
        mTreeMap.putAll(testData.getData());
        mTSTMap.putAll(testData.getData());
    }

    @Test
    public void testFirstKey() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertEquals(mTreeMap.firstKey(), mTSTMap.firstKey());
    }

    @Test
    public void testLastKey() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertEquals(mTreeMap.lastKey(), mTSTMap.lastKey());
    }

    @Test
    public void testContainsFail() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        String key = "not in map";
        assertEquals(mTreeMap.containsKey(key), mTSTMap.containsKey(key));
    }

    @Test
    public void testContains() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        String key = "sweden";
        assertEquals(mTreeMap.containsKey(key), mTSTMap.containsKey(key));
    }

    @Test
    public void testEntrySet() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertEquals(mTreeMap.entrySet(), mTSTMap.entrySet());
    }

    @Test
    public void testGet() {
        MapData.TestDataFixture testData = MapData.getFiveLowerCaseEntriesOrderingExpected();
        fillWithFiveLowerCaseEntriesOrderingExpected();
        for (CharSequence key : testData.getData().keySet()) {
            assertEquals(mTreeMap.get(key), mTSTMap.get(key));
        }
    }

    @Test
    public void testGetFail() {
        MapData.TestDataFixtureMapMap testData = MapData.getFiveLowerCaseEntriesOrderingExpected();
        fillWithFiveLowerCaseEntriesOrderingExpected();
        for (String key : testData.getData().values()) {
            assertEquals(mTreeMap.get(key), mTSTMap.get(key));
        }
    }

    @Test(expected = ClassCastException.class)
    public void testTSTMapGetWithArbitraryClassFails() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        mTSTMap.get(new GermanyTestClass());
    }

    @Test(expected = ClassCastException.class)
    public void testJavaMapGetWithArbitraryClassFails() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        mTreeMap.get(new GermanyTestClass());
    }

    @Test(expected = ClassCastException.class)
    public void testTSTMapKeySetContainsWithArbitraryClassFails() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        mTSTMap.keySet().contains(new GermanyTestClass());
    }

    @Test(expected = ClassCastException.class)
    public void testJavaMapKeySetContainsWithArbitraryClassFails() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        mTreeMap.keySet().contains(new GermanyTestClass());
    }

    @Test(expected = ClassCastException.class)
    public void testTSTMapRemoveWithArbitraryClassFails() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        mTreeMap.remove(new GermanyTestClass());
    }

    @Test(expected = ClassCastException.class)
    public void testJavaMapRemoveWithArbitraryClassFails() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        mTSTMap.remove(new GermanyTestClass());
    }

    @Test
    public void testEquals() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertEquals(mTreeMap, mTSTMap);
    }

    @Test
    public void testHashCode() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertEquals(mTreeMap.hashCode(), mTSTMap.hashCode());
    }

    @Test
    public void testKeySet() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertEquals(mTreeMap.keySet(), mTSTMap.keySet());
    }

    @Test
    public void testRemove() {
        MapData.TestDataFixtureMapMap testData = MapData.getFiveLowerCaseEntriesOrderingExpected();
        fillWithFiveLowerCaseEntriesOrderingExpected();
        for (CharSequence key : testData.getData().keySet()) {
            assertEquals(mTreeMap.remove(key), mTSTMap.remove(key));
            assertEquals(mTreeMap.size(), mTSTMap.size());
        }
    }

    @Test
    public void testRemoveFromEntrySetInvalidEntry() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        SimpleEntry<CharSequence, String> entryNotInMap = new SimpleEntry<CharSequence, String>("sverige", "surstromming");
        SimpleEntry<CharSequence, String> entryInMap = new SimpleEntry<CharSequence, String>("deutschland", "sauerkraut");

        mTreeMap.entrySet().remove(entryNotInMap);
        mTreeMap.entrySet().remove(entryInMap);
        mTSTMap.entrySet().remove(entryNotInMap);
        mTSTMap.entrySet().remove(entryInMap);
        assertEquals(mTreeMap, mTSTMap);
    }

    // TESTS OF THE COLLECTION RETURNED BY values()
    @Test
    public void testValues() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertEquals(mTSTMap.values(), mTreeMap.values());
    }

    @Test
    public void testValues_toArray() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertEquals(mTreeMap.values().toArray(), mTSTMap.values().toArray());
    }

    @Test
    public void testValues_toArrayArrayParam() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertEquals(mTreeMap.values().toArray(new String[1]), mTSTMap.values().toArray(new String[1]));
    }

    private class GermanyTestClass implements Comparable<GermanyTestClass> {
        @Override
        public String toString() {
            return "deutschland";
        }

        public int compareTo(GermanyTestClass o) {
            return 0;
        }
    }
}
