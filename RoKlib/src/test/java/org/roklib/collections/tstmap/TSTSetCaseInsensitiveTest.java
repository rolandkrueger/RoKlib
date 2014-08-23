/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 25.01.2011
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.roklib.collections.TernarySearchTreeSet;

public class TSTSetCaseInsensitiveTest
{
  private TernarySearchTreeSet mTestObj;

  @Before
  public void setUp ()
  {
    mTestObj = new TernarySearchTreeSet (TSTSetTest.STRINGS, true);
  }

  @Test
  public void testContains ()
  {
    for (CharSequence element : mTestObj)
    {
      assertTrue (mTestObj.contains (element.toString ().toUpperCase (Locale.getDefault ())));
    }
  }

  @Test
  public void testRemove ()
  {
    for (CharSequence element : mTestObj)
    {
      assertTrue (mTestObj.remove (element.toString ().toUpperCase (Locale.getDefault ())));
    }
    assertTrue (mTestObj.isEmpty ());
  }

  @Test
  public void testIndexOf ()
  {
    for (int index = 0; index < mTestObj.size (); ++index)
    {
      assertEquals (index,
          mTestObj.indexOf (mTestObj.getElementAt (index).toString ().toUpperCase (Locale.getDefault ())));
    }
  }

  @Test
  public void testPredecessor ()
  {
    SortedSet<CharSequence> compareSet = new TreeSet<CharSequence> (mTestObj);
    Iterator<CharSequence> compareIterator = compareSet.iterator ();
    compareIterator.next ();
    int index = 0;
    while (compareIterator.hasNext ())
    {
      assertEquals (mTestObj.getElementAt (index),
          mTestObj.predecessor (compareIterator.next ().toString ().toUpperCase (Locale.getDefault ())));
      index++;
    }
  }

  @Test
  public void testSuccessor ()
  {
    SortedSet<CharSequence> compareSet = new TreeSet<CharSequence> (mTestObj);
    Iterator<CharSequence> compareIterator = compareSet.iterator ();
    int index = 1;
    while (index < mTestObj.size ())
    {
      assertEquals (mTestObj.getElementAt (index),
          mTestObj.successor (compareIterator.next ().toString ().toUpperCase (Locale.getDefault ())));
      index++;
    }
  }

  @Test
  public void testPrefixMatch ()
  {
    CharSequence[] expectedValues = new CharSequence[] { "artichoke", "avocado" };
    int index = 0;
    for (CharSequence string : mTestObj.getPrefixMatch ("A"))
    {
      assertEquals (expectedValues[index], string);
      index++;
    }
  }

  @Test
  public void testArrayConstructor ()
  {
    mTestObj = new TernarySearchTreeSet (new String[] { "a", "B", "c" }, true);
    assertEquals (3, mTestObj.size ());
    assertEquals ("[a, B, c]", mTestObj.toString ());
  }

  @Test
  public void testSortedSetConstructor ()
  {
    SortedSet<CharSequence> set = new TreeSet<CharSequence> (Arrays.asList ("a", "B", "c"));
    mTestObj = new TernarySearchTreeSet (set, true);
    assertEquals (3, mTestObj.size ());
    assertEquals ("[a, B, c]", mTestObj.toString ());
  }
}
