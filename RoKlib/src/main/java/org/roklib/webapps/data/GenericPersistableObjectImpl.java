/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 27.01.2010
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
package org.roklib.webapps.data;

public class GenericPersistableObjectImpl<KeyClass> implements GenericPersistableObject<KeyClass> {
    private static final long serialVersionUID = 1820908905463077361L;

    private KeyClass key;

    public KeyClass getKey() {
        return key;
    }

    public void setKey(KeyClass key) {
        this.key = key;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (key == null)
            return false;
        if (obj == null)
            return false;
        if (obj == this)
            return true;

        if (obj instanceof GenericPersistableObjectImpl) {
            GenericPersistableObjectImpl other = (GenericPersistableObjectImpl) obj;
            return key.equals(other.key);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (key == null)
            return -1;
        return key.hashCode();
    }
}
