/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 02.02.2007
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
package org.roklib.swing.augmentedtyping;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a mapping of keyboard characters on arrays of related characters from the IPA charset.
 *
 * @author Roland Krueger
 */
public class AugmentedTypingKeyMapping {
    private final HashMap<Character, Character[]> mMapping;
    private boolean mIsInstalled = false;

    public AugmentedTypingKeyMapping() {
        mMapping = new HashMap<Character, Character[]>();
    }

    public void addMapping(Character keyBoardChar, Character[] mappedChars) {
        if (mIsInstalled)
            throw new IllegalStateException("This mapping is already installed. Changes are not permitted in this state.");
        Character[] chars = new Character[mappedChars.length + 1];
        chars[0] = keyBoardChar;
        System.arraycopy(mappedChars, 0, chars, 1, mappedChars.length);
        mMapping.put(keyBoardChar, chars);
    }

    public Map<Character, Character[]> getData() {
        return Collections.unmodifiableMap(mMapping);
    }

    public void setInstalled() {
        mIsInstalled = true;
    }
}
