/*
 * $Id: UserRegistration.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 05.02.2010
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
import info.rolandkrueger.roklib.webapps.actions.interfaces.IUserRegistrationMethods;
import info.rolandkrueger.roklib.webapps.data.access.IUserDataAccess;
import info.rolandkrueger.roklib.webapps.data.usermgmt.GenericUser;
import info.rolandkrueger.roklib.webapps.data.usermgmt.UserRegistrationStatus;
import info.rolandkrueger.roklib.webapps.state.GenericUserState;

import java.io.Serializable;

public class UserRegistration<KeyClass, UserData, U extends GenericUser<KeyClass, UserData>>
    implements Serializable
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

  private IUserDataAccess<KeyClass, UserData, U> mDataAccess;
  private IUserRegistrationMethods<KeyClass, UserData, U> mRegistrationMethods;
  private int mRegistrationKeyLength;

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
    if (requireConfirmation && ! mRegistrationMethods.sendRegistrationNotification (user))
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
      return new RegistrationConfirmationResult (
          RegistrationConfirmationOutcome.REGISTRATION_KEY_UNKNOWN, null);

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
    private U mUser;

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
