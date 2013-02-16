/*
 * $Id: HasState.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 09.02.2010
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
package info.rolandkrueger.roklib.util.conditionalengine.conditions;

import info.rolandkrueger.roklib.util.bool.IBooleanValueProvider;
import info.rolandkrueger.roklib.util.helper.CheckForNull;
import info.rolandkrueger.roklib.util.state.State;
import info.rolandkrueger.roklib.util.state.State.StateValue;

public class HasState<S extends State<?>> implements IBooleanValueProvider
{
  private static final long serialVersionUID = - 232946455411913695L;

  private State<S> mState;
  private StateValue<S> mStateValue;

  public HasState (State<S> state, StateValue<S> stateValue)
  {
    CheckForNull.check (state, stateValue);
    mState = state;
    mStateValue = stateValue;
  }

  public boolean getBooleanValue ()
  {
    return mState.hasState (mStateValue);
  }
}
