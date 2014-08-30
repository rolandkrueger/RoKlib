/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 29.01.2010
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
package org.roklib.state;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StateTest {
    private static class TestState extends State<TestState> {
        private static final long serialVersionUID = -3126104638124546290L;
        public final static StateValue<TestState> TEST_STATE_ON = new StateValue<TestState>("TEST_STATE_ON");
        public final static StateValue<TestState> TEST_STATE_OFF = new StateValue<TestState>("TEST_STATE_OFF");

        public TestState() {
            super();
        }

        public TestState(StateValue<TestState> defaultState) {
            super(defaultState);
        }
    }

    private static class TestStateDerived extends TestState {
        private static final long serialVersionUID = 3136674823264291904L;
        public final static StateValue<TestState> TEST_STATE_NONE = new StateValue<TestState>("TEST_STATE_NONE");
    }

    private TestState testObj;

    @Before
    public void setUp() {
        testObj = new TestState();
    }

    @Test
    public void testHasState() {
        assertTrue(testObj.hasState(null));
        testObj.setStateValue(TestState.TEST_STATE_ON);
        assertTrue(testObj.hasState(TestState.TEST_STATE_ON));
        assertFalse(testObj.hasState(TestState.TEST_STATE_OFF));
        testObj.setStateValue(TestStateDerived.TEST_STATE_NONE);
        assertTrue(testObj.hasState(TestStateDerived.TEST_STATE_NONE));
        assertFalse(testObj.hasState(TestStateDerived.TEST_STATE_ON));
    }

    @Test
    public void testGetState() {
        testObj.setStateValue(TestState.TEST_STATE_ON);
        assertEquals(TestState.TEST_STATE_ON, testObj.getStateValue());
        assertEquals(TestStateDerived.TEST_STATE_ON, testObj.getStateValue());
    }

    @Test
    public void testReset() {
        testObj.setStateValue(TestState.TEST_STATE_ON);
        testObj.reset();
        assertNull(testObj.getStateValue());
        testObj = new TestState(TestState.TEST_STATE_OFF);
        assertEquals(TestState.TEST_STATE_OFF, testObj.getStateValue());
        testObj.setStateValue(TestState.TEST_STATE_ON);
        assertEquals(TestState.TEST_STATE_ON, testObj.getStateValue());
        testObj.reset();
        assertEquals(TestState.TEST_STATE_OFF, testObj.getStateValue());
    }

    public void testLock() {
        String lockKey = new String();
        testObj.setStateValue(TestState.TEST_STATE_OFF);
        testObj.lock(lockKey);
        // locking again with the same key is silently ignored
        testObj.lock(lockKey);
        testObj.setStateValue(TestState.TEST_STATE_ON);
    }

    @Test(expected = IllegalStateException.class)
    public void testLock_Fail() {
        testObj.setStateValue(TestState.TEST_STATE_OFF);
        testObj.lock(new String());
        // try to lock again with a different key, this will fail
        testObj.lock(new String());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStateWithWrongKey() {
        testObj.setStateValue(TestState.TEST_STATE_OFF);
        testObj.lock(new String());
        testObj.setStateValue(TestState.TEST_STATE_ON, new Object());
    }

    @Test
    public void testSetStateWithLock() {
        String lockKey = new String();
        testObj.setStateValue(TestState.TEST_STATE_OFF);
        testObj.lock(lockKey);
        testObj.setStateValue(TestState.TEST_STATE_ON, lockKey);
        assertEquals(TestState.TEST_STATE_ON, testObj.getStateValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetStateWithLock_Fail() {
        testObj.setStateValue(TestState.TEST_STATE_OFF);
        testObj.lock(new String());
        testObj.setStateValue(TestState.TEST_STATE_ON);
    }

    @Test
    public void testUnlock() {
        String lockKey = new String();
        testObj.setStateValue(TestState.TEST_STATE_OFF);
        testObj.lock(lockKey);
        assertTrue(testObj.isLocked());
        testObj.unlock(lockKey);
        assertFalse(testObj.isLocked());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnlock_Fail() {
        testObj.setStateValue(TestState.TEST_STATE_OFF);
        testObj.lock(new String());
        // try to unlock with a different key, this will fail
        testObj.unlock(new String());
    }

    @Test
    public void testToString() {
        assertEquals("null", testObj.toString());
        testObj.setStateValue(TestState.TEST_STATE_OFF);
        assertEquals("TEST_STATE_OFF", testObj.toString());
    }
}
