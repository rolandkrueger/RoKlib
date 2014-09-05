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
import org.roklib.webapps.testhelper.TUser;
import org.roklib.webapps.testhelper.TUserDataAccess;
import org.roklib.webapps.testhelper.TUserRegistrationMethods;
import org.roklib.webapps.state.GenericUserState;

import static org.junit.Assert.*;

public class UserRegistrationTest {
    private UserRegistration<Long, String, TUser> testObj;
    private TUserRegistrationMethods userRegistrationMethods;
    private TUserDataAccess userDataAccess;

    @Before
    public void setUp() {
        userDataAccess = new TUserDataAccess();
        userRegistrationMethods = new TUserRegistrationMethods();
        testObj = new UserRegistration<Long, String, TUser>(userDataAccess, userRegistrationMethods);
    }

    @Test
    public void testRegisterNewUser() {
        TUser user = new TUser("newuser", "password");
        RegistrationOutcome result = testObj.registerNewUser(user);
        assertEquals(RegistrationOutcome.OK, result);
        assertTrue(userRegistrationMethods.registrationSent);
        assertTrue(user == userDataAccess.persistedUser);
        assertTrue(userDataAccess.transactionStarted);
        assertTrue(userDataAccess.committed);
        assertNotNull(user.getRegistrationStatus().getRegistrationKey());
        assertTrue(user.getState().hasState(GenericUserState.REGISTRATION_CONFIRMATION_PENDING));
    }

    @Test
    public void testRegisterNewUser_ErrorDuringRegistration() {
        userRegistrationMethods.setSendRegistrationSuccessful(false);
        TUser user = new TUser("newuser", "password");
        RegistrationOutcome result = testObj.registerNewUser(user);
        assertEquals(RegistrationOutcome.ERROR_DURING_REGISTRATION, result);
        assertTrue(userDataAccess.rolledBack);
        assertNull(userDataAccess.persistedUser);
        assertNull(user.getRegistrationStatus());
    }

    @Test
    public void testRegisterNewUser_UserNameExists() {
        TUser user = new TUser("active", "password");
        RegistrationOutcome result = testObj.registerNewUser(user);
        assertEquals(RegistrationOutcome.USERNAME_ALREADY_REGISTERED, result);
        assertFalse(userRegistrationMethods.registrationSent);
        assertFalse(userDataAccess.transactionStarted);
        assertNull(userDataAccess.persistedUser);
        assertNull(user.getRegistrationStatus());
    }

    @Test
    public void testCompleteRegistration() {
        TUser user = new TUser("newuser", "password");
        testObj.registerNewUser(user);
        UserRegistration<Long, String, TUser>.RegistrationConfirmationResult result = testObj.completeRegistration(user
                .getRegistrationStatus().getRegistrationKey());
        assertEquals(RegistrationConfirmationOutcome.OK, result.getOutcome());
        assertTrue(user.getState().hasState(GenericUserState.REGISTERED));
        assertTrue(user == result.getUser());
        assertTrue(userDataAccess.transactionStarted);
        assertTrue(userDataAccess.committed);
    }

    @Test
    public void testCompleteRegistration_Fail() {
        UserRegistration<Long, String, TUser>.RegistrationConfirmationResult result = testObj
                .completeRegistration("unknown_key");
        assertEquals(RegistrationConfirmationOutcome.REGISTRATION_KEY_UNKNOWN, result.getOutcome());
        assertNull(result.getUser());
        assertFalse(userDataAccess.transactionStarted);
    }
}
