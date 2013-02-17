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
package org.roklib.webapps.implementation;


import java.util.HashMap;
import java.util.Map;

import org.roklib.webapps.data.access.IUserDataAccess;
import org.roklib.webapps.state.GenericUserState;

public class TUserDataAccess implements IUserDataAccess<Long, String, TUser>
{
  private static final long  serialVersionUID              = 329146749772283339L;

  public final static String UNKNOWN_USER                  = "unknown";
  public final static String ACTIVE_USER                   = "active";
  public final static String DEACTIVATED_USER              = "deactivated";
  public final static String OTHER_USER                    = "other";
  public final static String CONFIRMATION_PENDING_FOR_USER = "pending";

  private Map<String, TUser> mUserDatabase;
  public TUser               persistedUser;
  public boolean             transactionStarted            = false;
  public boolean             committed                     = false;
  public boolean             rolledBack                    = false;

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
