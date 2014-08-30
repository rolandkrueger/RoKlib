/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 20.01.2011
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
import org.roklib.collections.TernarySearchTreeSet;

import java.util.*;

import static org.junit.Assert.*;

public class TSTSetTest {
    public static final CharSequence[] STRINGS = {"artichoke", "artichoke", "eggplant", "avocado", "courgette",
            "parsley", "basil", "juniper", "sorrel", "sorrel"};
    private final int EXPECTED_SIZE = STRINGS.length - 2; // two strings are contained twice in the array

    private TernarySearchTreeSet testObj;

    @Before
    public void setUp() {
        testObj = new TernarySearchTreeSet(STRINGS);
    }

    @Test
    public void testDefaultConstructor() {
        TernarySearchTreeSet set = new TernarySearchTreeSet();
        assertIsEmpty(set);
    }

    @Test
    public void testConstructorForCharSequenceArray() {
        assertEquals(EXPECTED_SIZE, testObj.size());
    }

    @Test
    public void testConstructorForSortedSet() {
        SortedSet<CharSequence> dataSet = new TreeSet<CharSequence>(Arrays.asList(STRINGS));
        TernarySearchTreeSet set = new TernarySearchTreeSet(dataSet);
        assertEquals(EXPECTED_SIZE, set.size());
    }

    @Test
    public void testConstructorForCollection() {
        List<CharSequence> list = new LinkedList<CharSequence>(Arrays.asList(STRINGS));
        TernarySearchTreeSet set = new TernarySearchTreeSet(list);
        assertEquals(EXPECTED_SIZE, set.size());
    }

    @Test
    public void testComparator() {
        assertNull(testObj.comparator());
    }

    @Test
    public void testFirst() {
        assertEquals("artichoke", testObj.first());
    }

    @Test
    public void testLast() {
        assertEquals("sorrel", testObj.last());
    }

    @Test
    public void testAdd() {
        assertFalse(testObj.add("clover"));
        assertEquals(STRINGS.length - 1, testObj.size());
        assertTrue(testObj.add("clover"));
        assertEquals(STRINGS.length - 1, testObj.size());
    }

    @Test(expected = NullPointerException.class)
    public void testAddNull() {
        testObj.add(null);
    }

    @Test
    public void testAddAll() {
        List<CharSequence> list = new LinkedList<CharSequence>();
        list.addAll(Arrays.asList("boletus", "chanterelle", "truffle", "truffle"));
        testObj.addAll(list);
        assertEquals(STRINGS.length + list.size() - 3, testObj.size());
    }

    @Test
    public void testContains() {
        assertTrue(testObj.contains("sorrel"));
        assertFalse(testObj.contains("boletus"));
    }

    @Test(expected = NullPointerException.class)
    public void testContainsNull() {
        testObj.contains(null);
    }

    @Test
    public void testContainsAll() {
        List<CharSequence> list = new LinkedList<CharSequence>();
        list.addAll(Arrays.asList("parsley", "basil", "juniper", "sorrel", "sorrel"));
        assertTrue(testObj.containsAll(list));
    }

    @Test
    public void testDoesNotContainAll() {
        List<CharSequence> list = new LinkedList<CharSequence>();
        list.addAll(Arrays.asList("boletus", "basil", "sorrel"));
        assertFalse(testObj.containsAll(list));
    }

    @Test(expected = NullPointerException.class)
    public void testContainsAllFails() {
        List<CharSequence> list = new LinkedList<CharSequence>();
        list.addAll(Arrays.asList(null, "basil", "sorrel"));
        testObj.containsAll(list);
    }

    @Test
    public void testRetainAll() {
        List<CharSequence> list = new LinkedList<CharSequence>();
        list.addAll(Arrays.asList("boletus", "basil", "sorrel"));
        boolean changed = testObj.retainAll(list);
        assertEquals(2, testObj.size());

        assertTrue(testObj.contains("basil"));
        assertTrue(testObj.contains("sorrel"));
        assertTrue(changed);
    }

    @Test
    public void testRetainAllDoesntChangeSet() {
        List<CharSequence> list = new LinkedList<CharSequence>();
        list.addAll(Arrays.asList(STRINGS));
        boolean changed = testObj.retainAll(list);
        assertEquals(EXPECTED_SIZE, testObj.size());
        assertFalse(changed);
    }

    @Test(expected = NullPointerException.class)
    public void testRetainAllWithNull() {
        testObj.retainAll(null);
    }

    @Test
    public void testClear() {
        testObj.clear();
        assertIsEmpty(testObj);
    }

    @Test
    public void testRemove() {
        boolean wasContained = testObj.remove("basil");
        assertTrue(wasContained);
        assertFalse(testObj.contains("basil"));

        wasContained = testObj.remove("bolete");
        assertFalse(wasContained);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNull() {
        testObj.remove(null);
    }

    @Test
    public void testRemoveAll() {
        List<CharSequence> list = new LinkedList<CharSequence>();
        list.addAll(Arrays.asList("boletus", "basil", "sorrel"));
        boolean changed = testObj.removeAll(list);
        assertTrue(changed);
        assertFalse(testObj.contains("basil"));
        assertFalse(testObj.contains("sorrel"));
        assertEquals(EXPECTED_SIZE - 2, testObj.size());
    }

    @Test
    public void testRemoveAllDoesntChange() {
        List<CharSequence> list = new LinkedList<CharSequence>();
        list.addAll(Arrays.asList("boletus", "chanterelle", "truffle", "truffle"));
        boolean changed = testObj.removeAll(list);
        assertFalse(changed);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveAllWithNull() {
        List<CharSequence> list = new LinkedList<CharSequence>();
        list.addAll(Arrays.asList(null, "basil"));
        testObj.removeAll(list);
    }

    @Test
    public void testToArray() {
        Object[] array = testObj.toArray();
        assertEquals(EXPECTED_SIZE, array.length);
        assertEquals(new Object[]{"artichoke", "avocado", "basil", "courgette", "eggplant", "juniper", "parsley",
                "sorrel"}, array);
    }

    @Test
    public void testToCharSequenceArray() {
        CharSequence[] array = testObj.toArray(new CharSequence[]{});
        assertEquals(EXPECTED_SIZE, array.length);
        assertEquals(new CharSequence[]{"artichoke", "avocado", "basil", "courgette", "eggplant", "juniper", "parsley",
                "sorrel"}, array);
    }

    @Test
    public void testToString() {
        Set<CharSequence> dataSet = new TreeSet<CharSequence>(Arrays.asList(STRINGS));
        assertEquals(dataSet.toString(), testObj.toString());
    }

    @Test
    public void testEquals() {
        Set<CharSequence> treeSet = new TreeSet<CharSequence>(Arrays.asList(STRINGS));
        Set<CharSequence> hashSet = new HashSet<CharSequence>(Arrays.asList(STRINGS));
        assertEquals(treeSet, testObj);
        assertEquals(testObj, treeSet);
        assertEquals(hashSet, testObj);
        assertEquals(testObj, hashSet);

        assertEquals(testObj, testObj);
        assertFalse(testObj.equals(null));
    }

    @Test
    public void testHashCode() {
        Set<CharSequence> treeSet = new TreeSet<CharSequence>(Arrays.asList(STRINGS));
        assertEquals(treeSet.hashCode(), testObj.hashCode());
    }

    @Test
    public void testIterator() {
        Iterator<CharSequence> testIterator = testObj.iterator();
        Set<CharSequence> treeSet = new TreeSet<CharSequence>(Arrays.asList(STRINGS));
        Iterator<CharSequence> compareIterator = treeSet.iterator();
        while (testIterator.hasNext()) {
            assertEquals(compareIterator.next(), testIterator.next());
        }
    }

    @Test
    public void testHeadSet() {
        SortedSet<CharSequence> headSet = testObj.headSet("pomegranade");
        assertNotNull(headSet);
        assertEquals(7, headSet.size());
        assertEquals(
                new TreeSet<CharSequence>(Arrays.asList("artichoke", "avocado", "basil", "courgette", "eggplant", "juniper",
                        "parsley")), headSet);
    }

    @Test
    public void testTailSet() {
        SortedSet<CharSequence> tailSet = testObj.tailSet("pomegranade");
        assertNotNull(tailSet);
        assertEquals(1, tailSet.size());
        assertTrue(tailSet.contains("sorrel"));
    }

    @Test
    public void testSubSet() {
        SortedSet<CharSequence> subSet = testObj.subSet("cranberry", "pomegranade");
        assertNotNull(subSet);
        assertEquals(3, subSet.size());
        assertEquals(new TreeSet<CharSequence>(Arrays.asList("eggplant", "juniper", "parsley")), subSet);
    }

    @Test
    public void testIndexOf() {
        assertEquals(-1, testObj.indexOf("not in set"));
        assertEquals(0, testObj.indexOf("artichoke"));
        assertEquals(EXPECTED_SIZE - 1, testObj.indexOf("sorrel"));
    }

    @Test
    public void testGetElementAt() {
        for (int index = 0; index < testObj.size(); ++index) {
            assertEquals(index, testObj.indexOf(testObj.getElementAt(index)));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetElementAtWithIndexTooLarge() {
        assertNull(testObj.getElementAt(999));
    }

    @Test
    public void testPredecessorOfFirstElementIsNull() {
        assertNull(testObj.predecessor(testObj.first()));
    }

    @Test
    public void testPredecessorForKeysInSet() {
        assertEquals("parsley", testObj.predecessor(testObj.last()));
        assertEquals("artichoke", testObj.predecessor("avocado"));
        assertEquals("avocado", testObj.predecessor("basil"));
        assertEquals("basil", testObj.predecessor("courgette"));
    }

    @Test
    public void testPredecessorForKeysNotInSet() {
        assertEquals("courgette", testObj.predecessor("cranberry"));
        assertNull(testObj.predecessor("aardvark"));
    }

    @Test
    public void testSuccessorOfLastElementIsNull() {
        assertNull(testObj.successor(testObj.last()));
    }

    @Test
    public void testSuccessorForKeysInSet() {
        assertEquals("courgette", testObj.successor("basil"));
        assertEquals("parsley", testObj.successor("juniper"));
    }

    @Test
    public void testSuccessorForKeysNotInSet() {
        assertNull(testObj.successor("yams"));
        assertEquals("juniper", testObj.successor("horseradish"));
    }

    @Test
    public void testGetPrefixMatch() {
        CharSequence[] expectedValues = new CharSequence[]{"artichoke", "avocado"};
        int index = 0;
        for (CharSequence string : testObj.getPrefixMatch("a")) {
            assertEquals(expectedValues[index], string);
            index++;
        }
    }

    @Test
    public void testPrefixMatchYieldsNoResult() {
        Iterable<CharSequence> it = testObj.getPrefixMatch("x");
        assertFalse(it.iterator().hasNext());
    }

    @Test
    public void testPrefixMatchYieldsAllElements() {
        CharSequence[] expectedValues = new CharSequence[]{"artichoke", "avocado", "basil", "courgette", "eggplant",
                "juniper", "parsley", "sorrel"};
        int index = 0;
        for (CharSequence string : testObj.getPrefixMatch("")) {
            assertEquals(expectedValues[index], string);
            index++;
        }
    }

    @Test
    public void testMatchAlmost() {
        TernarySearchTreeSet set = new TernarySearchTreeSet();
        set.addAll(Arrays.asList("xxxx", "1234", "yxxxx", "zxxx", "xxzz"));
        SortedSet<CharSequence> result = set.matchAlmost("yxxx", 1, 1);
        assertEquals(new TreeSet<CharSequence>(Arrays.asList("xxxx", "yxxxx", "zxxx")), result);
    }

    @Test
    public void testMatchAlmostYieldsNoResult() {
        assertEquals(0, testObj.matchAlmost("xxxx", 0, 0).size());
    }

    @Test
    public void testMatchAlmostYieldsAllElements() {
        assertEquals(8, testObj.matchAlmost("xxxxxxxx", 999, 999).size());
    }

    private void assertIsEmpty(TernarySearchTreeSet set) {
        assertEquals(0, set.size());
        assertTrue(set.isEmpty());
    }
}
