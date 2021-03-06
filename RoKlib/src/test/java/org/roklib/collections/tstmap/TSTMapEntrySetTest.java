/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 25.11.2009
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

import java.util.*;
import java.util.Map.Entry;

import static org.junit.Assert.*;

public class TSTMapEntrySetTest {
    private final static String KEY1 = "key1";
    private final static String KEY2 = "key2";
    private final static String KEY3 = "key3";
    private final static String KEY4 = "key4";
    private final static String EMPTY_KEY = "";

    private final static String VALUE1 = "value1";
    private final static String VALUE2 = "value2";
    private final static String VALUE3 = "value3";
    private final static String VALUE4 = "value4";
    private final static String VALUE5 = "emptyKey";

    private TernarySearchTreeMap<String> map;
    private Set<Entry<CharSequence, String>> entrySet;

    @Before
    public void setUp() {
        map = new TernarySearchTreeMap<String>();
        map.put(KEY1, VALUE1);
        map.put(KEY2, VALUE2);
        map.put(KEY3, VALUE3);
        map.put(KEY4, VALUE4);
        map.put(EMPTY_KEY, VALUE5);
        entrySet = map.entrySet();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddFail() {
        entrySet.add(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        entrySet.addAll(null);
    }

    @Test
    public void testEqual() {
        assertEquals(entrySet.size(), map.size());
        for (Entry<CharSequence, String> entry : entrySet) {
            assertTrue(map.containsKey(entry.getKey()));
            assertEquals(entry.getValue(), map.get(entry.getKey()));
        }
    }

    @Test
    public void testClear() {
        entrySet.clear();
        assertTrue(map.isEmpty());
        assertTrue(entrySet.isEmpty());
        assertEquals(0, map.size());
        assertEquals(0, entrySet.size());
        assertTrue(map.equals(Collections.emptyMap()));
    }

    @Test
    public void testRemoveAll() {
        Iterator<Entry<CharSequence, String>> it = entrySet.iterator();
        it.next();
        Entry<CharSequence, String> e1 = it.next();
        it.next();
        Entry<CharSequence, String> e2 = it.next();
        List<Entry<CharSequence, String>> c = new LinkedList<Entry<CharSequence, String>>();
        c.add(e1);
        c.add(e2);
        entrySet.removeAll(c);
        assertEquals(3, map.size());
        assertEquals(3, entrySet.size());
        assertFalse(map.containsKey(e1.getKey()));
        assertFalse(map.containsKey(e2.getKey()));
        assertFalse(map.containsValue(e1.getValue()));
        assertFalse(map.containsValue(e2.getValue()));
    }

    @Test
    public void testRetainAll() {
        Iterator<Entry<CharSequence, String>> it = entrySet.iterator();
        it.next();
        Entry<CharSequence, String> e1 = it.next();
        it.next();
        Entry<CharSequence, String> e2 = it.next();
        List<Entry<CharSequence, String>> c = new LinkedList<Entry<CharSequence, String>>();
        c.add(e1);
        c.add(e2);
        entrySet.retainAll(c);
        assertEquals(2, map.size());
        assertEquals(2, entrySet.size());
        assertTrue(map.containsKey(e1.getKey()));
        assertTrue(map.containsKey(e2.getKey()));
        assertTrue(map.containsValue(e1.getValue()));
        assertTrue(map.containsValue(e2.getValue()));
    }

    @Test
    public void testRemove() {
        Iterator<Entry<CharSequence, String>> it = entrySet.iterator();
        Entry<CharSequence, String> removed1 = it.next();
        Entry<CharSequence, String> kept1 = it.next();
        Entry<CharSequence, String> removed2 = it.next();
        Entry<CharSequence, String> kept2 = it.next();
        Entry<CharSequence, String> removed3 = it.next();
        assertTrue(entrySet.remove(removed1));
        assertTrue(entrySet.remove(removed2));
        assertFalse(entrySet.remove(removed2));
        assertTrue(entrySet.remove(removed3));
        assertEquals(2, map.size());
        assertEquals(2, entrySet.size());
        assertFalse(map.containsKey(removed1.getKey()));
        assertFalse(map.containsKey(removed2.getKey()));
        assertFalse(map.containsKey(removed3.getKey()));
        assertFalse(map.containsValue(removed1.getValue()));
        assertFalse(map.containsValue(removed2.getValue()));
        assertFalse(map.containsValue(removed3.getValue()));

        assertTrue(map.containsKey(kept1.getKey()));
        assertTrue(map.containsKey(kept2.getKey()));
        assertTrue(map.containsValue(kept1.getValue()));
        assertTrue(map.containsValue(kept2.getValue()));
    }
}
