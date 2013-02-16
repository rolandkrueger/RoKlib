/*
 * $Id: TSTMapEntrySetTest.java 208 2010-11-16 18:29:22Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 25.11.2009
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import info.rolandkrueger.roklib.util.TernarySearchTreeMap;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class TSTMapEntrySetTest
{
  private final static String KEY1 = "key1";
  private final static String KEY2 = "key2";
  private final static String KEY3 = "key3";
  private final static String KEY4 = "key4";
  private final static String EMPTY_KEY = "";

  private final static String VALUE1 = "value1";
  private final static String VALUE2 = "value2";
  private final static String VALUE3 = "value3";
  private final static String VALUE4 = "value4";
  private final static String VALUE5 = "emptyKey";

  private TernarySearchTreeMap<String> map;
  private Set<Entry<CharSequence, String>> entrySet;

  @Before
  public void setUp ()
  {
    map = new TernarySearchTreeMap<String> ();
    map.put (KEY1, VALUE1);
    map.put (KEY2, VALUE2);
    map.put (KEY3, VALUE3);
    map.put (KEY4, VALUE4);
    map.put (EMPTY_KEY, VALUE5);
    entrySet = map.entrySet ();
  }

  @Test (expected = UnsupportedOperationException.class)
  public void testAddFail ()
  {
    entrySet.add (null);
  }

  @Test (expected = UnsupportedOperationException.class)
  public void testAddAll ()
  {
    entrySet.addAll (null);
  }

  @Test
  public void testEqual ()
  {
    assertEquals (entrySet.size (), map.size ());
    for (Entry<CharSequence, String> entry : entrySet)
    {
      assertTrue (map.containsKey (entry.getKey ()));
      assertEquals (entry.getValue (), map.get (entry.getKey ()));
    }
  }

  @Test
  public void testClear ()
  {
    entrySet.clear ();
    assertTrue (map.isEmpty ());
    assertTrue (entrySet.isEmpty ());
    assertEquals (0, map.size ());
    assertEquals (0, entrySet.size ());
    assertTrue (map.equals (Collections.emptyMap ()));
  }

  @Test
  public void testRemoveAll ()
  {
    Iterator<Entry<CharSequence, String>> it = entrySet.iterator ();
    it.next ();
    Entry<CharSequence, String> e1 = it.next ();
    it.next ();
    Entry<CharSequence, String> e2 = it.next ();
    List<Entry<CharSequence, String>> c = new LinkedList<Entry<CharSequence, String>> ();
    c.add (e1);
    c.add (e2);
    entrySet.removeAll (c);
    assertEquals (3, map.size ());
    assertEquals (3, entrySet.size ());
    assertFalse (map.containsKey (e1.getKey ()));
    assertFalse (map.containsKey (e2.getKey ()));
    assertFalse (map.containsValue (e1.getValue ()));
    assertFalse (map.containsValue (e2.getValue ()));
  }

  @Test
  public void testRetainAll ()
  {
    Iterator<Entry<CharSequence, String>> it = entrySet.iterator ();
    it.next ();
    Entry<CharSequence, String> e1 = it.next ();
    it.next ();
    Entry<CharSequence, String> e2 = it.next ();
    List<Entry<CharSequence, String>> c = new LinkedList<Entry<CharSequence, String>> ();
    c.add (e1);
    c.add (e2);
    entrySet.retainAll (c);
    assertEquals (2, map.size ());
    assertEquals (2, entrySet.size ());
    assertTrue (map.containsKey (e1.getKey ()));
    assertTrue (map.containsKey (e2.getKey ()));
    assertTrue (map.containsValue (e1.getValue ()));
    assertTrue (map.containsValue (e2.getValue ()));
  }

  @Test
  public void testRemove ()
  {
    Iterator<Entry<CharSequence, String>> it = entrySet.iterator ();
    Entry<CharSequence, String> removed1 = it.next ();
    Entry<CharSequence, String> kept1 = it.next ();
    Entry<CharSequence, String> removed2 = it.next ();
    Entry<CharSequence, String> kept2 = it.next ();
    Entry<CharSequence, String> removed3 = it.next ();
    assertTrue (entrySet.remove (removed1));
    assertTrue (entrySet.remove (removed2));
    assertFalse (entrySet.remove (removed2));
    assertTrue (entrySet.remove (removed3));
    assertEquals (2, map.size ());
    assertEquals (2, entrySet.size ());
    assertFalse (map.containsKey (removed1.getKey ()));
    assertFalse (map.containsKey (removed2.getKey ()));
    assertFalse (map.containsKey (removed3.getKey ()));
    assertFalse (map.containsValue (removed1.getValue ()));
    assertFalse (map.containsValue (removed2.getValue ()));
    assertFalse (map.containsValue (removed3.getValue ()));

    assertTrue (map.containsKey (kept1.getKey ()));
    assertTrue (map.containsKey (kept2.getKey ()));
    assertTrue (map.containsValue (kept1.getValue ()));
    assertTrue (map.containsValue (kept2.getValue ()));
  }
}
