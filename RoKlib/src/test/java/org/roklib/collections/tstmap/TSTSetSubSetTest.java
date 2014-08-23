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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.roklib.collections.TernarySearchTreeSet;

public class TSTSetSubSetTest
{
  private final CharSequence[] STRINGS = { "darmstadt", "frankenthal", "heidelberg", "hemsbach", "heppenheim",
      "ludwigshafen", "mannheim", "schwetzingen", "sulzbach", "viernheim", "weinheim" };

  private TernarySearchTreeSet mTestObj;

  @Before
  public void setUp ()
  {
    mTestObj = new TernarySearchTreeSet (STRINGS);
  }

  @Test
  public void testHeadSetWithElementInSet ()
  {
    SortedSet<CharSequence> headSet = mTestObj.headSet ("heppenheim");
    assertFalse (headSet.contains ("heppenheim"));
    assertEquals (getExpectedValues ("darmstadt", "frankenthal", "heidelberg", "hemsbach"), headSet);
  }

  @Test
  public void testHeadSetWithElementNotInSet ()
  {
    SortedSet<CharSequence> headSet = mTestObj.headSet ("hockenheim");
    assertEquals (getExpectedValues ("darmstadt", "frankenthal", "heidelberg", "hemsbach", "heppenheim"), headSet);
  }

  @Test
  public void testAddToHeadSetChangesBaseSet ()
  {
    SortedSet<CharSequence> headSet = mTestObj.headSet ("frankfurt");
    assertFalse (headSet.add ("altrip"));
    assertTrue (mTestObj.contains ("altrip"));
    assertTrue (headSet.add ("altrip"));
  }

  @Test
  public void testAddToBaseSetChangesHeadSet ()
  {
    SortedSet<CharSequence> headSet = mTestObj.headSet ("frankfurt");
    mTestObj.add ("altrip");
    assertTrue (headSet.contains ("altrip"));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testAddToHeadSetOutOfBoundsThrowsExcpetion ()
  {
    mTestObj.headSet ("dresden").add ("pforzheim");
  }

  @Test (expected = NullPointerException.class)
  public void testHeadSetContainsNullThrowsException ()
  {
    mTestObj.headSet ("pforzheim").contains (null);
  }

  @Test (expected = NullPointerException.class)
  public void testRemoveNullFromHeadSetThrowsException ()
  {
    mTestObj.headSet ("dresden").remove (null);
  }

  @Test
  public void testRemoveSucceeds ()
  {
    SortedSet<CharSequence> headSet = mTestObj.headSet ("frankfurt");
    assertTrue (headSet.remove ("darmstadt"));
    assertFalse (mTestObj.contains ("darmstadt"));
  }

  @Test
  public void testRemoveNotSuccessful ()
  {
    SortedSet<CharSequence> headSet = mTestObj.headSet ("frankfurt");
    assertFalse (headSet.remove ("weinheim"));
    assertTrue (mTestObj.contains ("weinheim"));
  }

  @Test
  public void testHeadSetFirst ()
  {
    assertEquals (mTestObj.first (), mTestObj.headSet ("weinheim").first ());
  }

  @Test
  public void testHeadSetLast ()
  {
    assertEquals ("sulzbach", mTestObj.headSet ("viernheim").last ());
  }

  @Test
  public void testHeadSetOfHeadSetSuccessful ()
  {
    SortedSet<CharSequence> headSet = mTestObj.headSet ("hemsbach").headSet ("frankfurt");
    assertEquals (getExpectedValues ("darmstadt", "frankenthal"), headSet);
    headSet.add ("altrip");
    assertTrue (mTestObj.contains ("altrip"));
  }

  @Test
  public void testHeadSetTailSet ()
  {
    SortedSet<CharSequence> headSet = mTestObj.headSet ("hemsbach").tailSet ("frankenthal");
    assertEquals (getExpectedValues ("frankenthal", "heidelberg"), headSet);
  }

  @Test
  public void testHeadSetSubSet ()
  {
    SortedSet<CharSequence> headSet = mTestObj.headSet ("viernheim").subSet ("heidelberg", "heppenheim");
    assertEquals (getExpectedValues ("heidelberg", "hemsbach"), headSet);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testHeadSetOfHeadSetFails ()
  {
    mTestObj.headSet ("frankfurt").headSet ("hemsbach");
  }

  @Test (expected = NullPointerException.class)
  public void testHeadSetFailsWithNullBound ()
  {
    mTestObj.headSet (null);
  }

  @Test (expected = NullPointerException.class)
  public void testTailSetFailsWithNullBound ()
  {
    mTestObj.tailSet (null);
  }

  @Test (expected = NullPointerException.class)
  public void testSubSetFailsWithNullLowerBound ()
  {
    mTestObj.subSet (null, "");
  }

  @Test (expected = NullPointerException.class)
  public void testSubSetFailsWithNullUpperBound ()
  {
    mTestObj.subSet ("", null);
  }

  @Test
  public void testHeadSetComparatorReturnsNull ()
  {
    assertNull (mTestObj.headSet ("weinheim").comparator ());
  }

  private SortedSet<CharSequence> getExpectedValues (CharSequence... values)
  {
    return new TreeSet<CharSequence> (Arrays.asList (values));
  }
}
