/*
 * $Id: TSTMapCaseInsensitiveMapEntrySetTest.java 210 2010-11-17 20:33:44Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 16.11.2010
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;

import info.rolandkrueger.roklib.util.TernarySearchTreeMapCaseInsensitive;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TSTMapCaseInsensitiveMapEntrySetTest
{
  private static final String EMPTY = "";
  private static final String ABCD = "ABcd";
  private static final String XYZ  = "xyz";
  private static final String FOO  = "Foo";
  private static final String BAR  = "Bar";
  private static final String BAZ  = "BAZ";
    
  private TernarySearchTreeMapCaseInsensitive<String> mTestObj;
  private Set<Map.Entry<CharSequence, String>> mEntrySet;
  
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
    List<Map.Entry<CharSequence, String>> entries = new ArrayList<Map.Entry<CharSequence,String>> (5);
    entries.add (new SimpleEntry<CharSequence, String> ("abcd", ABCD));
    entries.add (new SimpleEntry<CharSequence, String> ("fOO", FOO));
    entries.add (new SimpleEntry<CharSequence, String> ("baZ", BAZ));
    entries.add (new SimpleEntry<CharSequence, String> ("BAR", BAR));
    entries.add (new SimpleEntry<CharSequence, String> (XYZ, XYZ));
    assertTrue (mEntrySet.containsAll (entries));
  }
  
  @Test (expected=UnsupportedOperationException.class)
  public void testAdd ()
  {
    mEntrySet.add (null);
  }

  @Test (expected=UnsupportedOperationException.class)
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
