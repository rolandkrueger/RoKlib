/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 09.06.2007
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
import org.roklib.util.RandomStringIDGenerator;

import java.util.*;
import java.util.Map.Entry;

import static org.junit.Assert.*;

public class TernarySearchTreeMapTest {
    private static final String NOT_IN_MAP = "not in map";
    private static final String VALUE1 = "another value";
    private static final String VALUE2 = "";
    private static final String VALUE3 = "last value";
    private TernarySearchTreeMap<String> testObj, copy;

    @Before
    public void setUp() {
        testObj = new TernarySearchTreeMap<String>();
        copy = new TernarySearchTreeMap<String>();
    }

    @Test
    public void testEquals() {
        String key = "some key";
        String value = "some value";
        testObj.put(key, value);
        testObj.put(VALUE3, VALUE1);
        testObj.put(VALUE2, VALUE2);
        testObj.put(VALUE1, VALUE3);
        TernarySearchTreeMap<String> other = new TernarySearchTreeMap<String>();
        other.put(key, value);
        other.put(VALUE1, VALUE3);
        other.put(VALUE2, VALUE2);
        other.put(VALUE3, VALUE1);
        assertTrue(testObj.equals(testObj));
        assertFalse(testObj.equals(null));
        assertTrue(testObj.equals(other));
        assertTrue(other.equals(testObj));
    }

    @Test
    public void testNotEqual() {
        String key = "some key";
        String value = "some value";
        testObj.put(key, value);
        testObj.put(VALUE3, VALUE1);
        testObj.put(VALUE2, VALUE2);
        testObj.put(VALUE1, VALUE3);
        TernarySearchTreeMap<String> other = new TernarySearchTreeMap<String>();
        other.put(key, value);
        other.put(VALUE1, VALUE3);
        other.put(VALUE2, VALUE2);
        assertFalse(testObj.equals(other));
        assertFalse(other.equals(testObj));
    }

    @Test
    public void testHashCode() {
        String key = "some key";
        String value = "some value";
        testObj.put(key, value);
        testObj.put(VALUE3, VALUE1);
        testObj.put(VALUE2, VALUE2);
        testObj.put(VALUE1, VALUE3);
        TernarySearchTreeMap<String> other = new TernarySearchTreeMap<String>();
        other.put(key, value);
        other.put(VALUE1, VALUE3);
        other.put(VALUE2, VALUE2);
        other.put(VALUE3, VALUE1);
        assertTrue(testObj.hashCode() == other.hashCode());
        assertTrue(testObj.hashCode() == testObj.hashCode());
    }

    @Test
    public void testHashCodeNotEqual() {
        String key = "some key";
        String value = "some value";
        testObj.put(key, value);
        testObj.put(VALUE3, VALUE1);
        testObj.put(VALUE2, VALUE2);
        testObj.put(VALUE1, VALUE3);
        TernarySearchTreeMap<String> other = new TernarySearchTreeMap<String>();
        other.put(key, value);
        other.put(VALUE1, VALUE3);
        other.put(VALUE2, VALUE2);
        assertFalse(testObj.hashCode() == other.hashCode());
    }

    @Test
    public void testConstructorWithMapParameter() {
        Map<CharSequence, String> sourceMap = new HashMap<CharSequence, String>();
        sourceMap.put(VALUE1, VALUE1);
        sourceMap.put(VALUE2, VALUE2);
        sourceMap.put(VALUE3, VALUE3);
        TernarySearchTreeMap<String> map = new TernarySearchTreeMap<String>(sourceMap);

        assertEquals(3, map.size());
        assertTrue(map.get(VALUE1).equals(VALUE1));
        assertTrue(map.get(VALUE2).equals(VALUE2));
        assertTrue(map.get(VALUE3).equals(VALUE3));
    }

    @Test
    public void testConstructorWithSortedMapParameter() {
        TreeMap<CharSequence, String> sourceMap = new TreeMap<CharSequence, String>();
        sourceMap.put(VALUE1, VALUE1);
        sourceMap.put(VALUE2, VALUE2);
        sourceMap.put(VALUE3, VALUE3);
        TernarySearchTreeMap<String> map = new TernarySearchTreeMap<String>(sourceMap);
        assertEquals(3, map.size());
        assertTrue(map.get(VALUE1).equals(VALUE1));
        assertTrue(map.get(VALUE2).equals(VALUE2));
        assertTrue(map.get(VALUE3).equals(VALUE3));
        assertEquals(sourceMap.comparator(), map.comparator());
    }

    @Test
    public void testContainsKey() {
        fillMaps();
        assertTrue(testObj.containsKey(VALUE1));
        assertTrue(testObj.containsKey(VALUE2));
        assertTrue(testObj.containsKey(VALUE3));
        assertFalse(testObj.containsKey(NOT_IN_MAP));
    }

    @Test
    public void testContainsValue() {
        fillMaps();
        assertTrue(testObj.containsValue(VALUE1));
        assertTrue(testObj.containsValue(VALUE2));
        assertTrue(testObj.containsValue(VALUE3));
        assertFalse(testObj.containsValue(NOT_IN_MAP));
    }

    @Test
    public void testPut() {
        String o = new String();
        String formerValue = testObj.put("testKey", o);
        assertEquals(testObj.get("testKey"), o);
        assertNull(formerValue);

        testObj.clear();
        String x = "x";
        testObj.put(x, x);
        testObj.put("", "empty");
        assertEquals(x, testObj.get(x));
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        testObj.put(null, "");
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        testObj.put("", null);
    }

    @Test
    public void testClear() {
        fillMaps();
        assertEquals(3, testObj.size());
        testObj.clear();
        assertEquals(0, testObj.size());
        assertTrue(testObj.isEmpty());
    }

    @Test
    public void testRemoveWithSuccess() {
        Set<String> testKeySet = new HashSet<String>();
        Map<String, String> compareMap = new HashMap<String, String>();
        Random random = new Random(System.currentTimeMillis());

        String x = "x";
        testKeySet.add(x);
        compareMap.put(x, x);
        testObj.put(x, x);

        String key;
        for (int i = 0; i < 200; ++i) {
            key = RandomStringIDGenerator.getUniqueID(random.nextInt(20) + 1);
            testKeySet.add(key);
            compareMap.put(key, key);
            testObj.put(key, key);
            assertEquals(testObj, compareMap);
        }

        for (String removeKey : new HashSet<String>(testKeySet)) {
            testKeySet.remove(removeKey);
            compareMap.remove(removeKey);
            assertEquals(removeKey, testObj.remove(removeKey));
            assertEquals(testObj, compareMap);
            assertEquals(testKeySet.size(), testObj.size());
        }

        assertTrue(testObj.isEmpty());
    }

    @Test
    public void testRemoveWithoutSuccess() {
        fillMaps();
        assertNull(testObj.remove(NOT_IN_MAP));
        assertEquals(3, testObj.size());
    }

    @Test
    public void testIsEmpty() {
        fillMaps();
        testObj.clear();
        assertTrue(testObj.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEntrySetValueNull() {
        fillMaps();
        testObj.getEntry(VALUE2).setValue(null);
    }

    @Test
    public void testGetEntry() {
        fillMaps();
        assertEquals(VALUE1, testObj.getEntry(VALUE1).getValue());
        assertEquals(VALUE1, testObj.getEntry(VALUE1).getKey());
        assertEquals(VALUE2, testObj.getEntry(VALUE2).getValue());
        assertEquals(VALUE2, testObj.getEntry(VALUE2).getKey());
        assertEquals(VALUE3, testObj.getEntry(VALUE3).getValue());
        assertEquals(VALUE3, testObj.getEntry(VALUE3).getKey());

        for (Iterator<Map.Entry<CharSequence, String>> it = testObj.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<CharSequence, String> entry = it.next();
            assertEquals(entry, testObj.getEntry(entry.getKey()));
            assertFalse(testObj.getEntry(entry.getKey()).equals(testObj));
        }
    }

    @Test
    public void testGetKeyAt() {
        fillMaps();
        assertTrue(testObj.getKeyAt(1).equals(VALUE1)); // VALUE1 is the second value
        // due to the map's ordering
    }

    @Test
    public void testGetValueAt() {
        fillMaps();
        assertTrue(testObj.getValueAt(1).equals(VALUE1)); // VALUE1 is the second
        // value due to the map's
        // ordering
    }

    @Test
    public void testIndexOf() {
        fillMaps();
        assertEquals(0, testObj.indexOf(VALUE2));
        assertEquals(1, testObj.indexOf(VALUE1));
        assertEquals(2, testObj.indexOf(VALUE3));
        assertEquals(-1, testObj.indexOf(NOT_IN_MAP));
    }

    @Test
    public void testPredecessorEntryWithKeyFromMap() {
        fillMapWithFiveLowerCaseEntriesOrderingExpected();
        Entry<CharSequence, String> predecessor = testObj.predecessorEntry("sverige");
        assertEquals("suomi", predecessor.getKey());
        assertEquals("salmiakki", predecessor.getValue());
    }

    @Test(expected = ClassCastException.class)
    public void testPredecessorEntryFailsWithIncorrectDataType() {
        testObj.predecessorEntry(new Object());
    }

    @Test
    public void testNoPredecessorEntryWithKeyFromMap() {
        fillMapWithFiveLowerCaseEntriesOrderingExpected();
        assertNull(testObj.predecessorEntry("deutschland"));
    }

    @Test
    public void testPredecessorEntryWithKeyNotInMap() {
        fillMapWithFiveLowerCaseEntriesOrderingExpected();
        Entry<CharSequence, String> predecessor = testObj.predecessorEntry("schland");
        assertNotNull(predecessor);
        assertEquals("france", predecessor.getKey());
        assertEquals("fromage", predecessor.getValue());
    }

    @Test
    public void testPredecessorEntryForFirstKey() {
        fillMapWithFiveLowerCaseEntriesOrderingExpected();
        assertNull(testObj.predecessorEntry("deutschland"));
    }

    @Test
    public void testSuccessorEntryWithKeyFromMap() {
        fillMapWithFiveLowerCaseEntriesOrderingExpected();
        Entry<CharSequence, String> successor = testObj.successorEntry("deutschland");
        assertEquals("england", successor.getKey());
        assertEquals("fishnchips", successor.getValue());
    }

    @Test
    public void testSuccessorEntryWithKeyNotInMap() {
        fillMapWithFiveLowerCaseEntriesOrderingExpected();
        Entry<CharSequence, String> successor = testObj.successorEntry("schland");
        assertNotNull(successor);
        assertEquals("suomi", successor.getKey());
        assertEquals("salmiakki", successor.getValue());
    }

    @Test
    public void testSuccessorEntryForLastKey() {
        fillMapWithFiveLowerCaseEntriesOrderingExpected();
        assertNull(testObj.successorEntry("sverige"));
    }

    @Test(expected = ClassCastException.class)
    public void testSuccessorEntryFailsWithIncorrectDataType() {
        testObj.successorEntry(new Object());
    }

    @Test
    public void testNoSuccessorEntryWithKeyFromMap() {
        fillMapWithFiveLowerCaseEntriesOrderingExpected();
        assertNull(testObj.successorEntry("sverige"));
    }

    @Test
    public void testPredecessorOfFirstElementIsNull() {
        fillMapWithFiveLowerCaseEntriesOrderingExpected();
        assertNull(testObj.predecessor(testObj.firstKey()));
    }

    @Test
    public void testPredecessorForKeysInSet() {
        fillMapWithFiveLowerCaseEntriesOrderingExpected();
        assertEquals("suomi", testObj.predecessor(testObj.lastKey()));
        assertEquals("deutschland", testObj.predecessor("england"));
        assertEquals("england", testObj.predecessor("espana"));
        assertEquals("espana", testObj.predecessor("france"));
    }

    @Test
    public void testPredecessorForKeysNotInSet() {
        fillMapWithFiveLowerCaseEntriesOrderingExpected();
        assertEquals("france", testObj.predecessor("ghana"));
        assertNull(testObj.predecessor("australia"));
    }

    @Test
    public void testSuccessorOfLastElementIsNull() {
        fillMapWithFiveLowerCaseEntriesOrderingExpected();
        assertNull(testObj.successor(testObj.lastKey()));
    }

    @Test
    public void testSuccessorForKeysInSet() {
        fillMapWithFiveLowerCaseEntriesOrderingExpected();
        assertEquals("england", testObj.successor("deutschland"));
        assertEquals("sverige", testObj.successor("suomi"));
    }

    @Test
    public void testSuccessorForKeysNotInSet() {
        fillMapWithFiveLowerCaseEntriesOrderingExpected();
        assertNull(testObj.successor("vietnam"));
        assertEquals("deutschland", testObj.successor("australia"));
    }

    @Test
    public void testGetKeysForPrefix() {
        fillMaps();
        testObj.put("land", "");
        testObj.put("lasso", "");
        testObj.put("lasting", "");
        Iterator<CharSequence> it = testObj.getPrefixMatch("la").iterator();
        assertEquals("land", it.next());
        assertEquals("lasso", it.next());
        assertEquals("last value", it.next());
        assertEquals("lasting", it.next());

        it = testObj.getPrefixMatch("las").iterator();
        assertEquals("lasso", it.next());
        assertEquals("last value", it.next());
        assertEquals("lasting", it.next());

        it = testObj.getPrefixMatch("lasso").iterator();
        assertEquals("lasso", it.next());
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratorRemoveFail1() {
        fillMaps();
        testObj.entrySet().iterator().remove();
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratorRemoveFail2() {
        fillMaps();
        Iterator<Map.Entry<CharSequence, String>> it = testObj.entrySet().iterator();
        it.next();
        it.remove();
        it.remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorRemoveFail3() {
        fillMaps();
        Iterator<Map.Entry<CharSequence, String>> it = testObj.entrySet().iterator();
        while (it.hasNext())
            it.next();
        it.remove();
    }

    private void fillMapWithFiveLowerCaseEntriesOrderingExpected() {
        TestDataFixture testData = MapData.getFiveLowerCaseEntriesOrderingExpected();
        testObj.putAll(testData.getData());
    }

    private void fillMaps() {
        testObj.put(VALUE1, VALUE1);
        testObj.put(VALUE2, VALUE2);
        testObj.put(VALUE3, VALUE3);
        copy.put(VALUE1, VALUE1);
        copy.put(VALUE2, VALUE2);
        copy.put(VALUE3, VALUE3);
    }
}
