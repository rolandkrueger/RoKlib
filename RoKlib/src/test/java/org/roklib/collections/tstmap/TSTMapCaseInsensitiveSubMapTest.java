/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 20.11.2010
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

import java.util.Set;
import java.util.SortedMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TSTMapCaseInsensitiveSubMapTest {
    private static final String EMPTY = "";
    private static final String PFORZHEIM = "PForzhEIm";
    private static final String MANNHEIM = "mannHEIM";
    private static final String WEINHEIM = "wEInhEIm";
    private static final String DRESDEN = "DREsden";
    private static final String MAGDEBURG = "maGDEburG";
    private static final String LEIPZIG = "LeipZig";
    private static final String FREIBURG = "freiBuRg";

    private TernarySearchTreeMapCaseInsensitive<String> testObj;
    private SortedMap<CharSequence, String> subMap;

    @Before
    public void setUp() {
        testObj = new TernarySearchTreeMapCaseInsensitive<String>();
        testObj.put(EMPTY, EMPTY);
        testObj.put(DRESDEN, DRESDEN);
        testObj.put(FREIBURG, FREIBURG);
        testObj.put(LEIPZIG, LEIPZIG);
        testObj.put(MAGDEBURG, MAGDEBURG);
        testObj.put(MANNHEIM, MANNHEIM);
        testObj.put(PFORZHEIM, PFORZHEIM);
        testObj.put(WEINHEIM, WEINHEIM);
        subMap = testObj.subMap("ERFURT", "marburg");
    }

    @Test
    public void testSubMap() {
        assertEquals(4, subMap.size());
        assertTrue(subMap.containsKey(FREIBURG));
        assertTrue(subMap.containsKey(LEIPZIG));
        assertTrue(subMap.containsKey(MAGDEBURG));
        assertTrue(subMap.containsKey(MANNHEIM));
    }

    @Test
    public void testKeySet() {
        Set<CharSequence> keySet = subMap.keySet();
        assertEquals(4, keySet.size());
        assertTrue(keySet.contains(MANNHEIM));
        assertTrue(keySet.contains("mannheim"));
        assertTrue(keySet.contains("MANNHEIM"));
    }

    @Test
    public void testFirstKey() {
        assertEquals(FREIBURG, subMap.firstKey());
    }

    @Test
    public void testLastKey() {
        assertEquals(MANNHEIM, subMap.lastKey());
    }

    @Test
    public void testPut() {
        subMap.put("GerA", "GERA");
        assertTrue(testObj.containsKey("GERA"));
    }

}
