/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 23.11.2009
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.roklib.collections.TernarySearchTreeMap;

public class TSTMapWithEmptyStringKeyTest
{
  private TernarySearchTreeMap<String> map;

  @Before
  public void setUp ()
  {
    map = new TernarySearchTreeMap<String> ();
    map.put ("", "value");
  }

  @Test
  public void testUseEmptyStringAsKey ()
  {
    assertEquals ("value", map.get (""));
  }

  @Test
  public void testContainsKey ()
  {
    map.clear ();
    assertFalse (map.containsKey (""));
    map.put ("", "value");
    assertTrue (map.containsKey (""));
  }

  @Test
  public void testKeySet ()
  {
    map.put ("another key", "xxx");
    assertEquals (2, map.size ());
    assertTrue (map.keySet ().contains (""));
    assertTrue (map.keySet ().contains ("another key"));
  }

  @Test
  public void testValues ()
  {
    map.put ("another key", "xxx");
    Collection<String> values = map.values ();
    assertTrue (values.contains ("xxx"));
    assertTrue (values.contains ("value"));
    assertEquals (2, values.size ());
  }

  @Test
  public void testRemove ()
  {
    map.put ("another key", "xxx");
    map.put ("key2", "2");
    map.put ("key3", "3");
    assertTrue (map.keySet ().contains (""));
    map.remove ("key2");
    assertTrue (map.containsKey (""));
    assertFalse (map.containsKey ("key2"));
    assertTrue (map.containsKey ("key3"));

    assertEquals (3, map.size ());
    map.remove ("another key");
    assertEquals (2, map.size ());
    assertFalse (map.containsKey ("another key"));
    assertTrue (map.containsKey (""));
    assertTrue (map.containsKey ("key3"));

    map.remove ("");
    assertFalse (map.containsKey (""));
    assertTrue (map.containsKey ("key3"));
    assertEquals (1, map.size ());
    map.remove ("key3");
    assertTrue (map.isEmpty ());
  }

  @Test
  public void testPredecessor ()
  {
    map.put ("another key", "xxx");
    Entry<CharSequence, String> entry = map.predecessorEntry ("another key");
    assertNotNull (entry);
    assertEquals ("", entry.getKey ());
    assertEquals ("value", entry.getValue ());
    entry = map.predecessorEntry ("");
    assertNull (entry);
  }

  @Test
  public void testGetKeyAt ()
  {
    map.put ("another key", "xxx");
    map.put ("zzz key", "xxx");
    assertEquals ("", map.getKeyAt (0));
    assertEquals ("zzz key", map.getKeyAt (2));
  }

  @Test
  public void testContainsValue ()
  {
    map.put ("another key", "xxx");
    assertTrue (map.containsValue ("xxx"));
    assertTrue (map.containsValue ("value"));
    assertFalse (map.containsValue ("not in map"));
  }

  @Test
  public void testIndexOf ()
  {
    map.put ("another key", "xxx");
    map.put ("zzz key", "xxx");
    assertEquals (1, map.indexOf ("another key"));
    assertEquals (0, map.indexOf (""));
    assertEquals (2, map.indexOf ("zzz key"));
    assertEquals (-1, map.indexOf ("not in map"));
  }

  @Test
  public void testFirstKey ()
  {
    assertEquals ("", map.firstKey ());
  }

  @Test
  public void testIsEmpty ()
  {
    assertFalse (map.isEmpty ());
  }

  @Test
  public void testClear ()
  {
    map.clear ();
    assertTrue (map.isEmpty ());
  }

  @Test
  public void testSize ()
  {
    assertEquals (1, map.size ());
    map.put ("key", "value");
    assertEquals (2, map.size ());
    map.clear ();
    assertEquals (0, map.size ());
  }

  @Test
  public void testEntrySet ()
  {
    map.put ("another key", "xxx");
    Set<Entry<CharSequence, String>> entrySet = map.entrySet ();
    Iterator<Entry<CharSequence, String>> it = entrySet.iterator ();
    assertTrue (it.hasNext ());
    assertTrue (it.hasNext ());
    Entry<CharSequence, String> entry = it.next ();
    if (entry.getKey ().equals (""))
    {
      assertEquals ("value", entry.getValue ());
    } else if (entry.getKey ().equals ("another key"))
    {
      assertEquals ("xxx", entry.getValue ());
    } else
      Assert.fail (String.format ("Key was '%s'.", entry.getKey ()));

    assertTrue (it.hasNext ());
    assertTrue (it.hasNext ());
    entry = it.next ();
    if (entry.getKey ().equals ("another key"))
    {
      assertEquals ("xxx", entry.getValue ());
    } else if (entry.getKey ().equals (""))
    {
      assertEquals ("value", entry.getValue ());
    } else
      Assert.fail (String.format ("Key was '%s'.", entry.getKey ()));

    assertEquals (2, entrySet.size ());
    map.clear ();
    map.put ("", "value");
    entrySet = map.entrySet ();
    assertEquals (1, entrySet.size ());
    entry = entrySet.iterator ().next ();
    assertEquals ("", entry.getKey ());
    assertEquals ("value", entry.getValue ());
  }
}
