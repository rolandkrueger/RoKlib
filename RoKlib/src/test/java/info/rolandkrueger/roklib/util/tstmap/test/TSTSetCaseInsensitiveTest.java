/*
 * $Id: TSTSetCaseInsensitiveTest.java 254 2011-01-25 18:48:50Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 25.01.2011
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
import java.util.Iterator;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import info.rolandkrueger.roklib.util.TernarySearchTreeSet;

import org.junit.Before;
import org.junit.Test;

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
      assertEquals (index, mTestObj.indexOf (mTestObj.getElementAt (index).toString ().toUpperCase (Locale.getDefault ())));
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
    CharSequence[] expectedValues = new CharSequence[] {"artichoke", "avocado"};
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
    mTestObj = new TernarySearchTreeSet (new String[] {"a", "B", "c"}, true);
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
