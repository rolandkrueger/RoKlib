/*
 * $Id: TernarySearchTreeSet.java 254 2011-01-25 18:48:50Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 24.06.2007
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
package info.rolandkrueger.roklib.util;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

public class TernarySearchTreeSet extends AbstractSet<CharSequence> 
  implements SortedSet<CharSequence>, ITernarySearchTreeQuery
{
  private static Object MARKER = new Object ();

  private ITernarySearchTreeMap<Object> mData;

  public TernarySearchTreeSet ()
  {
    this (false);
  }
  
  public TernarySearchTreeSet (boolean caseInsensitive)
  {
    if (caseInsensitive)
    {
      mData = new TernarySearchTreeMapCaseInsensitive<Object> ();
    } else
    {
      mData = new TernarySearchTreeMap<Object> ();
    }      
  }

  public TernarySearchTreeSet (CharSequence[] values)
  {
    this (Arrays.asList (values), false);
  }

  public TernarySearchTreeSet (Collection<? extends CharSequence> values)
  {
    this (values, false);
  }
  
  public TernarySearchTreeSet (SortedSet<? extends CharSequence> values)
  {
    this ((Collection<? extends CharSequence>) values, false);
  }
  
  public TernarySearchTreeSet (CharSequence[] values, boolean caseInsensitive)
  {
    this (Arrays.asList (values), caseInsensitive);
  }
  
  public TernarySearchTreeSet (Collection<? extends CharSequence> values, boolean caseInsensitive)
  {
    this (caseInsensitive);
    addAll (values);
  }
  
  public TernarySearchTreeSet (SortedSet<? extends CharSequence> values, boolean caseInsensitive)
  {
    this ((Collection<? extends CharSequence>) values, caseInsensitive);
  }
  
  public Comparator<? super CharSequence> comparator ()
  {
    // this SortedSet uses the natural ordering of its elements
    return null;
  }

  public CharSequence first ()
  {
    return mData.firstKey ();
  }

  public SortedSet<CharSequence> headSet (CharSequence toElement)
  {
    return new TSTStringSetSubSet (mData.headMap (toElement));
  }

  public CharSequence last ()
  {
    return mData.lastKey ();
  }

  public SortedSet<CharSequence> subSet (CharSequence fromElement, CharSequence toElement)
  {
    return new TSTStringSetSubSet (mData.subMap (fromElement, toElement));
  }

  public SortedSet<CharSequence> tailSet (CharSequence fromElement)
  {
    return new TSTStringSetSubSet (mData.tailMap (fromElement));
  }

  public boolean add (CharSequence key)
  {
    return mData.put (key, MARKER) != null;
  }

  public boolean contains (Object object)
  {
    if (object == null)
    {
      throw new NullPointerException ();
    }
    return mData.containsKey (object);
  }

  public Iterator<CharSequence> iterator ()
  {
    return mData.keySet ().iterator ();
  }

  public boolean remove (Object element)
  {
    if (element == null)
    {
      throw new NullPointerException ();
    }
    return mData.remove (element) != null;
  }

  public int size ()
  {
    return mData.size ();
  }

  public int indexOf (CharSequence string)
  {
    return mData.indexOf (string);
  }
  
  public CharSequence getElementAt (int index)
  {
    return mData.getKeyAt (index);
  }
  
  public CharSequence predecessor (CharSequence forElement)
  {
    Map.Entry<CharSequence, Object> entry = mData.predecessorEntry (forElement); 
    return entry == null ? null : entry.getKey ();
  }
  
  public CharSequence successor (CharSequence forElement)
  {
    Map.Entry<CharSequence, Object> entry = mData.successorEntry (forElement);
    return entry == null ? null : entry.getKey ();
  }

  public Iterable<CharSequence> getPrefixMatch (CharSequence prefix)
  {
    return mData.getPrefixMatch (prefix);
  }

  public SortedSet<CharSequence> matchAlmost (CharSequence string, int distance, int lengthTolerance)
  {
    return mData.matchAlmost (string, distance, lengthTolerance);
  }
  
  @Override
  public String toString ()
  {
    return mData.keySet ().toString ();
  } 

  private class TSTStringSetSubSet extends AbstractSet<CharSequence> implements SortedSet<CharSequence>
  {
    private SortedMap<CharSequence, Object> mParent;

    private TSTStringSetSubSet (SortedMap<CharSequence, Object> parent)
    {
      mParent = parent;
    }

    public Comparator<? super CharSequence> comparator ()
    {
      return mParent.comparator ();
    }

    public CharSequence first ()
    {
      return mParent.firstKey ();
    }

    public SortedSet<CharSequence> headSet (CharSequence toElement)
    {
      return new TSTStringSetSubSet (mParent.headMap (toElement));
    }

    public CharSequence last ()
    {
      return mParent.lastKey ();
    }

    public SortedSet<CharSequence> subSet (CharSequence fromElement, CharSequence toElement)
    {
      return new TSTStringSetSubSet (mParent.subMap (fromElement, toElement));
    }

    public SortedSet<CharSequence> tailSet (CharSequence fromElement)
    {
      return new TSTStringSetSubSet (mParent.tailMap (fromElement));
    }

    public boolean add (CharSequence e)
    {
      return mParent.put (e, MARKER) != null;
    }

    public boolean contains (Object object)
    {
      if (object == null)
      {
        throw new NullPointerException ();
      }
      return super.contains (object);
    }

    public Iterator<CharSequence> iterator ()
    {
      return mParent.keySet ().iterator ();
    }

    public boolean remove (Object element)
    {
      if (element == null)
      {
        throw new NullPointerException ();
      }
      return super.remove (element);
    }

    public int size ()
    {
      return mParent.size ();
    } 
  }
}
