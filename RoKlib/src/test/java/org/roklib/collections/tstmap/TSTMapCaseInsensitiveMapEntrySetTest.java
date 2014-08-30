/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 16.11.2010
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
import org.roklib.collections.TernarySearchTreeMapCaseInsensitive;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;

import static org.junit.Assert.*;

public class TSTMapCaseInsensitiveMapEntrySetTest {
    private static final String EMPTY = "";
    private static final String ABCD = "ABcd";
    private static final String XYZ = "xyz";
    private static final String FOO = "Foo";
    private static final String BAR = "Bar";
    private static final String BAZ = "BAZ";

    private TernarySearchTreeMapCaseInsensitive<String> testObj;
    private Set<Map.Entry<CharSequence, String>> entrySet;

    @Before
    public void setUp() {
        testObj = new TernarySearchTreeMapCaseInsensitive<String>();
        testObj.put(EMPTY, EMPTY);
        testObj.put(BAR, BAR);
        testObj.put(BAZ, BAZ);
        testObj.put(ABCD, ABCD);
        testObj.put(XYZ, XYZ);
        testObj.put(FOO, FOO);
        entrySet = testObj.entrySet();
    }

    @Test
    public void testEntrySet() {
        assertEquals(testObj.size(), entrySet.size());
        Iterator<Map.Entry<CharSequence, String>> iterator = entrySet.iterator();
        assertTrue(iterator.hasNext());
        Map.Entry<CharSequence, String> entry = iterator.next();
        assertNotNull(entry);
        assertEquals("", entry.getKey());
        assertEquals("", entry.getValue());
        entry = iterator.next();
        assertNotNull(entry);
        assertEquals(ABCD, entry.getKey());
        assertEquals(ABCD, entry.getValue());
        entry = iterator.next();
        assertNotNull(entry);
        assertEquals(BAR, entry.getKey());
        assertEquals(BAR, entry.getValue());
        entry = iterator.next();
        assertNotNull(entry);
        assertEquals(BAZ, entry.getKey());
        assertEquals(BAZ, entry.getValue());
        entry = iterator.next();
        assertNotNull(entry);
        assertEquals(FOO, entry.getKey());
        assertEquals(FOO, entry.getValue());
        entry = iterator.next();
        assertNotNull(entry);
        assertEquals(XYZ, entry.getKey());
        assertEquals(XYZ, entry.getValue());
    }

    @Test
    public void testContainsAll() {
        List<Map.Entry<CharSequence, String>> entries = new ArrayList<Map.Entry<CharSequence, String>>(5);
        entries.add(new SimpleEntry<CharSequence, String>("abcd", ABCD));
        entries.add(new SimpleEntry<CharSequence, String>("fOO", FOO));
        entries.add(new SimpleEntry<CharSequence, String>("baZ", BAZ));
        entries.add(new SimpleEntry<CharSequence, String>("BAR", BAR));
        entries.add(new SimpleEntry<CharSequence, String>(XYZ, XYZ));
        assertTrue(entrySet.containsAll(entries));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        entrySet.add(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        entrySet.addAll(null);
    }

    @Test
    public void testSize() {
        assertEquals(6, entrySet.size());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(entrySet.isEmpty());
        entrySet.clear();
        assertTrue(entrySet.isEmpty());
    }

    @Test
    public void testRemove() {
        assertTrue(entrySet.remove(new SimpleEntry<CharSequence, String>("abCD", ABCD)));
        assertFalse(testObj.containsKey(ABCD));
        assertTrue(entrySet.remove(new SimpleEntry<CharSequence, String>("fOO", FOO)));
        assertFalse(testObj.containsKey(FOO));
        assertFalse(entrySet.remove(new SimpleEntry<CharSequence, String>("zzz", "zzz")));
        assertFalse(entrySet.remove(new SimpleEntry<CharSequence, String>(XYZ, "zzz")));
        assertFalse(entrySet.remove(new SimpleEntry<CharSequence, String>(null, "zzz")));
        assertTrue(testObj.containsKey(XYZ));
        assertFalse(entrySet.remove(null));
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void testRetainAll() {
        List list = new ArrayList();
        list.add(new SimpleEntry<CharSequence, String>("abCD", ABCD));
        list.add(new SimpleEntry<CharSequence, String>("fOO", FOO));
        list.add(new SimpleEntry<CharSequence, String>("zzz", "zzz"));
        list.add("some String");
        entrySet.retainAll(list);
        assertEquals(2, entrySet.size());
        assertTrue(testObj.containsKey(ABCD));
        assertTrue(testObj.containsKey(FOO));
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void testRemoveAll() {
        List list = new ArrayList();
        list.add(new SimpleEntry<CharSequence, String>("abCD", ABCD));
        list.add(new SimpleEntry<CharSequence, String>("fOO", FOO));
        list.add(new SimpleEntry<CharSequence, String>("zzz", "zzz"));
        entrySet.removeAll(list);
        assertEquals(4, testObj.size());
        assertFalse(testObj.containsKey(ABCD));
        assertFalse(testObj.containsKey(FOO));

        list.add("1");
        list.add(new SimpleEntry<CharSequence, String>("xYz", XYZ));
        list.add(new SimpleEntry<CharSequence, String>("BAR", BAR));
        list.add(new SimpleEntry<CharSequence, String>("baZ", BAZ));
        list.add(new SimpleEntry<CharSequence, String>("", EMPTY));
        entrySet.removeAll(list);
        assertTrue(testObj.isEmpty());
    }

    @Test
    public void testEntrySetContains() {
        SimpleEntry<CharSequence, String> entry = new SimpleEntry<CharSequence, String>("abCD", ABCD);
        assertTrue(entrySet.contains(entry));
        entry = new SimpleEntry<CharSequence, String>("abcd", ABCD);
        assertTrue(entrySet.contains(entry));
        entry = new SimpleEntry<CharSequence, String>("fOO", FOO);
        assertTrue(entrySet.contains(entry));
        entry = new SimpleEntry<CharSequence, String>("baZ", BAZ);
        assertTrue(entrySet.contains(entry));
        entry = new SimpleEntry<CharSequence, String>("BAR", BAR);
        assertTrue(entrySet.contains(entry));
        entry = new SimpleEntry<CharSequence, String>(XYZ, XYZ);
        assertTrue(entrySet.contains(entry));

        entry = new SimpleEntry<CharSequence, String>("zzz", "zzz");
        assertFalse(entrySet.contains(entry));
        entry = new SimpleEntry<CharSequence, String>(XYZ, "zzz");
        assertFalse(entrySet.contains(entry));
        entry = new SimpleEntry<CharSequence, String>(XYZ, null);
        assertFalse(entrySet.contains(entry));
        entry = new SimpleEntry<CharSequence, String>(null, XYZ);
        assertFalse(entrySet.contains(entry));
        entry = new SimpleEntry<CharSequence, String>(null, null);
        assertFalse(entrySet.contains(entry));
        assertFalse(entrySet.contains("foo"));
        assertFalse(entrySet.contains(null));
    }
}
