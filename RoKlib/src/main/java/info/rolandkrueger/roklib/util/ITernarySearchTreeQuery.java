/*
 * $Id: ITernarySearchTreeQuery.java 249 2011-01-22 13:59:04Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 21.01.2011
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

import java.util.SortedSet;

public interface ITernarySearchTreeQuery
{
  /**
   * Returns the index of the specified string if it is stored in the Ternary
   * Search Tree. Otherwise -1 is returned. The indexing is the same as with
   * arrays, i.e. the first element in the tree has index 0 and the last element
   * has index <code>size()-1</code>.
   * 
   * @param string
   *          The string whose index is to be returned.
   * @return the string index or -1 if the string is not an element of the
   *         tree.
   */
  public abstract int indexOf (CharSequence string);
  
  public abstract CharSequence predecessor (CharSequence forElement);
  public abstract CharSequence successor (CharSequence forElement);  

  public abstract Iterable<CharSequence> getPrefixMatch (final CharSequence prefix);

  /**
   * Generates an ordered list of Strings that partially match the method's
   * argument. This can be useful for spell checking or the like. The method's
   * framework and its underlying algorithm is adopted from the above mentioned
   * article on <a href=" http://www.javaworld.com/javaworld/jw-02-2001
   * /jw-0216-ternary.html">Javaworld</a> by Wally Flint.
   * 
   * @param string
   *          The String's <code>toString()</code> method is used to generate a
   *          string representation of that String in order to find a set of
   *          partially matching String differing only by a number of
   *          <code>distance</code> characters.
   * @param distance
   *          The number of maximum characters that may be different from the
   *          characters of <code>string</code>.
   * @param lengthTolerance
   *          specifies by how many characters the length of the resulting set's
   *          strings may differ from the length of the search key.
   */
  public abstract SortedSet<CharSequence> matchAlmost (CharSequence string, int distance,
      int lengthTolerance);
}
