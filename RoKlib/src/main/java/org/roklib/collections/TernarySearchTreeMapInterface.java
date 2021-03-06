/*
 * Copyright (C) 2007 - 2013 Roland Krüger
 * Created on 14.11.2010
 *
 * Author: Roland Krüger (www.rolandkrueger.info)
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
package org.roklib.collections;

import java.util.SortedMap;

interface TernarySearchTreeMapInterface<V> extends SortedMap<CharSequence, V>, TernarySearchTreeQuery {
    public abstract Entry<CharSequence, V> getEntry(Object key);

    public abstract V getValueAt(int index);

    public abstract CharSequence getKeyAt(int index);

    public abstract Iterable<Entry<CharSequence, V>> getPrefixSubtreeIterator(CharSequence prefix);

    public abstract Iterable<Entry<CharSequence, V>> getPrefixSubtreeIterator(CharSequence pPrefix, boolean inverseSearch);

    public abstract Entry<CharSequence, V> predecessorEntry(Object keyObject);

    public abstract Entry<CharSequence, V> successorEntry(Object keyObject);
}