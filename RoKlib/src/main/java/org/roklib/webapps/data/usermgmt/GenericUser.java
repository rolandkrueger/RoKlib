/*
 * Copyright (C) 2007 - 2010 Roland Krueger
 * Created on 27.01.2010
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
package org.roklib.webapps.data.usermgmt;


import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.roklib.state.State.StateValue;
import org.roklib.util.helper.CheckForNull;
import org.roklib.webapps.actions.DefaultPasswordMD5HashGenerator;
import org.roklib.webapps.actions.interfaces.IPasswordHashGenerator;
import org.roklib.webapps.authorization.AdmissionTicketContainer;
import org.roklib.webapps.data.GenericPersistableObject;
import org.roklib.webapps.state.GenericUserState;

public class GenericUser<KeyClass, UserData> extends GenericPersistableObject<KeyClass>
{
  private static final long        serialVersionUID = -8767507542975979387L;

  private final Serializable       STATE_LOCK       = new Serializable ()
                                                    {
                                                      private static final long serialVersionUID = 1098724371435032386L;
                                                    };

  private String                   mLoginName;
  private String                   mPasswordHash;
  protected UserData               mUserData;
  private Set<UserRole<KeyClass>>  mUserRoles;
  private AdmissionTicketContainer mRoleHandler;
  private GenericUserState         mState;
  private UserOnlineStatus         mOnlineStatus;
  private UserRegistrationStatus   mRegistrationStatus;
  private IPasswordHashGenerator   mPasswordHashGenerator;

  public GenericUser ()
  {
    mUserRoles = new HashSet<UserRole<KeyClass>> ();
    mState = new GenericUserState (GenericUserState.UNKNOWN);
    mState.lock (STATE_LOCK);
    mOnlineStatus = new UserOnlineStatus ();
    mPasswordHashGenerator = new DefaultPasswordMD5HashGenerator ();
    mRoleHandler = new AdmissionTicketContainer ();
  }

  public void setPasswordHashGenerator (IPasswordHashGenerator generator)
  {
    CheckForNull.check (generator);
    mPasswordHashGenerator = generator;
  }

  public IPasswordHashGenerator getPasswordHashGenerator ()
  {
    return mPasswordHashGenerator;
  }

  public GenericUser (UserData userData)
  {
    this ();
    mUserData = userData;
  }

  public Set<? extends UserRole<KeyClass>> getUserRoles ()
  {
    return mUserRoles;
  }

  protected void setUserRoles (Set<? extends UserRole<KeyClass>> userRoles)
  {
    CheckForNull.check (userRoles);
    mUserRoles.clear ();
    mUserRoles.addAll (userRoles);
    for (UserRole<KeyClass> role : userRoles)
    {
      mRoleHandler.addTicket (role);
    }
  }

  public <R extends UserRole<KeyClass>> void addRole (R newRole)
  {
    CheckForNull.check (newRole);
    mUserRoles.add (newRole);
    mRoleHandler.addTicket (newRole);
  }

  public <R extends UserRole<KeyClass>> void removeRole (R role)
  {
    mUserRoles.remove (role);
    mRoleHandler.removeTicket (role);
  }

  public <R extends UserRole<KeyClass>> boolean hasRole (R role)
  {
    return mRoleHandler.hasTicket (role);
  }

  public GenericUserState getState ()
  {
    return mState;
  }

  public boolean hasState (StateValue<GenericUserState> state)
  {
    return mState.hasState (state);
  }

  public void setState (StateValue<GenericUserState> newState)
  {
    mState.setStateValue (newState, STATE_LOCK);
  }

  public UserOnlineStatus getOnlineStatus ()
  {
    return mOnlineStatus;
  }

  public void setOnlineStatus (UserOnlineStatus onlineStatus)
  {
    CheckForNull.check (onlineStatus);
    mOnlineStatus = onlineStatus;
  }

  public UserRegistrationStatus getRegistrationStatus ()
  {
    return mRegistrationStatus;
  }

  public void setRegistrationStatus (UserRegistrationStatus registrationStatus)
  {
    mRegistrationStatus = registrationStatus;
  }

  public String getLoginName ()
  {
    return mLoginName;
  }

  public void setLoginName (String loginName)
  {
    mLoginName = loginName;
  }

  public String getPasswordHash ()
  {
    return mPasswordHash;
  }

  public void setPassword (String password)
  {
    setPasswordHash (mPasswordHashGenerator.createPasswordHash (password));
  }

  public void setPasswordHash (String passwordHash)
  {
    mPasswordHash = passwordHash;
  }

  public UserData getUserData ()
  {
    return mUserData;
  }

  public void setUserData (UserData userData)
  {
    mUserData = userData;
  }

  /**
   * @return
   * @see org.roklib.webapps.data.usermgmt.UserRegistrationStatus#getRegisteredSince()
   */
  public Date getRegisteredSince ()
  {
    if (mRegistrationStatus == null)
      return null;
    return mRegistrationStatus.getRegisteredSince ();
  }

  /**
   * @return
   * @see org.roklib.webapps.data.usermgmt.UserRegistrationStatus#getRegistrationKey()
   */
  public String getRegistrationKey ()
  {
    if (mRegistrationStatus == null)
      return null;
    return mRegistrationStatus.getRegistrationKey ();
  }

  /**
   * @param registeredSince
   * @see org.roklib.webapps.data.usermgmt.UserRegistrationStatus#setRegisteredSince(java.util.Date)
   */
  public void setRegisteredSince (Date registeredSince)
  {
    if (registeredSince == null)
      return;
    if (mRegistrationStatus == null)
      mRegistrationStatus = new UserRegistrationStatus ();
    mRegistrationStatus.setRegisteredSince (registeredSince);
  }

  /**
   * @param registrationKey
   * @see org.roklib.webapps.data.usermgmt.UserRegistrationStatus#setRegistrationKey(java.lang.String)
   */
  public void setRegistrationKey (String registrationKey)
  {
    if (registrationKey == null)
      return;
    if (mRegistrationStatus == null)
      mRegistrationStatus = new UserRegistrationStatus ();
    mRegistrationStatus.setRegistrationKey (registrationKey);
  }
}
