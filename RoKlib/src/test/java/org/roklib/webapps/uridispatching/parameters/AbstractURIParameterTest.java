/*
 * Copyright (C) 2007 - 2010 Roland Krueger
 * Created on 07.03.2010
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
package org.roklib.webapps.uridispatching.parameters;

import org.junit.Before;
import org.junit.Test;
import org.roklib.webapps.uridispatching.AbstractURIActionCommand;
import org.roklib.webapps.uridispatching.AbstractURIActionHandler;
import org.roklib.webapps.uridispatching.TURIActionCommand;
import org.roklib.webapps.uridispatching.TURIActionHandler;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public abstract class AbstractURIParameterTest<V extends Serializable> {
    private AbstractURIParameter<V> mTestObj;

    public abstract AbstractURIParameter<V> getTestURIParameter();

    public abstract V getTestValue();

    @Test
    public abstract void testGetSingleValueCount();

    @Test
    public abstract void testGetParameterNames();

    @Before
    public void setUp() {
        mTestObj = getTestURIParameter();
    }

    @Test
    public void testSetGetValue() {
        V value = getTestValue();
        mTestObj.setValue(value);
        assertEquals(value, mTestObj.getValue());
    }

    @Test
    public void testHasValue() {
        V value = getTestValue();
        assertFalse(mTestObj.hasValue());
        mTestObj.setValue(value);
        assertTrue(mTestObj.hasValue());
    }

    @Test
    public void testClearValue() {
        mTestObj.setValue(getTestValue());
        mTestObj.clearValue();
        assertNull(mTestObj.getValue());
    }

    @Test
    public void testSetValueAndParameterizeURLHandler() {
        TURLParameter testObj = new TURLParameter();

        testObj.setValueAndParameterizeURIHandler("value", new TURIActionHandler("action", new TURIActionCommand()));
        assertEquals("value", testObj.getValue());
        assertTrue(testObj.parameterized());
    }

    @Test
    public void testSetOptional() {
        mTestObj.setOptional(true);
        assertTrue(mTestObj.isOptional());
        mTestObj.setOptional(false);
        assertFalse(mTestObj.isOptional());
    }

    @SuppressWarnings("serial")
    private static class TURLParameter extends AbstractURIParameter<String> {
        boolean parameterized = false;

        public boolean parameterized() {
            return parameterized;
        }

        @Override
        protected boolean consumeImpl(Map<String, List<String>> parameters) {
            return true;
        }

        public AbstractURIActionCommand getErrorCommandIfInvalid() {
            return null;
        }

        public void parameterizeURIHandler(AbstractURIActionHandler handler) {
            parameterized = true;
        }

        @Override
        protected boolean consumeListImpl(String[] values) {
            return true;
        }

        public int getSingleValueCount() {
            return 1;
        }

        public List<String> getParameterNames() {
            return null;
        }
    }
}