/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 21.01.2011
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

import java.util.SortedSet;

interface ITernarySearchTreeQuery {
    /**
     * Returns the index of the specified string if it is stored in the Ternary Search Tree. Otherwise -1 is returned. The
     * indexing is the same as with arrays, i.e. the first element in the tree has index 0 and the last element has index
     * <code>size()-1</code>.
     *
     * @param string The string whose index is to be returned.
     * @return the string index or -1 if the string is not an element of the tree.
     */
    public abstract int indexOf(CharSequence string);

    public abstract CharSequence predecessor(CharSequence forElement);

    public abstract CharSequence successor(CharSequence forElement);

    public abstract Iterable<CharSequence> getPrefixMatch(final CharSequence prefix);

    /**
     * Generates an ordered list of Strings that partially match the method's argument. This can be useful for spell
     * checking or the like. The method's framework and its underlying algorithm is adopted from the above mentioned
     * article on <a href=" http://www.javaworld.com/javaworld/jw-02-2001 /jw-0216-ternary.html">Javaworld</a> by Wally
     * Flint.
     *
     * @param string          The String's <code>toString()</code> method is used to generate a string representation of that String in
     *                        order to find a set of partially matching String differing only by a number of <code>distance</code>
     *                        characters.
     * @param distance        The number of maximum characters that may be different from the characters of <code>string</code>.
     * @param lengthTolerance specifies by how many characters the length of the resulting set's strings may differ from the length of
     *                        the search key.
     */
    public abstract SortedSet<CharSequence> matchAlmost(CharSequence string, int distance, int lengthTolerance);
}
