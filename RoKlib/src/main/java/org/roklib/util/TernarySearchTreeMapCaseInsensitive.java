/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 13.11.2010
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
package org.roklib.util;


import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.roklib.util.helper.CheckForNull;

public class TernarySearchTreeMapCaseInsensitive<V> implements ITernarySearchTreeMap<V>
{
  private static final long                                serialVersionUID = 6106815305526987949L;

  private TernarySearchTreeMap<Map.Entry<CharSequence, V>> mData;
  private Locale                                           mLocale;

  public TernarySearchTreeMapCaseInsensitive ()
  {
    this ((Locale) null);
  }

  public TernarySearchTreeMapCaseInsensitive (Locale pLocale)
  {
    mLocale = pLocale;
    if (mLocale == null)
    {
      mLocale = Locale.getDefault ();
    }
    mData = new TernarySearchTreeMap<Map.Entry<CharSequence, V>> ();
  }

  public TernarySearchTreeMapCaseInsensitive (Map<? extends CharSequence, ? extends V> map)
  {
    this ((Locale) null);
    putAll (map);
  }

  public TernarySearchTreeMapCaseInsensitive (Map<? extends CharSequence, ? extends V> map, Locale pLocale)
  {
    this (pLocale);
    putAll (map);
  }

  public SortedSet<CharSequence> matchAlmost (CharSequence key, int distance, int lengthTolerance)
  {
    CheckForNull.check (key);
    SortedSet<CharSequence> result = new TreeSet<CharSequence> ();
    for (CharSequence resultKey : mData.matchAlmost (key.toString ().toLowerCase (mLocale), distance, lengthTolerance))
    {
      result.add (mData.get (resultKey).getKey ());
    }
    return result;
  }

  public Entry<CharSequence, V> getEntry (Object key)
  {
    String keyString = ((CharSequence) key).toString ().toLowerCase (mLocale);
    Entry<CharSequence, Map.Entry<CharSequence, V>> entry = mData.getEntry (keyString);
    return entry == null ? null : new AbstractMap.SimpleEntry<CharSequence, V> (entry.getValue ().getKey (), entry
        .getValue ().getValue ());
  }

  public V getValueAt (int index)
  {
    return mData.getValueAt (index).getValue ();
  }

  public CharSequence getKeyAt (int index)
  {
    return mData.get (mData.getKeyAt (index)).getKey ();
  }

  public int indexOf (CharSequence key)
  {
    return mData.indexOf (key.toString ().toLowerCase (mLocale));
  }

  public CharSequence predecessor (CharSequence forElement)
  {
    Map.Entry<CharSequence, Map.Entry<CharSequence, V>> entry = mData.predecessorEntry (forElement.toString ()
        .toLowerCase (mLocale));
    return entry == null ? null : entry.getValue ().getKey ();
  }

  public CharSequence successor (CharSequence forElement)
  {
    Map.Entry<CharSequence, Map.Entry<CharSequence, V>> entry = mData.successorEntry (forElement.toString ()
        .toLowerCase (mLocale));
    return entry == null ? null : entry.getValue ().getKey ();
  }

  public Iterable<CharSequence> getPrefixMatch (CharSequence prefix)
  {
    CheckForNull.check (prefix);
    final Iterator<Entry<CharSequence, Map.Entry<CharSequence, V>>> iterator = mData.getPrefixSubtreeIterator (
        prefix.toString ().toLowerCase (mLocale), false).iterator ();

    return new Iterable<CharSequence> ()
    {
      public Iterator<CharSequence> iterator ()
      {
        return new Iterator<CharSequence> ()
        {

          public boolean hasNext ()
          {
            return iterator.hasNext ();
          }

          public CharSequence next ()
          {
            return iterator.next ().getValue ().getKey ();
          }

          public void remove ()
          {
            iterator.remove ();
          }
        };
      }
    };
  }

  public Iterable<Entry<CharSequence, V>> getPrefixSubtreeIterator (CharSequence prefix)
  {
    return getPrefixSubtreeIterator (prefix, false);
  }

  public Iterable<Entry<CharSequence, V>> getPrefixSubtreeIterator (final CharSequence pPrefix,
      final boolean inverseSearch)
  {
    return new Iterable<Map.Entry<CharSequence, V>> ()
    {
      private Iterator<Map.Entry<CharSequence, V>> mIterator = getPrefixSubtreeIteratorImpl (pPrefix, inverseSearch);

      public Iterator<java.util.Map.Entry<CharSequence, V>> iterator ()
      {
        return mIterator;
      }
    };
  }

  private Iterator<Entry<CharSequence, V>> getPrefixSubtreeIteratorImpl (CharSequence pPrefix, boolean inverseSearch)
  {
    CheckForNull.check (pPrefix);
    final Iterator<Entry<CharSequence, Map.Entry<CharSequence, V>>> iterator = mData.getPrefixSubtreeIterator (
        pPrefix.toString ().toLowerCase (mLocale), inverseSearch).iterator ();

    return new Iterator<Map.Entry<CharSequence, V>> ()
    {

      public boolean hasNext ()
      {
        return iterator.hasNext ();
      }

      public java.util.Map.Entry<CharSequence, V> next ()
      {
        return iterator.next ().getValue ();
      }

      public void remove ()
      {
        iterator.remove ();
      }
    };
  }

  public Entry<CharSequence, V> predecessorEntry (Object keyObject)
  {
    Map.Entry<CharSequence, Map.Entry<CharSequence, V>> entry = mData.predecessorEntry (((CharSequence) keyObject)
        .toString ().toLowerCase (mLocale));
    return entry == null ? null : entry.getValue ();
  }

  public Entry<CharSequence, V> successorEntry (Object keyObject)
  {
    Map.Entry<CharSequence, Map.Entry<CharSequence, V>> entry = mData.successorEntry (((CharSequence) keyObject)
        .toString ().toLowerCase (mLocale));
    return entry == null ? null : entry.getValue ();
  }

  public int size ()
  {
    return mData.size ();
  }

  public boolean isEmpty ()
  {
    return mData.isEmpty ();
  }

  @Override
  public String toString ()
  {
    return entrySet ().toString ();
  }

  public boolean containsKey (Object key)
  {
    return mData.containsKey (key.toString ().toLowerCase (mLocale));
  }

  public boolean containsValue (Object pValue)
  {
    for (Map.Entry<CharSequence, V> entry : mData.values ())
    {
      if (entry.getValue ().equals (pValue))
        return true;
    }
    return false;
  }

  public V get (Object key)
  {
    String keyString = ((CharSequence) key).toString ().toLowerCase (mLocale);
    Map.Entry<CharSequence, V> result = mData.get (keyString);
    return result == null ? null : result.getValue ();
  }

  public V put (CharSequence key, V value)
  {
    CheckForNull.check (key, value);
    SimpleEntry<CharSequence, V> entry = new SimpleEntry<CharSequence, V> (key, value);
    Map.Entry<CharSequence, V> oldValue = mData.put (key.toString ().toLowerCase (mLocale), entry);
    return oldValue == null ? null : oldValue.getValue ();
  }

  public V remove (Object key)
  {
    String keyString = ((CharSequence) key).toString ().toLowerCase (mLocale);
    Map.Entry<CharSequence, V> entry = mData.remove (keyString);
    return entry == null ? null : entry.getValue ();
  }

  public void putAll (Map<? extends CharSequence, ? extends V> map)
  {
    for (Map.Entry<? extends CharSequence, ? extends V> entry : map.entrySet ())
    {
      put (entry.getKey (), entry.getValue ());
    }
  }

  public void clear ()
  {
    mData.clear ();
  }

  public Comparator<? super CharSequence> comparator ()
  {
    return mData.comparator ();
  }

  public SortedMap<CharSequence, V> subMap (CharSequence fromKey, CharSequence toKey)
  {
    return new TSTSubMapCaseInsensitive (fromKey, toKey);
  }

  public SortedMap<CharSequence, V> headMap (CharSequence toKey)
  {
    return new TSTSubMapCaseInsensitive (null, toKey);
  }

  public SortedMap<CharSequence, V> tailMap (CharSequence fromKey)
  {
    return new TSTSubMapCaseInsensitive (fromKey, null);
  }

  public CharSequence firstKey ()
  {
    if (mData.isEmpty ())
      return null;
    return mData.get (mData.firstKey ()).getKey ();
  }

  public CharSequence lastKey ()
  {
    if (mData.isEmpty ())
      return null;
    return mData.get (mData.lastKey ()).getKey ();
  }

  public Set<CharSequence> keySet ()
  {
    return new TSTKeySetCaseInsensitive ();
  }

  public Collection<V> values ()
  {
    Collection<Map.Entry<CharSequence, V>> mapValues = mData.values ();
    List<V> result = new ArrayList<V> (mapValues.size ());
    for (Map.Entry<CharSequence, V> entry : mapValues)
    {
      result.add (entry.getValue ());
    }
    return result;
  }

  public Set<Map.Entry<CharSequence, V>> entrySet ()
  {
    return new TSTEntrySetCaseInsensitive (null, null);
  }

  private class TSTEntrySetCaseInsensitive extends AbstractSet<Entry<CharSequence, V>> implements Serializable
  {
    private static final long                                        serialVersionUID = -291147638123041581L;
    private Set<Map.Entry<CharSequence, Map.Entry<CharSequence, V>>> mEntrySet;

    public TSTEntrySetCaseInsensitive (CharSequence fromKey, CharSequence toKey)
    {
      if (fromKey != null && toKey != null)
      {
        mEntrySet = mData.subMap (fromKey.toString ().toLowerCase (mLocale), toKey.toString ().toLowerCase (mLocale))
            .entrySet ();
      } else if (fromKey != null && toKey == null)
      {
        mEntrySet = mData.tailMap (fromKey.toString ().toLowerCase (mLocale)).entrySet ();
      } else if (fromKey == null && toKey != null)
      {
        mEntrySet = mData.headMap (toKey.toString ().toLowerCase (mLocale)).entrySet ();
      } else
      {
        mEntrySet = mData.entrySet ();
      }
    }

    @Override
    public Iterator<Map.Entry<CharSequence, V>> iterator ()
    {
      final Iterator<Entry<CharSequence, Map.Entry<CharSequence, V>>> entryIterator = mEntrySet.iterator ();

      return new Iterator<Map.Entry<CharSequence, V>> ()
      {

        public boolean hasNext ()
        {
          return entryIterator.hasNext ();
        }

        public java.util.Map.Entry<CharSequence, V> next ()
        {
          return entryIterator.next ().getValue ();
        }

        public void remove ()
        {
          entryIterator.remove ();
        }
      };
    }

    @Override
    public int size ()
    {
      return mEntrySet.size ();
    }

    @Override
    public boolean isEmpty ()
    {
      return mEntrySet.isEmpty ();
    }

    @Override
    @SuppressWarnings ({ "rawtypes", "unchecked" })
    public boolean contains (Object object)
    {
      if (!(object instanceof Map.Entry))
        return false;
      Map.Entry entry = (Map.Entry) object;
      if (entry.getKey () == null)
        return false;
      String lowerCaseKey = entry.getKey ().toString ().toLowerCase (mLocale);
      Map.Entry value = mData.get (lowerCaseKey);
      if (value == null)
        return false;
      return mEntrySet.contains (new SimpleEntry<CharSequence, Map.Entry<CharSequence, V>> (lowerCaseKey,
          new SimpleEntry (value.getKey (), entry.getValue ())));
    }

    @Override
    public boolean add (Map.Entry<CharSequence, V> e)
    {
      throw new UnsupportedOperationException (new String (
          "TernarySearchTreeMapCaseInsensitive's entry set: add() not allowed!"));
    }

    @Override
    @SuppressWarnings ({ "rawtypes", "unchecked" })
    public boolean remove (Object object)
    {
      if (!(object instanceof Map.Entry))
        return false;
      Map.Entry entry = (Map.Entry) object;
      if (entry.getKey () == null)
        return false;
      String lowerCaseKey = entry.getKey ().toString ().toLowerCase (mLocale);
      Map.Entry value = mData.get (lowerCaseKey);
      if (value == null)
        return false;
      return mEntrySet.remove (new SimpleEntry<CharSequence, Map.Entry<CharSequence, V>> (lowerCaseKey,
          new SimpleEntry (value.getKey (), entry.getValue ())));
    }

    @Override
    public boolean retainAll (Collection<?> collection)
    {
      return super.retainAll (getEntriesFromMap (collection));
    }

    @SuppressWarnings ({ "rawtypes", "unchecked" })
    private Collection<?> getEntriesFromMap (Collection<?> collection)
    {
      List list = new LinkedList ();
      for (Object obj : collection)
      {
        if (!(obj instanceof Map.Entry))
          continue;
        Map.Entry entry = (Map.Entry) obj;
        Map.Entry mapEntry = getEntry (entry.getKey ());
        if (mapEntry != null)
          list.add (mapEntry);
      }
      return list;
    }

    @Override
    public boolean removeAll (Collection<?> collection)
    {
      return super.removeAll (getEntriesFromMap (collection));
    }

    @Override
    public boolean addAll (Collection<? extends Map.Entry<CharSequence, V>> c)
    {
      throw new UnsupportedOperationException (new String (
          "TernarySearchTreeMapCaseInsensitive's entry set: addAll() not allowed!"));
    }

    @Override
    public void clear ()
    {
      mEntrySet.clear ();
    }
  }

  private class TSTSubMapCaseInsensitive extends AbstractMap<CharSequence, V> implements SortedMap<CharSequence, V>,
      Serializable
  {
    private static final long                                   serialVersionUID = 6887075934244545880L;

    private SortedMap<CharSequence, Map.Entry<CharSequence, V>> mSubMap;
    private CharSequence                                        mFromKey;
    private CharSequence                                        mExclusiveToKey;

    public TSTSubMapCaseInsensitive ()
    {
    }

    public TSTSubMapCaseInsensitive (CharSequence fromKey, CharSequence toKey)
    {
      mFromKey = fromKey;
      mExclusiveToKey = toKey;
      if (fromKey == null && toKey != null)
      {
        mSubMap = mData.headMap (toKey.toString ().toLowerCase (mLocale));
      } else if (toKey == null && fromKey != null)
      {
        mSubMap = mData.tailMap (fromKey.toString ().toLowerCase (mLocale));
      } else if (fromKey != null && toKey != null)
      {
        mSubMap = mData.subMap (fromKey.toString ().toLowerCase (mLocale), toKey.toString ().toLowerCase (mLocale));
      } else
      {
        throw new IllegalArgumentException ("fromKey is null and toKey is null");
      }
    }

    public Comparator<? super CharSequence> comparator ()
    {
      return mData.comparator ();
    }

    public V put (CharSequence key, V value)
    {
      CheckForNull.check (key, value);
      SimpleEntry<CharSequence, V> entry = new SimpleEntry<CharSequence, V> (key, value);
      Map.Entry<CharSequence, V> oldValue = mSubMap.put (key.toString ().toLowerCase (mLocale), entry);
      return oldValue == null ? null : oldValue.getValue ();
    }

    @Override
    public boolean containsKey (Object key)
    {
      if (key == null)
        return false;
      if (!(key instanceof CharSequence))
        return false;
      return mSubMap.containsKey (key.toString ().toLowerCase (mLocale));
    }

    public SortedMap<CharSequence, V> subMap (CharSequence fromKey, CharSequence toKey)
    {
      TSTSubMapCaseInsensitive result = new TSTSubMapCaseInsensitive ();
      result.mSubMap = mSubMap.subMap (fromKey.toString ().toLowerCase (mLocale),
          toKey.toString ().toLowerCase (mLocale));
      return result;
    }

    public SortedMap<CharSequence, V> headMap (CharSequence toKey)
    {
      TSTSubMapCaseInsensitive result = new TSTSubMapCaseInsensitive ();
      result.mSubMap = mSubMap.headMap (toKey.toString ().toLowerCase (mLocale));
      return result;
    }

    public SortedMap<CharSequence, V> tailMap (CharSequence fromKey)
    {
      TSTSubMapCaseInsensitive result = new TSTSubMapCaseInsensitive ();
      result.mSubMap = mSubMap.tailMap (fromKey.toString ().toLowerCase (mLocale));
      return result;
    }

    public CharSequence firstKey ()
    {
      CharSequence firstKey = mSubMap.firstKey ();
      return firstKey == null ? null : mSubMap.get (firstKey).getKey ();
    }

    public CharSequence lastKey ()
    {
      CharSequence lastKey = mSubMap.lastKey ();
      return lastKey == null ? null : mSubMap.get (lastKey).getKey ();
    }

    @Override
    public Set<Entry<CharSequence, V>> entrySet ()
    {
      return new TSTEntrySetCaseInsensitive (mFromKey, mExclusiveToKey);
    }
  }

  private class TSTKeySetCaseInsensitive extends TernarySearchTreeMap<Map.Entry<CharSequence, V>>.TSTKeySet
  {
    private static final long serialVersionUID = -4830094843193803479L;

    private CharSequence      mFromKey;
    private CharSequence      mExclusiveToKey;

    public TSTKeySetCaseInsensitive ()
    {
      mData.super ();
    }

    @Override
    public boolean contains (Object o)
    {
      return super.contains (((CharSequence) o).toString ().toLowerCase (mLocale));
    }

    @Override
    public boolean remove (Object o)
    {
      return super.remove (((CharSequence) o).toString ().toLowerCase (mLocale));
    }

    @Override
    public Iterator<CharSequence> iterator ()
    {
      final Iterator<Entry<CharSequence, Map.Entry<CharSequence, V>>> entryIterator;
      if (mFromKey != null || mExclusiveToKey != null)
      {
        entryIterator = mData.subMap (mFromKey, mExclusiveToKey).entrySet ().iterator ();
      } else
      {
        entryIterator = mData.entrySet ().iterator ();
      }

      return new Iterator<CharSequence> ()
      {
        public boolean hasNext ()
        {
          return entryIterator.hasNext ();
        }

        public CharSequence next ()
        {
          return entryIterator.next ().getValue ().getKey ();
        }

        public void remove ()
        {
          entryIterator.remove ();
        }
      };
    }
  }
}
