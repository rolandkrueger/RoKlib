/*
 * $Id: UserRole.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 27.01.2010
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
package info.rolandkrueger.roklib.webapps.data.usermgmt;

import info.rolandkrueger.roklib.util.authorization.IAdmissionTicket;
import info.rolandkrueger.roklib.webapps.data.GenericPersistableObject;

public class UserRole<KeyClass> extends GenericPersistableObject<KeyClass> implements
    IAdmissionTicket
{
  private static final long serialVersionUID = 467091219897410434L;

  private String mRoleName;
  private String mDescription;

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
