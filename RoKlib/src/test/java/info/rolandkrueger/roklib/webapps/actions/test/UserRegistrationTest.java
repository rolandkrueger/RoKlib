/*
 * $Id: UserRegistrationTest.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 08.03.2010
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
package info.rolandkrueger.roklib.webapps.actions.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import info.rolandkrueger.roklib.webapps.actions.UserRegistration;
import info.rolandkrueger.roklib.webapps.actions.UserRegistration.RegistrationConfirmationOutcome;
import info.rolandkrueger.roklib.webapps.actions.UserRegistration.RegistrationOutcome;
import info.rolandkrueger.roklib.webapps.implementation.TUser;
import info.rolandkrueger.roklib.webapps.implementation.TUserDataAccess;
import info.rolandkrueger.roklib.webapps.implementation.TUserRegistrationMethods;
import info.rolandkrueger.roklib.webapps.state.GenericUserState;

import org.junit.Before;
import org.junit.Test;

public class UserRegistrationTest
{
  private UserRegistration<Long, String, TUser> mTestObj;
  private TUserRegistrationMethods mUserRegistrationMethods;
  private TUserDataAccess mUserDataAccess;

  @Before
  public void setUp ()
  {
    mUserDataAccess = new TUserDataAccess ();
    mUserRegistrationMethods = new TUserRegistrationMethods ();
    mTestObj = new UserRegistration<Long, String, TUser> (mUserDataAccess, mUserRegistrationMethods);
  }

  @Test
  public void testRegisterNewUser ()
  {
    TUser user = new TUser ("newuser", "password");
    RegistrationOutcome result = mTestObj.registerNewUser (user);
    assertEquals (RegistrationOutcome.OK, result);
    assertTrue (mUserRegistrationMethods.registrationSent);
    assertTrue (user == mUserDataAccess.persistedUser);
    assertTrue (mUserDataAccess.transactionStarted);
    assertTrue (mUserDataAccess.committed);
    assertNotNull (user.getRegistrationStatus ().getRegistrationKey ());
    assertTrue (user.getState ().hasState (GenericUserState.REGISTRATION_CONFIRMATION_PENDING));
  }

  @Test
  public void testRegisterNewUser_ErrorDuringRegistration ()
  {
    mUserRegistrationMethods.setSendRegistrationSuccessful (false);
    TUser user = new TUser ("newuser", "password");
    RegistrationOutcome result = mTestObj.registerNewUser (user);
    assertEquals (RegistrationOutcome.ERROR_DURING_REGISTRATION, result);
    assertTrue (mUserDataAccess.rolledBack);
    assertNull (mUserDataAccess.persistedUser);
    assertNull (user.getRegistrationStatus ());
  }

  @Test
  public void testRegisterNewUser_UserNameExists ()
  {
    TUser user = new TUser ("active", "password");
    RegistrationOutcome result = mTestObj.registerNewUser (user);
    assertEquals (RegistrationOutcome.USERNAME_ALREADY_REGISTERED, result);
    assertFalse (mUserRegistrationMethods.registrationSent);
    assertFalse (mUserDataAccess.transactionStarted);
    assertNull (mUserDataAccess.persistedUser);
    assertNull (user.getRegistrationStatus ());
  }

  @Test
  public void testCompleteRegistration ()
  {
    TUser user = new TUser ("newuser", "password");
    mTestObj.registerNewUser (user);
    UserRegistration<Long, String, TUser>.RegistrationConfirmationResult result = mTestObj
        .completeRegistration (user.getRegistrationStatus ().getRegistrationKey ());
    assertEquals (RegistrationConfirmationOutcome.OK, result.getOutcome ());
    assertTrue (user.getState ().hasState (GenericUserState.REGISTERED));
    assertTrue (user == result.getUser ());
    assertTrue (mUserDataAccess.transactionStarted);
    assertTrue (mUserDataAccess.committed);
  }

  @Test
  public void testCompleteRegistration_Fail ()
  {
    UserRegistration<Long, String, TUser>.RegistrationConfirmationResult result = mTestObj
        .completeRegistration ("unknown_key");
    assertEquals (RegistrationConfirmationOutcome.REGISTRATION_KEY_UNKNOWN, result.getOutcome ());
    assertNull (result.getUser ());
    assertFalse (mUserDataAccess.transactionStarted);
  }
}
