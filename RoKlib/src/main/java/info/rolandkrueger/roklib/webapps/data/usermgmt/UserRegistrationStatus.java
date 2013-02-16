/*
 * $Id: UserRegistrationStatus.java 178 2010-10-31 18:01:20Z roland $
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
package info.rolandkrueger.roklib.webapps.data.usermgmt;

import info.rolandkrueger.roklib.util.RandomStringIDGenerator;
import info.rolandkrueger.roklib.util.helper.CheckForNull;

import java.io.Serializable;
import java.util.Date;

public class UserRegistrationStatus implements Serializable
{
  private static final long serialVersionUID = 325123813003034741L;

  private Date mRegisteredSince;
  private String mRegistrationKey;

  public UserRegistrationStatus ()
  {
  }

  public UserRegistrationStatus (Date registeredSince, String registrationKey)
  {
    CheckForNull.check (registeredSince, registrationKey);
    mRegisteredSince = registeredSince;
    mRegistrationKey = registrationKey;
  }

  public UserRegistrationStatus (int registrationKeyLength)
  {
    this (new Date (), RandomStringIDGenerator.getUniqueID (registrationKeyLength));
  }

  public String getRegistrationKey ()
  {
    return mRegistrationKey;
  }

  public void setRegistrationKey (String registrationKey)
  {
    mRegistrationKey = registrationKey;
  }

  public Date getRegisteredSince ()
  {
    return mRegisteredSince;
  }

  public void setRegisteredSince (Date registeredSince)
  {
    mRegisteredSince = registeredSince;
  }
}
