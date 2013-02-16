/*
 * $Id: TSTMapValuesTest.java 181 2010-11-01 09:39:13Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on  11.07.2007
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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import info.rolandkrueger.roklib.util.TernarySearchTreeMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TSTMapValuesTest
{
  private Collection<String> mValues;
  private TernarySearchTreeMap<String> mMap;
  private Collection<String> mOriginalValues;

  @Before
  public void setUp ()
  {
    mMap = new TernarySearchTreeMap<String> ();
    mMap.putAll (MapData.getFiveLowerCaseEntriesOrderingExpected ().getData ());
    mValues = mMap.values ();
    mOriginalValues = MapData.getFiveLowerCaseEntriesOrderingExpected ().getData ().values ();
  }

  @Test (expected = UnsupportedOperationException.class)
  public void testAddAllFail ()
  {
    List<String> l = new ArrayList<String> ();
    l.add ("");
    mValues.addAll (l);
  }

  @Test (expected = UnsupportedOperationException.class)
  public void testAddFail ()
  {
    mValues.add ("");
  }

  @Test
  public void testClear ()
  {
    mValues.clear ();
    assertTrue (mMap.isEmpty ());
  }

  @Test
  public void testContains ()
  {
    Collection<String> c = MapData.getFiveLowerCaseEntriesOrderingExpected ().getData ().values ();

    for (String s : c)
    {
      assertTrue (mValues.contains (s));
    }
  }

  @Test
  public void testContainsAll ()
  {
    assertTrue (mValues.containsAll (mOriginalValues));
  }

  @Test
  public void testContainsAllFails ()
  {
    List<String> values = new ArrayList<String> (mOriginalValues);
    values.add ("extra");
    assertFalse (mValues.containsAll (values));
  }

  @Test
  public void testEquals ()
  {
    assertTrue (mValues.equals (MapData.getFiveLowerCaseEntriesOrderingExpected ().getData ()
        .values ()));
  }

  @Test
  public void testIsEmpty ()
  {
    mValues.clear ();
    assertTrue (mValues.isEmpty ());
  }

  @Test
  public void testIterator ()
  {
    for (Iterator<String> it = mValues.iterator (); it.hasNext ();)
    {
      assertTrue (mMap.containsValue (it.next ()));
    }
  }

  @Test
  public void testRemove ()
  {
    assertTrue (mValues.remove (mOriginalValues.iterator ().next ()));
    assertEquals (mOriginalValues.size () - 1, mMap.size ());
    mOriginalValues.remove (mOriginalValues.iterator ().next ());
    assertEquals (mValues, mOriginalValues);
    assertFalse (mValues.remove ("not in map"));
    assertEquals (mOriginalValues.size (), mMap.size ());
  }

  @Test
  public void testRemoveAll ()
  {
    Collection<String> notInMap = new ArrayList<String> ();
    notInMap.add ("not");
    notInMap.add ("in");
    notInMap.add ("map");
    assertFalse (mValues.removeAll (notInMap));
    assertEquals (mOriginalValues.size (), mValues.size ());

    Collection<String> inMap = new ArrayList<String> ();
    Iterator<String> it = mOriginalValues.iterator ();
    inMap.add (it.next ());
    inMap.add (it.next ());
    assertTrue (mValues.removeAll (inMap));
    assertEquals (mOriginalValues.size () - 2, mValues.size ());
  }

  @Test
  public void testRetainAll ()
  {
    Collection<String> inMap = new ArrayList<String> ();
    Iterator<String> it = mOriginalValues.iterator ();
    inMap.add (it.next ());
    inMap.add (it.next ());
    assertTrue (mValues.retainAll (inMap));
    assertEquals (inMap.size (), mValues.size ());
  }

  @Test
  public void testRetainAllMapWillNotChange ()
  {
    assertFalse (mValues.retainAll (mOriginalValues));
  }
}
