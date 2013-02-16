/*
 * $Id: TSTKeySetTest.java 181 2010-11-01 09:39:13Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 16.07.2007
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
import info.rolandkrueger.roklib.util.tstmap.test.MapData.TestDataFixture;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class TSTKeySetTest
{
  private TernarySearchTreeMap<String> mMap;
  private TestDataFixture mTestData = MapData.getFiveLowerCaseEntriesOrderingExpected ();
  private Set<CharSequence> mKeySet;
  private Map<CharSequence, String> mComparisonMap;

  @Before
  public void setUp ()
  {
    mMap = new TernarySearchTreeMap<String> ();
    mTestData = MapData.getFiveLowerCaseEntriesOrderingExpected ();
    mMap.putAll (mTestData.getData ());
    mKeySet = mMap.keySet ();
    mComparisonMap = new TreeMap<CharSequence, String> (mTestData.getData ());
  }

  @Test (expected = UnsupportedOperationException.class)
  public void testAdd ()
  {
    mKeySet.add ("test");
  }

  @Test (expected = UnsupportedOperationException.class)
  public void testAddAll ()
  {
    mKeySet.addAll (new TreeSet<CharSequence> ());
  }

  @Test
  public void testClear ()
  {
    mKeySet.clear ();
    assertEquals (0, mMap.size ());
    assertTrue (mKeySet.isEmpty ());
  }

  @Test
  public void testContains ()
  {
    assertTrue (mKeySet.contains ("sverige"));
    assertFalse (mKeySet.contains ("russia"));
  }

  @Test
  public void testContainsAll ()
  {
    Set<CharSequence> data = new TreeSet<CharSequence> ();
    data.add ("deutschland");
    data.add ("france");
    assertTrue (mKeySet.containsAll (data));
  }

  @Test
  public void testContainsAllFail ()
  {
    Set<CharSequence> data = new TreeSet<CharSequence> ();
    data.add ("deutschland");
    data.add ("france");
    data.add ("belgium");
    assertFalse (mKeySet.containsAll (data));

    data.clear ();
    data.add ("norge");
    data.add ("danmark");
    assertFalse (mKeySet.containsAll (data));
  }

  @Test
  public void testRemove ()
  {
    boolean changed;
    changed = mKeySet.remove ("deutschland");
    mComparisonMap.remove ("deutschland");
    assertTrue (changed);
    assertEquals (mComparisonMap, mMap);
  }

  @Test
  public void testRemoveNothingRemoved ()
  {
    boolean changed;
    changed = mKeySet.remove ("not in map");
    mComparisonMap.remove ("not in map");
    assertFalse (changed);
    assertEquals (mComparisonMap, mMap);
  }

  @Test
  public void testRemoveAll ()
  {
    boolean changed;
    Set<CharSequence> data = new TreeSet<CharSequence> ();
    data.add ("deutschland");
    data.add ("france");

    changed = mKeySet.removeAll (data);
    assertTrue (changed);
    mComparisonMap.keySet ().removeAll (data);
    assertEquals (mComparisonMap, mMap);
  }

  @Test
  public void testRemoveAllFail ()
  {
    boolean changed;
    Set<CharSequence> data = new TreeSet<CharSequence> ();
    data.add ("not in");
    data.add ("map");

    changed = mKeySet.removeAll (data);
    assertFalse (changed);
    mComparisonMap.keySet ().removeAll (data);
    assertEquals (mComparisonMap, mMap);
  }

  @Test
  public void testRetainAll ()
  {
    boolean changed;
    Set<CharSequence> data = new TreeSet<CharSequence> ();
    data.add ("deutschland");
    data.add ("france");
    data.add ("not in map");

    changed = mKeySet.retainAll (data);
    assertTrue (changed);
    mComparisonMap.keySet ().retainAll (data);
    assertEquals (mComparisonMap, mMap);
    assertEquals (2, mMap.size ());
  }

  @Test
  public void testIterator_Remove ()
  {
    Iterator<CharSequence> it = mKeySet.iterator ();
    it.next ();
    it.remove ();
    mComparisonMap.remove ("deutschland");
    assertEquals (mComparisonMap.keySet (), mKeySet);
  }
}
