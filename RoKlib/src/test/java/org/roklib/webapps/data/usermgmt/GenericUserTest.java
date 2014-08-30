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
package org.roklib.webapps.data.usermgmt;

import org.junit.Before;
import org.junit.Test;
import org.roklib.webapps.state.GenericUserState;

import static org.junit.Assert.*;

public class GenericUserTest {
    private GenericUser<Long, String> testObj;

    @Before
    public void setUp() {
        testObj = new GenericUser<Long, String>();
    }

    @Test
    public void testGetUserRoles() {
        assertTrue(testObj.getUserRoles().isEmpty());
        testObj.addRole(new UserRole<Long>(17L, "17"));
        assertEquals(1, testObj.getUserRoles().size());
    }

    @Test
    public void testAddRole() {
        UserRole<Long> role1 = new UserRole<Long>(17L, "17");
        UserRole<Long> role2 = new UserRole<Long>(23L, "23");
        testObj.addRole(role1);
        testObj.addRole(role1);
        testObj.addRole(role2);
        assertEquals(2, testObj.getUserRoles().size());
    }

    @Test
    public void testHasRole() {
        UserRole<Long> role1 = new UserRole<Long>(17L, "17");
        UserRole<Long> role2 = new UserRole<Long>(23L, "23");
        UserRole<Long> role3 = new UserRole<Long>(42L, "42");
        testObj.addRole(role1);
        testObj.addRole(role2);
        assertTrue(testObj.hasRole(role1));
        assertTrue(testObj.hasRole(role2));
        assertFalse(testObj.hasRole(role3));
    }

    @Test
    public void testSetState() {
        assertFalse(testObj.getState().hasState(GenericUserState.DEACTIVATED));
        testObj.setState(GenericUserState.DEACTIVATED);
        assertTrue(testObj.getState().hasState(GenericUserState.DEACTIVATED));
    }

    @Test(expected = IllegalStateException.class)
    public void testSetStateExternal_Fail() {
        testObj.getState().setStateValue(GenericUserState.DEACTIVATED);
    }
}
