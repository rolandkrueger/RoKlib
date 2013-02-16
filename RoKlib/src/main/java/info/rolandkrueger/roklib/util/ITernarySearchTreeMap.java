/*
 * $Id: ITernarySearchTreeMap.java 253 2011-01-25 16:16:31Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 14.11.2010
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

import java.util.SortedMap;

public interface ITernarySearchTreeMap<V> extends SortedMap<CharSequence, V>, ITernarySearchTreeQuery
{
  public abstract Entry<CharSequence, V> getEntry (Object key);

  public abstract V getValueAt (int index);

  public abstract CharSequence getKeyAt (int index);

  public abstract Iterable<Entry<CharSequence, V>> getPrefixSubtreeIterator (CharSequence prefix);

  public abstract Iterable<Entry<CharSequence, V>> getPrefixSubtreeIterator (CharSequence pPrefix,
      boolean inverseSearch);

  public abstract Entry<CharSequence, V> predecessorEntry (Object keyObject);

  public abstract Entry<CharSequence, V> successorEntry (Object keyObject);
}