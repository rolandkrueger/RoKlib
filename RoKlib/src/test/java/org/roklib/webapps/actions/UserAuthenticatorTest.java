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
package org.roklib.webapps.actions;

import org.junit.Before;
import org.junit.Test;
import org.roklib.webapps.actions.UserAuthenticator.AuthenticationOutcome;
import org.roklib.webapps.implementation.TUser;
import org.roklib.webapps.implementation.TUserDataAccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserAuthenticatorTest {
    private UserAuthenticator<Long, String, TUser> mTestObj;

    @Before
    public void setUp() {
        mTestObj = new UserAuthenticator<Long, String, TUser>(new TUserDataAccess());
    }

    @Test
    public void testSuccessfulAuthentication() {
        UserAuthenticator<Long, String, TUser>.AuthenticationResult result = mTestObj.authenticate(
                TUserDataAccess.ACTIVE_USER, TUserDataAccess.ACTIVE_USER);
        assertEquals(AuthenticationOutcome.USER_AUTHENTICATED, result.getOutcome());
        assertEquals(TUserDataAccess.ACTIVE_USER, result.getUser().getLoginName());
    }

    @Test
    public void testIncorrectPassword() {
        UserAuthenticator<Long, String, TUser>.AuthenticationResult result = mTestObj.authenticate(
                TUserDataAccess.ACTIVE_USER, "inc0rrect");
        assertEquals(AuthenticationOutcome.INCORRECT_PASSWORD, result.getOutcome());
        assertEquals(TUserDataAccess.ACTIVE_USER, result.getUser().getLoginName());
    }

    @Test
    public void testUnknownUser() {
        UserAuthenticator<Long, String, TUser>.AuthenticationResult result = mTestObj.authenticate(
                TUserDataAccess.UNKNOWN_USER, "somePassword");
        assertEquals(AuthenticationOutcome.UNKNOWN_USER, result.getOutcome());
        assertNull(result.getUser());
    }

    @Test
    public void testUserWithConfirmationPending() {
        UserAuthenticator<Long, String, TUser>.AuthenticationResult result = mTestObj.authenticate(
                TUserDataAccess.CONFIRMATION_PENDING_FOR_USER, TUserDataAccess.CONFIRMATION_PENDING_FOR_USER);
        assertEquals(AuthenticationOutcome.CONFIRMATION_PENDING_FOR_USER, result.getOutcome());
        assertEquals(TUserDataAccess.CONFIRMATION_PENDING_FOR_USER, result.getUser().getLoginName());
    }

    @Test
    public void testDeactivatedUser() {
        UserAuthenticator<Long, String, TUser>.AuthenticationResult result = mTestObj.authenticate(
                TUserDataAccess.DEACTIVATED_USER, TUserDataAccess.DEACTIVATED_USER);
        assertEquals(AuthenticationOutcome.USER_DEACTIVATED, result.getOutcome());
        assertEquals(TUserDataAccess.DEACTIVATED_USER, result.getUser().getLoginName());
    }
}
