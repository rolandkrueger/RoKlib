/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 22.01.2011
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

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class TSTSetSubSetTest {
    private final CharSequence[] STRINGS = {"darmstadt", "frankenthal", "heidelberg", "hemsbach", "heppenheim",
            "ludwigshafen", "mannheim", "schwetzingen", "sulzbach", "viernheim", "weinheim"};

    private TernarySearchTreeSet testObj;

    @Before
    public void setUp() {
        testObj = new TernarySearchTreeSet(STRINGS);
    }

    @Test
    public void testHeadSetWithElementInSet() {
        SortedSet<CharSequence> headSet = testObj.headSet("heppenheim");
        assertFalse(headSet.contains("heppenheim"));
        assertEquals(getExpectedValues("darmstadt", "frankenthal", "heidelberg", "hemsbach"), headSet);
    }

    @Test
    public void testHeadSetWithElementNotInSet() {
        SortedSet<CharSequence> headSet = testObj.headSet("hockenheim");
        assertEquals(getExpectedValues("darmstadt", "frankenthal", "heidelberg", "hemsbach", "heppenheim"), headSet);
    }

    @Test
    public void testAddToHeadSetChangesBaseSet() {
        SortedSet<CharSequence> headSet = testObj.headSet("frankfurt");
        assertFalse(headSet.add("altrip"));
        assertTrue(testObj.contains("altrip"));
        assertTrue(headSet.add("altrip"));
    }

    @Test
    public void testAddToBaseSetChangesHeadSet() {
        SortedSet<CharSequence> headSet = testObj.headSet("frankfurt");
        testObj.add("altrip");
        assertTrue(headSet.contains("altrip"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddToHeadSetOutOfBoundsThrowsExcpetion() {
        testObj.headSet("dresden").add("pforzheim");
    }

    @Test(expected = NullPointerException.class)
    public void testHeadSetContainsNullThrowsException() {
        testObj.headSet("pforzheim").contains(null);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullFromHeadSetThrowsException() {
        testObj.headSet("dresden").remove(null);
    }

    @Test
    public void testRemoveSucceeds() {
        SortedSet<CharSequence> headSet = testObj.headSet("frankfurt");
        assertTrue(headSet.remove("darmstadt"));
        assertFalse(testObj.contains("darmstadt"));
    }

    @Test
    public void testRemoveNotSuccessful() {
        SortedSet<CharSequence> headSet = testObj.headSet("frankfurt");
        assertFalse(headSet.remove("weinheim"));
        assertTrue(testObj.contains("weinheim"));
    }

    @Test
    public void testHeadSetFirst() {
        assertEquals(testObj.first(), testObj.headSet("weinheim").first());
    }

    @Test
    public void testHeadSetLast() {
        assertEquals("sulzbach", testObj.headSet("viernheim").last());
    }

    @Test
    public void testHeadSetOfHeadSetSuccessful() {
        SortedSet<CharSequence> headSet = testObj.headSet("hemsbach").headSet("frankfurt");
        assertEquals(getExpectedValues("darmstadt", "frankenthal"), headSet);
        headSet.add("altrip");
        assertTrue(testObj.contains("altrip"));
    }

    @Test
    public void testHeadSetTailSet() {
        SortedSet<CharSequence> headSet = testObj.headSet("hemsbach").tailSet("frankenthal");
        assertEquals(getExpectedValues("frankenthal", "heidelberg"), headSet);
    }

    @Test
    public void testHeadSetSubSet() {
        SortedSet<CharSequence> headSet = testObj.headSet("viernheim").subSet("heidelberg", "heppenheim");
        assertEquals(getExpectedValues("heidelberg", "hemsbach"), headSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHeadSetOfHeadSetFails() {
        testObj.headSet("frankfurt").headSet("hemsbach");
    }

    @Test(expected = NullPointerException.class)
    public void testHeadSetFailsWithNullBound() {
        testObj.headSet(null);
    }

    @Test(expected = NullPointerException.class)
    public void testTailSetFailsWithNullBound() {
        testObj.tailSet(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSubSetFailsWithNullLowerBound() {
        testObj.subSet(null, "");
    }

    @Test(expected = NullPointerException.class)
    public void testSubSetFailsWithNullUpperBound() {
        testObj.subSet("", null);
    }

    @Test
    public void testHeadSetComparatorReturnsNull() {
        assertNull(testObj.headSet("weinheim").comparator());
    }

    private SortedSet<CharSequence> getExpectedValues(CharSequence... values) {
        return new TreeSet<CharSequence>(Arrays.asList(values));
    }
}
