/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 24.06.2007
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
package org.roklib.collections;

import java.util.*;


public class TernarySearchTreeSet extends AbstractSet<CharSequence> implements SortedSet<CharSequence>,
        TernarySearchTreeQuery {
    private static final Object MARKER = new Object();

    private final TernarySearchTreeMapInterface<Object> data;

    public TernarySearchTreeSet() {
        this(false);
    }

    public TernarySearchTreeSet(boolean caseInsensitive) {
        if (caseInsensitive) {
            data = new TernarySearchTreeMapCaseInsensitive<Object>();
        } else {
            data = new TernarySearchTreeMap<Object>();
        }
    }

    public TernarySearchTreeSet(CharSequence[] values) {
        this(Arrays.asList(values), false);
    }

    public TernarySearchTreeSet(Collection<? extends CharSequence> values) {
        this(values, false);
    }

    public TernarySearchTreeSet(SortedSet<? extends CharSequence> values) {
        this((Collection<? extends CharSequence>) values, false);
    }

    public TernarySearchTreeSet(CharSequence[] values, boolean caseInsensitive) {
        this(Arrays.asList(values), caseInsensitive);
    }

    public TernarySearchTreeSet(Collection<? extends CharSequence> values, boolean caseInsensitive) {
        this(caseInsensitive);
        addAll(values);
    }

    public TernarySearchTreeSet(SortedSet<? extends CharSequence> values, boolean caseInsensitive) {
        this((Collection<? extends CharSequence>) values, caseInsensitive);
    }

    public Comparator<? super CharSequence> comparator() {
        // this SortedSet uses the natural ordering of its elements
        return null;
    }

    public CharSequence first() {
        return data.firstKey();
    }

    public SortedSet<CharSequence> headSet(CharSequence toElement) {
        return new TSTStringSetSubSet(data.headMap(toElement));
    }

    public CharSequence last() {
        return data.lastKey();
    }

    public SortedSet<CharSequence> subSet(CharSequence fromElement, CharSequence toElement) {
        return new TSTStringSetSubSet(data.subMap(fromElement, toElement));
    }

    public SortedSet<CharSequence> tailSet(CharSequence fromElement) {
        return new TSTStringSetSubSet(data.tailMap(fromElement));
    }

    public boolean add(CharSequence key) {
        return data.put(key, MARKER) != null;
    }

    public boolean contains(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
        return data.containsKey(object);
    }

    public Iterator<CharSequence> iterator() {
        return data.keySet().iterator();
    }

    public boolean remove(Object element) {
        if (element == null) {
            throw new NullPointerException();
        }
        return data.remove(element) != null;
    }

    public int size() {
        return data.size();
    }

    public int indexOf(CharSequence string) {
        return data.indexOf(string);
    }

    public CharSequence getElementAt(int index) {
        return data.getKeyAt(index);
    }

    public CharSequence predecessor(CharSequence forElement) {
        Map.Entry<CharSequence, Object> entry = data.predecessorEntry(forElement);
        return entry == null ? null : entry.getKey();
    }

    public CharSequence successor(CharSequence forElement) {
        Map.Entry<CharSequence, Object> entry = data.successorEntry(forElement);
        return entry == null ? null : entry.getKey();
    }

    public Iterable<CharSequence> getPrefixMatch(CharSequence prefix) {
        return data.getPrefixMatch(prefix);
    }

    public SortedSet<CharSequence> matchAlmost(CharSequence string, int distance, int lengthTolerance) {
        return data.matchAlmost(string, distance, lengthTolerance);
    }

    @Override
    public String toString() {
        return data.keySet().toString();
    }

    private class TSTStringSetSubSet extends AbstractSet<CharSequence> implements SortedSet<CharSequence> {
        private final SortedMap<CharSequence, Object> mParent;

        private TSTStringSetSubSet(SortedMap<CharSequence, Object> parent) {
            mParent = parent;
        }

        public Comparator<? super CharSequence> comparator() {
            return mParent.comparator();
        }

        public CharSequence first() {
            return mParent.firstKey();
        }

        public SortedSet<CharSequence> headSet(CharSequence toElement) {
            return new TSTStringSetSubSet(mParent.headMap(toElement));
        }

        public CharSequence last() {
            return mParent.lastKey();
        }

        public SortedSet<CharSequence> subSet(CharSequence fromElement, CharSequence toElement) {
            return new TSTStringSetSubSet(mParent.subMap(fromElement, toElement));
        }

        public SortedSet<CharSequence> tailSet(CharSequence fromElement) {
            return new TSTStringSetSubSet(mParent.tailMap(fromElement));
        }

        public boolean add(CharSequence e) {
            return mParent.put(e, MARKER) != null;
        }

        public boolean contains(Object object) {
            if (object == null) {
                throw new NullPointerException();
            }
            return super.contains(object);
        }

        public Iterator<CharSequence> iterator() {
            return mParent.keySet().iterator();
        }

        public boolean remove(Object element) {
            if (element == null) {
                throw new NullPointerException();
            }
            return super.remove(element);
        }

        public int size() {
            return mParent.size();
        }
    }
}
