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
package info.rolandkrueger.roklib.util.tstmap.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.roklib.collections.TernarySearchTreeMapCaseInsensitive;

public class TSTMapCaseInsensitiveTest
{
  private static final String                         EMPTY = "";
  private static final String                         ABCD  = "ABcd";
  private static final String                         XYZ   = "xyz";
  private static final String                         FOO   = "Foo";
  private static final String                         BAR   = "Bar";
  private static final String                         BAZ   = "BAZ";

  private TernarySearchTreeMapCaseInsensitive<String> mTestObj;

  @Before
  public void setUp ()
  {
    mTestObj = new TernarySearchTreeMapCaseInsensitive<String> ();
    mTestObj.put (EMPTY, EMPTY);
    mTestObj.put (BAR, BAR);
    mTestObj.put (BAZ, BAZ);
    mTestObj.put (ABCD, ABCD);
    mTestObj.put (XYZ, XYZ);
    mTestObj.put (FOO, FOO);
  }

  @Test
  @Ignore
  public void testComparator ()
  {
    fail ();
  }

  @Test
  public void testSubMap ()
  {
    SortedMap<CharSequence, String> subMap = mTestObj.subMap ("AXXX", "GGG");
    assertEquals (3, subMap.size ());
    assertTrue (subMap.containsKey ("baR"));
    assertTrue (subMap.containsKey ("Baz"));
    assertTrue (subMap.containsKey ("fOO"));
    assertTrue (subMap.containsValue (BAR));
    assertTrue (subMap.containsValue (BAZ));
    assertTrue (subMap.containsValue (FOO));

    assertEquals (BAR, subMap.firstKey ());
    assertEquals (FOO, subMap.lastKey ());

    Set<CharSequence> subMapKeySet = subMap.keySet ();
    assertTrue (subMapKeySet.contains ("bAr"));
    assertTrue (subMapKeySet.contains ("fOo"));
    assertTrue (subMapKeySet.contains (BAZ));
    assertFalse (subMapKeySet.contains (XYZ));
  }

  @Test
  public void testSuccessor ()
  {
    assertNull (mTestObj.successorEntry (XYZ));
    assertEquals (ABCD, mTestObj.successorEntry (EMPTY).getKey ());
    assertEquals (FOO, mTestObj.successorEntry ("baZ").getKey ());
    assertEquals (BAR, mTestObj.successorEntry ("abCD").getKey ());
    assertEquals (FOO, mTestObj.successorEntry ("exx").getKey ());
  }

  @Test
  public void testPredecessor ()
  {
    assertNull (mTestObj.predecessorEntry (EMPTY));
    assertEquals (BAZ, mTestObj.predecessorEntry ("bxx").getKey ());
    assertEquals (EMPTY, mTestObj.predecessorEntry (ABCD).getKey ());
    assertEquals (BAZ, mTestObj.predecessorEntry ("fOO").getKey ());
    assertEquals (ABCD, mTestObj.predecessorEntry ("baR").getKey ());
  }

  @Test
  public void testGetValueAt ()
  {
    assertEquals (EMPTY, mTestObj.getValueAt (0));
    assertEquals (ABCD, mTestObj.getValueAt (1));
    assertEquals (BAR, mTestObj.getValueAt (2));
    assertEquals (BAZ, mTestObj.getValueAt (3));
    assertEquals (FOO, mTestObj.getValueAt (4));
    assertEquals (XYZ, mTestObj.getValueAt (5));
  }

  @Test
  public void testGetEntry ()
  {
    Map.Entry<CharSequence, String> entry = mTestObj.getEntry ("abCD");
    assertNotNull (entry);
    assertEquals (ABCD, entry.getKey ());
    assertEquals (ABCD, entry.getValue ());
    entry = mTestObj.getEntry ("bar");
    assertNotNull (entry);
    assertEquals (BAR, entry.getKey ());
    assertEquals (BAR, entry.getValue ());
    entry = mTestObj.getEntry ("bAZ");
    assertNotNull (entry);
    assertEquals (BAZ, entry.getKey ());
    assertEquals (BAZ, entry.getValue ());
    entry = mTestObj.getEntry ("fOO");
    assertNotNull (entry);
    assertEquals (FOO, entry.getKey ());
    assertEquals (FOO, entry.getValue ());
    entry = mTestObj.getEntry ("Xyz");
    assertNotNull (entry);
    assertEquals (XYZ, entry.getKey ());
    assertEquals (XYZ, entry.getValue ());
    assertNull (mTestObj.getEntry ("not in map"));
  }

  @Test
  public void testGetPrefixSubtreeIterator ()
  {
    Iterator<Entry<CharSequence, String>> iterator = mTestObj.getPrefixSubtreeIterator ("bA").iterator ();
    assertTrue (iterator.hasNext ());
    assertEquals (BAR, iterator.next ().getKey ());
    assertTrue (iterator.hasNext ());
    assertEquals (BAZ, iterator.next ().getKey ());
    assertFalse (iterator.hasNext ());
  }

  @Test
  public void testGetKeysForPrefix ()
  {
    List<CharSequence> keys = new ArrayList<CharSequence> ();
    for (CharSequence key : mTestObj.getPrefixMatch ("BA"))
    {
      keys.add (key);
    }
    assertEquals (2, keys.size ());
    assertEquals (BAR, keys.get (0));
    assertEquals (BAZ, keys.get (1));
  }

  @Test
  public void testMatchAlmost ()
  {
    Set<CharSequence> resultSet = mTestObj.matchAlmost ("bam", 1, 0);
    assertEquals (2, resultSet.size ());
    assertTrue (resultSet.contains (BAR));
    assertTrue (resultSet.contains (BAZ));
    assertFalse (resultSet.contains ("bar"));
    assertFalse (resultSet.contains ("baz"));
    assertFalse (resultSet.contains ("bam"));
  }

  @Test
  public void testContainsValue ()
  {
    assertTrue (mTestObj.containsValue (EMPTY));
    assertTrue (mTestObj.containsValue (ABCD));
    assertTrue (mTestObj.containsValue (BAZ));
    assertTrue (mTestObj.containsValue (FOO));
    assertTrue (mTestObj.containsValue (BAR));
    assertTrue (mTestObj.containsValue (XYZ));
  }

  @Test
  public void testValues ()
  {
    Collection<String> values = mTestObj.values ();
    ArrayList<String> valuesList = new ArrayList<String> ();
    valuesList.addAll (values);
    ArrayList<String> expected = new ArrayList<String> ();
    expected.addAll (Arrays.asList (EMPTY, ABCD, BAR, BAZ, FOO, XYZ));
    assertEquals (expected, valuesList);
  }

  @Test
  public void testFirstKey ()
  {
    assertEquals (EMPTY, mTestObj.firstKey ());
    mTestObj.remove (EMPTY);
    assertEquals (ABCD, mTestObj.firstKey ());
    mTestObj.remove (ABCD);
    assertEquals (BAR, mTestObj.firstKey ());
    mTestObj.remove (BAR);
    assertEquals (BAZ, mTestObj.firstKey ());
    mTestObj.remove (BAZ);
    assertEquals (FOO, mTestObj.firstKey ());
    mTestObj.remove (FOO);
    assertEquals (XYZ, mTestObj.firstKey ());
    mTestObj.remove (XYZ);
    assertNull (mTestObj.firstKey ());
  }

  @Test
  public void testLastKey ()
  {
    assertEquals (XYZ, mTestObj.lastKey ());
    mTestObj.remove (XYZ);
    assertEquals (FOO, mTestObj.lastKey ());
    mTestObj.remove (FOO);
    assertEquals (BAZ, mTestObj.lastKey ());
    mTestObj.remove (BAZ);
    assertEquals (BAR, mTestObj.lastKey ());
    mTestObj.remove (BAR);
    assertEquals (ABCD, mTestObj.lastKey ());
    mTestObj.remove (ABCD);
    assertEquals (EMPTY, mTestObj.lastKey ());
    mTestObj.remove (EMPTY);
    assertNull (mTestObj.lastKey ());
  }

  @Test
  public void testKeySet ()
  {
    Set<String> expected = new HashSet<String> ();
    expected.addAll (Arrays.asList (EMPTY, ABCD, XYZ, FOO, BAR, BAZ));
    assertEquals (expected, mTestObj.keySet ());
    Set<CharSequence> keySet = mTestObj.keySet ();
    assertTrue (keySet.contains (EMPTY));
    assertTrue (keySet.contains ("abcd"));
    assertTrue (keySet.contains ("ABCD"));
    assertTrue (keySet.contains ("fOO"));
    assertTrue (keySet.contains ("XyZ"));
    assertTrue (keySet.contains ("bar"));
    assertTrue (keySet.contains ("baZ"));
  }

  @Test
  public void testKeySetRemove ()
  {
    Set<CharSequence> keySet = mTestObj.keySet ();
    assertTrue (keySet.remove ("abCD"));
    assertEquals (5, mTestObj.size ());
    assertFalse (keySet.remove ("not in map"));
  }

  @Test
  public void testGetKeyAt ()
  {
    assertEquals (EMPTY, mTestObj.getKeyAt (0));
    assertEquals (ABCD, mTestObj.getKeyAt (1));
    assertEquals (BAR, mTestObj.getKeyAt (2));
    assertEquals (BAZ, mTestObj.getKeyAt (3));
    assertEquals (FOO, mTestObj.getKeyAt (4));
    assertEquals (XYZ, mTestObj.getKeyAt (5));
  }

  @Test
  public void testIndexOf ()
  {
    assertEquals (0, mTestObj.indexOf (EMPTY));
    assertEquals (1, mTestObj.indexOf (ABCD));
    assertEquals (2, mTestObj.indexOf (BAR));
    assertEquals (3, mTestObj.indexOf (BAZ));
    assertEquals (4, mTestObj.indexOf (FOO));
    assertEquals (5, mTestObj.indexOf (XYZ));
  }

  @Test
  public void testGet ()
  {
    String value1 = mTestObj.get ("Foo");
    String value2 = mTestObj.get ("FOO");
    String value3 = mTestObj.get ("FoO");

    assertSame (value1, value2);
    assertSame (value1, value3);
    assertSame (value2, value3);
    assertSame (mTestObj.get ("BAZ"), mTestObj.get ("baz"));
    assertEquals (ABCD, mTestObj.get (ABCD));
    assertEquals (XYZ, mTestObj.get ("xYz"));
    assertEquals (BAR, mTestObj.get ("bar"));
    assertEquals (BAZ, mTestObj.get ("baz"));
    assertEquals (EMPTY, mTestObj.get (EMPTY));
  }

  @Test
  public void testContainsKey ()
  {
    assertFalse (mTestObj.containsKey ("bzz"));
    assertTrue (mTestObj.containsKey ("abcd"));
    assertTrue (mTestObj.containsKey ("fOO"));
    assertTrue (mTestObj.containsKey ("XyZ"));
    assertTrue (mTestObj.containsKey ("Bar"));
    assertTrue (mTestObj.containsKey ("BAR"));
    assertTrue (mTestObj.containsKey ("bar"));
    assertTrue (mTestObj.containsKey ("Baz"));
    assertTrue (mTestObj.containsKey ("BAZ"));
    assertTrue (mTestObj.containsKey ("baz"));
  }

  @Test
  public void testRemove ()
  {
    assertNull (mTestObj.remove ("bxx"));
    assertEquals (EMPTY, mTestObj.remove (EMPTY));
    assertEquals (ABCD, mTestObj.remove (ABCD));
    assertEquals (XYZ, mTestObj.remove ("XYZ"));
    assertEquals (BAZ, mTestObj.remove ("baz"));
    assertEquals (FOO, mTestObj.remove ("fOO"));
    assertEquals (BAR, mTestObj.remove ("bAr"));
    assertTrue (mTestObj.isEmpty ());

    mTestObj.put ("TESTKEY1", "value1");
    mTestObj.put ("TEsTKEY2", "value2");
    mTestObj.put ("TEsTKeY3", "value3");

    assertEquals ("value3", mTestObj.remove ("testkey3"));
    assertEquals ("value2", mTestObj.remove ("testkey2"));
    assertEquals ("value1", mTestObj.remove ("testkey1"));
    assertTrue (mTestObj.isEmpty ());
  }

  @Test
  public void testPut ()
  {
    mTestObj.clear ();
    assertNull (mTestObj.put ("key", "key"));
    assertEquals ("key", mTestObj.put ("KEY", "key"));
    assertEquals ("key", mTestObj.put ("Key", "value"));
    assertEquals (1, mTestObj.size ());
    assertEquals ("Key", mTestObj.keySet ().iterator ().next ());
    assertEquals ("value", mTestObj.get ("KEY"));
  }

  @Test
  public void testPredecessorOfFirstElementIsNull ()
  {
    assertNull (mTestObj.predecessor (mTestObj.firstKey ()));
  }

  @Test
  public void testPredecessorForKeysInSet ()
  {
    assertEquals (FOO, mTestObj.predecessor (mTestObj.lastKey ()));
    assertEquals (ABCD, mTestObj.predecessor ("bar"));
    assertEquals (EMPTY, mTestObj.predecessor ("ABCD"));
    assertEquals (BAR, mTestObj.predecessor ("BAz"));
  }

  @Test
  public void testPredecessorForKeysNotInSet ()
  {
    assertEquals (EMPTY, mTestObj.predecessor ("aaa"));
    mTestObj.remove (EMPTY);
    assertNull (mTestObj.predecessor ("aaa"));
  }

  @Test
  public void testSuccessorOfLastElementIsNull ()
  {
    assertNull (mTestObj.successor (mTestObj.lastKey ()));
  }

  @Test
  public void testSuccessorForKeysInSet ()
  {
    assertEquals (BAZ, mTestObj.successor ("bar"));
    assertEquals (XYZ, mTestObj.successor ("fOO"));
  }

  @Test
  public void testSuccessorForKeysNotInSet ()
  {
    assertNull (mTestObj.successor ("zzz"));
    assertEquals (ABCD, mTestObj.successor ("aaa"));
  }
}
