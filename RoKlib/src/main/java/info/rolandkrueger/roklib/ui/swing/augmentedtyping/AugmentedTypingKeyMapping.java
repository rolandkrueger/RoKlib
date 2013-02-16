/*
 * $Id: AugmentedTypingKeyMapping.java 224 2011-01-05 19:18:38Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 02.02.2007
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
package info.rolandkrueger.roklib.ui.swing.augmentedtyping;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a mapping of keyboard characters on arrays of related
 * characters from the IPA charset.
 * 
 * @author Roland Krueger
 */
public class AugmentedTypingKeyMapping
{
  private HashMap<Character, Character[]> mMapping;
  private boolean mIsInstalled = false;

  public AugmentedTypingKeyMapping ()
  {
    mMapping = new HashMap<Character, Character[]> ();
  }

  public void addMapping (Character keyBoardChar, Character[] mappedChars)
  {
    if (mIsInstalled)
      throw new IllegalStateException (
          "This mapping is already installed. Changes are not permitted in this state.");
    Character[] chars = new Character[mappedChars.length + 1];
    chars[0] = keyBoardChar;
    System.arraycopy (mappedChars, 0, chars, 1, mappedChars.length);
    mMapping.put (keyBoardChar, chars);
  }

  public Map<Character, Character[]> getData ()
  {
    return Collections.unmodifiableMap (mMapping);
  }

  public void setInstalled ()
  {
    mIsInstalled = true;
  }
}
