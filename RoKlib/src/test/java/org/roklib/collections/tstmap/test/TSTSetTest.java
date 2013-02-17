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
package org.roklib.collections.tstmap.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.roklib.collections.TernarySearchTreeSet;

public class TSTSetTest
{
  public static final CharSequence[] STRINGS       = { "artichoke", "artichoke", "eggplant", "avocado", "courgette",
      "parsley", "basil", "juniper", "sorrel", "sorrel" };
  private final int                  EXPECTED_SIZE = STRINGS.length - 2; // two strings are contained twice in the array

  private TernarySearchTreeSet       mTestObj;

  @Before
  public void setUp ()
  {
    mTestObj = new TernarySearchTreeSet (STRINGS);
  }

  @Test
  public void testDefaultConstructor ()
  {
    TernarySearchTreeSet set = new TernarySearchTreeSet ();
    assertIsEmpty (set);
  }

  @Test
  public void testConstructorForCharSequenceArray ()
  {
    assertEquals (EXPECTED_SIZE, mTestObj.size ());
  }

  @Test
  public void testConstructorForSortedSet ()
  {
    SortedSet<CharSequence> dataSet = new TreeSet<CharSequence> (Arrays.asList (STRINGS));
    TernarySearchTreeSet set = new TernarySearchTreeSet (dataSet);
    assertEquals (EXPECTED_SIZE, set.size ());
  }

  @Test
  public void testConstructorForCollection ()
  {
    List<CharSequence> list = new LinkedList<CharSequence> (Arrays.asList (STRINGS));
    TernarySearchTreeSet set = new TernarySearchTreeSet (list);
    assertEquals (EXPECTED_SIZE, set.size ());
  }

  @Test
  public void testComparator ()
  {
    assertNull (mTestObj.comparator ());
  }

  @Test
  public void testFirst ()
  {
    assertEquals ("artichoke", mTestObj.first ());
  }

  @Test
  public void testLast ()
  {
    assertEquals ("sorrel", mTestObj.last ());
  }

  @Test
  public void testAdd ()
  {
    assertFalse (mTestObj.add ("clover"));
    assertEquals (STRINGS.length - 1, mTestObj.size ());
    assertTrue (mTestObj.add ("clover"));
    assertEquals (STRINGS.length - 1, mTestObj.size ());
  }

  @Test (expected = NullPointerException.class)
  public void testAddNull ()
  {
    mTestObj.add (null);
  }

  @Test
  public void testAddAll ()
  {
    List<CharSequence> list = new LinkedList<CharSequence> ();
    list.addAll (Arrays.asList ("boletus", "chanterelle", "truffle", "truffle"));
    mTestObj.addAll (list);
    assertEquals (STRINGS.length + list.size () - 3, mTestObj.size ());
  }

  @Test
  public void testContains ()
  {
    assertTrue (mTestObj.contains ("sorrel"));
    assertFalse (mTestObj.contains ("boletus"));
  }

  @Test (expected = NullPointerException.class)
  public void testContainsNull ()
  {
    mTestObj.contains (null);
  }

  @Test
  public void testContainsAll ()
  {
    List<CharSequence> list = new LinkedList<CharSequence> ();
    list.addAll (Arrays.asList ("parsley", "basil", "juniper", "sorrel", "sorrel"));
    assertTrue (mTestObj.containsAll (list));
  }

  @Test
  public void testDoesNotContainAll ()
  {
    List<CharSequence> list = new LinkedList<CharSequence> ();
    list.addAll (Arrays.asList ("boletus", "basil", "sorrel"));
    assertFalse (mTestObj.containsAll (list));
  }

  @Test (expected = NullPointerException.class)
  public void testContainsAllFails ()
  {
    List<CharSequence> list = new LinkedList<CharSequence> ();
    list.addAll (Arrays.asList (null, "basil", "sorrel"));
    mTestObj.containsAll (list);
  }

  @Test
  public void testRetainAll ()
  {
    List<CharSequence> list = new LinkedList<CharSequence> ();
    list.addAll (Arrays.asList ("boletus", "basil", "sorrel"));
    boolean changed = mTestObj.retainAll (list);
    assertEquals (2, mTestObj.size ());

    assertTrue (mTestObj.contains ("basil"));
    assertTrue (mTestObj.contains ("sorrel"));
    assertTrue (changed);
  }

  @Test
  public void testRetainAllDoesntChangeSet ()
  {
    List<CharSequence> list = new LinkedList<CharSequence> ();
    list.addAll (Arrays.asList (STRINGS));
    boolean changed = mTestObj.retainAll (list);
    assertEquals (EXPECTED_SIZE, mTestObj.size ());
    assertFalse (changed);
  }

  @Test (expected = NullPointerException.class)
  public void testRetainAllWithNull ()
  {
    mTestObj.retainAll (null);
  }

  @Test
  public void testClear ()
  {
    mTestObj.clear ();
    assertIsEmpty (mTestObj);
  }

  @Test
  public void testRemove ()
  {
    boolean wasContained = mTestObj.remove ("basil");
    assertTrue (wasContained);
    assertFalse (mTestObj.contains ("basil"));

    wasContained = mTestObj.remove ("bolete");
    assertFalse (wasContained);
  }

  @Test (expected = NullPointerException.class)
  public void testRemoveNull ()
  {
    mTestObj.remove (null);
  }

  @Test
  public void testRemoveAll ()
  {
    List<CharSequence> list = new LinkedList<CharSequence> ();
    list.addAll (Arrays.asList ("boletus", "basil", "sorrel"));
    boolean changed = mTestObj.removeAll (list);
    assertTrue (changed);
    assertFalse (mTestObj.contains ("basil"));
    assertFalse (mTestObj.contains ("sorrel"));
    assertEquals (EXPECTED_SIZE - 2, mTestObj.size ());
  }

  @Test
  public void testRemoveAllDoesntChange ()
  {
    List<CharSequence> list = new LinkedList<CharSequence> ();
    list.addAll (Arrays.asList ("boletus", "chanterelle", "truffle", "truffle"));
    boolean changed = mTestObj.removeAll (list);
    assertFalse (changed);
  }

  @Test (expected = NullPointerException.class)
  public void testRemoveAllWithNull ()
  {
    List<CharSequence> list = new LinkedList<CharSequence> ();
    list.addAll (Arrays.asList (null, "basil"));
    mTestObj.removeAll (list);
  }

  @Test
  public void testToArray ()
  {
    Object[] array = mTestObj.toArray ();
    assertEquals (EXPECTED_SIZE, array.length);
    assertEquals (new Object[] { "artichoke", "avocado", "basil", "courgette", "eggplant", "juniper", "parsley",
        "sorrel" }, array);
  }

  @Test
  public void testToCharSequenceArray ()
  {
    CharSequence[] array = mTestObj.toArray (new CharSequence[] {});
    assertEquals (EXPECTED_SIZE, array.length);
    assertEquals (new CharSequence[] { "artichoke", "avocado", "basil", "courgette", "eggplant", "juniper", "parsley",
        "sorrel" }, array);
  }

  @Test
  public void testToString ()
  {
    Set<CharSequence> dataSet = new TreeSet<CharSequence> (Arrays.asList (STRINGS));
    assertEquals (dataSet.toString (), mTestObj.toString ());
  }

  @Test
  public void testEquals ()
  {
    Set<CharSequence> treeSet = new TreeSet<CharSequence> (Arrays.asList (STRINGS));
    Set<CharSequence> hashSet = new HashSet<CharSequence> (Arrays.asList (STRINGS));
    assertEquals (treeSet, mTestObj);
    assertEquals (mTestObj, treeSet);
    assertEquals (hashSet, mTestObj);
    assertEquals (mTestObj, hashSet);

    assertEquals (mTestObj, mTestObj);
    assertFalse (mTestObj.equals (null));
  }

  @Test
  public void testHashCode ()
  {
    Set<CharSequence> treeSet = new TreeSet<CharSequence> (Arrays.asList (STRINGS));
    assertEquals (treeSet.hashCode (), mTestObj.hashCode ());
  }

  @Test
  public void testIterator ()
  {
    Iterator<CharSequence> testIterator = mTestObj.iterator ();
    Set<CharSequence> treeSet = new TreeSet<CharSequence> (Arrays.asList (STRINGS));
    Iterator<CharSequence> compareIterator = treeSet.iterator ();
    while (testIterator.hasNext ())
    {
      assertEquals (compareIterator.next (), testIterator.next ());
    }
  }

  @Test
  public void testHeadSet ()
  {
    SortedSet<CharSequence> headSet = mTestObj.headSet ("pomegranade");
    assertNotNull (headSet);
    assertEquals (7, headSet.size ());
    assertEquals (
        new TreeSet<CharSequence> (Arrays.asList ("artichoke", "avocado", "basil", "courgette", "eggplant", "juniper",
            "parsley")), headSet);
  }

  @Test
  public void testTailSet ()
  {
    SortedSet<CharSequence> tailSet = mTestObj.tailSet ("pomegranade");
    assertNotNull (tailSet);
    assertEquals (1, tailSet.size ());
    assertTrue (tailSet.contains ("sorrel"));
  }

  @Test
  public void testSubSet ()
  {
    SortedSet<CharSequence> subSet = mTestObj.subSet ("cranberry", "pomegranade");
    assertNotNull (subSet);
    assertEquals (3, subSet.size ());
    assertEquals (new TreeSet<CharSequence> (Arrays.asList ("eggplant", "juniper", "parsley")), subSet);
  }

  @Test
  public void testIndexOf ()
  {
    assertEquals (-1, mTestObj.indexOf ("not in set"));
    assertEquals (0, mTestObj.indexOf ("artichoke"));
    assertEquals (EXPECTED_SIZE - 1, mTestObj.indexOf ("sorrel"));
  }

  @Test
  public void testGetElementAt ()
  {
    for (int index = 0; index < mTestObj.size (); ++index)
    {
      assertEquals (index, mTestObj.indexOf (mTestObj.getElementAt (index)));
    }
  }

  @Test (expected = IndexOutOfBoundsException.class)
  public void testGetElementAtWithIndexTooLarge ()
  {
    assertNull (mTestObj.getElementAt (999));
  }

  @Test
  public void testPredecessorOfFirstElementIsNull ()
  {
    assertNull (mTestObj.predecessor (mTestObj.first ()));
  }

  @Test
  public void testPredecessorForKeysInSet ()
  {
    assertEquals ("parsley", mTestObj.predecessor (mTestObj.last ()));
    assertEquals ("artichoke", mTestObj.predecessor ("avocado"));
    assertEquals ("avocado", mTestObj.predecessor ("basil"));
    assertEquals ("basil", mTestObj.predecessor ("courgette"));
  }

  @Test
  public void testPredecessorForKeysNotInSet ()
  {
    assertEquals ("courgette", mTestObj.predecessor ("cranberry"));
    assertNull (mTestObj.predecessor ("aardvark"));
  }

  @Test
  public void testSuccessorOfLastElementIsNull ()
  {
    assertNull (mTestObj.successor (mTestObj.last ()));
  }

  @Test
  public void testSuccessorForKeysInSet ()
  {
    assertEquals ("courgette", mTestObj.successor ("basil"));
    assertEquals ("parsley", mTestObj.successor ("juniper"));
  }

  @Test
  public void testSuccessorForKeysNotInSet ()
  {
    assertNull (mTestObj.successor ("yams"));
    assertEquals ("juniper", mTestObj.successor ("horseradish"));
  }

  @Test
  public void testGetPrefixMatch ()
  {
    CharSequence[] expectedValues = new CharSequence[] { "artichoke", "avocado" };
    int index = 0;
    for (CharSequence string : mTestObj.getPrefixMatch ("a"))
    {
      assertEquals (expectedValues[index], string);
      index++;
    }
  }

  @Test
  public void testPrefixMatchYieldsNoResult ()
  {
    Iterable<CharSequence> it = mTestObj.getPrefixMatch ("x");
    assertFalse (it.iterator ().hasNext ());
  }

  @Test
  public void testPrefixMatchYieldsAllElements ()
  {
    CharSequence[] expectedValues = new CharSequence[] { "artichoke", "avocado", "basil", "courgette", "eggplant",
        "juniper", "parsley", "sorrel" };
    int index = 0;
    for (CharSequence string : mTestObj.getPrefixMatch (""))
    {
      assertEquals (expectedValues[index], string);
      index++;
    }
  }

  @Test
  public void testMatchAlmost ()
  {
    TernarySearchTreeSet set = new TernarySearchTreeSet ();
    set.addAll (Arrays.asList ("xxxx", "1234", "yxxxx", "zxxx", "xxzz"));
    SortedSet<CharSequence> result = set.matchAlmost ("yxxx", 1, 1);
    assertEquals (new TreeSet<CharSequence> (Arrays.asList ("xxxx", "yxxxx", "zxxx")), result);
  }

  @Test
  public void testMatchAlmostYieldsNoResult ()
  {
    assertEquals (0, mTestObj.matchAlmost ("xxxx", 0, 0).size ());
  }

  @Test
  public void testMatchAlmostYieldsAllElements ()
  {
    assertEquals (8, mTestObj.matchAlmost ("xxxxxxxx", 999, 999).size ());
  }

  private void assertIsEmpty (TernarySearchTreeSet set)
  {
    assertEquals (0, set.size ());
    assertTrue (set.isEmpty ());
  }
}
