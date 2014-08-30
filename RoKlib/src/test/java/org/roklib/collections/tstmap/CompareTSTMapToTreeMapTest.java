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

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
    private TreeMap<CharSequence, String> treeMap;
    private TernarySearchTreeMap<String> tstMap;

    @Before
    public void setUp() {
        treeMap = new TreeMap<CharSequence, String>();
        tstMap = new TernarySearchTreeMap<String>();
    }

    private void fillWithFiveLowerCaseEntriesOrderingExpected() {
        MapData.TestDataFixture testData = MapData.getFiveLowerCaseEntriesOrderingExpected();
        treeMap.putAll(testData.getData());
        tstMap.putAll(testData.getData());
    }

    @Test
    public void testFirstKey() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertEquals(treeMap.firstKey(), tstMap.firstKey());
    }

    @Test
    public void testLastKey() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertEquals(treeMap.lastKey(), tstMap.lastKey());
    }

    @Test
    public void testContainsFail() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        String key = "not in map";
        assertEquals(treeMap.containsKey(key), tstMap.containsKey(key));
    }

    @Test
    public void testContains() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        String key = "sweden";
        assertEquals(treeMap.containsKey(key), tstMap.containsKey(key));
    }

    @Test
    public void testEntrySet() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertEquals(treeMap.entrySet(), tstMap.entrySet());
    }

    @Test
    public void testGet() {
        MapData.TestDataFixture testData = MapData.getFiveLowerCaseEntriesOrderingExpected();
        fillWithFiveLowerCaseEntriesOrderingExpected();
        for (CharSequence key : testData.getData().keySet()) {
            assertEquals(treeMap.get(key), tstMap.get(key));
        }
    }

    @Test
    public void testGetFail() {
        MapData.TestDataFixtureMapMap testData = MapData.getFiveLowerCaseEntriesOrderingExpected();
        fillWithFiveLowerCaseEntriesOrderingExpected();
        for (String key : testData.getData().values()) {
            assertEquals(treeMap.get(key), tstMap.get(key));
        }
    }

    @Test
    public void testTSTMapGetWithArbitraryClass() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertThat(tstMap.get(new GermanyTestClass()), equalTo("sauerkraut"));
    }

    @Test(expected = ClassCastException.class)
    public void testJavaMapGetWithArbitraryClassFails() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        treeMap.get(new GermanyTestClass());
    }

    @Test
    public void testTSTMapKeySetContainsWithArbitraryClass() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertThat(tstMap.keySet().contains(new GermanyTestClass()), equalTo(true));
    }

    @Test(expected = ClassCastException.class)
    public void testJavaMapKeySetContainsWithArbitraryClassFails() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        treeMap.keySet().contains(new GermanyTestClass());
    }

    @Test(expected = ClassCastException.class)
    public void testTSTMapRemoveWithArbitraryClassFails() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        treeMap.remove(new GermanyTestClass());
    }

    @Test
    public void testJavaMapRemoveWithArbitraryClass() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertThat(tstMap.remove(new GermanyTestClass()), equalTo("sauerkraut"));
    }

    @Test
    public void testEquals() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertEquals(treeMap, tstMap);
    }

    @Test
    public void testHashCode() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertEquals(treeMap.hashCode(), tstMap.hashCode());
    }

    @Test
    public void testKeySet() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertEquals(treeMap.keySet(), tstMap.keySet());
    }

    @Test
    public void testRemove() {
        MapData.TestDataFixtureMapMap testData = MapData.getFiveLowerCaseEntriesOrderingExpected();
        fillWithFiveLowerCaseEntriesOrderingExpected();
        for (CharSequence key : testData.getData().keySet()) {
            assertEquals(treeMap.remove(key), tstMap.remove(key));
            assertEquals(treeMap.size(), tstMap.size());
        }
    }

    @Test
    public void testRemoveFromEntrySetInvalidEntry() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        SimpleEntry<CharSequence, String> entryNotInMap = new SimpleEntry<CharSequence, String>("sverige", "surstromming");
        SimpleEntry<CharSequence, String> entryInMap = new SimpleEntry<CharSequence, String>("deutschland", "sauerkraut");

        treeMap.entrySet().remove(entryNotInMap);
        treeMap.entrySet().remove(entryInMap);
        tstMap.entrySet().remove(entryNotInMap);
        tstMap.entrySet().remove(entryInMap);
        assertEquals(treeMap, tstMap);
    }

    // TESTS OF THE COLLECTION RETURNED BY values()
    @Test
    public void testValues() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertEquals(tstMap.values(), treeMap.values());
    }

    @Test
    public void testValues_toArray() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertThat(treeMap.values(), contains(tstMap.values().toArray()));
    }

    @Test
    public void testValues_toArrayArrayParam() {
        fillWithFiveLowerCaseEntriesOrderingExpected();
        assertThat(treeMap.values(), contains(tstMap.values().toArray()));
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
