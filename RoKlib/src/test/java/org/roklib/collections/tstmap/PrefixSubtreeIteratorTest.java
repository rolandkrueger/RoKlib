/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 23.09.2007
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
import java.util.Arrays;
import java.util.Map.Entry;

import static org.junit.Assert.assertEquals;

public class PrefixSubtreeIteratorTest {
    private final static String ABIBOR = "Abigor";
    private final static String ABORTED = "Aborted";
    private final static String AMON_AMARTH = "Amon Amarth";
    private final static String AMORPHIS = "Amorphis";
    private final static String BLACK_DESTINY = "Black Destiny";
    private final static String BLACKFIELD = "Blackfield";
    private final static String OPERA_IX = "Opera IX";
    private final static String OPETH = "Opeth";
    private final static String PORCUPINE_TREE = "Porcupine Tree";
    private final static String PORTISHEAD = "Portishead";

    private TernarySearchTreeMap<String> map;

    @Before
    public void setUp() {
        map = new TernarySearchTreeMap<String>();
        map.put(ABIBOR, ABIBOR);
        map.put(ABORTED, ABORTED);
        map.put(AMON_AMARTH, AMON_AMARTH);
        map.put(AMORPHIS, AMORPHIS);
        map.put(BLACK_DESTINY, BLACK_DESTINY);
        map.put(BLACKFIELD, BLACKFIELD);
        map.put(OPERA_IX, OPERA_IX);
        map.put(OPETH, OPETH);
        map.put(PORCUPINE_TREE, PORCUPINE_TREE);
        map.put(PORTISHEAD, PORTISHEAD);
    }

    @Test
    public void testInversePrefixSearchWithEmptyString() {
        String[] result = {};
        compareResults(map.getPrefixSubtreeIterator("", true), result);
    }

    @Test
    public void testInversePrefixSearchWithExactPrefix() {
        String[] result = {ABIBOR, ABORTED, AMON_AMARTH, AMORPHIS, BLACK_DESTINY, BLACKFIELD, OPERA_IX, PORCUPINE_TREE,
                PORTISHEAD};
        compareResults(map.getPrefixSubtreeIterator(OPETH, true), result);
    }

    @Test
    public void testInversePrefixSearchWithShortPrefix() {
        map.put("Op", "");
        String[] result = {ABIBOR, ABORTED, AMON_AMARTH, AMORPHIS, BLACK_DESTINY, BLACKFIELD, "Op", PORCUPINE_TREE,
                PORTISHEAD};
        compareResults(map.getPrefixSubtreeIterator("Ope", true), result);
    }

    @Test
    public void testInversePrefixSearchWithEmptyStringInMap() {
        map.put("", "");
        String[] result = {"", ABIBOR, ABORTED, AMORPHIS, BLACK_DESTINY, BLACKFIELD, OPERA_IX, OPETH, PORCUPINE_TREE,
                PORTISHEAD};
        compareResults(map.getPrefixSubtreeIterator("Amon", true), result);
    }

    @Test
    public void testInversePrefixSearchWithKeyNotInMapWithEmptyStringInMap() {
        map.put("", "");
        String[] result = {"", ABIBOR, ABORTED, AMON_AMARTH, AMORPHIS, BLACK_DESTINY, BLACKFIELD, OPERA_IX, OPETH,
                PORCUPINE_TREE, PORTISHEAD};
        compareResults(map.getPrefixSubtreeIterator("xxx", true), result);
    }

    @Test
    public void testInversePrefixSearch() {
        String[] result = {ABIBOR, ABORTED, BLACK_DESTINY, BLACKFIELD, OPERA_IX, OPETH, PORCUPINE_TREE, PORTISHEAD};
        compareResults(map.getPrefixSubtreeIterator("Amo", true), result);
    }

    @Test
    public void testInversePrefixSearchWithKeyNotInMap() {
        String[] result = {ABIBOR, ABORTED, AMON_AMARTH, AMORPHIS, BLACK_DESTINY, BLACKFIELD, OPERA_IX, OPETH,
                PORCUPINE_TREE, PORTISHEAD};
        compareResults(map.getPrefixSubtreeIterator("xxx", true), result);
    }

    @Test
    public void testOneResultInPrefixSubtree() {
        String[] result = {OPERA_IX};
        compareResults(map.getPrefixSubtreeIterator("Opera"), result);
    }

    @Test
    public void testExactMatch() {
        String[] result = {PORCUPINE_TREE};
        compareResults(map.getPrefixSubtreeIterator(PORCUPINE_TREE), result);
    }

    @Test
    public void testNoMatch() {
        String[] result = {};
        compareResults(map.getPrefixSubtreeIterator("prefix_not_contained"), result);
    }

    @Test
    public void testMatchAtMapsBeginning() {
        String[] result = {ABIBOR, ABORTED};
        compareResults(map.getPrefixSubtreeIterator("Ab"), result);
    }

    @Test
    public void testMatchAtMapsEnd() {
        String[] result = {PORCUPINE_TREE, PORTISHEAD};
        compareResults(map.getPrefixSubtreeIterator("Por"), result);
    }

    @Test
    public void testMatchEmptyPrefix() {
        String[] result = {ABIBOR, ABORTED, AMON_AMARTH, AMORPHIS, BLACK_DESTINY, BLACKFIELD, OPERA_IX, OPETH,
                PORCUPINE_TREE, PORTISHEAD};
        compareResults(map.getPrefixSubtreeIterator(""), result);
    }

    private void compareResults(Iterable<Entry<CharSequence, String>> iterable, String[] values) {
        ArrayList<CharSequence> valueList = new ArrayList<CharSequence>(Arrays.asList(values));
        ArrayList<CharSequence> iteratorValues = new ArrayList<CharSequence>();
        while (iterable.iterator().hasNext()) {
            Entry<CharSequence, String> entry = iterable.iterator().next();
            iteratorValues.add(entry.getKey());
        }

        assertEquals(valueList, iteratorValues);
    }
}
