/*
 * $Id: GenericUserState.java 178 2010-10-31 18:01:20Z roland $
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
package info.rolandkrueger.roklib.webapps.state;

import info.rolandkrueger.roklib.util.state.State;

public class GenericUserState extends State<GenericUserState>
{
  private static final long serialVersionUID = 7603305865808291485L;

  private final static String sPrefix = GenericUserState.class.getCanonicalName ();

  public final static StateValue<GenericUserState> UNKNOWN = new StateValue<GenericUserState> (
      sPrefix + ".UNKNOWN");
  public final static StateValue<GenericUserState> REGISTERED = new StateValue<GenericUserState> (
      sPrefix + ".REGISTERED");
  public final static StateValue<GenericUserState> REGISTRATION_CONFIRMATION_PENDING = new StateValue<GenericUserState> (
      sPrefix + ".REGISTRATION_CONFIRMATION_PENDING");
  public final static StateValue<GenericUserState> DEACTIVATED = new StateValue<GenericUserState> (
      sPrefix + ".DEACTIVATED");

  public GenericUserState ()
  {
    super ();
  }

  public GenericUserState (StateValue<GenericUserState> defaultState)
  {
    super (defaultState);
  }

  public String getFullStateValueNameFor (String stateValue)
  {
    return sPrefix + "." + stateValue;
  }
}
