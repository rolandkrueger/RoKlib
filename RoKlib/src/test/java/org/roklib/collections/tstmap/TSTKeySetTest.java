/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 16.07.2007
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
import org.roklib.collections.tstmap.MapData.TestDataFixture;

import java.util.*;

import static junit.framework.Assert.*;

public class TSTKeySetTest {
    private TernarySearchTreeMap<String> map;
    private TestDataFixture testData = MapData.getFiveLowerCaseEntriesOrderingExpected();
    private Set<CharSequence> keySet;
    private Map<CharSequence, String> comparisonMap;

    @Before
    public void setUp() {
        map = new TernarySearchTreeMap<String>();
        testData = MapData.getFiveLowerCaseEntriesOrderingExpected();
        map.putAll(testData.getData());
        keySet = map.keySet();
        comparisonMap = new TreeMap<CharSequence, String>(testData.getData());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        keySet.add("test");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        keySet.addAll(new TreeSet<CharSequence>());
    }

    @Test
    public void testClear() {
        keySet.clear();
        assertEquals(0, map.size());
        assertTrue(keySet.isEmpty());
    }

    @Test
    public void testContains() {
        assertTrue(keySet.contains("sverige"));
        assertFalse(keySet.contains("russia"));
    }

    @Test
    public void testContainsAll() {
        Set<CharSequence> data = new TreeSet<CharSequence>();
        data.add("deutschland");
        data.add("france");
        assertTrue(keySet.containsAll(data));
    }

    @Test
    public void testContainsAllFail() {
        Set<CharSequence> data = new TreeSet<CharSequence>();
        data.add("deutschland");
        data.add("france");
        data.add("belgium");
        assertFalse(keySet.containsAll(data));

        data.clear();
        data.add("norge");
        data.add("danmark");
        assertFalse(keySet.containsAll(data));
    }

    @Test
    public void testRemove() {
        boolean changed;
        changed = keySet.remove("deutschland");
        comparisonMap.remove("deutschland");
        assertTrue(changed);
        assertEquals(comparisonMap, map);
    }

    @Test
    public void testRemoveNothingRemoved() {
        boolean changed;
        changed = keySet.remove("not in map");
        comparisonMap.remove("not in map");
        assertFalse(changed);
        assertEquals(comparisonMap, map);
    }

    @Test
    public void testRemoveAll() {
        boolean changed;
        Set<CharSequence> data = new TreeSet<CharSequence>();
        data.add("deutschland");
        data.add("france");

        changed = keySet.removeAll(data);
        assertTrue(changed);
        comparisonMap.keySet().removeAll(data);
        assertEquals(comparisonMap, map);
    }

    @Test
    public void testRemoveAllFail() {
        boolean changed;
        Set<CharSequence> data = new TreeSet<CharSequence>();
        data.add("not in");
        data.add("map");

        changed = keySet.removeAll(data);
        assertFalse(changed);
        comparisonMap.keySet().removeAll(data);
        assertEquals(comparisonMap, map);
    }

    @Test
    public void testRetainAll() {
        boolean changed;
        Set<CharSequence> data = new TreeSet<CharSequence>();
        data.add("deutschland");
        data.add("france");
        data.add("not in map");

        changed = keySet.retainAll(data);
        assertTrue(changed);
        comparisonMap.keySet().retainAll(data);
        assertEquals(comparisonMap, map);
        assertEquals(2, map.size());
    }

    @Test
    public void testIterator_Remove() {
        Iterator<CharSequence> it = keySet.iterator();
        it.next();
        it.remove();
        comparisonMap.remove("deutschland");
        assertEquals(comparisonMap.keySet(), keySet);
    }
}
