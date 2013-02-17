/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 16.07.2007
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
import info.rolandkrueger.roklib.util.tstmap.test.MapData.TestDataFixture;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.roklib.util.TernarySearchTreeMap;

public class TSTKeySetTest
{
  private TernarySearchTreeMap<String> mMap;
  private TestDataFixture              mTestData = MapData.getFiveLowerCaseEntriesOrderingExpected ();
  private Set<CharSequence>            mKeySet;
  private Map<CharSequence, String>    mComparisonMap;

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
