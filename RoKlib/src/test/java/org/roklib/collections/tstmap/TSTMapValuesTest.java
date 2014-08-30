/*
 * Copyright (C) 2007 Roland Krueger
 * Created on  11.07.2007
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.*;

public class TSTMapValuesTest {
    private Collection<String> values;
    private TernarySearchTreeMap<String> map;
    private Collection<String> originalValues;

    @Before
    public void setUp() {
        map = new TernarySearchTreeMap<String>();
        map.putAll(MapData.getFiveLowerCaseEntriesOrderingExpected().getData());
        values = map.values();
        originalValues = MapData.getFiveLowerCaseEntriesOrderingExpected().getData().values();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllFail() {
        List<String> l = new ArrayList<String>();
        l.add("");
        values.addAll(l);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddFail() {
        values.add("");
    }

    @Test
    public void testClear() {
        values.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testContains() {
        Collection<String> c = MapData.getFiveLowerCaseEntriesOrderingExpected().getData().values();

        for (String s : c) {
            assertTrue(values.contains(s));
        }
    }

    @Test
    public void testContainsAll() {
        assertTrue(values.containsAll(originalValues));
    }

    @Test
    public void testContainsAllFails() {
        List<String> values = new ArrayList<String>(originalValues);
        values.add("extra");
        assertFalse(this.values.containsAll(values));
    }

    @Test
    public void testEquals() {
        assertTrue(values.equals(MapData.getFiveLowerCaseEntriesOrderingExpected().getData().values()));
    }

    @Test
    public void testIsEmpty() {
        values.clear();
        assertTrue(values.isEmpty());
    }

    @Test
    public void testIterator() {
        for (Iterator<String> it = values.iterator(); it.hasNext(); ) {
            assertTrue(map.containsValue(it.next()));
        }
    }

    @Test
    public void testRemove() {
        assertTrue(values.remove(originalValues.iterator().next()));
        assertEquals(originalValues.size() - 1, map.size());
        originalValues.remove(originalValues.iterator().next());
        assertEquals(values, originalValues);
        assertFalse(values.remove("not in map"));
        assertEquals(originalValues.size(), map.size());
    }

    @Test
    public void testRemoveAll() {
        Collection<String> notInMap = new ArrayList<String>();
        notInMap.add("not");
        notInMap.add("in");
        notInMap.add("map");
        assertFalse(values.removeAll(notInMap));
        assertEquals(originalValues.size(), values.size());

        Collection<String> inMap = new ArrayList<String>();
        Iterator<String> it = originalValues.iterator();
        inMap.add(it.next());
        inMap.add(it.next());
        assertTrue(values.removeAll(inMap));
        assertEquals(originalValues.size() - 2, values.size());
    }

    @Test
    public void testRetainAll() {
        Collection<String> inMap = new ArrayList<String>();
        Iterator<String> it = originalValues.iterator();
        inMap.add(it.next());
        inMap.add(it.next());
        assertTrue(values.retainAll(inMap));
        assertEquals(inMap.size(), values.size());
    }

    @Test
    public void testRetainAllMapWillNotChange() {
        assertFalse(values.retainAll(originalValues));
    }
}
