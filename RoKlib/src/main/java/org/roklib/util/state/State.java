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
package org.roklib.util.state;


import java.io.Serializable;

import org.roklib.util.helper.CheckForNull;

public class State<S extends State<?>> implements Serializable
{
  private static final long serialVersionUID = 6243348683850423328L;

  private Serializable      mLockKey;
  private StateValue<S>     mCurrentState;
  private StateValue<S>     mDefaultState;

  public static class StateValue<S extends State<?>> implements Serializable
  {
    private static final long serialVersionUID = -1916548888416932116L;
    private String            mName;

    public StateValue (String name)
    {
      mName = name;
    }

    public String getName ()
    {
      return mName;
    }

    @SuppressWarnings ({ "unchecked", "rawtypes" })
    @Override
    public boolean equals (Object obj)
    {
      if (obj == null)
        return false;
      if (obj == this)
        return true;
      if (obj instanceof StateValue)
      {
        StateValue other = (StateValue) obj;
        return other.mName.equals (mName);
      }
      return false;
    }

    @Override
    public int hashCode ()
    {
      return mName.hashCode ();
    }
  }

  public State ()
  {
  }

  public State (StateValue<S> defaultState)
  {
    mCurrentState = defaultState;
    mDefaultState = defaultState;
  }

  public boolean hasState (StateValue<S> state)
  {
    if (state == null && mCurrentState == null)
      return true;
    if (state == null && mCurrentState != null)
      return false;

    return mCurrentState.equals (state);
  }

  public void setStateValue (StateValue<S> state)
  {
    if (mLockKey != null)
      throw new IllegalStateException (
          "Cannot set status: object is locked. Use setState with the correct key instead.");
    mCurrentState = state;
  }

  public void setStateValue (StateValue<S> state, Object lockKey)
  {
    if (mLockKey != null && mLockKey != lockKey)
      throw new IllegalArgumentException ("Unlock failed: wrong key.");
    mCurrentState = state;
  }

  public StateValue<S> getStateValue ()
  {
    return mCurrentState;
  }

  public void reset ()
  {
    mCurrentState = mDefaultState;
  }

  public void lock (Serializable lockKey)
  {
    CheckForNull.check (lockKey);
    if (mLockKey != null && mLockKey != lockKey)
      throw new IllegalStateException ("Already locked. Cannot lock again with a different key.");
    mLockKey = lockKey;
  }

  public void unlock (Object lockKey)
  {
    if (mLockKey == null)
      return;
    if (lockKey != mLockKey)
      throw new IllegalArgumentException ("Unlock failed: wrong key.");
    mLockKey = null;
  }

  public boolean isLocked ()
  {
    return mLockKey != null;
  }

  @Override
  public String toString ()
  {
    if (mCurrentState == null)
      return "null";
    return mCurrentState.mName;
  }

  @Override
  public int hashCode ()
  {
    if (mCurrentState == null)
      return -1;
    return mCurrentState.hashCode ();
  }

  @SuppressWarnings ("unchecked")
  @Override
  public boolean equals (Object obj)
  {
    if (obj == null)
      return false;
    if (obj == this)
      return true;
    if (mCurrentState == null)
      return false;
    if (obj instanceof State)
    {
      State other = (State) obj;
      if (other.mCurrentState == null)
        return false;
      return other.mCurrentState.equals (mCurrentState);
    }

    return false;
  }
}
