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
package info.rolandkrueger.roklib.util.tstmap.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import info.rolandkrueger.roklib.util.RandomStringIDGenerator;
import info.rolandkrueger.roklib.util.TernarySearchTreeMap;
import info.rolandkrueger.roklib.util.tstmap.test.MapData.TestDataFixture;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

public class TernarySearchTreeMapTest
{
  private static final String NOT_IN_MAP = "not in map";
  private static final String VALUE1     = "another value";
  private static final String VALUE2     = "";
  private static final String VALUE3     = "last value";
  private TernarySearchTreeMap<String> mTestObj, copy;

  @Before
  public void setUp ()
  {
    mTestObj = new TernarySearchTreeMap<String> ();
    copy = new TernarySearchTreeMap<String> ();
  }

  @Test
  public void testEquals ()
  {
    String key = "some key";
    String value = "some value";
    mTestObj.put (key, value);
    mTestObj.put (VALUE3, VALUE1);
    mTestObj.put (VALUE2, VALUE2);
    mTestObj.put (VALUE1, VALUE3);
    TernarySearchTreeMap<String> other = new TernarySearchTreeMap<String> ();
    other.put (key, value);
    other.put (VALUE1, VALUE3);
    other.put (VALUE2, VALUE2);
    other.put (VALUE3, VALUE1);
    assertTrue (mTestObj.equals (mTestObj));
    assertFalse (mTestObj.equals (null));
    assertTrue (mTestObj.equals (other));
    assertTrue (other.equals (mTestObj));
  }

  @Test
  public void testNotEqual ()
  {
    String key = "some key";
    String value = "some value";
    mTestObj.put (key, value);
    mTestObj.put (VALUE3, VALUE1);
    mTestObj.put (VALUE2, VALUE2);
    mTestObj.put (VALUE1, VALUE3);
    TernarySearchTreeMap<String> other = new TernarySearchTreeMap<String> ();
    other.put (key, value);
    other.put (VALUE1, VALUE3);
    other.put (VALUE2, VALUE2);
    assertFalse (mTestObj.equals (other));
    assertFalse (other.equals (mTestObj));
  }

  @Test
  public void testHashCode ()
  {
    String key = "some key";
    String value = "some value";
    mTestObj.put (key, value);
    mTestObj.put (VALUE3, VALUE1);
    mTestObj.put (VALUE2, VALUE2);
    mTestObj.put (VALUE1, VALUE3);
    TernarySearchTreeMap<String> other = new TernarySearchTreeMap<String> ();
    other.put (key, value);
    other.put (VALUE1, VALUE3);
    other.put (VALUE2, VALUE2);
    other.put (VALUE3, VALUE1);
    assertTrue (mTestObj.hashCode () == other.hashCode ());
    assertTrue (mTestObj.hashCode () == mTestObj.hashCode ());
  }

  @Test
  public void testHashCodeNotEqual ()
  {
    String key = "some key";
    String value = "some value";
    mTestObj.put (key, value);
    mTestObj.put (VALUE3, VALUE1);
    mTestObj.put (VALUE2, VALUE2);
    mTestObj.put (VALUE1, VALUE3);
    TernarySearchTreeMap<String> other = new TernarySearchTreeMap<String> ();
    other.put (key, value);
    other.put (VALUE1, VALUE3);
    other.put (VALUE2, VALUE2);
    assertFalse (mTestObj.hashCode () == other.hashCode ());
  }

  @Test
  public void testConstructorWithMapParameter ()
  {
    Map<CharSequence, String> sourceMap = new HashMap<CharSequence, String> ();
    sourceMap.put (VALUE1, VALUE1);
    sourceMap.put (VALUE2, VALUE2);
    sourceMap.put (VALUE3, VALUE3);
    TernarySearchTreeMap<String> map = new TernarySearchTreeMap<String> (sourceMap);

    assertEquals (3, map.size ());
    assertTrue (map.get (VALUE1).equals (VALUE1));
    assertTrue (map.get (VALUE2).equals (VALUE2));
    assertTrue (map.get (VALUE3).equals (VALUE3));
  }

  @Test
  public void testConstructorWithSortedMapParameter ()
  {
    TreeMap<CharSequence, String> sourceMap = new TreeMap<CharSequence, String> ();
    sourceMap.put (VALUE1, VALUE1);
    sourceMap.put (VALUE2, VALUE2);
    sourceMap.put (VALUE3, VALUE3);
    TernarySearchTreeMap<String> map = new TernarySearchTreeMap<String> (sourceMap);
    assertEquals (3, map.size ());
    assertTrue (map.get (VALUE1).equals (VALUE1));
    assertTrue (map.get (VALUE2).equals (VALUE2));
    assertTrue (map.get (VALUE3).equals (VALUE3));
    assertEquals (sourceMap.comparator (), map.comparator ());
  }

  @Test
  public void testContainsKey ()
  {
    fillMaps ();
    assertTrue (mTestObj.containsKey (VALUE1));
    assertTrue (mTestObj.containsKey (VALUE2));
    assertTrue (mTestObj.containsKey (VALUE3));
    assertFalse (mTestObj.containsKey (NOT_IN_MAP));
  }

  @Test
  public void testContainsValue ()
  {
    fillMaps ();
    assertTrue (mTestObj.containsValue (VALUE1));
    assertTrue (mTestObj.containsValue (VALUE2));
    assertTrue (mTestObj.containsValue (VALUE3));
    assertFalse (mTestObj.containsValue (NOT_IN_MAP));
  }

  @Test
  public void testPut ()
  {
    String o = new String ();
    String formerValue = mTestObj.put ("testKey", o);
    assertEquals (mTestObj.get ("testKey"), o);
    assertNull (formerValue);

    mTestObj.clear ();
    String x = "x";
    mTestObj.put (x, x);
    mTestObj.put ("", "empty");
    assertEquals (x, mTestObj.get (x));
  }

  @Test (expected = NullPointerException.class)
  public void testPutNullKey ()
  {
    mTestObj.put (null, "");
  }

  @Test (expected = NullPointerException.class)
  public void testPutNullValue ()
  {
    mTestObj.put ("", null);
  }

  @Test
  public void testClear ()
  {
    fillMaps ();
    assertEquals (3, mTestObj.size ());
    mTestObj.clear ();
    assertEquals (0, mTestObj.size ());
    assertTrue (mTestObj.isEmpty ());
  }

  @Test
  public void testRemoveWithSuccess ()
  {
    Set<String> testKeySet = new HashSet<String> ();
    Map<String, String> compareMap = new HashMap<String, String> ();
    Random random = new Random (System.currentTimeMillis ());

    String x = "x";
    testKeySet.add (x);
    compareMap.put (x, x);
    mTestObj.put (x, x);

    String key;
    for (int i = 0; i < 200; ++i)
    {
      key = RandomStringIDGenerator.getUniqueID (random.nextInt (20) + 1);
      testKeySet.add (key);
      compareMap.put (key, key);
      mTestObj.put (key, key);
      assertEquals (mTestObj, compareMap);
    }

    for (String removeKey : new HashSet<String> (testKeySet))
    {
      testKeySet.remove (removeKey);
      compareMap.remove (removeKey);
      assertEquals (removeKey, mTestObj.remove (removeKey));
      assertEquals (mTestObj, compareMap);
      assertEquals (testKeySet.size (), mTestObj.size ());
    }

    assertTrue (mTestObj.isEmpty ());
  }

  @Test
  public void testRemoveWithoutSuccess ()
  {
    fillMaps ();
    assertNull (mTestObj.remove (NOT_IN_MAP));
    assertEquals (3, mTestObj.size ());
  }

  @Test
  public void testIsEmpty ()
  {
    fillMaps ();
    mTestObj.clear ();
    assertTrue (mTestObj.isEmpty ());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testEntrySetValueNull ()
  {
    fillMaps ();
    mTestObj.getEntry (VALUE2).setValue (null);
  }

  @Test
  public void testGetEntry ()
  {
    fillMaps ();
    assertEquals (VALUE1, mTestObj.getEntry (VALUE1).getValue ());
    assertEquals (VALUE1, mTestObj.getEntry (VALUE1).getKey ());
    assertEquals (VALUE2, mTestObj.getEntry (VALUE2).getValue ());
    assertEquals (VALUE2, mTestObj.getEntry (VALUE2).getKey ());
    assertEquals (VALUE3, mTestObj.getEntry (VALUE3).getValue ());
    assertEquals (VALUE3, mTestObj.getEntry (VALUE3).getKey ());

    for (Iterator<Map.Entry<CharSequence, String>> it = mTestObj.entrySet ().iterator (); it.hasNext ();)
    {
      Map.Entry<CharSequence, String> entry = it.next ();
      assertEquals (entry, mTestObj.getEntry (entry.getKey ()));
      assertFalse (mTestObj.getEntry (entry.getKey ()).equals (mTestObj));
    }
  }

  @Test
  public void testGetKeyAt ()
  {
    fillMaps ();
    assertTrue (mTestObj.getKeyAt (1).equals (VALUE1)); // VALUE1 is the second value
    // due to the map's ordering
  }

  @Test
  public void testGetValueAt ()
  {
    fillMaps ();
    assertTrue (mTestObj.getValueAt (1).equals (VALUE1)); // VALUE1 is the second
    // value due to the map's
    // ordering
  }

  @Test
  public void testIndexOf ()
  {
    fillMaps ();
    assertEquals (0, mTestObj.indexOf (VALUE2));
    assertEquals (1, mTestObj.indexOf (VALUE1));
    assertEquals (2, mTestObj.indexOf (VALUE3));
    assertEquals (-1, mTestObj.indexOf (NOT_IN_MAP));
  }

  @Test
  public void testPredecessorEntryWithKeyFromMap ()
  {
    fillMapWithFiveLowerCaseEntriesOrderingExpected ();
    Entry<CharSequence, String> predecessor = mTestObj.predecessorEntry ("sverige");
    assertEquals ("suomi", predecessor.getKey ());
    assertEquals ("salmiakki", predecessor.getValue ());
  }

  @Test (expected = ClassCastException.class)
  public void testPredecessorEntryFailsWithIncorrectDataType ()
  {
    mTestObj.predecessorEntry (new Object ());
  }

  @Test
  public void testNoPredecessorEntryWithKeyFromMap ()
  {
    fillMapWithFiveLowerCaseEntriesOrderingExpected ();
    assertNull (mTestObj.predecessorEntry ("deutschland"));
  }

  @Test
  public void testPredecessorEntryWithKeyNotInMap ()
  {
    fillMapWithFiveLowerCaseEntriesOrderingExpected ();
    Entry<CharSequence, String> predecessor = mTestObj.predecessorEntry ("schland");
    assertNotNull (predecessor);
    assertEquals ("france", predecessor.getKey ());
    assertEquals ("fromage", predecessor.getValue ());
  }

  @Test
  public void testPredecessorEntryForFirstKey ()
  {
    fillMapWithFiveLowerCaseEntriesOrderingExpected ();
    assertNull (mTestObj.predecessorEntry ("deutschland"));
  }

  @Test
  public void testSuccessorEntryWithKeyFromMap ()
  {
    fillMapWithFiveLowerCaseEntriesOrderingExpected ();
    Entry<CharSequence, String> successor = mTestObj.successorEntry ("deutschland");
    assertEquals ("england", successor.getKey ());
    assertEquals ("fishnchips", successor.getValue ());
  }

  @Test
  public void testSuccessorEntryWithKeyNotInMap ()
  {
    fillMapWithFiveLowerCaseEntriesOrderingExpected ();
    Entry<CharSequence, String> successor = mTestObj.successorEntry ("schland");
    assertNotNull (successor);
    assertEquals ("suomi", successor.getKey ());
    assertEquals ("salmiakki", successor.getValue ());
  }

  @Test
  public void testSuccessorEntryForLastKey ()
  {
    fillMapWithFiveLowerCaseEntriesOrderingExpected ();
    assertNull (mTestObj.successorEntry ("sverige"));
  }

  @Test (expected = ClassCastException.class)
  public void testSuccessorEntryFailsWithIncorrectDataType ()
  {
    mTestObj.successorEntry (new Object ());
  }

  @Test
  public void testNoSuccessorEntryWithKeyFromMap ()
  {
    fillMapWithFiveLowerCaseEntriesOrderingExpected ();
    assertNull (mTestObj.successorEntry ("sverige"));
  }

  @Test
  public void testPredecessorOfFirstElementIsNull ()
  {
    fillMapWithFiveLowerCaseEntriesOrderingExpected ();
    assertNull (mTestObj.predecessor (mTestObj.firstKey ()));
  }

  @Test
  public void testPredecessorForKeysInSet ()
  {
    fillMapWithFiveLowerCaseEntriesOrderingExpected ();
    assertEquals ("suomi", mTestObj.predecessor (mTestObj.lastKey ()));
    assertEquals ("deutschland", mTestObj.predecessor ("england"));
    assertEquals ("england", mTestObj.predecessor ("espana"));
    assertEquals ("espana", mTestObj.predecessor ("france"));
  }

  @Test
  public void testPredecessorForKeysNotInSet ()
  {
    fillMapWithFiveLowerCaseEntriesOrderingExpected ();
    assertEquals ("france", mTestObj.predecessor ("ghana"));
    assertNull (mTestObj.predecessor ("australia"));
  }

  @Test
  public void testSuccessorOfLastElementIsNull ()
  {
    fillMapWithFiveLowerCaseEntriesOrderingExpected ();
    assertNull (mTestObj.successor (mTestObj.lastKey ()));
  }

  @Test
  public void testSuccessorForKeysInSet ()
  {
    fillMapWithFiveLowerCaseEntriesOrderingExpected ();
    assertEquals ("england", mTestObj.successor ("deutschland"));
    assertEquals ("sverige", mTestObj.successor ("suomi"));
  }

  @Test
  public void testSuccessorForKeysNotInSet ()
  {
    fillMapWithFiveLowerCaseEntriesOrderingExpected ();
    assertNull (mTestObj.successor ("vietnam"));
    assertEquals ("deutschland", mTestObj.successor ("australia"));
  }

  @Test
  public void testGetKeysForPrefix ()
  {
    fillMaps ();
    mTestObj.put ("land", "");
    mTestObj.put ("lasso", "");
    mTestObj.put ("lasting", "");
    Iterator<CharSequence> it = mTestObj.getPrefixMatch ("la").iterator ();
    assertEquals ("land", it.next ());
    assertEquals ("lasso", it.next ());
    assertEquals ("last value", it.next ());
    assertEquals ("lasting", it.next ());

    it = mTestObj.getPrefixMatch ("las").iterator ();
    assertEquals ("lasso", it.next ());
    assertEquals ("last value", it.next ());
    assertEquals ("lasting", it.next ());

    it = mTestObj.getPrefixMatch ("lasso").iterator ();
    assertEquals ("lasso", it.next ());
  }

  @Test (expected = IllegalStateException.class)
  public void testIteratorRemoveFail1 ()
  {
    fillMaps ();
    mTestObj.entrySet ().iterator ().remove ();
  }

  @Test (expected = IllegalStateException.class)
  public void testIteratorRemoveFail2 ()
  {
    fillMaps ();
    Iterator<Map.Entry<CharSequence, String>> it = mTestObj.entrySet ().iterator ();
    it.next ();
    it.remove ();
    it.remove ();
  }

  @Test (expected = NoSuchElementException.class)
  public void testIteratorRemoveFail3 ()
  {
    fillMaps ();
    Iterator<Map.Entry<CharSequence, String>> it = mTestObj.entrySet ().iterator ();
    while (it.hasNext ())
      it.next ();
    it.remove ();
  }

  private void fillMapWithFiveLowerCaseEntriesOrderingExpected ()
  {
    TestDataFixture testData = MapData.getFiveLowerCaseEntriesOrderingExpected ();
    mTestObj.putAll (testData.getData ());
  }

  private void fillMaps ()
  {
    mTestObj.put (VALUE1, VALUE1);
    mTestObj.put (VALUE2, VALUE2);
    mTestObj.put (VALUE3, VALUE3);
    copy.put (VALUE1, VALUE1);
    copy.put (VALUE2, VALUE2);
    copy.put (VALUE3, VALUE3);
  }
}
