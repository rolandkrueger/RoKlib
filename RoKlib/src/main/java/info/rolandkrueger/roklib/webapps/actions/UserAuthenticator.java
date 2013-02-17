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
package info.rolandkrueger.roklib.webapps.actions;

import info.rolandkrueger.roklib.util.helper.CheckForNull;
import info.rolandkrueger.roklib.webapps.data.access.IUserDataAccess;
import info.rolandkrueger.roklib.webapps.data.usermgmt.GenericUser;
import info.rolandkrueger.roklib.webapps.state.GenericUserState;

import java.io.Serializable;

public class UserAuthenticator<KeyClass, UserData, U extends GenericUser<KeyClass, UserData>> implements Serializable
{
  private static final long serialVersionUID = 5402843962259459370L;

  public enum AuthenticationOutcome
  {
    USER_AUTHENTICATED, INCORRECT_PASSWORD, UNKNOWN_USER, USER_DEACTIVATED, CONFIRMATION_PENDING_FOR_USER
  };

  public class AuthenticationResult implements Serializable
  {
    private static final long     serialVersionUID = -2978021069510568345L;

    private AuthenticationOutcome mOutcome;
    private U                     mUser;

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
    if (user == null)
      return new AuthenticationResult (null, AuthenticationOutcome.UNKNOWN_USER);
    GenericUserState userState = user.getState ();
    assert userState != null;
    if (userState.hasState (GenericUserState.DEACTIVATED))
      return new AuthenticationResult (user, AuthenticationOutcome.USER_DEACTIVATED);
    if (userState.hasState (GenericUserState.REGISTRATION_CONFIRMATION_PENDING))
      return new AuthenticationResult (user, AuthenticationOutcome.CONFIRMATION_PENDING_FOR_USER);
    if (user.getPasswordHash () == null
        || !user.getPasswordHash ().equals (user.getPasswordHashGenerator ().createPasswordHash (password)))
      return new AuthenticationResult (user, AuthenticationOutcome.INCORRECT_PASSWORD);

    user.getOnlineStatus ().setOnline (true);
    return new AuthenticationResult (user, AuthenticationOutcome.USER_AUTHENTICATED);
  }
}
