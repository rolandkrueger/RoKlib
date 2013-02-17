/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 19.01.2010
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
package org.roklib.collections.tstmap.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.junit.Before;
import org.junit.Test;
import org.roklib.collections.TernarySearchTreeMap;

public class TSTSubMapTest
{
  private final static String          A = "aaa";
  private final static String          B = "bbb";
  private final static String          C = "ccc";
  private final static String          D = "ddd";
  private final static String          E = "eee";
  private final static String          F = "fff";
  private final static String          G = "ggg";

  private TernarySearchTreeMap<String> mTestObj;

  // TODO: test removing boundary values
  // TODO: test submaps of submaps

  @Before
  public void setUp ()
  {
    mTestObj = new TernarySearchTreeMap<String> ();
    mTestObj.put ("", "EMPTY");
    mTestObj.put (D, D.toUpperCase ());
    mTestObj.put (F, F.toUpperCase ());
    mTestObj.put (C, C.toUpperCase ());
    mTestObj.put (B, B.toUpperCase ());
    mTestObj.put (A, A.toUpperCase ());
    mTestObj.put (G, G.toUpperCase ());
    mTestObj.put (E, E.toUpperCase ());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSubMapInvalidArguments ()
  {
    mTestObj.subMap (F, A);
  }

  @Test
  public void testSubMapGetComparator ()
  {
    assertEquals (mTestObj.comparator (), mTestObj.subMap (A, B).comparator ());
  }

  @Test
  public void testFirstKeyFromSubMap_EndpointsInMap ()
  {
    // low endpoint is contained in submap
    assertEquals ("aaa", mTestObj.subMap (A, D).firstKey ());
  }

  @Test
  public void testLastKeyFromSubMap_EndpointsInMap ()
  {
    // high endpoint is not contained in the submap
    assertEquals ("ccc", mTestObj.subMap (A, D).lastKey ());
  }

  @Test
  public void testFirstKeyFromSubMap_EndpointsNotInMap ()
  {
    // low endpoint is contained in submap
    assertEquals ("aaa", mTestObj.subMap ("a", "faaaa").firstKey ());
  }

  @Test
  public void testLastKeyFromSubMap_EndpointsNotInMap ()
  {
    // high endpoint is not contained in the submap
    assertEquals ("eee", mTestObj.subMap ("a", "faaaa").lastKey ());
  }

  @Test
  public void testFirstKeyWithEmptyMap ()
  {
    mTestObj.clear ();
    assertNull (mTestObj.subMap ("ab", "xy").firstKey ());
  }

  @Test
  public void testLastKeyWithEmptyMap ()
  {
    mTestObj.clear ();
    assertNull (mTestObj.subMap ("ab", "xy").lastKey ());
  }

  @Test
  public void testFirstKeyWithEmptySubMap ()
  {
    assertNull (mTestObj.subMap ("vvv", "xxx").firstKey ());
  }

  @Test
  public void testLastKeyWithEmptySubMap ()
  {
    assertNull (mTestObj.subMap ("vvv", "xxx").lastKey ());
  }

  @Test
  public void testSize_EndpointsInMap ()
  {
    assertEquals (3, mTestObj.subMap (A, D).size ());
  }

  @Test
  public void testSize_EndpointsNotInMap ()
  {
    assertEquals (4, mTestObj.subMap ("abbb", "faaaa").size ());
  }

  @Test
  public void testSizeForEmptyBaseMap ()
  {
    SortedMap<CharSequence, String> subMap = mTestObj.subMap (C, "dumbo");
    mTestObj.clear ();
    assertEquals (0, subMap.size ());
  }

  @Test
  public void testValues_EndpointsInMap ()
  {
    List<String> valueList = new ArrayList<String> (mTestObj.subMap (B, D).values ());
    Collection<String> expectedValues = Arrays.asList ("BBB", "CCC");
    assertEquals (expectedValues, valueList);
  }

  @Test
  public void testValues_EndpointsNotInMap ()
  {
    List<String> valueList = new ArrayList<String> (mTestObj.subMap ("abbb", "faaaa").values ());
    Collection<String> expectedValues = Arrays.asList ("BBB", "CCC", "DDD", "EEE");
    assertEquals (expectedValues, valueList);
    assertEquals (mTestObj.subMap ("ab", "fa"), mTestObj.subMap (B, F));
  }

  @Test (expected = UnsupportedOperationException.class)
  public void testValuesAddNotSupported ()
  {
    mTestObj.subMap ("ab", F).values ().add (null);
  }

  @Test (expected = UnsupportedOperationException.class)
  public void testValuesAddAllNotSupported ()
  {
    mTestObj.subMap ("ab", F).values ().addAll (null);
  }

  @Test
  public void testValuesRemove ()
  {
    Collection<String> values = mTestObj.subMap (C, E).values ();
    assertFalse (values.remove (A.toUpperCase ()));
    assertFalse (values.remove (F.toUpperCase ()));
    assertTrue (values.remove (D.toUpperCase ()));
    assertFalse (mTestObj.containsValue (D.toUpperCase ()));
  }

  @Test
  public void testValuesClear ()
  {
    Collection<String> values = mTestObj.subMap (C, E).values ();
    values.clear ();
    assertEquals (0, values.size ());
    assertEquals (6, mTestObj.size ());
    assertFalse (mTestObj.containsKey (C));
    assertFalse (mTestObj.containsKey (D));
    assertTrue (mTestObj.containsKey (E));
  }

  @Test
  public void testValuesContains ()
  {
    Collection<String> values = mTestObj.subMap (C, E).values ();
    assertFalse (values.contains (B.toUpperCase ()));
    assertTrue (values.contains (C.toUpperCase ()));
    assertTrue (values.contains (D.toUpperCase ()));
    assertFalse (values.contains (E.toUpperCase ()));
  }

  @Test
  public void testSubMapWithEqualBoundaries ()
  {
    assertTrue (mTestObj.subMap (D, D).isEmpty ());
  }

  @Test
  public void testClearSubMapChangesMap ()
  {
    Map<CharSequence, String> expected = new HashMap<CharSequence, String> ();
    expected.putAll (mTestObj);
    expected.remove (A);
    expected.remove (B);
    mTestObj.subMap (A, C).clear ();
    assertEquals (expected, mTestObj);
    assertTrue (mTestObj.subMap (A, C).isEmpty ());
  }

  @Test
  public void testClearSubMapWithEqualBoundariesDoesntChangeMap ()
  {
    Map<CharSequence, String> expected = new HashMap<CharSequence, String> ();
    expected.putAll (mTestObj);
    mTestObj.subMap (D, D).clear ();
    assertEquals (expected, mTestObj);
  }

  @Test
  public void testClearDisjunctSubMapDoesntChangeMap ()
  {
    Map<CharSequence, String> expected = new HashMap<CharSequence, String> ();
    expected.putAll (mTestObj);
    mTestObj.subMap ("xxx", "zzz").clear ();
    assertEquals (expected, mTestObj);
  }

  @Test (expected = NullPointerException.class)
  public void testSubMapWithNullFromKey ()
  {
    mTestObj.subMap (null, C);
  }

  @Test (expected = NullPointerException.class)
  public void testSubMapWithNullToKey ()
  {
    mTestObj.subMap (C, null);
  }

  @Test
  public void testContainsKeyWithEmptyStringKey ()
  {
    SortedMap<CharSequence, String> subMap = mTestObj.subMap ("", "dumbo");
    assertTrue (subMap.containsKey (""));
  }

  @Test
  public void testContainsKey ()
  {
    SortedMap<CharSequence, String> subMap = mTestObj.subMap (C, "dumbo");
    assertFalse (subMap.containsKey (""));
    assertTrue (subMap.containsKey (C));
    assertTrue (subMap.containsKey (D));
    assertFalse (subMap.containsKey (E));
    assertFalse (subMap.containsKey (A));
    assertFalse (subMap.containsKey (B));
    assertFalse (subMap.containsKey ("dumbo"));

    // add new key to base map, which falls into the range of the submap
    mTestObj.put ("cd", "CD");
    assertTrue (subMap.containsKey ("cd"));
  }

  @Test
  public void testContainsValue ()
  {
    SortedMap<CharSequence, String> subMap = mTestObj.subMap (C, "dumbo");
    assertTrue (subMap.containsValue (C.toUpperCase ()));
    assertTrue (subMap.containsValue (D.toUpperCase ()));
    assertFalse (subMap.containsValue (E.toUpperCase ()));
    assertFalse (subMap.containsValue (A.toUpperCase ()));
    assertFalse (subMap.containsValue (B.toUpperCase ()));

    // add new key to base map, which falls into the range of the submap
    mTestObj.put ("cd", "CD");
    assertTrue (subMap.containsValue ("CD"));
  }

  @Test
  public void testGet ()
  {
    SortedMap<CharSequence, String> subMap = mTestObj.subMap (C, "dumbo");
    assertEquals (C.toUpperCase (), subMap.get (C));
    assertEquals (D.toUpperCase (), subMap.get (D));
    assertNull (subMap.get (A));
    assertNull (subMap.get (E));
  }

  @Test
  public void testIsEmpty ()
  {
    SortedMap<CharSequence, String> subMap = mTestObj.subMap (C, "dumbo");
    assertFalse (subMap.isEmpty ());
    subMap.clear ();
    assertTrue (subMap.isEmpty ());
    assertTrue (mTestObj.subMap ("xxx", "yyy").isEmpty ());
  }

  @Test
  public void testPut ()
  {
    SortedMap<CharSequence, String> subMap = mTestObj.subMap (C, G);
    subMap.put ("fargo", "jesses");
    assertTrue (mTestObj.containsKey ("fargo"));
    assertTrue (mTestObj.containsValue ("jesses"));
    assertTrue (subMap.containsKey ("fargo"));
    assertTrue (subMap.containsValue ("jesses"));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testPutOutsideRange ()
  {
    SortedMap<CharSequence, String> subMap = mTestObj.subMap (C, G);
    subMap.put ("illegal", "argument");
  }

  @Test
  public void testPutAll ()
  {
    SortedMap<CharSequence, String> subMap = mTestObj.subMap (C, G);
    Map<CharSequence, String> map = new HashMap<CharSequence, String> ();
    map.put ("dumbo", "elephant");
    map.put ("elephant", "man");
    subMap.putAll (map);
    assertTrue (mTestObj.containsKey ("dumbo"));
    assertTrue (mTestObj.containsKey ("elephant"));
    assertTrue (mTestObj.containsValue ("elephant"));
    assertTrue (mTestObj.containsValue ("man"));
  }

  @Test
  public void testRemove ()
  {
    SortedMap<CharSequence, String> subMap = mTestObj.subMap (C, F);
    String value = subMap.remove (E);
    assertEquals (E.toUpperCase (), value);
    assertFalse (subMap.containsKey (E));
    assertFalse (mTestObj.containsKey (E));

    // remove keys from outside the boundaries of the submap
    assertNull (subMap.remove (A));
    assertTrue (mTestObj.containsKey (A));
    assertNull (subMap.remove (F));
    assertTrue (mTestObj.containsKey (F));
  }

  @Test (expected = NullPointerException.class)
  public void testRemoveNullKey ()
  {
    mTestObj.subMap (A, C).remove (null);
  }

  @Test
  public void testKeySet ()
  {
    SortedMap<CharSequence, String> subMap = mTestObj.subMap ("baaa", "dzzz");
    Set<CharSequence> keySet = subMap.keySet ();
    assertEquals (subMap.size (), keySet.size ());
    Set<CharSequence> expectedSet = new HashSet<CharSequence> (Arrays.asList (B, C, D));
    assertEquals (expectedSet, keySet);
  }

  @Test (expected = UnsupportedOperationException.class)
  public void testKeySetAdd ()
  {
    mTestObj.subMap ("baaa", "dzzz").keySet ().add (null);
  }

  @Test (expected = UnsupportedOperationException.class)
  public void testKeySetAddAll ()
  {
    mTestObj.subMap ("baaa", "dzzz").keySet ().addAll (null);
  }

  @Test
  public void testKeySetClear ()
  {
    Set<CharSequence> keySet = mTestObj.subMap ("baaa", "dzzz").keySet ();
    keySet.clear ();
    assertTrue (keySet.isEmpty ());
    assertTrue (mTestObj.containsKey (A));
    assertFalse (mTestObj.containsKey (B));
    assertFalse (mTestObj.containsKey (C));
    assertFalse (mTestObj.containsKey (D));
    assertTrue (mTestObj.containsKey (E));
    assertTrue (mTestObj.containsKey (F));
    assertTrue (mTestObj.containsKey (G));
  }

  @Test
  public void testKeySetContains ()
  {
    Set<CharSequence> keySet = mTestObj.subMap ("baaa", "dzzz").keySet ();
    assertFalse (keySet.contains (""));
    assertFalse (keySet.contains (A));
    assertTrue (keySet.contains (B));
    assertTrue (keySet.contains (C));
    assertTrue (keySet.contains (D));
    assertFalse (keySet.contains (E));
    assertFalse (keySet.contains (F));
    assertFalse (keySet.contains (G));
  }

  @Test
  public void testKeySetContainsAll ()
  {
    Set<CharSequence> keySet = mTestObj.subMap ("baaa", "dzzz").keySet ();
    assertTrue (keySet.containsAll (Arrays.asList (B, C, D)));
    assertFalse (keySet.containsAll (Arrays.asList (A, E, F, G)));
  }

  @Test
  public void testKeySetIsEmpty ()
  {
    assertFalse (mTestObj.subMap ("baaa", "dzzz").keySet ().isEmpty ());
    assertTrue (mTestObj.subMap ("xxx", "zzz").keySet ().isEmpty ());
  }

  @Test
  public void testKeySetIterator ()
  {
    Iterator<CharSequence> it = mTestObj.subMap ("baaa", "dzzz").keySet ().iterator ();
    assertTrue (it.hasNext ());
    assertEquals (B, it.next ());
    assertTrue (it.hasNext ());
    assertEquals (C, it.next ());
    assertTrue (it.hasNext ());
    assertEquals (D, it.next ());
    assertFalse (it.hasNext ());
  }

  @Test
  public void testKeySetRemove ()
  {
    Set<CharSequence> keySet = mTestObj.subMap ("baaa", "dzzz").keySet ();
    assertTrue (mTestObj.containsKey (C));
    assertTrue (keySet.remove (C));
    assertFalse (mTestObj.containsKey (C));
    assertFalse (keySet.remove (G));
    assertTrue (mTestObj.containsKey (G));
  }

  @Test
  public void testKeySetRemoveAll ()
  {
    Set<CharSequence> keySet = mTestObj.subMap ("baaa", "dzzz").keySet ();
    // remove keys A, B, and C. Only B and C are expected to be contained by the key set
    assertTrue (keySet.removeAll (Arrays.asList (A, B, C)));
    assertFalse (keySet.removeAll (Arrays.asList (A, B, C)));
    assertFalse (keySet.removeAll (Arrays.asList (G, F)));
    assertEquals (1, keySet.size ());
  }

  @Test
  public void testKeySetRetainAll ()
  {
    Set<CharSequence> keySet = mTestObj.subMap ("baaa", "dzzz").keySet ();
    assertEquals (3, keySet.size ());
    assertTrue (keySet.retainAll (Arrays.asList (A, B, C)));
    assertEquals (2, keySet.size ());
    assertFalse (keySet.contains (A));
    assertTrue (keySet.contains (B));
    assertTrue (keySet.contains (C));
    assertFalse (keySet.contains (D));

    keySet = mTestObj.subMap ("baaa", "dzzz").keySet ();
    assertFalse (keySet.retainAll (Arrays.asList (B, C, D)));
  }

  @Test
  public void testEntrySet ()
  {
    Set<Map.Entry<CharSequence, String>> entrySet = mTestObj.subMap ("baaa", "dzzz").entrySet ();
    assertEquals (3, entrySet.size ());
    Iterator<Map.Entry<CharSequence, String>> it = entrySet.iterator ();
    assertTrue (it.hasNext ());
    Map.Entry<CharSequence, String> entry = it.next ();
    assertEquals (entry.getKey (), B);
    assertEquals (entry.getValue (), B.toUpperCase ());
    assertEquals (B.toUpperCase (), entry.setValue ("new value"));
    assertTrue (it.hasNext ());
    entry = it.next ();
    assertEquals (entry.getKey (), C);
    assertEquals (entry.getValue (), C.toUpperCase ());
    assertEquals (C.toUpperCase (), entry.setValue ("new value"));
    assertTrue (it.hasNext ());
    entry = it.next ();
    assertEquals (entry.getKey (), D);
    assertEquals (entry.getValue (), D.toUpperCase ());
    assertEquals (D.toUpperCase (), entry.setValue ("new value"));
    assertFalse (it.hasNext ());

    assertEquals ("new value", mTestObj.get (B));
    assertEquals ("new value", mTestObj.get (C));
    assertEquals ("new value", mTestObj.get (D));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetEntryValueNull ()
  {
    Set<Map.Entry<CharSequence, String>> entrySet = mTestObj.subMap ("baaa", "dzzz").entrySet ();
    Iterator<Map.Entry<CharSequence, String>> it = entrySet.iterator ();
    Map.Entry<CharSequence, String> entry = it.next ();
    entry.setValue (null);
  }

  @Test (expected = UnsupportedOperationException.class)
  public void testEntrySetAdd ()
  {
    mTestObj.subMap ("baaa", "dzzz").entrySet ().add (null);
  }

  @Test (expected = UnsupportedOperationException.class)
  public void testEntrySetAddAll ()
  {
    mTestObj.subMap ("baaa", "dzzz").entrySet ().addAll (null);
  }

  @Test
  public void testEntrySetClear ()
  {
    Set<Map.Entry<CharSequence, String>> entrySet = mTestObj.subMap ("baaa", "dzzz").entrySet ();
    assertEquals (3, entrySet.size ());
    entrySet.clear ();
    assertEquals (5, mTestObj.size ());
    assertFalse (mTestObj.containsKey (B));
    assertFalse (mTestObj.containsKey (C));
    assertFalse (mTestObj.containsKey (D));
  }

  @Test
  public void testEntrySetContains ()
  {
    Set<Map.Entry<CharSequence, String>> entrySet = mTestObj.subMap ("baaa", "dzzz").entrySet ();
    assertEquals (3, entrySet.size ());
    TernarySearchTreeMap<String> expected = new TernarySearchTreeMap<String> ();
    expected.put (B, B.toUpperCase ());
    expected.put (C, C.toUpperCase ());
    expected.put (D, D.toUpperCase ());
    TernarySearchTreeMap<String> notExpected = new TernarySearchTreeMap<String> ();
    notExpected.put (A, A.toUpperCase ());
    notExpected.put (E, E.toUpperCase ());
    notExpected.put ("baaaa", "not in map");
    notExpected.put (F, F.toUpperCase ());
    notExpected.put (G, G.toUpperCase ());
    for (Iterator<Map.Entry<CharSequence, String>> it = expected.entrySet ().iterator (); it.hasNext ();)
    {
      assertTrue (entrySet.contains (it.next ()));
    }
    for (Iterator<Map.Entry<CharSequence, String>> it = notExpected.entrySet ().iterator (); it.hasNext ();)
    {
      assertFalse (entrySet.contains (it.next ()));
    }
    assertFalse (entrySet.contains (mTestObj));
  }

  @Test
  public void testEntrySetContainsAll ()
  {
    Set<Map.Entry<CharSequence, String>> entrySet = mTestObj.subMap ("baaa", "dzzz").entrySet ();
    TernarySearchTreeMap<String> expected = new TernarySearchTreeMap<String> ();
    expected.put (B, B.toUpperCase ());
    expected.put (C, C.toUpperCase ());
    expected.put (D, D.toUpperCase ());
    TernarySearchTreeMap<String> notExpected = new TernarySearchTreeMap<String> ();
    notExpected.put (A, A.toUpperCase ());
    notExpected.put (E, E.toUpperCase ());
    notExpected.put ("baaaa", "not in map");
    notExpected.put (F, F.toUpperCase ());
    notExpected.put (G, G.toUpperCase ());
    assertTrue (entrySet.containsAll (expected.entrySet ()));
    assertFalse (entrySet.containsAll (notExpected.entrySet ()));
  }

  @Test
  public void testEntrySetIsEmpty ()
  {
    assertFalse (mTestObj.subMap ("baaa", "dzzz").entrySet ().isEmpty ());
    assertTrue (mTestObj.subMap ("xxx", "zzz").entrySet ().isEmpty ());
  }

  @Test
  public void testEntrySetRemove ()
  {
    Set<Map.Entry<CharSequence, String>> entrySet = mTestObj.subMap ("baaa", D).entrySet ();
    assertFalse (entrySet.remove (mTestObj));
    assertFalse (entrySet.remove (mTestObj.entrySet ().iterator ().next ()));
    TernarySearchTreeMap<String> map = new TernarySearchTreeMap<String> ();
    map.put (B, B.toUpperCase ());
    Map.Entry<CharSequence, String> entry = map.entrySet ().iterator ().next ();
    assertTrue (entrySet.remove (entry));
    assertFalse (mTestObj.containsKey (B));
    assertFalse (entrySet.contains (entry));

    // removing the upper bound of the entry set will not succeed
    map.clear ();
    map.put (D, D.toUpperCase ());
    entry = map.entrySet ().iterator ().next ();
    assertFalse (entrySet.remove (entry));

    map.clear ();
    map.put ("caaa", "caaa");
    entry = map.entrySet ().iterator ().next ();
    assertFalse (entrySet.remove (entry));
  }

  @Test
  public void testEntrySetRemoveAll ()
  {
    Set<Map.Entry<CharSequence, String>> entrySet = mTestObj.subMap ("baaa", D).entrySet ();
    TernarySearchTreeMap<String> map = new TernarySearchTreeMap<String> ();
    map.put (B, B.toUpperCase ());
    map.put ("not in map", "");
    map.put (C, C.toUpperCase ());
    assertTrue (entrySet.removeAll (map.entrySet ()));
    assertFalse (mTestObj.containsKey (B));
    assertFalse (mTestObj.containsKey (C));

    map.clear ();
    map.put (D, D.toUpperCase ());
    map.put (E, E.toUpperCase ());
    assertFalse (entrySet.removeAll (map.entrySet ()));
  }

  @Test
  public void testEntrySetRetainAll ()
  {
    Set<Map.Entry<CharSequence, String>> entrySet = mTestObj.subMap ("baaa", F).entrySet ();
    TernarySearchTreeMap<String> map = new TernarySearchTreeMap<String> ();
    map.put (B, B.toUpperCase ());
    map.put ("not in map", "");
    map.put (C, C.toUpperCase ());

    assertTrue (entrySet.retainAll (map.entrySet ()));

    map.put (D, D.toUpperCase ());
    map.put (E, E.toUpperCase ());
    assertFalse (entrySet.retainAll (map.entrySet ()));
  }

  @Test
  public void testEntrySetEquals ()
  {
    Set<Map.Entry<CharSequence, String>> entrySet = mTestObj.subMap ("baaa", E).entrySet ();
    Map<CharSequence, String> map = new HashMap<CharSequence, String> ();
    map.put (B, B.toUpperCase ());
    map.put (C, C.toUpperCase ());
    map.put (D, D.toUpperCase ());

    assertTrue (entrySet.equals (entrySet));
    assertFalse (entrySet.equals (null));
    assertTrue (entrySet.equals (map.entrySet ()));
    assertTrue (map.entrySet ().equals (entrySet));

    map.put ("x", "y");
    assertFalse (entrySet.equals (map.entrySet ()));
    assertFalse (map.entrySet ().equals (entrySet));
  }

  @Test
  public void testEntrySetHashCode ()
  {
    Set<Map.Entry<CharSequence, String>> entrySet = mTestObj.subMap ("baaa", E).entrySet ();
    Map<CharSequence, String> map = new HashMap<CharSequence, String> ();
    map.put (B, B.toUpperCase ());
    map.put (C, C.toUpperCase ());
    map.put (D, D.toUpperCase ());

    assertEquals (entrySet.hashCode (), map.entrySet ().hashCode ());

    map.put ("x", "y");
    assertFalse (entrySet.hashCode () == map.entrySet ().hashCode ());
  }

  @Test
  public void testHeadMap ()
  {
    SortedMap<CharSequence, String> headMap = mTestObj.subMap (B, "dumbo").headMap (D);
    assertEquals (2, headMap.size ());
    assertTrue (headMap.containsKey (B));
    assertTrue (headMap.containsKey (C));
    assertFalse (headMap.containsKey (D));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testHeadMapFailsLowerBound ()
  {
    mTestObj.subMap (B, "dumbo").headMap (A);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testHeadMapFailsUpperBound ()
  {
    mTestObj.subMap (B, "dumbo").headMap (E);
  }

  @Test
  public void testAddToHeadMap ()
  {
    SortedMap<CharSequence, String> headMap = mTestObj.subMap (B, "dumbo").headMap (D);
    headMap.put ("bcd", "bcd");
    assertTrue (headMap.containsKey ("bcd"));
    assertTrue (mTestObj.containsKey ("bcd"));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testAddToHeadMapFails1 ()
  {
    SortedMap<CharSequence, String> headMap = mTestObj.subMap (B, "dumbo").headMap (D);
    headMap.put ("a", "a");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testAddToHeadMapFails2 ()
  {
    SortedMap<CharSequence, String> headMap = mTestObj.subMap (B, "dumbo").headMap (D);
    headMap.put ("dzz", "dzz");
  }

  @Test
  public void testTailMap ()
  {
    SortedMap<CharSequence, String> tailMap = mTestObj.subMap (B, "dumbo").tailMap (C);
    assertEquals (2, tailMap.size ());
    assertFalse (tailMap.containsKey (B));
    assertTrue (tailMap.containsKey (C));
    assertTrue (tailMap.containsKey (D));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testTailMapFailsLowerBound ()
  {
    mTestObj.subMap (B, "dumbo").tailMap (A);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testTailMapFailsUpperBound ()
  {
    mTestObj.subMap (B, "dumbo").tailMap (E);
  }

  @Test
  public void testAddToTailMap ()
  {
    SortedMap<CharSequence, String> tailMap = mTestObj.subMap (B, "dumbo").tailMap (C);
    tailMap.put ("ccd", "ccd");
    assertTrue (tailMap.containsKey ("ccd"));
    assertTrue (mTestObj.containsKey ("ccd"));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testAddToTailMapFails1 ()
  {
    mTestObj.subMap (B, "dumbo").tailMap (C).put ("baa", "b");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testAddToTailMapFails2 ()
  {
    mTestObj.subMap (B, "dumbo").tailMap (C).put ("dzzz", "z");
  }

  @Test
  public void testSubMapOfSubmap ()
  {
    SortedMap<CharSequence, String> subMap = mTestObj.subMap (B, "dumbo").subMap (C, D);
    assertEquals (1, subMap.size ());
    assertTrue (subMap.containsKey (C));
    assertFalse (subMap.containsKey (D));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSubMapOfSubmapFail1 ()
  {
    mTestObj.subMap (B, "dumbo").subMap (A, D);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSubMapOfSubmapFail2 ()
  {
    mTestObj.subMap (B, "dumbo").subMap (C, F);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSubMapOfSubmapFail3 ()
  {
    mTestObj.subMap (B, "dumbo").subMap (A, F);
  }

}
