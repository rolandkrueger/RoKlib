/*
 * $Id: Condition.java 178 2010-10-31 18:01:20Z roland $
 * Copyright (C) 2007 Roland Krueger
 * Created on 20.10.2009
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
package info.rolandkrueger.roklib.util.conditionalengine;

import info.rolandkrueger.roklib.util.helper.CheckForNull;

public class Condition extends AbstractCondition
{
  private static final long serialVersionUID = 2843942589476694624L;

  private boolean mBooleanValue;
  private String mName;

  public Condition (String name)
  {
    CheckForNull.check (name);
    mName = name;
    mBooleanValue = false;
  }

  public Condition (String name, boolean value)
  {
    this (name);
    mBooleanValue = value;
  }

  public boolean getBooleanValue ()
  {
    return mBooleanValue;
  }

  public void setValue (boolean value)
  {
    boolean previousValue = mBooleanValue;
    mBooleanValue = value;
    if (previousValue != value) fireConditionChanged ();
  }

  public String getName ()
  {
    return mName;
  }

  @Override
  public boolean equals (Object obj)
  {
    if (obj == null) return false;
    if (obj == this) return true;
    if (obj instanceof Condition)
    {
      Condition other = (Condition) obj;
      if (other.mName.equals (mName)) return true;
    }
    return false;
  }

  @Override
  public int hashCode ()
  {
    return mName.hashCode ();
  }

  @Override
  public String toString ()
  {
    return String.format ("%s=%b", mName, mBooleanValue);
  }
}
