/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 15.10.2003
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


import org.roklib.util.helper.CheckForNull;

import java.io.Serializable;
import java.util.*;

/*
 * TODO: 
 * - optimize TSTEntrySet.contains(): use TSTM.indexOf  
 * - add to documentation: null keys and values are not allowed
 * - note that prefix matching and spell checking is only possible with the map keys 
 * - matchAlmost() is not thread-safe/iterator is not thread-safe
 * - matchAlmost() should have the same return type as getPrefixSubtreeIterator()
 * - check for concurrent modifications while an iterator is active
 * - write a subtree iterator, which returns all entries which are NOT matching a 
 *   particular prefix, the same for match almost
 * - add option to define a descending sort order 
 */

/**
 * This class implements a ternary search tree that can be used to store and access a large amount of data efficiently
 * and with low memory requirements.<BR>
 * <BR>
 * The search tree's implementation is based on Wally Flint's article on Ternary Search Trees on the Javaworld webpage.
 * The article can be found <a href="http://www.javaworld.com/javaworld/jw-02-2001/jw-0216-ternary.html"
 * target="_blank">here</a>. The code of method <code>get</code> is adapted from a code example in that article.<BR>
 * <BR>
 * This class can be used to store either key/value mappings or simple string values. Note that in the first case the
 * key's string representation is used as the actual key. After a key/value pair has been stored in the tree the key
 * object's data except for its string representation is no longer known to the data structure. So it is not possible to
 * restore the key object from the search tree thereafter.<BR>
 * <BR>
 * If the search tree is used to store values without an assigned key the single-argument version of <code>put</code>
 * can be used. Note that as above only the value's string representation is preserved within the data structure.<BR>
 * <BR>
 * The search tree's iterator is best used if single values are stored in the data structure. It returns the tree's data
 * elements in sorted order. If an iterator is applied with the data structure handling key/value pairs, only the values
 * are returned sorted by their keys.
 *
 * @author Roland Krueger
 * @version CVS $Id: TernarySearchTreeMap.java 255 2011-01-26 16:06:32Z roland $
 */
public class TernarySearchTreeMap<V> extends AbstractMap<CharSequence, V> implements Serializable,
        TernarySearchTreeMapInterface<V> {
    private static final long serialVersionUID = 8532235443989332299L;

    private enum NodeType implements Serializable {
        NONE, LOKID, EQKID, HIKID
    }

    private TSTNode<V> mRootNode;
    private TreeSet<CharSequence> mMatchingKeys;                  // needed for method
    // matchAlmost()
    private int mLengthTolerance;               // needed as a global variable for
    // matchAlmost()
    private boolean mContainsEmptyStringKey = false;
    private V mEmptyStringKeyValue = null;
    private final Comparator<? super CharSequence> mComparator = null;

    // statistical data
    private int mNodeCount;

    /**
     * Private default constructor.
     */
    public TernarySearchTreeMap() {
        mRootNode = new TSTNode<V>();
    }

    public TernarySearchTreeMap(SortedMap<? extends CharSequence, V> map) {
        this((Map<? extends CharSequence, V>) map);
    }

    public TernarySearchTreeMap(Map<? extends CharSequence, V> map) {
        this();
        putAll(map);
    }

    public String getMapStructureAsString() {
        return mRootNode.toString();
    }

    public void clear() {
        mRootNode = new TSTNode<V>();
        mContainsEmptyStringKey = false;
        mEmptyStringKeyValue = null;
    }

    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        return get(key) != null;
    }

    public boolean containsValue(Object value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        if (mContainsEmptyStringKey) {
            if (mEmptyStringKeyValue != null && mEmptyStringKeyValue.equals(value))
                return true;
        }
        for (Map.Entry<CharSequence, V> entry : entrySet()) {
            if (entry.getValue().equals(value))
                return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return (size() == 0);
    }

    private Iterator<Entry<CharSequence, V>> getIterator(CharSequence fromKey, CharSequence toKey) {
        return new TSTIterator(fromKey, toKey);
    }

    public Set<CharSequence> keySet() {
        return new TSTKeySet();
    }

    @SuppressWarnings("unchecked")
    private int compare(CharSequence firstKey, CharSequence secondKey) {
        return (mComparator == null ? ((Comparable<CharSequence>) firstKey).compareTo(secondKey) : mComparator.compare(
                firstKey, secondKey));
    }

    public void putAll(Map<? extends CharSequence, ? extends V> t) {
        for (CharSequence element : t.keySet()) {
            put(element, t.get(element));
        }
    }

    public V remove(Object key) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        try {
            String keyString = key.toString();
            V oldValue;
            if (keyString.equals("") && mContainsEmptyStringKey) {
                mContainsEmptyStringKey = false;
                oldValue = mEmptyStringKeyValue;
                mEmptyStringKeyValue = null;
                return oldValue;
            }
            Stack<TSTStackNode<V>> stack = new Stack<TSTStackNode<V>>();
            TSTNode<V> currentNode = mRootNode;
            TSTStackNode<V> currentStackNode;
            int charIndex = 0;
            int keyStringLength = keyString.length();

            while (true) {
                if (currentNode == null) {
                    return null; // return null if key is not stored in map
                } else {
                    currentStackNode = new TSTStackNode<V>(currentNode);
                }
                stack.push(currentStackNode);
                char splitChar = currentNode.mSplitChar;
                char keyChar = keyString.charAt(charIndex);

                if (keyChar == splitChar) {
                    charIndex++;
                    if ((charIndex == keyStringLength) && (currentNode.mData != null)) {
                        // we've reached the node that stores a value
                        oldValue = currentNode.mData;
                        currentNode.mData = null;
                        while ((currentNode.mLokid == null) && (currentNode.mEqkid == null) && (currentNode.mHikid == null)) {
                            // delete current node as long as it has no children
                            if ((stack.size() == 0) && (mRootNode.mData != null)) {
                                // We've reached the root node. The map contains has only
                                // one element left residing in the root.
                                return oldValue;
                            } else if (stack.size() == 0) {
                                mRootNode.mSplitChar = '\0';
                                return oldValue;
                            }
                            currentNode = stack.pop().mNode; // get parent node
                            currentNode.mSubarrayLength--; // decrease the size of the node's subarray.
                            charIndex--; // take previous character for comparison

                            // we've completely erased all of key's characters from the tree.
                            // and delete the correct child node
                            if (charIndex < 0)
                                break;
                            if (currentNode.mEqkid != null && currentNode.mEqkid.mSubarrayLength == 0)
                                currentNode.mEqkid = null;
                            else if (currentNode.mHikid != null && currentNode.mHikid.mSubarrayLength == 0)
                                currentNode.mHikid = null;
                        }
                        while (stack.size() > 0) {
                            currentNode = stack.pop().mNode;
                            currentNode.mSubarrayLength--; // decrease the size of the node's
                            // subarray.
                        }
                        return oldValue;
                    } else {
                        currentNode = currentNode.mEqkid;
                    }
                } else if (keyChar < splitChar) {
                    currentNode = currentNode.mLokid;
                } else {
                    currentNode = currentNode.mHikid;
                }
            }
        } finally {
            if (isEmpty()) {
                clear();
            }
        }
    }

    public Collection<V> values() {
        return new TSTValuesCollection();
    }

    public Comparator<? super CharSequence> comparator() {
        return null;
    }

    public CharSequence firstKey() {
        return getKeyAt(0);
    }

    public CharSequence lastKey() {
        return getKeyAt(size() - 1);
    }

    public SortedMap<CharSequence, V> headMap(CharSequence toKey) {
        if (toKey == null) {
            throw new NullPointerException("this data structure does not allow null elements");
        }
        return new TSTSubMap(null, toKey);
    }

    public SortedMap<CharSequence, V> subMap(CharSequence fromKey, CharSequence toKey) {
        if (fromKey == null || toKey == null)
            throw new NullPointerException("One of the submap endpoints is null.");
        return new TSTSubMap(fromKey, toKey);
    }

    public SortedMap<CharSequence, V> tailMap(CharSequence fromKey) {
        if (fromKey == null) {
            throw new NullPointerException("this data structure does not allow null elements");
        }
        return new TSTSubMap(fromKey, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.roklib.util.TernarySearchTreeMapInterface#matchAlmost(java.lang.CharSequence, int, int)
     */
    public SortedSet<CharSequence> matchAlmost(CharSequence key, int distance, int lengthTolerance) {
        mMatchingKeys = new TreeSet<CharSequence>();
        this.mLengthTolerance = lengthTolerance;
        matchAlmost(key.toString(), 0, mRootNode, distance, new StringBuilder(), key.toString().length());
        TreeSet<CharSequence> result = mMatchingKeys;
        mMatchingKeys = null;
        return result;
    }

    private void matchAlmost(String key, int i, TSTNode<V> currentNode, int distance, StringBuilder prefix, int keyLength) {
        int nextDist;
        prefix.setLength(i);
        if ((currentNode == null) || (distance < 0) || (i >= keyLength + mLengthTolerance)) {
            return;
        }

        matchAlmost(key, i, currentNode.mLokid, distance, prefix, keyLength);

        if (i < keyLength)
            nextDist = (key.charAt(i) == currentNode.mSplitChar) ? distance : distance - 1;
        else
            nextDist = distance - 1;
        if (distance < 0)
            return;
        prefix.append(currentNode.mSplitChar);

        if ((Math.abs(keyLength - i - 1) <= mLengthTolerance) && (nextDist >= 0) && (currentNode.mData != null)) {
            mMatchingKeys.add(prefix.toString());
        }

        matchAlmost(key, i + 1, currentNode.mEqkid, nextDist, prefix, keyLength);
        matchAlmost(key, i, currentNode.mHikid, distance, prefix, keyLength);
    }

    public V get(Object key) {
        Entry<CharSequence, V> result = getEntry(key);
        if (result == null)
            return null;
        return result.getValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.roklib.util.TernarySearchTreeMapInterface#getEntry(java.lang.Object)
     */
    public Entry<CharSequence, V> getEntry(Object key) {
        CheckForNull.check(key);
        String keyString = key.toString();
        if (keyString.equals("") && mContainsEmptyStringKey)
            return new TSTEntry<CharSequence, V>(keyString, mEmptyStringKeyValue);
        else if (keyString.equals("") && !mContainsEmptyStringKey)
            return null;

        TSTNode<V> currentNode = mRootNode;
        int charIndex = 0;
        int keyStringLength = keyString.length();
        while (true) {
            if (currentNode == null) {
                return null; // given key is not contained in map
            }

            char splitChar = currentNode.mSplitChar;
            char keyChar = keyString.charAt(charIndex);

            if (keyChar == splitChar) {
                charIndex++;
                if (charIndex == keyStringLength) {
                    return currentNode.mData == null ? null : new TSTEntry<CharSequence, V>(keyString, currentNode.mData);
                } else {
                    currentNode = currentNode.mEqkid;
                }
            } else if (keyChar < splitChar) {
                currentNode = currentNode.mLokid;
            } else {
                currentNode = currentNode.mHikid;
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.roklib.util.TernarySearchTreeMapInterface#getValueAt(int)
     */
    public V getValueAt(int index) throws IndexOutOfBoundsException {
        return getElementAt(index).getValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.roklib.util.TernarySearchTreeMapInterface#getKeyAt(int)
     */
    public CharSequence getKeyAt(int index) throws IndexOutOfBoundsException {
        return getElementAt(index).getKey();
    }

    /**
     * Returns either the key at the specified position if <code>getKey</code> is true or the value otherwise.
     *
     * @param index
     * @return Object
     * @throws IndexOutOfBoundsException
     */
    private TSTEntry<CharSequence, V> getElementAt(int index) throws IndexOutOfBoundsException {
        if ((index < 0) || (index > this.size()))
            throw new IndexOutOfBoundsException();
        if (mContainsEmptyStringKey && index == 0) {
            return new TSTEntry<CharSequence, V>("", mEmptyStringKeyValue);
        }

        TSTNode<V> currentNode = mRootNode; // start at the root
        if (mContainsEmptyStringKey) {
            index--;
        }

        StringBuilder buf = new StringBuilder();
        int offset, loLength, eqLength, hiLength;

        index++; // we need an index numbering starting with 1
        while (true) {
            offset = 0;
            // get children's subarray length values
            if (currentNode.mLokid != null)
                loLength = currentNode.mLokid.mSubarrayLength;
            else
                loLength = 0;
            if (currentNode.mEqkid != null)
                eqLength = currentNode.mEqkid.mSubarrayLength;
            else
                eqLength = 0;
            if (currentNode.mHikid != null)
                hiLength = currentNode.mHikid.mSubarrayLength;
            else
                hiLength = 0;

            // check end condition
            if ((loLength + eqLength + hiLength == currentNode.mSubarrayLength - 1) && (index - loLength == 1))
                break;

            if (currentNode.mData != null) {
                // if the currently examined node contains data itself, it is part of
                // the subset it comprises. Then an additional offset is needed in the
                // following index calculations.
                offset = 1;
            }

            // now choose the next branch that contains the element with the specified
            // index.
            if (loLength + offset >= index) {
                currentNode = currentNode.mLokid;
            } else if (loLength + eqLength + offset >= index) {
                // adjust index to subarray boundaries
                index -= loLength + offset;
                buf.append(currentNode.mSplitChar);
                currentNode = currentNode.mEqkid;
            } else {
                index -= loLength + eqLength + offset;
                currentNode = currentNode.mHikid;
            }
        } // end of while

        return new TSTEntry<CharSequence, V>(buf.append(currentNode.mSplitChar).toString(), currentNode.mData);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.roklib.util.TernarySearchTreeMapInterface#indexOf(java.lang.CharSequence)
     */
    public int indexOf(CharSequence key) {
        String keyString = key.toString();
        if (mContainsEmptyStringKey && keyString.equals("")) {
            return 0;
        }

        TSTNode<V> currentNode = mRootNode;
        int charIndex = 0;
        int index = mContainsEmptyStringKey ? 1 : 0;
        int keyStringLength = keyString.length();
        int offset, loLength, eqLength;

        while (true) {
            if (currentNode == null)
                return -1;
            offset = 0;
            // get children's subarray length values
            if (currentNode.mLokid != null)
                loLength = currentNode.mLokid.mSubarrayLength;
            else
                loLength = 0;
            if (currentNode.mEqkid != null)
                eqLength = currentNode.mEqkid.mSubarrayLength;
            else
                eqLength = 0;

            if (currentNode.mData != null) {
                // if the currently examined node contains data itself, it is part of
                // the subset it comprises. Then an additional offset is needed in the
                // following index calculations.
                offset = 1;
            }

            if (keyString.charAt(charIndex) == currentNode.mSplitChar) {
                charIndex++;
                if ((charIndex == keyStringLength) && (currentNode.mData != null)) {
                    // we have reached the correct node
                    return index;
                } else if ((charIndex == keyStringLength) && (currentNode.mData == null)) {
                    // we have worked through the search string, but it's no valid key as
                    // the node we have
                    // reached so far contains no data
                    return -1;
                } else {
                    index += loLength + offset;
                    currentNode = currentNode.mEqkid;
                }
            } else if (keyString.charAt(charIndex) < currentNode.mSplitChar) {
                currentNode = currentNode.mLokid;
            } else {
                index += loLength + eqLength + offset;
                currentNode = currentNode.mHikid;
            }
        }
    }

    public CharSequence predecessor(CharSequence forElement) {
        Map.Entry<CharSequence, V> entry = predecessorEntry(forElement);
        return entry == null ? null : entry.getKey();
    }

    public CharSequence successor(CharSequence forElement) {
        Map.Entry<CharSequence, V> entry = successorEntry(forElement);
        return entry == null ? null : entry.getKey();
    }

    public Iterable<CharSequence> getPrefixMatch(final CharSequence prefix) {
        return new Iterable<CharSequence>() {
            private final Iterator<CharSequence> mIterator = new Iterator<CharSequence>() {
                final Iterator<Entry<CharSequence, V>> it = getPrefixSubtreeIterator(
                        prefix.toString())
                        .iterator();

                public boolean hasNext() {
                    return it.hasNext();
                }

                public CharSequence next() {
                    return it.next().getKey();
                }

                public void remove() {
                    it.remove();
                }
            };

            public Iterator<CharSequence> iterator() {
                return mIterator;
            }
        };
    }

    public Iterable<Entry<CharSequence, V>> getPrefixSubtreeIterator(CharSequence prefix) {
        return getPrefixSubtreeIterator(prefix, false);
    }

    public Iterable<Entry<CharSequence, V>> getPrefixSubtreeIterator(final CharSequence pPrefix,
                                                                     final boolean inverseSearch) {
        return new Iterable<Map.Entry<CharSequence, V>>() {
            private final Iterator<Map.Entry<CharSequence, V>> mIterator = getPrefixSubtreeIteratorImpl(pPrefix, inverseSearch);

            public Iterator<Map.Entry<CharSequence, V>> iterator() {
                return mIterator;
            }
        };
    }

    private Iterator<Entry<CharSequence, V>> getPrefixSubtreeIteratorImpl(CharSequence pPrefix, boolean inverseSearch) {
        CheckForNull.check(pPrefix);
        String prefix = pPrefix.toString();
        StringBuilder prefixBuilder = new StringBuilder();
        if (!inverseSearch && prefix.equals(""))
            return new TSTIterator();
        else if (inverseSearch && prefix.equals(""))
            return new TSTIterator(null, "", false, null, null);

        TSTNode<V> currentNode = mRootNode;
        int charIndex = 0;
        int prefixLength = prefix.length();

        while (charIndex != prefixLength) {
            if (currentNode == null) {
                if (inverseSearch)
                    return new TSTIterator();
                else
                    return new TSTIterator(null, "", false, null, null);
            }
            char splitChar = currentNode.mSplitChar;
            char currentChar = prefix.charAt(charIndex);

            if (currentChar == splitChar) {
                charIndex++;
                prefixBuilder.append(currentNode.mSplitChar);
                if ((charIndex == prefixLength) && (currentNode.mData != null)) {
                    return new TSTIterator(currentNode, currentNode.mEqkid, prefixBuilder.toString(), inverseSearch, null, null);
                } else if (charIndex == prefixLength) {
                    return new TSTIterator(currentNode.mEqkid, prefixBuilder.toString(), inverseSearch, null, null);
                } else {
                    currentNode = currentNode.mEqkid;
                }
            } else if (currentChar < splitChar) {
                currentNode = currentNode.mLokid;
            } else {
                currentNode = currentNode.mHikid;
            }
        }
        if (inverseSearch)
            return new TSTIterator();
        else
            return new TSTIterator(null, "", false, null, null);
    }

    public V put(CharSequence key, V value) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        if (key.equals("")) {
            mEmptyStringKeyValue = value;
            mContainsEmptyStringKey = true;
            return value;
        }
        V prevValue = null; // stores previous value associated with specified key
        TSTNode<V> currentNode = mRootNode;

        TSTNode<V> prevNode = new TSTNode<V>(); // node that was examined during
        // preceding iteration
        String keyString = key.toString();
        int keyStringLength = keyString.length();
        // needed to calculate the element's position within the ArrayList
        int charIndex = 0;
        // specifies which child has been selected in preceding iteration
        NodeType prevBranch = NodeType.NONE;
        boolean keyAlreadyInList = containsKey(key);
        char currentChar = keyString.charAt(charIndex);

        while (true) {
            if ((currentNode == null) || (mRootNode.mSplitChar == '\0')) {
                // we reached a leaf node, thus create new node
                if (currentNode == mRootNode) {
                    mRootNode.mSplitChar = currentChar;
                } else {
                    currentNode = new TSTNode<V>(currentChar);
                    switch (prevBranch) {
                        case HIKID:
                            prevNode.mHikid = currentNode;
                            break;
                        case EQKID:
                            prevNode.mEqkid = currentNode;
                            break;
                        case LOKID:
                            prevNode.mLokid = currentNode;
                            break;
                        default: // case NONE:
                    }
                }
            }
            if (currentChar == currentNode.mSplitChar) {
                ++charIndex;
                if (charIndex == keyStringLength) {
                    // we have reached the element's final destination
                    // put the data element into the tree
                    if (prevValue == null) {
                        prevValue = currentNode.mData;
                    }
                    currentNode.mData = value;
                    if (!keyAlreadyInList)
                        ++currentNode.mSubarrayLength; // increase the
                    // length of
                    // the
                    // current node's subarray.
                    return prevValue; // we're done...
                } else {
                    currentChar = keyString.charAt(charIndex);
                    if (!keyAlreadyInList)
                        ++currentNode.mSubarrayLength; // increase the
                    // length of
                    // the
                    // current node's subarray.
                    prevNode = currentNode;
                    currentNode = currentNode.mEqkid;
                    prevBranch = NodeType.EQKID;
                }
            } else if (currentChar < currentNode.mSplitChar) {
                if (!keyAlreadyInList)
                    ++currentNode.mSubarrayLength; // increase the
                // length of the
                // current
                // node's subarray.
                prevNode = currentNode;
                currentNode = currentNode.mLokid;
                prevBranch = NodeType.LOKID;
            } else {
                if (!keyAlreadyInList)
                    ++currentNode.mSubarrayLength; // increase the
                // length of the
                // current
                // node's subarray.
                prevNode = currentNode;
                currentNode = currentNode.mHikid;
                prevBranch = NodeType.HIKID;
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.roklib.util.TernarySearchTreeMapInterface#predecessor(java.lang.Object)
     */
    public Entry<CharSequence, V> predecessorEntry(Object keyObject) {
        CharSequence key = (CharSequence) keyObject;

        if (key.equals(""))
            return null; // the empty string is always the first entry in the map
        SortedMap<CharSequence, V> headMap = headMap(key);
        CharSequence prevKey = headMap.lastKey();
        return prevKey == null ? null : getEntry(prevKey);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.roklib.util.TernarySearchTreeMapInterface#successor(java.lang.Object)
     */
    public Entry<CharSequence, V> successorEntry(Object keyObject) {
        CharSequence key = (CharSequence) keyObject;

        Iterator<Map.Entry<CharSequence, V>> it = tailMap(key).entrySet().iterator();
        if (containsKey(key)) {
            it.next();
        }
        return it.hasNext() ? it.next() : null;
    }

    public int size() {
        if (mContainsEmptyStringKey)
            return mRootNode.mSubarrayLength + 1;
        return mRootNode.mSubarrayLength;
    }

    /**
     * @see java.util.Map#entrySet()
     */
    public Set<Entry<CharSequence, V>> entrySet() {
        return new TSTEntrySet(null, null);
    }

    private class TSTStackNode<NodeValue> implements Serializable {
        private static final long serialVersionUID = -9203260538139494037L;

        public TSTStackNode(TSTNode<NodeValue> node) {
            mNode = node;
        }

        public final TSTNode<NodeValue> mNode;
        public boolean mNeedsRecheck = false;
        public int mCharIndex;
        public boolean mRecheckHiKid = false;
    }

    private class TSTNode<NodeValue> implements Serializable {
        private static final long serialVersionUID = -692198357972673845L;

        public TSTNode<NodeValue> mLokid, mEqkid, mHikid;
        public NodeValue mData;
        public int mSubarrayLength = 0;                   // this field takes the number of data elements

        // that are stored in the subtree that is attached to the specific node.
        // The node itself is included if it stores a data element. This is
        // needed for accessing the individual data elements by an index.
        public char mSplitChar = '\0';

        public TSTNode() {
            super();
        }

        public TSTNode(char splitChar) {
            mSplitChar = splitChar;
        }

        @Override
        public String toString() {
            return toString(0);
        }

        private String toString(int level) {
            StringBuilder indentBuf = new StringBuilder();
            for (int i = 0; i < level; ++i)
                indentBuf.append("  ");
            String indent = indentBuf.toString();

            return String.format("{SL=%d; ch=%s; data=%s;\n%slo=%s,\n%seq=%s,\n%shi=%s}", mSubarrayLength, mSplitChar,
                    mData == null ? "" : mData.toString(), indent, mLokid == null ? "0" : mLokid.toString(level + 1), indent,
                    mEqkid == null ? "0" : mEqkid.toString(level + 1), indent,
                    mHikid == null ? "0" : mHikid.toString(level + 1));
        }
    } // End of private class TSTNode

    class TSTIterator implements Iterator<Entry<CharSequence, V>>, Serializable {
        private static final long serialVersionUID = -7270329414991887561L;

        private Stack<TSTItStackNode> mStack;
        private String mPrefix;
        private String mPrefixForInverseSearch;
        private StringBuilder mBuffer;
        private boolean mWentToNextItem = false;                // specifies whether or not tree
        // has been traversed to the next
        // item
        private TSTNode<V> mFirstElement = null;
        private String mPreviouslyReturnedKey;                       // key that was returned by the last
        // call of next(). Needed for
        // remove()
        private boolean mPrevKeyRemoved = false;                // needed for remove() to raise
        private boolean mProvideEmptyKeyValue = false;
        private boolean mIteratorEmpty = false;
        private boolean mInverseSearch;
        private int mPrefixForInverseSearchLength;
        private CharSequence mFromKey;
        private CharSequence mExclusiveToKey;

        public TSTIterator() {
            this(null, null);
        }

        public TSTIterator(CharSequence fromKey, CharSequence toKey) {
            this(mRootNode, "", false, fromKey, toKey);
        }

        public TSTIterator(TSTNode<V> startNode, String prefix, boolean inverse, CharSequence fromKey, CharSequence toKey) {
            this(null, startNode, prefix, inverse, fromKey, toKey);
        }

        public TSTIterator(TSTNode<V> firstElement, TSTNode<V> startNode, String prefix, boolean inverse,
                           CharSequence fromKey, CharSequence toKey) {
            if (fromKey != null && toKey != null && compare(fromKey, toKey) > 0)
                throw new IllegalArgumentException("Invalid parameters: fromKey > toKey");

            mFromKey = fromKey;
            mExclusiveToKey = toKey;
            if ("".equals(mExclusiveToKey))
                mIteratorEmpty = true;
            if (inverse) {
                mPrefixForInverseSearch = prefix;
                mPrefixForInverseSearchLength = prefix.length();
                init(mRootNode, "");
            } else {
                mFirstElement = firstElement;
                init(startNode, prefix);
            }
            mInverseSearch = inverse;
        }

        private void init(TSTNode<V> startNode, String prefix) {
            mBuffer = new StringBuilder();
            mStack = new Stack<TSTItStackNode>();
            mPrefix = "";
            if (startNode != null)
                mStack.push(new TSTItStackNode(startNode));
            if (prefix != null)
                mPrefix = prefix;
            if (mPrefix.equals("") && mContainsEmptyStringKey) {
                if (mFromKey == null || "".equals(mFromKey)) {
                    mProvideEmptyKeyValue = true;
                }
            }
        }

        private void goToNextElement() {
            TSTItStackNode currentNode;
            // go to next smallest element in TST
            currentNode = mStack.pop();

            while (true) {
                if ((currentNode.mNode.mData != null) && (!currentNode.mReturned)
                        && (currentNode.mVisited == TSTItStackNode.LOKID)) {
                    mStack.push(currentNode);
                    if (!mInverseSearch || mBuffer.length() + 1 < mPrefixForInverseSearchLength)
                        return;
                    if (!(mBuffer.toString() + currentNode.mNode.mSplitChar).startsWith(mPrefixForInverseSearch))
                        return;
                }
                if ((currentNode.mNode.mLokid != null) && (currentNode.mVisited == TSTItStackNode.NONE)) {
                    currentNode.mVisited = TSTItStackNode.LOKID;
                    mStack.push(currentNode);
                    currentNode = new TSTItStackNode(currentNode.mNode.mLokid);
                } else if ((currentNode.mNode.mLokid == null) && (currentNode.mVisited == TSTItStackNode.NONE))
                    currentNode.mVisited = TSTItStackNode.LOKID;
                else if ((currentNode.mNode.mEqkid != null) && (currentNode.mVisited < TSTItStackNode.LOEQKID)) {
                    mBuffer.append(currentNode.mNode.mSplitChar);
                    currentNode.mVisited = TSTItStackNode.LOEQKID;
                    mStack.push(currentNode);
                    currentNode = new TSTItStackNode(currentNode.mNode.mEqkid);
                } else if ((currentNode.mNode.mEqkid == null) && (currentNode.mVisited < TSTItStackNode.LOEQKID))
                    currentNode.mVisited = TSTItStackNode.LOEQKID;
                else if ((currentNode.mNode.mHikid != null) && (currentNode.mVisited < TSTItStackNode.ALL)) {
                    currentNode.mVisited = TSTItStackNode.ALL;
                    mStack.push(currentNode);
                    currentNode = new TSTItStackNode(currentNode.mNode.mHikid);
                } else if ((currentNode.mNode.mHikid == null) && (currentNode.mVisited < TSTItStackNode.ALL))
                    currentNode.mVisited = TSTItStackNode.ALL;
                else if (currentNode.mVisited == TSTItStackNode.ALL) {
                    if (mStack.empty())
                        return;
                    currentNode = mStack.pop();
                    if (currentNode.mVisited == TSTItStackNode.LOEQKID) {
                        mBuffer.setLength(mBuffer.length() - 1);
                    }
                }
            }
        }

        public TSTEntry<CharSequence, V> next() {
            if (!hasNext())
                throw new NoSuchElementException();
            mPrevKeyRemoved = false; // reset that variable

            if (mProvideEmptyKeyValue) {
                mProvideEmptyKeyValue = false;
                mPreviouslyReturnedKey = "";
                return new TSTEntry<CharSequence, V>("", mEmptyStringKeyValue);
            }

            if (mFirstElement != null) {
                V dummy = mFirstElement.mData;
                mFirstElement = null;
                mPreviouslyReturnedKey = mPrefix;
                return new TSTEntry<CharSequence, V>(mPrefix, dummy);
            }
            if (!mWentToNextItem) {
                goToNextElement();
            } else {
                mWentToNextItem = false;
            }
            TSTItStackNode stNode = mStack.peek();
            String key = new StringBuilder().append(mPrefix).append(mBuffer).append(stNode.mNode.mSplitChar).toString();
            stNode.mReturned = true;
            mPreviouslyReturnedKey = key;
            return new TSTEntry<CharSequence, V>(key, stNode.mNode.mData);
        }

        public boolean hasNext() {
            if (mIteratorEmpty)
                return false;
            if (mFirstElement != null || mProvideEmptyKeyValue)
                return true;
            if ((!mWentToNextItem) && (mStack.size() > 0)) {
                goToNextElement();
                mWentToNextItem = true;
            }
            mIteratorEmpty = mStack.isEmpty();
            String currentKey = null;
            if (!mIteratorEmpty) {
                currentKey = new StringBuilder().append(mPrefix).append(mBuffer).append(mStack.peek().mNode.mSplitChar)
                        .toString();
            }

            if (!mIteratorEmpty && mFromKey != null) {
                while (compare(mFromKey, currentKey) > 0) {
                    goToNextElement();
                    mIteratorEmpty = mStack.isEmpty();
                    if (mIteratorEmpty)
                        return false;
                    mStack.peek().mReturned = true;
                    currentKey = new StringBuilder().append(mPrefix).append(mBuffer).append(mStack.peek().mNode.mSplitChar)
                            .toString();
                }
            }

            if (!mIteratorEmpty && mExclusiveToKey != null) {
                if (compare(currentKey, mExclusiveToKey) >= 0)
                    return false;
            }
            return !mIteratorEmpty;
        }

        public void remove() throws IllegalStateException {
            if (mIteratorEmpty)
                throw new NoSuchElementException();
            if (!mPrevKeyRemoved) {
                if (mPreviouslyReturnedKey != null) {
                    TernarySearchTreeMap.this.remove(mPreviouslyReturnedKey);
                } else
                    throw new IllegalStateException("Iterator.next() must be called prior to Iterator.remove().");
            } else
                throw new IllegalStateException("Iterator's previously returned key has already been removed.");
            mPrevKeyRemoved = true;
        }

        private class TSTItStackNode implements Serializable {
            private static final long serialVersionUID = -5471137639654374101L;

            public final static int NONE = 0;
            public final static int LOKID = 1;
            public final static int LOEQKID = 2;
            public final static int ALL = 3;
            public final TSTNode<V> mNode;
            public int mVisited;                                // which child nodes have already been visited
            public boolean mReturned;                               // has data for specific node already been
            // returned

            public TSTItStackNode(TSTNode<V> node) {
                this(node, NONE);
            }

            public TSTItStackNode(TSTNode<V> node, int visited) {
                mReturned = false;
                mNode = node;
                mVisited = visited;
            }
        }
    } // End of private class TSTIterator

    private class TSTValuesCollection extends AbstractCollection<V> implements Serializable {
        private static final long serialVersionUID = 8889125197129391125L;

        private CharSequence mFromKey;
        private CharSequence mExclusiveToKey;

        public TSTValuesCollection() {
            this(null, null);
        }

        public TSTValuesCollection(CharSequence fromKey, CharSequence toKey) {
            if (fromKey != null && toKey != null && compare(fromKey, toKey) > 0)
                throw new IllegalArgumentException("Invalid parameters: fromKey > toKey");
            mFromKey = fromKey;
            mExclusiveToKey = toKey;
        }

        @Override
        public boolean add(V o) {
            throw new UnsupportedOperationException("TernarySearchTree's value collection: add() not allowed!");
        }

        @Override
        public boolean addAll(Collection<? extends V> c) {
            throw new UnsupportedOperationException("TernarySearchTree's value collection: addAll() not allowed!");
        }

        @Override
        public void clear() {
            if (mFromKey == null && mExclusiveToKey == null) {
                TernarySearchTreeMap.this.clear();
            } else {
                for (Iterator<Entry<CharSequence, V>> it = new TSTIterator(mFromKey, mExclusiveToKey); it.hasNext(); ) {
                    it.next();
                    it.remove();
                }
            }
        }

        @Override
        public boolean contains(Object o) {
            for (Map.Entry<CharSequence, V> entry : new TSTSubMap(mFromKey, mExclusiveToKey).entrySet()) {
                if (entry.getValue() == null && o == null)
                    return true;
                if (entry.getValue().equals(o))
                    return true;
            }
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            for (Object o : c) {
                if (!contains(o))
                    return false;
            }
            return true;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof Collection<?>))
                return false;
            Collection other = (Collection) obj;
            if (other.size() != size())
                return false;
            for (Object o : other) {
                if (!contains(o))
                    return false;
            }
            return true;
        }

        @Override
        public boolean isEmpty() {
            return size() == 0;
        }

        public Iterator<V> iterator() {
            final Iterator<Entry<CharSequence, V>> entryIterator = new TSTIterator(mFromKey, mExclusiveToKey);
            return new Iterator<V>() {
                public boolean hasNext() {
                    return entryIterator.hasNext();
                }

                public V next() {
                    return entryIterator.next().getValue();
                }

                public void remove() {
                    entryIterator.remove();
                }
            };
        }

        @Override
        public boolean remove(Object o) {
            for (Iterator<V> it = iterator(); it.hasNext(); ) {
                V value = it.next();
                if (value.equals(o)) {
                    it.remove();
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            boolean changed = false;
            for (Iterator<V> it = iterator(); it.hasNext(); ) {
                V value = it.next();
                if (c.contains(value)) {
                    it.remove();
                    changed = true;
                }
            }
            return changed;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            boolean changed = false;
            for (Iterator<V> it = iterator(); it.hasNext(); ) {
                V value = it.next();
                if (!c.contains(value)) {
                    it.remove();
                    changed = true;
                }
            }
            return changed;
        }

        public int size() {
            if (mFromKey == null && mExclusiveToKey == null) {
                return TernarySearchTreeMap.this.size();
            } else {
                int count = 0;
                for (Iterator<Entry<CharSequence, V>> it = new TSTIterator(mFromKey, mExclusiveToKey); it.hasNext(); ) {
                    it.next();
                    ++count;
                }
                return count;
            }
        }
    }

    class TSTKeySet extends AbstractSet<CharSequence> implements Set<CharSequence>, Serializable {
        private static final long serialVersionUID = -2892939426138635211L;

        private CharSequence mFromKey;
        private CharSequence mExclusiveToKey;

        public TSTKeySet() {
            this(null, null);
        }

        public TSTKeySet(CharSequence fromKey, CharSequence toKey) {
            assert (!(fromKey != null && toKey != null && compare(fromKey, toKey) > 0));

            mFromKey = fromKey;
            mExclusiveToKey = toKey;
        }

        public boolean add(CharSequence o) {
            throw new UnsupportedOperationException("TernarySearchTree's key set: add() not allowed!");
        }

        public boolean addAll(Collection<? extends CharSequence> c) {
            throw new UnsupportedOperationException("TernarySearchTree's key set: addAll() not allowed!");
        }

        public void clear() {
            if (mFromKey == null && mExclusiveToKey == null)
                TernarySearchTreeMap.this.clear();
            for (Iterator<Entry<CharSequence, V>> it = new TSTIterator(mFromKey, mExclusiveToKey); it.hasNext(); ) {
                it.next();
                it.remove();
            }
        }

        public boolean contains(Object o) {
            String key = o.toString();
            return !(mFromKey != null && compare(mFromKey, key) > 0) && !(mExclusiveToKey != null && (compare(key, mExclusiveToKey) > 0 || compare(key, mExclusiveToKey) == 0)) && TernarySearchTreeMap.this.containsKey(o);

        }

        public boolean containsAll(Collection<?> c) {
            for (Object o : c) {
                if (!contains(o))
                    return false;
            }
            return true;
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public Iterator<CharSequence> iterator() {
            final Iterator<Entry<CharSequence, V>> entryIterator = new TSTIterator(mFromKey, mExclusiveToKey);
            return new Iterator<CharSequence>() {
                public boolean hasNext() {
                    return entryIterator.hasNext();
                }

                public CharSequence next() {
                    return entryIterator.next().getKey();
                }

                public void remove() {
                    entryIterator.remove();
                }
            };
        }

        public boolean remove(Object o) {
            return contains(o) && TernarySearchTreeMap.this.remove(o) != null;
        }

        public boolean removeAll(Collection<?> c) {
            boolean changed = false;

            for (Object o : c) {
                if (contains(o) && remove(o))
                    changed = true;
            }

            return changed;
        }

        public boolean retainAll(Collection<?> c) {
            boolean changed = false;

            for (Iterator<CharSequence> it = iterator(); it.hasNext(); ) {
                CharSequence element = it.next();
                if (!c.contains(element)) {
                    changed = true;
                    remove(element);
                }
            }

            return changed;
        }

        public int size() {
            if ("".equals(mExclusiveToKey))
                return 0;
            if (mFromKey == null && mExclusiveToKey == null) {
                return TernarySearchTreeMap.this.size();
            } else {
                int count = 0;
                for (Iterator<Entry<CharSequence, V>> it = new TSTIterator(mFromKey, mExclusiveToKey); it.hasNext(); ) {
                    it.next();
                    ++count;
                }
                return count;
            }
        }
    }

    private class TSTEntrySet extends AbstractSet<Entry<CharSequence, V>> implements Set<Entry<CharSequence, V>>,
            Serializable {
        private static final long serialVersionUID = -7845332141119069118L;

        private CharSequence mFromKey;
        private CharSequence mExclusiveToKey;

        public TSTEntrySet(CharSequence fromKey, CharSequence toKey) {
            if (fromKey != null && toKey != null && compare(fromKey, toKey) > 0)
                throw new IllegalArgumentException("Invalid parameters: fromKey > toKey");
            mFromKey = fromKey;
            mExclusiveToKey = toKey;
        }

        @Override
        public boolean addAll(Collection<? extends java.util.Map.Entry<CharSequence, V>> c) {
            throw new UnsupportedOperationException("TernarySearchTreeMap's entry set: addAll() not allowed!");
        }

        public boolean add(Entry<CharSequence, V> o) {
            throw new UnsupportedOperationException("TernarySearchTreeMap's entry set: add() not allowed!");
        }

        public void clear() {
            for (Iterator<Entry<CharSequence, V>> it = iterator(); it.hasNext(); ) {
                it.next();
                it.remove();
            }
        }

        @SuppressWarnings("unchecked")
        public boolean contains(Object entry) {
            return entry instanceof Entry<?, ?> && containsEntry((Entry<CharSequence, V>) entry);
        }

        private boolean containsEntry(Map.Entry<CharSequence, V> entry) {
            if (mFromKey != null && compare(mFromKey, entry.getKey()) > 0)
                return false;
            if (mExclusiveToKey != null
                    && (compare(entry.getKey(), mExclusiveToKey) > 0 || compare(entry.getKey(), mExclusiveToKey) == 0))
                return false;
            if (!containsKey(entry.getKey()))
                return false;
            V value = TernarySearchTreeMap.this.get(entry.getKey());
            return !(value == null || !value.equals(entry.getValue()));
        }

        // public boolean containsAll (Collection<?> c)
        // {
        // for (Object o : c)
        // {
        // if (! contains (o)) return false;
        // }
        // return true;
        // }

        // public boolean isEmpty ()
        // {
        // return size () == 0;
        // }

        public Iterator<Entry<CharSequence, V>> iterator() {
            return new TSTIterator(mFromKey, mExclusiveToKey);
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<CharSequence, V> entry = (Map.Entry<CharSequence, V>) o;
            if (mFromKey != null && compare(mFromKey, entry.getKey()) > 0)
                return false;
            if (mExclusiveToKey != null
                    && (compare(entry.getKey(), mExclusiveToKey) > 0 || compare(entry.getKey(), mExclusiveToKey) == 0))
                return false;
            Object key = ((Map.Entry) o).getKey();
            Map.Entry mapEntry = getEntry(key);
            return mapEntry != null && mapEntry.getValue().equals(entry.getValue()) && TernarySearchTreeMap.this.remove(key) != null;
        }

        // public boolean removeAll (Collection<?> c)
        // {
        // boolean changed = false;
        // for (Object o : c)
        // {
        // if (remove (o)) changed = true;
        // }
        // return changed;
        // }

        // @SuppressWarnings ("rawtypes")
        // public boolean retainAll (Collection<?> c)
        // {
        // boolean changed = false;
        // for (Map.Entry element : this)
        // {
        // if (! c.contains (element))
        // {
        // changed = true;
        // remove (element);
        // }
        // }
        // return changed;
        // }

        public int size() {
            if (mFromKey == null && mExclusiveToKey == null)
                return TernarySearchTreeMap.this.size();
            int count = 0;
            for (Iterator<Entry<CharSequence, V>> it = iterator(); it.hasNext(); ) {
                it.next();
                ++count;
            }
            return count;
        }
    } // End of private class TSTEntrySet

    private class TSTEntry<EntryK extends CharSequence, EntryV extends V> implements Map.Entry<EntryK, EntryV>,
            Serializable {
        private static final long serialVersionUID = -5785604459208599077L;

        private final EntryK mKey;
        private EntryV mValue;

        public TSTEntry(EntryK key, EntryV value) {
            mKey = key;
            mValue = value;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Map.Entry))
                return false;
            Map.Entry entry = (Map.Entry) other;
            return (entry.getKey() == null ? getKey() == null : entry.getKey().equals(getKey()))
                    && (entry.getValue() == null ? getValue() == null : entry.getValue().equals(getValue()));
        }

        public EntryK getKey() {
            return mKey;
        }

        public EntryV getValue() {
            return mValue;
        }

        @Override
        public int hashCode() {
            return (getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
        }

        public EntryV setValue(EntryV value) {
            CheckForNull.check(value);
            EntryV oldValue = mValue;
            mValue = value;
            TernarySearchTreeMap.this.put(mKey, value);
            return oldValue;
        }

        @Override
        public String toString() {
            return mKey.toString() + "=" + mValue.toString();
        }
    } // End of private class TSTEntry

    private class TSTSubMap extends AbstractMap<CharSequence, V> implements SortedMap<CharSequence, V>, Serializable {
        private static final long serialVersionUID = -6603551293795525865L;

        private CharSequence mFromKey;
        private CharSequence mExclusiveToKey;

        public TSTSubMap(CharSequence fromKey, CharSequence toKey) {
            if (fromKey != null && toKey != null && compare(fromKey, toKey) > 0)
                throw new IllegalArgumentException("Invalid parameters: fromKey > toKey");

            mFromKey = fromKey;
            mExclusiveToKey = toKey;
        }

        private boolean isNotInRange(Object keyObj) {
            CharSequence key = (CharSequence) keyObj;
            if (mContainsEmptyStringKey && "".equals(key.toString())) {
                if ((mFromKey != null && !"".equals(mFromKey)) || (mExclusiveToKey != null && "".equals(mExclusiveToKey))) {
                    return true;
                }
                if (mFromKey != null && "".equals(mFromKey)) {
                    return false;
                }
            }
            return mFromKey != null && compare(key, mFromKey) < 0 || mExclusiveToKey != null && compare(key, mExclusiveToKey) >= 0;
        }

        public Comparator<? super CharSequence> comparator() {
            return TernarySearchTreeMap.this.comparator();
        }

        public CharSequence firstKey() {
            Iterator<Entry<CharSequence, V>> it = new TSTIterator(mFromKey, mExclusiveToKey);
            if (it.hasNext())
                return it.next().getKey();
            return null;
        }

        public CharSequence lastKey() {
            return lastKey(true);
        }

        private CharSequence lastKey(boolean throwException) {
            Iterator<Entry<CharSequence, V>> it = new TSTIterator(mFromKey, mExclusiveToKey);
            Entry<CharSequence, V> entry = null;
            while (it.hasNext()) {
                entry = it.next();
            }
            return entry == null ? null : entry.getKey();
        }

        public SortedMap<CharSequence, V> headMap(CharSequence toKey) {
            if (mExclusiveToKey != null && compare(toKey, mExclusiveToKey) > 0)
                throw new IllegalArgumentException("toKey out of range");

            return new TSTSubMap(mFromKey, toKey);
        }

        public SortedMap<CharSequence, V> subMap(CharSequence fromKey, CharSequence toKey) {
            if (mFromKey != null && compare(mFromKey, fromKey) > 0)
                throw new IllegalArgumentException("fromKey out of range");
            if (mExclusiveToKey != null && compare(toKey, mExclusiveToKey) > 0)
                throw new IllegalArgumentException("toKey out of range");

            return new TSTSubMap(fromKey, toKey);
        }

        public SortedMap<CharSequence, V> tailMap(CharSequence fromKey) {
            if (mFromKey != null && compare(mFromKey, fromKey) > 0)
                throw new IllegalArgumentException("fromKey out of range");
            return new TSTSubMap(fromKey, mExclusiveToKey);
        }

        public void clear() {
            for (CharSequence key : this.keySet()) {
                TernarySearchTreeMap.this.remove(key);
            }
        }

        public boolean containsKey(Object key) {
            return !isNotInRange(key) && (TernarySearchTreeMap.this.containsKey(key));
        }

        public boolean containsValue(Object value) {
            for (Map.Entry<CharSequence, V> entry : entrySet()) {
                if (entry.getValue().equals(value))
                    return true;
            }
            return false;
        }

        public V get(Object key) {
            if (isNotInRange(key))
                return null;
            return TernarySearchTreeMap.this.get(key);
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public V put(CharSequence key, V value) {
            if (isNotInRange(key))
                throw new IllegalArgumentException("The key " + key + " is not within the bounds of this submap.");
            return TernarySearchTreeMap.this.put(key, value);
        }

        public void putAll(Map<? extends CharSequence, ? extends V> t) {
            for (CharSequence key : t.keySet()) {
                put(key, t.get(key));
            }
        }

        public V remove(Object key) {
            if (key == null)
                throw new NullPointerException("key is null");
            if (!containsKey(key))
                return null; // check if the key is in range
            return TernarySearchTreeMap.this.remove(key);
        }

        public int size() {
            if (TernarySearchTreeMap.this.isEmpty() || "".equals(mExclusiveToKey))
                return 0;
            int count = 0;
            for (Iterator<Entry<CharSequence, V>> it = new TSTIterator(mFromKey, mExclusiveToKey); it.hasNext(); ) {
                it.next();
                ++count;
            }
            return count;
        }

        public Set<Map.Entry<CharSequence, V>> entrySet() {
            return new TSTEntrySet(mFromKey, mExclusiveToKey);
        }

        public Set<CharSequence> keySet() {
            return new TSTKeySet(mFromKey, mExclusiveToKey);
        }

        public Collection<V> values() {
            return new TSTValuesCollection(mFromKey, mExclusiveToKey);
        }
    } // End of class TernarySearchTreeMap.SubMap
} // End of class TernarySearchTreeMap

