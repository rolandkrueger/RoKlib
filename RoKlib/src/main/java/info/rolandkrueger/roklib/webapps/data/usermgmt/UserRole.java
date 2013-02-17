/*
 * Copyright (C) 2007 Roland Krueger
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
package info.rolandkrueger.roklib.webapps.data.usermgmt;

import info.rolandkrueger.roklib.util.authorization.IAdmissionTicket;
import info.rolandkrueger.roklib.webapps.data.GenericPersistableObject;

public class UserRole<KeyClass> extends GenericPersistableObject<KeyClass> implements IAdmissionTicket
{
  private static final long serialVersionUID = 467091219897410434L;

  private String            mRoleName;
  private String            mDescription;

  public UserRole ()
  {
    super ();
  }

  public UserRole (KeyClass key)
  {
    this (key, null);
  }

  public UserRole (String roleName)
  {
    setName (roleName);
  }

  public UserRole (KeyClass key, String roleName)
  {
    this (roleName);
    setKey (key);
  }

  public void setName (String roleName)
  {
    mRoleName = roleName;
  }

  public String getDescription ()
  {
    return mDescription;
  }

  public void setDescription (String description)
  {
    mDescription = description;
  }

  @Override
  public String toString ()
  {
    return mRoleName;
  }

  public String getName ()
  {
    return mRoleName;
  }
}
