/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 08.03.2010
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
import org.roklib.webapps.actions.UserRegistration.RegistrationConfirmationOutcome;
import org.roklib.webapps.actions.UserRegistration.RegistrationOutcome;
import org.roklib.webapps.implementation.TUser;
import org.roklib.webapps.implementation.TUserDataAccess;
import org.roklib.webapps.implementation.TUserRegistrationMethods;
import org.roklib.webapps.state.GenericUserState;

import static org.junit.Assert.*;

public class UserRegistrationTest {
    private UserRegistration<Long, String, TUser> mTestObj;
    private TUserRegistrationMethods mUserRegistrationMethods;
    private TUserDataAccess mUserDataAccess;

    @Before
    public void setUp() {
        mUserDataAccess = new TUserDataAccess();
        mUserRegistrationMethods = new TUserRegistrationMethods();
        mTestObj = new UserRegistration<Long, String, TUser>(mUserDataAccess, mUserRegistrationMethods);
    }

    @Test
    public void testRegisterNewUser() {
        TUser user = new TUser("newuser", "password");
        RegistrationOutcome result = mTestObj.registerNewUser(user);
        assertEquals(RegistrationOutcome.OK, result);
        assertTrue(mUserRegistrationMethods.registrationSent);
        assertTrue(user == mUserDataAccess.persistedUser);
        assertTrue(mUserDataAccess.transactionStarted);
        assertTrue(mUserDataAccess.committed);
        assertNotNull(user.getRegistrationStatus().getRegistrationKey());
        assertTrue(user.getState().hasState(GenericUserState.REGISTRATION_CONFIRMATION_PENDING));
    }

    @Test
    public void testRegisterNewUser_ErrorDuringRegistration() {
        mUserRegistrationMethods.setSendRegistrationSuccessful(false);
        TUser user = new TUser("newuser", "password");
        RegistrationOutcome result = mTestObj.registerNewUser(user);
        assertEquals(RegistrationOutcome.ERROR_DURING_REGISTRATION, result);
        assertTrue(mUserDataAccess.rolledBack);
        assertNull(mUserDataAccess.persistedUser);
        assertNull(user.getRegistrationStatus());
    }

    @Test
    public void testRegisterNewUser_UserNameExists() {
        TUser user = new TUser("active", "password");
        RegistrationOutcome result = mTestObj.registerNewUser(user);
        assertEquals(RegistrationOutcome.USERNAME_ALREADY_REGISTERED, result);
        assertFalse(mUserRegistrationMethods.registrationSent);
        assertFalse(mUserDataAccess.transactionStarted);
        assertNull(mUserDataAccess.persistedUser);
        assertNull(user.getRegistrationStatus());
    }

    @Test
    public void testCompleteRegistration() {
        TUser user = new TUser("newuser", "password");
        mTestObj.registerNewUser(user);
        UserRegistration<Long, String, TUser>.RegistrationConfirmationResult result = mTestObj.completeRegistration(user
                .getRegistrationStatus().getRegistrationKey());
        assertEquals(RegistrationConfirmationOutcome.OK, result.getOutcome());
        assertTrue(user.getState().hasState(GenericUserState.REGISTERED));
        assertTrue(user == result.getUser());
        assertTrue(mUserDataAccess.transactionStarted);
        assertTrue(mUserDataAccess.committed);
    }

    @Test
    public void testCompleteRegistration_Fail() {
        UserRegistration<Long, String, TUser>.RegistrationConfirmationResult result = mTestObj
                .completeRegistration("unknown_key");
        assertEquals(RegistrationConfirmationOutcome.REGISTRATION_KEY_UNKNOWN, result.getOutcome());
        assertNull(result.getUser());
        assertFalse(mUserDataAccess.transactionStarted);
    }
}
