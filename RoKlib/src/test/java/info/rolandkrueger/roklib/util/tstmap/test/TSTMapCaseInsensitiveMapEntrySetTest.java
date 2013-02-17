/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 16.11.2010
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
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.roklib.collections.TernarySearchTreeMapCaseInsensitive;

public class TSTMapCaseInsensitiveMapEntrySetTest
{
  private static final String                         EMPTY = "";
  private static final String                         ABCD  = "ABcd";
  private static final String                         XYZ   = "xyz";
  private static final String                         FOO   = "Foo";
  private static final String                         BAR   = "Bar";
  private static final String                         BAZ   = "BAZ";

  private TernarySearchTreeMapCaseInsensitive<String> mTestObj;
  private Set<Map.Entry<CharSequence, String>>        mEntrySet;

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
    mEntrySet = mTestObj.entrySet ();
  }

  @Test
  public void testEntrySet ()
  {
    assertEquals (mTestObj.size (), mEntrySet.size ());
    Iterator<Map.Entry<CharSequence, String>> iterator = mEntrySet.iterator ();
    assertTrue (iterator.hasNext ());
    Map.Entry<CharSequence, String> entry = iterator.next ();
    assertNotNull (entry);
    assertEquals ("", entry.getKey ());
    assertEquals ("", entry.getValue ());
    entry = iterator.next ();
    assertNotNull (entry);
    assertEquals (ABCD, entry.getKey ());
    assertEquals (ABCD, entry.getValue ());
    entry = iterator.next ();
    assertNotNull (entry);
    assertEquals (BAR, entry.getKey ());
    assertEquals (BAR, entry.getValue ());
    entry = iterator.next ();
    assertNotNull (entry);
    assertEquals (BAZ, entry.getKey ());
    assertEquals (BAZ, entry.getValue ());
    entry = iterator.next ();
    assertNotNull (entry);
    assertEquals (FOO, entry.getKey ());
    assertEquals (FOO, entry.getValue ());
    entry = iterator.next ();
    assertNotNull (entry);
    assertEquals (XYZ, entry.getKey ());
    assertEquals (XYZ, entry.getValue ());
  }

  @Test
  public void testContainsAll ()
  {
    List<Map.Entry<CharSequence, String>> entries = new ArrayList<Map.Entry<CharSequence, String>> (5);
    entries.add (new SimpleEntry<CharSequence, String> ("abcd", ABCD));
    entries.add (new SimpleEntry<CharSequence, String> ("fOO", FOO));
    entries.add (new SimpleEntry<CharSequence, String> ("baZ", BAZ));
    entries.add (new SimpleEntry<CharSequence, String> ("BAR", BAR));
    entries.add (new SimpleEntry<CharSequence, String> (XYZ, XYZ));
    assertTrue (mEntrySet.containsAll (entries));
  }

  @Test (expected = UnsupportedOperationException.class)
  public void testAdd ()
  {
    mEntrySet.add (null);
  }

  @Test (expected = UnsupportedOperationException.class)
  public void testAddAll ()
  {
    mEntrySet.addAll (null);
  }

  @Test
  public void testSize ()
  {
    assertEquals (6, mEntrySet.size ());
  }

  @Test
  public void testIsEmpty ()
  {
    assertFalse (mEntrySet.isEmpty ());
    mEntrySet.clear ();
    assertTrue (mEntrySet.isEmpty ());
  }

  @Test
  public void testRemove ()
  {
    assertTrue (mEntrySet.remove (new SimpleEntry<CharSequence, String> ("abCD", ABCD)));
    assertFalse (mTestObj.containsKey (ABCD));
    assertTrue (mEntrySet.remove (new SimpleEntry<CharSequence, String> ("fOO", FOO)));
    assertFalse (mTestObj.containsKey (FOO));
    assertFalse (mEntrySet.remove (new SimpleEntry<CharSequence, String> ("zzz", "zzz")));
    assertFalse (mEntrySet.remove (new SimpleEntry<CharSequence, String> (XYZ, "zzz")));
    assertFalse (mEntrySet.remove (new SimpleEntry<CharSequence, String> (null, "zzz")));
    assertTrue (mTestObj.containsKey (XYZ));
    assertFalse (mEntrySet.remove (null));
  }

  @Test
  @SuppressWarnings ({ "unchecked", "rawtypes" })
  public void testRetainAll ()
  {
    List list = new ArrayList ();
    list.add (new SimpleEntry<CharSequence, String> ("abCD", ABCD));
    list.add (new SimpleEntry<CharSequence, String> ("fOO", FOO));
    list.add (new SimpleEntry<CharSequence, String> ("zzz", "zzz"));
    list.add ("some String");
    mEntrySet.retainAll (list);
    assertEquals (2, mEntrySet.size ());
    assertTrue (mTestObj.containsKey (ABCD));
    assertTrue (mTestObj.containsKey (FOO));
  }

  @Test
  @SuppressWarnings ({ "unchecked", "rawtypes" })
  public void testRemoveAll ()
  {
    List list = new ArrayList ();
    list.add (new SimpleEntry<CharSequence, String> ("abCD", ABCD));
    list.add (new SimpleEntry<CharSequence, String> ("fOO", FOO));
    list.add (new SimpleEntry<CharSequence, String> ("zzz", "zzz"));
    mEntrySet.removeAll (list);
    assertEquals (4, mTestObj.size ());
    assertFalse (mTestObj.containsKey (ABCD));
    assertFalse (mTestObj.containsKey (FOO));

    list.add ("1");
    list.add (new SimpleEntry<CharSequence, String> ("xYz", XYZ));
    list.add (new SimpleEntry<CharSequence, String> ("BAR", BAR));
    list.add (new SimpleEntry<CharSequence, String> ("baZ", BAZ));
    list.add (new SimpleEntry<CharSequence, String> ("", EMPTY));
    mEntrySet.removeAll (list);
    assertTrue (mTestObj.isEmpty ());
  }

  @Test
  public void testEntrySetContains ()
  {
    SimpleEntry<CharSequence, String> entry = new SimpleEntry<CharSequence, String> ("abCD", ABCD);
    assertTrue (mEntrySet.contains (entry));
    entry = new SimpleEntry<CharSequence, String> ("abcd", ABCD);
    assertTrue (mEntrySet.contains (entry));
    entry = new SimpleEntry<CharSequence, String> ("fOO", FOO);
    assertTrue (mEntrySet.contains (entry));
    entry = new SimpleEntry<CharSequence, String> ("baZ", BAZ);
    assertTrue (mEntrySet.contains (entry));
    entry = new SimpleEntry<CharSequence, String> ("BAR", BAR);
    assertTrue (mEntrySet.contains (entry));
    entry = new SimpleEntry<CharSequence, String> (XYZ, XYZ);
    assertTrue (mEntrySet.contains (entry));

    entry = new SimpleEntry<CharSequence, String> ("zzz", "zzz");
    assertFalse (mEntrySet.contains (entry));
    entry = new SimpleEntry<CharSequence, String> (XYZ, "zzz");
    assertFalse (mEntrySet.contains (entry));
    entry = new SimpleEntry<CharSequence, String> (XYZ, null);
    assertFalse (mEntrySet.contains (entry));
    entry = new SimpleEntry<CharSequence, String> (null, XYZ);
    assertFalse (mEntrySet.contains (entry));
    entry = new SimpleEntry<CharSequence, String> (null, null);
    assertFalse (mEntrySet.contains (entry));
    assertFalse (mEntrySet.contains ("foo"));
    assertFalse (mEntrySet.contains (null));
  }
}
