/*
 * $Id: UserAuthenticatorTest.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 29.01.2010
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
import static org.junit.Assert.assertNull;
import info.rolandkrueger.roklib.webapps.actions.UserAuthenticator;
import info.rolandkrueger.roklib.webapps.actions.UserAuthenticator.AuthenticationOutcome;
import info.rolandkrueger.roklib.webapps.implementation.TUser;
import info.rolandkrueger.roklib.webapps.implementation.TUserDataAccess;

import org.junit.Before;
import org.junit.Test;

public class UserAuthenticatorTest
{
  private UserAuthenticator<Long, String, TUser> mTestObj;

  @Before
  public void setUp ()
  {
    mTestObj = new UserAuthenticator<Long, String, TUser> (new TUserDataAccess ());
  }

  @Test
  public void testSuccessfulAuthentication ()
  {
    UserAuthenticator<Long, String, TUser>.AuthenticationResult result = mTestObj.authenticate (
        TUserDataAccess.ACTIVE_USER, TUserDataAccess.ACTIVE_USER);
    assertEquals (AuthenticationOutcome.USER_AUTHENTICATED, result.getOutcome ());
    assertEquals (TUserDataAccess.ACTIVE_USER, result.getUser ().getLoginName ());
  }

  @Test
  public void testIncorrectPassword ()
  {
    UserAuthenticator<Long, String, TUser>.AuthenticationResult result = mTestObj.authenticate (
        TUserDataAccess.ACTIVE_USER, "inc0rrect");
    assertEquals (AuthenticationOutcome.INCORRECT_PASSWORD, result.getOutcome ());
    assertEquals (TUserDataAccess.ACTIVE_USER, result.getUser ().getLoginName ());
  }

  @Test
  public void testUnknownUser ()
  {
    UserAuthenticator<Long, String, TUser>.AuthenticationResult result = mTestObj.authenticate (
        TUserDataAccess.UNKNOWN_USER, "somePassword");
    assertEquals (AuthenticationOutcome.UNKNOWN_USER, result.getOutcome ());
    assertNull (result.getUser ());
  }

  @Test
  public void testUserWithConfirmationPending ()
  {
    UserAuthenticator<Long, String, TUser>.AuthenticationResult result = mTestObj.authenticate (
        TUserDataAccess.CONFIRMATION_PENDING_FOR_USER,
        TUserDataAccess.CONFIRMATION_PENDING_FOR_USER);
    assertEquals (AuthenticationOutcome.CONFIRMATION_PENDING_FOR_USER, result.getOutcome ());
    assertEquals (TUserDataAccess.CONFIRMATION_PENDING_FOR_USER, result.getUser ().getLoginName ());
  }

  @Test
  public void testDeactivatedUser ()
  {
    UserAuthenticator<Long, String, TUser>.AuthenticationResult result = mTestObj.authenticate (
        TUserDataAccess.DEACTIVATED_USER, TUserDataAccess.DEACTIVATED_USER);
    assertEquals (AuthenticationOutcome.USER_DEACTIVATED, result.getOutcome ());
    assertEquals (TUserDataAccess.DEACTIVATED_USER, result.getUser ().getLoginName ());
  }
}
