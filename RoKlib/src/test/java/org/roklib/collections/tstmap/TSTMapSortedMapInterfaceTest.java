/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 23.12.2009
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

import java.util.SortedMap;
import java.util.TreeMap;

import static junit.framework.Assert.*;

public class TSTMapSortedMapInterfaceTest {
    private final static String APACHE = "Apache";
    private final static String AUDACITY = "Audacity";
    private final static String CDEX = "CDEx";
    private final static String DEBIAN = "Debian";
    private final static String ECLIPSE = "Eclipse";
    private final static String EMACS = "Emacs";
    private final static String FEDORA = "Fedora";
    private final static String FIREFOX = "Firefox";
    private final static String GIMP = "Gimp";
    private final static String GNOME = "Gnome";
    private final static String INKSCAPE = "Inkscape";
    private final static String JOOMLA = "Joomla";
    private final static String KDE = "KDE";
    private final static String LEXIS = "Lexis";
    private final static String LINUX = "Linux";
    private final static String OPENOFFICE = "OpenOffice";
    private final static String ROKLIB = "RoKlib";
    private final static String THUNDERBIRD = "Thunderbird";
    private final static String UBUNTU = "Ubuntu";
    private final static String VLC = "VLC";
    private final static String XMMS = "XMMS";

    private TernarySearchTreeMap<String> testObj;

    @Before
    public void setUp() {
        testObj = new TernarySearchTreeMap<String>();
        testObj.put(DEBIAN, DEBIAN);
        testObj.put(EMACS, EMACS);
        testObj.put(GNOME, GNOME);
        testObj.put(XMMS, XMMS);
        testObj.put(FEDORA, FEDORA);
        testObj.put(INKSCAPE, INKSCAPE);
        testObj.put(FIREFOX, FIREFOX);
        testObj.put(THUNDERBIRD, THUNDERBIRD);
        testObj.put(LINUX, LINUX);
        testObj.put(JOOMLA, JOOMLA);
        testObj.put(GIMP, GIMP);
        testObj.put(UBUNTU, UBUNTU);
        testObj.put(KDE, KDE);
        testObj.put(CDEX, CDEX);
        testObj.put(APACHE, APACHE);
        testObj.put(ECLIPSE, ECLIPSE);
        testObj.put(AUDACITY, AUDACITY);
        testObj.put(OPENOFFICE, OPENOFFICE);
        testObj.put(VLC, VLC);
        testObj.put(ROKLIB, ROKLIB);
        testObj.put(LEXIS, LEXIS);
    }

    @Test
    public void testJDKSortedMapForComparison() {
        TreeMap<CharSequence, String> map = new TreeMap<CharSequence, String>();
        map.putAll(testObj);
        SortedMap<CharSequence, String> headMap = map.headMap(LINUX);
        headMap.put("AAAA", "");
        assertEquals("AAAA", headMap.firstKey());
        SortedMap<CharSequence, String> tailMap = map.tailMap(THUNDERBIRD);
        tailMap.put("zzzzz", "");
        assertNull(tailMap.remove(JOOMLA));
        assertTrue(map.containsKey(JOOMLA));
        assertNotNull(tailMap.remove(XMMS));
        SortedMap<CharSequence, String> subMap = map.subMap(APACHE, AUDACITY);
        assertEquals(1, subMap.size());
        assertEquals(APACHE, subMap.firstKey());
        subMap.remove(APACHE);
        assertEquals(0, subMap.size());
        subMap = map.subMap(APACHE, GIMP);
        subMap.remove(APACHE);
        subMap.remove(AUDACITY);
        subMap.put(APACHE, "");
        subMap = map.subMap(GIMP, GIMP);
        assertEquals(0, subMap.size());
        subMap = map.headMap(KDE);
        assertFalse(subMap.containsKey(KDE));
        subMap = subMap.headMap(KDE);

        map.remove(GIMP);
        headMap = map.headMap("Dxxx");
        headMap = map.headMap(GIMP);
        headMap = headMap.headMap(GIMP);
        assertFalse(map.containsKey(GIMP));

    }

    @Test
    public void testFirstKey() {
        assertEquals(APACHE, testObj.firstKey());
    }

    @Test
    public void testLastKey() {
        assertEquals(XMMS, testObj.lastKey());
    }

    @Test
    public void testComparator() {
        assertNull(testObj.comparator());
    }

    @Test
    public void testHeadMap() {
        SortedMap<CharSequence, String> headMap = testObj.headMap(EMACS);
        assertEquals(5, headMap.size());
        assertTrue(headMap.containsKey(APACHE));
        assertTrue(headMap.containsKey(AUDACITY));
        assertTrue(headMap.containsKey(CDEX));
        assertTrue(headMap.containsKey(DEBIAN));
        assertTrue(headMap.containsKey(ECLIPSE));
        assertFalse(headMap.containsKey(EMACS));
        assertEquals(APACHE, headMap.firstKey());
        assertEquals(ECLIPSE, headMap.lastKey());
        assertNull(headMap.get(EMACS));
        assertEquals(APACHE, headMap.get(APACHE));
    }

    @Test
    public void testHeadMapWithEmptyStringKeyIsEmpty() {
        testObj.put("", "");
        SortedMap<CharSequence, String> headMap = testObj.headMap("");
        assertFalse(headMap.containsKey(""));
        assertEquals(0, headMap.keySet().size());
        assertFalse(headMap.keySet().iterator().hasNext());
        assertEquals(0, headMap.values().size());
        assertFalse(headMap.values().iterator().hasNext());
        assertNull(headMap.firstKey());
        assertNull(headMap.lastKey());
        assertEquals(0, headMap.size());
        assertFalse(headMap.containsValue(""));
        assertNull(headMap.get(""));
        assertEquals(0, headMap.entrySet().size());
        assertFalse(headMap.entrySet().iterator().hasNext());
        headMap.entrySet().clear();
        assertTrue(testObj.containsKey(""));
        headMap.clear();
        assertTrue(testObj.containsKey(""));
    }

    @Test
    public void testTailMapContainsWithEmptyStringKey() {
        testObj.put("", "");
        SortedMap<CharSequence, String> tailMap = testObj.tailMap("");
        assertTrue(tailMap.containsKey(""));
        tailMap = testObj.tailMap("A");
        assertFalse(tailMap.containsKey(""));
        assertEquals(testObj.size() - 1, tailMap.size());
    }

    @Test
    public void testTailMap() {
        SortedMap<CharSequence, String> tailMap = testObj.tailMap(ROKLIB);
        assertTrue(tailMap.containsKey(ROKLIB));
        assertTrue(tailMap.containsKey(THUNDERBIRD));
        assertTrue(tailMap.containsKey(UBUNTU));
        assertTrue(tailMap.containsKey(VLC));
        assertTrue(tailMap.containsKey(XMMS));
        assertEquals(ROKLIB, tailMap.firstKey());
        assertEquals(XMMS, tailMap.lastKey());
        assertEquals(5, tailMap.size());
        assertNull(tailMap.get(OPENOFFICE));
        assertEquals(ROKLIB, tailMap.get(ROKLIB));
    }

    @Test
    public void testSubMap() {
        SortedMap<CharSequence, String> subMap = testObj.subMap("Gabc", "Kubuntu");
        assertEquals(5, subMap.size());
        assertTrue(subMap.containsKey(GIMP));
        assertTrue(subMap.containsKey(GNOME));
        assertTrue(subMap.containsKey(INKSCAPE));
        assertTrue(subMap.containsKey(JOOMLA));
        assertTrue(subMap.containsKey(KDE));
        assertFalse(subMap.containsKey("Kubuntu"));
        assertEquals(KDE, subMap.lastKey());
        assertEquals(GIMP, subMap.firstKey());
        assertNull(subMap.get(LEXIS));
        assertNull(subMap.get(FIREFOX));
        assertEquals(GIMP, subMap.get(GIMP));
    }

    @Test
    public void testInsertIntoHeadMap() {
        SortedMap<CharSequence, String> headMap = testObj.headMap(EMACS);
        String newKey = "Aaaaa";
        headMap.put(newKey, "");
        assertTrue(testObj.containsKey(newKey));
        assertTrue(headMap.containsKey(newKey));
        assertEquals(6, headMap.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertIntoHeadMap_Fail() {
        testObj.headMap(EMACS).put("xxx", "xxx");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHeadMap_Fail() {
        testObj.headMap(EMACS).headMap(LEXIS);
    }

    @Test
    public void testInsertIntoSubMap() {
        SortedMap<CharSequence, String> subMap = testObj.subMap(GIMP, KDE);
        String newKey = "Hurd";
        subMap.put(newKey, "");
        assertEquals(5, subMap.size());
        assertTrue(subMap.containsKey(newKey));
        assertTrue(testObj.containsKey(newKey));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertIntoSubMap_Fail1() {
        testObj.subMap(GIMP, KDE).put("Apppp", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertIntoSubMap_Fail2() {
        testObj.subMap(GIMP, KDE).put("Xxxx", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubMap_Fail1() {
        testObj.subMap(GIMP, KDE).subMap(AUDACITY, JOOMLA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubMap_Fail2() {
        testObj.subMap(GIMP, KDE).subMap(JOOMLA, XMMS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubMap_Fail3() {
        testObj.subMap(GIMP, APACHE);
    }

    @Test
    public void testInsertIntoTailMap() {
        SortedMap<CharSequence, String> tailMap = testObj.tailMap(ROKLIB);
        assertEquals(5, tailMap.size());
        String newKey = "Zzzz";
        tailMap.put(newKey, "");
        assertTrue(tailMap.containsKey(newKey));
        assertTrue(testObj.containsKey(newKey));
        assertEquals(6, tailMap.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertIntoTailMap_Fail() {
        testObj.tailMap(ROKLIB).put("AAA", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTailMap_Fail() {
        testObj.tailMap(ROKLIB).tailMap("AAA");
    }
}
