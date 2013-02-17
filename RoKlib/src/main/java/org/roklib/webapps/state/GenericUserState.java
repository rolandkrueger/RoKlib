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
package org.roklib.webapps.state;

import org.roklib.state.State;

public class GenericUserState extends State<GenericUserState>
{
  private static final long                        serialVersionUID                  = 7603305865808291485L;

  private final static String                      sPrefix                           = GenericUserState.class
                                                                                         .getCanonicalName ();

  public final static StateValue<GenericUserState> UNKNOWN                           = new StateValue<GenericUserState> (
                                                                                         sPrefix + ".UNKNOWN");
  public final static StateValue<GenericUserState> REGISTERED                        = new StateValue<GenericUserState> (
                                                                                         sPrefix + ".REGISTERED");
  public final static StateValue<GenericUserState> REGISTRATION_CONFIRMATION_PENDING = new StateValue<GenericUserState> (
                                                                                         sPrefix
                                                                                             + ".REGISTRATION_CONFIRMATION_PENDING");
  public final static StateValue<GenericUserState> DEACTIVATED                       = new StateValue<GenericUserState> (
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
