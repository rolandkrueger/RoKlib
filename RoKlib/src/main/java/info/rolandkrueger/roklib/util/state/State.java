/*
 * $Id: State.java 226 2011-01-05 21:02:51Z roland $
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
package info.rolandkrueger.roklib.util.state;

import info.rolandkrueger.roklib.util.helper.CheckForNull;

import java.io.Serializable;

public class State<S extends State<?>> implements Serializable
{
  private static final long serialVersionUID = 6243348683850423328L;

  private Serializable  mLockKey;
  private StateValue<S> mCurrentState;
  private StateValue<S> mDefaultState;

  public static class StateValue<S extends State<?>> implements Serializable
  {
    private static final long serialVersionUID = - 1916548888416932116L;
    private String mName;

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
      if (obj == null) return false;
      if (obj == this) return true;
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
    if (state == null && mCurrentState == null) return true;
    if (state == null && mCurrentState != null) return false;

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
    if (mLockKey == null) return;
    if (lockKey != mLockKey) throw new IllegalArgumentException ("Unlock failed: wrong key.");
    mLockKey = null;
  }

  public boolean isLocked ()
  {
    return mLockKey != null;
  }

  @Override
  public String toString ()
  {
    if (mCurrentState == null) return "null";
    return mCurrentState.mName;
  }

  @Override
  public int hashCode ()
  {
    if (mCurrentState == null) return - 1;
    return mCurrentState.hashCode ();
  }

  @SuppressWarnings ("unchecked")
  @Override
  public boolean equals (Object obj)
  {
    if (obj == null) return false;
    if (obj == this) return true;
    if (mCurrentState == null) return false;
    if (obj instanceof State)
    {
      State other = (State) obj;
      if (other.mCurrentState == null) return false;
      return other.mCurrentState.equals (mCurrentState);
    }

    return false;
  }
}
