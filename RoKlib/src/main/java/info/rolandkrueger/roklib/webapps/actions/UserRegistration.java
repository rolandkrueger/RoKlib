/*
 * Copyright (C) 2007 Roland Krueger
 * Created on 05.02.2010
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
import info.rolandkrueger.roklib.webapps.actions.interfaces.IUserRegistrationMethods;
import info.rolandkrueger.roklib.webapps.data.access.IUserDataAccess;
import info.rolandkrueger.roklib.webapps.data.usermgmt.GenericUser;
import info.rolandkrueger.roklib.webapps.data.usermgmt.UserRegistrationStatus;
import info.rolandkrueger.roklib.webapps.state.GenericUserState;

import java.io.Serializable;

public class UserRegistration<KeyClass, UserData, U extends GenericUser<KeyClass, UserData>> implements Serializable
{
  private static final long serialVersionUID = 7913299696050270835L;

  public enum RegistrationOutcome
  {
    USERNAME_ALREADY_REGISTERED, ERROR_DURING_REGISTRATION, OK
  };

  public enum RegistrationConfirmationOutcome
  {
    REGISTRATION_KEY_UNKNOWN, OK
  }

  private IUserDataAccess<KeyClass, UserData, U>          mDataAccess;
  private IUserRegistrationMethods<KeyClass, UserData, U> mRegistrationMethods;
  private int                                             mRegistrationKeyLength;

  public UserRegistration (IUserDataAccess<KeyClass, UserData, U> dataAccess,
      IUserRegistrationMethods<KeyClass, UserData, U> registrationMethods)
  {
    this (dataAccess, registrationMethods, 30);
  }

  public UserRegistration (IUserDataAccess<KeyClass, UserData, U> dataAccess,
      IUserRegistrationMethods<KeyClass, UserData, U> registrationMethods, int registrationKeyLength)
  {
    CheckForNull.check (dataAccess, registrationMethods);
    if (registrationKeyLength < 5)
      throw new IllegalArgumentException ("Registration key length is too small (< 5)");

    mDataAccess = dataAccess;
    mRegistrationMethods = registrationMethods;
    mRegistrationKeyLength = registrationKeyLength;
  }

  public RegistrationOutcome registerNewUser (U user)
  {
    return registerNewUser (user, true);
  }

  public RegistrationOutcome registerNewUser (U user, boolean requireConfirmation)
  {
    CheckForNull.check (user);
    if (mDataAccess.getUserWithLogin (user.getLoginName ()) != null)
      return RegistrationOutcome.USERNAME_ALREADY_REGISTERED;

    mDataAccess.startTransaction ();

    user.setRegistrationStatus (new UserRegistrationStatus (mRegistrationKeyLength));
    if (requireConfirmation)
    {
      user.setState (GenericUserState.REGISTRATION_CONFIRMATION_PENDING);
    } else
    {
      user.setState (GenericUserState.REGISTERED);
      user.getRegistrationStatus ().setRegistrationKey ("");
    }

    mDataAccess.persistUser (user);
    if (requireConfirmation && !mRegistrationMethods.sendRegistrationNotification (user))
    {
      mDataAccess.rollback ();
      user.setRegistrationStatus (null);
      return RegistrationOutcome.ERROR_DURING_REGISTRATION;
    }

    mDataAccess.commit ();
    return RegistrationOutcome.OK;
  }

  public RegistrationConfirmationResult completeRegistration (String registrationKey)
  {
    U user = mDataAccess.getUserForRegistrationKey (registrationKey);
    if (user == null)
      return new RegistrationConfirmationResult (RegistrationConfirmationOutcome.REGISTRATION_KEY_UNKNOWN, null);

    user.setState (GenericUserState.REGISTERED);
    user.getRegistrationStatus ().setRegistrationKey ("");

    mDataAccess.startTransaction ();
    mDataAccess.updateUser (user);
    mDataAccess.commit ();
    return new RegistrationConfirmationResult (RegistrationConfirmationOutcome.OK, user);
  }

  public class RegistrationConfirmationResult
  {
    private RegistrationConfirmationOutcome mOutcome;
    private U                               mUser;

    protected RegistrationConfirmationResult (RegistrationConfirmationOutcome outcome, U user)
    {
      mOutcome = outcome;
      mUser = user;
    }

    public RegistrationConfirmationOutcome getOutcome ()
    {
      return mOutcome;
    }

    public U getUser ()
    {
      return mUser;
    }
  }
}
