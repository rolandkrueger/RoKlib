/*
 * $Id: TUserDataAccess.java 178 2010-10-31 18:01:20Z roland $
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
package info.rolandkrueger.roklib.webapps.implementation;

import info.rolandkrueger.roklib.webapps.data.access.IUserDataAccess;
import info.rolandkrueger.roklib.webapps.state.GenericUserState;

import java.util.HashMap;
import java.util.Map;

public class TUserDataAccess implements IUserDataAccess<Long, String, TUser>
{
  private static final long serialVersionUID = 329146749772283339L;

  public final static String UNKNOWN_USER = "unknown";
  public final static String ACTIVE_USER = "active";
  public final static String DEACTIVATED_USER = "deactivated";
  public final static String OTHER_USER = "other";
  public final static String CONFIRMATION_PENDING_FOR_USER = "pending";

  private Map<String, TUser> mUserDatabase;
  public TUser persistedUser;
  public boolean transactionStarted = false;
  public boolean committed = false;
  public boolean rolledBack = false;

  public TUserDataAccess ()
  {
    mUserDatabase = new HashMap<String, TUser> ();
    TUser user = new TUser (ACTIVE_USER, ACTIVE_USER);
    user.setState (GenericUserState.REGISTERED);
    mUserDatabase.put (ACTIVE_USER, user);

    user = new TUser (DEACTIVATED_USER, DEACTIVATED_USER);
    user.setState (GenericUserState.DEACTIVATED);
    mUserDatabase.put (DEACTIVATED_USER, user);

    user = new TUser (OTHER_USER, OTHER_USER);
    user.setState (GenericUserState.REGISTERED);
    mUserDatabase.put (OTHER_USER, user);

    user = new TUser (CONFIRMATION_PENDING_FOR_USER, CONFIRMATION_PENDING_FOR_USER);
    user.setState (GenericUserState.REGISTRATION_CONFIRMATION_PENDING);
    mUserDatabase.put (CONFIRMATION_PENDING_FOR_USER, user);
  }

  public TUser getUserWithLogin (String username)
  {
    return mUserDatabase.get (username);
  }

  public void persistUser (TUser user)
  {
    persistedUser = user;
    mUserDatabase.put (user.getLoginName (), user);
  }

  public TUser getUserForRegistrationKey (String registrationKey)
  {
    for (TUser user : mUserDatabase.values ())
    {
      if (user.getRegistrationStatus () != null
          && user.getRegistrationStatus ().getRegistrationKey ().equals (registrationKey))
        return user;
    }
    return null;
  }

  public void commit ()
  {
    committed = true;
  }

  public void rollback ()
  {
    persistedUser = null;
    rolledBack = true;
  }

  public void startTransaction ()
  {
    transactionStarted = true;
    committed = false;
    rolledBack = false;
  }

  public void updateUser (TUser user)
  {
    persistUser (user);
  }
}
