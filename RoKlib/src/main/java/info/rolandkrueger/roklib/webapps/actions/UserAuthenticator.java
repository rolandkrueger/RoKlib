/*
 * $Id: UserAuthenticator.java 178 2010-10-31 18:01:20Z roland $
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
package info.rolandkrueger.roklib.webapps.actions;

import info.rolandkrueger.roklib.util.helper.CheckForNull;
import info.rolandkrueger.roklib.webapps.data.access.IUserDataAccess;
import info.rolandkrueger.roklib.webapps.data.usermgmt.GenericUser;
import info.rolandkrueger.roklib.webapps.state.GenericUserState;

import java.io.Serializable;

public class UserAuthenticator<KeyClass, UserData, U extends GenericUser<KeyClass, UserData>>
    implements Serializable
{
  private static final long serialVersionUID = 5402843962259459370L;

  public enum AuthenticationOutcome
  {
    USER_AUTHENTICATED, INCORRECT_PASSWORD, UNKNOWN_USER, USER_DEACTIVATED, CONFIRMATION_PENDING_FOR_USER
  };

  public class AuthenticationResult implements Serializable
  {
    private static final long serialVersionUID = - 2978021069510568345L;

    private AuthenticationOutcome mOutcome;
    private U mUser;

    public AuthenticationResult (U user, AuthenticationOutcome outcome)
    {
      CheckForNull.check (outcome);
      mOutcome = outcome;
      mUser = user;
    }

    public AuthenticationOutcome getOutcome ()
    {
      return mOutcome;
    }

    public U getUser ()
    {
      return mUser;
    }
  }

  private IUserDataAccess<KeyClass, UserData, U> mDataAccess;

  public UserAuthenticator (IUserDataAccess<KeyClass, UserData, U> dataAccess)
  {
    CheckForNull.check (dataAccess);
    mDataAccess = dataAccess;
  }

  public AuthenticationResult authenticate (String userLogin, String password)
  {
    CheckForNull.check (userLogin, password);
    U user = mDataAccess.getUserWithLogin (userLogin);
    if (user == null) return new AuthenticationResult (null, AuthenticationOutcome.UNKNOWN_USER);
    GenericUserState userState = user.getState ();
    assert userState != null;
    if (userState.hasState (GenericUserState.DEACTIVATED))
      return new AuthenticationResult (user, AuthenticationOutcome.USER_DEACTIVATED);
    if (userState.hasState (GenericUserState.REGISTRATION_CONFIRMATION_PENDING))
      return new AuthenticationResult (user, AuthenticationOutcome.CONFIRMATION_PENDING_FOR_USER);
    if (user.getPasswordHash () == null
        || ! user.getPasswordHash ().equals (
            user.getPasswordHashGenerator ().createPasswordHash (password)))
      return new AuthenticationResult (user, AuthenticationOutcome.INCORRECT_PASSWORD);

    user.getOnlineStatus ().setOnline (true);
    return new AuthenticationResult (user, AuthenticationOutcome.USER_AUTHENTICATED);
  }
}
