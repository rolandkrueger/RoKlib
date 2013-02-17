/*
 * Copyright (C) 2007 Roland Krueger
 * Created on  11.07.2007
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
  private Collection<String>           mValues;
  private TernarySearchTreeMap<String> mMap;
  private Collection<String>           mOriginalValues;

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
    assertTrue (mValues.equals (MapData.getFiveLowerCaseEntriesOrderingExpected ().getData ().values ()));
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
