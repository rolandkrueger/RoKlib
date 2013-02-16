/*
 * $Id: TSTSetSubSetTest.java 252 2011-01-24 18:34:30Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 22.01.2011
 *
 * Author: Roland Krueger (www.rolandkrueger.info)
 *
 * This file is part of RoKlib.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package info.rolandkrueger.roklib.util.tstmap.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import info.rolandkrueger.roklib.util.TernarySearchTreeSet;

import org.junit.Before;
import org.junit.Test;

public class TSTSetSubSetTest
{
  private final CharSequence[] STRINGS = {
      "darmstadt", "frankenthal", "heidelberg", "hemsbach", "heppenheim", 
      "ludwigshafen", "mannheim", "schwetzingen", "sulzbach", "viernheim", "weinheim"};
  
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
  
  @Test (expected=IllegalArgumentException.class)
  public void testAddToHeadSetOutOfBoundsThrowsExcpetion ()
  {    
    mTestObj.headSet ("dresden").add ("pforzheim");
  }

  @Test (expected=NullPointerException.class)
  public void testHeadSetContainsNullThrowsException ()
  {
    mTestObj.headSet ("pforzheim").contains (null);
  }
  
  @Test (expected=NullPointerException.class)
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
  
  @Test (expected=IllegalArgumentException.class)
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
  
  private SortedSet<CharSequence> getExpectedValues (CharSequence ... values)
  {
    return new TreeSet<CharSequence> (Arrays.asList (values));
  }
}
