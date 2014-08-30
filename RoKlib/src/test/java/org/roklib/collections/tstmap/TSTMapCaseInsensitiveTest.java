/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 06.11.2010
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

import java.util.*;
import java.util.Map.Entry;

import static org.junit.Assert.*;

public class TSTMapCaseInsensitiveTest {
    private static final String EMPTY = "";
    private static final String ABCD = "ABcd";
    private static final String XYZ = "xyz";
    private static final String FOO = "Foo";
    private static final String BAR = "Bar";
    private static final String BAZ = "BAZ";

    private TernarySearchTreeMapCaseInsensitive<String> testObj;

    @Before
    public void setUp() {
        testObj = new TernarySearchTreeMapCaseInsensitive<String>();
        testObj.put(EMPTY, EMPTY);
        testObj.put(BAR, BAR);
        testObj.put(BAZ, BAZ);
        testObj.put(ABCD, ABCD);
        testObj.put(XYZ, XYZ);
        testObj.put(FOO, FOO);
    }

    @Test
    public void testSubMap() {
        SortedMap<CharSequence, String> subMap = testObj.subMap("AXXX", "GGG");
        assertEquals(3, subMap.size());
        assertTrue(subMap.containsKey("baR"));
        assertTrue(subMap.containsKey("Baz"));
        assertTrue(subMap.containsKey("fOO"));
        assertTrue(subMap.containsValue(BAR));
        assertTrue(subMap.containsValue(BAZ));
        assertTrue(subMap.containsValue(FOO));

        assertEquals(BAR, subMap.firstKey());
        assertEquals(FOO, subMap.lastKey());

        Set<CharSequence> subMapKeySet = subMap.keySet();
        assertTrue(subMapKeySet.contains("bAr"));
        assertTrue(subMapKeySet.contains("fOo"));
        assertTrue(subMapKeySet.contains(BAZ));
        assertFalse(subMapKeySet.contains(XYZ));
    }

    @Test
    public void testSuccessor() {
        assertNull(testObj.successorEntry(XYZ));
        assertEquals(ABCD, testObj.successorEntry(EMPTY).getKey());
        assertEquals(FOO, testObj.successorEntry("baZ").getKey());
        assertEquals(BAR, testObj.successorEntry("abCD").getKey());
        assertEquals(FOO, testObj.successorEntry("exx").getKey());
    }

    @Test
    public void testPredecessor() {
        assertNull(testObj.predecessorEntry(EMPTY));
        assertEquals(BAZ, testObj.predecessorEntry("bxx").getKey());
        assertEquals(EMPTY, testObj.predecessorEntry(ABCD).getKey());
        assertEquals(BAZ, testObj.predecessorEntry("fOO").getKey());
        assertEquals(ABCD, testObj.predecessorEntry("baR").getKey());
    }

    @Test
    public void testGetValueAt() {
        assertEquals(EMPTY, testObj.getValueAt(0));
        assertEquals(ABCD, testObj.getValueAt(1));
        assertEquals(BAR, testObj.getValueAt(2));
        assertEquals(BAZ, testObj.getValueAt(3));
        assertEquals(FOO, testObj.getValueAt(4));
        assertEquals(XYZ, testObj.getValueAt(5));
    }

    @Test
    public void testGetEntry() {
        Map.Entry<CharSequence, String> entry = testObj.getEntry("abCD");
        assertNotNull(entry);
        assertEquals(ABCD, entry.getKey());
        assertEquals(ABCD, entry.getValue());
        entry = testObj.getEntry("bar");
        assertNotNull(entry);
        assertEquals(BAR, entry.getKey());
        assertEquals(BAR, entry.getValue());
        entry = testObj.getEntry("bAZ");
        assertNotNull(entry);
        assertEquals(BAZ, entry.getKey());
        assertEquals(BAZ, entry.getValue());
        entry = testObj.getEntry("fOO");
        assertNotNull(entry);
        assertEquals(FOO, entry.getKey());
        assertEquals(FOO, entry.getValue());
        entry = testObj.getEntry("Xyz");
        assertNotNull(entry);
        assertEquals(XYZ, entry.getKey());
        assertEquals(XYZ, entry.getValue());
        assertNull(testObj.getEntry("not in map"));
    }

    @Test
    public void testGetPrefixSubtreeIterator() {
        Iterator<Entry<CharSequence, String>> iterator = testObj.getPrefixSubtreeIterator("bA").iterator();
        assertTrue(iterator.hasNext());
        assertEquals(BAR, iterator.next().getKey());
        assertTrue(iterator.hasNext());
        assertEquals(BAZ, iterator.next().getKey());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetKeysForPrefix() {
        List<CharSequence> keys = new ArrayList<CharSequence>();
        for (CharSequence key : testObj.getPrefixMatch("BA")) {
            keys.add(key);
        }
        assertEquals(2, keys.size());
        assertEquals(BAR, keys.get(0));
        assertEquals(BAZ, keys.get(1));
    }

    @Test
    public void testMatchAlmost() {
        Set<CharSequence> resultSet = testObj.matchAlmost("bam", 1, 0);
        assertEquals(2, resultSet.size());
        assertTrue(resultSet.contains(BAR));
        assertTrue(resultSet.contains(BAZ));
        assertFalse(resultSet.contains("bar"));
        assertFalse(resultSet.contains("baz"));
        assertFalse(resultSet.contains("bam"));
    }

    @Test
    public void testContainsValue() {
        assertTrue(testObj.containsValue(EMPTY));
        assertTrue(testObj.containsValue(ABCD));
        assertTrue(testObj.containsValue(BAZ));
        assertTrue(testObj.containsValue(FOO));
        assertTrue(testObj.containsValue(BAR));
        assertTrue(testObj.containsValue(XYZ));
    }

    @Test
    public void testValues() {
        Collection<String> values = testObj.values();
        ArrayList<String> valuesList = new ArrayList<String>();
        valuesList.addAll(values);
        ArrayList<String> expected = new ArrayList<String>();
        expected.addAll(Arrays.asList(EMPTY, ABCD, BAR, BAZ, FOO, XYZ));
        assertEquals(expected, valuesList);
    }

    @Test
    public void testFirstKey() {
        assertEquals(EMPTY, testObj.firstKey());
        testObj.remove(EMPTY);
        assertEquals(ABCD, testObj.firstKey());
        testObj.remove(ABCD);
        assertEquals(BAR, testObj.firstKey());
        testObj.remove(BAR);
        assertEquals(BAZ, testObj.firstKey());
        testObj.remove(BAZ);
        assertEquals(FOO, testObj.firstKey());
        testObj.remove(FOO);
        assertEquals(XYZ, testObj.firstKey());
        testObj.remove(XYZ);
        assertNull(testObj.firstKey());
    }

    @Test
    public void testLastKey() {
        assertEquals(XYZ, testObj.lastKey());
        testObj.remove(XYZ);
        assertEquals(FOO, testObj.lastKey());
        testObj.remove(FOO);
        assertEquals(BAZ, testObj.lastKey());
        testObj.remove(BAZ);
        assertEquals(BAR, testObj.lastKey());
        testObj.remove(BAR);
        assertEquals(ABCD, testObj.lastKey());
        testObj.remove(ABCD);
        assertEquals(EMPTY, testObj.lastKey());
        testObj.remove(EMPTY);
        assertNull(testObj.lastKey());
    }

    @Test
    public void testKeySet() {
        Set<String> expected = new HashSet<String>();
        expected.addAll(Arrays.asList(EMPTY, ABCD, XYZ, FOO, BAR, BAZ));
        assertEquals(expected, testObj.keySet());
        Set<CharSequence> keySet = testObj.keySet();
        assertTrue(keySet.contains(EMPTY));
        assertTrue(keySet.contains("abcd"));
        assertTrue(keySet.contains("ABCD"));
        assertTrue(keySet.contains("fOO"));
        assertTrue(keySet.contains("XyZ"));
        assertTrue(keySet.contains("bar"));
        assertTrue(keySet.contains("baZ"));
    }

    @Test
    public void testKeySetRemove() {
        Set<CharSequence> keySet = testObj.keySet();
        assertTrue(keySet.remove("abCD"));
        assertEquals(5, testObj.size());
        assertFalse(keySet.remove("not in map"));
    }

    @Test
    public void testGetKeyAt() {
        assertEquals(EMPTY, testObj.getKeyAt(0));
        assertEquals(ABCD, testObj.getKeyAt(1));
        assertEquals(BAR, testObj.getKeyAt(2));
        assertEquals(BAZ, testObj.getKeyAt(3));
        assertEquals(FOO, testObj.getKeyAt(4));
        assertEquals(XYZ, testObj.getKeyAt(5));
    }

    @Test
    public void testIndexOf() {
        assertEquals(0, testObj.indexOf(EMPTY));
        assertEquals(1, testObj.indexOf(ABCD));
        assertEquals(2, testObj.indexOf(BAR));
        assertEquals(3, testObj.indexOf(BAZ));
        assertEquals(4, testObj.indexOf(FOO));
        assertEquals(5, testObj.indexOf(XYZ));
    }

    @Test
    public void testGet() {
        String value1 = testObj.get("Foo");
        String value2 = testObj.get("FOO");
        String value3 = testObj.get("FoO");

        assertSame(value1, value2);
        assertSame(value1, value3);
        assertSame(value2, value3);
        assertSame(testObj.get("BAZ"), testObj.get("baz"));
        assertEquals(ABCD, testObj.get(ABCD));
        assertEquals(XYZ, testObj.get("xYz"));
        assertEquals(BAR, testObj.get("bar"));
        assertEquals(BAZ, testObj.get("baz"));
        assertEquals(EMPTY, testObj.get(EMPTY));
    }

    @Test
    public void testContainsKey() {
        assertFalse(testObj.containsKey("bzz"));
        assertTrue(testObj.containsKey("abcd"));
        assertTrue(testObj.containsKey("fOO"));
        assertTrue(testObj.containsKey("XyZ"));
        assertTrue(testObj.containsKey("Bar"));
        assertTrue(testObj.containsKey("BAR"));
        assertTrue(testObj.containsKey("bar"));
        assertTrue(testObj.containsKey("Baz"));
        assertTrue(testObj.containsKey("BAZ"));
        assertTrue(testObj.containsKey("baz"));
    }

    @Test
    public void testRemove() {
        assertNull(testObj.remove("bxx"));
        assertEquals(EMPTY, testObj.remove(EMPTY));
        assertEquals(ABCD, testObj.remove(ABCD));
        assertEquals(XYZ, testObj.remove("XYZ"));
        assertEquals(BAZ, testObj.remove("baz"));
        assertEquals(FOO, testObj.remove("fOO"));
        assertEquals(BAR, testObj.remove("bAr"));
        assertTrue(testObj.isEmpty());

        testObj.put("TESTKEY1", "value1");
        testObj.put("TEsTKEY2", "value2");
        testObj.put("TEsTKeY3", "value3");

        assertEquals("value3", testObj.remove("testkey3"));
        assertEquals("value2", testObj.remove("testkey2"));
        assertEquals("value1", testObj.remove("testkey1"));
        assertTrue(testObj.isEmpty());
    }

    @Test
    public void testPut() {
        testObj.clear();
        assertNull(testObj.put("key", "key"));
        assertEquals("key", testObj.put("KEY", "key"));
        assertEquals("key", testObj.put("Key", "value"));
        assertEquals(1, testObj.size());
        assertEquals("Key", testObj.keySet().iterator().next());
        assertEquals("value", testObj.get("KEY"));
    }

    @Test
    public void testPredecessorOfFirstElementIsNull() {
        assertNull(testObj.predecessor(testObj.firstKey()));
    }

    @Test
    public void testPredecessorForKeysInSet() {
        assertEquals(FOO, testObj.predecessor(testObj.lastKey()));
        assertEquals(ABCD, testObj.predecessor("bar"));
        assertEquals(EMPTY, testObj.predecessor("ABCD"));
        assertEquals(BAR, testObj.predecessor("BAz"));
    }

    @Test
    public void testPredecessorForKeysNotInSet() {
        assertEquals(EMPTY, testObj.predecessor("aaa"));
        testObj.remove(EMPTY);
        assertNull(testObj.predecessor("aaa"));
    }

    @Test
    public void testSuccessorOfLastElementIsNull() {
        assertNull(testObj.successor(testObj.lastKey()));
    }

    @Test
    public void testSuccessorForKeysInSet() {
        assertEquals(BAZ, testObj.successor("bar"));
        assertEquals(XYZ, testObj.successor("fOO"));
    }

    @Test
    public void testSuccessorForKeysNotInSet() {
        assertNull(testObj.successor("zzz"));
        assertEquals(ABCD, testObj.successor("aaa"));
    }
}
