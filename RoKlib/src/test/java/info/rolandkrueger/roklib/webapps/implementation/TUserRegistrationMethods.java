/*
 * $Id: TUserRegistrationMethods.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 08.03.2010
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

import info.rolandkrueger.roklib.webapps.actions.interfaces.IUserRegistrationMethods;

@SuppressWarnings ("serial")
public class TUserRegistrationMethods implements IUserRegistrationMethods<Long, String, TUser>
{
  public TUser notificationRecipient;
  public boolean registrationSent = false;
  private boolean sendRegistrationSuccessful = true;

  public boolean sendRegistrationNotification (TUser registeredUser)
  {
    if (sendRegistrationSuccessful) registrationSent = true;
    return sendRegistrationSuccessful;
  }

  public void setSendRegistrationSuccessful (boolean successful)
  {
    sendRegistrationSuccessful = successful;
  }
}
